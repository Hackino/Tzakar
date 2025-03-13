package com.senior25.tzakar.data.local.model.reminder

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reminder")
data class ReminderModel(
    @PrimaryKey(autoGenerate = true) val rowId: Int = 0,
    val id:Long? = null,
    val type:Int? = null,
    val title:String? = null,
    val description:String? = null,
    val date:String? = null,
    val time:String? = null,
    val isEnabled:Boolean? = null
)