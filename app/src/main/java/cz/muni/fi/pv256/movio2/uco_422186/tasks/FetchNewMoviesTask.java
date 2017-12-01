package cz.muni.fi.pv256.movio2.uco_422186.tasks;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import cz.muni.fi.pv256.movio2.uco_422186.MainActivity;
import cz.muni.fi.pv256.movio2.uco_422186.data.Movies;
import cz.muni.fi.pv256.movio2.uco_422186.dto.APIResult;
import cz.muni.fi.pv256.movio2.uco_422186.dto.MovieDTO;
import cz.muni.fi.pv256.movio2.uco_422186.helpers.DtoMapper;
import cz.muni.fi.pv256.movio2.uco_422186.helpers.FetchHelpers;
import cz.muni.fi.pv256.movio2.uco_422186.helpers.TimeHelpers;
import cz.muni.fi.pv256.movio2.uco_422186.models.Movie;

public class FetchNewMoviesTask extends AsyncTask<Void, Void, Void> {

    private final WeakReference<MainActivity> mMainActivityWeakReference;
    private final OkHttpClient client = new OkHttpClient();

    public FetchNewMoviesTask(MainActivity mainActivity) {
        mMainActivityWeakReference = new WeakReference<MainActivity>(mainActivity);
    }

    @Override
    protected Void doInBackground(Void... params) {
        Request request = new Request.Builder()
                .url(FetchHelpers.getBaseUrl() +
                        "&primary_release_date.gte=" + TimeHelpers.getNowReleaseDate() +
                        "&primary_release_date.lte=" + TimeHelpers.getWeekFromNowDate() +
                        "&with_release_type=1")
                .build();

        Call call = client.newCall(request);
        try {
            Response response = call.execute();
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            APIResult result = FetchHelpers.parseResponse(response);
            setNewMovies(result);
        } catch (IOException | JsonParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void setNewMovies(APIResult result) {
        Movies.newMovies = new ArrayList<>();
        for (MovieDTO movieDTO : result.movies) {
            Movies.newMovies.add(DtoMapper.mapDTOToMovie(movieDTO));
        }
    }

    @Override
    protected void onPostExecute(Void param) {
        MainActivity activity = mMainActivityWeakReference.get();

        if (activity == null) {
            return;
        }

        activity.onNewMoviesTaskFinished();
    }
}
