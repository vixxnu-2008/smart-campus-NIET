package com.smartcampus.app.data;

/**
 * Callback contract used by every Repository method, e.g.:
 *
 *   authRepository.login(email, password, new RepositoryCallback&lt;User&gt;() {
 *       public void onComplete(Result&lt;User&gt; result) {
 *           if (result.isSuccess()) { ... } else { ... }
 *       }
 *   });
 *
 * Firebase calls are inherently async (Task/OnCompleteListener). Wrapping
 * them behind this callback means ViewModels never import a single
 * com.google.firebase.* class - they only depend on this interface and
 * Result&lt;T&gt;. That decoupling is what makes the ViewModel layer unit
 * testable without a real Firebase connection.
 */
public interface RepositoryCallback<T> {
    void onComplete(Result<T> result);
}
