package cz.muni.fi.pv256.movio2.uco_422186.data.source;

import java.util.List;

import cz.muni.fi.pv256.movio2.uco_422186.data.Movie;

public interface MoviesDataSource {

    interface GetTheatreMoviesCallback {
        void onTheatreMoviesLoaded(List<Movie> movies);
    }

    interface GetNewReleasesCallback {
        void onNewReleasesLoaded(List<Movie> movies);
    }

    interface GetMovieCallback {
        void onMovieUpdated(Movie movie);
    }

    void getTheatreMovies();
    void getNewReleases();
    void getMovie(Movie movie);
}
