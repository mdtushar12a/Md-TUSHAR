package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.UserProfile
import com.example.ui.theme.FieryRed
import com.example.ui.theme.FlameOrange
import com.example.ui.theme.SlateCardBg
import com.example.ui.theme.SlateCardBorder
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSecondary

@Composable
fun ProfileDialog(
    userProfile: UserProfile?,
    onDismiss: () -> Unit,
    onSaveProfile: (displayName: String, inGameName: String, inGameUid: String, phone: String) -> Unit,
    onResetData: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    var displayName by remember { mutableStateOf(userProfile?.displayName ?: "") }
    var inGameName by remember { mutableStateOf(userProfile?.inGameName ?: "") }
    var inGameUid by remember { mutableStateOf(userProfile?.inGameUid.takeIf { !it.isNullOrEmpty() } ?: (10000000..99999999).random().toString()) }
    var phone by remember { mutableStateOf(userProfile?.phone ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = SlateCardBg,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "🔥 ", fontSize = 20.sp)
                Text(
                    text = "Gamer Profile & Settings",
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 18.sp
                )
            }
        },
        text = {
            Column {
                // Google Login Button Simulator
                OutlinedButton(
                    onClick = {
                        displayName = "Google User (Tushar)"
                        inGameName = "Tushar_FF"
                        inGameUid = "1849204812"
                    },
                    modifier = Modifier
                        .testTag("google_login_sim_btn")
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(text = "🌐 Google দিয়ে Login (Instant Sync)", fontWeight = FontWeight.Bold, color = Color.White)
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Player Information:",
                    fontSize = 12.sp,
                    color = TextSecondary,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = displayName,
                    onValueChange = { displayName = it },
                    label = { Text("Display Name") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = FlameOrange) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = FlameOrange,
                        unfocusedBorderColor = SlateCardBorder,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = inGameName,
                    onValueChange = { inGameName = it },
                    label = { Text("Free Fire In-Game Name") },
                    leadingIcon = { Icon(Icons.Default.SportsEsports, contentDescription = null, tint = FlameOrange) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = FlameOrange,
                        unfocusedBorderColor = SlateCardBorder,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = inGameUid,
                    onValueChange = { inGameUid = it },
                    label = { Text("Free Fire Player UID") },
                    leadingIcon = { Icon(Icons.Default.Badge, contentDescription = null, tint = FlameOrange) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = FlameOrange,
                        unfocusedBorderColor = SlateCardBorder,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Mobile Number (bKash/Nagad)") },
                    leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = FlameOrange) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = FlameOrange,
                        unfocusedBorderColor = SlateCardBorder,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onLogout,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("logout_btn"),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color.Gray)
                    ) {
                        Text(text = "🚪 লগ আউট", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 12.sp)
                    }

                    OutlinedButton(
                        onClick = onResetData,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("reset_all_data_btn"),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = FieryRed),
                        border = androidx.compose.foundation.BorderStroke(1.dp, FieryRed)
                    ) {
                        Text(text = "🔄 ডাটা রিসেট", fontWeight = FontWeight.Bold, color = FieryRed, fontSize = 12.sp)
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSaveProfile(displayName, inGameName, inGameUid, phone)
                },
                modifier = Modifier.testTag("save_profile_button"),
                colors = ButtonDefaults.buttonColors(containerColor = FieryRed)
            ) {
                Text(text = "SAVE PROFILE", fontWeight = FontWeight.Bold, color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "CLOSE", color = TextMuted)
            }
        }
    )
}
