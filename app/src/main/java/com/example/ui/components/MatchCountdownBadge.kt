package com.example.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun MatchCountdownBadge(
    startTimestamp: Long,
    matchTimeText: String = "",
    modifier: Modifier = Modifier
) {
    var currentTime by remember { mutableLongStateOf(System.currentTimeMillis()) }

    LaunchedEffect(startTimestamp) {
        while (true) {
            currentTime = System.currentTimeMillis()
            delay(1000L)
        }
    }

    // If startTimestamp is 0, estimate from a default offset so preview is always live
    val effectiveTarget = if (startTimestamp > 0) startTimestamp else (currentTime + 25 * 60 * 1000L)
    val remainingMillis = effectiveTarget - currentTime

    val (displayText, badgeColor, isUrgent) = when {
        remainingMillis <= 0 -> {
            Triple("🟢 লাইভ / রুম ওপেন", Color(0xFF00E676), false)
        }
        remainingMillis < 60 * 1000L -> {
            val secs = (remainingMillis / 1000).coerceAtLeast(0)
            Triple("⏱️ ${secs}s বাকি", Color(0xFFFF5252), true)
        }
        remainingMillis < 60 * 60 * 1000L -> {
            val mins = remainingMillis / (60 * 1000L)
            val secs = (remainingMillis % (60 * 1000L)) / 1000
            Triple("⏱️ ${mins}m ${secs}s বাকি", Color(0xFFFFD600), true)
        }
        else -> {
            val hours = remainingMillis / (60 * 60 * 1000L)
            val mins = (remainingMillis % (60 * 60 * 1000L)) / (60 * 1000L)
            Triple("⏳ ${hours}h ${mins}m বাকি", Color(0xFF00E5FF), false)
        }
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(badgeColor.copy(alpha = 0.15f))
            .border(1.dp, badgeColor.copy(alpha = 0.5f), RoundedCornerShape(6.dp))
            .padding(horizontal = 7.dp, vertical = 2.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = displayText,
            color = badgeColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
