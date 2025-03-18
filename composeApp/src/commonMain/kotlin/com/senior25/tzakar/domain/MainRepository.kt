package com.senior25.tzakar.domain

import com.senior25.tzakar.data.local.model.notification.NotificationModel
import com.senior25.tzakar.data.local.model.reminder.ReminderModel
import kotlinx.coroutines.flow.Flow

interface MainRepository{
    suspend fun addReminder(reminderModel: ReminderModel)
    fun enableReminder(reminderModel: ReminderModel)
    fun getAllNotifications():Flow<List<NotificationModel>>
    suspend fun addNotification(notificationModel: NotificationModel)

    fun getReminderById(reminderId:String?):Flow<ReminderModel?>

    suspend fun fetchServerNotifications()
    suspend fun insertNotification(notificationModel: NotificationModel)
    suspend fun insertNotifications(notifications: List<NotificationModel>)
    suspend fun deleteNotification(ids: List<String>)

}