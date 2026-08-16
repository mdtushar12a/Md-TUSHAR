package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.HeadsetMic
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Percent
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.runtime.remember
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.models.UserProfile

@Composable
fun ProfileScreen(
    userProfile: UserProfile?,
    onOpenWallet: () -> Unit,
    onOpenDeposit: () -> Unit,
    onOpenStatements: () -> Unit,
    onOpenLeaderboard: () -> Unit,
    onOpenEditProfile: () -> Unit,
    onOpenTerms: () -> Unit,
    onOpenDeveloperProfile: () -> Unit,
    onOpenRefer: () -> Unit,
    onOpenNotification: () -> Unit = {}
) {
    val context = LocalContext.current

    val darkBackground = Color(0xFF0C1017)
    val cardBackground = Color(0xFF131A26)
    val cardBorder = Color(0xFF1E283A)
    val cyanAccent = Color(0xFF00E5FF)
    val greenAddButton = Color(0xFF2EA043)
    val yellowBadge = Color(0xFFFFC107)
    val mutedText = Color(0xFF8B9BB0)

    val balance = userProfile?.balance ?: 0.0
    val displayName = userProfile?.displayName.takeIf { !it.isNull_or_Empty() } ?: "Md TUSHAR"
    val phone = userProfile?.phone.takeIf { !it.isNullOrEmpty() } ?: "+8801962579201"
    val email = userProfile?.email.takeIf { !it.isNullOrEmpty() } ?: "mt8908778@gmail.com"
    val uid = userProfile?.inGameUid.takeIf { !it.isNullOrEmpty() } ?: remember { (10000000..99999999).random().toString() }
    val matchesPlayed = userProfile?.totalMatchesPlayed ?: 0
    val totalEarnings = userProfile?.totalEarnings ?: 0.0

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(darkBackground)
    ) {
        // Top App Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left: Logo + App Name
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.games_club_logo_1786525357371),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .size(34.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "FF CLUB BD",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            // Right: Notification Bell + Wallet Balance Pill
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .clickable { onOpenNotification() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notifications",
                        tint = yellowBadge,
                        modifier = Modifier.size(24.dp)
                    )
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .size(16.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE53935)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "4",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.width(10.dp))

                Row(
                    modifier = Modifier
                        .testTag("profile_top_wallet_pill")
                        .clip(CircleShape)
                        .background(Color(0xFF141B26))
                        .border(1.dp, Color(0xFF263248), CircleShape)
                        .clickable { onOpenWallet() }
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "৳",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.Black
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = String.format("%.2f", balance),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }

        // Main Scrollable Body
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 8.dp,
                bottom = 32.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. User Header Section
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Profile Avatar with Yellow Circle Border
                    Box(
                        modifier = Modifier
                            .size(76.dp)
                            .clip(CircleShape)
                            .border(3.dp, yellowBadge, CircleShape)
                            .background(Color(0xFF1B2A42)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "👦",
                            fontSize = 38.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = displayName,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = phone,
                            fontSize = 13.sp,
                            color = mutedText
                        )

                        Text(
                            text = email,
                            fontSize = 13.sp,
                            color = mutedText
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // UID Pill
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(Color(0xFF18202E))
                                    .clickable {
                                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                        val clip = ClipData.newPlainText("UID", uid)
                                        clipboard.setPrimaryClip(clip)
                                        Toast.makeText(context, "UID কপি করা হয়েছে: $uid", Toast.LENGTH_SHORT).show()
                                    }
                                    .padding(horizontal = 10.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "👤 UID: $uid",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.Default.ContentCopy,
                                    contentDescription = "Copy UID",
                                    tint = mutedText,
                                    modifier = Modifier.size(12.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            // Edit Button
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(yellowBadge)
                                    .clickable { onOpenEditProfile() }
                                    .padding(horizontal = 10.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Edit",
                                    tint = Color.Black,
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Edit",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                            }
                        }
                    }
                }
            }

            // 2. AVAILABLE BALANCE & Stats Card
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(cardBackground)
                        .border(1.dp, cardBorder, RoundedCornerShape(16.dp))
                        .padding(16.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(16.dp)
                                            .background(yellowBadge, RoundedCornerShape(3.dp)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text("💳", fontSize = 10.sp)
                                    }
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "AVAILABLE BALANCE",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = mutedText,
                                        letterSpacing = 0.5.sp
                                    )
                                }

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = "BDT ${String.format("%.2f", balance)}",
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color.White
                                )
                            }

                            // Green + ADD Button
                            Button(
                                onClick = onOpenWallet,
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = greenAddButton,
                                    contentColor = Color.White
                                ),
                                modifier = Modifier
                                    .testTag("profile_add_money_btn")
                                    .height(38.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Add",
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "ADD",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Stats Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Winning Pill
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0xFF1C2638))
                                    .padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Winning: ",
                                    fontSize = 12.sp,
                                    color = mutedText
                                )
                                Box(
                                    modifier = Modifier
                                        .background(yellowBadge, RoundedCornerShape(3.dp))
                                        .padding(horizontal = 4.dp, vertical = 1.dp)
                                ) {
                                    Text(
                                        text = "${totalEarnings.toInt()}",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Black
                                    )
                                }
                            }

                            // Matches
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "✦ ", fontSize = 12.sp, color = yellowBadge)
                                Text(
                                    text = "$matchesPlayed ",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = "MATCHES",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = mutedText
                                )
                            }

                            // Referrals
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "👥 ", fontSize = 12.sp)
                                Text(
                                    text = "0 ",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = "REFERRALS",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = mutedText
                                )
                            }

                            // Winnings
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "🏆 ", fontSize = 12.sp)
                                Text(
                                    text = "${totalEarnings.toInt()} ",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = "WINNINGS",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = mutedText
                                )
                            }
                        }
                    }
                }
            }

            // 3. ACCOUNT Group
            item {
                SectionHeader(title = "ACCOUNT")
            }

            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    ProfileMenuItem(
                        icon = Icons.Default.AccountBalanceWallet,
                        title = "My Wallet",
                        onClick = onOpenWallet
                    )
                    ProfileMenuItem(
                        icon = Icons.Default.SwapHoriz,
                        title = "All Statements",
                        onClick = onOpenStatements
                    )
                    ProfileMenuItem(
                        icon = Icons.Default.Percent,
                        title = "Refer & Earn",
                        badge = "NEW",
                        onClick = onOpenRefer
                    )
                }
            }

            // 4. GAME CENTER Group
            item {
                SectionHeader(title = "GAME CENTER")
            }

            item {
                ProfileMenuItem(
                    icon = Icons.Default.EmojiEvents,
                    title = "Leaderboard",
                    badge = "NEW",
                    onClick = onOpenLeaderboard
                )
            }

            // 5. MORE Group
            item {
                SectionHeader(title = "MORE")
            }

            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    ProfileMenuItem(
                        icon = Icons.Default.HeadsetMic,
                        title = "Customer Support (টেলিগ্রাম সাপোর্ট)",
                        onClick = {
                            val url = "https://t.me/R5EXXDnOYTc5OTdl"
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            try {
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                Toast.makeText(context, "Telegram: https://t.me/R5EXXDnOYTc5OTdl", Toast.LENGTH_LONG).show()
                            }
                        }
                    )
                    ProfileMenuItem(
                        icon = Icons.Default.Info,
                        title = "Terms and Conditions",
                        onClick = onOpenTerms
                    )
                    ProfileMenuItem(
                        icon = Icons.Default.Code,
                        title = "Developer Profile",
                        onClick = onOpenDeveloperProfile
                    )
                    ProfileMenuItem(
                        icon = Icons.Default.Share,
                        title = "Share App",
                        onClick = {
                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putcharExtra(Intent.EXTRA_SUBJECT, "FF CLUB BD")
                                putcharExtra(
                                    Intent.EXTRA_TEXT,
                                    "FF CLUB BD তে জয়েন করুন এবং ফ্রি ফায়ার খেলে টাকা আয় করুন! আপনার রেফারেল কোড: $uid\nডাউনলোড লিংক: https://ffclub.bd"
                                )
                            }
                            context.startActivity(Intent.createChooser(shareIntent, "Share FF CLUB BD"))
                        }
                    )
                }
            }
        }
    }
}

// Helper extension function for Intent EXTRA_TEXT safely
private fun Intent.putcharExtra(name: String, value: String) {
    putExtra(name, value)
}

// Helper extension function for Null or Empty string safety
private fun String?.isNull_or_Empty(): Boolean = this == null || this.isEmpty()

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF7D8EA0),
        letterSpacing = 1.sp,
        modifier = Modifier.padding(top = 4.dp, bottom = 2.dp)
    )
}

@Composable
private fun ProfileMenuItem(
    icon: ImageVector,
    title: String,
    badge: String? = null,
    onClick: () -> Unit
) {
    val cardBackground = Color(0xFF131A26)
    val cardBorder = Color(0xFF1E283A)
    val greenBadge = Color(0xFF2EA043)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(54.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(cardBackground)
            .border(1.dp, cardBorder, RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )

            Spacer(modifier = Modifier.width(14.dp))

            Text(
                text = title,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )

            if (badge != null) {
                Spacer(modifier = Modifier.width(10.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(greenBadge)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = badge,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }

        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = "Next",
            tint = Color(0xFF63738A),
            modifier = Modifier.size(18.dp)
        )
    }
}
