package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.MeetingRoom
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.JoinedMatch
import com.example.data.models.Tournament
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.FieryRed
import com.example.ui.theme.FlameOrange
import com.example.ui.theme.GoldYellow
import com.example.ui.theme.SlateCardBg
import com.example.ui.theme.SlateCardBorder
import com.example.ui.theme.SlateDarkBg
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSecondary

import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.foundation.clickable
import com.example.ui.components.AdminSetRoomDialog

@Composable
fun MyMatchesScreen(
    joinedMatches: List<JoinedMatch>,
    tournaments: List<Tournament>,
    onUpdateRoomCredentials: ((Long, String, String) -> Unit)? = null
) {
    val context = LocalContext.current
    val tournamentMap = tournaments.associateBy { it.id }
    var editingTournament by remember { mutableStateOf<Tournament?>(null) }

    if (editingTournament != null) {
        AdminSetRoomDialog(
            tournament = editingTournament!!,
            onDismiss = { editingTournament = null },
            onSave = { roomId, roomPassword ->
                onUpdateRoomCredentials?.invoke(editingTournament!!.id, roomId, roomPassword)
                Toast.makeText(context, "রুম আইডি ও পাসওয়ার্ড সফলভাবে সেভ হয়েছে!", Toast.LENGTH_SHORT).show()
            }
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(SlateDarkBg),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp)
    ) {
        item {
            Text(
                text = "My Registered Matches",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Black,
                color = Color.White
            )
            Text(
                text = "Check Room ID & Password 15 minutes before match start time",
                fontSize = 12.sp,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        if (joinedMatches.isEmpty()) {
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
                            tint = TextMuted,
                            modifier = Modifier.size(56.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "You haven't joined any tournament yet!",
                            color = TextMuted,
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "Go to Tournaments feed and tap 'JOIN NOW'",
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        } else {
            items(joinedMatches, key = { it.id }) { joined ->
                val tournament = tournamentMap[joined.tournamentId]

                Card(
                    modifier = Modifier
                        .testTag("my_match_card_${joined.id}")
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .border(1.dp, SlateCardBorder, RoundedCornerShape(16.dp)),
                    colors = CardDefaults.cardColors(containerColor = SlateCardBg),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(FieryRed)
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = joined.gameMode,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Schedule,
                                    contentDescription = null,
                                    tint = GoldYellow,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = joined.matchTime,
                                    color = GoldYellow,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = joined.tournamentTitle,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row {
                            Text(text = "In-Game Name: ", color = TextSecondary, fontSize = 12.sp)
                            Text(text = joined.inGameName, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(text = "UID: ", color = TextSecondary, fontSize = 12.sp)
                            Text(text = joined.inGameUid, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Room Credentials Card
                        val roomId = tournament?.roomId ?: ""
                        val roomPass = tournament?.roomPassword ?: ""
                        val hasRoom = roomId.isNotBlank() && roomPass.isNotBlank()

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (hasRoom) EmeraldGreen.copy(alpha = 0.15f) else SlateDarkBg)
                                .border(
                                    1.dp,
                                    if (hasRoom) EmeraldGreen else SlateCardBorder,
                                    RoundedCornerShape(12.dp)
                                )
                                .padding(12.dp)
                        ) {
                            Column {
                                // Top row: Status title & Admin Edit button
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            Icons.Default.MeetingRoom,
                                            contentDescription = null,
                                            tint = if (hasRoom) EmeraldGreen else GoldYellow,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "রুম আইডি ও পাসওয়ার্ড",
                                            fontWeight = FontWeight.Bold,
                                            color = if (hasRoom) EmeraldGreen else GoldYellow,
                                            fontSize = 12.sp
                                        )
                                    }

                                    // Admin edit button
                                    if (tournament != null) {
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(6.dp))
                                                .background(Color(0xFF1E293B))
                                                .border(1.dp, Color(0xFF3B82F6).copy(alpha = 0.5f), RoundedCornerShape(6.dp))
                                                .clickable { editingTournament = tournament }
                                                .padding(horizontal = 8.dp, vertical = 3.dp)
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(
                                                    imageVector = Icons.Default.Edit,
                                                    contentDescription = "Edit",
                                                    tint = Color(0xFF60A5FA),
                                                    modifier = Modifier.size(12.dp)
                                                )
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text(
                                                    text = if (hasRoom) "✏️ এডিট" else "✏️ সেট করুন",
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color(0xFF93C5FD)
                                                )
                                            }
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                if (hasRoom) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                modifier = Modifier.clickable {
                                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                                    val clip = ClipData.newPlainText("FF Room ID", roomId)
                                                    clipboard.setPrimaryClip(clip)
                                                    Toast.makeText(context, "Room ID কপি করা হয়েছে: $roomId", Toast.LENGTH_SHORT).show()
                                                }
                                            ) {
                                                Text(text = "Room ID: ", color = TextSecondary, fontSize = 13.sp)
                                                Text(text = roomId, fontWeight = FontWeight.Black, color = Color.White, fontSize = 14.sp)
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Icon(Icons.Default.ContentCopy, contentDescription = "Copy", tint = EmeraldGreen, modifier = Modifier.size(13.dp))
                                            }

                                            Spacer(modifier = Modifier.height(4.dp))

                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                modifier = Modifier.clickable {
                                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                                    val clip = ClipData.newPlainText("FF Password", roomPass)
                                                    clipboard.setPrimaryClip(clip)
                                                    Toast.makeText(context, "Password কপি করা হয়েছে: $roomPass", Toast.LENGTH_SHORT).show()
                                                }
                                            ) {
                                                Text(text = "Password: ", color = TextSecondary, fontSize = 13.sp)
                                                Text(text = roomPass, fontWeight = FontWeight.Black, color = Color.White, fontSize = 14.sp)
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Icon(Icons.Default.ContentCopy, contentDescription = "Copy", tint = EmeraldGreen, modifier = Modifier.size(13.dp))
                                            }
                                        }

                                        // Gamers Copy Button
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(EmeraldGreen)
                                                .clickable {
                                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                                    val clip = ClipData.newPlainText("FF Room Info", "Room ID: $roomId\nPassword: $roomPass")
                                                    clipboard.setPrimaryClip(clip)
                                                    Toast.makeText(context, "রুম আইডি ও পাসওয়ার্ড কপি করা হয়েছে!", Toast.LENGTH_SHORT).show()
                                                }
                                                .padding(horizontal = 10.dp, vertical = 6.dp)
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(Icons.Default.ContentCopy, contentDescription = "Copy", tint = Color.Black, modifier = Modifier.size(14.dp))
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text(text = "কপি", fontWeight = FontWeight.Black, color = Color.Black, fontSize = 12.sp)
                                            }
                                        }
                                    }
                                } else {
                                    Text(
                                        text = "⏳ রুম আইডি ও পাসওয়ার্ড ম্যাচ শুরুর ৫-১০ মিনিট আগে দেওয়া হবে।",
                                        fontSize = 12.sp,
                                        color = FlameOrange,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
