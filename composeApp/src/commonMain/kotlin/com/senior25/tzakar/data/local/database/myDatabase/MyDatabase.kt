package com.senior25.tzakar.data.local.database.myDatabase

import androidx.room.ConstructedBy
import androidx.room.Database

import androidx.room.RoomDatabase
import com.senior25.tzakar.data.local.database.dao.ReminderDao
import com.senior25.tzakar.data.local.model.reminder.ReminderModel
import androidx.room.RoomDatabaseConstructor
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.senior25.tzakar.data.local.database.dao.NotificationDao
import com.senior25.tzakar.data.local.model.notification.NotificationModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

@Database(
    entities = [
        ReminderModel::class,
        NotificationModel::class,
    ], version = 5
)
@ConstructedBy(MyDatabaseConstructor::class)
abstract class MyDatabase : RoomDatabase() {
    abstract fun reminderDao(): ReminderDao
    abstract fun notificationDao(): NotificationDao
}


@Suppress("NO_ACTUAL_FOR_EXPECT", "EXPECT_AND_ACTUAL_IN_THE_SAME_MODULE")
expect object MyDatabaseConstructor : RoomDatabaseConstructor<MyDatabase>

class CreateDatabase(private val builder:RoomDatabase.Builder<MyDatabase>){
    fun getDatabase(): MyDatabase {
        return builder.fallbackToDestructiveMigration(dropAllTables = true)
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
    }
}