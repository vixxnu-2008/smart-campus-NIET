package com.smartcampus.app.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.provider.Settings;

/**
 * Provides a stable per-install device identifier used by the "Device
 * Binding" security feature: once a student registers their face on Device
 * A, attendance attempts from Device B are flagged for admin review even if
 * the correct password is entered. Wired into the Auth/Attendance
 * repositories starting Phase 2/4.
 *
 * Uses Settings.Secure.ANDROID_ID rather than IMEI/serial: ANDROID_ID
 * doesn't require any dangerous permission, is unique per app-signing-key +
 * user + device, and resets on factory reset - which is the correct
 * behavior for this use case (a wiped device should re-verify).
 */
public final class DeviceUtils {

    private DeviceUtils() {
    }

    @SuppressLint("HardwareIds")
    public static String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }
}
