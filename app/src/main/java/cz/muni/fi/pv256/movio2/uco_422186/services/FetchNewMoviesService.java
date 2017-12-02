package cz.muni.fi.pv256.movio2.uco_422186.services;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import cz.muni.fi.pv256.movio2.uco_422186.MainActivity;
import cz.muni.fi.pv256.movio2.uco_422186.data.Movies;
import cz.muni.fi.pv256.movio2.uco_422186.dto.APIResult;
import cz.muni.fi.pv256.movio2.uco_422186.dto.MovieDTO;
import cz.muni.fi.pv256.movio2.uco_422186.helpers.DtoMapper;
import cz.muni.fi.pv256.movio2.uco_422186.helpers.TimeHelpers;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FetchNewMoviesService {

    private final WeakReference<MainActivity> mMainActivityWeakReference;

    public FetchNewMoviesService(MainActivity mainActivity) {
        mMainActivityWeakReference = new WeakReference<MainActivity>(mainActivity);
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
            public void onResponse(retrofit2.Call<APIResult> call, retrofit2.Response<APIResult> response) {
                APIResult result = response.body();
                setNewMovies(result);
                notifyActivity();
            }

            @Override
            public void onFailure(retrofit2.Call<APIResult> call, Throwable t) {
                call.cancel();
            }
        });
    }

    private void setNewMovies(APIResult result) {
        Movies.newMovies = new ArrayList<>();
        for (MovieDTO movieDTO : result.movies) {
            Movies.newMovies.add(DtoMapper.mapDTOToMovie(movieDTO));
        }
    }

    private void notifyActivity() {
        MainActivity activity = mMainActivityWeakReference.get();

        if (activity == null) {
            return;
        }

        activity.onNewMoviesFetchFinished();
    }
}
