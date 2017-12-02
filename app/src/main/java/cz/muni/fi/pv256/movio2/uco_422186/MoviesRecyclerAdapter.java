package cz.muni.fi.pv256.movio2.uco_422186;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import cz.muni.fi.pv256.movio2.uco_422186.models.Movie;

public class MoviesRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final String TAG = MoviesRecyclerAdapter.class.getSimpleName();
    private static final int TYPE_MOVIE = 0;
    private static final int TYPE_HEADER = 1;

    private Context mContext;
    private List<Item> mItems;
    private int mSecondHeaderPosition;
    private MainFragment.OnMovieClickListener mMovieClickListener;

    public MoviesRecyclerAdapter(Context context, List<Movie> theatreMovies, List<Movie> newMovies, MainFragment.OnMovieClickListener listener) {
        mContext = context;
        mMovieClickListener = listener;
        mSecondHeaderPosition = theatreMovies.size() + 1;
        mItems = new ArrayList<>();
        mItems.add(new Item(TYPE_HEADER, null, "In Theaters"));
        for (Movie m : theatreMovies) {
            mItems.add(new Item(TYPE_MOVIE, m, null));
        }
        mItems.add(new Item(TYPE_HEADER, null, "New Movies"));
        for (Movie m : newMovies) {
            mItems.add(new Item(TYPE_MOVIE, m, null));
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (BuildConfig.logging) {
            Log.d(TAG, "Inflating new view");
        }

        View view = null;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        switch (viewType) {
            case TYPE_MOVIE:
                view = inflater.inflate(R.layout.movies_list_item, parent, false);
                view.setOnClickListener(mMovieClickListener);
                return new MovieViewHolder(view);
            case TYPE_HEADER:
                view = inflater.inflate(R.layout.movies_list_item_header, parent, false);
                return new HeaderViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (BuildConfig.logging) {
            Log.d(TAG, "Binding to existing view");
        }

        switch (getItemViewType(position)) {
            case TYPE_MOVIE:
                Movie movie = mItems.get(position).mMovie;
                MovieViewHolder movieViewHolder = (MovieViewHolder) holder;
                movieViewHolder.title.setText(movie.getTitle());
                movieViewHolder.rating.setText(String.format("%.1f", movie.getPopularity()));
                movieViewHolder.itemView.setTag(movie);

                RequestOptions options = new RequestOptions();
                options.centerCrop();
                options.placeholder(R.drawable.placeholder);
                Glide
                    .with(mContext)
                    .load(Movie.BASE_BACKDROP_URL + movie.getBackdrop())
                    .apply(options)
                    .into(movieViewHolder.backdrop);

                break;
            case TYPE_HEADER:
                String header = mItems.get(position).mHeader;
                HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
                headerViewHolder.text.setText(header);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 || position == mSecondHeaderPosition ? TYPE_HEADER : TYPE_MOVIE;
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView rating;
        public ImageView backdrop;

        public MovieViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.movie_list_title);
            rating = itemView.findViewById(R.id.movie_list_rating);
            backdrop = itemView.findViewById(R.id.movie_list_image);
        }
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        public TextView text;

        public HeaderViewHolder(View itemView) {
            super(itemView);

            text = itemView.findViewById(R.id.movie_list_header);
        }
    }

    private class Item {
        private int mType;
        private Movie mMovie;
        private String mHeader;

        public Item(int type, Movie movie, String header) {
            mType = type;
            mMovie = movie;
            mHeader = header;
        }
    }
}
