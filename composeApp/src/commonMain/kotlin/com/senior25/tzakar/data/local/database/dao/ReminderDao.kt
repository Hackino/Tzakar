package com.senior25.tzakar.data.local.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.senior25.tzakar.data.local.model.reminder.ReminderModel
import androidx.room.Delete
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ReminderDao {

    @Upsert
    suspend fun insert(reminder: ReminderModel): Long

    @Update
    suspend fun update(reminder: ReminderModel)

    @Query("SELECT * FROM reminder_table WHERE id = :id")
    suspend fun getReminderById(id: String): ReminderModel?

    @Delete
    suspend fun delete(reminder: ReminderModel)

    @Query("DELETE FROM reminder_table WHERE id = :id")
    suspend fun deleteById(id: String): Int

    @Query("SELECT * FROM reminder_table")
    suspend fun getAllReminder(): List<ReminderModel>

    @Query("SELECT * FROM reminder_table")
    fun getAllRemindersFlow(): Flow<List<ReminderModel>>
}