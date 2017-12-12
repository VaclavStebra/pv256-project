package cz.muni.fi.pv256.movio2.uco_422186.data.source;

import android.support.annotation.NonNull;

public class MoviesRepository implements MoviesDataSource {

    private static MoviesRepository INSTANCE = null;

    private final MoviesDataSource mMoviesRemoteDataSource;

    public static MoviesRepository getInstance(@NonNull MoviesDataSource moviesRemoteDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new MoviesRepository(moviesRemoteDataSource);
        }
        return INSTANCE;
    }

    private MoviesRepository(@NonNull MoviesDataSource moviesRemoteDataSource) {
        mMoviesRemoteDataSource = moviesRemoteDataSource;
    }

    @Override
    public void getTheatreMovies() {
        mMoviesRemoteDataSource.getTheatreMovies();
    }

    @Override
    public void getNewReleases() {
        mMoviesRemoteDataSource.getNewReleases();
    }
}
