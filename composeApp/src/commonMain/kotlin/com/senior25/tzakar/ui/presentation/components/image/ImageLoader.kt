package com.senior25.tzakar.ui.presentation.components.image

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import com.seiko.imageloader.EmptyPainter
import com.seiko.imageloader.ImageLoader
import com.seiko.imageloader.LocalImageLoader
import com.seiko.imageloader.rememberImagePainter
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource


@Composable
fun LoadMediaImage(modifier: Modifier, url:String?, default:DrawableResource?) {
    CompositionLocalProvider(
        LocalImageLoader provides remember { generateImageLoader() },
    ) {
        val final = rememberImagePainter(
            url = url?:"",
            placeholderPainter = {default?.let { painterResource(it)}?:EmptyPainter},
            errorPainter = {default?.let { painterResource(it)}?:EmptyPainter}
        )
        Image(
            painter = final,
            contentDescription = null,
            modifier = modifier,
            contentScale = ContentScale.Crop
        )
    }
}

expect fun generateImageLoader(): ImageLoader