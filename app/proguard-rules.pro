# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Uncomment this for debbug information
#-dontobfuscate
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# TikXML
-keep class com.tickaroo.tikxml.** { *; }
-keep @com.tickaroo.tikxml.annotation.Xml public class *
-keep class **$$TypeAdapter { *; }

-keepclasseswithmembernames class * {
    @com.tickaroo.tikxml.* <fields>;
}

-keepclasseswithmembernames class * {
    @com.tickaroo.tikxml.* <methods>;
}
