package cz.muni.fi.pv256.movio2.uco_422186.movies;

import org.joda.time.DateTimeZone;
import org.joda.time.tz.UTCProvider;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import cz.muni.fi.pv256.movio2.uco_422186.data.Movie;
import cz.muni.fi.pv256.movio2.uco_422186.data.source.MoviesRepository;
import cz.muni.fi.pv256.movio2.uco_422186.moviedetail.MovieDetailContract;
import cz.muni.fi.pv256.movio2.uco_422186.moviedetail.MovieDetailPresenter;

import static org.mockito.Mockito.verify;

public class MovieDetailPresenterTest {
    @Mock
    private MovieDetailContract.View mMovieDetailView;

    @Mock
    private MoviesRepository mMoviesRepository;

    private MovieDetailPresenter mMovieDetailPresenter;
    private Movie mMovie;

    @Before
    public void setupMovieDetailPresenter() {
        MockitoAnnotations.initMocks(this);
        DateTimeZone.setProvider(new UTCProvider());
        mMovie = Helpers.fakeMovie();
        mMovieDetailPresenter = new MovieDetailPresenter(mMovie, mMoviesRepository, mMovieDetailView);
    }

    @Test
    public void showMovie() {
        mMovieDetailPresenter.showMovie();

        verify(mMovieDetailView).showMovie(mMovie);
    }

    @Test
    public void showMovie_noMovie() {
        mMovieDetailPresenter.setMovie(null);

        mMovieDetailPresenter.showMovie();

        verify(mMovieDetailView).showNoMovieSelected();
    }

    @Test
    public void showNoMovieSelected() {
        mMovieDetailPresenter.showNoMovieSelected();

        verify(mMovieDetailView).showNoMovieSelected();
    }

    @Test
    public void toggleFavoriteMovie_isFavorite() {
        mMovie.setFavorite(true);

        mMovieDetailPresenter.toggleFavoriteMovie();

        verify(mMoviesRepository).removeFavoriteMovie(mMovie);
        verify(mMovieDetailView).toggleMovieFavorite(false);
    }

    @Test
    public void toggleFavoriteMovie_isNotFavorite() {
        mMovie.setFavorite(false);

        mMovieDetailPresenter.toggleFavoriteMovie();

        verify(mMoviesRepository).addFavoriteMovie(mMovie);
        verify(mMovieDetailView).toggleMovieFavorite(true);
    }

}
