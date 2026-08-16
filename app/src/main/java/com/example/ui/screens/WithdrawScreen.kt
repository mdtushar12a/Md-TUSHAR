package com.example.ui.screens

import android.widget.Toast
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.UserProfile

@Composable
fun WithdrawScreen(
    userProfile: UserProfile?,
    onBack: () -> Unit,
    onSubmitWithdrawal: (method: String, amount: Double, accountNumber: String) -> Unit = { _, _, _ -> }
) {
    val context = LocalContext.current
    val winningBalance = userProfile?.totalEarnings ?: 0.0

    var selectedChannel by remember { mutableStateOf("bKash") }
    var walletNumber by remember { mutableStateOf("") }
    var amountText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Dark Top Header Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF0B1017))
                .padding(horizontal = 14.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Withdraw",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        // Main Content Area
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            // Subtitle
            Text(
                text = "Choose your payment channel",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF8E8E93),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                textAlign = TextAlign.Center
            )

            // Payment Channel Cards Row (bKash, Nagad, Rocket)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // bKash Card
                PaymentChannelCard(
                    name = "bKash",
                    logoColor = Color(0xFFE91E63),
                    logoText = "bKash",
                    isSelected = selectedChannel == "bKash",
                    onClick = { selectedChannel = "bKash" },
                    modifier = Modifier.weight(1f)
                )

                // Nagad Card
                PaymentChannelCard(
                    name = "Nagad",
                    logoColor = Color(0xFFE65100),
                    logoText = "নগদ",
                    isSelected = selectedChannel == "Nagad",
                    onClick = { selectedChannel = "Nagad" },
                    modifier = Modifier.weight(1f)
                )

                // Rocket Card
                PaymentChannelCard(
                    name = "Rocket",
                    logoColor = Color(0xFF7B1FA2),
                    logoText = "রকেট",
                    isSelected = selectedChannel == "Rocket",
                    onClick = { selectedChannel = "Rocket" },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Wallet Number Input
            OutlinedTextField(
                value = walletNumber,
                onValueChange = { walletNumber = it },
                placeholder = {
                    Text(
                        text = "Enter your Wallet Number:",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E293B),
                        fontSize = 14.sp
                    )
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = Color(0xFF4338CA),
                    unfocusedBorderColor = Color(0xFF94A3B8),
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Amount Input
            OutlinedTextField(
                value = amountText,
                onValueChange = { amountText = it },
                placeholder = {
                    Text(
                        text = "Amount",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E293B),
                        fontSize = 14.sp
                    )
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = Color(0xFF4338CA),
                    unfocusedBorderColor = Color(0xFF94A3B8),
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // WITHDRAW Red Button
            Button(
                onClick = {
                    val amount = amountText.toDoubleOrNull()
                    if (walletNumber.trim().isEmpty()) {
                        Toast.makeText(context, "অনুগ্রহ করে আপনার $selectedChannel নম্বর লিখুন", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    if (amount == null || amount < 100) {
                        Toast.makeText(context, "সর্বনিম্ন উত্তোলন পরিমাণ ১০০ টাকা", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    if (amount > 25000) {
                        Toast.makeText(context, "সর্বোচ্চ উত্তোলন পরিমাণ ২৫,০০০ টাকা", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    if (amount > (userProfile?.balance ?: 0.0)) {
                        Toast.makeText(context, "আপনার পর্যাপ্ত ব্যালেন্স নেই!", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    onSubmitWithdrawal(selectedChannel, amount, walletNumber)
                    Toast.makeText(context, "উইথড্র রিকোয়েস্ট সফলভাবে জমা হয়েছে! ১২ ঘণ্টার মধ্যে প্রসেস হবে।", Toast.LENGTH_LONG).show()
                    walletNumber = ""
                    amountText = ""
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC62828)),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(
                    text = "WITHDRAW",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Info Box 1: Withdrawal Balance
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFE8F4FB))
                    .padding(vertical = 14.dp, horizontal = 16.dp)
            ) {
                Text(
                    text = "Your Withdrawal Balance is: ${winningBalance.toInt()}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color(0xFF0F172A)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Info Box 2: Rules Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFEAF5EA))
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        text = "উত্তোলন সময়: সকাল ১০টা থেকে রাত ১২টা পর্যন্ত।",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = Color(0xFFD32F2F)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "সর্বনিম্ন উত্তোলন পরিমাণ ১০০ টাকা এবং সর্বোচ্চ উত্তোলন পরিমাণ ২৫,০০০ টাকা।",
                        fontSize = 13.sp,
                        color = Color(0xFF1E293B),
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "একজন ব্যবহারকারী প্রতিদিন সর্বোচ্চ ২ বার উত্তোলন করতে পারবেন।",
                        fontSize = 13.sp,
                        color = Color(0xFF1E293B),
                        lineHeight = 18.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun PaymentChannelCard(
    name: String,
    logoColor: Color,
    logoText: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cardBg = if (isSelected) Color(0xFFE2F3E5) else Color.White
    val borderColor = if (isSelected) Color(0xFF4CAF50) else Color(0xFFCBD5E1)
    val borderWidth = if (isSelected) 2.dp else 1.dp
    val textColor = if (isSelected) Color(0xFF2E7D32) else Color(0xFF334155)

    Box(
        modifier = modifier
            .height(115.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(cardBg)
            .border(borderWidth, borderColor, RoundedCornerShape(14.dp))
            .clickable { onClick() }
    ) {
        // Selected Checkmark Badge on Top-Right
        if (isSelected) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(6.dp)
                    .size(18.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF4CAF50)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Selected",
                    tint = Color.White,
                    modifier = Modifier.size(12.dp)
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Circular Logo Icon
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(logoColor),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = logoText,
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = name,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
        }
    }
}
