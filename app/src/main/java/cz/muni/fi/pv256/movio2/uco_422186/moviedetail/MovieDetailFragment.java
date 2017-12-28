package cz.muni.fi.pv256.movio2.uco_422186.moviedetail;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import cz.muni.fi.pv256.movio2.uco_422186.R;
import cz.muni.fi.pv256.movio2.uco_422186.data.Movie;
import cz.muni.fi.pv256.movio2.uco_422186.helpers.TimeHelpers;

public class MovieDetailFragment extends Fragment implements MovieDetailContract.View {

    private MovieDetailContract.Presenter mPresenter;

    private Context mContext;

    private TextView mMovieTitle;
    private TextView mMovieRelease;
    private TextView mMoviePopularity;
    private TextView mMovieOverview;
    private ImageView mMovieImage;
    private FloatingActionButton mFab;
    private FloatingActionButton mFabRemove;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity().getApplicationContext();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.showMovie();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mContext = null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.moviedetail_frag, container, false);

        mMovieTitle = view.findViewById(R.id.movie_title);
        mMovieRelease = view.findViewById(R.id.movie_release);
        mMoviePopularity = view.findViewById(R.id.movie_popularity);
        mMovieOverview = view.findViewById(R.id.movie_overview);
        mMovieImage = view.findViewById(R.id.movie_image);
        mFab = view.findViewById(R.id.fab);
        mFabRemove = view.findViewById(R.id.fab_remove);

        View.OnClickListener favoriteToggleListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.toggleFavoriteMovie();
            }
        };

        mFab.setOnClickListener(favoriteToggleListener);
        mFabRemove.setOnClickListener(favoriteToggleListener);

        return view;
    }

    public static MovieDetailFragment newInstance() {
        return new MovieDetailFragment();
    }

    @Override
    public void setPresenter(MovieDetailContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showMovie(Movie movie) {
        toggleMovieFavorite(movie.isFavorite());

        mMovieTitle.setText(movie.getTitle());
        mMovieRelease.setText(TimeHelpers.formatDateForDetailView(movie.getReleaseDate()));
        mMoviePopularity.setText(String.format("%.1f", movie.getPopularity()));
        mMovieOverview.setText(movie.getOverview());

        RequestOptions options = new RequestOptions()
            .centerCrop()
            .placeholder(R.drawable.placeholder);

        Glide
            .with(mContext)
            .load(Movie.BASE_BACKDROP_URL + movie.getBackdrop())
            .apply(options)
            .into(mMovieImage);
    }

    @Override
    public void toggleMovieFavorite(boolean isFavorite) {
        if (isFavorite) {
            mFab.setVisibility(View.GONE);
            mFabRemove.setVisibility(View.VISIBLE);
        } else {
            mFab.setVisibility(View.VISIBLE);
            mFabRemove.setVisibility(View.GONE);
        }
    }
}
