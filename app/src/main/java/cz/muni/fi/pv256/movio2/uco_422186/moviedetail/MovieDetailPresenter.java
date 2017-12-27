package cz.muni.fi.pv256.movio2.uco_422186.moviedetail;

import cz.muni.fi.pv256.movio2.uco_422186.data.source.MoviesRepository;

public class MovieDetailPresenter implements MovieDetailContract.Presenter {
    private final MoviesRepository mMoviesRepository;
    private final MovieDetailContract.View mMovieDetailView;
    private long mMovieId;

    public MovieDetailPresenter(long movieId, MoviesRepository moviesRepository, MovieDetailContract.View movieDetailView) {
        mMoviesRepository = moviesRepository;
        mMovieDetailView = movieDetailView;
        mMovieId = movieId;
    }
}
