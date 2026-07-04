package com.smartcampus.app.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;

/**
 * Small helper to check connectivity before attempting a Firestore write
 * that isn't covered by offline persistence (e.g. checking WorkManager
 * status). Firestore itself queues writes automatically when offline, so
 * this is mainly used to decide when to show an "Offline - will sync when
 * connected" banner (Student "Offline Sync" feature, wired in a later phase).
 */
public final class NetworkUtils {

    private NetworkUtils() {
    }

    @SuppressWarnings("deprecation")
    public static boolean isConnected(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return false;

        NetworkCapabilities capabilities = cm.getNetworkCapabilities(cm.getActiveNetwork());
        if (capabilities != null) {
            return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
        }
        return false;
    }
}
