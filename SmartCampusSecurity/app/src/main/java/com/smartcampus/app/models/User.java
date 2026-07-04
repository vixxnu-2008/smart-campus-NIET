package com.smartcampus.app.models;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

/**
 * Base fields shared by every account type, mirrors a document in the
 * "users" Firestore collection. Field names here MUST exactly match the
 * Firestore field names because Firestore's toObject()/POJO mapping is
 * reflection-based and matches by name.
 *
 * See /docs/FIRESTORE_SCHEMA.md for the full "users" collection spec.
 */
public class User {

    private String uid;          // Firebase Auth UID - also the document ID
    private String name;
    private String email;
    private String role;         // "student" | "faculty" | "admin" - see Constants
    private String phone;
    private String profileImageUrl;
    private boolean faceRegistered;
    private String deviceId;     // bound device (see DeviceUtils) - null until first registration
    private boolean active;      // admin can deactivate an account without deleting it

    @ServerTimestamp
    private Date createdAt;      // set automatically by Firestore server, never by the client

    public User() {
        // Required no-arg constructor for Firestore deserialization
    }

    public User(String uid, String name, String email, String role) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.role = role;
        this.active = true;
        this.faceRegistered = false;
    }

    // ---------- Getters / Setters ----------

    public String getUid() { return uid; }
    public void setUid(String uid) { this.uid = uid; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getProfileImageUrl() { return profileImageUrl; }
    public void setProfileImageUrl(String profileImageUrl) { this.profileImageUrl = profileImageUrl; }

    public boolean isFaceRegistered() { return faceRegistered; }
    public void setFaceRegistered(boolean faceRegistered) { this.faceRegistered = faceRegistered; }

    public String getDeviceId() { return deviceId; }
    public void setDeviceId(String deviceId) { this.deviceId = deviceId; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    @Exclude
    public boolean isStudent() { return com.smartcampus.app.utils.Constants.ROLE_STUDENT.equals(role); }
}
