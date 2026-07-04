package com.smartcampus.app.models;

import java.util.List;

/**
 * Faculty-specific profile fields, stored in the "faculty" collection
 * (document ID == uid). See /docs/FIRESTORE_SCHEMA.md.
 */
public class Faculty {

    private String uid;
    private String employeeId;
    private String department;
    private String designation;      // "Assistant Professor", "HOD", etc.
    private List<String> subjectsHandled;
    private boolean onLeaveToday;    // denormalized flag, updated by Cloud Function when a leave is approved for today

    public Faculty() {
    }

    public Faculty(String uid, String employeeId, String department, String designation) {
        this.uid = uid;
        this.employeeId = employeeId;
        this.department = department;
        this.designation = designation;
        this.onLeaveToday = false;
    }

    public String getUid() { return uid; }
    public void setUid(String uid) { this.uid = uid; }

    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getDesignation() { return designation; }
    public void setDesignation(String designation) { this.designation = designation; }

    public List<String> getSubjectsHandled() { return subjectsHandled; }
    public void setSubjectsHandled(List<String> subjectsHandled) { this.subjectsHandled = subjectsHandled; }

    public boolean isOnLeaveToday() { return onLeaveToday; }
    public void setOnLeaveToday(boolean onLeaveToday) { this.onLeaveToday = onLeaveToday; }
}
