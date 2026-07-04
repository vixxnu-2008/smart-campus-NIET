package com.smartcampus.app.models;

/**
 * Student-specific profile fields, stored in the "students" collection
 * (document ID == uid, same as the matching "users" document). Kept as a
 * separate collection from "users" rather than nested fields so Firestore
 * Security Rules can grant faculty/admin read access to student roll
 * numbers/department without exposing every student's auth-level fields.
 * See /docs/FIRESTORE_SCHEMA.md.
 */
public class Student {

    private String uid;
    private String rollNumber;
    private String department;
    private String section;
    private int year;               // 1-4
    private String semester;
    private double attendancePercentage; // denormalized, recalculated by Cloud Function (Phase 6)
    private String parentPhone;
    private String parentEmail;

    public Student() {
    }

    public Student(String uid, String rollNumber, String department, String section, int year) {
        this.uid = uid;
        this.rollNumber = rollNumber;
        this.department = department;
        this.section = section;
        this.year = year;
        this.attendancePercentage = 0.0;
    }

    public String getUid() { return uid; }
    public void setUid(String uid) { this.uid = uid; }

    public String getRollNumber() { return rollNumber; }
    public void setRollNumber(String rollNumber) { this.rollNumber = rollNumber; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getSection() { return section; }
    public void setSection(String section) { this.section = section; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }

    public double getAttendancePercentage() { return attendancePercentage; }
    public void setAttendancePercentage(double attendancePercentage) { this.attendancePercentage = attendancePercentage; }

    public String getParentPhone() { return parentPhone; }
    public void setParentPhone(String parentPhone) { this.parentPhone = parentPhone; }

    public String getParentEmail() { return parentEmail; }
    public void setParentEmail(String parentEmail) { this.parentEmail = parentEmail; }
}
