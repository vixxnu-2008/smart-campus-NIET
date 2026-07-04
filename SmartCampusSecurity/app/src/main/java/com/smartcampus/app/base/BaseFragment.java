package com.smartcampus.app.base;

import android.widget.Toast;

import androidx.fragment.app.Fragment;

/**
 * Base class every Fragment (dashboard tabs, timetable list, attendance
 * history, etc. - built starting Phase 2) extends. Keeps a single
 * showToast/showError helper so UI-feedback code doesn't get duplicated
 * across 30+ fragments in the finished app.
 */
public abstract class BaseFragment extends Fragment {

    protected void showToast(String message) {
        if (getContext() != null) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }

    protected void showError(String message) {
        if (getContext() != null) {
            Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
        }
    }
}
