package practice.android.searchmovies.adapter;

/**
 * Created by Yee on 7/17/17.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import practice.android.searchmovies.ImageUtility;
import practice.android.searchmovies.MovieInfoActivity;
import practice.android.searchmovies.R;
import practice.android.searchmovies.manager.ScreenAppearanceManager;
import practice.android.searchmovies.model.Movie;
import practice.android.searchmovies.model.MoviesLab;


/**
 * Created by Stella on 6/8/17.
 */

public class MoviesListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final String TAG = "MoviesListAdapter";
    public static final String MOVIE_EXTRA = "current clicked movie";

    public static final String IMAGE_URL_PREFIX = "https://image.tmdb.org/t/p/original";
    public static final int TYPE_NORMAL_ITEM = 0;
    public static final int TYPE_BOTTOM_REFRESH_ITEM = 1;

    private ArrayList<Movie> mMovies;
    private Context mContext;

    public MoviesListAdapter(ArrayList<Movie> movies) {
        mMovies = movies;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_NORMAL_ITEM) {
            View view = LayoutInflater.from(mContext = parent.getContext()).inflate(R.layout.list_item, parent,
                    false);
            return new MoviesHolder(view);
        } else {
            View view = LayoutInflater.from(mContext = parent.getContext()).inflate(R.layout.list_tempty_item,
                    parent, false);
            return new BottomRefreshViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MoviesHolder) {
            final Movie movie = mMovies.get(position);
            ((MoviesHolder) holder).title.setText(movie.getTitle());
            ((MoviesHolder) holder).vote.setText(movie.getVoteAverage());
            ((MoviesHolder) holder).releaseDate.setText(movie.getReleaseDate());
            String url = IMAGE_URL_PREFIX + movie.getPosterPath();
            final ImageView poster = ((MoviesHolder) holder).poster;
            final ImageUtility utility = ((MoviesHolder) holder).utility;
            final int width = ((MoviesHolder) holder).width;
            final int length = ((MoviesHolder) holder).length;
            Picasso.with(mContext).load(url).centerCrop().resize(width, length).into(poster, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(mContext).load(R.drawable.no_image_available).centerCrop().resize(width, length).into
                            (poster);
                }
            });
            ((MoviesHolder) holder).itemContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bitmap bitmap = ((BitmapDrawable) poster.getDrawable()).getBitmap();
                    utility.saveImage(bitmap);
                    Context context = v.getContext();
                    Intent intent = new Intent(context, MovieInfoActivity.class);
                    intent.putExtra(MOVIE_EXTRA, movie);
                    context.startActivity(intent);
                }
            });

        } else if (holder instanceof BottomRefreshViewHolder) {
            ((BottomRefreshViewHolder) holder).mProgressBar.getIndeterminateDrawable().setColorFilter(ResourcesCompat
                    .getColor(mContext.getResources(), R.color.pureWhite, null), PorterDuff.Mode.MULTIPLY);
        }
    }

    @Override
    public int getItemCount() {
        return mMovies.size() == MoviesLab.size ? mMovies.size() : mMovies.size() + 1;
    }


    private class MoviesHolder extends RecyclerView.ViewHolder {
        CardView itemContainer;
        ImageView poster;
        TextView title;
        TextView vote;
        TextView releaseDate;
        int width;
        int length;
        ImageUtility utility;

        MoviesHolder(View itemView) {
            super(itemView);
            itemContainer = (CardView) itemView.findViewById(R.id.list_item_container);
            poster = (ImageView) itemView.findViewById(R.id.poster_item);
            title = (TextView) itemView.findViewById(R.id.title_item);
            vote = (TextView) itemView.findViewById(R.id.vote_star);
            releaseDate = (TextView) itemView.findViewById(R.id.release_data_value);
            ScreenAppearanceManager appearanceManager = new ScreenAppearanceManager(mContext);
            width = (int) (appearanceManager.getScreenWidth() * 0.25);
            length = (int) (width * 1.5);
            utility = new ImageUtility(itemView.getContext());
        }
    }

    private class BottomRefreshViewHolder extends RecyclerView.ViewHolder {
        ProgressBar mProgressBar;

        BottomRefreshViewHolder(View itemView) {
            super(itemView);
            mProgressBar = (ProgressBar) itemView.findViewById(R.id.progress_bar_item);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position < mMovies.size()) {
            return TYPE_NORMAL_ITEM;
        } else {
            return TYPE_BOTTOM_REFRESH_ITEM;
        }
    }

    public void setMovies(ArrayList<Movie> movies) {
        mMovies = movies;
    }
}