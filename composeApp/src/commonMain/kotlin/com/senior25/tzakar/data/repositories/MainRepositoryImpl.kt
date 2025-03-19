package com.senior25.tzakar.data.repositories

import com.senior25.tzakar.data.local.database.dao.NotificationDao
import com.senior25.tzakar.data.local.database.dao.ReminderDao
import com.senior25.tzakar.data.local.model.notification.NotificationModel
import com.senior25.tzakar.data.local.model.reminder.ReminderModel
import com.senior25.tzakar.data.local.model.reminder.toNotificationModel
import com.senior25.tzakar.data.local.preferences.SharedPref
import com.senior25.tzakar.domain.MainRepository
import com.senior25.tzakar.helper.DataBaseReference
import com.senior25.tzakar.helper.notification.NotificationHelper
import com.senior25.tzakar.ktx.decodeJson
import com.senior25.tzakar.ktx.fixStringObjectJson
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.database.database
import io.ktor.util.encodeBase64
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class MainRepositoryImpl(
    private val reminderDao: ReminderDao,
    private val notificationDao: NotificationDao
): MainRepository {

    override suspend fun addReminder(reminderModel: ReminderModel) {
        SharedPref.loggedInEmail?.let {
            val ref = Firebase.database.reference(DataBaseReference.UserProfiles.reference)
                .child(it.encodeBase64())
                .child("reminders")
            ref.child(reminderModel.id).setValue(reminderModel)
        }
        if (reminderModel.isEnabled == true) {
            NotificationHelper.showNotification(reminderModel.toNotificationModel())
        } else {
            NotificationHelper.cancelNotification(listOf(reminderModel.id))
        }
        reminderDao.insert(reminderModel)
    }

    override suspend fun updateReminder(reminderModel: ReminderModel) {
        if (reminderModel.isEnabled == true) {
            NotificationHelper.showNotification(reminderModel.toNotificationModel())
        } else {
            NotificationHelper.cancelNotification(listOf(reminderModel.id))
        }

        reminderDao.update(reminderModel)
    }

    override fun enableReminder(reminderModel: ReminderModel) {}

    override fun getAllNotifications() = notificationDao.getAllNotificationFlow()

    override suspend fun addNotification(notificationModel: NotificationModel) {
        notificationDao.insert(notificationModel)
    }

    override fun getReminderById(reminderId: String?): Flow<ReminderModel?> = flow {
        reminderId?.let { emit(reminderDao.getReminderById(it)) }
    }

    override fun getAllReminders(): Flow<List<ReminderModel>?> = reminderDao.getAllRemindersFlow()

    override suspend fun fetchServerNotifications() {
        SharedPref.loggedInEmail?.let {
            val ref = Firebase.database
                .reference(DataBaseReference.UserProfiles.reference)
                .child(it.encodeBase64())
                .child("Notifications")
            val notificationsString = ref.valueEvents.firstOrNull()?.value
            if (notificationsString != null) {
                val notifications = notificationsString.toString().fixStringObjectJson().decodeJson(hashMapOf<String?, NotificationModel?>())
                insertNotifications(notifications?.map { it.value }?.filterNotNull() ?: emptyList())
                return
            }
        }
    }

    override suspend fun fetchServerReminder() {
        SharedPref.loggedInEmail?.let {
            val ref = Firebase.database
                .reference(DataBaseReference.UserProfiles.reference)
                .child(it.encodeBase64())
                .child("reminders")

            val reminderString = ref.valueEvents.firstOrNull()?.value
            if (reminderString != null) {
                val reminders = reminderString.toString().fixStringObjectJson().apply {
                    println(this)
                }

//                    .decodeJson(hashMapOf<String?, ReminderModel?>())
//                (reminders?.map { it.value }?.filterNotNull() ?: emptyList()).onEach { addReminder(it) }
                return
            }
        }
    }

    override suspend fun insertNotification(notificationModel: NotificationModel) {
        notificationDao.insert(notificationModel)
        SharedPref.loggedInEmail?.let {
            val ref = Firebase.database.reference(DataBaseReference.UserProfiles.reference)
                .child(it.encodeBase64()).child("Notifications")
            ref.child(notificationModel.id).setValue(notificationModel)
        }
    }

    override suspend fun insertNotifications(notifications: List<NotificationModel>) {
        notificationDao.insert(notifications)
        SharedPref.loggedInEmail?.let {
            val ref = Firebase.database.reference(DataBaseReference.UserProfiles.reference)
                .child(it.encodeBase64()).child("Notifications")
            notifications.forEach { data -> ref.child(data.id).setValue(data) }
        }
    }

    override suspend fun deleteNotification(ids: List<String>) {
        CoroutineScope(Dispatchers.IO).launch {
//            ids.onEach { notificationDao.deleteByReferenceId(it)
        }
    }

}