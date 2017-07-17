package practice.android.searchmovies;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import practice.android.searchmovies.manager.ScreenAppearanceManager;
import practice.android.searchmovies.model.Movie;

import static practice.android.searchmovies.FindMoviesActivity.IS_LARGE;
import static practice.android.searchmovies.ImageUtility.DEFAULT_POS;
import static practice.android.searchmovies.ImageUtility.TYPE;
import static practice.android.searchmovies.adapter.MoviesListAdapter.IMAGE_URL_PREFIX;
import static practice.android.searchmovies.adapter.MoviesListAdapter.MOVIE_EXTRA;
import static practice.android.searchmovies.manager.ScreenAppearanceManager.SHARED_PREFERENCE;

/**
 * Created by Stella on 7/9/17.
 */

public class MovieInfoActivity extends AppCompatActivity {
    public static final String TAG = "MovieInfoActivity";
    public static final String CITY = "current city";

    boolean isLarge;
    ShareActionProvider mShareActionProvider;
    TextView mOverview;
    ImageView mPoster;
    Movie mMovie;
    ImageUtility mUtility;
    String path;
    Button mSaveButton;
    BitmapDrawable mBitmapDrawable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        SharedPreferences prefs = getSharedPreferences(SHARED_PREFERENCE, MODE_PRIVATE);
        isLarge = prefs.getBoolean(IS_LARGE, false);
        mMovie = getIntent().getParcelableExtra(MOVIE_EXTRA);
        mOverview = (TextView) findViewById(R.id.over_view_info_page);
        mOverview.setText(mMovie.getOverview());
        mPoster = (ImageView) findViewById(R.id.poster_info_page);
        ScreenAppearanceManager manager = new ScreenAppearanceManager(this);
        mUtility = new ImageUtility(this);
        path = getFilesDir() + TYPE + DEFAULT_POS;
        if (isLarge) {
            int width = (int) (manager.getScreenWidth() * 0.8);
            int length = (int) (width * 1.5);
//            File imageFile = new File(path);
//            Picasso.with(this).load(imageFile).resize(width, length).centerCrop().into(mPoster);
            String url = IMAGE_URL_PREFIX + mMovie.getPosterPath();
            Picasso.with(this).load(url).centerCrop().resize(width, length).into(mPoster, new Callback() {
                @Override
                public void onSuccess() {
                    mSaveButton = (Button) findViewById(R.id.btn_save);
                    mBitmapDrawable = (BitmapDrawable) mPoster.getDrawable();
                    if (mBitmapDrawable == null) {
                        mSaveButton.setVisibility(View.INVISIBLE);
                    } else {
                        mSaveButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Bitmap bitmap = mBitmapDrawable.getBitmap();
                                mUtility.saveImageToSDCard(bitmap, mMovie.getTitle());
                                Toast.makeText(getApplicationContext(), "Image Saved", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }

                @Override
                public void onError() {

                }
            });
        } else {
            mPoster.setImageBitmap(mUtility.getImage(path));
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_share, menu);
        MenuItem item = menu.findItem(R.id.menu_item_share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        mShareActionProvider.setOnShareTargetSelectedListener(new ShareActionProvider.OnShareTargetSelectedListener() {
            @Override
            public boolean onShareTargetSelected(ShareActionProvider source, Intent intent) {
                sendNotification(mOverview.getText().toString());
                return false;
            }
        });
        setShareIntent();
        return true;
    }

    private void setShareIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, mOverview.getText());
        shareIntent.setType("text/plain");
        //to trigger the intent picker
        //startActivity(Intent.createChooser(shareIntent, "send"));
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }

    public void sendNotification(String messageBody) {
        Intent intent = new Intent(this, FindMoviesActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent
                .FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setContentTitle(getString(R.string.notification_title))
                .setContentText(messageBody)
                .setSmallIcon(android.R.drawable.ic_notification_overlay)
                .setLargeIcon(mUtility.getImage(path))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(0, notificationBuilder.build());
    }
}
