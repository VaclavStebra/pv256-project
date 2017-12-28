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

public class MainActivity extends AppCompatActivity implements
        MoviesDataSource.GetMovieCallback
{

    public static final String SHOW_FAVORITES = "SHOW_FAVORITES";

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

        //setContentView(R.layout.activity_main);
        if (!mShowFavorites) {
            // fetchMovies();
        } else {
            //fetchFavoriteMovies();
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
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(SHOW_FAVORITES, mShowFavorites);
        super.onSaveInstanceState(outState);
    }

//    @Override
//    public void onMovieSelect(Movie movie) {
//        if (mTwoPane) {
//            FragmentManager fm = getSupportFragmentManager();
//            DetailFragment fragment = DetailFragment.newInstance(movie);
//
//            fm.beginTransaction()
//                    .replace(R.id.movie_detail_container, fragment, DetailFragment.TAG)
//                    .commit();
//        } else {
//            Intent intent = new Intent(this, MovieDetailActivity.class);
//            intent.putExtra(MovieDetailActivity.EXTRA_MOVIE, movie);
//            startActivity(intent);
//        }
//    }

//    private void updateMoviesView() {
//        MainFragment mainFragment = (MainFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_main);
//        if (mainFragment != null) {
//            //mainFragment.moviesUpdated();
//        }
//    }
//
//    @Override
//    public void onFavoriteSelectionChanged(boolean showFavorites) {
//        mShowFavorites = showFavorites;
//        if (!mShowFavorites) {
//            //fetchMovies();
//        } else {
//            fetchFavoriteMovies();
//        }
//    }

    //private void showMovieUpdatedNotification() {
        //showNotification("Favorite movie updated");
    //}

    @Override
    public void onMovieUpdated(Movie movie) {
        //showMovieUpdatedNotification();
        mMoviesRepository.updateFavoriteMovie(movie);
    }
}
