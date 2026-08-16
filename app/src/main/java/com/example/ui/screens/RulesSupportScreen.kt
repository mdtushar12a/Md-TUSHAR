package com.example.ui.screens

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.HeadsetMic
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.FieryRed
import com.example.ui.theme.FlameOrange
import com.example.ui.theme.GoldYellow
import com.example.ui.theme.SlateCardBg
import com.example.ui.theme.SlateCardBorder
import com.example.ui.theme.SlateDarkBg
import com.example.ui.theme.TextSecondary

@Composable
fun RulesSupportScreen(
    onOpenSupport: () -> Unit
) {
    val rulesBengali = listOf(
        "১. হ্যাক, স্ক্রিপ্ট বা এমুলেটর সম্পূর্ণ নিষিদ্ধ। ধরা পড়লে পারমানেন্ট ব্যান করা হবে।",
        "২. রুম খোলার ১৫ মিনিট আগে 'My Matches' ট্যাবে Room ID & Password দেওয়া হবে।",
        "৩. সঠিক ইন-গেম নাম (Free Fire Name) এবং UID দিয়ে জয়েন করতে হবে।",
        "৪. জয়েন করার পর নির্দিষ্ট সময়ের মধ্যে কাস্টম রুমে জয়েন করতে হবে। সময় শেষ হলে রিফান্ড পাবেন না।",
        "৫. ম্যাচের পর প্রাইজ মানি ২৪ ঘণ্টার মধ্যে ওয়ালেটে বা বিকাশ/নগদে যুক্ত হয়ে যাবে।",
        "৬. কোনো বিরোধ থাকলে স্ক্রিনশট বা ভিডিও রেকর্ড নিয়ে সাপোর্ট টিমে যোগাযোগ করুন।"
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(SlateDarkBg),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp)
    ) {
        item {
            Text(
                text = "Tournament Rules & Fair Play",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Black,
                color = Color.White
            )
            Text(
                text = "গেমস ক্লাব বিডি নিয়মাবলী ও দিকনির্দেশনা",
                fontSize = 12.sp,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Live Support Banner Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, FlameOrange, RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = SlateCardBg),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.HeadsetMic, contentDescription = null, tint = FlameOrange, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "Need Instant Help?", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 15.sp)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Contact 24/7 Admin Support for deposit, room, or prize issues.", fontSize = 12.sp, color = TextSecondary)
                    }

                    Button(
                        onClick = onOpenSupport,
                        modifier = Modifier.testTag("live_support_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = FlameOrange),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(text = "HELP", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }

        item {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 12.dp)) {
                Icon(Icons.Default.Gavel, contentDescription = null, tint = FieryRed, modifier = Modifier.size(22.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Official Rules (টুর্নামেন্ট নিয়মাবলী)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }

        items(rulesBengali.size) { index ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .border(1.dp, SlateCardBorder, RoundedCornerShape(12.dp)),
                colors = CardDefaults.cardColors(containerColor = SlateCardBg),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = rulesBengali[index],
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White,
                        lineHeight = 18.sp
                    )
                }
            }
        }
    }
}
