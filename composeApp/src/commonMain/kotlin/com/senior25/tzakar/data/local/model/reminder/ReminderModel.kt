package com.senior25.tzakar.data.local.model.reminder

data class ReminderModel(
    val id:String? = null,
    val type:Int? = null,
    val title:String? = null,
    val description:String? = null,
    val date:String? = null,
    val time:String? = null,
    val isEnabled:Boolean? = null,

)