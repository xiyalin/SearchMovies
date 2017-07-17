package practice.android.searchmovies;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import practice.android.searchmovies.adapter.MoviesListAdapter;
import practice.android.searchmovies.model.Movie;
import practice.android.searchmovies.model.MoviesLab;

import static android.content.Context.MODE_PRIVATE;
import static practice.android.searchmovies.manager.ScreenAppearanceManager.SHARED_PREFERENCE;
import static practice.android.searchmovies.service.SearchService.JSON;

/**
 * Created by Stella on 6/8/17.
 */

public class SearchFragment extends Fragment {
    public static final String DATA = "transferred movies data";
    public static final int NO_POSITION = 0;

    ArrayList<Movie> mMovies;
    RecyclerView mMoviesList;
    SwipeRefreshLayout mRefreshLayout;
    SharedPreferences mPrefs;
    MoviesListAdapter mAdapter;
    MoviesLab mLab;
    int searchFrom;

    public static SearchFragment newInstance(ArrayList<Movie> movies) {
        Bundle args = new Bundle();
        args.putParcelableArrayList(DATA, movies);
        SearchFragment fragment = new SearchFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        mPrefs = getActivity().getSharedPreferences(SHARED_PREFERENCE, MODE_PRIVATE);
        mMovies = getArguments().getParcelableArrayList(DATA);
        mLab = new MoviesLab();
        searchFrom = 0;
        if (mMovies != null && mMovies.size() != 0) {
            mMoviesList = (RecyclerView) view.findViewById(R.id.movies_list);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            mMoviesList.setLayoutManager(layoutManager);
            mMoviesList.setHasFixedSize(true);
            mMoviesList.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    if (isBottomViewVisible()) {
                        loadAction();
                    }
                }
            });
            setupAdapter();
            mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.list_container);
            mRefreshLayout.setRefreshing(false);
            mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    loadAction();
                    mRefreshLayout.setRefreshing(false);
                }
            });
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void setupAdapter() {
        if (mAdapter == null) {
            mAdapter = new MoviesListAdapter(mMovies);
            mMoviesList.setAdapter(mAdapter);
        }
        mAdapter.setMovies(mMovies);
        mAdapter.notifyDataSetChanged();
    }

    private boolean isBottomViewVisible() {
        int lastItemIndex = getLastVisibleItemPosition();
        return lastItemIndex != NO_POSITION && lastItemIndex == mAdapter.getItemCount() - 1;
    }

    private int getLastVisibleItemPosition() {
        RecyclerView.LayoutManager manager = mMoviesList.getLayoutManager();
        if (manager instanceof LinearLayoutManager) {
            return ((LinearLayoutManager) manager).findLastVisibleItemPosition();
        }
        return NO_POSITION;
    }

    private void loadAction() {
        searchFrom += 5;
        MoviesLab lab = new MoviesLab();
        lab.addMovies(mPrefs.getString(JSON, ""), searchFrom);
        mMovies.addAll(lab.getMovies());
        setupAdapter();
    }
}
