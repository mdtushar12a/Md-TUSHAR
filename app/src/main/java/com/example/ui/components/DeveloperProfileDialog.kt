package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R

@Composable
fun ReferEarnDialog(
    userUid: String,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val darkBg = Color(0xFF131A26)
    val cardBg = Color(0xFF1A2333)
    val cyanAccent = Color(0xFF00E5FF)
    val greenButton = Color(0xFF2EA043)

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = darkBg,
        title = {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                Text(text = "🎁 Refer & Earn", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "বন্ধুদের ইনভাইট করে প্রতি রেফারে ৳৫০ পর্যন্ত আয় করুন!", fontSize = 13.sp, color = Color(0xFF8B9BB0), textAlign = TextAlign.Center)
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "আপনার ইউনিক রেফারেল কোড:",
                    fontSize = 13.sp,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(cardBg)
                        .border(1.dp, cyanAccent, RoundedCornerShape(10.dp))
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = userUid,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = cyanAccent,
                        letterSpacing = 2.sp
                    )

                    OutlinedButton(
                        onClick = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("Referral Code", userUid)
                            clipboard.setPrimaryClip(clip)
                            Toast.makeText(context, "কোড কপি করা হয়েছে: $userUid", Toast.LENGTH_SHORT).show()
                        },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                        border = androidx.compose.foundation.BorderStroke(1.dp, cyanAccent)
                    ) {
                        Icon(Icons.Default.ContentCopy, contentDescription = "Copy", modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "কপি", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "১. আপনার বন্ধুকে অ্যাপে রেজিস্ট্রেশন করতে বলুন।\n২. বন্ধু প্রথম ডিপোজিট করলেই আপনার ওয়ালেটে বোনাস যোগ হবে।",
                    fontSize = 12.sp,
                    color = Color(0xFF8B9BB0),
                    lineHeight = 18.sp
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = greenButton),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "ঠিক আছে", fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    )
}

@Composable
fun DeveloperProfileDialog(
    onDismiss: () -> Unit
) {
    val darkBg = Color(0xFF131A26)
    val cyanAccent = Color(0xFF00E5FF)

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = darkBg,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.games_club_logo_1786525357371),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = "Developer Profile", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(text = "📱 FF CLUB BD eSports Engine", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = cyanAccent)
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = "Version: 2.4.0 (Build 2026)", fontSize = 13.sp, color = Color.White)
                Text(text = "Developer: Games Club Studio Ltd.", fontSize = 13.sp, color = Color.White)
                Text(text = "Tech Stack: Jetpack Compose, Kotlin, Room Database, Modern UI M3 Design", fontSize = 13.sp, color = Color(0xFF8B9BB0))
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = "বাংলাদেশের গেমারদের জন্য দ্রুততম টুর্নামেন্ট ও ইনস্ট্যান্ট পেআউট গেমিং প্ল্যাটফর্ম।", fontSize = 12.sp, color = Color(0xFF8B9BB0))
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = cyanAccent, contentColor = Color.Black),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "বন্ধ করুন", fontWeight = FontWeight.Bold)
            }
        }
    )
}
