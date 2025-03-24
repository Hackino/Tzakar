package com.senior25.tzakar.data.local.model.reminder

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.senior25.tzakar.data.local.model.notification.NotificationModel
import com.senior25.tzakar.platform_specific.utils.generateUUID
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "reminder_table")
data class ReminderModel(
    @PrimaryKey
    val id:String = "",
    val type:Int? = null,
    val title:String? = null,
    val description:String? = null,
    val date:String? = null,
    val time:String? = null,
    val isEnabled:Int? = 0,
    val lastUpdateTimestamp:Long? = 0L,
    val shownTimestamp:Long? = 0L,
    var isCompleted:Boolean? = null
    )

fun ReminderModel.toNotificationModel():NotificationModel{
    return NotificationModel(
        id = generateUUID(),
        title = title,
        body = description,
        date = date,
        time = time,
        referenceId = id
    )
}