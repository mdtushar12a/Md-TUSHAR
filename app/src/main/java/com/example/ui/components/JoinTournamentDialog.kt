package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.Tournament
import com.example.ui.theme.FieryRed
import com.example.ui.theme.FlameOrange
import com.example.ui.theme.GoldYellow
import com.example.ui.theme.SlateCardBg
import com.example.ui.theme.SlateCardBorder
import com.example.ui.theme.SlateDarkBg
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSecondary

@Composable
fun JoinTournamentDialog(
    tournament: Tournament,
    userInGameName: String,
    userInGameUid: String,
    userBalance: Double,
    onDismiss: () -> Unit,
    onConfirm: (inGameName: String, inGameUid: String) -> Unit
) {
    var ffName by remember { mutableStateOf(userInGameName) }
    var ffUid by remember { mutableStateOf(userInGameUid) }
    var errorMsg by remember { mutableStateOf<String?>(null) }

    val gradientBrush = Brush.horizontalGradient(
        colors = listOf(FieryRed, FlameOrange)
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = SlateCardBg,
        title = {
            Column {
                Text(
                    text = "Confirm Match Registration",
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 18.sp
                )
                Text(
                    text = tournament.title,
                    fontSize = 14.sp,
                    color = FlameOrange,
                    fontWeight = FontWeight.SemiBold
                )
            }
        },
        text = {
            Column {
                // Fee & Balance Info Box
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(SlateDarkBg, RoundedCornerShape(10.dp))
                        .border(1.dp, SlateCardBorder, RoundedCornerShape(10.dp))
                        .padding(12.dp)
                ) {
                    Row {
                        Text(text = "Entry Fee: ", color = TextSecondary, fontSize = 13.sp)
                        Text(
                            text = if (tournament.entryFee == 0) "FREE" else "৳ ${tournament.entryFee} BDT",
                            color = GoldYellow,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Row {
                        Text(text = "Your Balance: ", color = TextSecondary, fontSize = 13.sp)
                        Text(
                            text = "৳ ${userBalance.toInt()} BDT",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Enter your Free Fire Player Details:",
                    fontSize = 13.sp,
                    color = TextSecondary,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Free Fire Name Input
                OutlinedTextField(
                    value = ffName,
                    onValueChange = { ffName = it; errorMsg = null },
                    label = { Text("Free Fire In-Game Name") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = FlameOrange) },
                    singleLine = true,
                    modifier = Modifier
                        .testTag("ff_name_input")
                        .fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = FlameOrange,
                        unfocusedBorderColor = SlateCardBorder,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Free Fire UID Input
                OutlinedTextField(
                    value = ffUid,
                    onValueChange = { ffUid = it; errorMsg = null },
                    label = { Text("Free Fire Player UID") },
                    leadingIcon = { Icon(Icons.Default.Badge, contentDescription = null, tint = FlameOrange) },
                    singleLine = true,
                    modifier = Modifier
                        .testTag("ff_uid_input")
                        .fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = FlameOrange,
                        unfocusedBorderColor = SlateCardBorder,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Rules Note Box
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF0F172A), RoundedCornerShape(8.dp))
                        .border(1.dp, Color(0xFF334155), RoundedCornerShape(8.dp))
                        .padding(10.dp)
                ) {
                    Text(
                        text = "📜 ম্যাচ নিয়মাবলী:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldYellow
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "• সঠিক গেম নাম ও ইউআইডি (Game UID) দিয়ে জয়েন করুন।\n• প্লেয়ার আইডির লেভেল কমপক্ষে ৪০ হতে হবে।\n• রুম আইডি ও পাসওয়ার্ড ৫-১০ মিনিট আগে মাই ম্যাচেসে পাবেন।\n• কোনো হ্যাক বা টিম-আপ সম্পূর্ণ নিষিদ্ধ।",
                        fontSize = 11.sp,
                        color = TextSecondary,
                        lineHeight = 16.sp
                    )
                }

                errorMsg?.let { msg ->
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = msg, color = FieryRed, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (ffName.isBlank()) {
                        errorMsg = "দয়া করে আপনার Free Fire In-Game Name দিন"
                        return@Button
                    }
                    if (ffUid.isBlank()) {
                        errorMsg = "দয়া করে আপনার Free Fire Player UID দিন"
                        return@Button
                    }
                    if (userBalance < tournament.entryFee && tournament.entryFee > 0) {
                        errorMsg = "অপর্যাপ্ত ব্যালেন্স! ম্যাচে জয়েন করতে ৳${tournament.entryFee} ব্যালেন্স প্রয়োজন।"
                        return@Button
                    }
                    onConfirm(ffName, ffUid)
                },
                modifier = Modifier
                    .testTag("confirm_join_button")
                    .background(gradientBrush, RoundedCornerShape(8.dp)),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
            ) {
                Text(text = "CONFIRM & JOIN", fontWeight = FontWeight.Bold, color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "CANCEL", color = TextMuted)
            }
        }
    )
}
