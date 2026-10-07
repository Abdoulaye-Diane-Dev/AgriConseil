package com.odc.agri_conseil.View

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.getValue

@Composable
fun AppHeader(title: String, subtitle: String? = null) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp, bottom = 36.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    SproutIcon(modifier = Modifier.size(26.dp), tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Agri-Conseil", style = MaterialTheme.typography.titleLarge, color = Color.White, fontWeight = FontWeight.Bold)
                }
                PersonIcon(modifier = Modifier.size(26.dp), tint = Color.White)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                title,
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            if (subtitle != null) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.9f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(20.dp)
                .background(
                    MaterialTheme.colorScheme.background,
                    shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                )
        )
    }
}



@Composable
fun AppHeaderSecond(title: String, subtitle: String? = null) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp, bottom = 36.dp)
        ) {
            Text(
                title,
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            if (subtitle != null) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.9f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(20.dp)
                .background(
                    MaterialTheme.colorScheme.background,
                    shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                )
        )
    }
}
/** Dégradé sous l'en-tête, visible seulement quand la liste a défilé. */
@Composable
fun ScrollFadeOverlay(listState: LazyListState, modifier: Modifier = Modifier) {
    val alpha by animateFloatAsState(
        targetValue = if (listState.canScrollBackward) 1f else 0f,
        label = "scrollFadeAlpha"
    )
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(20.dp)
            .graphicsLayer { this.alpha = alpha }
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.background.copy(alpha = 0f)
                    )
                )
            )
    )
}

