package practice.android.searchmovies;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

import practice.android.searchmovies.manager.ScreenAppearanceManager;
import practice.android.searchmovies.model.Movie;
import practice.android.searchmovies.service.SearchService;

import static android.view.View.VISIBLE;
import static android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH;
import static practice.android.searchmovies.manager.ScreenAppearanceManager.SHARED_PREFERENCE;

/**
 * Created by Stella on 6/8/17.
 */

public class FindMoviesActivity extends AppCompatActivity {
    public static final String TAG = "FindMoviesActivity";
    public static final String SEARCH_INPUT = "input in the edit text";
    public static final String SEARCH_FROM = "search starting index";
    public static final int WRITE_PERMISSION = 0;
    public static final String IS_LARGE = "image settings";
    public static final String MOVIES_DATA = "saved movies data";
    public static final String IS_EDIT_VISIBLE = "is edit text visible";

    BroadcastReceiver mReceiver;
    EditText mEditSearch;
    ActionBar mActionBar;
    ArrayList<Movie> mMovies;
    ProgressDialog mDialog;
    SharedPreferences mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kata);
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission
//                .WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            if (Build.VERSION.SDK_INT >= 23) { // Marshmallow
//                ActivityCompat.requestPermissions(this, new String[]{Manifest
//                        .permission.WRITE_EXTERNAL_STORAGE}, WRITE_PERMISSION);
//            }
//        }
        mPrefs = getSharedPreferences(SHARED_PREFERENCE, MODE_PRIVATE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.action_bar_title);
        toolbar.setSubtitle(R.string.action_bar_subtitle);
        setSupportActionBar(toolbar);
        ScreenAppearanceManager appearanceManager = new ScreenAppearanceManager(this);
        if (appearanceManager.getScreenLength() == -1) {
            appearanceManager.setScreenPreference(getWindowManager().getDefaultDisplay());
        }
        //mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mMovies = intent.getParcelableArrayListExtra(SearchService.DATA);
                createFragment();
                mDialog.dismiss();
            }
        };
        mEditSearch = (EditText) findViewById(R.id.edit_movie_search);
        mEditSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == IME_ACTION_SEARCH) {
                    //mProgressBar.setVisibility(VISIBLE);
                    String input = mEditSearch.getText().toString();
                    startFetchService(input);
                    hideKeyboard();
                }
                return false;
            }
        });
        if (savedInstanceState != null) {
            mMovies = savedInstanceState.getParcelableArrayList(MOVIES_DATA);
            boolean isVisible = savedInstanceState.getBoolean(IS_EDIT_VISIBLE);
            if (isVisible) {
                hideActionBarTitle();
                mEditSearch.setVisibility(VISIBLE);
                String input = savedInstanceState.getString(SEARCH_INPUT);
                mEditSearch.setText(input);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_kata, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View view = LayoutInflater.from(this).inflate(R.layout.selector_settings, null);
            final RadioGroup settingsGroup = (RadioGroup) view.findViewById(R.id.image_settings);
            builder.setView(view);
            builder.setCancelable(true);
            builder.setPositiveButton(R.string.settings_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    int id = settingsGroup.getCheckedRadioButtonId();
                    SharedPreferences.Editor editor = mPrefs.edit();
                    if (id == R.id.large_label) {
                        editor.putBoolean(IS_LARGE, true);
                    } else if (id == R.id.small_label) {
                        editor.putBoolean(IS_LARGE, false);
                    }
                    editor.apply();
                }
            });
            builder.setNegativeButton(R.string.settings_cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.show();
        }

        if (id == R.id.action_map) {
            Intent intent = new Intent(FindMoviesActivity.this, MapPane.class);
            startActivity(intent);
        }

        if (id == R.id.action_search) {
            hideActionBarTitle();
            mEditSearch.setVisibility(VISIBLE);
            String input = mEditSearch.getText().toString();
            Log.d(TAG, input);
            if (!input.isEmpty()) {
                startFetchService(input);
                hideKeyboard();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter(SearchService.SUCCESS);
        registerReceiver(mReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }

    private void createFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        SearchFragment fragment = (SearchFragment) fragmentManager.findFragmentById(R.id.fragment_container);
        if (fragment != null) {
            fragmentManager.beginTransaction().remove(fragment).commit();
        }
        fragment = SearchFragment.newInstance(mMovies);
        fragmentManager.beginTransaction().add(R.id.fragment_container, fragment).commit();
    }

    private void hideActionBarTitle() {
        mActionBar = getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setDisplayShowTitleEnabled(false);
        }
    }

    private void hideKeyboard() {
        InputMethodManager manager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void startFetchService(String input) {
        showProgressDialog();
        Intent intent = SearchService.newIntent(getApplicationContext());
        intent.putExtra(SEARCH_INPUT, input);
        startService(intent);
    }

    private void showProgressDialog() {
        mDialog = new ProgressDialog(this);
        mDialog.setTitle(R.string.progress_dialog_title);
        mDialog.setCancelable(false);
        mDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.progress_dialog_cancel), new
                DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = SearchService.newIntent(FindMoviesActivity.this);
                        stopService(intent);
                        dialog.dismiss();
                    }
                });
        mDialog.show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(MOVIES_DATA, mMovies);
        boolean visible = mEditSearch.getVisibility() == VISIBLE;
        outState.putBoolean(IS_EDIT_VISIBLE, visible);
        String input = mEditSearch != null ? mEditSearch.getText().toString() : "";
        outState.putString(SEARCH_INPUT, input);

        Log.d("edu.deanza.cis53.exam", "Calculation error");
    }
}
