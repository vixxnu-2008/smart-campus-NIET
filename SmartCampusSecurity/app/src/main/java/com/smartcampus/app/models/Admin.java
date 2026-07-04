package com.smartcampus.app.models;

/**
 * Admin-specific profile fields, stored in the "admins" collection
 * (document ID == uid). Deliberately minimal: admin privileges are
 * primarily enforced through Firestore Security Rules + custom auth claims
 * (Phase 2), not through anything stored here.
 */
public class Admin {

    private String uid;
    private String accessLevel; // "super_admin" | "department_admin"
    private String managedDepartment; // set only when accessLevel == department_admin

    public Admin() {
    }

    public Admin(String uid, String accessLevel) {
        this.uid = uid;
        this.accessLevel = accessLevel;
    }

    public String getUid() { return uid; }
    public void setUid(String uid) { this.uid = uid; }

    public String getAccessLevel() { return accessLevel; }
    public void setAccessLevel(String accessLevel) { this.accessLevel = accessLevel; }

    public String getManagedDepartment() { return managedDepartment; }
    public void setManagedDepartment(String managedDepartment) { this.managedDepartment = managedDepartment; }
}
