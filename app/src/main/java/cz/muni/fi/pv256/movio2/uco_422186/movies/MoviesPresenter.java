package cz.muni.fi.pv256.movio2.uco_422186.movies;

import java.util.List;

import cz.muni.fi.pv256.movio2.uco_422186.data.Movie;
import cz.muni.fi.pv256.movio2.uco_422186.data.Movies;
import cz.muni.fi.pv256.movio2.uco_422186.data.source.MoviesDataSource;
import cz.muni.fi.pv256.movio2.uco_422186.data.source.MoviesRepository;

public class MoviesPresenter implements MoviesContract.Presenter,
        MoviesDataSource.GetTheatreMoviesCallback,
        MoviesDataSource.GetNewReleasesCallback {
    private final MoviesRepository mMoviesRepository;
    private final MoviesContract.View mMoviesView;
    private MoviesFilterType mFiltering = MoviesFilterType.ALL;

    public MoviesPresenter(MoviesRepository moviesRepository, MoviesContract.View moviesView) {
        mMoviesRepository = moviesRepository;
        mMoviesView = moviesView;
    }

    @Override
    public void loadMovies() {
        if (mFiltering == MoviesFilterType.ALL) {
            loadMovieList();
        } else {
            loadFavoriteMovies();
        }
    }

    @Override
    public void favoriteMoviesLoaded(List<Movie> movies) {
        if (movies.size() > 0) {
            mMoviesView.showMovies(movies);
        } else {
            mMoviesView.showNoMovies();
        }
    }

    private void loadMovieList() {
        mMoviesView.showFetchingMoviesNotification();
        mMoviesRepository.getTheatreMovies();
        mMoviesRepository.getNewReleases();
    }

    private void loadFavoriteMovies() {
        mMoviesView.loadFavoriteMovies();
    }

    @Override
    public void openMovieDetails(Movie requestedMovie) {
        mMoviesView.showMovieDetailsUi(requestedMovie);
    }

    @Override
    public void setFiltering(MoviesFilterType filtering) {
        mFiltering = filtering;
    }

    @Override
    public MoviesFilterType getFiltering() {
        return mFiltering;
    }

    @Override
    public void favoriteMovieUpdated(Movie movie) {
        mMoviesRepository.updateFavoriteMovie(movie);
        if (getFiltering() == MoviesFilterType.FAVORITE) {
            loadFavoriteMovies();
        }
    }

    @Override
    public void onTheatreMoviesLoaded(List<Movie> movies) {
        Movies.theaterMovies = movies;
        mMoviesView.showMoviesFetchedNotification();
        showMovies();
    }

    @Override
    public void onNewReleasesLoaded(List<Movie> movies) {
        Movies.newMovies = movies;
        mMoviesView.showMoviesFetchedNotification();
        showMovies();
    }

    private void showMovies() {
        if (mFiltering == MoviesFilterType.ALL) {
            if (Movies.theaterMovies.size() == 0 && Movies.newMovies.size() == 0) {
                mMoviesView.showNoMovies();
            } else {
                mMoviesView.showMovies(Movies.theaterMovies, Movies.newMovies);
            }
        }
    }
}
