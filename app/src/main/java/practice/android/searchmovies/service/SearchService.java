package practice.android.searchmovies.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;

import practice.android.searchmovies.callback.FetchMoviesCallback;
import practice.android.searchmovies.model.MoviesLab;

import static practice.android.searchmovies.FindMoviesActivity.SEARCH_INPUT;
import static practice.android.searchmovies.manager.ScreenAppearanceManager.SHARED_PREFERENCE;

/**
 * Created by Stella on 7/15/17.
 */

public class SearchService extends Service implements FetchMoviesCallback {
    public static final String TAG = "SearchService";
    public static final String SUCCESS = "movies are fetched";
    public static final String DATA = "movies data";
    public static final String JSON = "fetched movie data";

    static Context mContext;
    FetchMoviesTask mFetchMoviesTask;

    public static Intent newIntent(Context context) {
        mContext = context;
        return new Intent(mContext, SearchService.class);
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        mFetchMoviesTask = new FetchMoviesTask(mContext,this);
        assert intent != null;
        String input = intent.getStringExtra(SEARCH_INPUT);
        mFetchMoviesTask.execute(input);
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onFetchTaskSuccess(String json) {
        Intent intent = new Intent(SearchService.SUCCESS);
        MoviesLab moviesLab = new MoviesLab();
        moviesLab.initMovies(json);
        intent.putParcelableArrayListExtra(DATA, moviesLab.getMovies());
        SharedPreferences prefs = mContext.getSharedPreferences(SHARED_PREFERENCE, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(JSON, json);
        editor.apply();
        sendBroadcast(intent);
    }

    @Override
    public void onFetchTaskFailed() {

    }

    @Override
    public boolean stopService(Intent name) {
        if (mFetchMoviesTask != null) {
            mFetchMoviesTask.cancel(true);
        }
        return super.stopService(name);
    }

}
