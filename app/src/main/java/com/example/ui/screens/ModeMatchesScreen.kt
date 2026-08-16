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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.JoinedMatch
import com.example.data.models.Tournament
import com.example.ui.components.TournamentCard

@Composable
fun ModeMatchesScreen(
    modeTitle: String,
    tournaments: List<Tournament>,
    joinedMatches: List<JoinedMatch>,
    onBack: () -> Unit,
    onJoinClick: (Tournament) -> Unit,
    onUpdateRoomCredentials: ((Long, String, String) -> Unit)? = null
) {
    val darkBg = Color(0xFF090D14)
    val cardBg = Color(0xFF131A27)
    val cyanAccent = Color(0xFF00E5FF)
    val fieryRed = Color(0xFFE50914)
    val textMuted = Color(0xFF8FA1B8)

    // Filter matches for this mode strictly
    val modeTournaments = remember(tournaments, modeTitle) {
        if (modeTitle.contains("FREE", ignoreCase = true)) {
            tournaments.filter { it.entryFee == 0 || it.title.contains("Free", ignoreCase = true) }
        } else {
            tournaments.filter {
                it.gameMode.equals(modeTitle, ignoreCase = true) ||
                it.title.contains(modeTitle, ignoreCase = true)
            }
        }
    }

    var selectedSubFilter by remember { mutableStateOf("ALL") }

    val subFilters = listOf(
        "ALL" to "🔥 All $modeTitle",
        "Solo" to "👤 Solo",
        "Duo" to "👥 Duo",
        "Squad" to "🛡️ Squad",
        "Free" to "🎁 Free Entry"
    )

    val filteredList = remember(modeTournaments, selectedSubFilter) {
        val list = when (selectedSubFilter) {
            "ALL" -> modeTournaments
            "Free" -> modeTournaments.filter { it.entryFee == 0 }
            else -> modeTournaments.filter {
                it.title.contains(selectedSubFilter, ignoreCase = true) ||
                it.gameMode.contains(selectedSubFilter, ignoreCase = true)
            }
        }
        list.sortedWith(compareBy<Tournament> { it.startTimestamp }.thenBy { it.id })
    }

    val joinedIds = joinedMatches.map { it.tournamentId }.toSet()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(darkBg)
    ) {
        // Top Bar Header with Back Button
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
                Spacer(modifier = Modifier.width(6.dp))
                Column {
                    Text(
                        text = "$modeTitle Matches",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "FF CLUB BD Dedicated Arena",
                        fontSize = 11.sp,
                        color = cyanAccent
                    )
                }
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFF1E293B))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "${filteredList.size} Matches",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        // Sub Filter Row
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(subFilters) { (key, label) ->
                val isSelected = selectedSubFilter == key
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSelected) fieryRed else cardBg)
                        .border(
                            1.dp,
                            if (isSelected) fieryRed else Color(0xFF1F2B3E),
                            RoundedCornerShape(12.dp)
                        )
                        .clickable { selectedSubFilter = key }
                        .padding(horizontal = 12.dp, vertical = 7.dp)
                ) {
                    Text(
                        text = label,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) Color.White else textMuted
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Match List
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            if (filteredList.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.SportsEsports,
                                contentDescription = null,
                                tint = textMuted,
                                modifier = Modifier.size(52.dp)
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                            Text(
                                text = "এই ফিল্টারে কোনো $modeTitle ম্যাচ নেই",
                                color = Color.White,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "শীঘ্রই নতুন টুর্নামেন্ট যোগ করা হবে!",
                                color = textMuted,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            } else {
                items(filteredList, key = { it.id }) { tournament ->
                    Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
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
}
