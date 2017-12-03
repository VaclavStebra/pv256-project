package cz.muni.fi.pv256.movio2.uco_422186;

import android.test.AndroidTestCase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cz.muni.fi.pv256.movio2.uco_422186.data.MoviesContract;
import cz.muni.fi.pv256.movio2.uco_422186.data.MoviesManager;
import cz.muni.fi.pv256.movio2.uco_422186.helpers.TimeHelpers;
import cz.muni.fi.pv256.movio2.uco_422186.models.Movie;

public class TestMoviesManager extends AndroidTestCase {
    private static final String TAG = TestMoviesManager.class.getSimpleName();

    private MoviesManager mManager;

    @Override
    protected void setUp() throws Exception {
        mManager = new MoviesManager(mContext);
    }

    @Override
    protected void tearDown() throws Exception {
        mContext.getContentResolver().delete(
                MoviesContract.MovieEntry.CONTENT_URI,
                null,
                null
        );
    }

    public void testGetMovies() throws Exception {
        List<Movie> expectedMovies = new ArrayList<>(2);
        Movie movie1 = createMovie(1, TimeHelpers.getCurrentTime().getTime(),null, "Movie 1", null, 1f, "Overview 1");
        Movie movie2 = createMovie(2, TimeHelpers.getCurrentTime().getTime(),null, "Movie 2", null, 2f, "Overview 2");
        expectedMovies.add(movie1);
        expectedMovies.add(movie2);

        mManager.createMovie(movie1);
        mManager.createMovie(movie2);

        List<Movie> movies = mManager.getMovies();
        Log.d(TAG, movies.toString());
        assertTrue(movies.size() == 2);
        assertEquals(expectedMovies, movies);
    }

    public void testDeleteMovie() throws Exception {
        List<Movie> expectedMovies = new ArrayList<>(1);
        Movie movie1 = createMovie(1, TimeHelpers.getCurrentTime().getTime(),null, "Movie 1", null, 1f, "Overview 1");
        Movie movie2 = createMovie(2, TimeHelpers.getCurrentTime().getTime(),null, "Movie 2", null, 2f, "Overview 2");
        expectedMovies.add(movie2);
        mManager.createMovie(movie1);
        mManager.createMovie(movie2);

        mManager.deleteMovie(movie1);

        List<Movie> movies = mManager.getMovies();
        assertTrue(movies.size() == 1);
        assertEquals(expectedMovies, movies);
    }

    private Movie createMovie(long id, long releaseDate, String coverPath, String title, String backdrop, float popularity, String overview) {
        return new Movie(id, releaseDate, coverPath, title, backdrop, popularity, overview);
    }
}
