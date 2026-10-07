package com.odc.agri_conseil.View

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun LeafIcon(modifier: Modifier = Modifier, tint: Color = LocalContentColor.current) {
    Canvas(modifier = modifier.size(24.dp)) {
        val w = size.width
        val h = size.height
        val leaf = Path().apply {
            moveTo(w * 0.5f, h * 0.95f)
            cubicTo(w * 0.05f, h * 0.75f, w * 0.1f, h * 0.15f, w * 0.5f, h * 0.05f)
            cubicTo(w * 0.9f, h * 0.15f, w * 0.95f, h * 0.75f, w * 0.5f, h * 0.95f)
            close()
        }
        drawPath(leaf, color = tint)
    }
}

@Composable
fun ParcelleIcon(modifier: Modifier = Modifier, tint: Color = LocalContentColor.current) {
    Canvas(modifier = modifier.size(24.dp)) {
        val w = size.width
        val h = size.height
        val strokeW = w * 0.09f

        drawRoundRect(
            color = tint,
            topLeft = Offset(w * 0.1f, h * 0.15f),
            size = Size(w * 0.8f, h * 0.7f),
            cornerRadius = CornerRadius(w * 0.08f),
            style = Stroke(width = strokeW)
        )
        listOf(0.35f, 0.5f, 0.65f).forEach { fy ->
            drawLine(
                color = tint,
                start = Offset(w * 0.22f, h * fy),
                end = Offset(w * 0.78f, h * fy),
                strokeWidth = strokeW * 0.6f
            )
        }
    }
}

@Composable
fun SproutIcon(modifier: Modifier = Modifier, tint: Color = LocalContentColor.current) {
    Canvas(modifier = modifier.size(24.dp)) {
        val w = size.width
        val h = size.height
        val strokeW = w * 0.09f

        drawLine(color = tint, start = Offset(w * 0.15f, h * 0.85f), end = Offset(w * 0.85f, h * 0.85f), strokeWidth = strokeW, cap = StrokeCap.Round)
        drawLine(color = tint, start = Offset(w * 0.5f, h * 0.85f), end = Offset(w * 0.5f, h * 0.35f), strokeWidth = strokeW, cap = StrokeCap.Round)

        val leftLeaf = Path().apply {
            moveTo(w * 0.5f, h * 0.45f)
            quadraticTo(w * 0.15f, h * 0.35f, w * 0.2f, h * 0.1f)
            quadraticTo(w * 0.45f, h * 0.2f, w * 0.5f, h * 0.45f)
            close()
        }
        drawPath(leftLeaf, color = tint)

        val rightLeaf = Path().apply {
            moveTo(w * 0.5f, h * 0.55f)
            quadraticTo(w * 0.85f, h * 0.45f, w * 0.8f, h * 0.2f)
            quadraticTo(w * 0.55f, h * 0.3f, w * 0.5f, h * 0.55f)
            close()
        }
        drawPath(rightLeaf, color = tint)
    }
}

@Composable
fun PersonIcon(modifier: Modifier = Modifier, tint: Color = LocalContentColor.current) {
    Canvas(modifier = modifier.size(24.dp)) {
        val w = size.width
        val h = size.height

        drawCircle(color = tint, radius = w * 0.18f, center = Offset(w * 0.5f, h * 0.32f))

        val body = Path().apply {
            moveTo(w * 0.2f, h * 0.95f)
            quadraticTo(w * 0.2f, h * 0.55f, w * 0.5f, h * 0.55f)
            quadraticTo(w * 0.8f, h * 0.55f, w * 0.8f, h * 0.95f)
            close()
        }
        drawPath(body, color = tint)
    }
}

@Composable
fun RizIcon(modifier: Modifier = Modifier, tint: Color = LocalContentColor.current) {
    Canvas(modifier = modifier.size(24.dp)) {
        val w = size.width
        val h = size.height
        val strokeW = w * 0.07f

        val tige = Path().apply {
            moveTo(w * 0.35f, h * 0.95f)
            cubicTo(w * 0.4f, h * 0.6f, w * 0.75f, h * 0.5f, w * 0.7f, h * 0.15f)
        }
        drawPath(tige, color = tint, style = Stroke(width = strokeW, cap = StrokeCap.Round))

        val grains = listOf(
            Offset(w * 0.68f, h * 0.18f),
            Offset(w * 0.6f, h * 0.28f),
            Offset(w * 0.72f, h * 0.35f),
            Offset(w * 0.58f, h * 0.43f),
            Offset(w * 0.68f, h * 0.52f),
            Offset(w * 0.55f, h * 0.58f)
        )
        grains.forEach { c ->
            drawOval(
                color = tint,
                topLeft = Offset(c.x - w * 0.07f, c.y - h * 0.045f),
                size = Size(w * 0.14f, h * 0.09f)
            )
        }
    }
}

