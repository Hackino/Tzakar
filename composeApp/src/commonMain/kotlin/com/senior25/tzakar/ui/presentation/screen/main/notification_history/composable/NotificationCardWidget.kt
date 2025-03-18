package com.senior25.tzakar.ui.presentation.screen.main.notification_history.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.senior25.tzakar.data.local.model.notification.NotificationModel
import com.senior25.tzakar.ui.presentation.components.separator.Separator
import com.senior25.tzakar.ui.theme.MyColors
import com.senior25.tzakar.ui.theme.fontH3
import com.senior25.tzakar.ui.theme.fontLink
import com.senior25.tzakar.ui.theme.fontParagraphM
import org.jetbrains.compose.resources.painterResource
import tzakar_reminder.composeapp.generated.resources.Res
import tzakar_reminder.composeapp.generated.resources.ic_arrow_forward_ios

@Composable
fun NotificationCardWidget(
    modifier: Modifier,
    item: NotificationModel?,
    border:Shape = RoundedCornerShape(8.dp),
    addSpacer:Boolean = true,
    interaction: NotificationItemInteraction? = null
){
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(border)
            .background(MyColors.colorWhite)
            .clickable { interaction?.onNotificationClick(item) }
            .padding(2.dp)

    ) {

        Row(
            modifier = Modifier.fillMaxWidth().padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 10.dp)
            ) {
                Text(
                    text = item?.title?:"",
                    style = fontH3,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Date: " ,
                        style = fontLink,
                        fontSize = 15.sp,
                        textAlign = TextAlign.Start,
                    )
                    Text(
                        text = item?.date + "-" + item?.time,
                        style = fontParagraphM,
                        textAlign = TextAlign.Start,
                    )
                }
            }
            Icon(
                modifier = Modifier.padding(horizontal = 8.dp),
                painter = painterResource(Res.drawable.ic_arrow_forward_ios),
                contentDescription = null,
                tint = MyColors.colorDarkBlue
            )
        }
        Box(modifier.align(Alignment.BottomCenter).padding(horizontal = 16.dp)) {
            if (addSpacer) Separator()
        }
    }
}

interface NotificationItemInteraction{
    fun onNotificationClick(data: NotificationModel?)
}