package com.senior25.tzakar.data.local.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.senior25.tzakar.data.local.model.reminder.ReminderModel
import androidx.room.Delete

@Dao
interface ReminderDao {

    @Upsert
    suspend fun insert(reminder: ReminderModel): Long

    @Query("SELECT * FROM reminder WHERE id = :reminderId")
    suspend fun getReminderById(reminderId: Long): ReminderModel?

    @Delete
    suspend fun delete(reminder: ReminderModel)

    @Query("DELETE FROM reminder WHERE id = :reminderId")
    suspend fun deleteById(reminderId: Long): Int

    @Query("SELECT * FROM reminder")
    suspend fun getAllUsers(): List<ReminderModel>
}