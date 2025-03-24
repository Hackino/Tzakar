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
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.database.database
import io.ktor.util.encodeBase64
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import network.chaintech.kmp_date_time_picker.utils.now

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
        if (reminderModel.isEnabled == 1) {
            val latestDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
            val currentTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).time
            reminderModel.date?.let { dateStr ->
                val parsedDate = LocalDate.parse(dateStr)
                if (parsedDate < latestDate){
                    NotificationHelper.cancelNotification(listOf(reminderModel.id))
                } else if(parsedDate == latestDate){
                    reminderModel.time?.let { timeStr ->
                        val parsedTime = LocalTime.parse(timeStr)
                        if (parsedTime <= currentTime){
                            NotificationHelper.cancelNotification(listOf(reminderModel.id))
                        }else{
                            NotificationHelper.showNotification(reminderModel.toNotificationModel())
                        }
                    }
                }else{
                    NotificationHelper.showNotification(reminderModel.toNotificationModel())

                }
            }
        } else {
            NotificationHelper.cancelNotification(listOf(reminderModel.id))
        }
        reminderDao.insert(reminderModel)
    }

    override suspend fun updateReminder(reminderModel: ReminderModel) {
        if (reminderModel.isEnabled == 1) {
            val latestDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
            val currentTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).time
            reminderModel.date?.let { dateStr ->
                val parsedDate = LocalDate.parse(dateStr)
                if (parsedDate < latestDate){
                    NotificationHelper.cancelNotification(listOf(reminderModel.id))
                } else if(parsedDate == latestDate){
                    reminderModel.time?.let { timeStr ->
                        val parsedTime = LocalTime.parse(timeStr)
                        if (parsedTime <= currentTime){
                            NotificationHelper.cancelNotification(listOf(reminderModel.id))
                        }else{
                            NotificationHelper.showNotification(reminderModel.toNotificationModel())
                        }
                    }
                }else{
                    NotificationHelper.showNotification(reminderModel.toNotificationModel())
                }
            }
        } else {
            NotificationHelper.cancelNotification(listOf(reminderModel.id))
        }
        val currentTime = LocalDateTime.now().toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
        val reminder= reminderModel.copy(lastUpdateTimestamp = currentTime)
        reminderDao.update(reminder)
        withContext(Dispatchers.IO) {
            SharedPref.loggedInEmail?.let {
                val ref = Firebase.database.reference(DataBaseReference.UserProfiles.reference)
                    .child(it.encodeBase64())
                    .child("reminders")
                ref.child(reminder.id).setValue(reminder)
            }
        }
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
            val ref = Firebase.database.reference(DataBaseReference.UserProfiles.reference)
                .child(it.encodeBase64()).child("Notifications")

            val snapshot = ref.valueEvents.firstOrNull()
            val notifications = snapshot?.value<Map<String?, NotificationModel?>>()?.values?.filterNotNull()
            insertNotifications(notifications ?: emptyList())
        }
    }

    override suspend fun fetchServerReminder() {
        SharedPref.loggedInEmail?.let {
            val ref = Firebase.database.reference(DataBaseReference.UserProfiles.reference)
                .child(it.encodeBase64()).child("reminders")

            val snapshot = ref.valueEvents.firstOrNull()
            val reminders = snapshot?.value<Map<String?, ReminderModel?>>()?.values?.filterNotNull()
            reminders?.forEach { reminder ->
                val cached = reminderDao.getReminderById(reminder.id)
                if ( (cached?.lastUpdateTimestamp?:0) >= (reminder.lastUpdateTimestamp?:0)){
                    ref.child(reminder.id).setValue(cached)
                }else{
                    reminderDao.insert(reminder)
                }


                if (reminder.isEnabled == 1) {
                    val latestDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
                    val currentTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).time
                    reminder.date?.let { dateStr ->
                        val parsedDate = LocalDate.parse(dateStr)
                        if (parsedDate < latestDate){
                            NotificationHelper.cancelNotification(listOf(reminder.id))
                        } else if(parsedDate == latestDate){
                            reminder.time?.let { timeStr ->
                                val parsedTime = LocalTime.parse(timeStr)
                                if (parsedTime <= currentTime){
                                    NotificationHelper.cancelNotification(listOf(reminder.id))
                                }else{
                                    NotificationHelper.showNotification(reminder.toNotificationModel())
                                }
                            }
                        }else{
                            NotificationHelper.showNotification(reminder.toNotificationModel())
                        }
                    }
                } else {
                    NotificationHelper.cancelNotification(listOf(reminder.id))
                }
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

    override suspend fun deleteNotification(ids: List<String>) {}

}