package cz.muni.fi.pv256.movio2.uco_422186.data;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import cz.muni.fi.pv256.movio2.uco_422186.models.Movie;

public class MoviesManager {

    public static final int COL_MOVIES_ID = 0;
    public static final int COL_RELEASE_DATE = 1;
    public static final int COL_COVER_PATH = 2;
    public static final int COL_TITLE = 3;
    public static final int COL_BACKDROP = 4;
    public static final int COL_POPULARITY = 5;
    public static final int COL_OVERVIEW = 6;
    private static final String[] MOVIES_COLUMNS = {
            MoviesContract.MovieEntry._ID,
            MoviesContract.MovieEntry.COLUMN_RELEASE_DATE,
            MoviesContract.MovieEntry.COLUMN_COVER_PATH,
            MoviesContract.MovieEntry.COLUMN_TITLE,
            MoviesContract.MovieEntry.COLUMN_BACKDROP,
            MoviesContract.MovieEntry.COLUMN_POPULARITY,
            MoviesContract.MovieEntry.COLUMN_OVERVIEW
    };

    private static final String WHERE_ID = MoviesContract.MovieEntry._ID + " = ?";

    private Context mContext;

    public MoviesManager(Context context) {
        mContext = context.getApplicationContext();
    }

    public void createMovie(Movie movie) {
        ContentUris.parseId(mContext.getContentResolver().insert(MoviesContract.MovieEntry.CONTENT_URI, prepareMovieValues(movie)));
    }

    public void deleteMovie(Movie movie) {
        mContext.getContentResolver().delete(MoviesContract.MovieEntry.CONTENT_URI, WHERE_ID, new String[] {String.valueOf(movie.getId())});
    }

    public List<Movie> getMovies() {
        Cursor cursor = mContext.getContentResolver().query(MoviesContract.MovieEntry.CONTENT_URI, MOVIES_COLUMNS, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            List<Movie> movies = new ArrayList<>(cursor.getCount());
            try {
                while (!cursor.isAfterLast()) {
                    movies.add(getMovie(cursor));
                    cursor.moveToNext();
                }
            } finally {
                cursor.close();
            }
            return movies;
        }
        return Collections.emptyList();
    }

    public Movie getMovie(Movie movie) {
        Cursor cursor = mContext.getContentResolver().query(MoviesContract.MovieEntry.CONTENT_URI, MOVIES_COLUMNS, WHERE_ID, new String[] {String.valueOf(movie.getId())}, null);
        if (cursor != null && cursor.moveToFirst()) {
            Movie foundMovie = null;
            try {
                foundMovie = getMovie(cursor);
            } finally {
                cursor.close();
            }
            return foundMovie;
        }
        return null;
    }

    private ContentValues prepareMovieValues(Movie movie) {
        ContentValues values = new ContentValues();
        values.put(MoviesContract.MovieEntry._ID, movie.getId());
        values.put(MoviesContract.MovieEntry.COLUMN_RELEASE_DATE, MoviesContract.getDbDateString(new Date(movie.getReleaseDate())));
        values.put(MoviesContract.MovieEntry.COLUMN_COVER_PATH, movie.getCoverPath());
        values.put(MoviesContract.MovieEntry.COLUMN_TITLE, movie.getTitle());
        values.put(MoviesContract.MovieEntry.COLUMN_BACKDROP, movie.getBackdrop());
        values.put(MoviesContract.MovieEntry.COLUMN_POPULARITY, movie.getPopularity());
        values.put(MoviesContract.MovieEntry.COLUMN_OVERVIEW, movie.getOverview());
        return values;
    }

    private Movie getMovie(Cursor cursor) {
        Movie movie = new Movie();
        movie.setId(cursor.getLong(COL_MOVIES_ID));
        movie.setReleaseDate(MoviesContract.getDateFromDb(cursor.getString(COL_RELEASE_DATE)).getTime());
        movie.setCoverPath(cursor.getString(COL_COVER_PATH));
        movie.setTitle(cursor.getString(COL_TITLE));
        movie.setBackdrop(cursor.getString(COL_BACKDROP));
        movie.setPopularity(cursor.getLong(COL_POPULARITY));
        movie.setOverview(cursor.getString(COL_OVERVIEW));
        movie.setFavorite(true);
        return movie;
    }
}
