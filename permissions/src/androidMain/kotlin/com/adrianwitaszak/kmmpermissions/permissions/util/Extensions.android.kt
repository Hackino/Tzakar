package com.adrianwitaszak.kmmpermissions.permissions.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.adrianwitaszak.kmmpermissions.permissions.model.Permission
import com.adrianwitaszak.kmmpermissions.permissions.model.PermissionState

internal fun Context.openPage(
    action: String,
    newData: Uri? = null,
    onError: (Exception) -> Unit,
) {
    try {
        val intent = Intent(action).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            newData?.let { data = it }
        }
        startActivity(intent)
    } catch (e: Exception) {
        onError(e)
    }
}

internal fun checkPermissions(
    context: Context,
    activity: Lazy<Activity>,
    permissions: List<String>,
): PermissionState {
    permissions.ifEmpty { return PermissionState.GRANTED } // no permissions needed
    val status: List<Int> = permissions.map { context.checkSelfPermission(it) }
    val isAllGranted: Boolean = status.all { it == PackageManager.PERMISSION_GRANTED }
    Log.e("hackinoooo","is  Permission granted ${isAllGranted} ${permissions}")

    if (isAllGranted) return PermissionState.GRANTED

    Log.e("hackinoooo","is  Permission granted ${isAllGranted} ${permissions}")

    val isAllRequestRationale: Boolean = try {
        permissions.all { !activity.value.shouldShowRequestPermissionRationale(it) }
    } catch (t: Throwable) {
        Log.e("hackinoooo","print error for checking")
        t.printStackTrace()
        true
    }
    Log.e("hackinoooo","is rational ${isAllRequestRationale}")

    return if (isAllRequestRationale) PermissionState.NOT_DETERMINED
    else PermissionState.DENIED
}


fun Context.hasPermissions(permissions: List<String>): Boolean {
    return permissions.all {
        ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
    }
}
internal fun Activity.providePermissions(
    permissions: List<String>,
    onError: (Throwable) -> Unit,
) {
    try {
        Log.e("hackinoooo","has permission ${hasPermissions(permissions)}")

        if (!hasPermissions(permissions)) {
            Log.e("hackinoooo","Request Permission")
            Log.e("hackinoooo","activity ${this::class.simpleName}")

            ActivityCompat.requestPermissions(
                this, permissions.toTypedArray(), 100
            )
        }

    } catch (t: Throwable) {
        Log.e("hackinoooo","Request Permission Failed")
        onError(t)
    }
}

internal fun Context.openAppSettingsPage(permission: Permission) {
    openPage(
        action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        newData = Uri.parse("package:$packageName"),
        onError = { throw CannotOpenSettingsException(permission.name) }
    )
}
