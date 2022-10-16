package com.github.pwoicik.uekschedule.presentation.util

import android.content.Context
import android.content.Intent
import android.net.Uri

fun Context.openInBrowser(url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    startActivity(intent)
}

fun Context.openPlayStorePage() {
    val appPackage = packageName
    val storePageUri = Uri.parse("market://details?id=$appPackage")
    val storePageIntent = Intent(Intent.ACTION_VIEW, storePageUri)
    startActivity(storePageIntent)
}
