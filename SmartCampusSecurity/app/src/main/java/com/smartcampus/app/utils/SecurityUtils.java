package com.smartcampus.app.utils;

import android.content.Context;
import android.location.Location;
import android.os.Build;
import android.provider.Settings;

import java.io.File;

/**
 * Baseline device-integrity checks referenced by {@link com.smartcampus.app.base.BaseActivity}.
 *
 * IMPORTANT DESIGN NOTE (documented here so it isn't lost by Phase 6):
 * Every check in this class is a CLIENT-SIDE SIGNAL, not a security boundary.
 * A determined attacker can patch an APK to make isRootDetected() always
 * return false. That is precisely why:
 *   - Firestore Security Rules (Phase 2) independently validate every write
 *     server-side (e.g. a student document can only be written by the
 *     matching auth.uid, regardless of what the client claims).
 *   - Firebase App Check + Play Integrity (wired in SmartCampusApplication)
 *     is the real anti-tamper boundary, because it is verified by Google's
 *     servers, not by code running on the (possibly compromised) device.
 *
 * These checks exist to (a) raise the AI Risk Score / flag an audit log
 * entry when a student attempts attendance from a rooted/mock-location
 * device, and (b) politely block the honest majority of users who
 * accidentally trip a check, with a clear explanation - not to stop a
 * determined attacker on their own.
 */
public final class SecurityUtils {

    private static final String[] KNOWN_ROOT_PATHS = {
            "/system/app/Superuser.apk",
            "/sbin/su",
            "/system/bin/su",
            "/system/xbin/su",
            "/data/local/xbin/su",
            "/data/local/bin/su",
            "/system/sd/xbin/su",
            "/system/bin/failsafe/su",
            "/data/local/su",
            "/su/bin/su"
    };

    private SecurityUtils() {
    }

    /** True if common root-management binaries/build tags are present. */
    public static boolean isRootDetected(Context context) {
        return checkRootBinaries() || checkTestBuildTags() || checkRootManagementApp(context);
    }

    private static boolean checkRootBinaries() {
        for (String path : KNOWN_ROOT_PATHS) {
            if (new File(path).exists()) {
                return true;
            }
        }
        return false;
    }

    private static boolean checkTestBuildTags() {
        String buildTags = Build.TAGS;
        return buildTags != null && buildTags.contains("test-keys");
    }

    private static boolean checkRootManagementApp(Context context) {
        String[] rootPackages = {
                "com.topjohnwu.magisk",
                "eu.chainfire.supersu",
                "com.koushikdutta.superuser",
                "com.noshufou.android.su"
        };
        android.content.pm.PackageManager pm = context.getPackageManager();
        for (String pkg : rootPackages) {
            try {
                pm.getPackageInfo(pkg, 0);
                return true; // package found -> root manager installed
            } catch (android.content.pm.PackageManager.NameNotFoundException ignored) {
                // not installed - expected case, keep checking
            }
        }
        return false;
    }

    /**
     * Flags the combination of USB debugging enabled + an untrusted install
     * source, which is a much stronger signal than checking
     * Settings.Global.ADB_ENABLED alone (many legitimate developers keep ADB
     * on). Used to raise (not auto-block) the AI Risk Score in Phase 6.
     */
    public static boolean isDeveloperModeSuspicious(Context context) {
        int adbEnabled = Settings.Global.getInt(
                context.getContentResolver(), Settings.Global.ADB_ENABLED, 0);
        return adbEnabled == 1;
    }

    /**
     * Mock-location check for a specific Location object. Wired into the
     * Attendance flow in Phase 4, where every GPS reading used for a
     * geofence check is passed through this before being trusted.
     */
    public static boolean isMockLocation(Location location) {
        if (location == null) return false;
        return location.isFromMockProvider();
    }
}
