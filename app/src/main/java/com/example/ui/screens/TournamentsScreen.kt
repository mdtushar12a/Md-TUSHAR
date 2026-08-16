package com.example.ui.screens

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.SportsEsports
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
import com.example.data.models.JoinedMatch
import com.example.data.models.Tournament
import com.example.ui.components.HeroBanner
import com.example.ui.components.TournamentCard
import com.example.ui.theme.FieryRed
import com.example.ui.theme.FlameOrange
import com.example.ui.theme.SlateCardBg
import com.example.ui.theme.SlateCardBorder
import com.example.ui.theme.SlateDarkBg
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSecondary

@Composable
fun TournamentsScreen(
    tournaments: List<Tournament>,
    joinedMatches: List<JoinedMatch>,
    selectedCategory: String,
    onCategorySelect: (String) -> Unit,
    onJoinClick: (Tournament) -> Unit,
    onBackToHome: (() -> Unit)? = null,
    onUpdateRoomCredentials: ((Long, String, String) -> Unit)? = null
) {
    val categories = listOf(
        "ALL" to "🔥 All",
        "Solo" to "👤 Solo",
        "Duo" to "👥 Duo",
        "Squad" to "🛡️ Squad",
        "BR MATCH" to "🪂 BR Match",
        "CS 4 VS 4" to "⚡ CS 4v4",
        "PRO LEAGUE" to "🏆 Pro League",
        "SURVIVAL" to "🌵 Survival",
        "LONE WOLF" to "🐺 Lone Wolf",
        "Free" to "🎁 Free Entry"
    )

    val joinedIds = joinedMatches.map { it.tournamentId }.toSet()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(SlateDarkBg),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        if (onBackToHome != null) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onBackToHome() }
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "← হোম পেজে ফিরে যান",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF00E5FF)
                    )
                }
            }
        }

        // Hero Banner
        item {
            HeroBanner()
        }

        // Category Filter Row
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = "Filter",
                        tint = FlameOrange,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.size(6.dp))
                    Text(
                        text = "Match Categories",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(categories) { (key, label) ->
                        val isSelected = selectedCategory == key
                        Box(
                            modifier = Modifier
                                .testTag("category_chip_$key")
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) FieryRed else SlateCardBg)
                                .border(
                                    1.dp,
                                    if (isSelected) FieryRed else SlateCardBorder,
                                    RoundedCornerShape(12.dp)
                                )
                                .clickable { onCategorySelect(key) }
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = label,
                                fontSize = 13.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else TextSecondary
                            )
                        }
                    }
                }
            }
        }

        // Section Title
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Live & Upcoming Matches",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
                Text(
                    text = "${tournaments.size} Available",
                    fontSize = 12.sp,
                    color = TextMuted
                )
            }
        }

        // Empty state
        if (tournaments.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.SportsEsports,
                            contentDescription = null,
                            tint = TextMuted,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No matches found in this category",
                            color = TextMuted,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        } else {
            items(tournaments, key = { it.id }) { tournament ->
                Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                    TournamentCard(
                        tournament = tournament,
                        isJoined = joinedIds.contains(tournament.id),
                        onJoinClick = onJoinClick,
                        onUpdateRoomCredentials = onUpdateRoomCredentials
                    )
                }
            }
        }
    }
}
