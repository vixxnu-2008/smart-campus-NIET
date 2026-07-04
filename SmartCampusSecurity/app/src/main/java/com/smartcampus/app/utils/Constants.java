package com.smartcampus.app.utils;

/**
 * Single source of truth for constants used across the app.
 * Keeping these in one file prevents "magic string" typos when different
 * phases (Auth, Attendance, AI Engine...) all need to reference the same
 * Firestore field name or SharedPreferences key.
 */
public final class Constants {

    private Constants() {
        // no instances
    }

    // ---------- User roles ----------
    public static final String ROLE_STUDENT = "student";
    public static final String ROLE_FACULTY = "faculty";
    public static final String ROLE_ADMIN = "admin";

    // ---------- SharedPreferences keys (values are stored ENCRYPTED - see PreferenceManager) ----------
    public static final String PREF_FILE_NAME = "smart_campus_secure_prefs";
    public static final String KEY_USER_ID = "key_user_id";
    public static final String KEY_USER_ROLE = "key_user_role";
    public static final String KEY_IS_LOGGED_IN = "key_is_logged_in";
    public static final String KEY_FACE_REGISTERED = "key_face_registered";
    public static final String KEY_DEVICE_ID = "key_device_id"; // used for device-binding in Phase 3/4

    // ---------- Intent extras ----------
    public static final String EXTRA_ROLE = "extra_role";
    public static final String EXTRA_USER_ID = "extra_user_id";

    // ---------- Attendance status values (used from Phase 4 onward) ----------
    public static final String ATTENDANCE_PRESENT = "present";
    public static final String ATTENDANCE_ABSENT = "absent";
    public static final String ATTENDANCE_LATE = "late";
    public static final String ATTENDANCE_LEFT_EARLY = "left_early";

    // ---------- Misc ----------
    public static final int MIN_PASSWORD_LENGTH = 8;
    public static final long QR_CODE_VALIDITY_MS = 30_000L; // dynamic QR refreshes every 30s (Phase 4)
    public static final double DEFAULT_GEOFENCE_RADIUS_METERS = 150.0;
}
