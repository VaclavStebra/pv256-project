package cz.muni.fi.pv256.movio2.uco_422186.data.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class UpdaterSyncService extends Service {
    public static final Object LOCK = new Object();
    private static UpdaterSyncAdapter sUpdaterSyncAdapter = null;

    @Override
    public void onCreate() {
        synchronized (LOCK) {
            if (sUpdaterSyncAdapter == null) {
                sUpdaterSyncAdapter = new UpdaterSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return sUpdaterSyncAdapter.getSyncAdapterBinder();
    }
}
