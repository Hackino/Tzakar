package com.senior25.tzakar.ui.presentation.components.loader

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.unit.dp
import com.senior25.tzakar.ui.theme.MyColors


@Composable
fun LoaderWidget(modifier: Modifier) {
    Box(
        modifier = modifier
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        CustomLoader()
    }
}

@Composable
fun CustomLoader() {
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = ""
    )

    Canvas(
        modifier = Modifier
            .size(50.dp)
    ) {
        drawCustomRing(rotation)
    }
}

private fun DrawScope.drawCustomRing(rotation: Float) {
    val ringSize = size.minDimension
    val ringWidth = ringSize * 0.08f
    val innerRadiusRatio = 3f

    withTransform({
        rotate(rotation, pivot = center)
    }) {
        drawRing(
            size = ringSize,
            ringWidth = ringWidth,
            gradientColors = listOf(
                Color(0xFF6e6e6e),
               MyColors.colorPurple ,
                Color(0xFFc6c6c6)
            ),
            innerRadiusRatio = innerRadiusRatio
        )
    }
}

private fun DrawScope.drawRing(
    size: Float,
    ringWidth: Float,
    gradientColors: List<Color>,
    innerRadiusRatio: Float
) {
    val gradient = Brush.sweepGradient(
        colors = gradientColors,
        center = Offset(size / 2f, size / 2f)
    )

    drawCircle(
        brush = gradient,
        radius = (size / 2f) - (ringWidth / 2),
        center = center,
        style = Stroke(width = ringWidth)
    )
}


@Composable
private fun Preview() {
    LoaderWidget(modifier = Modifier)
}
