# Phase 1 — Project Foundation & Core Architecture

## What this phase delivers
A project that **opens, syncs, and runs in Android Studio right now**,
showing Splash → Role Selection, with the full MVVM + Repository skeleton,
Firebase wiring, encrypted local storage, baseline device-security checks,
and the complete Firestore database design that every later phase builds on.

Nothing here talks to a real backend yet — that starts in Phase 2 — but
every class Phase 2+ needs to plug into already exists.

---

## Full phase roadmap (for context — we are building Phase 1 only, right now)

| Phase | Delivers |
|---|---|
| **1 (this one)** | Project setup, architecture, Firestore schema, base classes |
| 2 | Firebase Auth (Student/Faculty/Admin login+registration), Firestore Security Rules, custom claims |
| 3 | ML Kit Face Registration + Face Recognition + Liveness Detection |
| 4 | Dynamic QR Attendance, Geofencing, GPS/Mock-location verification, continuous presence monitoring service |
| 5 | Timetable, notifications (FCM), leave requests, class swap |
| 6 | AI Analytics Engine (risk score, absence prediction, substitute recommendation) — Cloud Functions |
| 7 | Admin Web Dashboard (separate web app) |
| 8 | Reports (PDF/Excel export), Audit Log viewer, polish + dark mode pass |
| 9 | Full documentation package (abstract, diagrams, IEEE paper, PPT, viva Q&A) |

---

## File-by-file explanation

### Build configuration
| File | Purpose |
|---|---|
| `settings.gradle` | Declares the `app` module and plugin repositories |
| `build.gradle` (root) | Declares the AGP + Google Services plugin versions |
| `gradle.properties` | JVM args, AndroidX flags |
| `app/build.gradle` | Every dependency for the **entire** project (all phases) is declared now so you only run one Gradle sync — see inline comments marking which phase uses each block |
| `app/proguard-rules.pro` | Keeps Firebase/ML Kit/ZXing classes from being stripped in release builds |

### Manifest & security config
| File | Purpose |
|---|---|
| `AndroidManifest.xml` | Registers the app class + Splash/RoleSelection activities; declares every permission the finished app needs (camera, location, foreground service, notifications) |
| `res/xml/network_security_config.xml` | Blocks all cleartext (HTTP) traffic app-wide |
| `res/xml/data_extraction_rules.xml` | Excludes local data from Android backup/transfer (biometric + attendance data must never leave the device via backup) |

### Core architecture (`java/com/smartcampus/app/`)
| File | Purpose |
|---|---|
| `SmartCampusApplication.java` | Initializes Firebase, App Check (Play Integrity), and encrypted preferences on app start |
| `base/BaseActivity.java` | Shared toast helpers + `isDeviceTrusted()` security gate every sensitive screen calls |
| `base/BaseFragment.java` | Shared toast helpers for fragments |
| `base/BaseViewModel.java` | Shared `isLoading` / `errorMessage` LiveData every ViewModel exposes |
| `data/Result.java` | Generic success/error wrapper returned by every Repository method |
| `data/RepositoryCallback.java` | Callback interface — decouples ViewModels from Firebase types entirely |
| `firebase/FirebaseManager.java` | Single access point for Auth/Firestore/Storage instances; enables Firestore offline persistence |
| `firebase/FirestoreCollections.java` | String constants for every Firestore collection name |
| `utils/Constants.java` | Roles, pref keys, intent extras, attendance status values, misc config |
| `utils/PreferenceManager.java` | AES-256 encrypted SharedPreferences wrapper (Jetpack Security) |
| `utils/NetworkUtils.java` | Connectivity check for the "Offline Sync" banner |
| `utils/SecurityUtils.java` | Root detection, suspicious-developer-mode detection, mock-location check |
| `utils/DeviceUtils.java` | Stable per-device ID (ANDROID_ID) for the Device Binding feature |
| `models/User.java` | Base account model → `users/{uid}` |
| `models/Student.java` | → `students/{uid}` |
| `models/Faculty.java` | → `faculty/{uid}` |
| `models/Admin.java` | → `admins/{uid}` |
| `models/AttendanceRecord.java` | → `attendance/{recordId}` |
| `ui/splash/SplashActivity.java` | Launcher screen, brief brand delay, routes to role selection |
| `ui/roleselect/RoleSelectionActivity.java` | Student/Faculty/Admin picker; TODO marks the Phase 2 hand-off point |

