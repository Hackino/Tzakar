package com.senior25.tzakar.data.local.database.my_database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.senior25.tzakar.data.local.database.myDatabase.MyDatabase

fun androidDatabaseBuilder(context: Context): RoomDatabase.Builder<MyDatabase>{
    val dbFile = context.applicationContext.getDatabasePath(  "room_db.db")
    return Room. databaseBuilder(
        context,
        dbFile.absolutePath
    )
}