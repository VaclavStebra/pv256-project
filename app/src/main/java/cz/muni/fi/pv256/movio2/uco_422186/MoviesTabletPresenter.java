package cz.muni.fi.pv256.movio2.uco_422186;

import cz.muni.fi.pv256.movio2.uco_422186.data.Movie;
import cz.muni.fi.pv256.movio2.uco_422186.data.source.MoviesRepository;
import cz.muni.fi.pv256.movio2.uco_422186.moviedetail.MovieDetailContract;
import cz.muni.fi.pv256.movio2.uco_422186.moviedetail.MovieDetailPresenter;
import cz.muni.fi.pv256.movio2.uco_422186.movies.MoviesContract;
import cz.muni.fi.pv256.movio2.uco_422186.movies.MoviesPresenter;

public class MoviesTabletPresenter implements MoviesContract.Presenter, MovieDetailContract.Presenter {
    private final MoviesRepository mMoviesRepository;
    private final MoviesPresenter mMoviesPresenter;
    private MovieDetailPresenter mMovieDetailPresenter;

    public MoviesTabletPresenter(MoviesRepository moviesRepository, MoviesPresenter moviesPresenter) {
        mMoviesRepository = moviesRepository;
        mMoviesPresenter = moviesPresenter;
    }

    @Override
    public void loadMovies() {

    }

    @Override
    public void onMoviesResult() {

    }

    @Override
    public void openMovieDetails(Movie requestedMovie) {

    }

    public void setMovieDetailPresenter(MovieDetailPresenter movieDetailPresenter) {
        mMovieDetailPresenter = movieDetailPresenter;
    }
}
