package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R

data class GameModeItem(
    val id: String,
    val title: String,
    val matchCount: String,
    val drawableRes: Int
)

@Composable
fun SelectModeScreen(
    tournaments: List<com.example.data.models.Tournament> = emptyList(),
    onBack: () -> Unit,
    onSelectMode: (GameModeItem) -> Unit
) {
    val darkBackground = Color(0xFF090D14)
    val cyanAccent = Color(0xFF00E5FF)
    val cardBg = Color(0xFF131A27)
    val cardBorder = Color(0xFF1F2B3E)

    fun getCountText(modeTitle: String, defaultFallback: String): String {
        val count = if (modeTitle.contains("FREE", ignoreCase = true)) {
            tournaments.count { it.entryFee == 0 || it.title.contains("Free", ignoreCase = true) }
        } else {
            tournaments.count {
                it.gameMode.equals(modeTitle, ignoreCase = true) || it.title.contains(modeTitle, ignoreCase = true)
            }
        }
        return if (count > 0) "$count matches available" else defaultFallback
    }

    val modes = listOf(
        GameModeItem("br_match", "BR MATCH", getCountText("BR MATCH", "56 matches available"), R.drawable.img_mode_br_match_1786567370481),
        GameModeItem("pro_league", "PRO LEAGUE", getCountText("PRO LEAGUE", "23 matches available"), R.drawable.img_mode_pro_league_1786567385498),
        GameModeItem("survival", "SURVIVAL", getCountText("SURVIVAL", "35 matches available"), R.drawable.img_mode_survival_1786567398863),
        GameModeItem("cs_4vs4", "CS 4 VS 4", getCountText("CS 4 VS 4", "73 matches available"), R.drawable.img_mode_cs_4vs4_1786567414099),
        GameModeItem("clash_squad", "CLASH SQUAD", getCountText("CLASH SQUAD", "57 matches available"), R.drawable.img_mode_clash_squad_1786567428560),
        GameModeItem("lone_wolf", "LONE WOLF", getCountText("LONE WOLF", "137 matches available"), R.drawable.img_mode_lone_wolf_1786567439697),
        GameModeItem("headshot_only", "HEADSHOT ONLY", getCountText("HEADSHOT ONLY", "42 matches available"), R.drawable.img_mode_headshot_only_1786567451076),
        GameModeItem("lose_to_win", "LOSE TO WIN", getCountText("LOSE TO WIN", "29 matches available"), R.drawable.img_mode_lose_to_win_1786567464388),
        GameModeItem("free_match", "FREE MATCH", getCountText("FREE MATCH", "18 matches available"), R.drawable.img_mode_free_match_1786567792695)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(darkBackground)
    ) {
        // Top Bar: Back Button, Title "Select Mode", Right Play Button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Select Mode",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(cyanAccent),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Play",
                    tint = Color.Black,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // 2-Column Grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 12.dp, end = 12.dp, top = 4.dp, bottom = 80.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(modes, key = { it.id }) { mode ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(cardBg)
                        .border(1.dp, cardBorder, RoundedCornerShape(14.dp))
                        .clickable { onSelectMode(mode) }
                ) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        // Top Image Area
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(125.dp)
                        ) {
                            Image(
                                painter = painterResource(id = mode.drawableRes),
                                contentDescription = mode.title,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )

                            // Subtle bottom gradient overlay
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Brush.verticalGradient(
                                            colors = listOf(
                                                Color.Transparent,
                                                Color.Black.copy(alpha = 0.7f)
                                            )
                                        )
                                    )
                            )
                        }

                        // Bottom Text Area
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(55.dp)
                                .background(Color(0xFF101623))
                                .padding(horizontal = 8.dp, vertical = 6.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = mode.title,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White,
                                textAlign = TextAlign.Center,
                                maxLines = 1
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = mode.matchCount,
                                fontSize = 11.sp,
                                color = Color(0xFF8FA1B8),
                                textAlign = TextAlign.Center,
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }
    }
}
