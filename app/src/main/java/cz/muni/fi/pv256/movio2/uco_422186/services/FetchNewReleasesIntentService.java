package cz.muni.fi.pv256.movio2.uco_422186.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

public class FetchNewReleasesIntentService extends IntentService {


    public FetchNewReleasesIntentService() {
        super("FetchNewReleasesIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        FetchNewReleasesService fetchService = new FetchNewReleasesService(getApplicationContext());
        fetchService.fetch();
    }
}
