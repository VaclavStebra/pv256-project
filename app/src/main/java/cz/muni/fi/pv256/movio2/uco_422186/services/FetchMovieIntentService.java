package cz.muni.fi.pv256.movio2.uco_422186.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import cz.muni.fi.pv256.movio2.uco_422186.data.Movie;

public class FetchMovieIntentService extends IntentService {

    public static final String EXTRA_MOVIE = "EXTRA_MOVIE";

    public FetchMovieIntentService() {
        super("FetchMovieIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        FetchMovieService fetchService = new FetchMovieService(getApplicationContext());
        Movie movie = intent.getParcelableExtra(EXTRA_MOVIE);
        fetchService.fetch(movie);
    }
}
