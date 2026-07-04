package com.smartcampus.app;

import android.app.Application;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.appcheck.FirebaseAppCheck;
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory;
import com.smartcampus.app.utils.PreferenceManager;

/**
 * Application entry point.
 *
 * Responsibilities:
 *  1. Initialize Firebase as early as possible (before any Activity touches it).
 *  2. Register Firebase App Check with Play Integrity - this stops unauthorized
 *     clients (e.g. a rebuilt/tampered APK, or direct REST calls to Firestore)
 *     from reading/writing data even if they have a valid Firebase config file.
 *     This is the single most important anti-tamper control in the whole app,
 *     because it is enforced server-side and cannot be bypassed by rooting
 *     the device or patching the APK.
 *  3. Initialize the lightweight PreferenceManager singleton used for storing
 *     the signed-in user's role/session flags (encrypted - see PreferenceManager).
 *
 * Every later phase (Auth, Face Recognition, QR Attendance, AI Engine) builds
 * on top of what is initialized here, so this class intentionally stays small
 * and dependency-free.
 */
public class SmartCampusApplication extends Application {

    private static final String TAG = "SmartCampusApp";

    @Override
    public void onCreate() {
        super.onCreate();

        // 1. Firebase core
        FirebaseApp.initializeApp(this);

        // 2. App Check - Play Integrity provider (production-grade device attestation)
        try {
            FirebaseAppCheck.getInstance().installAppCheckProviderFactory(
                    PlayIntegrityAppCheckProviderFactory.getInstance());
        } catch (Exception e) {
            // Play Integrity requires the app to be signed + uploaded to Play Console
            // with an internal test track before it will succeed. During local
            // development this call can fail safely; we log rather than crash so
            // the rest of the app remains usable while you set up Play Integrity.
            Log.w(TAG, "App Check provider not installed (expected during local dev): " + e.getMessage());
        }

        // 3. Local preference/session store
        PreferenceManager.init(this);

        Log.i(TAG, "SmartCampusApplication initialized");
    }
}
