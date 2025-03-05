package com.senior25.tzakar.ui.presentation.screen.main.notification_history

import com.senior25.tzakar.data.local.model.notification.NotificationModel

data class NotificationHistoryPageData(
    val notifications: MutableList<NotificationModel>? = null,
){
    companion object{
        fun updatePageData(notifications: List<NotificationModel>?, oldState: NotificationHistoryPageData?): NotificationHistoryPageData {
            return (oldState?: NotificationHistoryPageData()).copy(
                notifications =mutableListOf<NotificationModel>().apply {
                    notifications?.let { addAll(it) }
                },
            )
        }

        fun NotificationHistoryPageData?.checkIfNotContainData():Boolean {
            if (this == null) return true
            return this.notifications.isNullOrEmpty()
        }
    }
}

