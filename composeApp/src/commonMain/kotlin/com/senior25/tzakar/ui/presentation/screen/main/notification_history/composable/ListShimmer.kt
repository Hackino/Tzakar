package com.senior25.tzakar.ui.presentation.screen.main.notification_history.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.senior25.tzakar.ui.presentation.screen.common.composable.utils.shimmer
import com.senior25.tzakar.ui.presentation.screen.common.composable.utils.shimmerBackground

@Composable
fun ListShimmer(radius:Int = 10) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 2000.dp)
        ,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(10){
            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
                Box(
                    Modifier
                        .height(60.dp)
                        .width(60.dp)
                        .clip(RoundedCornerShape(radius.dp))
                        .background(color = shimmerBackground)
                        .shimmer()
                )
                Spacer(modifier = Modifier.width(8.dp))

                Column(modifier = Modifier.
                fillMaxWidth(),
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        Modifier
                            .width(100.dp)
                            .height(20.dp)
                            .background(color = shimmerBackground)
                            .shimmer()
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Box(
                        Modifier
                            .width(250.dp)
                            .height(20.dp)
                            .background(color = shimmerBackground)
                            .shimmer()
                    )
                }
            }

        }
    }
}


