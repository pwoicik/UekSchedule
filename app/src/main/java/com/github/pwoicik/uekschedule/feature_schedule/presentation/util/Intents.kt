package com.github.pwoicik.uekschedule.feature_schedule.presentation.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.github.pwoicik.uekschedule.feature_schedule.common.Constants

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

fun Context.openComposeEmailToSupport() {
    val sendEmailIntent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:")
        putExtra(Intent.EXTRA_EMAIL, Constants.CONTACT_EMAIL)
    }
    startActivity(sendEmailIntent)
}
