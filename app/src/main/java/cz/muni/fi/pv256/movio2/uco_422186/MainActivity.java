package cz.muni.fi.pv256.movio2.uco_422186;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cz.muni.fi.pv256.movio2.uco_422186.data.Movies;
import cz.muni.fi.pv256.movio2.uco_422186.data.source.MoviesDataSource;
import cz.muni.fi.pv256.movio2.uco_422186.data.source.MoviesRepository;
import cz.muni.fi.pv256.movio2.uco_422186.data.source.MoviesRepositoryImpl;
import cz.muni.fi.pv256.movio2.uco_422186.data.source.local.MoviesManager;
import cz.muni.fi.pv256.movio2.uco_422186.data.source.remote.MoviesRemoteDataSource;
import cz.muni.fi.pv256.movio2.uco_422186.data.Movie;
import cz.muni.fi.pv256.movio2.uco_422186.data.sync.UpdaterSyncAdapter;

public class MainActivity extends AppCompatActivity implements MainFragment.OnMovieSelectListener,
        MainFragment.OnFavoriteSelectionChanged,
        MoviesDataSource.GetTheatreMoviesCallback,
        MoviesDataSource.GetNewReleasesCallback,
        MoviesDataSource.GetMovieCallback
{

    private static final String TAG = MainActivity.class.getSimpleName();
    public static final String SHOW_FAVORITES = "SHOW_FAVORITES";

    private ResponseReceiver mReceiver;

    private boolean mTwoPane;
    private boolean mShowFavorites = false;

    private MoviesRepository mMoviesRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UpdaterSyncAdapter.initializeSyncAdapter(this);

        if (savedInstanceState != null) {
            mShowFavorites = savedInstanceState.getBoolean(SHOW_FAVORITES);
        }

        mMoviesRepository = MoviesRepositoryImpl.getInstance(MoviesRemoteDataSource.getInstance(getApplicationContext()),
                new MoviesManager(getApplicationContext()));

        setContentView(R.layout.activity_main);
        if (!mShowFavorites) {
            fetchMovies();
        } else {
            fetchFavoriteMovies();
        }

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

        IntentFilter filter = new IntentFilter(ResponseReceiver.ACTION_RESPONSE);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        mReceiver = new ResponseReceiver();
        registerReceiver(mReceiver, filter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(SHOW_FAVORITES, mShowFavorites);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    private void fetchFavoriteMovies() {
        MainFragment mainFragment = (MainFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_main);
        if (mainFragment != null) {
            mainFragment.showFavoriteMovies();
        }
    }

    public void fetchMovies() {
        showNotification("Fetching movie list");

        mMoviesRepository.getNewReleases();
        mMoviesRepository.getTheatreMovies();
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

    private void updateMoviesView() {
        MainFragment mainFragment = (MainFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_main);
        if (mainFragment != null) {
            mainFragment.moviesUpdated();
        }
    }

    @Override
    public void onFavoriteSelectionChanged(boolean showFavorites) {
        mShowFavorites = showFavorites;
        if (!mShowFavorites) {
            fetchMovies();
        } else {
            fetchFavoriteMovies();
        }
    }

    @Override
    public void onTheatreMoviesLoaded(List<Movie> movies) {
        Movies.theaterMovies = movies;
        onMoviesLoaded();
    }

    @Override
    public void onNewReleasesLoaded(List<Movie> movies) {
        Movies.newMovies = movies;
        onMoviesLoaded();
    }

    private void onMoviesLoaded() {
        updateMoviesView();
        showMoviesLoadedNotification();
    }

    private void showMoviesLoadedNotification() {
        showNotification("Movie list fetched");
    }

    private void showMovieUpdatedNotification() {
        showNotification("Favorite movie updated");
    }

    private void showNotification(String title) {
        Intent appIntent = new Intent(MainActivity.this, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(MainActivity.this, 0, appIntent, 0);

        Notification n = new Notification.Builder(MainActivity.this)
                .setContentTitle(title)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentIntent(pIntent)
                .setAutoCancel(true)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, n);
    }

    @Override
    public void onMovieUpdated(Movie movie) {
        showMovieUpdatedNotification();
        mMoviesRepository.updateFavoriteMovie(movie);
    }

    public class ResponseReceiver extends BroadcastReceiver {
        public static final String ACTION_RESPONSE =
                "cz.muni.fi.pv256.movio2.uco_422186.ACTION_RESPONSE";

        public static final String NEW_RELEASES = "NEW_RELEASES";
        public static final String THEATRE_MOVIES = "THEATRE_MOVIES";
        public static final String MOVIE = "MOVIE";

        @Override
        public void onReceive(Context context, Intent intent) {
            ArrayList<Movie> newReleases = intent.getParcelableArrayListExtra(NEW_RELEASES);
            if (newReleases != null) {
                onNewReleasesLoaded(newReleases);
            }
            ArrayList<Movie> theaterMovies = intent.getParcelableArrayListExtra(THEATRE_MOVIES);
            if (theaterMovies != null) {
                onTheatreMoviesLoaded(theaterMovies);
            }
            Movie updatedMovie = intent.getParcelableExtra(MOVIE);
            if (updatedMovie != null) {
                onMovieUpdated(updatedMovie);
            }
        }
    }
}
