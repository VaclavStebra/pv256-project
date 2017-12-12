package cz.muni.fi.pv256.movio2.uco_422186.movies;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.tz.UTCProvider;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import cz.muni.fi.pv256.movio2.uco_422186.models.Movie;

import static org.mockito.Mockito.verify;

public class MoviesPresenterTest {
    @Mock
    private TasksContract.View mMoviesView;

    private MoviesPresenter mMoviesPresenter;

    @Before
    public void setupMoviesPresenter() {
        MockitoAnnotations.initMocks(this);

        mMoviesPresenter = new MoviesPresenter(mMoviesView);
        DateTimeZone.setProvider(new UTCProvider());
    }

    @Test
    public void clickOnMovie_ShowsMovieDetailUi() {
        Movie movie = new Movie(1l, new DateTime().toDateTime().getMillis(), "", "", "", 0.2f, "");

        mMoviesPresenter.openMovieDetails(movie);

        verify(mMoviesView).showMovieDetailsUi(movie);
    }
}
