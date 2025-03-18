package com.senior25.tzakar.data.repositories

import com.senior25.tzakar.data.local.database.dao.NotificationDao
import com.senior25.tzakar.data.local.database.dao.ReminderDao
import com.senior25.tzakar.data.local.model.notification.NotificationModel
import com.senior25.tzakar.data.local.model.reminder.ReminderModel
import com.senior25.tzakar.data.local.preferences.SharedPref
import com.senior25.tzakar.domain.MainRepository
import com.senior25.tzakar.helper.DataBaseReference
import com.senior25.tzakar.ktx.decodeJson
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
): MainRepository{

    override suspend fun addReminder(reminderModel: ReminderModel){
        reminderDao.insert(reminderModel)
    }

    override fun enableReminder(reminderModel: ReminderModel){}

    override fun getAllNotifications() = notificationDao.getAllNotificationFlow()

    override suspend fun addNotification(notificationModel: NotificationModel) {
        notificationDao.insert(notificationModel)
    }

    override fun getReminderById(reminderId: String?): Flow<ReminderModel?>  = flow {
        reminderId?.let { emit(reminderDao.getReminderById(it)) }
    }

    override suspend fun fetchServerNotifications() {
        SharedPref.loggedInEmail?.let {
            val ref = Firebase.database
                .reference(DataBaseReference.UserProfiles.reference)
                .child(it.encodeBase64())
                .child("Notifications")
            val notificationsString = ref.valueEvents.firstOrNull()?.value
            if (notificationsString != null) {
                val notifications =  notificationsString.toString().decodeJson(HashMap<String,NotificationModel>())
                insertNotifications(notifications?.map { it.value }?: emptyList())
                return
            }
        }
    }

    override suspend fun insertNotification(notificationModel: NotificationModel) {
        SharedPref.loggedInEmail?.let {
            val ref = Firebase.database.reference(DataBaseReference.UserProfiles.reference).child(it.encodeBase64()).child("Notifications")
            ref.child(notificationModel.id).setValue(notificationModel)
        }
        notificationDao.insert(notificationModel)
    }

    override suspend fun insertNotifications(notifications: List<NotificationModel>) {
        SharedPref.loggedInEmail?.let {
            val ref = Firebase.database.reference(DataBaseReference.UserProfiles.reference).child(it.encodeBase64()).child("Notifications")
            notifications.forEach {data-> ref.child(data.id).setValue(data) }
        }
        notificationDao.insert(notifications)
    }

    override suspend fun deleteNotification(ids: List<String>) {
        CoroutineScope(Dispatchers.IO).launch{
            ids.onEach { notificationDao.deleteByReferenceId(it) }

        }
    }
}