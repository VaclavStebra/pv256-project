package cz.muni.fi.pv256.movio2.uco_422186.moviedetail;

import cz.muni.fi.pv256.movio2.uco_422186.data.Movie;
import cz.muni.fi.pv256.movio2.uco_422186.data.Movies;
import cz.muni.fi.pv256.movio2.uco_422186.data.source.MoviesRepository;

public class MovieDetailPresenter implements MovieDetailContract.Presenter {
    private final MoviesRepository mMoviesRepository;
    private final MovieDetailContract.View mMovieDetailView;
    private Movie mMovie;

    public MovieDetailPresenter(Movie movie, MoviesRepository moviesRepository, MovieDetailContract.View movieDetailView) {
        mMoviesRepository = moviesRepository;
        mMovieDetailView = movieDetailView;
        mMovie = movie;
    }

    @Override
    public void showMovie() {
        if (mMovie != null) {
            mMovieDetailView.showMovie(mMovie);
        }
    }

    @Override
    public void showNoMovieSelected() {
        mMovieDetailView.showNoMovieSelected();
    }

    @Override
    public void toggleFavoriteMovie() {
        if (mMovie.isFavorite()) {
            mMoviesRepository.removeFavoriteMovie(mMovie);
        } else {
            mMoviesRepository.addFavoriteMovie(mMovie);
        }
        updateMovieInCache();
        mMovie.setFavorite(!mMovie.isFavorite());
        mMovieDetailView.toggleMovieFavorite(mMovie.isFavorite());
    }


    private void updateMovieInCache() {
        int theatreIndex = Movies.theaterMovies.indexOf(mMovie);
        if (theatreIndex != -1) {
            Movies.theaterMovies.set(theatreIndex, mMovie);
        }
        int newMoviesIndex = Movies.newMovies.indexOf(mMovie);
        if (newMoviesIndex != -1) {
            Movies.newMovies.set(newMoviesIndex, mMovie);
        }
    }

    public Movie getMovie() {
        return mMovie;
    }

    public void setMovie(Movie movie) {
        mMovie = movie;
    }
}
