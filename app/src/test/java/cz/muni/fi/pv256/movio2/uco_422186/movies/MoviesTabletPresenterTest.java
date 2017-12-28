package cz.muni.fi.pv256.movio2.uco_422186.movies;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import cz.muni.fi.pv256.movio2.uco_422186.MoviesTabletPresenter;
import cz.muni.fi.pv256.movio2.uco_422186.data.Movie;
import cz.muni.fi.pv256.movio2.uco_422186.data.Movies;
import cz.muni.fi.pv256.movio2.uco_422186.data.source.MoviesRepository;
import cz.muni.fi.pv256.movio2.uco_422186.moviedetail.MovieDetailPresenter;

import static cz.muni.fi.pv256.movio2.uco_422186.movies.Helpers.fakeMovie;
import static org.mockito.Mockito.verify;

public class MoviesTabletPresenterTest {

    @Mock
    private MoviesRepository mMoviesRepository;

    @Mock
    private MoviesPresenter mMoviesPresenter;

    @Mock
    private MovieDetailPresenter mMovieDetailPresenter;

    private MoviesTabletPresenter mMoviesTabletPresenter;

    @Before
    public void setupMoviesTabletPresenter() {
        MockitoAnnotations.initMocks(this);

        mMoviesTabletPresenter = new MoviesTabletPresenter(
                mMoviesRepository, mMoviesPresenter);

        mMoviesTabletPresenter.setMovieDetailPresenter(mMovieDetailPresenter);
    }

    @Test
    public void loadMovies() {
        mMoviesTabletPresenter.loadMovies();

        verify(mMoviesPresenter).loadMovies();
    }

    @Test
    public void favoriteMoviesLoaded_noMovies() {
        List<Movie> movies = new ArrayList<>();

        mMoviesTabletPresenter.favoriteMoviesLoaded(movies);

        verify(mMoviesPresenter).favoriteMoviesLoaded(movies);
        verify(mMovieDetailPresenter).showNoMovieSelected();
    }

    @Test
    public void favoriteMoviesLoaded() {
        List<Movie> movies = new ArrayList<>(1);
        movies.add(fakeMovie());

        mMoviesTabletPresenter.favoriteMoviesLoaded(movies);

        verify(mMoviesPresenter).favoriteMoviesLoaded(movies);
        verify(mMovieDetailPresenter).setMovie(fakeMovie());
        verify(mMovieDetailPresenter).showMovie();
    }

    @Test
    public void openMovieDetails() {
        Movie movie = fakeMovie();

        mMoviesTabletPresenter.openMovieDetails(movie);

        verify(mMovieDetailPresenter).setMovie(movie);
        verify(mMovieDetailPresenter).showMovie();
    }

    @Test
    public void setFiltering() {
        mMoviesTabletPresenter.setFiltering(MoviesFilterType.ALL);

        verify(mMoviesPresenter).setFiltering(MoviesFilterType.ALL);
        verify(mMovieDetailPresenter).setMovie(null);
        verify(mMovieDetailPresenter).showNoMovieSelected();
    }

    @Test
    public void getFiltering() {
        mMoviesTabletPresenter.getFiltering();

        verify(mMoviesPresenter).getFiltering();
    }

    @Test
    public void favoriteMovieUpdated() {
        mMoviesTabletPresenter.favoriteMovieUpdated(fakeMovie());

        verify(mMoviesPresenter).favoriteMovieUpdated(fakeMovie());
    }

    @Test
    public void showMovie() {
        mMoviesTabletPresenter.showMovie();

        verify(mMovieDetailPresenter).showMovie();
    }

    @Test
    public void showMovie_noMovie() {
        Movies.theaterMovies = new ArrayList<>();
        Movies.newMovies = new ArrayList<>();

        mMovieDetailPresenter.setMovie(null);
        mMoviesTabletPresenter.showMovie();

        verify(mMovieDetailPresenter).showNoMovieSelected();
    }

    @Test
    public void toggleFavoriteMovie() {
        mMoviesTabletPresenter.toggleFavoriteMovie();

        verify(mMovieDetailPresenter).toggleFavoriteMovie();
        verify(mMoviesPresenter).loadMovies();
    }

    @Test
    public void onTheatreMoviesLoaded() {
        ArrayList<Movie> movies = new ArrayList<>();
        mMoviesTabletPresenter.onTheatreMoviesLoaded(movies);

        verify(mMoviesPresenter).onTheatreMoviesLoaded(movies);
    }

    @Test
    public void onNewReleasesMoviesLoaded() {
        ArrayList<Movie> movies = new ArrayList<>();
        mMoviesTabletPresenter.onNewReleasesLoaded(movies);

        verify(mMoviesPresenter).onNewReleasesLoaded(movies);
    }
}
