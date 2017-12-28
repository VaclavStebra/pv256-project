package cz.muni.fi.pv256.movio2.uco_422186.movies;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cz.muni.fi.pv256.movio2.uco_422186.moviedetail.MovieDetailActivity;
import cz.muni.fi.pv256.movio2.uco_422186.MoviesRecyclerAdapter;
import cz.muni.fi.pv256.movio2.uco_422186.R;
import cz.muni.fi.pv256.movio2.uco_422186.data.Movie;
import cz.muni.fi.pv256.movio2.uco_422186.data.Movies;
import cz.muni.fi.pv256.movio2.uco_422186.data.source.MoviesDataSource;
import cz.muni.fi.pv256.movio2.uco_422186.data.source.MoviesRepository;
import cz.muni.fi.pv256.movio2.uco_422186.data.source.MoviesRepositoryImpl;
import cz.muni.fi.pv256.movio2.uco_422186.data.source.local.MoviesManager;
import cz.muni.fi.pv256.movio2.uco_422186.data.source.remote.MoviesRemoteDataSource;
import cz.muni.fi.pv256.movio2.uco_422186.util.ActivityUtils;

public class MoviesFragment extends Fragment implements MoviesContract.View,
        LoaderManager.LoaderCallbacks<List<Movie>> {

    private MoviesContract.Presenter mMoviesPresenter;

    private RecyclerView mTheatreMoviesRecyclerView;
    private TextView mEmptyTheatreMoviesView;
    private Menu mOptionsMenu;

    private Context mContext;
    private ResponseReceiver mReceiver;

    @Override
    public void onResume() {
        super.onResume();

        IntentFilter filter = new IntentFilter(ResponseReceiver.ACTION_RESPONSE);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        mReceiver = new ResponseReceiver();
        getActivity().registerReceiver(mReceiver, filter);

        mMoviesPresenter.loadMovies();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity().getApplicationContext();
        setHasOptionsMenu(true);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(mReceiver);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mContext = null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.movies_frag, container, false);

        mTheatreMoviesRecyclerView = view.findViewById(R.id.recycler_view);
        mEmptyTheatreMoviesView = view.findViewById(R.id.empty_view);

        showNoMovies();

        mTheatreMoviesRecyclerView.setHasFixedSize(true);
        mTheatreMoviesRecyclerView.setNestedScrollingEnabled(false);
        mTheatreMoviesRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mTheatreMoviesRecyclerView.setItemAnimator(new DefaultItemAnimator());

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        mOptionsMenu = menu;
        inflater.inflate(R.menu.menu, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        if (mOptionsMenu != null) {
            MenuItem item = mOptionsMenu.findItem(R.id.show_favorites);
            item.setChecked(showFavorites());
        }
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.show_favorites:
                mMoviesPresenter.setFiltering(changedFiltering(item));
                item.setChecked(showFavorites());
                mMoviesPresenter.loadMovies();
                return true;
            /*case R.id.sync_btn:
                UpdaterSyncAdapter.syncImmediately(getActivity().getApplicationContext());
                return true;*/
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {
        return new FetchMovies(mContext);
    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {

    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> movies) {
        mMoviesPresenter.favoriteMoviesLoaded(movies);
    }

    private boolean showFavorites() {
        return mMoviesPresenter.getFiltering() == MoviesFilterType.FAVORITE;
    }

    private MoviesFilterType changedFiltering(MenuItem item) {
        return ! item.isChecked() ? MoviesFilterType.FAVORITE : MoviesFilterType.ALL;
    }

    @Override
    public void setPresenter(MoviesContract.Presenter presenter) {
        mMoviesPresenter = presenter;
    }

    @Override
    public void showMovies(List<Movie> movies) {
        showMoviesView();
        OnMovieClickListener clickListener = new OnMovieClickListener();
        MoviesRecyclerAdapter adapter = new MoviesRecyclerAdapter(mContext, movies, clickListener);
        mTheatreMoviesRecyclerView.swapAdapter(adapter, false);
    }

    private void showMoviesView() {
        mEmptyTheatreMoviesView.setVisibility(View.GONE);
        mTheatreMoviesRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showMovies(List<Movie> theatreMovies, List<Movie> newReleases) {
        showMoviesView();
        swapAdapter();
    }

    @Override
    public void showMovieDetailsUi(Movie movie) {
        Intent intent = new Intent(getContext(), MovieDetailActivity.class);
        intent.putExtra(MovieDetailActivity.EXTRA_MOVIE, movie);
        startActivity(intent);
    }

    @Override
    public void showNoMovies() {
        mEmptyTheatreMoviesView.setVisibility(View.VISIBLE);
        mTheatreMoviesRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public void showFetchingMoviesNotification() {
        ActivityUtils.showNotification(mContext, "Fetching movie list");
    }

    @Override
    public void showMoviesFetchedNotification() {
        ActivityUtils.showNotification(mContext, "Movie list fetched");
    }

    @Override
    public void loadFavoriteMovies() {
        getLoaderManager().restartLoader(0, null, this).forceLoad();
    }

    public static MoviesFragment newInstance() {
        return new MoviesFragment();
    }

    private void swapAdapter() {
        MoviesFragment.OnMovieClickListener clickListener = new MoviesFragment.OnMovieClickListener();
        MoviesRecyclerAdapter adapter = new MoviesRecyclerAdapter(mContext,
                Movies.theaterMovies, Movies.newMovies, clickListener);
        mTheatreMoviesRecyclerView.swapAdapter(adapter, false);
    }
    public class OnMovieClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Movie movie = (Movie) view.getTag();
            mMoviesPresenter.openMovieDetails(movie);
        }

    }
    public class ResponseReceiver extends BroadcastReceiver {

        public static final String ACTION_RESPONSE =
                "cz.muni.fi.pv256.movio2.uco_422186.ACTION_RESPONSE";
        public static final String NEW_RELEASES = "NEW_RELEASES";
        public static final String THEATRE_MOVIES = "THEATRE_MOVIES";

        public static final String MOVIE = "MOVIE";
        @Override
        public void onReceive(Context context, Intent intent) {
            ArrayList<Movie> newReleases = intent.getParcelableArrayListExtra(NEW_RELEASES);
            if (newReleases != null) {
                ((MoviesDataSource.GetNewReleasesCallback) mMoviesPresenter).onNewReleasesLoaded(newReleases);
            }
            ArrayList<Movie> theaterMovies = intent.getParcelableArrayListExtra(THEATRE_MOVIES);
            if (theaterMovies != null) {
                ((MoviesDataSource.GetTheatreMoviesCallback) mMoviesPresenter).onTheatreMoviesLoaded(theaterMovies);
            }
            /*Movie updatedMovie = intent.getParcelableExtra(MOVIE);
            if (updatedMovie != null) {
                onMovieUpdated(updatedMovie);
            }*/
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
