package cz.muni.fi.pv256.movio2.uco_422186.movies;

import java.util.List;

import cz.muni.fi.pv256.movio2.uco_422186.models.Movie;

public interface TasksContract {

    interface View {

        void showMovies(List<Movie> movies);

        void showMovieDetailsUi(Movie movie);
    }

    interface Presenter {

        void loadMovies();

        void onMoviesResult();

        void openMovieDetails(Movie requestedMovie);
    }
}
