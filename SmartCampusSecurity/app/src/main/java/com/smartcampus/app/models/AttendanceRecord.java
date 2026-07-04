package com.smartcampus.app.models;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

/**
 * One document per attendance mark, stored in the "attendance" collection.
 * Modeled now (Phase 1) because the schema needs to be locked in before
 * Face Recognition (Phase 3) and QR/Geofencing (Phase 4) can write to it.
 * See /docs/FIRESTORE_SCHEMA.md for indexing strategy.
 */
public class AttendanceRecord {

    private String recordId;
    private String studentUid;
    private String subjectId;
    private String facultyUid;
    private String classroomId;
    private String status;          // present | absent | late | left_early - see Constants
    private String method;          // "face" | "qr" | "manual"
    private double latitude;
    private double longitude;
    private boolean withinGeofence;
    private boolean mockLocationFlagged;
    private String deviceId;

    @ServerTimestamp
    private Date timestamp;

    public AttendanceRecord() {
    }

    public String getRecordId() { return recordId; }
    public void setRecordId(String recordId) { this.recordId = recordId; }

    public String getStudentUid() { return studentUid; }
    public void setStudentUid(String studentUid) { this.studentUid = studentUid; }

    public String getSubjectId() { return subjectId; }
    public void setSubjectId(String subjectId) { this.subjectId = subjectId; }

    public String getFacultyUid() { return facultyUid; }
    public void setFacultyUid(String facultyUid) { this.facultyUid = facultyUid; }

    public String getClassroomId() { return classroomId; }
    public void setClassroomId(String classroomId) { this.classroomId = classroomId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public boolean isWithinGeofence() { return withinGeofence; }
    public void setWithinGeofence(boolean withinGeofence) { this.withinGeofence = withinGeofence; }

    public boolean isMockLocationFlagged() { return mockLocationFlagged; }
    public void setMockLocationFlagged(boolean mockLocationFlagged) { this.mockLocationFlagged = mockLocationFlagged; }

    public String getDeviceId() { return deviceId; }
    public void setDeviceId(String deviceId) { this.deviceId = deviceId; }

    public Date getTimestamp() { return timestamp; }
    public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }
}
