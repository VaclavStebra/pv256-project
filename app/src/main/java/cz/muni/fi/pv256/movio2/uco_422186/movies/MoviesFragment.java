package cz.muni.fi.pv256.movio2.uco_422186.movies;

import android.support.v4.app.Fragment;

import java.util.List;

import cz.muni.fi.pv256.movio2.uco_422186.data.Movie;

public class MoviesFragment extends Fragment implements MoviesContract.View {
    @Override
    public void setPresenter(MoviesContract.Presenter presenter) {

    }

    @Override
    public void showMovies(List<Movie> movies) {

    }

    @Override
    public void showMovieDetailsUi(Movie movie) {

    }

    @Override
    public void showNoMovies() {

    }

    public static MoviesFragment newInstance() {
        return new MoviesFragment();
    }
}
