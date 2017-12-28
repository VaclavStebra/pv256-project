package cz.muni.fi.pv256.movio2.uco_422186.movies;

import java.util.List;

import cz.muni.fi.pv256.movio2.uco_422186.BaseView;
import cz.muni.fi.pv256.movio2.uco_422186.data.Movie;

public interface MoviesContract {

    interface View extends BaseView<Presenter> {

        void showMovies(List<Movie> movies);

        void showMovies(List<Movie> theatreMovies, List<Movie> newReleases);

        void showMovieDetailsUi(Movie movie);

        void showNoMovies();

        void showFetchingMoviesNotification();

        void showMoviesFetchedNotification();

        void loadFavoriteMovies();
    }

    interface Presenter {

        void loadMovies();

        void favoriteMoviesLoaded(List<Movie> movies);

        void openMovieDetails(Movie requestedMovie);

        void setFiltering(MoviesFilterType filtering);

        MoviesFilterType getFiltering();

        void favoriteMovieUpdated(Movie movie);
    }
}
