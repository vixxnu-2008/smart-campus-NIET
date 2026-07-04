# Keep Firebase model classes (Firestore uses reflection to (de)serialize POJOs)
-keepclassmembers class com.smartcampus.app.models.** {
    *;
}

# Firebase
-keep class com.google.firebase.** { *; }
-dontwarn com.google.firebase.**

# ML Kit
-keep class com.google.mlkit.** { *; }

# ZXing
-keep class com.google.zxing.** { *; }
-keep class com.journeyapps.barcodescanner.** { *; }

# Keep security utils - do not obfuscate detection logic away accidentally
-keep class com.smartcampus.app.utils.SecurityUtils { *; }
