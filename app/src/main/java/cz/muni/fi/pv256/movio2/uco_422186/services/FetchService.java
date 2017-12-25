package cz.muni.fi.pv256.movio2.uco_422186.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

import cz.muni.fi.pv256.movio2.uco_422186.MainActivity;
import cz.muni.fi.pv256.movio2.uco_422186.R;
import cz.muni.fi.pv256.movio2.uco_422186.data.source.MoviesRepository;
import cz.muni.fi.pv256.movio2.uco_422186.data.source.MoviesRepositoryImpl;
import cz.muni.fi.pv256.movio2.uco_422186.data.source.local.MoviesManager;
import cz.muni.fi.pv256.movio2.uco_422186.data.source.remote.MoviesRemoteDataSource;
import cz.muni.fi.pv256.movio2.uco_422186.dto.APIResult;
import cz.muni.fi.pv256.movio2.uco_422186.dto.MovieDTO;
import cz.muni.fi.pv256.movio2.uco_422186.helpers.DtoMapper;
import cz.muni.fi.pv256.movio2.uco_422186.data.Movie;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class FetchService {

    protected Context mContext;
    private final MoviesRepository mMoviesRepository;

    public FetchService(Context context) {
        mContext = context;
        mMoviesRepository = MoviesRepositoryImpl.getInstance(MoviesRemoteDataSource.getInstance(mContext),
                new MoviesManager(mContext));
    }

    protected MovioDbService buildService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(MovioDbService.class);
    }

    protected ArrayList<Movie> getMoviesFromResponse(APIResult result) {
        ArrayList<Movie> movies = new ArrayList<>();
        for (MovieDTO movieDTO : result.movies) {
            Movie movie = DtoMapper.mapDTOToMovie(movieDTO);
            Movie dbMovie = mMoviesRepository.getFavoriteMovie(movie);
            movie.setFavorite(dbMovie != null);
            movies.add(movie);
        }
        return movies;
    }

    protected void fetchError() {
        Intent appIntent = new Intent(mContext, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(mContext, 0, appIntent, 0);

        Notification n = new Notification.Builder(mContext)
                .setContentTitle("Error occured when downloading movie list")
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentIntent(pIntent)
                .setAutoCancel(true)
                .build();

        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(mContext.NOTIFICATION_SERVICE);
        notificationManager.notify(0, n);
    }

    protected void notifyActivity(String key, ArrayList<Movie> movies) {
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(MainActivity.ResponseReceiver.ACTION_RESPONSE);
        broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
        broadcastIntent.putParcelableArrayListExtra(key, movies);
        mContext.sendBroadcast(broadcastIntent);
    }

    protected void notifyActivityOnMovieUpdate(Movie movie) {
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(MainActivity.ResponseReceiver.ACTION_RESPONSE);
        broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
        broadcastIntent.putExtra(MainActivity.ResponseReceiver.MOVIE, movie);
        mContext.sendBroadcast(broadcastIntent);
    }
}
