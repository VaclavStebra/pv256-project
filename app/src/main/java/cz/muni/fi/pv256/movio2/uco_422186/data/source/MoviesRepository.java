package cz.muni.fi.pv256.movio2.uco_422186.data.source;

import java.util.List;

import cz.muni.fi.pv256.movio2.uco_422186.data.Movie;

public interface MoviesRepository {
    void getTheatreMovies();
    void getNewReleases();
    void getMovieDetails(Movie movie);
    List<Movie> getFavoriteMovies();
    Movie getFavoriteMovie(Movie movie);
    void addFavoriteMovie(Movie movie);
    void removeFavoriteMovie(Movie movie);
    void updateFavoriteMovie(Movie movie);
}
