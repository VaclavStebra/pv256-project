package cz.muni.fi.pv256.movio2.uco_422186;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import cz.muni.fi.pv256.movio2.uco_422186.models.Movie;
import cz.muni.fi.pv256.movio2.uco_422186.services.FetchMoviesIntentService;

public class MainActivity extends AppCompatActivity implements MainFragment.OnMovieSelectListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private ResponseReceiver mReceiver;

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

        IntentFilter filter = new IntentFilter(ResponseReceiver.ACTION_RESPONSE);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        mReceiver = new ResponseReceiver();
        registerReceiver(mReceiver, filter);
    }

    public void fetchMovies() {
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

    public class ResponseReceiver extends BroadcastReceiver {
        public static final String ACTION_RESPONSE =
                "cz.muni.fi.pv256.movio2.uco_422186.ACTION_RESPONSE";

        @Override
        public void onReceive(Context context, Intent intent) {
            updateMoviesView();
        }
    }
}
