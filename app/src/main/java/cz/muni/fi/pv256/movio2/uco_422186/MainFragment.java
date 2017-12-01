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
import android.widget.Button;
import android.widget.TextView;

import cz.muni.fi.pv256.movio2.uco_422186.models.Movie;

public class MainFragment extends Fragment {

    private static final String TAG = MainFragment.class.getSimpleName();

    private Context mContext;
    private OnMovieSelectListener mListener;
    private RecyclerView mRecyclerView;
    private TextView mEmptyView;

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

        mRecyclerView = view.findViewById(R.id.recycler_view);
        mEmptyView = view.findViewById(R.id.empty_view);

        swapViews();

        mRecyclerView.setHasFixedSize(true);

        OnMovieClickListener movieClickListener = new OnMovieClickListener();

        MoviesRecyclerAdapter adapter = new MoviesRecyclerAdapter(mContext, MainActivity.movies, movieClickListener);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(adapter);
        return view;
    }

    public void moviesUpdated() {
        OnMovieClickListener movieClickListener = new OnMovieClickListener();
        MoviesRecyclerAdapter adapter = new MoviesRecyclerAdapter(mContext, MainActivity.movies, movieClickListener);
        mRecyclerView.swapAdapter(adapter, false);
        swapViews();
    }

    public void swapViews() {
        if (MainActivity.movies.size() == 0) {
            mEmptyView.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        } else {
            mEmptyView.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    public interface OnMovieSelectListener {
        void onMovieSelect(Movie movie);
    }

    public class OnMovieClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            int itemPosition = mRecyclerView.getChildLayoutPosition(view);
            Movie movie = MainActivity.movies.get(itemPosition);
            mListener.onMovieSelect(movie);
        }
    }
}
