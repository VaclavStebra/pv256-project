package cz.muni.fi.pv256.movio2.uco_422186.movies;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.tz.UTCProvider;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import cz.muni.fi.pv256.movio2.uco_422186.data.Movie;
import cz.muni.fi.pv256.movio2.uco_422186.data.Movies;
import cz.muni.fi.pv256.movio2.uco_422186.data.source.MoviesRepository;

import static cz.muni.fi.pv256.movio2.uco_422186.movies.Helpers.fakeMovie;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class MoviesPresenterTest {
    @Mock
    private MoviesContract.View mMoviesView;

    @Mock
    private MoviesRepository mMoviesRepository;

    private MoviesPresenter mMoviesPresenter;

    @Before
    public void setupMoviesPresenter() {
        MockitoAnnotations.initMocks(this);
        mMoviesPresenter = new MoviesPresenter(mMoviesRepository, mMoviesView);
        DateTimeZone.setProvider(new UTCProvider());
    }

    @Test
    public void loadMovies_all() {
        mMoviesPresenter.setFiltering(MoviesFilterType.ALL);

        mMoviesPresenter.loadMovies();

        verify(mMoviesView).showFetchingMoviesNotification();
        verify(mMoviesRepository).getTheatreMovies();
        verify(mMoviesRepository).getNewReleases();
        verify(mMoviesView, never()).loadFavoriteMovies();
    }

    @Test
    public void loadMovies_favorite() {
        mMoviesPresenter.setFiltering(MoviesFilterType.FAVORITE);

        mMoviesPresenter.loadMovies();

        verify(mMoviesView).loadFavoriteMovies();
        verify(mMoviesRepository, never()).getTheatreMovies();
        verify(mMoviesRepository, never()).getNewReleases();
    }

    @Test
    public void favoriteMoviesLoaded_noMovies() {
        mMoviesPresenter.favoriteMoviesLoaded(new ArrayList());

        verify(mMoviesView).showNoFavoriteMovies();
    }

    @Test
    public void favoriteMoviesLoaded_hasMovies() {
        List<Movie> movies = new ArrayList<>(1);
        movies.add(fakeMovie());

        mMoviesPresenter.favoriteMoviesLoaded(movies);

        verify(mMoviesView).showMovies(movies);
    }

    @Test
    public void openMovieDetails() {
        Movie movie = fakeMovie();

        mMoviesPresenter.openMovieDetails(movie);

        verify(mMoviesView).showMovieDetailsUi(movie);
    }

    @Test
    public void favoriteMovieUpdated_allFilter() {
        mMoviesPresenter.setFiltering(MoviesFilterType.ALL);
        Movie movie = fakeMovie();

        mMoviesPresenter.favoriteMovieUpdated(movie);

        verify(mMoviesRepository).updateFavoriteMovie(movie);
        verify(mMoviesView, never()).loadFavoriteMovies();
    }

    @Test
    public void favoriteMovieUpdated_favoriteFilter() {
        mMoviesPresenter.setFiltering(MoviesFilterType.FAVORITE);
        Movie movie = fakeMovie();

        mMoviesPresenter.favoriteMovieUpdated(movie);

        verify(mMoviesRepository).updateFavoriteMovie(movie);
        verify(mMoviesView).loadFavoriteMovies();
    }

    @Test
    public void onTheatreMoviesLoaded() {
        Movies.theaterMovies = new ArrayList<>();
        Movies.newMovies = new ArrayList<>();

        List<Movie> movies = new ArrayList<>(1);
        movies.add(fakeMovie());

        mMoviesPresenter.onTheatreMoviesLoaded(movies);

        verify(mMoviesView).showMoviesFetchedNotification();
        verify(mMoviesView).showMovies(movies, new ArrayList<Movie>());
    }

    @Test
    public void onNewReleasesLoaded() {
        Movies.theaterMovies = new ArrayList<>();
        Movies.newMovies = new ArrayList<>();
        List<Movie> movies = new ArrayList<>(1);
        movies.add(fakeMovie());

        mMoviesPresenter.onNewReleasesLoaded(movies);

        verify(mMoviesView).showMoviesFetchedNotification();
        verify(mMoviesView).showMovies(new ArrayList<Movie>(), movies);
    }

    @Test
    public void onTheatreMoviesLoaded_noMovies() {
        Movies.theaterMovies = new ArrayList<>();
        Movies.newMovies = new ArrayList<>();
        List<Movie> movies = new ArrayList<>(1);

        mMoviesPresenter.onTheatreMoviesLoaded(movies);

        verify(mMoviesView).showMoviesFetchedNotification();
        verify(mMoviesView).showNoMovies();
    }
}
