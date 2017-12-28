package cz.muni.fi.pv256.movio2.uco_422186.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import cz.muni.fi.pv256.movio2.uco_422186.MainActivity;
import cz.muni.fi.pv256.movio2.uco_422186.MovieDetailActivity;
import cz.muni.fi.pv256.movio2.uco_422186.R;

public class ActivityUtils {
    public static void addFragmentToActivity(@NonNull FragmentManager fragmentManager,
                                             @NonNull Fragment fragment, int frameId) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(frameId, fragment);
        transaction.commit();
    }

    public static boolean isTablet(Context context) {
        return context.getResources().getBoolean(R.bool.isTablet);
    }

    public static void showNotification(Context context, String title) {
        Intent appIntent = new Intent(context, MovieDetailActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, appIntent, 0);

        Notification n = new Notification.Builder(context)
                .setContentTitle(title)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentIntent(pIntent)
                .setAutoCancel(true)
                .build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, n);
    }
}
