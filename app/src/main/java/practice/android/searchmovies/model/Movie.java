package practice.android.searchmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

import java.util.UUID;

/**
 * Created by Stella on 6/15/17.
 */

public class Movie implements Parcelable, ParseData {
    public static final String TAG = "Movie";
    public static final String DEFAULT_VALUE = "N/A";

    private String posterPath;
    private String backdropPath;
    private String adult;
    private String overview;
    private String releaseDate;
    private String originalTitle;
    private String title;
    private String originalLanguage;
    private String popularity;
    private String voteCount;
    private String voteAverage;
    private UUID mId;

    public Movie() {
        mId = UUID.randomUUID();
    }

    protected Movie(Parcel in) {
        posterPath = in.readString();
        backdropPath = in.readString();
        adult = in.readString();
        overview = in.readString();
        releaseDate = in.readString();
        originalTitle = in.readString();
        title = in.readString();
        originalLanguage = in.readString();
        popularity = in.readString();
        voteCount = in.readString();
        voteAverage = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(posterPath);
        dest.writeString(backdropPath);
        dest.writeString(adult);
        dest.writeString(overview);
        dest.writeString(releaseDate);
        dest.writeString(originalTitle);
        dest.writeString(title);
        dest.writeString(originalLanguage);
        dest.writeString(popularity);
        dest.writeString(voteCount);
        dest.writeString(voteAverage);
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public void setAdult(String adult) {
        this.adult = adult;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }

    public void setVoteCount(String voteCount) {
        this.voteCount = voteCount;
    }

    public void setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage;
    }

    public void setId() {
        mId = UUID.randomUUID();
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public String getAdult() {
        return adult;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getTitle() {
        return title;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public String getPopularity() {
        return popularity;
    }

    public String getVoteCount() {
        return voteCount;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public UUID getId() {
        return mId;
    }

    @Override
    public void parseData(JSONObject object) {
        String temp;
        posterPath = (temp = object.optString("poster_path")).equals("") ? DEFAULT_VALUE : temp;
        backdropPath = (temp = object.optString("backdrop_path")).equals("") ? DEFAULT_VALUE : temp;
        adult = (temp = object.optString("adult")).equals("") ? DEFAULT_VALUE : temp;
        overview = (temp = object.optString("overview")).equals("") ? DEFAULT_VALUE : temp;
        releaseDate = (temp = object.optString("release_date")).equals("") ? DEFAULT_VALUE : temp;
        originalTitle = (temp = object.optString("original_title")).equals("") ? DEFAULT_VALUE : temp;
        title = (temp = object.optString("title")).equals("") ? DEFAULT_VALUE : temp;
        originalLanguage = (temp = object.optString("original_language")).equals("") ? DEFAULT_VALUE : temp;
        popularity = (temp = object.optString("popularity")).equals("") ? DEFAULT_VALUE : temp;
        voteCount = (temp = object.optString("vote_count")).equals("") ? DEFAULT_VALUE : temp;
        voteAverage = (temp = object.optString("vote_average")).equals("") ? DEFAULT_VALUE : temp;
    }
}
