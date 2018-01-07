package cz.muni.fi.pv256.movio2.uco_422186.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

public class FetchMoviesIntentService extends IntentService {

    public static final String MOVIES_CATEGORY = "movies_category";
    public static final int THEATRE_MOVIES = 0;
    public static final int NEW_MOVIES = 1;

    public FetchMoviesIntentService() {
        super("FetchMoviesIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        int type = intent.getIntExtra(MOVIES_CATEGORY, 0);
        if (type == THEATRE_MOVIES) {
            FetchTheatreMoviesService fetchService = new FetchTheatreMoviesService(getApplicationContext());
            fetchService.fetch();
        } else {
            FetchNewMoviesService fetchService = new FetchNewMoviesService(getApplicationContext());
            fetchService.fetch();
        }
    }
}
