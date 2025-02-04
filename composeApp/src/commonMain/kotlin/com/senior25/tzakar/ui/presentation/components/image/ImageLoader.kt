package com.senior25.tzakar.ui.presentation.components.image

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil3.compose.rememberAsyncImagePainter
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource


@Composable
fun LoadMediaImage(modifier: Modifier, url:String?, default:DrawableResource?) {
    val painter = rememberAsyncImagePainter(
        model = url,
        placeholder = default?.let { painterResource( it) },
        error = default?.let { painterResource( it) }
    )

    Image(
        painter = painter,
        contentDescription = null,
        modifier = modifier,
        contentScale = ContentScale.Crop
    )
}
