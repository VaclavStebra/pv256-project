package cz.muni.fi.pv256.movio2.uco_422186.moviedetail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import cz.muni.fi.pv256.movio2.uco_422186.Injection;
import cz.muni.fi.pv256.movio2.uco_422186.R;
import cz.muni.fi.pv256.movio2.uco_422186.data.Movie;
import cz.muni.fi.pv256.movio2.uco_422186.util.ActivityUtils;

public class MovieDetailActivity extends AppCompatActivity {
    public static final String EXTRA_MOVIE = "EXTRA_MOVIE";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.moviedetail_act);

        Movie movie = getIntent().getParcelableExtra(EXTRA_MOVIE);

        MovieDetailFragment movieDetailFragment = (MovieDetailFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);

        if (movieDetailFragment == null) {
            movieDetailFragment = MovieDetailFragment.newInstance();

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    movieDetailFragment, R.id.contentFrame);
        }

        MovieDetailPresenter movieDetailPresenter = new MovieDetailPresenter(
                movie,
                Injection.provideMoviesRepository(getApplicationContext()),
                movieDetailFragment
        );
        movieDetailFragment.setPresenter(movieDetailPresenter);
    }
}