### Resources
`res/values/{strings,colors,themes}.xml`, `res/values-night/colors.xml` (dark mode palette), `res/layout/{activity_splash,activity_role_selection}.xml`, adaptive launcher icon (`res/drawable/ic_launcher_*.xml` + `res/mipmap-anydpi-v26/`).

### Docs
`docs/FIRESTORE_SCHEMA.md` — full database design (17 collections, fields, indexes, relationships).

---

## How to run

1. **Open in Android Studio** (Hedgehog 2023.1.1 or newer): `File → Open` → select the `SmartCampusSecurity` folder.
2. **Create a Firebase project**: go to the [Firebase Console](https://console.firebase.google.com), create a project, then add an Android app with package name `com.smartcampus.app`.
3. **Download `google-services.json`** from that Firebase app and place it at `app/google-services.json` (this file is git-ignored on purpose — it's project-specific and shouldn't be committed to a shared repo).
4. In the Firebase Console, enable:
   - **Authentication** → Email/Password provider (used starting Phase 2)
   - **Firestore Database** → create in production mode, any region
   - **App Check** → register the app with the **Play Integrity** provider (used starting Phase 2; safe to skip for now, `SmartCampusApplication` catches the failure gracefully during local dev)
5. **Gradle Sync**: Android Studio will prompt automatically, or `File → Sync Project with Gradle Files`.
6. **Run** on an emulator or device with **API 26+** (minSdk is 26 because face recognition + biometric-adjacent APIs used from Phase 3 need it).

You should see: Splash screen (1.2s) → Role Selection screen with three cards. Tapping a card shows a toast confirming the role — that's the expected Phase 1 behavior; Login screens arrive in Phase 2.

---

## Testing procedure for this phase

Since there's no backend logic yet, Phase 1 testing is structural:

1. **Build check**: `./gradlew assembleDebug` should complete with no errors (once `google-services.json` is in place — the build will fail without it, since the Google Services Gradle plugin requires it).
2. **Manifest check**: confirm all 3 activities launch without a `ClassNotFoundException` (a common typo source when adding new activities by hand).
3. **Encrypted prefs check**: run the app, then pull the shared_prefs XML via `adb shell run-as com.smartcampus.app.debug cat /data/data/.../shared_prefs/smart_campus_secure_prefs.xml` — you should see ciphertext, not readable key/value pairs.
4. **Dark mode check**: toggle system dark mode (Settings → Display) and confirm the Role Selection screen switches to the dark palette (`values-night/colors.xml`) automatically.
5. **Root/dev-mode check**: run `SecurityUtils.isRootDetected(context)` on an emulator (should be `false` on a standard AVD) and on a rooted test device or Magisk-patched AVD image if you have one (should be `true`).

---

## Common errors & fixes

| Error | Cause | Fix |
|---|---|---|
| `File google-services.json is missing` | You haven't downloaded it from Firebase Console yet | Step 3 above — must be at `app/google-services.json` exactly |
| `Duplicate class ... found in modules` | Mismatched Firebase library versions | Don't add extra `firebase-*` dependencies without going through the BoM (`platform('com.google.firebase:firebase-bom:...')`) — the BoM already pins compatible versions |
| `Unresolved reference: ActivitySplashBinding` | View Binding class not generated yet | Rebuild the project (`Build → Rebuild Project`) — binding classes are generated from layout XML file names at build time, not visible until first build |
| App crashes immediately with `IllegalStateException: Could not initialize secure storage` | Emulator/device Keystore is unavailable (rare, some bare-bones emulator images) | Use a standard Google Play emulator image, or a real device |
| `INSTALL_FAILED_OLDER_SDK` | Running on an emulator below API 26 | Create an AVD with API 26+ |
| Gradle sync fails on `com.android.application` version | Old Android Studio / Gradle version | Update Android Studio to Hedgehog+ (bundles a compatible Gradle) |

---

## Next phase (2) preview
Firebase Authentication for all three roles (email/password + registration
forms), Firestore Security Rules (the real server-side enforcement layer
referenced throughout this phase's code comments), custom auth claims for
role-based access, `AuthRepository` + `AuthViewModel`, and the `LoginActivity`
/ `RegisterActivity` screens that `RoleSelectionActivity` currently just
stubs out with a TODO.

**Reply "approved" or "continue" when you're ready for Phase 2 — or let me
know if you want any changes to Phase 1 first (e.g. separate Student/Faculty
APKs instead of one app, a different color palette, additional Firestore
fields, etc.)**
