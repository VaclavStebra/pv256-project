package cz.muni.fi.pv256.movio2.uco_422186;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.gson.Gson;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import cz.muni.fi.pv256.movio2.uco_422186.dto.APIResult;
import cz.muni.fi.pv256.movio2.uco_422186.dto.MovieDTO;
import cz.muni.fi.pv256.movio2.uco_422186.models.Movie;

public class MainActivity extends AppCompatActivity implements MainFragment.OnMovieSelectListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private FetchTask mFetchTask;

    private boolean mTwoPane;
    public static List<Movie> movies = new ArrayList<>();

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
        if (mFetchTask == null) {
            mFetchTask = new FetchTask(MainActivity.this);
            mFetchTask.execute();
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

        if (mFetchTask != null) {
            mFetchTask.cancel(true);
        }
    }

    public void onTaskFinished() {
        mFetchTask = null;
        MainFragment mainFragment = (MainFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_main);
        if (mainFragment != null) {
            mainFragment.moviesUpdated();
        }
    }

    private static Date getCurrentTime() {
        Calendar cal = getCurrentCal();
        return cal.getTime();
    }

    private static Calendar getCurrentCal() {
        return Calendar.getInstance(TimeZone.getTimeZone("GMT+1:00"));
    }

    private static String getNowReleaseDate() {
        return new SimpleDateFormat("yyyy-MM-dd").format(getCurrentTime());
    }

    private static String getEndReleaseDate() {
        Calendar cal = getCurrentCal();
        cal.add(Calendar.MONTH, 1);
        return new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
    }

    private static class FetchTask extends AsyncTask<Void, Void, Void> {

        private final WeakReference<MainActivity> mMainActivityWeakReference;
        private final OkHttpClient client = new OkHttpClient();

        private FetchTask(MainActivity mainActivity) {
            mMainActivityWeakReference = new WeakReference<MainActivity>(mainActivity);
        }

        @Override
        protected Void doInBackground(Void... params) {
            Request request = new Request.Builder()
                    .url("https://api.themoviedb.org/3/discover/movie?api_key=" + BuildConfig.API_KEY + "" +
                        "&language=en-US" +
                        "&sort_by=vote_average.desc" +
                        "&include_adult=false" +
                        "&include_video=false" +
                        "&primary_release_date.gte=" + getNowReleaseDate() +
                        "&primary_release_date.lte=" + getEndReleaseDate() +
                        "&with_release_type=2%7C3")
                    .build();

            Call call = client.newCall(request);
            try {
                Response response = call.execute();
                if ( ! response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }
                Gson gson = new Gson();
                APIResult result = gson.fromJson(response.body().string(), APIResult.class);
                movies = new ArrayList<>();
                for (MovieDTO movieDTO : result.movies) {
                    movies.add(new Movie(getCurrentTime().getTime(), movieDTO.posterPath, movieDTO.title, movieDTO.backdropPath, movieDTO.popularity / 20));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            MainActivity activity = mMainActivityWeakReference.get();

            if (activity == null) {
                return;
            }

            activity.onTaskFinished();
        }
    }
}
