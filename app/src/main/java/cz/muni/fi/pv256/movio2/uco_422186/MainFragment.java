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

import cz.muni.fi.pv256.movio2.uco_422186.models.Movie;

public class MainFragment extends Fragment {

    private static final String TAG = MainFragment.class.getSimpleName();

    private Context mContext;
    private OnMovieSelectListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mListener = (OnMovieSelectListener) context;
        } catch (ClassCastException e) {
            Log.e(TAG, "Activity must implement OnMovieSelectListener", e);
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

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        MoviesRecyclerAdapter adapter = new MoviesRecyclerAdapter(mContext, MainActivity.movies);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        Button movie1Btn = (Button) view.findViewById(R.id.movie1_btn);
        Button movie2Btn = (Button) view.findViewById(R.id.movie2_btn);
        Button movie3Btn = (Button) view.findViewById(R.id.movie3_btn);

        movie1Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onMovieSelect(MainActivity.movies.get(0));
                }
            }
        });

        movie2Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onMovieSelect(MainActivity.movies.get(1));
                }
            }
        });

        movie3Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onMovieSelect(MainActivity.movies.get(2));
                }
            }
        });

        return view;
    }

    public interface OnMovieSelectListener {
        void onMovieSelect(Movie movie);
    }
}
