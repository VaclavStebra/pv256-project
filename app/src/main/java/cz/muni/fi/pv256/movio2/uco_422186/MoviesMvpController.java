package cz.muni.fi.pv256.movio2.uco_422186;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import cz.muni.fi.pv256.movio2.uco_422186.data.Movie;
import cz.muni.fi.pv256.movio2.uco_422186.moviedetail.MovieDetailFragment;
import cz.muni.fi.pv256.movio2.uco_422186.moviedetail.MovieDetailPresenter;
import cz.muni.fi.pv256.movio2.uco_422186.movies.MoviesActivity;
import cz.muni.fi.pv256.movio2.uco_422186.movies.MoviesFilterType;
import cz.muni.fi.pv256.movio2.uco_422186.movies.MoviesFragment;
import cz.muni.fi.pv256.movio2.uco_422186.movies.MoviesPresenter;
import cz.muni.fi.pv256.movio2.uco_422186.util.ActivityUtils;

import static cz.muni.fi.pv256.movio2.uco_422186.util.ActivityUtils.isTablet;

public class MoviesMvpController {

    private final FragmentActivity mFragmentActivity;
    private final Movie mMovie;

    private MoviesTabletPresenter mMoviesTabletPresenter;
    private MoviesPresenter mMoviesPresenter;

    public MoviesMvpController(FragmentActivity fragmentActivity, Movie movie) {
        mFragmentActivity = fragmentActivity;
        mMovie = movie;
    }

    public static MoviesMvpController createMoviesView(MoviesActivity moviesActivity, Movie movie) {
        MoviesMvpController moviesMvpController = new MoviesMvpController(moviesActivity, movie);
        moviesMvpController.initMoviesView();
        return moviesMvpController;
    }

    private void initMoviesView() {
        if (isTablet(mFragmentActivity)) {
            createTabletElements();
        } else {
            createPhoneElements();
        }
    }

    private void createTabletElements() {
        MoviesFragment moviesFragment = findOrCreateMoviesFragment(R.id.contentFrame_list);
        mMoviesPresenter = createMoviesPresenter(moviesFragment);

        MovieDetailFragment movieDetailFragment = findOrCreateMovieDetailFragmentForTablet();
        MovieDetailPresenter movieDetailPresenter = createMovieDetailPresenter(movieDetailFragment);
        movieDetailPresenter.setMovie(mMovie);

        mMoviesTabletPresenter = new MoviesTabletPresenter(
                Injection.provideMoviesRepository(mFragmentActivity.getApplicationContext()),
                mMoviesPresenter);
        moviesFragment.setPresenter(mMoviesTabletPresenter);
        movieDetailFragment.setPresenter(mMoviesTabletPresenter);
        mMoviesTabletPresenter.setMovieDetailPresenter(movieDetailPresenter);
    }

    private void createPhoneElements() {
        MoviesFragment moviesFragment = findOrCreateMoviesFragment(R.id.contentFrame);
        mMoviesPresenter = createMoviesPresenter(moviesFragment);
        moviesFragment.setPresenter(mMoviesPresenter);
    }

    private MoviesPresenter createMoviesPresenter(MoviesFragment moviesFragment) {
        mMoviesPresenter = new MoviesPresenter(
                Injection.provideMoviesRepository(mFragmentActivity.getApplicationContext()),
                moviesFragment);
        return mMoviesPresenter;
    }

    private MovieDetailPresenter createMovieDetailPresenter(MovieDetailFragment movieDetailFragment) {
        return new MovieDetailPresenter(mMovie,
                Injection.provideMoviesRepository(mFragmentActivity.getApplicationContext()),
                movieDetailFragment);
    }

    private MoviesFragment findOrCreateMoviesFragment(int frameId) {
        MoviesFragment moviesFragment = (MoviesFragment) getFragmentById(frameId);
        if (moviesFragment == null) {
            moviesFragment = MoviesFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), moviesFragment, frameId);
        }
        return moviesFragment;
    }

    private MovieDetailFragment findOrCreateMovieDetailFragmentForTablet() {
        MovieDetailFragment movieDetailFragment = (MovieDetailFragment) getFragmentById(R.id.contentFrame_detail);
        if (movieDetailFragment == null) {
            movieDetailFragment = MovieDetailFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    movieDetailFragment, R.id.contentFrame_detail);
        }
        return movieDetailFragment;
    }

    private Fragment getFragmentById(int frameId) {
        return getSupportFragmentManager().findFragmentById(frameId);
    }

    public void setFiltering(MoviesFilterType filtering) {
        mMoviesPresenter.setFiltering(filtering);
    }

    public MoviesFilterType getFiltering() {
        return mMoviesPresenter.getFiltering();
    }

    public Movie getMovie() {
        return mMoviesTabletPresenter.getMovieDetailPresenter().getMovie();
    }

    private FragmentManager getSupportFragmentManager() {
        return mFragmentActivity.getSupportFragmentManager();
    }
}
