# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-keepclasseswithmembernames class * {
 native <methods>;
}

-keep public class * extends android.view.View {
 public <init>(android.content.Context);
 public <init>(android.content.Context, android.util.AttributeSet);
 public <init>(android.content.Context, android.util.AttributeSet, int);
    void set*(***);
    *** get*();
}


-keepclasseswithmembers class * {
 public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
 public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * extends android.app.Activity {
 public void *(android.view.View);
}

## For enumeration classes, see http://proguard.sourceforge.net/manual/examples.html#enumerations
-keepclassmembers enum * {
 public static **[] values();
 public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
 public static final android.os.Parcelable$Creator *;
}

-keepclassmembers class **.R$* {
 public static <fields>;
}

-keepclassmembers class ** {
   public static *** parse(...);
}

-keep class androidx.support.v4.app.** { *; }
-keep class androidx.support.v7.app.** { *; }
-keep class androidx.support.v7.widget.** { *; }

-keep interface androidx.support.v4.app.** { *; }
-keep interface androidx.support.v7.app.** { *; }
-keep interface androidx.support.v7.widget.** { *; }

-keep class org.apache.**
-keep class org.apache.** { *; }

#-keep class com.pataa.sdk.** { *; }

-keep class com.pataa.sdk.AppConstants{ *; }
-keep class com.pataa.sdk.PataaAutoFillView{ *; }
-keep class com.pataa.sdk.GetPataaDetailResponse**{ *; }
-keep class com.pataa.sdk.OnAddress{ *; }
-keep class com.pataa.sdk.User{ *; }
-keep class com.pataa.sdk.Pataa{ *; }

