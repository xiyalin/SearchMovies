package practice.android.searchmovies;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

import static practice.android.searchmovies.MovieInfoActivity.CITY;


/**
 * Created by Yee on 7/12/17.
 */

public class MapPane extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks {
    public static final float DEFAULT_ZOOM = 9.5f;
    public static final String TAG = "MapPane";
    public static final int REQUEST_CODE_PERMISSION_LOCATION = 0;

    GoogleMap mMap;
    String mCity;
    GoogleApiClient mClient;
    SupportMapFragment mFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_pane);
        mClient = new GoogleApiClient.Builder(this).addApi(LocationServices.API).addConnectionCallbacks(this).build();
        mFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mCity = getIntent().getStringExtra(CITY);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager
                .PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission
                .ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this, new String[]{Manifest
                    .permission.ACCESS_FINE_LOCATION, Manifest
                    .permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE_PERMISSION_LOCATION);
            return;
        }
        Location currentLocation = LocationServices.FusedLocationApi.getLastLocation(mClient);
        mMap = googleMap;
        Geocoder gc = new Geocoder(this);
        try {
            double latitude = currentLocation.getLatitude();
            double longitude = currentLocation.getLongitude();
            List<Address> addresses = gc.getFromLocation(latitude, longitude, 1);
            Address address = addresses.get(0);
            LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            MarkerOptions options = new MarkerOptions().title(address
                    .getLocality())
                    .position(latLng)
                    .icon(BitmapDescriptorFactory.defaultMarker());
            mMap.addMarker(options);
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM);
            mMap.animateCamera(update);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mClient.disconnect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mFragment.getMapAsync(this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}
