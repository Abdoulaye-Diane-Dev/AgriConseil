package com.odc.agri_conseil.View

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onFinished: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(10000)
        onFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF87CEEB),
                        Color(0xFFFFE0B2),
                        Color(0xFF2E7D32)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            LeafLogo()
            Spacer(modifier = Modifier.height(16.dp))
            Text("Agri-Conseil", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1B5E20))
            Spacer(modifier = Modifier.height(8.dp))
            Text("Des conseils aujourd'hui,", fontSize = 14.sp, color = Color(0xFF1B5E20))
            Text("une meilleure récolte demain", fontSize = 14.sp, color = Color(0xFF1B5E20))
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 48.dp)
        ) {
            CircularProgressIndicator(color = Color.White)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Chargement...", fontSize = 14.sp, color = Color.White)
        }
    }
}

@Composable
private fun LeafLogo() {
    Canvas(modifier = Modifier.size(72.dp)) {
        val w = size.width
        val h = size.height

        val leaf1 = Path().apply {
            moveTo(w * 0.5f, h * 0.9f)
            quadraticTo(w * 0.1f, h * 0.6f, w * 0.35f, h * 0.15f)
            quadraticTo(w * 0.55f, h * 0.5f, w * 0.5f, h * 0.9f)
            close()
        }
        drawPath(leaf1, color = Color(0xFF43A047))

        val leaf2 = Path().apply {
            moveTo(w * 0.5f, h * 0.9f)
            quadraticTo(w * 0.95f, h * 0.55f, w * 0.7f, h * 0.05f)
            quadraticTo(w * 0.45f, h * 0.45f, w * 0.5f, h * 0.9f)
            close()
        }
        drawPath(leaf2, color = Color(0xFF2E7D32))
    }
}