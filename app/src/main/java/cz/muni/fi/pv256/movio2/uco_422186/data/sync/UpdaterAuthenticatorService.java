package cz.muni.fi.pv256.movio2.uco_422186.data.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class UpdaterAuthenticatorService extends Service {

    private UpdaterAuthenticator mAuthenticator;

    @Override
    public void onCreate() {
        mAuthenticator = new UpdaterAuthenticator(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }
}
