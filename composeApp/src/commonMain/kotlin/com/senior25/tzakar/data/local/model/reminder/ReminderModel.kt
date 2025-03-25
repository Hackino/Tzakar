package com.senior25.tzakar.data.local.model.reminder

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.senior25.tzakar.data.local.model.gender.GenderModel
import com.senior25.tzakar.data.local.model.notification.NotificationModel
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
    var isCompleted:Boolean? = null,
    val dateTimeEpoch:Long? =null,
    val sound:String? = null
)

fun ReminderModel.toNotificationModel():NotificationModel{
    return NotificationModel(
        id = "$id-1",
        title = title,
        body = description,
        date = date,
        time = time,
        referenceId = id,
        dateTimeEpoch = dateTimeEpoch,
        sound = sound
    )
}

object RingTones{
    fun getAllRingTones()= listOf(
        "sound_1",
        "sound_2",
        "sound_3",
        "sound_4",
        "sound_5",
        "sound_6",
        "sound_7",
        "sound_8",
        "sound_9",
        "sound_10",
        "sound_11",
        "sound_12",
        )
}