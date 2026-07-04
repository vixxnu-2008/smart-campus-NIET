package com.smartcampus.app.data;

/**
 * Generic wrapper every Repository method returns (via callback - see
 * RepositoryCallback) instead of throwing exceptions or returning raw
 * Firebase Task objects. This keeps ViewModels free of any Firebase-specific
 * types, which is the whole point of the Repository pattern: ViewModels only
 * ever see Result&lt;T&gt;, never DocumentSnapshot or FirebaseFirestoreException.
 */
public final class Result<T> {

    private final boolean success;
    private final T data;
    private final String errorMessage;

    private Result(boolean success, T data, String errorMessage) {
        this.success = success;
        this.data = data;
        this.errorMessage = errorMessage;
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(true, data, null);
    }

    public static <T> Result<T> error(String message) {
        return new Result<>(false, null, message);
    }

    public boolean isSuccess() {
        return success;
    }

    public T getData() {
        return data;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
