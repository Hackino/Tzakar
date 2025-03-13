package com.senior25.tzakar.data.local.database.my_database

import androidx.room.Room
import androidx.room.RoomDatabase
import com.senior25.tzakar.data.local.database.myDatabase.MyDatabase
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

fun iosDatabaseBuilder (): RoomDatabase.Builder<MyDatabase> {
    val dbFilePath = documentDirectory() + "/room_db.db"
    return Room.databaseBuilder<MyDatabase>(dbFilePath)
}

@OptIn( ExperimentalForeignApi::class)
private fun documentDirectory(): String {
    val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null
    )
    return requireNotNull(documentDirectory?.path)
}