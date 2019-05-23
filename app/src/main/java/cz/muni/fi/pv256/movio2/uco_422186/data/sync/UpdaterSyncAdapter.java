package cz.muni.fi.pv256.movio2.uco_422186.data.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import cz.muni.fi.pv256.movio2.uco_422186.R;
import cz.muni.fi.pv256.movio2.uco_422186.data.Movie;
import cz.muni.fi.pv256.movio2.uco_422186.data.source.MoviesRepository;
import cz.muni.fi.pv256.movio2.uco_422186.data.source.MoviesRepositoryImpl;
import cz.muni.fi.pv256.movio2.uco_422186.data.source.local.MoviesManager;
import cz.muni.fi.pv256.movio2.uco_422186.data.source.remote.MoviesRemoteDataSource;

public class UpdaterSyncAdapter extends AbstractThreadedSyncAdapter {
    public static final int SYNC_INTERVAL = 60 * 60 * 24;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL / 3;

    private final MoviesRepository mMoviesRepository;

    public UpdaterSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mMoviesRepository = MoviesRepositoryImpl.getInstance(MoviesRemoteDataSource.getInstance(context),
                new MoviesManager(context));
    }

    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SyncRequest request = new SyncRequest.Builder()
                    .syncPeriodic(syncInterval, flexTime)
                    .setSyncAdapter(account, authority)
                    .setExtras(Bundle.EMPTY)
                    .build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account, authority, Bundle.EMPTY, syncInterval);
        }
    }

    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.content_authority), bundle);
    }

    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
    }

    private static Account getSyncAccount(Context context) {
        AccountManager accountManager = (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);
        Account newAccount = new Account(context.getString(R.string.app_name), context.getString(R.string.sync_account_type));
        if (null == accountManager.getPassword(newAccount)) {
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            ContentResolver.setIsSyncable(newAccount, context.getString(R.string.content_authority), 1);
            onAccountCreated(newAccount, context);
        }
        return newAccount;
    }

    private static void onAccountCreated(Account newAccount, Context context) {
        UpdaterSyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);
        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);
        syncImmediately(context);
    }

    @Override
    public void onPerformSync(Account account, Bundle bundle, String authority, ContentProviderClient contentProviderClient, SyncResult syncResult) {
        List<Movie> favoriteMovies = mMoviesRepository.getFavoriteMovies();
        for (Movie movie : favoriteMovies) {
            mMoviesRepository.getMovieDetails(movie);
        }
    }
}
