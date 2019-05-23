package cz.muni.fi.pv256.movio2.uco_422186.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

public class FetchTheatreMoviesIntentService extends IntentService {

    public FetchTheatreMoviesIntentService() {
        super("FetchTheatreMoviesIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        FetchTheatreMoviesService fetchService = new FetchTheatreMoviesService(getApplicationContext());
        fetchService.fetch();
    }
}
