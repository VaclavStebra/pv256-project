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

import java.util.List;

import cz.muni.fi.pv256.movio2.uco_422186.data.MoviesManager;
import cz.muni.fi.pv256.movio2.uco_422186.models.Movie;
import cz.muni.fi.pv256.movio2.uco_422186.services.FetchMoviesIntentService;

public class MainActivity extends AppCompatActivity implements MainFragment.OnMovieSelectListener, MainFragment.OnFavoriteSelectionChanged {

    private static final String TAG = MainActivity.class.getSimpleName();
    public static final String SHOW_FAVORITES = "SHOW_FAVORITES";

    private ResponseReceiver mReceiver;

    private boolean mTwoPane;
    private boolean mShowFavorites = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            mShowFavorites = savedInstanceState.getBoolean(SHOW_FAVORITES);
        }

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
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

        Notification n = new Notification.Builder(this)
                .setContentTitle("Fetching movie list")
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentIntent(pIntent)
                .setAutoCancel(true)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, n);

        Intent fetchTheatreMoviesIntent = new Intent(this, FetchMoviesIntentService.class);
        fetchTheatreMoviesIntent.putExtra(FetchMoviesIntentService.MOVIES_CATEGORY, FetchMoviesIntentService.THEATRE_MOVIES);
        startService(fetchTheatreMoviesIntent);

        Intent fetchNewMoviesIntent = new Intent(this, FetchMoviesIntentService.class);
        fetchNewMoviesIntent.putExtra(FetchMoviesIntentService.MOVIES_CATEGORY, FetchMoviesIntentService.NEW_MOVIES);
        startService(fetchNewMoviesIntent);
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

    public class ResponseReceiver extends BroadcastReceiver {
        public static final String ACTION_RESPONSE =
                "cz.muni.fi.pv256.movio2.uco_422186.ACTION_RESPONSE";

        @Override
        public void onReceive(Context context, Intent intent) {
            updateMoviesView();

            Intent appIntent = new Intent(MainActivity.this, MainActivity.class);
            PendingIntent pIntent = PendingIntent.getActivity(MainActivity.this, 0, appIntent, 0);

            Notification n = new Notification.Builder(MainActivity.this)
                    .setContentTitle("Movie list fetched")
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setContentIntent(pIntent)
                    .setAutoCancel(true)
                    .build();

            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(0, n);
        }
    }
}
