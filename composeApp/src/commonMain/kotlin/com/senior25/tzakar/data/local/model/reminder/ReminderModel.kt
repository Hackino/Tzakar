package com.senior25.tzakar.data.local.model.reminder

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reminder_table")
data class ReminderModel(
    @PrimaryKey
    val id:String = "",
    val type:Int? = null,
    val title:String? = null,
    val description:String? = null,
    val date:String? = null,
    val time:String? = null,
    val isEnabled:Boolean? = null
)