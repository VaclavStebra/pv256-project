package cz.muni.fi.pv256.movio2.uco_422186.data.source.remote;

import android.content.Context;
import android.content.Intent;

import cz.muni.fi.pv256.movio2.uco_422186.data.Movie;
import cz.muni.fi.pv256.movio2.uco_422186.data.source.MoviesDataSource;
import cz.muni.fi.pv256.movio2.uco_422186.services.FetchMovieIntentService;
import cz.muni.fi.pv256.movio2.uco_422186.services.FetchMovieService;
import cz.muni.fi.pv256.movio2.uco_422186.services.FetchNewReleasesIntentService;
import cz.muni.fi.pv256.movio2.uco_422186.services.FetchTheatreMoviesIntentService;

public class MoviesRemoteDataSource implements MoviesDataSource {

    private static MoviesRemoteDataSource INSTANCE;
    private final Context mContext;

    public static MoviesRemoteDataSource getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new MoviesRemoteDataSource(context);
        }
        return INSTANCE;
    }

    private MoviesRemoteDataSource(Context context) {
        mContext = context;
    }

    @Override
    public void getTheatreMovies() {
        Intent fetchTheatreMoviesIntent = new Intent(mContext, FetchTheatreMoviesIntentService.class);
        mContext.startService(fetchTheatreMoviesIntent);
    }

    @Override
    public void getNewReleases() {
        Intent fetchNewReleasesIntent = new Intent(mContext, FetchNewReleasesIntentService.class);
        mContext.startService(fetchNewReleasesIntent);
    }

    @Override
    public void getMovie(Movie movie) {
        Intent fetchMovieIntent = new Intent(mContext, FetchMovieIntentService.class);
        fetchMovieIntent.putExtra(FetchMovieIntentService.EXTRA_MOVIE, movie);
        mContext.startService(fetchMovieIntent);
    }
}
