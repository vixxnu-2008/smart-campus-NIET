package com.smartcampus.app.base;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * Base class every ViewModel extends (LoginViewModel, AttendanceViewModel,
 * TimetableViewModel, etc. - built starting Phase 2).
 *
 * Exposes two LiveData streams every screen needs:
 *  - isLoading: drives a progress indicator so the UI never needs its own
 *    ad-hoc loading flag.
 *  - errorMessage: a single channel Activities/Fragments observe to show
 *    Firestore/Auth errors, so error handling isn't reimplemented per screen.
 *
 * Concrete ViewModels call setLoading()/setError() from inside their
 * Repository callbacks (Repository pattern is introduced in Phase 2).
 */
public abstract class BaseViewModel extends ViewModel {

    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public LiveData<Boolean> isLoading() {
        return loading;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    protected void setLoading(boolean isLoading) {
        loading.postValue(isLoading);
    }

    protected void setError(String message) {
        errorMessage.postValue(message);
    }
}
