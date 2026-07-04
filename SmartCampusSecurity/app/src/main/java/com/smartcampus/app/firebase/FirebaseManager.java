package com.smartcampus.app.firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.storage.FirebaseStorage;

/**
 * Single access point for Firebase service instances.
 *
 * Why this exists instead of every Repository calling FirebaseFirestore.getInstance()
 * directly: it lets us configure Firestore ONCE (offline persistence, cache size) in
 * one place, and it gives every Repository a single seam to mock in unit tests.
 *
 * Repositories (built starting Phase 2) will call FirebaseManager.getFirestore(),
 * FirebaseManager.getAuth(), etc. instead of touching the SDK singletons directly.
 */
public final class FirebaseManager {

    private static FirebaseFirestore firestoreInstance;

    private FirebaseManager() {
    }

    public static synchronized FirebaseFirestore getFirestore() {
        if (firestoreInstance == null) {
            firestoreInstance = FirebaseFirestore.getInstance();

            // Enables offline persistence - required by the spec's "Offline Sync"
            // student feature. Reads/writes made while offline are cached locally
            // and automatically synced when connectivity returns.
            FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                    .setPersistenceEnabled(true)
                    .build();
            firestoreInstance.setFirestoreSettings(settings);
        }
        return firestoreInstance;
    }

    public static FirebaseAuth getAuth() {
        return FirebaseAuth.getInstance();
    }

    public static FirebaseStorage getStorage() {
        return FirebaseStorage.getInstance();
    }
}
