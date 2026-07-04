# Firestore Database Schema — Smart Campus Security System

This is the locked-in schema every phase builds against. Collection name
constants live in `FirestoreCollections.java` so code never hardcodes these
strings. Designed for Firestore's document model: denormalized where reads
vastly outnumber writes (e.g. `attendancePercentage` cached on the student
doc), normalized where consistency matters more (e.g. attendance records are
never embedded inside a student document, because that document would grow
unbounded and hit Firestore's 1MB document limit within a few months).

---

## 1. `users/{uid}`
Root identity record for every account. Document ID = Firebase Auth UID.

| Field | Type | Notes |
|---|---|---|
| uid | string | duplicate of doc ID, kept for easy querying in collectionGroup queries |
| name | string | |
| email | string | |
| role | string | `student` \| `faculty` \| `admin` |
| phone | string | |
| profileImageUrl | string | Firebase Storage download URL |
| faceRegistered | boolean | gates access to face-based attendance (Phase 3) |
| deviceId | string | bound device (ANDROID_ID) — see Device Binding |
| active | boolean | admin can deactivate without deleting (preserves attendance history) |
| createdAt | timestamp | server-generated |

**Relationships:** 1:1 with exactly one of `students/{uid}`, `faculty/{uid}`,
or `admins/{uid}` depending on `role`. Split this way (rather than one huge
document) so Firestore Security Rules can give faculty read access to
`students/*` fields without exposing `users/*` auth-adjacent fields.

---

## 2. `students/{uid}`
| Field | Type | Notes |
|---|---|---|
| uid | string | |
| rollNumber | string | unique, indexed |
| department | string | FK → `departments/{id}` |
| section | string | |
| year | number | 1–4 |
| semester | string | |
| attendancePercentage | number | recalculated by a Cloud Function trigger whenever `attendance/*` changes for this student |
| parentPhone | string | for low-attendance SMS alerts |
| parentEmail | string | |

## 3. `faculty/{uid}`
| Field | Type | Notes |
|---|---|---|
| uid | string | |
| employeeId | string | unique |
| department | string | FK → `departments/{id}` |
| designation | string | |
| subjectsHandled | array\<string\> | subject IDs |
| onLeaveToday | boolean | denormalized flag, set by a Cloud Function when a leave for today is approved — powers the Admin "Class Without Faculty" alert |

## 4. `admins/{uid}`
| Field | Type | Notes |
|---|---|---|
| uid | string | |
| accessLevel | string | `super_admin` \| `department_admin` |
| managedDepartment | string | only set for `department_admin` |

---

## 5. `departments/{deptId}`
| Field | Type |
|---|---|
| name | string |
| code | string |
| hodUid | string (FK → faculty) |

## 6. `classrooms/{roomId}`
| Field | Type | Notes |
|---|---|---|
| roomNumber | string | |
| building | string | |
| capacity | number | |
| latitude / longitude | number | center point used for the geofence (Phase 4) |
| geofenceRadiusMeters | number | defaults to `Constants.DEFAULT_GEOFENCE_RADIUS_METERS` |
| wifiSsid | string | secondary verification signal alongside GPS |

## 7. `timetable/{entryId}`
| Field | Type | Notes |
|---|---|---|
| department, section, year | string/number | which class this slot belongs to |
| dayOfWeek | number | 1–7 |
| periodNumber | number | |
| startTime / endTime | string | "09:00" |
| subjectId | string | |
| facultyUid | string | FK → faculty |
| classroomId | string | FK → classrooms |

**Index:** composite index on `(department, section, dayOfWeek)` — this is
the query pattern for "today's timetable" (Student feature) and "current
class" (Faculty feature).

---

## 8. `attendance/{recordId}`
One document per mark. See `AttendanceRecord.java` for the exact field list
(studentUid, subjectId, facultyUid, classroomId, status, method, lat/lng,
withinGeofence, mockLocationFlagged, deviceId, timestamp).

**Indexes:**
- `(studentUid, timestamp DESC)` — attendance history / percentage calc
- `(classroomId, timestamp DESC)` — live classroom occupancy (AI feature)
- `(facultyUid, timestamp DESC)` — faculty attendance reports

**Why a flat collection instead of nesting under `students/{uid}/attendance`:**
subcollections would make cross-student queries (e.g. "everyone absent in
Room 204 right now" for Live Classroom Monitoring) impossible without a
collection-group query per classroom. A flat top-level collection with
composite indexes supports every query pattern in the spec with a single
model.

## 9. `leave_requests/{requestId}`
| Field | Type |
|---|---|
| requesterUid | string |
| requesterRole | string (`student` \| `faculty`) |
| fromDate / toDate | timestamp |
| reason | string |
| status | `pending` \| `approved` \| `rejected` |
| approverUid | string |
| createdAt | timestamp |

## 10. `class_swap_requests/{requestId}`
| Field | Type |
|---|---|
| requestingFacultyUid | string |
| targetFacultyUid | string |
| timetableEntryId | string |
| status | `pending` \| `approved` \| `rejected` |

## 11. `notifications/{notificationId}`
| Field | Type |
|---|---|
| recipientUid | string (or `"role:student"` / `"role:faculty"` for broadcast) |
| title / body | string |
| type | `attendance` \| `alert` \| `reminder` \| `emergency` |
| read | boolean |
| createdAt | timestamp |

## 12. `geofences/{geofenceId}`
| Field | Type |
|---|---|
| classroomId | string |
| latitude / longitude / radiusMeters | number |
| active | boolean |

## 13. `audit_logs/{logId}`
Append-only. Never updated or deleted by client code (Security Rules in
Phase 2 will enforce create-only, no update/delete).

| Field | Type |
|---|---|
| actorUid | string |
| action | string, e.g. `"LOGIN"`, `"ATTENDANCE_MARKED"`, `"ROOT_DETECTED"` |
| targetId | string | optional, e.g. the attendance record affected |
| metadata | map | free-form details |
| timestamp | timestamp |

## 14. `risk_scores/{studentUid}`
Written only by Cloud Functions (Phase 6 AI engine), never by the client.

| Field | Type |
|---|---|
| score | number, 0–100 |
| factors | array\<string\> | e.g. `["repeated_late_entry", "mock_location_detected"]` |
| lastUpdated | timestamp |

## 15. `face_embeddings/{uid}`
Stores the ML Kit face landmark vector used for match comparison (Phase 3).
Deliberately kept in its own collection with tighter Security Rules than
`students/*`, since biometric data needs stricter access control than a
roll number.

## 16. `device_bindings/{uid}`
| Field | Type |
|---|---|
| deviceId | string |
| boundAt | timestamp |
| previousDeviceIds | array\<string\> | history, for admin review when a student requests a device change |

## 17. `substitute_recommendations/{recommendationId}`
Written by the AI engine (Phase 6) when a faculty leave is approved for a
slot that has a scheduled class.

| Field | Type |
|---|---|
| timetableEntryId | string |
| absentFacultyUid | string |
| recommendedFacultyUid | string |
| reasonScore | map | e.g. `{ "sameDept": true, "freeAtThatPeriod": true, "workloadRank": 3 }` |

---

## Entity-relationship summary

```
users (1) ──< role determines >── students / faculty / admins (1:1)
departments (1) ──< faculty, students, classrooms
classrooms (1) ──< timetable entries, geofences
timetable (1) ──< attendance records (many, one per day held)
students (1) ──< attendance records (many)
students/faculty (1) ──< leave_requests (many)
faculty (1) ──< class_swap_requests (many)
users (1) ──< notifications (many)
users (1) ──< audit_logs (many, write-only)
students (1) ──1── risk_scores
students (1) ──1── face_embeddings
students (1) ──1── device_bindings
```

Firestore Security Rules enforcing all of the above (e.g. "a student can
only read their own `attendance` docs; only a Cloud Function service
account can write `risk_scores`") are delivered in **Phase 2**, alongside
Authentication, since rules depend on custom auth claims set at signup.
