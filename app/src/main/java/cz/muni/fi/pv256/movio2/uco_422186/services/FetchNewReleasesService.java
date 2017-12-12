package cz.muni.fi.pv256.movio2.uco_422186.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import java.util.ArrayList;

import cz.muni.fi.pv256.movio2.uco_422186.MainActivity;
import cz.muni.fi.pv256.movio2.uco_422186.R;
import cz.muni.fi.pv256.movio2.uco_422186.data.MoviesManager;
import cz.muni.fi.pv256.movio2.uco_422186.dto.APIResult;
import cz.muni.fi.pv256.movio2.uco_422186.dto.MovieDTO;
import cz.muni.fi.pv256.movio2.uco_422186.helpers.DtoMapper;
import cz.muni.fi.pv256.movio2.uco_422186.helpers.TimeHelpers;
import cz.muni.fi.pv256.movio2.uco_422186.models.Movie;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FetchNewReleasesService {

    private Context mContext;

    public FetchNewReleasesService(Context context) {
        mContext = context;
    }

    public void fetch() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MovioDbService service = retrofit.create(MovioDbService.class);
        retrofit2.Call<APIResult> call = service.newMovies(TimeHelpers.getNowReleaseDate(), TimeHelpers.getWeekFromNowDate());
        call.enqueue(new Callback<APIResult>() {

            @Override
            public void onResponse(@NonNull retrofit2.Call<APIResult> call, @NonNull retrofit2.Response<APIResult> response) {
                APIResult result = response.body();
                ArrayList<Movie> movies = getMoviesFromResponse(result);
                notifyActivity(movies);
            }

            @Override
            public void onFailure(@NonNull retrofit2.Call<APIResult> call, @NonNull Throwable t) {
                call.cancel();
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
        });
    }

    private ArrayList<Movie> getMoviesFromResponse(APIResult result) {
        ArrayList<Movie> movies = new ArrayList<>();
        for (MovieDTO movieDTO : result.movies) {
            Movie movie = DtoMapper.mapDTOToMovie(movieDTO);
            MoviesManager moviesManager = new MoviesManager(mContext);
            Movie dbMovie = moviesManager.getMovie(movie);
            movie.setFavorite(dbMovie != null);
            movies.add(movie);
        }
        return movies;
    }

    private void notifyActivity(ArrayList<Movie> movies) {
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(MainActivity.ResponseReceiver.ACTION_RESPONSE);
        broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
        broadcastIntent.putParcelableArrayListExtra(MainActivity.ResponseReceiver.NEW_RELEASES, movies);
        mContext.sendBroadcast(broadcastIntent);
    }
}
