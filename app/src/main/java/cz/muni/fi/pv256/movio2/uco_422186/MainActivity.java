package cz.muni.fi.pv256.movio2.uco_422186;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import cz.muni.fi.pv256.movio2.uco_422186.models.Movie;
import cz.muni.fi.pv256.movio2.uco_422186.tasks.FetchTheatreMoviesTask;

public class MainActivity extends AppCompatActivity implements MainFragment.OnMovieSelectListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private FetchTheatreMoviesTask mFetchTheatreMoviesTask;

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        fetchMovies();

        if (findViewById(R.id.movie_detail_container) != null) {
            mTwoPane = true;

            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container, new DetailFragment(), DetailFragment.TAG)
                        .commit();
            }
        } else {
            mTwoPane = false;
            getSupportActionBar().setElevation(0f);
        }
    }

    public void fetchMovies() {
        if (mFetchTheatreMoviesTask == null) {
            mFetchTheatreMoviesTask = new FetchTheatreMoviesTask(MainActivity.this);
            mFetchTheatreMoviesTask.execute();
        }
    }

    @Override
    public void onMovieSelect(Movie movie) {
        if (mTwoPane) {
            FragmentManager fm = getSupportFragmentManager();
            DetailFragment fragment = DetailFragment.newInstance(movie);

            fm.beginTransaction()
                    .replace(R.id.movie_detail_container, fragment, DetailFragment.TAG)
                    .commit();
        } else {
            Intent intent = new Intent(this, MovieDetailActivity.class);
            intent.putExtra(MovieDetailActivity.EXTRA_MOVIE, movie);
            startActivity(intent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mFetchTheatreMoviesTask != null) {
            mFetchTheatreMoviesTask.cancel(true);
        }
    }

    public void onTaskFinished() {
        mFetchTheatreMoviesTask = null;
        MainFragment mainFragment = (MainFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_main);
        if (mainFragment != null) {
            mainFragment.moviesUpdated();
        }
    }
}
