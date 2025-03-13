package com.senior25.tzakar.di

import com.senior25.tzakar.data.local.database.myDatabase.CreateDatabase
import com.senior25.tzakar.data.local.database.myDatabase.MyDatabase
import com.senior25.tzakar.data.local.database.my_database.iosDatabaseBuilder
import org.koin.dsl.module

actual fun platformModule()= module {
    single<MyDatabase> {
        val builder = iosDatabaseBuilder()
        CreateDatabase(builder).getDatabase()
    }

}