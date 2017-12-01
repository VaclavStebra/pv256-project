package cz.muni.fi.pv256.movio2.uco_422186;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cz.muni.fi.pv256.movio2.uco_422186.models.Movie;

public class MoviesRecyclerAdapter extends RecyclerView.Adapter<MoviesRecyclerAdapter.ViewHolder> {

    public static final String TAG = MoviesRecyclerAdapter.class.getSimpleName();

    private Context mContext;
    private List<Movie> mMovies;
    private MainFragment.OnMovieClickListener mMovieClickListener;

    public MoviesRecyclerAdapter(Context context, List<Movie> movies, MainFragment.OnMovieClickListener listener) {
        mContext = context;
        mMovies = movies;
        mMovieClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (BuildConfig.logging) {
            Log.d(TAG, "Inflating new view");
        }

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.movies_list_item, parent, false);
        view.setOnClickListener(mMovieClickListener);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (BuildConfig.logging) {
            Log.d(TAG, "Binding to existing view");
        }

        Movie movie = mMovies.get(position);
        holder.title.setText(movie.getTitle());
        holder.rating.setText(String.format("%.1f", movie.getPopularity()));
        holder.itemView.setTag(movie);
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView rating;

        public ViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.movie_list_title);
            rating = itemView.findViewById(R.id.movie_list_rating);
        }
    }
}
