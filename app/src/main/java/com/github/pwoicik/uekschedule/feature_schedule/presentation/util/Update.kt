package com.github.pwoicik.uekschedule.feature_schedule.presentation.util

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.ktx.AppUpdateResult
import com.google.android.play.core.ktx.requestUpdateFlow
import kotlinx.coroutines.flow.*
import timber.log.Timber

suspend fun Context.updateApp(): Flow<UpdateStatus> {
    val log = Timber.tag("app update")

    val downloadProgressFlow = MutableStateFlow(0f)

    return AppUpdateManagerFactory.create(this.applicationContext)
        .requestUpdateFlow()
        .catch { log.d(it) }
        .mapNotNull { result ->
            var emission: UpdateStatus? = null
            when (result) {
                AppUpdateResult.NotAvailable -> log.d("not available")
                is AppUpdateResult.Available -> {
                    log.d("available version code: ${result.updateInfo.availableVersionCode()}")
                    if (result.updateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                        log.d("can update")
                        result.startFlexibleUpdate(this as AppCompatActivity, 500)
                    }
                    result.updateInfo.installStatus()
                }
                is AppUpdateResult.InProgress -> {
                    when (result.installState.installStatus()) {
                        InstallStatus.CANCELED -> {
                            log.d("canceled")
                            emission = UpdateStatus.Canceled
                        }
                        InstallStatus.PENDING -> {
                            log.d("pending")
                            emission = UpdateStatus.Pending
                        }
                        InstallStatus.DOWNLOADING -> {
                            log.d("downloading")
                            val progress = (result.installState.bytesDownloaded()
                                    / result.installState.totalBytesToDownload()
                                    * 100f)
                            downloadProgressFlow.value = progress
                            emission = UpdateStatus.Downloading(downloadProgressFlow)
                        }
                        InstallStatus.FAILED -> {
                            log.d("failed error code: ${result.installState.installErrorCode()}")
                            emission = UpdateStatus.Failed
                        }
                        else -> {}
                    }
                }
                is AppUpdateResult.Downloaded -> {
                    log.d("downloaded")
                    emission = UpdateStatus.Downloaded { result.completeUpdate() }
                }
            }
            emission
        }
        .distinctUntilChanged { old, new -> old::class == new::class }
}

sealed class UpdateStatus {
    object Pending : UpdateStatus()
    object Canceled : UpdateStatus()
    object Failed : UpdateStatus()
    data class Downloading(val progress: StateFlow<Float>) : UpdateStatus()
    data class Downloaded(val restartApp: suspend () -> Unit) : UpdateStatus()
}
