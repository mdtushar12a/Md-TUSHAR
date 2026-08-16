package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.LocalAtm
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.R
import com.example.data.models.JoinedMatch
import com.example.data.models.Tournament
import com.example.data.models.UserProfile
import kotlinx.coroutines.delay

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MeetingRoom
import com.example.ui.components.AdminSetRoomDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JoinBattleScreen(
    tournament: Tournament,
    userProfile: UserProfile?,
    joinedMatches: List<JoinedMatch>,
    onBack: () -> Unit,
    onOpenDeposit: () -> Unit,
    onConfirmJoin: (inGameName: String, inGameUid: String) -> Unit,
    onUpdateRoomCredentials: ((Long, String, String) -> Unit)? = null
) {
    val context = LocalContext.current
    var showConfirmationSheet by remember { mutableStateOf(false) }
    var showParticipantsDialog by remember { mutableStateOf(false) }
    var showPrizePoolDialog by remember { mutableStateOf(false) }
    var showTutorialVideoDialog by remember { mutableStateOf(false) }
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

    val isAlreadyJoined = joinedMatches.any { it.tournamentId == tournament.id }
    val remainingSpots = (tournament.totalSlots - tournament.joinedSlots).coerceAtLeast(0)
    val isMatchFull = remainingSpots == 0

    // Live countdown timer from actual startTimestamp
    var currentTimeMillis by remember { mutableLongStateOf(System.currentTimeMillis()) }
    LaunchedEffect(tournament.startTimestamp) {
        while (true) {
            currentTimeMillis = System.currentTimeMillis()
            delay(1000L)
        }
    }
    val targetTime = if (tournament.startTimestamp > 0) tournament.startTimestamp else (currentTimeMillis + 25 * 60 * 1000L)
    val remainingMillis = (targetTime - currentTimeMillis).coerceAtLeast(0L)
    val totalSeconds = remainingMillis / 1000
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60
    val timeFormatted = if (remainingMillis <= 0) "লাইভ চলছে" else "${hours}h: ${minutes}m: ${seconds}s"

    val isMatchStarted = tournament.startTimestamp > 0 && currentTimeMillis >= tournament.startTimestamp

    // Colors matching screenshots
    val darkNavyBg = Color(0xFF0C101B)
    val cardNavyBg = Color(0xFF131C2E)
    val cardBorderColor = Color(0xFF1E293B)
    val yellowAccent = Color(0xFFFACC15)
    val goldYellow = Color(0xFFF59E0B)
    val greenStatusBar = Color(0xFF16A34A)
    val textMuted = Color(0xFF94A3B8)
    val textLight = Color(0xFFE2E8F0)

    val matchNumberStr = "Match No.157${tournament.id + 150}"

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(darkNavyBg)
            .statusBarsPadding()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // --- TOP APP BAR ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.testTag("join_battle_back_btn")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Join the Battle",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            // --- SCROLLABLE CONTENT ---
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 120.dp)
            ) {
                // 1. MATCH TITLE
                item {
                    Text(
                        text = "${tournament.title} || $matchNumberStr",
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )

                    // Match Time & Live Countdown Badge
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.AccessTime,
                                contentDescription = null,
                                tint = textMuted,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = tournament.matchTime.ifEmpty { "14 Aug 2026, 02:45 AM" },
                                fontSize = 13.sp,
                                color = textMuted,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        com.example.ui.components.MatchCountdownBadge(
                            startTimestamp = tournament.startTimestamp,
                            matchTimeText = tournament.matchTime
                        )
                    }

                    // 2. TAGS ROW
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Map Tag (Green)
                        TagPill(
                            icon = Icons.Default.SportsEsports,
                            text = tournament.mapName.ifEmpty { "Bermuda" },
                            bgColor = Color(0xFF15803D),
                            textColor = Color.White
                        )
                        // Mode Tag (Sky Blue)
                        TagPill(
                            icon = Icons.Default.SportsEsports,
                            text = tournament.gameMode.ifEmpty { "Solo" },
                            bgColor = Color(0xFF0284C7),
                            textColor = Color.White
                        )
                        // Platform Tag (Dark Slate)
                        TagPill(
                            icon = Icons.Default.Smartphone,
                            text = "Android",
                            bgColor = Color(0xFF334155),
                            textColor = Color.White
                        )
                        // Mode Category Tag (Purple)
                        TagPill(
                            icon = null,
                            text = if (tournament.title.contains("CS", ignoreCase = true)) "CS MATCH" else "BR MATCH",
                            bgColor = Color(0xFF9333EA),
                            textColor = Color.White
                        )
                        // Paid / Free Tag (Blue)
                        TagPill(
                            icon = Icons.Default.LocalAtm,
                            text = if (tournament.entryFee == 0) "Free Match" else "Paid Match",
                            bgColor = Color(0xFF2563EB),
                            textColor = Color.White
                        )
                    }
                }

                // 3. PRIZE & ENTRY FEE CARD
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, cardBorderColor, RoundedCornerShape(16.dp)),
                        colors = CardDefaults.cardColors(containerColor = cardNavyBg),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            // Row 1: WINNING PRIZE & PER KILL
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Winning Prize
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(Color(0xFF854D0E).copy(alpha = 0.35f))
                                            .border(1.dp, goldYellow.copy(alpha = 0.5f), RoundedCornerShape(10.dp)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.EmojiEvents,
                                            contentDescription = null,
                                            tint = goldYellow,
                                            modifier = Modifier.size(22.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = "WINNING PRIZE",
                                            fontSize = 11.sp,
                                            color = textMuted,
                                            fontWeight = FontWeight.Bold,
                                            letterSpacing = 0.5.sp
                                        )
                                        Text(
                                            text = "৳ ${tournament.prizePool}",
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.Black,
                                            color = Color.White
                                        )
                                    }
                                }

                                // Per Kill
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(Color(0xFF991B1B).copy(alpha = 0.35f))
                                            .border(1.dp, Color(0xFFEF4444).copy(alpha = 0.5f), RoundedCornerShape(10.dp)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Whatshot,
                                            contentDescription = null,
                                            tint = Color(0xFFEF4444),
                                            modifier = Modifier.size(22.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = "PER KILL",
                                            fontSize = 11.sp,
                                            color = textMuted,
                                            fontWeight = FontWeight.Bold,
                                            letterSpacing = 0.5.sp
                                        )
                                        Text(
                                            text = "৳ ${tournament.perKillPrize}",
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.Black,
                                            color = Color.White
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Row 2: ENTRY FEE & PRIZE POOL BUTTON
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Entry fee
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(Color(0xFF065F46).copy(alpha = 0.35f))
                                            .border(1.dp, Color(0xFF10B981).copy(alpha = 0.5f), RoundedCornerShape(10.dp)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.ConfirmationNumber,
                                            contentDescription = null,
                                            tint = Color(0xFF10B981),
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = "ENTRY FEE",
                                            fontSize = 11.sp,
                                            color = textMuted,
                                            fontWeight = FontWeight.Bold,
                                            letterSpacing = 0.5.sp
                                        )
                                        Text(
                                            text = if (tournament.entryFee == 0) "FREE" else "৳ ${tournament.entryFee}",
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.Black,
                                            color = Color.White
                                        )
                                    }
                                }

                                // PRIZE POOL button
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(20.dp))
                                        .background(Color(0xFF1E293B))
                                        .border(1.dp, Color(0xFF334155), RoundedCornerShape(20.dp))
                                        .clickable { showPrizePoolDialog = true }
                                        .padding(horizontal = 14.dp, vertical = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Visibility,
                                            contentDescription = null,
                                            tint = Color(0xFF93C5FD),
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "PRIZE POOL",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White,
                                            letterSpacing = 0.5.sp
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                }

                // 3.5 ROOM ID & PASSWORD SECTION
                item {
                    val hasRoomInfo = tournament.roomId.isNotBlank() && tournament.roomPassword.isNotBlank()

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                1.dp,
                                if (hasRoomInfo) Color(0xFF10B981).copy(alpha = 0.6f) else cardBorderColor,
                                RoundedCornerShape(16.dp)
                            ),
                        colors = CardDefaults.cardColors(
                            containerColor = if (hasRoomInfo) Color(0xFF10B981).copy(alpha = 0.10f) else cardNavyBg
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            // Header: Room Info Title + Admin Edit Button
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = if (hasRoomInfo) Icons.Default.MeetingRoom else Icons.Default.Lock,
                                        contentDescription = "Room",
                                        tint = if (hasRoomInfo) Color(0xFF10B981) else goldYellow,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "CUSTOM ROOM CREDENTIALS",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Black,
                                        color = if (hasRoomInfo) Color(0xFF10B981) else goldYellow,
                                        letterSpacing = 0.5.sp
                                    )
                                }

                                // Admin Set / Edit Button
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(0xFF1E293B))
                                        .border(1.dp, Color(0xFF3B82F6).copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                                        .clickable { showAdminRoomDialog = true }
                                        .padding(horizontal = 10.dp, vertical = 5.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Edit,
                                            contentDescription = "Set Room",
                                            tint = Color(0xFF60A5FA),
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = if (hasRoomInfo) "✏️ এডিট" else "✏️ সেট করুন",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF93C5FD)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            if (hasRoomInfo) {
                                // Gamers Credentials & Copy Option
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color(0xFF0F172A))
                                        .border(1.dp, Color(0xFF334155), RoundedCornerShape(12.dp))
                                        .padding(12.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            // Room ID Row
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                modifier = Modifier.clickable {
                                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                                    val clip = ClipData.newPlainText("Room ID", tournament.roomId)
                                                    clipboard.setPrimaryClip(clip)
                                                    Toast.makeText(context, "Room ID কপি করা হয়েছে: ${tournament.roomId}", Toast.LENGTH_SHORT).show()
                                                }
                                            ) {
                                                Text(
                                                    text = "Room ID: ",
                                                    color = textMuted,
                                                    fontSize = 14.sp,
                                                    fontWeight = FontWeight.Medium
                                                )
                                                Text(
                                                    text = tournament.roomId,
                                                    fontWeight = FontWeight.Black,
                                                    color = Color.White,
                                                    fontSize = 16.sp
                                                )
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Icon(
                                                    imageVector = Icons.Default.ContentCopy,
                                                    contentDescription = "Copy",
                                                    tint = Color(0xFF10B981),
                                                    modifier = Modifier.size(15.dp)
                                                )
                                            }

                                            Spacer(modifier = Modifier.height(8.dp))

                                            // Password Row
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                modifier = Modifier.clickable {
                                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                                    val clip = ClipData.newPlainText("Password", tournament.roomPassword)
                                                    clipboard.setPrimaryClip(clip)
                                                    Toast.makeText(context, "Password কপি করা হয়েছে: ${tournament.roomPassword}", Toast.LENGTH_SHORT).show()
                                                }
                                            ) {
                                                Text(
                                                    text = "Password: ",
                                                    color = textMuted,
                                                    fontSize = 14.sp,
                                                    fontWeight = FontWeight.Medium
                                                )
                                                Text(
                                                    text = tournament.roomPassword,
                                                    fontWeight = FontWeight.Black,
                                                    color = Color.White,
                                                    fontSize = 16.sp
                                                )
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Icon(
                                                    imageVector = Icons.Default.ContentCopy,
                                                    contentDescription = "Copy",
                                                    tint = Color(0xFF10B981),
                                                    modifier = Modifier.size(15.dp)
                                                )
                                            }
                                        }

                                        // Big Copy Button for Gamers
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(10.dp))
                                                .background(Color(0xFF10B981))
                                                .clickable {
                                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                                    val clip = ClipData.newPlainText(
                                                        "FF Room Details",
                                                        "Room ID: ${tournament.roomId}\nPassword: ${tournament.roomPassword}"
                                                    )
                                                    clipboard.setPrimaryClip(clip)
                                                    Toast.makeText(context, "রুম আইডি ও পাসওয়ার্ড কপি করা হয়েছে!", Toast.LENGTH_SHORT).show()
                                                }
                                                .padding(horizontal = 14.dp, vertical = 10.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(
                                                    imageVector = Icons.Default.ContentCopy,
                                                    contentDescription = "Copy All",
                                                    tint = Color.Black,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Text(
                                                    text = "কপি",
                                                    fontSize = 13.sp,
                                                    fontWeight = FontWeight.Black,
                                                    color = Color.Black
                                                )
                                            }
                                        }
                                    }
                                }
                            } else {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(Color(0xFF0F172A))
                                        .border(1.dp, Color(0xFF334155), RoundedCornerShape(10.dp))
                                        .padding(12.dp)
                                ) {
                                    Text(
                                        text = "⏳ রুম আইডি ও পাসওয়ার্ড ম্যাচ শুরুর ৫-১০ মিনিট আগে দেওয়া হবে। আপনি ম্যাচ শুরুর আগে এই পেজে অথবা My Matches পেজে পেয়ে যাবেন।",
                                        color = textMuted,
                                        fontSize = 12.5.sp,
                                        lineHeight = 18.sp
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                }

                // 4. RULES & REGULATIONS SECTION
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, cardBorderColor, RoundedCornerShape(16.dp)),
                        colors = CardDefaults.cardColors(containerColor = cardNavyBg),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            // Section title with Gavel / Rules icon
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(bottom = 14.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Gavel,
                                    contentDescription = null,
                                    tint = goldYellow,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "RULES & REGULATIONS",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = goldYellow,
                                    letterSpacing = 1.sp
                                )
                            }

                            // Watch Tutorial Video Banner
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(140.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .border(1.dp, Color(0xFF334155), RoundedCornerShape(12.dp))
                                    .clickable { showTutorialVideoDialog = true },
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.img_match_tutorial_banner_1786645606017),
                                    contentDescription = "Tutorial Video",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )

                                // Dark overlay
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(Color.Black.copy(alpha = 0.45f))
                                )

                                // Play Button + Text
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(30.dp))
                                        .background(Color.Black.copy(alpha = 0.65f))
                                        .padding(horizontal = 16.dp, vertical = 8.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clip(CircleShape)
                                            .background(Color.White),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.PlayArrow,
                                            contentDescription = "Play",
                                            tint = Color(0xFFEF4444),
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = "WATCH TUTORIAL VIDEO",
                                        fontWeight = FontWeight.Black,
                                        fontSize = 13.sp,
                                        color = Color.White,
                                        letterSpacing = 0.5.sp
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Subheading: Club Bd || BR MATCH RULES
                            Text(
                                text = "🎮 Club Bd || ${tournament.title} RULES",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(bottom = 12.dp)
                            )

                            // 15 Complete Bengali Rules from User Screenshots
                            val rulesList = listOf(
                                "ম্যাচে জয়েন করার সময় অবশ্যই আপনার গেম আইডি ভালোভাবে মিলিয়ে নেবেন। গেম আইডি ব্যতীত অন্য কিছু লিখে জয়েন করলে ম্যাচ খেলতে দেওয়া হবে না।",
                                "আপনি যে আইডি দিয়ে খেলবেন, সেই আইডির লেভেল কমপক্ষে ৪০ হতে হবে। এর কম লেভেলের হলে আপনাকে ম্যাচ খেলতে দেওয়া হবে না।",
                                "ম্যাচ বা অ্যাপ সংক্রান্ত কোনো সমস্যার ক্ষেত্রে অবশ্যই ভিডিও বা স্পষ্ট প্রমাণ সংরক্ষণ করে রাখবেন। কারণ প্রমাণ ছাড়া আমরা সমস্যাটি যাচাই করতে পারি না, তাই প্রমাণ না থাকলে কোনো অভিযোগ গ্রহণ বা সমাধান করা হবে না।",
                                "যদি আপনি নির্ধারিত সময়ের মধ্যে ম্যাচে ঢুকে দেখেন ৪৮ স্লট Full বা ম্যাচ ইতিমধ্যেই playing হয়ে গেছে, তখন আপনার ফোনের বর্তমান সময়সহ একটি ছোট ভিডিও রেকর্ড করবেন। এই ভিডিও আপনার ম্যাচ জয়েন করার সময় যাচাইয়ের জন্য আমাদের প্রয়োজন হবে। অন্যথায় সময় পার হলে রিফান্ড দেওয়া হবে না, আর এক্ষেত্রে আমাদের ভিডিও দিয়েও কোনো লাভ নেই।",
                                "Custom Roome জয়েন করার পর ম্যাচ Start হওয়ার সময় কোনো কারণে আপনি যদি ম্যাচে প্রবেশ করতে না পারেন, তাহলে তার জন্য Games Club দায়ী থাকবে না, কারণ এটি সাধারণত আপনার নেটওয়ার্ক সমস্যার কারণে হতে পারে।",
                                "আপনি যদি নিজের জয়েন করা ম্যাচে বাহিরের প্লেয়ার ঢুকিয়ে নিজেই কিল করেন, তাহলে আপনার একাউন্টের সকল ব্যালেন্স কেটে নেওয়া হবে।",
                                "ম্যাচের মধ্যে কোনো ধরনের Sniper বন্দুক ব্যবহার করা যাবে না।",
                                "যদি সেফ জোনে গাড়ি ব্যবহার করতে হয়, যেখান থেকে দৌড়ে বের হওয়া অসম্ভব, তাহলে সেখানে গাড়ি ব্যবহার করা যাবে — কিন্তু জোনে ঢোকার আগেই গাড়ি থেকে নেমে যেতে হবে।",
                                "যেকোনো ধরনের হ্যাকার, টিম-আপ বা আনফেয়ার গেমপ্লে সম্পূর্ণ নিষিদ্ধ।",
                                "রুম আইডি ও পাসওয়ার্ড ম্যাচ শুরুর ৫-১০ মিনিট আগে অ্যাপের My Matches পেজে দেওয়া হবে।",
                                "ম্যাচ চলাকালে কেউ যদি আমাদের উল্লেখিত রুলস ভঙ্গ করে আপনাকে কিল করে, তাহলে আপনি ইন-গেম থেকে বা রেকর্ডার চালু থাকলে সেই ভিডিওটি সাপোর্টে-Helpline পাঠাবেন। আপনার সমস্যার সমাধান করা হবে।",
                                "ম্যাচের মধ্যে কেউ কারো সাথে টিম আপ করলে, তাদের সবার একাউন্টের ব্যালেন্স ০ করে দেওয়া হবে।",
                                "ম্যাচের মধ্যে কেউ যদি হ্যাক বা অস্বাভাবিক কিছু ব্যবহার করে ধরা পড়ে, তাহলে তার অ্যাকাউন্টের সব ব্যালেন্সসহ অ্যাকাউন্টটি ব্যান করা হবে।",
                                "ম্যাচ চলাকালীন কোনো প্লেয়ার রুলস ভেঙে অন্য প্লেয়ারকে ড্যামেজ বা কিল করলে, ক্ষতিগ্রস্ত প্লেয়ারকে ইন-গেম থেকে ভিডিও প্রুফ নিয়ে জমা দিতে হবে; প্রুফ ছাড়া অভিযোগ গ্রহণযোগ্য হবে না।",
                                "একটি ম্যাচ শেষ হওয়ার ১০–২০ মিনিটের মধ্যে আপনার রিওয়ার্ড আপনার অ্যাকাউন্টে অ্যাড হয়ে যাবে।"
                            )

                            rulesList.forEachIndexed { index, ruleText ->
                                Text(
                                    text = "${index + 1}. $ruleText",
                                    fontSize = 13.5.sp,
                                    color = textLight,
                                    lineHeight = 20.sp,
                                    fontWeight = FontWeight.Normal,
                                    modifier = Modifier.padding(bottom = 12.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // View Participants Button
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFF4F46E5))
                                    .clickable { showParticipantsDialog = true }
                                    .padding(vertical = 12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Visibility,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "VIEW PARTICIPANTS",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = Color.White,
                                        letterSpacing = 1.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // --- STICKY BOTTOM BAR (Green Info Bar + Yellow JOIN Button) ---
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .navigationBarsPadding()
        ) {
            // Green Bar (Countdown timer + spots)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(greenStatusBar)
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = timeFormatted,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Text(
                    text = "$remainingSpots Spots available",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            // Bottom Action Bar (JOIN / ALREADY JOINED / MATCH STARTED / FULL)
            val btnBgColor = when {
                isAlreadyJoined -> Color(0xFF10B981)
                isMatchStarted -> Color(0xFF1E293B)
                isMatchFull -> Color(0xFF334155)
                else -> yellowAccent
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(btnBgColor)
                    .clickable(enabled = !isAlreadyJoined && !isMatchFull && !isMatchStarted) {
                        showConfirmationSheet = true
                    }
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                if (isMatchStarted && !isAlreadyJoined) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFEF4444))
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "ম্যাচ শুরু হয়ে গেছে (নতুন জয়েন বন্ধ)",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = textMuted,
                            letterSpacing = 0.5.sp
                        )
                    }
                } else {
                    Text(
                        text = when {
                            isAlreadyJoined -> if (isMatchStarted) "✓ JOINED (ম্যাচ লাইভ চলছে)" else "✓ ALREADY JOINED"
                            isMatchFull -> "MATCH FULL"
                            else -> "J  O  I  N"
                        },
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = when {
                            isAlreadyJoined -> Color.White
                            isMatchFull -> textMuted
                            else -> Color.Black
                        },
                        letterSpacing = if (isAlreadyJoined || isMatchFull) 1.sp else 3.sp
                    )
                }
            }
        }
    }

    // --- CONFIRMATION BOTTOM SHEET (Screenshot 4) ---
    if (showConfirmationSheet) {
        ConfirmationBottomSheet(
            tournament = tournament,
            userProfile = userProfile,
            remainingSpots = remainingSpots,
            onDismiss = { showConfirmationSheet = false },
            onOpenDeposit = {
                showConfirmationSheet = false
                onOpenDeposit()
            },
            onConfirmJoin = { name, uid ->
                showConfirmationSheet = false
                onConfirmJoin(name, uid)
            }
        )
    }

    // --- PRIZE POOL BREAKDOWN DIALOG ---
    if (showPrizePoolDialog) {
        PrizePoolDialog(
            tournament = tournament,
            onDismiss = { showPrizePoolDialog = false }
        )
    }

    // --- PARTICIPANTS LIST DIALOG ---
    if (showParticipantsDialog) {
        ParticipantsDialog(
            tournament = tournament,
            joinedMatches = joinedMatches,
            userProfile = userProfile,
            onDismiss = { showParticipantsDialog = false }
        )
    }

    // --- TUTORIAL VIDEO DIALOG ---
    if (showTutorialVideoDialog) {
        TutorialVideoGuideDialog(
            onDismiss = { showTutorialVideoDialog = false }
        )
    }
}

