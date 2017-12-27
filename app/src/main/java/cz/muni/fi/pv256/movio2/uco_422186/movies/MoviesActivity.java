package cz.muni.fi.pv256.movio2.uco_422186.movies;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import cz.muni.fi.pv256.movio2.uco_422186.MoviesMvpController;
import cz.muni.fi.pv256.movio2.uco_422186.R;

public class MoviesActivity extends AppCompatActivity {
    private static final String CURRENT_FILTERING_KEY = "CURRENT_FILTERING_KEY";
    private static final String CURRENT_MOVIE_ID_KEY = "CURRENT_MOVIE_ID_KEY";
    private MoviesMvpController mMoviesMvpTabletController;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movies_act);

        Long movieId = null;
        MoviesFilterType currentFiltering = null;
        if (savedInstanceState != null) {
            currentFiltering = (MoviesFilterType) savedInstanceState.getSerializable(CURRENT_FILTERING_KEY);
            movieId = savedInstanceState.getLong(CURRENT_MOVIE_ID_KEY);
        }

        mMoviesMvpTabletController = MoviesMvpController.createMoviesView(this, movieId);
        if (currentFiltering != null) {
            mMoviesMvpTabletController.setFiltering(currentFiltering);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(CURRENT_FILTERING_KEY, mMoviesMvpTabletController.getFiltering());
        outState.putLong(CURRENT_MOVIE_ID_KEY, mMoviesMvpTabletController.getMovieId());
        super.onSaveInstanceState(outState);
    }
}
