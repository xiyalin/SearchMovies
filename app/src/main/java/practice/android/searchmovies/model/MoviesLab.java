package practice.android.searchmovies.model;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Stella on 6/15/17.
 */

public class MoviesLab implements ParseData {
    public static final String TAG = "MoviesLab";

    private Context mContext;
    private ArrayList<Movie> mMovies;
    public static int size;
    private int searchFrom;

    public MoviesLab() {
        mMovies = new ArrayList<>();
    }

    public MoviesLab(Context context) {
        mContext = context;
    }

    @Override
    public void parseData(JSONObject object) {
        JSONArray jsonArray = object.optJSONArray("results");
        size = jsonArray.length();
        for (int i = searchFrom; i < Math.min(searchFrom + 5, size) && searchFrom < size; i++) {
            Movie movie = new Movie();
            try {
                movie.parseData(jsonArray.getJSONObject(i));
                mMovies.add(movie);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public ArrayList<Movie> addMovies(String json, int searchFrom) {
        try {
            this.searchFrom = searchFrom;
            JSONObject object = new JSONObject(json);
            parseData(object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mMovies;
    }

    public void initMovies(String json) {
        try {
            this.searchFrom = 0;
            JSONObject object = new JSONObject(json);
            parseData(object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Movie> getMovies() {
        return mMovies;
    }
}
