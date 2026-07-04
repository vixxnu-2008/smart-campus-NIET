package com.smartcampus.app.base;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.smartcampus.app.utils.SecurityUtils;

/**
 * Base class every Activity in the app extends.
 *
 * Centralizes two things every screen needs:
 *  1. A consistent way to show a message to the user (showToast/showError).
 *  2. A security gate (checkDeviceIntegrity) that every sensitive screen
 *     (login, face capture, attendance marking) calls in onCreate before
 *     showing any UI. If the device is rooted, has an active mock-location
 *     app, or is running in developer mode with USB debugging + an unknown
 *     signature, we block access to security-critical screens rather than
 *     silently trusting client-side data (root detection is a deterrent,
 *     never the sole line of defense - the real enforcement happens in
 *     Firestore Security Rules and Cloud Functions in later phases).
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    protected void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    /**
     * Call this from onCreate() of any security-sensitive screen
     * (LoginActivity, FaceCaptureActivity, QrScanActivity, etc.).
     * Returns true if the device passes baseline integrity checks.
     * Full implementation lives in {@link SecurityUtils} and is wired in Phase 2.
     */
    protected boolean isDeviceTrusted() {
        return SecurityUtils.isRootDetected(this) == false
                && SecurityUtils.isDeveloperModeSuspicious(this) == false;
    }
}
