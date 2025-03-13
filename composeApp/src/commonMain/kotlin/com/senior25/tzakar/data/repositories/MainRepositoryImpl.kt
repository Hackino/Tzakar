package com.senior25.tzakar.data.repositories

import com.senior25.tzakar.data.local.database.dao.ReminderDao
import com.senior25.tzakar.domain.MainRepository

class MainRepositoryImpl(
    reminderDao: ReminderDao
): MainRepository