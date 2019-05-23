package cz.muni.fi.pv256.movio2.uco_422186.data.source;

import android.support.annotation.NonNull;

import java.util.List;
import java.util.Map;

import cz.muni.fi.pv256.movio2.uco_422186.data.Movie;
import cz.muni.fi.pv256.movio2.uco_422186.data.source.local.MoviesManager;

public class MoviesRepositoryImpl implements MoviesRepository {

    private static MoviesRepositoryImpl INSTANCE = null;

    private final MoviesDataSource mMoviesRemoteDataSource;
    private final MoviesManager mMoviesManager;

    public static MoviesRepositoryImpl getInstance(@NonNull MoviesDataSource moviesRemoteDataSource, MoviesManager moviesManager) {
        if (INSTANCE == null) {
            INSTANCE = new MoviesRepositoryImpl(moviesRemoteDataSource, moviesManager);
        }
        return INSTANCE;
    }

    private MoviesRepositoryImpl(@NonNull MoviesDataSource moviesRemoteDataSource, MoviesManager moviesManager) {
        mMoviesRemoteDataSource = moviesRemoteDataSource;
        mMoviesManager = moviesManager;
    }

    @Override
    public void getTheatreMovies() {
        mMoviesRemoteDataSource.getTheatreMovies();
    }

    @Override
    public void getNewReleases() {
        mMoviesRemoteDataSource.getNewReleases();
    }

    @Override
    public void getMovieDetails(Movie movie) {
        mMoviesRemoteDataSource.getMovie(movie);
    }

    @Override
    public List<Movie> getFavoriteMovies() {
        return mMoviesManager.getMovies();
    }

    @Override
    public Movie getFavoriteMovie(Movie movie) {
        return mMoviesManager.getMovie(movie);
    }

    @Override
    public void addFavoriteMovie(Movie movie) {
        mMoviesManager.createMovie(movie);
    }

    @Override
    public void removeFavoriteMovie(Movie movie) {
        mMoviesManager.deleteMovie(movie);
    }

    @Override
    public void updateFavoriteMovie(Movie movie) {
        mMoviesManager.updateMovie(movie);
    }
}
