package cz.muni.fi.pv256.movio2.uco_422186.services;

import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

import cz.muni.fi.pv256.movio2.uco_422186.MainActivity;
import cz.muni.fi.pv256.movio2.uco_422186.data.Movies;
import cz.muni.fi.pv256.movio2.uco_422186.dto.APIResult;
import cz.muni.fi.pv256.movio2.uco_422186.dto.MovieDTO;
import cz.muni.fi.pv256.movio2.uco_422186.helpers.DtoMapper;
import cz.muni.fi.pv256.movio2.uco_422186.helpers.TimeHelpers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FetchTheatreMoviesService {

    private Context mContext;

    public FetchTheatreMoviesService(Context context) {
        mContext = context;
    }

    public void fetch() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MovioDbService service = retrofit.create(MovioDbService.class);
        Call<APIResult> call = service.theatreMovies(TimeHelpers.getNowReleaseDate(), TimeHelpers.getEndReleaseDate());
        call.enqueue(new Callback<APIResult>() {

            @Override
            public void onResponse(Call<APIResult> call, Response<APIResult> response) {
                APIResult result = response.body();
                setThatreMovies(result);
                notifyActivity();
            }

            @Override
            public void onFailure(Call<APIResult> call, Throwable t) {
                call.cancel();
            }
        });
    }

    private void setThatreMovies(APIResult result) {
        Movies.theaterMovies = new ArrayList<>();
        for (MovieDTO movieDTO : result.movies) {
            Movies.theaterMovies.add(DtoMapper.mapDTOToMovie(movieDTO));
        }
    }

    private void notifyActivity() {
       Intent broadcastIntent = new Intent();
       broadcastIntent.setAction(MainActivity.ResponseReceiver.ACTION_RESPONSE);
       broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
       mContext.sendBroadcast(broadcastIntent);
    }
}
