package cz.muni.fi.pv256.movio2.uco_422186;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cz.muni.fi.pv256.movio2.uco_422186.data.Movies;
import cz.muni.fi.pv256.movio2.uco_422186.models.Movie;

public class MainFragment extends Fragment {

    private static final String TAG = MainFragment.class.getSimpleName();
    private Context mContext;
    private OnMovieSelectListener mListener;
    private RecyclerView mTheatreMoviesRecyclerView;
    private TextView mEmptyTheatreMoviesView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mListener = (OnMovieSelectListener) context;
        } catch (ClassCastException e) {
            if (BuildConfig.logging) {
                Log.e(TAG, "Activity must implement OnMovieSelectListener", e);
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getActivity().getApplicationContext();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        mTheatreMoviesRecyclerView = view.findViewById(R.id.recycler_view);
        mEmptyTheatreMoviesView = view.findViewById(R.id.empty_view);

        swapViews();

        setupRecyclerView();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        moviesUpdated();
    }

    private void setupRecyclerView() {
        mTheatreMoviesRecyclerView.setHasFixedSize(true);
        mTheatreMoviesRecyclerView.setNestedScrollingEnabled(false);
        mTheatreMoviesRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mTheatreMoviesRecyclerView.setItemAnimator(new DefaultItemAnimator());
        OnMovieClickListener clickListener = new OnMovieClickListener();
        MoviesRecyclerAdapter adapter = new MoviesRecyclerAdapter(mContext, Movies.theaterMovies, Movies.newMovies, clickListener);
        mTheatreMoviesRecyclerView.setAdapter(adapter);
    }

    public void moviesUpdated() {
        swapAdapter();
        swapViews();
    }

    private void swapAdapter() {
        OnMovieClickListener clickListener = new OnMovieClickListener();
        MoviesRecyclerAdapter adapter = new MoviesRecyclerAdapter(mContext, Movies.theaterMovies, Movies.newMovies, clickListener);
        mTheatreMoviesRecyclerView.swapAdapter(adapter, false);
    }

    public void swapViews() {
        if (Movies.theaterMovies.size() == 0 && Movies.newMovies.size() == 0) {
            mEmptyTheatreMoviesView.setVisibility(View.VISIBLE);
            mTheatreMoviesRecyclerView.setVisibility(View.GONE);
        } else {
            mEmptyTheatreMoviesView.setVisibility(View.GONE);
            mTheatreMoviesRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    public interface OnMovieSelectListener {
        void onMovieSelect(Movie movie);
    }

    public class OnMovieClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Movie movie = (Movie) view.getTag();
            mListener.onMovieSelect(movie);
        }
    }
}
