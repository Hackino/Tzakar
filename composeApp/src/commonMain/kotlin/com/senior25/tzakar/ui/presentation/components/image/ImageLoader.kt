package com.senior25.tzakar.ui.presentation.components.image

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil3.compose.rememberAsyncImagePainter
import com.seiko.imageloader.EmptyPainter
import com.seiko.imageloader.ImageLoader
import com.seiko.imageloader.LocalImageLoader
import com.seiko.imageloader.component.setupDefaultComponents
import com.seiko.imageloader.intercept.bitmapMemoryCacheConfig
import com.seiko.imageloader.rememberImagePainter
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource


@Composable
fun LoadMediaImage(modifier: Modifier, url:String?, default:DrawableResource?) {
//    val painter = rememberAsyncImagePainter(
//        model = default,
//        placeholder = default?.let { painterResource( it) },
//        error = default?.let { painterResource( it) }
//    )
//    CompositionLocalProvider(
//        LocalImageLoader provides remember { generateImageLoader() },
//    ) {
        val final = rememberImagePainter(url = url?:"", placeholderPainter = {default?.let { painterResource(it)}?:EmptyPainter})

        Image(
            painter = final,
            contentDescription = null,
            modifier = modifier,
            contentScale = ContentScale.Crop
        )

//    val imagePainter = asyncPainterResource(data = url ?: "")
//
//    KamelImage(
//        resource = imagePainter,
//        contentDescription = null,
//        modifier = modifier,
//        contentScale = ContentScale.Crop,
//        onFailure = {
//            // Handle error state, fallback to default if needed
//        }
//    )
//    }
}

expect fun generateImageLoader(): ImageLoader