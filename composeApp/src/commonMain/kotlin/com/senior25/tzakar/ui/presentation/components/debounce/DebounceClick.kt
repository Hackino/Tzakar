package com.senior25.tzakar.ui.presentation.components.debounce

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.withLink
import com.senior25.tzakar.platform_specific.common.getSystemTimeMillis
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun Modifier.debounceClick(
    onClick: () -> Unit
): Modifier = this.
    pointerInput(Unit) {
        var lastClickTime = 0L
        detectTapGestures(
            onTap = {
                val currentTime = getSystemTimeMillis()
                if (currentTime - lastClickTime >= 700) {
                    onClick()
                    lastClickTime = currentTime
                }
            }
        )
    }


fun AnnotatedString.Builder.withDebounceAction(
    tag: String,
    styles: TextLinkStyles? = null,
    action:()->Unit,
    stringToAppend:String? = ""
): AnnotatedString.Builder {
    var lastClickTime = 0L
    return withLink(
        LinkAnnotation.Clickable(
            tag = tag,
            styles = styles,
            linkInteractionListener = {
                val currentTime = getSystemTimeMillis()
                if (currentTime - lastClickTime >= 700) {
                    action()
                    lastClickTime = currentTime
                }
            }
        )
    ) {
        append(stringToAppend)
    }
}


@Composable
fun rememberDebounceClick(onClick: () -> Unit): () -> Unit {
    val isClickable = remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()
    return {
        if (isClickable.value) {
            onClick()
            isClickable.value = false
            coroutineScope.launch {
                delay(700L)
                isClickable.value = true
            }
        }
    }
}