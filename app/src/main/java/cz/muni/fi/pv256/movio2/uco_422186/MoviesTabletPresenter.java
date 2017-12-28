package cz.muni.fi.pv256.movio2.uco_422186;

import java.util.List;

import cz.muni.fi.pv256.movio2.uco_422186.data.Movie;
import cz.muni.fi.pv256.movio2.uco_422186.data.source.MoviesRepository;
import cz.muni.fi.pv256.movio2.uco_422186.moviedetail.MovieDetailContract;
import cz.muni.fi.pv256.movio2.uco_422186.moviedetail.MovieDetailPresenter;
import cz.muni.fi.pv256.movio2.uco_422186.movies.MoviesContract;
import cz.muni.fi.pv256.movio2.uco_422186.movies.MoviesFilterType;
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
        mMoviesPresenter.loadMovies();
    }

    @Override
    public void favoriteMoviesLoaded(List<Movie> movies) {
        mMoviesPresenter.favoriteMoviesLoaded(movies);
    }

    @Override
    public void openMovieDetails(Movie requestedMovie) {

    }

    @Override
    public void setFiltering(MoviesFilterType filtering) {
        mMoviesPresenter.setFiltering(filtering);
    }

    @Override
    public MoviesFilterType getFiltering() {
        return mMoviesPresenter.getFiltering();
    }

    @Override
    public void favoriteMovieUpdated(Movie movie) {
        mMoviesPresenter.favoriteMovieUpdated(movie);
    }

    public void setMovieDetailPresenter(MovieDetailPresenter movieDetailPresenter) {
        mMovieDetailPresenter = movieDetailPresenter;
    }

    @Override
    public void showMovie() {
        mMovieDetailPresenter.showMovie();
    }

    @Override
    public void toggleFavoriteMovie() {
        mMovieDetailPresenter.toggleFavoriteMovie();
    }
}
