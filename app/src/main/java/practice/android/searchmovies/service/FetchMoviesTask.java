package practice.android.searchmovies.service;

import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Locale;

import practice.android.searchmovies.callback.FetchMoviesCallback;

/**
 * Created by Stella on 6/8/17.
 */

public class FetchMoviesTask extends AsyncTask<String, Void, String> {
    public static final String TAG = "FetchMoviesTask";
    public static final String API_KEY = "90e28f2839c7611f0641c8646f7353d3";
    public static final String URL_FORMAT = "https://api.themoviedb" +
            ".org/3/search/movie?api_key=%s&language=en-US&query=%s&page=1&include_adult=false";

    private Context mContext;
    FetchMoviesCallback mCallback;

    public FetchMoviesTask(Context context, FetchMoviesCallback callback) {
        mContext = context;
        mCallback = callback;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            String movie = params[0].replaceAll("[\\s]", "%20").toLowerCase();
            URL url = new URL(String.format(Locale.US, URL_FORMAT, API_KEY, movie));
            URLConnection connection = url.openConnection();
            InputStream inputStream = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            return stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (s == null || s.length() == 0) {
            mCallback.onFetchTaskFailed();
        } else {
            mCallback.onFetchTaskSuccess(s);
        }
    }
}
