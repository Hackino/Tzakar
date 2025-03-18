package com.senior25.tzakar.data.repositories

import com.senior25.tzakar.data.local.database.dao.NotificationDao
import com.senior25.tzakar.data.local.database.dao.ReminderDao
import com.senior25.tzakar.data.local.model.notification.NotificationModel
import com.senior25.tzakar.data.local.model.reminder.ReminderModel
import com.senior25.tzakar.domain.MainRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

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
}