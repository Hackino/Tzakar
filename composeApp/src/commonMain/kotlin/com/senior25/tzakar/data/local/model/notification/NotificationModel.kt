package com.senior25.tzakar.data.local.model.notification

data class NotificationModel(
    var id: String? = null,
    var title: String? = null,
    val body:String? =null,
    var isRead:Boolean? =null,
    val redirection:String? =null,
    val tag:String? =null,
    val date:String? =null,
    val time:String? =null,
    val image:String? =null,
    val sectionTitle:String? = null,
    val sectionType:String? = null
)
