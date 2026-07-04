package com.smartcampus.app.ui.roleselect;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.smartcampus.app.base.BaseActivity;
import com.smartcampus.app.databinding.ActivityRoleSelectionBinding;
import com.smartcampus.app.utils.Constants;

/**
 * First screen the user sees: choose Student, Faculty, or Admin.
 * The chosen role is passed as EXTRA_ROLE to LoginActivity, which is built
 * in Phase 2 along with FirebaseAuth-backed AuthRepository/AuthViewModel.
 *
 * Phase 1 only wires the UI + click listeners; the TODOs below are the
 * exact hand-off points for Phase 2.
 */
public class RoleSelectionActivity extends BaseActivity {

    private ActivityRoleSelectionBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRoleSelectionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.cardStudent.setOnClickListener(v -> onRoleChosen(Constants.ROLE_STUDENT));
        binding.cardFaculty.setOnClickListener(v -> onRoleChosen(Constants.ROLE_FACULTY));
        binding.cardAdmin.setOnClickListener(v -> onRoleChosen(Constants.ROLE_ADMIN));
    }

    private void onRoleChosen(String role) {
        // TODO (Phase 2): start LoginActivity with Constants.EXTRA_ROLE = role
        // Intent intent = new Intent(this, LoginActivity.class);
        // intent.putExtra(Constants.EXTRA_ROLE, role);
        // startActivity(intent);
        showToast("Role selected: " + role + " — Login screen arrives in Phase 2");
    }
}
