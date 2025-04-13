package com.adrianwitaszak.kmmpermissions.permissions.delegate

import android.Manifest
import android.app.Activity
import android.content.Context
import android.location.LocationManager
import android.os.Build
import android.provider.Settings
import androidx.core.app.ActivityCompat
import com.adrianwitaszak.kmmpermissions.permissions.delegate.PermissionDelegate
import com.adrianwitaszak.kmmpermissions.permissions.model.Permission
import com.adrianwitaszak.kmmpermissions.permissions.model.PermissionState
import com.adrianwitaszak.kmmpermissions.permissions.util.CannotOpenSettingsException
import com.adrianwitaszak.kmmpermissions.permissions.util.checkPermissions
import com.adrianwitaszak.kmmpermissions.permissions.util.openPage
import com.adrianwitaszak.kmmpermissions.permissions.util.providePermissions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

internal class PostNotificationPermissionDelegate(
    private val context: Context,
    private val activity: Lazy<Activity>,
) : PermissionDelegate {


    override fun getPermissionState(): PermissionState {
        return checkPermissions(context, activity, notificationPermission)
    }

    override suspend fun providePermission(onPermissionProvided: () -> Unit) {
        if (notificationPermission.isNotEmpty())
            activity.value.providePermissions(notificationPermission) {
                throw Exception(
                    it.localizedMessage ?: "Failed to request foreground location permission"
                )
            }
    }

    override fun openSettingPage() {
    }
}

internal val notificationPermission: List<String> =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        listOf(Manifest.permission.POST_NOTIFICATIONS,)
    } else { emptyList() }