package com.adrianwitaszak.kmmpermissions.permissions.delegate

import com.adrianwitaszak.kmmpermissions.permissions.model.PermissionState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.Foundation.NSThread
import platform.UserNotifications.UNAuthorizationOptionAlert
import platform.UserNotifications.UNAuthorizationOptionBadge
import platform.UserNotifications.UNAuthorizationOptionSound
import platform.UserNotifications.UNAuthorizationStatusAuthorized
import platform.UserNotifications.UNUserNotificationCenter
import kotlin.coroutines.resume

internal class PostNotificationPermissionDelegate: PermissionDelegate {

    override fun getPermissionState(): PermissionState {
        var result: PermissionState? = null
        var isDone = false
        UNUserNotificationCenter.currentNotificationCenter()
            .getNotificationSettingsWithCompletionHandler { settings ->
                result = if (settings?.authorizationStatus == UNAuthorizationStatusAuthorized) {
                    PermissionState.GRANTED
                } else {
                    PermissionState.DENIED
                }
                isDone = true
            }
        while (!isDone) {
            NSThread.sleepForTimeInterval(0.01)
        }
        return result ?: PermissionState.DENIED
    }

    override suspend fun providePermission() {
        suspendCancellableCoroutine { cont ->
            UNUserNotificationCenter.currentNotificationCenter().requestAuthorizationWithOptions(
                    options = UNAuthorizationOptionAlert or UNAuthorizationOptionSound or UNAuthorizationOptionBadge
                ) { granted, error ->
                    cont.resume(Unit)
                }
        }
    }

    override fun openSettingPage() {}

}
