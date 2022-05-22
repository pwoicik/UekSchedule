package com.github.pwoicik.uekschedule.presentation.util

import android.app.Activity
import com.google.android.play.core.ktx.launchReview
import com.google.android.play.core.ktx.requestReview
import com.google.android.play.core.review.ReviewManagerFactory
import timber.log.Timber

suspend fun requestReview(activity: Activity) {
    Timber.tag("in-app review").d("initiating")
    val manager = ReviewManagerFactory.create(activity.applicationContext)
    try {
        val reviewInfo = manager.requestReview()
        manager.launchReview(activity, reviewInfo)
    } catch (e: Exception) {
        Timber.tag("in-app review").d(e)
    }
}
