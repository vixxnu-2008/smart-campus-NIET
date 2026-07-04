package com.smartcampus.app.firebase;

/**
 * Central registry of Firestore collection names.
 * See /docs/FIRESTORE_SCHEMA.md for full document structure, field types
 * and relationships between these collections.
 *
 * Using constants instead of raw strings means a typo becomes a compile-time
 * reference you can "Find Usages" on, instead of a silent runtime bug where
 * a write goes to "attendence" instead of "attendance".
 */
public final class FirestoreCollections {

    private FirestoreCollections() {
    }

    public static final String USERS = "users";
    public static final String STUDENTS = "students";
    public static final String FACULTY = "faculty";
    public static final String ADMINS = "admins";
    public static final String DEPARTMENTS = "departments";
    public static final String CLASSROOMS = "classrooms";
    public static final String TIMETABLE = "timetable";
    public static final String ATTENDANCE = "attendance";
    public static final String LEAVE_REQUESTS = "leave_requests";
    public static final String CLASS_SWAP_REQUESTS = "class_swap_requests";
    public static final String NOTIFICATIONS = "notifications";
    public static final String GEOFENCES = "geofences";
    public static final String AUDIT_LOGS = "audit_logs";
    public static final String RISK_SCORES = "risk_scores";
    public static final String FACE_EMBEDDINGS = "face_embeddings";
    public static final String DEVICE_BINDINGS = "device_bindings";
    public static final String SUBSTITUTE_RECOMMENDATIONS = "substitute_recommendations";
}
