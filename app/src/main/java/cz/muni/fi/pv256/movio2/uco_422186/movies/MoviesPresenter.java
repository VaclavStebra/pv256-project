package cz.muni.fi.pv256.movio2.uco_422186.movies;

import cz.muni.fi.pv256.movio2.uco_422186.data.Movie;
import cz.muni.fi.pv256.movio2.uco_422186.data.source.MoviesRepository;

public class MoviesPresenter implements MoviesContract.Presenter {
    private final MoviesRepository mMoviesRepository;
    private final MoviesContract.View mMoviesView;
    private MoviesFilterType mFiltering;

    public MoviesPresenter(MoviesRepository moviesRepository, MoviesContract.View moviesView) {
        mMoviesRepository = moviesRepository;
        mMoviesView = moviesView;
    }

    @Override
    public void loadMovies() {

    }

    @Override
    public void onMoviesResult() {

    }

    @Override
    public void openMovieDetails(Movie requestedMovie) {
        mMoviesView.showMovieDetailsUi(requestedMovie);
    }

    public void setFiltering(MoviesFilterType filtering) {
        mFiltering = filtering;
    }

    public MoviesFilterType getFiltering() {
        return mFiltering;
    }
}
