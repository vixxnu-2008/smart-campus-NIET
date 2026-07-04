package com.smartcampus.app.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * Encrypted replacement for plain SharedPreferences.
 *
 * OWASP MASVS-STORAGE requires that session tokens, user IDs and role flags
 * are never stored in plaintext on disk (a rooted device or a backup extraction
 * tool can read plain SharedPreferences trivially). This class uses Android's
 * Jetpack Security library (AES256-GCM, key held in the hardware Keystore) so
 * the underlying XML file on disk is ciphertext even on a rooted device.
 *
 * Usage: call PreferenceManager.init(context) once in SmartCampusApplication,
 * then use the static get/put helpers anywhere in the app.
 */
public final class PreferenceManager {

    private static final String TAG = "PreferenceManager";
    private static SharedPreferences prefs;

    private PreferenceManager() {
    }

    public static void init(Context context) {
        try {
            MasterKey masterKey = new MasterKey.Builder(context)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();

            prefs = EncryptedSharedPreferences.create(
                    context,
                    Constants.PREF_FILE_NAME,
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (GeneralSecurityException | IOException e) {
            // Extremely rare (Keystore unavailable). Falling back to regular
            // prefs would violate MASVS-STORAGE, so we log loudly instead of
            // silently downgrading security.
            Log.e(TAG, "Failed to initialize encrypted preferences", e);
            throw new IllegalStateException("Could not initialize secure storage", e);
        }
    }

    public static void putString(String key, String value) {
        prefs.edit().putString(key, value).apply();
    }

    public static String getString(String key, String defaultValue) {
        return prefs.getString(key, defaultValue);
    }

    public static void putBoolean(String key, boolean value) {
        prefs.edit().putBoolean(key, value).apply();
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        return prefs.getBoolean(key, defaultValue);
    }

    public static void clear() {
        prefs.edit().clear().apply();
    }
}
