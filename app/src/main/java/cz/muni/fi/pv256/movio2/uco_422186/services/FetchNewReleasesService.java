package cz.muni.fi.pv256.movio2.uco_422186.services;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.ArrayList;

import cz.muni.fi.pv256.movio2.uco_422186.MainActivity;
import cz.muni.fi.pv256.movio2.uco_422186.dto.APIResult;
import cz.muni.fi.pv256.movio2.uco_422186.helpers.TimeHelpers;
import cz.muni.fi.pv256.movio2.uco_422186.models.Movie;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FetchNewReleasesService extends FetchService {

    public FetchNewReleasesService(Context context) {
        super(context);
    }

    public void fetch() {
        MovioDbService service = buildService();
        Call<APIResult> call = service.newMovies(TimeHelpers.getNowReleaseDate(), TimeHelpers.getWeekFromNowDate());
        call.enqueue(new Callback<APIResult>() {

            @Override
            public void onResponse(@NonNull Call<APIResult> call, @NonNull Response<APIResult> response) {
                APIResult result = response.body();
                ArrayList<Movie> movies = getMoviesFromResponse(result);
                notifyActivity(MainActivity.ResponseReceiver.NEW_RELEASES, movies);
            }

            @Override
            public void onFailure(@NonNull Call<APIResult> call, @NonNull Throwable t) {
                call.cancel();
                fetchError();
            }
        });
    }
}
