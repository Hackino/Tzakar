package com.senior25.tzakar.data.local.model.notification

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "notification_table")
data class NotificationModel(
    @PrimaryKey
    val id:String = "",
    var title: String? = null,
    val body:String? =null,
    var isRead:Boolean? =null,
    val redirection:String? =null,
    val tag:String? =null,
    val date:String? =null,
    val time:String? =null,
    val dateTimeEpoch:Long? =null,
    val image:String? =null,
    val sectionTitle:String? = null,
    val sectionType:String? = null,
    val referenceId:String? = null
)