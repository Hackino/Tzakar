package com.senior25.tzakar.helper.notification

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import com.senior25.tzakar.data.local.model.notification.NotificationModel
import com.senior25.tzakar.data.local.preferences.NotificationStatus
import com.senior25.tzakar.data.local.preferences.SharedPref
import com.senior25.tzakar.domain.MainRepository
import com.senior25.tzakar.ktx.decodeJson
import com.senior25.tzakar.ktx.encodeToJson
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.memScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import network.chaintech.kmp_date_time_picker.utils.now
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import platform.AVFAudio.AVAudioPlayer
import platform.Foundation.NSBundle
import platform.Foundation.NSFileManager
import platform.Foundation.NSLog
import platform.Foundation.NSURL
import platform.UserNotifications.UNAuthorizationOptionAlert
import platform.UserNotifications.UNAuthorizationOptionBadge
import platform.UserNotifications.UNAuthorizationOptionSound
import platform.UserNotifications.UNAuthorizationStatusAuthorized
import platform.UserNotifications.UNMutableNotificationContent
import platform.UserNotifications.UNNotification
import platform.UserNotifications.UNNotificationPresentationOptionAlert
import platform.UserNotifications.UNNotificationPresentationOptionSound
import platform.UserNotifications.UNNotificationPresentationOptions
import platform.UserNotifications.UNNotificationRequest
import platform.UserNotifications.UNNotificationResponse
import platform.UserNotifications.UNNotificationSound
import platform.UserNotifications.UNTimeIntervalNotificationTrigger
import platform.UserNotifications.UNUserNotificationCenter
import platform.UserNotifications.UNUserNotificationCenterDelegateProtocol
import platform.darwin.NSObject

actual object NotificationHelper: KoinComponent {

    private val mainRepository: MainRepository by inject()

    actual fun showNotification(notificationModel:NotificationModel) {

        val canterExternal = UNUserNotificationCenter.currentNotificationCenter()

        canterExternal.getPendingNotificationRequestsWithCompletionHandler { requests ->
            val notificationRequests = requests?.filterIsInstance<UNNotificationRequest>()

            val existingRequest = notificationRequests?.firstOrNull { it.identifier == notificationModel.referenceId }

            if (existingRequest == null) {
                val content = UNMutableNotificationContent().apply {
                    this.setTitle(notificationModel.title ?: "")
                    this.setBody(notificationModel.body ?: "")
//                    println("sound of ${notificationModel.title} ${notificationModel.sound}")

//                    val bundle = NSBundle.mainBundle
//                    val path = bundle.URLForResource("sound_1", "wav")
//                    listBundleFiles()
//                    if (path != null) {
//                        println("playing")
//                        val url = path.path?.let { NSURL.fileURLWithPath(it) }
//                        val avAudioPlayer = url?.let { AVAudioPlayer(it, error = null) }
//                        avAudioPlayer?.prepareToPlay()
//                        avAudioPlayer?.play()
//                    } else {
//                        println("Sound file NOT found in bundle")
//                    }

                    notificationModel.sound?.let {
                        this.setSound(UNNotificationSound.soundNamed(it))
                    } ?: this.setSound(UNNotificationSound.defaultSound())

                    this.setUserInfo(mapOf("notificationData" to notificationModel.encodeToJson()))
                }

                val currentTime = LocalDateTime.now().toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
                val  time = (notificationModel.dateTimeEpoch?.let { (currentTime - it)/1000.0 }?:60.0).let {
                    if (it<0) it * -1 else it
                }

                val trigger = UNTimeIntervalNotificationTrigger.triggerWithTimeInterval(time, false)

                val request = UNNotificationRequest.requestWithIdentifier(
                    notificationModel.referenceId ?: "",
                    content,
                    trigger
                )

                val centerInternal = UNUserNotificationCenter.currentNotificationCenter()

                centerInternal.delegate = object : NSObject(), UNUserNotificationCenterDelegateProtocol {

                    override fun userNotificationCenter(
                        center: UNUserNotificationCenter,
                        willPresentNotification: UNNotification,
                        withCompletionHandler: (UNNotificationPresentationOptions) -> Unit
                    ) {

                        val notificationData = willPresentNotification.request.content.userInfo

                        val model = notificationData["notificationData"]?.toString()
                            ?.decodeJson(NotificationModel())

                        CoroutineScope(Dispatchers.Default).launch {
                            if (model != null) mainRepository.insertNotification(model)
                        }
                        withCompletionHandler(UNNotificationPresentationOptionAlert or UNNotificationPresentationOptionSound)
                    }

                    override fun userNotificationCenter(
                        center: UNUserNotificationCenter,
                        didReceiveNotificationResponse: UNNotificationResponse,
                        withCompletionHandler: () -> Unit
                    ) {
                        withCompletionHandler()
                    }
                }

                if (SharedPref.notificationPermissionStatus == NotificationStatus.ON) {
                    cancelNotification(listOf(request.identifier))
                    centerInternal.addNotificationRequest(request) { error ->
                        if (error != null) println("Error -> $error") else println("Notification sent")
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    fun listBundleFiles() {
        val resourcePath = NSBundle.mainBundle.resourcePath
        if (resourcePath != null) {
            val fileManager = NSFileManager.defaultManager

            val files = fileManager.contentsOfDirectoryAtPath(resourcePath, null)
            if (files != null) {
                files.forEach { println(it) }
            }
        } else {
            println("Error: Resource path is null")
        }
    }

    actual fun isNotificationPermissionGranted(result:(Boolean)->Unit) {
        UNUserNotificationCenter.currentNotificationCenter().getNotificationSettingsWithCompletionHandler { settings ->
            result(settings?.authorizationStatus == UNAuthorizationStatusAuthorized)
        }
    }

    @Composable
    actual fun requestNotificationPermission(onResult: (Boolean) -> Unit) {
        val scope = rememberCoroutineScope()
        LaunchedEffect(Unit) {
            scope.launch(Dispatchers.Main) {
                UNUserNotificationCenter.currentNotificationCenter()
                    .requestAuthorizationWithOptions(
                        options = UNAuthorizationOptionAlert or UNAuthorizationOptionSound or UNAuthorizationOptionBadge
                    ) { granted, error -> scope.launch (Dispatchers.IO) { onResult(granted) } }
            }
        }
    }

    actual fun cancelNotification(ids: List<String>) {
        UNUserNotificationCenter
            .currentNotificationCenter()
            .removePendingNotificationRequestsWithIdentifiers(ids)
    }
}