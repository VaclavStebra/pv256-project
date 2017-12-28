package cz.muni.fi.pv256.movio2.uco_422186.moviedetail;

import cz.muni.fi.pv256.movio2.uco_422186.BaseView;
import cz.muni.fi.pv256.movio2.uco_422186.data.Movie;

public interface MovieDetailContract {
    interface View extends BaseView<Presenter> {
        void showMovie(Movie movie);
        void toggleMovieFavorite(boolean isFavorite);
        void showNoMovieSelected();
    }

    interface Presenter {
        void showMovie();
        void showNoMovieSelected();
        void toggleFavoriteMovie();
    }
}
