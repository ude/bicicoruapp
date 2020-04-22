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


-dontwarn com.google.errorprone.anclenotations.**

-keep class com.github.mikephil.chcleararting.** { *; }

-dontwarn okhttp3.**
-dontnote okhttp3.**
-dontwarn okio.**
-dontwarn retrofit2.Platform$Java8
# Dagger
-dontwarn com.google.errorprone.annotations.*
-dontwarn sun.misc.Unsafe
-dontnote sun.misc.Unsafe

-dontwarn kotlin.jvm.internal.Intrinsics
-dontnote kotlin.jvm.internal.Intrinsics
-dontwarn kotlin.reflect.jvm.internal.**
-dontnote kotlin.reflect.jvm.internal.**



-dontwarn org.jetbrains.annotations.**
-keep class kotlin.Metadata { *; }
-dontnote org.jetbrains.annotations.**

-keeppackagenames org.jsoup.nodes
-keep class org.jsoup.**



-keep class com.google.android.gms.** { *; }
-dontwarn com.google.android.gms.**
-dontnote com.google.android.gms.**


## Android architecture components: Lifecycle
# LifecycleObserver's empty constructor is considered to be unused by proguard
-keepclassmembers class * implements android.arch.lifecycle.LifecycleObserver {
    <init>(...);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
    }

# keep Lifecycle State and Event enums values
-keepclassmembers class android.arch.lifecycle.Lifecycle$State { *; }
-keepclassmembers class android.arch.lifecycle.Lifecycle$Event { *; }
# keep methods annotated with @OnLifecycleEvent even if they seem to be unused
# (Mostly for LiveData.LifecycleBoundObserver.onStateChange(), but who knows)
-keepclassmembers class * {
    @android.arch.lifecycle.OnLifecycleEvent *;
}

-keepclassmembers class * implements android.arch.lifecycle.LifecycleObserver {
    <init>(...);
}

-keep class * implements android.arch.lifecycle.LifecycleObserver {
    <init>(...);
}
-keepclassmembers class android.arch.** { *; }
-keep class android.arch.** { *; }
-dontwarn android.arch.**


#Avoid some duplicates
-dontnote org.apache.http.params.HttpParams
-dontnote org.apache.http.params.HttpConnectionParams
-dontnote org.apache.http.params.CoreConnectionPNames
-dontnote org.apache.http.conn.ConnectTimeoutException
-dontnote org.apache.http.conn.scheme.LayeredSocketFactory
-dontnote org.apache.http.conn.scheme.HostNameResolver
-dontnote org.apache.http.conn.scheme.SocketFactory
-dontnote android.net.http.SslCertificate$DName
-dontnote android.net.http.SslError
-dontnote android.net.http.SslCertificate
-dontnote android.net.http.HttpResponseCache

-keep class com.udepardo.bicicoru.data.model.** { *; }
-keep class com.udepardo.bicicoru.domain.model.** { *; }

-dontwarn android.arch.util.paging.CountedDataSource
-dontwarn android.arch.persistence.room.paging.LimitOffsetDataSource

## Joda Time 2.3

-dontwarn org.joda.convert.**
-dontwarn org.joda.time.**
-keep class org.joda.time.** { *; }
-keep interface org.joda.time.** { *; }



