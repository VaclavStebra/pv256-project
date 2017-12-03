package cz.muni.fi.pv256.movio2.uco_422186.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {

    private long mId;
    private long mReleaseDate;
    private String mCoverPath;
    private String mTitle;
    private String mBackdrop;
    private float mPopularity;
    private String mOverview;
    private boolean mIsFavorite;
    public static final String BASE_BACKDROP_URL = "http://image.tmdb.org/t/p/w300/";
    public static final String BASE_COVER_URL = "http://image.tmdb.org/t/p/w500/";

    public Movie() {}

    public Movie(long id, long releaseDate, String coverPath, String title, String backdrop, float popularity, String overview) {
        mId = id;
        mReleaseDate = releaseDate;
        mCoverPath = coverPath;
        mTitle = title;
        mBackdrop = backdrop;
        mPopularity = popularity;
        mOverview = overview;
        mIsFavorite = false;
    }

    public Movie(long id, long releaseDate, String coverPath, String title, String backdrop, float popularity, String overview, boolean isFavorite) {
        this(id, releaseDate, coverPath, title, backdrop, popularity, overview);
        mIsFavorite = isFavorite;
    }

    private Movie(Parcel in) {
        mId = in.readLong();
        mReleaseDate = in.readLong();
        mCoverPath = in.readString();
        mTitle = in.readString();
        mBackdrop = in.readString();
        mPopularity = in.readFloat();
        mOverview = in.readString();
        mIsFavorite = in.readByte() != 0;
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
        dest.writeLong(mId);
        dest.writeLong(mReleaseDate);
        dest.writeString(mCoverPath);
        dest.writeString(mTitle);
        dest.writeString(mBackdrop);
        dest.writeFloat(mPopularity);
        dest.writeString(mOverview);
        dest.writeByte((byte) (mIsFavorite ? 1 : 0));
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public long getReleaseDate() {
        return mReleaseDate;
    }

    public void setReleaseDate(long releaseDate) {
        this.mReleaseDate = releaseDate;
    }

    public String getCoverPath() {
        return mCoverPath;
    }

    public void setCoverPath(String coverPath) {
        this.mCoverPath = coverPath;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public String getBackdrop() {
        return mBackdrop;
    }

    public void setBackdrop(String backdrop) {
        this.mBackdrop = backdrop;
    }

    public float getPopularity() {
        return mPopularity;
    }

    public void setPopularity(float popularity) {
        this.mPopularity = popularity;
    }

    public String getOverview() {
        return mOverview;
    }

    public void setOverview(String overview) {
        mOverview = overview;
    }

    public boolean isFavorite() {
        return mIsFavorite;
    }

    public void setFavorite(boolean favorite) {
        mIsFavorite = favorite;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Movie movie = (Movie) o;

        return mId == movie.mId;
    }

    @Override
    public int hashCode() {
        return ((Long) mId).hashCode();
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + mId +
                ", title=" + mTitle +
                '}';
    }
}
