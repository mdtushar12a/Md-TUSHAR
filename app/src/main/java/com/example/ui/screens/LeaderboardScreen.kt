package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.LeaderboardItem
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.FieryRed
import com.example.ui.theme.FlameOrange
import com.example.ui.theme.GoldYellow
import com.example.ui.theme.SlateCardBg
import com.example.ui.theme.SlateCardBorder
import com.example.ui.theme.SlateDarkBg
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSecondary

@Composable
fun LeaderboardScreen(
    leaders: List<LeaderboardItem>
) {
    val top1 = leaders.find { it.rank == 1 }
    val top2 = leaders.find { it.rank == 2 }
    val top3 = leaders.find { it.rank == 3 }
    val remaining = leaders.filter { it.rank > 3 }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(SlateDarkBg),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp)
    ) {
        item {
            Text(
                text = "Hall of Fame & Leaderboard",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Black,
                color = Color.White
            )
            Text(
                text = "Top Free Fire players in Bangladesh by total earnings",
                fontSize = 12.sp,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(20.dp))
        }

        // Top 3 Podium
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Bottom
            ) {
                // Rank 2 (Silver)
                top2?.let { PodiumCard(item = it, rankColor = Color(0xFFC0C0C0), heightDp = 130) }
                // Rank 1 (Gold)
                top1?.let { PodiumCard(item = it, rankColor = GoldYellow, heightDp = 160) }
                // Rank 3 (Bronze)
                top3?.let { PodiumCard(item = it, rankColor = Color(0xFFCD7F32), heightDp = 110) }
            }
        }

        item {
            Text(
                text = "Top Players Ranking",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        items(remaining, key = { it.id }) { player ->
            Card(
                modifier = Modifier
                    .testTag("leaderboard_row_${player.rank}")
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .border(1.dp, SlateCardBorder, RoundedCornerShape(12.dp)),
                colors = CardDefaults.cardColors(containerColor = SlateCardBg),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(SlateDarkBg),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "#${player.rank}",
                                fontWeight = FontWeight.Bold,
                                color = TextSecondary,
                                fontSize = 13.sp
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = player.playerName,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 14.sp
                            )
                            Text(
                                text = "UID: ${player.playerUid} • ${player.matchesWon} Wins • ${player.totalKills} Kills",
                                fontSize = 11.sp,
                                color = TextMuted
                            )
                        }
                    }

                    Text(
                        text = "৳ ${player.totalEarningsBdt.toInt()} BDT",
                        fontWeight = FontWeight.Black,
                        color = GoldYellow,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun PodiumCard(
    item: LeaderboardItem,
    rankColor: Color,
    heightDp: Int
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(100.dp)
    ) {
        Icon(
            imageVector = Icons.Default.EmojiEvents,
            contentDescription = null,
            tint = rankColor,
            modifier = Modifier.size(28.dp)
        )
        Text(
            text = item.playerName,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            fontSize = 12.sp,
            maxLines = 1
        )
        Text(
            text = "৳ ${item.totalEarningsBdt.toInt()}",
            color = GoldYellow,
            fontWeight = FontWeight.Black,
            fontSize = 11.sp
        )

        Spacer(modifier = Modifier.height(6.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(heightDp.dp)
                .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                .background(rankColor.copy(alpha = 0.2f))
                .border(1.dp, rankColor, RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "#${item.rank}",
                fontSize = 24.sp,
                fontWeight = FontWeight.Black,
                color = rankColor
            )
        }
    }
}
