package cz.muni.fi.pv256.movio2.uco_422186.movies;

import java.util.List;

import cz.muni.fi.pv256.movio2.uco_422186.BaseView;
import cz.muni.fi.pv256.movio2.uco_422186.data.Movie;

public interface MoviesContract {

    interface View extends BaseView<Presenter> {

        void showMovies(List<Movie> movies);

        void showMovieDetailsUi(Movie movie);

        void showNoMovies();
    }

    interface Presenter {

        void loadMovies();

        void onMoviesResult();

        void openMovieDetails(Movie requestedMovie);
    }
}
