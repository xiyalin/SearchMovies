package practice.android.searchmovies.callback;

/**
 * Created by Stella on 6/8/17.
 */

public interface FetchMoviesCallback {

    void onFetchTaskSuccess(String json);

    void onFetchTaskFailed();
}
