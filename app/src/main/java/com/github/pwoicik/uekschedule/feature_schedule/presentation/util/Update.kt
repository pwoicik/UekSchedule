package com.github.pwoicik.uekschedule.feature_schedule.presentation.util

import android.app.Activity
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.ktx.AppUpdateResult
import com.google.android.play.core.ktx.requestUpdateFlow
import kotlinx.coroutines.flow.*
import timber.log.Timber

suspend fun updateApp(
    activity: Activity,
    onUpdatePending: () -> Unit,
    onUpdateDownloading: (progress: StateFlow<Float>) -> Unit,
    onUpdateFailed: () -> Unit,
    onUpdateDownloaded: (restartApp: suspend () -> Unit) -> Unit
) {
    val log = Timber.tag("app update")
    val manager = AppUpdateManagerFactory.create(activity.applicationContext)
    val downloadProgressFlow = MutableStateFlow(0f)
    manager.requestUpdateFlow()
        .onEach { result ->
            when (result) {
                AppUpdateResult.NotAvailable -> log.d("not available")
                is AppUpdateResult.Available -> {
                    log.d("available version code: ${result.updateInfo.availableVersionCode()}")
                    if (result.updateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                        log.d("can update")
                        result.startFlexibleUpdate(activity, 500)
                    }
                    result.updateInfo.installStatus()
                }
                is AppUpdateResult.InProgress -> {
                    when (result.installState.installStatus()) {
                        InstallStatus.CANCELED -> {
                            log.d("canceled")
                        }
                        InstallStatus.PENDING -> {
                            log.d("pending")
                            onUpdatePending()
                        }
                        InstallStatus.DOWNLOADING -> {
                            log.d("downloading")
                            val progress = result.installState.bytesDownloaded().toFloat() /
                                    result.installState.totalBytesToDownload().toFloat() *
                                    100f
                            downloadProgressFlow.value = progress
                            onUpdateDownloading(downloadProgressFlow)
                        }
                        InstallStatus.FAILED -> {
                            log.d("failed error code: ${result.installState.installErrorCode()}")
                            onUpdateFailed()
                        }
                        else -> {}
                    }
                }
                is AppUpdateResult.Downloaded -> {
                    log.d("downloaded")
                    onUpdateDownloaded { result.completeUpdate() }
                }
            }
        }.catch {
            log.d(it)
        }.collect()
}
