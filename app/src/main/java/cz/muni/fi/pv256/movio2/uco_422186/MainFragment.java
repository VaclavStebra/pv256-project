package cz.muni.fi.pv256.movio2.uco_422186;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cz.muni.fi.pv256.movio2.uco_422186.data.Movies;
import cz.muni.fi.pv256.movio2.uco_422186.data.source.MoviesRepository;
import cz.muni.fi.pv256.movio2.uco_422186.data.source.MoviesRepositoryImpl;
import cz.muni.fi.pv256.movio2.uco_422186.data.source.local.MoviesManager;
import cz.muni.fi.pv256.movio2.uco_422186.data.Movie;
import cz.muni.fi.pv256.movio2.uco_422186.data.source.remote.MoviesRemoteDataSource;

public class MainFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Movie>> {

    private static final String TAG = MainFragment.class.getSimpleName();
    private Context mContext;
    private OnMovieSelectListener mListener;
    private OnFavoriteSelectionChanged mFavoriteSelectionListener;
    private RecyclerView mTheatreMoviesRecyclerView;
    private TextView mEmptyTheatreMoviesView;
    private boolean mShowFavorites = false;
    private Menu mOptionsMenu;

    public static final String SHOW_FAVORITES = "SHOW_FAVORITES";

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mListener = (OnMovieSelectListener) context;
            mFavoriteSelectionListener = (OnFavoriteSelectionChanged) context;
        } catch (ClassCastException e) {
            if (BuildConfig.logging) {
                Log.e(TAG, "Activity must implement OnMovieSelectListener and OnFavoriteSelectionChanged", e);
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

        if (savedInstanceState != null) {
            mShowFavorites = savedInstanceState.getBoolean(SHOW_FAVORITES);
        }

        mContext = getActivity().getApplicationContext();

        setHasOptionsMenu(true);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean(SHOW_FAVORITES, mShowFavorites);
        super.onSaveInstanceState(outState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        if (savedInstanceState != null) {
            mShowFavorites = savedInstanceState.getBoolean(SHOW_FAVORITES);
        }

        mTheatreMoviesRecyclerView = view.findViewById(R.id.recycler_view);
        mEmptyTheatreMoviesView = view.findViewById(R.id.empty_view);

        swapViews(false);

        mTheatreMoviesRecyclerView.setHasFixedSize(true);
        mTheatreMoviesRecyclerView.setNestedScrollingEnabled(false);
        mTheatreMoviesRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mTheatreMoviesRecyclerView.setItemAnimator(new DefaultItemAnimator());

        if (mShowFavorites) {
            showFavoriteMovies();
        } else {
            swapAdapter();
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!mShowFavorites) {
            moviesUpdated();
        } else {
            showFavoriteMovies();
        }
    }

    public void moviesUpdated() {
        swapAdapter();
        swapViews(Movies.theaterMovies.size() != 0 || Movies.newMovies.size() != 0);
    }

    private void swapAdapter() {
        OnMovieClickListener clickListener = new OnMovieClickListener();
        MoviesRecyclerAdapter adapter = new MoviesRecyclerAdapter(mContext, Movies.theaterMovies, Movies.newMovies, clickListener);
        mTheatreMoviesRecyclerView.swapAdapter(adapter, false);
    }

    public void swapViews(boolean hasMovies) {
        if (hasMovies) {
            mEmptyTheatreMoviesView.setVisibility(View.GONE);
            mTheatreMoviesRecyclerView.setVisibility(View.VISIBLE);
        } else {
            mEmptyTheatreMoviesView.setVisibility(View.VISIBLE);
            mTheatreMoviesRecyclerView.setVisibility(View.GONE);
        }
    }

    public void showFavoriteMovies() {
        getLoaderManager().restartLoader(0, null, this).forceLoad();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        mOptionsMenu = menu;
        inflater.inflate(R.menu.menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.show_favorites:
                mShowFavorites = !item.isChecked();
                item.setChecked(mShowFavorites);
                mFavoriteSelectionListener.onFavoriteSelectionChanged(mShowFavorites);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        if (mOptionsMenu != null) {
            MenuItem item = mOptionsMenu.findItem(R.id.show_favorites);
            item.setChecked(mShowFavorites);
        }
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {
        return new FetchMovies(getContext());
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> data) {
        OnMovieClickListener clickListener = new OnMovieClickListener();
        MoviesRecyclerAdapter adapter = new MoviesRecyclerAdapter(mContext, data, clickListener);
        mTheatreMoviesRecyclerView.swapAdapter(adapter, false);
        swapViews(data.size() != 0);
    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {

    }

    public interface OnMovieSelectListener {
        void onMovieSelect(Movie movie);
    }

    public interface OnFavoriteSelectionChanged {
        void onFavoriteSelectionChanged(boolean showFavorites);
    }

    public class OnMovieClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Movie movie = (Movie) view.getTag();
            mListener.onMovieSelect(movie);
        }
    }

    private static class FetchMovies extends AsyncTaskLoader<List<Movie>> {

        private Context mContext;

        public FetchMovies(@NonNull Context context) {
            super(context);
            mContext = context;
        }

        @Nullable
        @Override
        public List<Movie> loadInBackground() {
            MoviesRepository moviesRepository = MoviesRepositoryImpl.getInstance(MoviesRemoteDataSource.getInstance(mContext),
                    new MoviesManager(mContext));
            return moviesRepository.getFavoriteMovies();
        }
    }
}
