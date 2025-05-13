package com.senior25.tzakar.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.senior25.tzakar.data.local.model.notification.NotificationModel
import com.senior25.tzakar.data.local.model.reminder.ReminderModel
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao {

    @Upsert
    suspend fun insert(notification: NotificationModel): Long

    @Upsert
    suspend fun insert(notifications: List<NotificationModel>): List<Long>

    @Delete
    suspend fun delete(notification: ReminderModel)

    @Query("DELETE FROM notification_table WHERE id = :id")
    suspend fun deleteById(id: String): Int


    @Query("DELETE FROM notification_table WHERE referenceId = :referenceId")
    suspend fun deleteByReferenceId(referenceId: String): Int

    @Query("SELECT * FROM notification_table")
    suspend fun getAllNotification(): List<NotificationModel?>

    @Query("SELECT * FROM notification_table")
    fun getAllNotificationFlow(): Flow<List<NotificationModel?>?>
}