// --- TAG PILL COMPONENT ---
@Composable
private fun TagPill(
    icon: androidx.compose.ui.graphics.vector.ImageVector?,
    text: String,
    bgColor: Color,
    textColor: Color
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = textColor,
                modifier = Modifier.size(13.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
        }
        Text(
            text = text,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
}

// --- CONFIRMATION BOTTOM SHEET (Screenshot 4) ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmationBottomSheet(
    tournament: Tournament,
    userProfile: UserProfile?,
    remainingSpots: Int,
    onDismiss: () -> Unit,
    onOpenDeposit: () -> Unit,
    onConfirmJoin: (name: String, uid: String) -> Unit
) {
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var selectedModeType by remember {
        mutableStateOf(
            if (tournament.gameMode.contains("Duo", ignoreCase = true)) "Duo"
            else if (tournament.gameMode.contains("Squad", ignoreCase = true)) "Squad"
            else "Solo"
        )
    }

    // Input fields for Player 1 (and optional player 2/3/4)
    var gameName1 by remember { mutableStateOf(userProfile?.inGameName.takeIf { !it.isNullOrEmpty() } ?: "") }
    var gameUid1 by remember { mutableStateOf(userProfile?.inGameUid.takeIf { !it.isNullOrEmpty() } ?: "") }
    var gameName2 by remember { mutableStateOf("") }
    var gameUid2 by remember { mutableStateOf("") }

    val userBalance = userProfile?.balance ?: 0.0
    val entryFeeMultiplier = when (selectedModeType) {
        "Duo" -> 1 // per team or standard fee
        "Squad" -> 1
        else -> 1
    }
    val totalFee = tournament.entryFee * entryFeeMultiplier
    val hasEnoughBalance = userBalance >= totalFee

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // BottomSheet Title
            Text(
                text = "Confirmation",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E293B),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Match Summary Card (Grey Card with border)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(12.dp)),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "${tournament.title} || Match No.157${tournament.id + 150}",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider(color = Color(0xFFCBD5E1), thickness = 1.dp)
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Minimum Entry Fee :",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF334155)
                        )
                        Text(
                            text = "৳${tournament.entryFee}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider(color = Color(0xFFCBD5E1), thickness = 1.dp)
                    Spacer(modifier = Modifier.height(10.dp))

                    // Spots left pill
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFF64748B))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "$remainingSpots Spots left",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Mode Selector Pill (Solo / Duo / Squad)
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("Solo", "Duo", "Squad").forEach { mode ->
                    val isSelected = selectedModeType == mode
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) Color(0xFFFACC15) else Color(0xFFF1F5F9))
                            .border(
                                1.dp,
                                if (isSelected) Color(0xFFEAB308) else Color(0xFFE2E8F0),
                                RoundedCornerShape(8.dp)
                            )
                            .clickable { selectedModeType = mode }
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = Color.Black,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                            }
                            Text(
                                text = mode,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) Color.Black else Color(0xFF64748B)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Total Entry Fee
            Text(
                text = "Total Entry Fee: ৳$totalFee",
                fontSize = 16.sp,
                fontWeight = FontWeight.Black,
                color = Color(0xFF0F172A)
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Game Name 1 Input Field
            OutlinedTextField(
                value = gameName1,
                onValueChange = { gameName1 = it },
                placeholder = { Text("Enter Game Name 1", color = Color(0xFF94A3B8)) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("game_name_input_1"),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF0F172A),
                    unfocusedBorderColor = Color(0xFFCBD5E1),
                    focusedTextColor = Color(0xFF0F172A),
                    unfocusedTextColor = Color(0xFF0F172A)
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Game UID 1 Input Field
            OutlinedTextField(
                value = gameUid1,
                onValueChange = { gameUid1 = it },
                placeholder = { Text("Enter Player Game UID 1 (Free Fire UID)", color = Color(0xFF94A3B8)) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("game_uid_input_1"),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF0F172A),
                    unfocusedBorderColor = Color(0xFFCBD5E1),
                    focusedTextColor = Color(0xFF0F172A),
                    unfocusedTextColor = Color(0xFF0F172A)
                )
            )

            // Optional Player 2 for Duo/Squad
            if (selectedModeType == "Duo" || selectedModeType == "Squad") {
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = gameName2,
                    onValueChange = { gameName2 = it },
                    placeholder = { Text("Enter Teammate Game Name 2", color = Color(0xFF94A3B8)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF0F172A),
                        unfocusedBorderColor = Color(0xFFCBD5E1),
                        focusedTextColor = Color(0xFF0F172A),
                        unfocusedTextColor = Color(0xFF0F172A)
                    )
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // User Balance Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (hasEnoughBalance) Color(0xFFF1F5F9) else Color(0xFFFEE2E2))
                    .border(
                        1.dp,
                        if (hasEnoughBalance) Color(0xFFCBD5E1) else Color(0xFFFCA5A5),
                        RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AccountBalanceWallet,
                        contentDescription = null,
                        tint = if (hasEnoughBalance) Color(0xFF16A34A) else Color(0xFFDC2626),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "আপনার বর্তমান ব্যালেন্স",
                            fontSize = 11.sp,
                            color = if (hasEnoughBalance) Color(0xFF64748B) else Color(0xFF991B1B),
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "৳ ${String.format("%.2f", userBalance)}",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Black,
                            color = if (hasEnoughBalance) Color(0xFF0F172A) else Color(0xFFDC2626)
                        )
                    }
                }

                if (!hasEnoughBalance) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFFDC2626))
                            .clickable { onOpenDeposit() }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "+ টাকা যোগ করুন",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }

            if (!hasEnoughBalance && totalFee > 0) {
                Spacer(modifier = Modifier.height(10.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFFFF1F2))
                        .border(1.dp, Color(0xFFFFCCD5), RoundedCornerShape(8.dp))
                        .padding(10.dp)
                ) {
                    Text(
                        text = "⚠️ অ্যাকাউন্টে পর্যাপ্ত ব্যালেন্স নেই!",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFBE123C)
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = "ম্যাচে জয়েন করতে এন্ট্রি ফি ৳$totalFee প্রয়োজন। দয়া করে আগে ডিপোজিট করে ব্যালেন্স রিচার্জ করুন।",
                        fontSize = 12.sp,
                        color = Color(0xFF881337),
                        lineHeight = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Action Button: Confirm or Deposit Now
            if (!hasEnoughBalance && totalFee > 0) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFFDC2626))
                        .clickable {
                            Toast.makeText(context, "ম্যাচে জয়েন করতে আগে ব্যালেন্স রিচার্জ করুন", Toast.LENGTH_SHORT).show()
                            onOpenDeposit()
                        }
                        .padding(vertical = 14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AccountBalanceWallet,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "ডিপোজিট করুন (টাকা যোগ করুন)",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White,
                            letterSpacing = 0.5.sp
                        )
                    }
                }
            } else {
                // Yellow CONFIRM Button (Screenshot 4)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFFFACC15))
                        .clickable {
                            if (gameName1.isBlank()) {
                                Toast.makeText(context, "দয়া করে আপনার গেমের নাম (Game Name) দিন", Toast.LENGTH_SHORT).show()
                                return@clickable
                            }
                            if (gameUid1.isBlank()) {
                                Toast.makeText(context, "দয়া করে আপনার সঠিক গেম ইউআইডি (Game UID) দিন", Toast.LENGTH_SHORT).show()
                                return@clickable
                            }
                            if (!hasEnoughBalance && totalFee > 0) {
                                Toast.makeText(context, "পর্যাপ্ত ব্যালেন্স নেই! দয়া করে ডিপোজিট করুন।", Toast.LENGTH_LONG).show()
                                onOpenDeposit()
                                return@clickable
                            }
                            onConfirmJoin(gameName1.trim(), gameUid1.trim())
                        }
                        .padding(vertical = 14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(22.dp)
                                .clip(CircleShape)
                                .background(Color.Black),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = Color(0xFFFACC15),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "C O N F I R M",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.Black,
                            letterSpacing = 2.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

// --- PRIZE POOL DIALOG ---
@Composable
fun PrizePoolDialog(
    tournament: Tournament,
    onDismiss: () -> Unit
) {
    val totalCollected = tournament.entryFee * tournament.totalSlots
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF131C2E))
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "🏆 Prize Pool Breakdown (৬০%)",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFACC15)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Info banner explaining the 60% pool & max 5 Tk kill
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF1E293B))
                        .padding(10.dp)
                ) {
                    if (tournament.entryFee > 0) {
                        Text(
                            text = "📊 মোট কালেকশন: ৳${totalCollected} (${tournament.totalSlots} স্লট × ৳${tournament.entryFee})",
                            color = Color(0xFF94A3B8),
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                    }
                    Text(
                        text = "🎁 প্লেয়ারদের দেওয়া হবে: ৬০% প্রাইজ পুল (৳${tournament.prizePool})",
                        color = Color(0xFF10B981),
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "🎯 কিল রিওয়ার্ড: ৳${tournament.perKillPrize} / কিল (সর্বোচ্চ ৳৫)",
                        color = Color(0xFF38BDF8),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                val prizes = listOf(
                    "1st Place (Booyah)" to "৳ ${tournament.prizePool * 60 / 100}",
                    "2nd Place" to "৳ ${tournament.prizePool * 25 / 100}",
                    "3rd Place" to "৳ ${tournament.prizePool * 15 / 100}",
                    "Per Kill Reward" to "৳ ${tournament.perKillPrize} / Kill (Max ৳5)"
                )

                prizes.forEach { (rank, prize) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = rank, color = Color(0xFF94A3B8), fontSize = 14.sp)
                        Text(text = prize, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                    HorizontalDivider(color = Color(0xFF1E293B), thickness = 0.5.dp)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF334155)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("CLOSE", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// --- PARTICIPANTS DIALOG ---
@Composable
fun ParticipantsDialog(
    tournament: Tournament,
    joinedMatches: List<JoinedMatch>,
    userProfile: UserProfile?,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF131C2E))
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "👥 Participants (${tournament.joinedSlots}/${tournament.totalSlots})",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                ) {
                    val myMatch = joinedMatches.find { it.tournamentId == tournament.id }
                    if (myMatch != null) {
                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFF16A34A).copy(alpha = 0.25f))
                                    .border(1.dp, Color(0xFF16A34A), RoundedCornerShape(8.dp))
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("Slot #1: ", color = Color(0xFF86EFAC), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    Text(myMatch.inGameName, color = Color.White, fontWeight = FontWeight.Black, fontSize = 13.sp)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("(YOU)", color = Color(0xFFFACC15), fontSize = 11.sp, fontWeight = FontWeight.ExtraBold)
                                }
                                Text("UID: ${myMatch.inGameUid}", color = Color(0xFFCBD5E1), fontSize = 12.sp)
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                        }
                    }

                    // Enrolled slots list
                    val totalShowing = tournament.joinedSlots.coerceAtMost(tournament.totalSlots)
                    val startIdx = if (myMatch != null) 2 else 1
                    items(count = (totalShowing - if (myMatch != null) 1 else 0).coerceAtLeast(0)) { idx ->
                        val slotNum = startIdx + idx
                        val randomName = listOf("Hunter_BD", "King_Gamer", "Sniper_007", "Apex_Fire", "Thunder_Boy", "Shadow_Pro", "Eagle_Eye", "Viper_99", "Cobra_Commander", "Fire_Soul").let { it[(slotNum * 7) % it.size] }
                        val randomUid = "18${(345210 + slotNum * 837) % 999999}"

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp, horizontal = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Slot #$slotNum: $randomName", color = Color(0xFF94A3B8), fontSize = 13.sp)
                            Text(text = "UID: $randomUid", color = Color(0xFF64748B), fontSize = 12.sp)
                        }
                        HorizontalDivider(color = Color(0xFF1E293B), thickness = 0.5.dp)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF334155)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("OK", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// --- TUTORIAL VIDEO GUIDE DIALOG ---
@Composable
fun TutorialVideoGuideDialog(
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF131C2E))
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "📺 Match Tutorial & Guidelines",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_match_tutorial_banner_1786645606017),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.5f))
                    )
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Playing",
                            tint = Color(0xFFEF4444),
                            modifier = Modifier.size(48.dp)
                        )
                        Text(
                            text = "Free Fire Custom Room Guide",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "ভিডিও নির্দেশিকা:\n১. ম্যাচ শুরুর ৫ মিনিট পূর্বে 'My Matches' পেজে রুম আইডি ও পাসওয়ার্ড দেখুন।\n২. ফ্রি ফায়ার গেমে গিয়ে Custom অপশনে রুম আইডি দিয়ে জয়েন করুন।\n৩. আপনার নির্দিষ্ট স্লটে বসুন।\n৪. কোনো সমস্যা হলে সরাসরি ভিডিও রেকর্ড করে হেল্পলাইনে জানান।",
                    fontSize = 12.5.sp,
                    color = Color(0xFFE2E8F0),
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFACC15)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("বুঝেছি (GOT IT)", color = Color.Black, fontWeight = FontWeight.Black)
                }
            }
        }
    }
}
