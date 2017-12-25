package cz.muni.fi.pv256.movio2.uco_422186;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import cz.muni.fi.pv256.movio2.uco_422186.data.Movies;
import cz.muni.fi.pv256.movio2.uco_422186.data.source.MoviesRepository;
import cz.muni.fi.pv256.movio2.uco_422186.data.source.MoviesRepositoryImpl;
import cz.muni.fi.pv256.movio2.uco_422186.data.source.local.MoviesManager;
import cz.muni.fi.pv256.movio2.uco_422186.data.source.remote.MoviesRemoteDataSource;
import cz.muni.fi.pv256.movio2.uco_422186.helpers.TimeHelpers;
import cz.muni.fi.pv256.movio2.uco_422186.data.Movie;

public class DetailFragment extends Fragment {

    public static final String TAG = DetailFragment.class.getSimpleName();
    private static final String ARGS_MOVIE = "args_movie";
    private MoviesRepository mMoviesRepository;

    private Context mContext;
    private Movie mMovie;

    public static DetailFragment newInstance(Movie movie) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARGS_MOVIE, movie);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        Bundle args = getArguments();
        if (args != null) {
            mMovie = args.getParcelable(ARGS_MOVIE);
        }

        Context context = getContext();
        mMoviesRepository = MoviesRepositoryImpl.getInstance(MoviesRemoteDataSource.getInstance(context),
                new MoviesManager(context));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        TextView movieTitle = view.findViewById(R.id.movie_title);
        TextView movieRelease = view.findViewById(R.id.movie_release);
        TextView moviePopularity = view.findViewById(R.id.movie_popularity);
        TextView movieOverview = view.findViewById(R.id.movie_overview);
        ImageView movieImage = view.findViewById(R.id.movie_image);
        final FloatingActionButton fab = view.findViewById(R.id.fab);
        final FloatingActionButton fabRemove = view.findViewById(R.id.fab_remove);

        toggleFabIcon(fab, fabRemove);

        if (mMovie == null) {
            mMovie = Movies.theaterMovies.get(0);
        }

        movieTitle.setText(mMovie.getTitle());
        movieRelease.setText(TimeHelpers.formatDateForDetailView(mMovie.getReleaseDate()));
        moviePopularity.setText(String.format("%.1f", mMovie.getPopularity()) + " / 5");
        movieOverview.setText(mMovie.getOverview());

        RequestOptions options = new RequestOptions();
        options.centerCrop();
        options.placeholder(R.drawable.placeholder);
        Glide
                .with(mContext)
                .load(Movie.BASE_BACKDROP_URL + mMovie.getBackdrop())
                .apply(options)
                .into(movieImage);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMoviesRepository.addFavoriteMovie(mMovie);
                mMovie.setFavorite(true);
                updateMovieInCache();
                toggleFabIcon(fab, fabRemove);
            }
        });

        fabRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMoviesRepository.removeFavoriteMovie(mMovie);
                mMovie.setFavorite(false);
                updateMovieInCache();
                toggleFabIcon(fab, fabRemove);
            }
        });

        return view;
    }

    private void updateMovieInCache() {
        int theatreIndex = Movies.theaterMovies.indexOf(mMovie);
        if (theatreIndex != -1) {
            Movies.theaterMovies.set(theatreIndex, mMovie);
        }
        int newMoviesIndex = Movies.newMovies.indexOf(mMovie);
        if (newMoviesIndex != -1) {
            Movies.newMovies.set(newMoviesIndex, mMovie);
        }
    }

    private void toggleFabIcon(FloatingActionButton fab, FloatingActionButton fabRemove) {
        if (mMovie.isFavorite()) {
            fab.setVisibility(View.GONE);
            fabRemove.setVisibility(View.VISIBLE);
        } else {
            fab.setVisibility(View.VISIBLE);
            fabRemove.setVisibility(View.GONE);
        }
    }
}