@Composable
fun ManiocIcon(modifier: Modifier = Modifier, tint: Color = LocalContentColor.current) {
    Canvas(modifier = modifier.size(24.dp)) {
        val w = size.width
        val h = size.height
        val strokeW = w * 0.07f

        drawLine(
            color = tint,
            start = Offset(w * 0.5f, h * 0.45f),
            end = Offset(w * 0.5f, h * 0.2f),
            strokeWidth = strokeW,
            cap = StrokeCap.Round
        )

        listOf(-1f, 0f, 1f).forEach { dir ->
            val lobe = Path().apply {
                moveTo(w * 0.5f, h * 0.2f)
                quadraticTo(
                    w * (0.5f + dir * 0.28f), h * 0.12f,
                    w * (0.5f + dir * 0.22f), h * 0.02f
                )
                quadraticTo(
                    w * (0.5f + dir * 0.1f), h * 0.1f,
                    w * 0.5f, h * 0.2f
                )
                close()
            }
            drawPath(lobe, color = tint)
        }

        val racineGauche = Path().apply {
            moveTo(w * 0.5f, h * 0.45f)
            cubicTo(w * 0.25f, h * 0.5f, w * 0.2f, h * 0.8f, w * 0.38f, h * 0.92f)
            cubicTo(w * 0.5f, h * 1.0f, w * 0.55f, h * 0.65f, w * 0.5f, h * 0.45f)
            close()
        }
        drawPath(racineGauche, color = tint)

        val racineDroite = Path().apply {
            moveTo(w * 0.5f, h * 0.45f)
            cubicTo(w * 0.7f, h * 0.5f, w * 0.78f, h * 0.75f, w * 0.62f, h * 0.85f)
            cubicTo(w * 0.53f, h * 0.92f, w * 0.47f, h * 0.6f, w * 0.5f, h * 0.45f)
            close()
        }
        drawPath(racineDroite, color = tint)
    }
}

@Composable
fun ArachideIcon(modifier: Modifier = Modifier, tint: Color = LocalContentColor.current) {
    Canvas(modifier = modifier.size(24.dp)) {
        val w = size.width
        val h = size.height

        val peanut = Path().apply {
            moveTo(w * 0.5f, h * 0.05f)
            cubicTo(w * 0.85f, h * 0.05f, w * 0.88f, h * 0.32f, w * 0.6f, h * 0.42f)
            cubicTo(w * 0.9f, h * 0.5f, w * 0.9f, h * 0.95f, w * 0.5f, h * 0.95f)
            cubicTo(w * 0.1f, h * 0.95f, w * 0.1f, h * 0.5f, w * 0.4f, h * 0.42f)
            cubicTo(w * 0.12f, h * 0.32f, w * 0.15f, h * 0.05f, w * 0.5f, h * 0.05f)
            close()
        }
        drawPath(peanut, color = tint)
    }
}

@Composable
fun FonioIcon(modifier: Modifier = Modifier, tint: Color = LocalContentColor.current) {
    Canvas(modifier = modifier.size(24.dp)) {
        val w = size.width
        val h = size.height
        val strokeW = w * 0.08f

        drawLine(
            color = tint,
            start = Offset(w * 0.5f, h * 0.95f),
            end = Offset(w * 0.5f, h * 0.4f),
            strokeWidth = strokeW,
            cap = StrokeCap.Round
        )

        val grains = listOf(
            Offset(w * 0.5f, h * 0.12f),
            Offset(w * 0.35f, h * 0.2f),
            Offset(w * 0.65f, h * 0.2f),
            Offset(w * 0.28f, h * 0.34f),
            Offset(w * 0.72f, h * 0.34f),
            Offset(w * 0.5f, h * 0.36f)
        )
        grains.forEach { c ->
            drawCircle(color = tint, radius = w * 0.07f, center = c)
        }
    }
}

@Composable
fun MaisIcon(modifier: Modifier = Modifier, tint: Color = LocalContentColor.current) {
    Canvas(modifier = modifier.size(24.dp)) {
        val w = size.width
        val h = size.height

        val epi = Path().apply {
            moveTo(w * 0.5f, h * 0.05f)
            cubicTo(w * 0.78f, h * 0.08f, w * 0.82f, h * 0.5f, w * 0.65f, h * 0.85f)
            cubicTo(w * 0.58f, h * 0.98f, w * 0.42f, h * 0.98f, w * 0.35f, h * 0.85f)
            cubicTo(w * 0.18f, h * 0.5f, w * 0.22f, h * 0.08f, w * 0.5f, h * 0.05f)
            close()
        }
        drawPath(epi, color = tint)

        listOf(0.28f, 0.42f, 0.56f, 0.70f).forEach { fy ->
            drawLine(
                color = tint.copy(alpha = 0.35f),
                start = Offset(w * 0.3f, h * fy),
                end = Offset(w * 0.7f, h * fy),
                strokeWidth = w * 0.045f
            )
        }
    }
}
