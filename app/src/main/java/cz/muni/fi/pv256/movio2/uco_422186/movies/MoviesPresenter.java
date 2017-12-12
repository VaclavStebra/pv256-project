package cz.muni.fi.pv256.movio2.uco_422186.movies;

import cz.muni.fi.pv256.movio2.uco_422186.models.Movie;

public class MoviesPresenter implements TasksContract.Presenter {

    private final TasksContract.View mMoviesView;

    public MoviesPresenter(TasksContract.View moviesView) {
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
}
