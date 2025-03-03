package com.senior25.tzakar.ui.presentation.screen.main.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.senior25.tzakar.data.local.model.profile.UserProfile
import com.senior25.tzakar.ui.presentation.components.image.LoadMediaImage
import com.senior25.tzakar.ui.theme.MyColors
import com.senior25.tzakar.ui.theme.fontHighlight
import com.senior25.tzakar.ui.theme.fontParagraphM
import tzakar_reminder.composeapp.generated.resources.Res
import tzakar_reminder.composeapp.generated.resources.ic_profile_placeholder


@Composable
fun ProfileCard(
    profile: UserProfile?,
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        backgroundColor = MyColors.colorPurple.copy(0.5f),
        elevation = 0.dp,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 120.dp)
            .padding(vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            LoadMediaImage(
                modifier = Modifier
                    .size(56.dp)
                    .background(Color.White, CircleShape)
                    .clip(CircleShape),
                url =profile?.image,
                default = Res.drawable.ic_profile_placeholder
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = profile?.userName?:"",
                    style = fontHighlight,
                    color = MyColors.colorDarkBlue
                )
                Text(
                    text = profile?.email?:"",
                    style = fontParagraphM,
                    color = MyColors.colorDarkBlue
                )
            }
        }
    }
}
