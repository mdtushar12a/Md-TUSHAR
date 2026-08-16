package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.MeetingRoom
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import kotlinx.coroutines.delay

import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock
import com.example.ui.components.AdminSetRoomDialog

@Composable
fun TournamentCard(
    tournament: Tournament,
    isJoined: Boolean,
    onJoinClick: (Tournament) -> Unit,
    onUpdateRoomCredentials: ((Long, String, String) -> Unit)? = null
) {
    val context = LocalContext.current
    var currentTimeMillis by remember { mutableLongStateOf(System.currentTimeMillis()) }
    var showAdminRoomDialog by remember { mutableStateOf(false) }

    if (showAdminRoomDialog) {
        AdminSetRoomDialog(
            tournament = tournament,
            onDismiss = { showAdminRoomDialog = false },
            onSave = { roomId, roomPassword ->
                onUpdateRoomCredentials?.invoke(tournament.id, roomId, roomPassword)
                Toast.makeText(context, "রুম আইডি ও পাসওয়ার্ড সফলভাবে সেভ হয়েছে!", Toast.LENGTH_SHORT).show()
            }
        )
    }

    LaunchedEffect(tournament.startTimestamp) {
        while (true) {
            currentTimeMillis = System.currentTimeMillis()
            delay(1000L)
        }
    }

    val isMatchStarted = tournament.startTimestamp > 0 && currentTimeMillis >= tournament.startTimestamp
    val isFull = tournament.joinedSlots >= tournament.totalSlots
    val slotRatio = if (tournament.totalSlots > 0) {
        tournament.joinedSlots.toFloat() / tournament.totalSlots.toFloat()
    } else 0f

    val gradientBrush = Brush.horizontalGradient(
        colors = listOf(FieryRed, FlameOrange)
    )

    Card(
        modifier = Modifier
            .testTag("tournament_card_${tournament.id}")
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .border(1.dp, SlateCardBorder, RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = SlateCardBg),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header Row: Game Mode & Map Name + Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(FieryRed.copy(alpha = 0.2f))
                            .border(1.dp, FieryRed, RoundedCornerShape(8.dp))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = tournament.gameMode,
                            color = FieryRed,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "Map: ${tournament.mapName}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary,
                        fontWeight = FontWeight.Medium
                    )
                }

                // Match Status / Schedule Badge + Live Countdown
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = "Match Time",
                            tint = GoldYellow,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = tournament.matchTime,
                            color = GoldYellow,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(modifier = Modifier.height(3.dp))

                    MatchCountdownBadge(
                        startTimestamp = tournament.startTimestamp,
                        matchTimeText = tournament.matchTime
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Tournament Title
            Text(
                text = tournament.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Stats Row: Prize Pool, Entry Fee, Per Kill
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(SlateDarkBg)
                    .border(1.dp, SlateCardBorder, RoundedCornerShape(12.dp))
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Prize Pool
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "PRIZE POOL",
                        fontSize = 10.sp,
                        color = TextMuted,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.EmojiEvents,
                            contentDescription = "Prize",
                            tint = GoldYellow,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "৳ ${tournament.prizePool}",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Black,
                            color = GoldYellow
                        )
                    }
                }

                // Entry Fee
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "ENTRY FEE",
                        fontSize = 10.sp,
                        color = TextMuted,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = if (tournament.entryFee == 0) "FREE" else "৳ ${tournament.entryFee}",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black,
                        color = if (tournament.entryFee == 0) EmeraldGreen else FieryRed
                    )
                }

                // Per Kill Prize
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "PER KILL",
                        fontSize = 10.sp,
                        color = TextMuted,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.MilitaryTech,
                            contentDescription = "Kill Bonus",
                            tint = FlameOrange,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = "৳ ${tournament.perKillPrize}",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Slot Progress Indicator
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Slots Filled",
                    fontSize = 12.sp,
                    color = TextSecondary
                )
                Text(
                    text = "${tournament.joinedSlots}/${tournament.totalSlots}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isFull) FieryRed else Color.White
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            LinearProgressIndicator(
                progress = { slotRatio },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = if (isFull) FieryRed else FlameOrange,
                trackColor = SlateDarkBg,
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Dedicated Room ID / Password Section Under Each Match
            val hasRoomInfo = tournament.roomId.isNotBlank() && tournament.roomPassword.isNotBlank()

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (hasRoomInfo) EmeraldGreen.copy(alpha = 0.12f) else SlateDarkBg)
                    .border(
                        1.dp,
                        if (hasRoomInfo) EmeraldGreen.copy(alpha = 0.6f) else SlateCardBorder,
                        RoundedCornerShape(12.dp)
                    )
                    .padding(10.dp)
            ) {
                Column {
                    // Header row: Title + Admin Edit Button
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (hasRoomInfo) Icons.Default.MeetingRoom else Icons.Default.Lock,
                                contentDescription = "Room Status",
                                tint = if (hasRoomInfo) EmeraldGreen else GoldYellow,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "কাস্টম রুম তথ্য (Room Info)",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (hasRoomInfo) EmeraldGreen else GoldYellow
                            )
                        }

                        // Admin Edit / Set Button
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFF263248))
                                .border(1.dp, Color(0xFF3B82F6).copy(alpha = 0.5f), RoundedCornerShape(6.dp))
                                .clickable { showAdminRoomDialog = true }
                                .padding(horizontal = 8.dp, vertical = 3.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Set Room Details",
                                    tint = Color(0xFF60A5FA),
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (hasRoomInfo) "✏️ এডিট" else "✏️ সেট করুন",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF93C5FD)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    if (hasRoomInfo) {
                        // Credentials Display for Gamers (Copy only)
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
                                        val clip = ClipData.newPlainText("FF Room ID", tournament.roomId)
                                        clipboard.setPrimaryClip(clip)
                                        Toast.makeText(context, "Room ID copied: ${tournament.roomId}", Toast.LENGTH_SHORT).show()
                                    }
                                ) {
                                    Text(
                                        text = "Room ID: ",
                                        color = TextSecondary,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        text = tournament.roomId,
                                        fontWeight = FontWeight.Black,
                                        color = Color.White,
                                        fontSize = 14.sp
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Icon(
                                        imageVector = Icons.Default.ContentCopy,
                                        contentDescription = "Copy Room ID",
                                        tint = EmeraldGreen,
                                        modifier = Modifier.size(13.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.height(4.dp))

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.clickable {
                                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                        val clip = ClipData.newPlainText("FF Room Password", tournament.roomPassword)
                                        clipboard.setPrimaryClip(clip)
                                        Toast.makeText(context, "Password copied: ${tournament.roomPassword}", Toast.LENGTH_SHORT).show()
                                    }
                                ) {
                                    Text(
                                        text = "Password: ",
                                        color = TextSecondary,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        text = tournament.roomPassword,
                                        fontWeight = FontWeight.Black,
                                        color = Color.White,
                                        fontSize = 14.sp
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Icon(
                                        imageVector = Icons.Default.ContentCopy,
                                        contentDescription = "Copy Password",
                                        tint = EmeraldGreen,
                                        modifier = Modifier.size(13.dp)
                                    )
                                }
                            }

                            // Prominent Copy Button for Gamers
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(EmeraldGreen)
                                    .clickable {
                                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                        val clip = ClipData.newPlainText(
                                            "FF Room Details",
                                            "Room ID: ${tournament.roomId}\nPassword: ${tournament.roomPassword}"
                                        )
                                        clipboard.setPrimaryClip(clip)
                                        Toast.makeText(context, "রুম আইডি ও পাসওয়ার্ড কপি করা হয়েছে!", Toast.LENGTH_SHORT).show()
                                    }
                                    .padding(horizontal = 10.dp, vertical = 6.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.ContentCopy,
                                        contentDescription = "Copy All",
                                        tint = Color.Black,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "কপি",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Black,
                                        color = Color.Black
                                    )
                                }
                            }
                        }
                    } else {
                        // Pending Notice for Gamers
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "⏳ রুম আইডি ও পাসওয়ার্ড ম্যাচ শুরুর ৫-১০ মিনিট আগে দেওয়া হবে",
                                color = TextMuted,
                                fontSize = 11.5.sp,
                                lineHeight = 16.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Action Button (Join Now / Joined / Completed / Match Started)
            when {
                tournament.status == "COMPLETED" -> {
                    OutlinedButton(
                        onClick = { },
                        enabled = false,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = tournament.winnerInfo.ifBlank { "Match Completed" },
                            fontSize = 13.sp,
                            color = TextMuted
                        )
                    }
                }
                isJoined -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(EmeraldGreen.copy(alpha = 0.2f))
                            .border(1.dp, EmeraldGreen, RoundedCornerShape(10.dp))
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = if (isMatchStarted) "✓ JOINED (ম্যাচ লাইভ চলছে)" else "✓ JOINED MATCH",
                                fontWeight = FontWeight.Black,
                                color = EmeraldGreen,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
                isMatchStarted -> {
                    // Match has started - JOIN NOW button is REMOVED
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(SlateDarkBg)
                            .border(1.dp, SlateCardBorder, RoundedCornerShape(10.dp))
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(FieryRed)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "ম্যাচ শুরু হয়ে গেছে (লাইভ চলছে)",
                                fontWeight = FontWeight.Bold,
                                color = TextMuted,
                                fontSize = 13.5.sp
                            )
                        }
                    }
                }
                isFull -> {
                    Button(
                        onClick = { },
                        enabled = false,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SlateDarkBg)
                    ) {
                        Text(
                            text = "SLOTS FULL",
                            fontWeight = FontWeight.Bold,
                            color = TextMuted
                        )
                    }
                }
                else -> {
                    Button(
                        onClick = { onJoinClick(tournament) },
                        modifier = Modifier
                            .testTag("join_now_button_${tournament.id}")
                            .fillMaxWidth()
                            .background(gradientBrush, RoundedCornerShape(10.dp)),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = "JOIN NOW",
                            fontWeight = FontWeight.Black,
                            fontSize = 15.sp,
                            color = Color.White,
                            letterSpacing = 1.sp
                        )
                    }
                }
            }
        }
    }
}
