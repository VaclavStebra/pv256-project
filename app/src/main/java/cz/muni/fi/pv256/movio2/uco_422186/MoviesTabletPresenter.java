package cz.muni.fi.pv256.movio2.uco_422186;

import java.util.List;

import cz.muni.fi.pv256.movio2.uco_422186.data.Movie;
import cz.muni.fi.pv256.movio2.uco_422186.data.Movies;
import cz.muni.fi.pv256.movio2.uco_422186.data.source.MoviesDataSource;
import cz.muni.fi.pv256.movio2.uco_422186.data.source.MoviesRepository;
import cz.muni.fi.pv256.movio2.uco_422186.moviedetail.MovieDetailContract;
import cz.muni.fi.pv256.movio2.uco_422186.moviedetail.MovieDetailPresenter;
import cz.muni.fi.pv256.movio2.uco_422186.movies.MoviesContract;
import cz.muni.fi.pv256.movio2.uco_422186.movies.MoviesFilterType;
import cz.muni.fi.pv256.movio2.uco_422186.movies.MoviesPresenter;

public class MoviesTabletPresenter implements MoviesContract.Presenter, MovieDetailContract.Presenter,
        MoviesDataSource.GetTheatreMoviesCallback,
        MoviesDataSource.GetNewReleasesCallback {
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
        if (movies.size() > 0) {
            mMovieDetailPresenter.setMovie(movies.get(0));
            mMovieDetailPresenter.showMovie();
        } else {
            showNoMovieSelected();
        }
    }

    @Override
    public void openMovieDetails(Movie requestedMovie) {
        mMovieDetailPresenter.setMovie(requestedMovie);
        mMovieDetailPresenter.showMovie();
    }

    @Override
    public void setFiltering(MoviesFilterType filtering) {
        mMoviesPresenter.setFiltering(filtering);
        mMovieDetailPresenter.setMovie(null);
        showNoMovieSelected();
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

    public MovieDetailPresenter getMovieDetailPresenter() {
        return mMovieDetailPresenter;
    }

    @Override
    public void showMovie() {
        if (mMovieDetailPresenter.getMovie() != null) {
            mMovieDetailPresenter.showMovie();
        } else {
            if (Movies.theaterMovies.size() > 0) {
                mMovieDetailPresenter.setMovie(Movies.theaterMovies.get(0));
                mMovieDetailPresenter.showMovie();
            } else if (Movies.newMovies.size() > 0) {
                mMovieDetailPresenter.setMovie(Movies.newMovies.get(0));
                mMovieDetailPresenter.showMovie();
            } else {
                showNoMovieSelected();
            }
        }
    }

    @Override
    public void showNoMovieSelected() {
        mMovieDetailPresenter.showNoMovieSelected();
    }

    @Override
    public void toggleFavoriteMovie() {
        mMovieDetailPresenter.toggleFavoriteMovie();
        mMoviesPresenter.loadMovies();
    }

    @Override
    public void onTheatreMoviesLoaded(List<Movie> movies) {
        mMoviesPresenter.onTheatreMoviesLoaded(movies);
        showMovie();
    }

    @Override
    public void onNewReleasesLoaded(List<Movie> movies) {
        mMoviesPresenter.onNewReleasesLoaded(movies);
        showMovie();
    }
}
