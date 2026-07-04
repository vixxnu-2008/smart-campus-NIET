package com.smartcampus.app.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.Nullable;

import com.smartcampus.app.base.BaseActivity;
import com.smartcampus.app.databinding.ActivitySplashBinding;
import com.smartcampus.app.ui.roleselect.RoleSelectionActivity;
import com.smartcampus.app.utils.Constants;
import com.smartcampus.app.utils.PreferenceManager;

/**
 * App launcher screen.
 *
 * Phase 1 behavior: always routes to RoleSelectionActivity after a short
 * brand delay (no session logic yet - there is no login system until
 * Phase 2). Once AuthRepository exists (Phase 2), this class will check
 * PreferenceManager.getBoolean(KEY_IS_LOGGED_IN, false) and route straight
 * to the correct role's dashboard for an already-signed-in user instead of
 * showing role selection every time.
 */
public class SplashActivity extends BaseActivity {

    private static final long SPLASH_DELAY_MS = 1200L;
    private ActivitySplashBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        new Handler(Looper.getMainLooper()).postDelayed(this::routeNext, SPLASH_DELAY_MS);
    }

    private void routeNext() {
        boolean isLoggedIn = PreferenceManager.getBoolean(Constants.KEY_IS_LOGGED_IN, false);

        // Phase 2 will branch here based on stored role to open the correct
        // dashboard directly. For now every launch goes to role selection.
        Intent intent = new Intent(this, RoleSelectionActivity.class);
        startActivity(intent);
        finish();
    }
}
