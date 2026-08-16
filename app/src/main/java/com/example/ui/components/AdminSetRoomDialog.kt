package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.MeetingRoom
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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

@Composable
fun AdminSetRoomDialog(
    tournament: Tournament,
    onDismiss: () -> Unit,
    onSave: (roomId: String, roomPassword: String) -> Unit
) {
    var inputRoomId by remember { mutableStateOf(tournament.roomId) }
    var inputPassword by remember { mutableStateOf(tournament.roomPassword) }
    var errorMsg by remember { mutableStateOf<String?>(null) }

    val gradientBrush = Brush.horizontalGradient(
        colors = listOf(FieryRed, FlameOrange)
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = SlateCardBg,
        shape = RoundedCornerShape(20.dp),
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(GoldYellow.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AdminPanelSettings,
                        contentDescription = null,
                        tint = GoldYellow,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "রুম আইডি ও পাসওয়ার্ড বসান",
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        fontSize = 17.sp
                    )
                    Text(
                        text = "ম্যাচ: ${tournament.title}",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                }
            }
        },
        text = {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(SlateDarkBg)
                        .border(1.dp, SlateCardBorder, RoundedCornerShape(10.dp))
                        .padding(12.dp)
                ) {
                    Text(
                        text = "এখানে আপনি কাস্টম রুম আইডি ও পাসওয়ার্ড বসাতে পারেন। সেভ করার সাথে সাথে গেমাররা এটি দেখতে ও কপি করতে পারবে।",
                        color = TextSecondary,
                        fontSize = 12.sp,
                        lineHeight = 17.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = inputRoomId,
                    onValueChange = {
                        inputRoomId = it
                        errorMsg = null
                    },
                    label = { Text("Custom Room ID (রুম আইডি)") },
                    placeholder = { Text("e.g. 19284729", color = TextMuted) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.MeetingRoom,
                            contentDescription = null,
                            tint = EmeraldGreen
                        )
                    },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = EmeraldGreen,
                        unfocusedBorderColor = SlateCardBorder,
                        focusedLabelColor = EmeraldGreen,
                        unfocusedLabelColor = TextSecondary,
                        focusedContainerColor = SlateDarkBg,
                        unfocusedContainerColor = SlateDarkBg
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = inputPassword,
                    onValueChange = {
                        inputPassword = it
                        errorMsg = null
                    },
                    label = { Text("Room Password (রুম পাসওয়ার্ড)") },
                    placeholder = { Text("e.g. 1234 বা 0000", color = TextMuted) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Key,
                            contentDescription = null,
                            tint = GoldYellow
                        )
                    },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = GoldYellow,
                        unfocusedBorderColor = SlateCardBorder,
                        focusedLabelColor = GoldYellow,
                        unfocusedLabelColor = TextSecondary,
                        focusedContainerColor = SlateDarkBg,
                        unfocusedContainerColor = SlateDarkBg
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                if (errorMsg != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = errorMsg!!,
                        color = FieryRed,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (inputRoomId.trim().isBlank()) {
                        errorMsg = "দয়া করে রুম আইডি লিখুন!"
                        return@Button
                    }
                    if (inputPassword.trim().isBlank()) {
                        errorMsg = "দয়া করে রুম পাসওয়ার্ড লিখুন!"
                        return@Button
                    }
                    onSave(inputRoomId.trim(), inputPassword.trim())
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(gradientBrush)
            ) {
                Text(
                    text = "সেভ করুন (Save)",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "বাতিল",
                    color = TextSecondary,
                    fontSize = 14.sp
                )
            }
        }
    )
}
