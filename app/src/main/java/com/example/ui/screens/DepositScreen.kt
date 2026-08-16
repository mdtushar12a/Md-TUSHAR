package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import java.util.Locale

@Composable
fun DepositScreen(
    userProfile: UserProfile?,
    onBack: () -> Unit,
    onSubmitDeposit: (method: String, amount: Double, senderNumber: String, trxId: String) -> Unit = { _, _, _, _ -> }
) {
    val context = LocalContext.current
    var currentStep by remember { mutableStateOf(1) }
    var amountText by remember { mutableStateOf("") }
    var selectedMethod by remember { mutableStateOf("bKash") }
    var transactionId by remember { mutableStateOf("") }
    var senderPhone by remember { mutableStateOf("") }
    var selectedTab by remember { mutableStateOf("Mobile Banking") }

    val invoiceNumber = remember { "INV${System.currentTimeMillis().toString().takeLast(12)}" }
    val agentNumber = "01610005046"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
    ) {
        // Dark Top Header Bar (Appears on all 3 steps)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF0B1017))
                .padding(horizontal = 14.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    if (currentStep > 1) {
                        currentStep -= 1
                    } else {
                        onBack()
                    }
                },
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
                text = "Deposit",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        // STEP 1: Enter Amount
        if (currentStep == 1) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(20.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.weight(1f))

                // Amount Input
                OutlinedTextField(
                    value = amountText,
                    onValueChange = { amountText = it },
                    placeholder = {
                        Text(
                            text = "Amount",
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E293B),
                            fontSize = 15.sp
                        )
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = Color(0xFF1E88E5),
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

                // CONTINUE Button
                Button(
                    onClick = {
                        val amt = amountText.toDoubleOrNull()
                        if (amt == null || amt < 10) {
                            Toast.makeText(context, "সঠিক ডিপোজিট পরিমাণ লিখুন (যেমন: ১০০ BDT)", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        currentStep = 2
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E88E5)),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text(
                        text = "CONTINUE",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Video Tutorial Text
                Text(
                    text = "কিভাবে টাকা ডিপোজিট/পেমেন্ট করবেন?",
                    fontSize = 13.sp,
                    color = Color(0xFF757575),
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "ভিডিওটি দেখুন",
                    fontSize = 14.sp,
                    color = Color(0xFF1E88E5),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com"))
                        context.startActivity(intent)
                    }
                )

                Spacer(modifier = Modifier.weight(1f))
            }
        }

        // STEP 2: Select Gateway
        else if (currentStep == 2) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                // Top Circular Close Button
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE0F7FA))
                        .clickable { currentStep = 1 },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Color(0xFF00897B),
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // User Summary White Card
                val amtVal = amountText.toDoubleOrNull() ?: 100.0
                val userName = userProfile?.displayName ?: "Md TUSHAR"

                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp)
                    ) {
                        Text(
                            text = userName,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF546E7A)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = String.format(Locale.US, "%.2f BDT", amtVal),
                            fontSize = 28.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF00897B)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Invoice: $invoiceNumber",
                            fontSize = 11.sp,
                            color = Color(0xFF90A4AE)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Tabs: Mobile Banking vs Crypto
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Column(
                        modifier = Modifier.clickable { selectedTab = "Mobile Banking" }
                    ) {
                        Text(
                            text = "Mobile Banking",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (selectedTab == "Mobile Banking") Color(0xFF00897B) else Color(0xFF78909C)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Box(
                            modifier = Modifier
                                .width(120.dp)
                                .height(3.dp)
                                .background(if (selectedTab == "Mobile Banking") Color(0xFF00897B) else Color.Transparent)
                        )
                    }

                    Spacer(modifier = Modifier.width(24.dp))

                    Column(
                        modifier = Modifier.clickable {
                            selectedTab = "Crypto"
                            Toast.makeText(context, "Crypto option coming soon!", Toast.LENGTH_SHORT).show()
                        }
                    ) {
                        Text(
                            text = "Crypto",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (selectedTab == "Crypto") Color(0xFF00897B) else Color(0xFF78909C)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Box(
                            modifier = Modifier
                                .width(60.dp)
                                .height(3.dp)
                                .background(if (selectedTab == "Crypto") Color(0xFF00897B) else Color.Transparent)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Gateways Grid (Row 1: bKash, Nagad, Rocket)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    DepositGatewayCard(
                        name = "বিকাশ",
                        logoColor = Color(0xFFE91E63),
                        logoText = "bKash",
                        onClick = {
                            selectedMethod = "bKash"
                            currentStep = 3
                        },
                        modifier = Modifier.weight(1f)
                    )

                    DepositGatewayCard(
                        name = "নগদ",
                        logoColor = Color(0xFFE65100),
                        logoText = "নগদ",
                        onClick = {
                            selectedMethod = "Nagad"
                            currentStep = 3
                        },
                        modifier = Modifier.weight(1f)
                    )

                    DepositGatewayCard(
                        name = "রকেট",
                        logoColor = Color(0xFF7B1FA2),
                        logoText = "রকেট",
                        onClick = {
                            selectedMethod = "Rocket"
                            currentStep = 3
                        },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Gateways Grid (Row 2: Upay, bKash Agent, Nagad Agent)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    DepositGatewayCard(
                        name = "উপায়",
                        logoColor = Color(0xFFFBC02D),
                        logoText = "উপায়",
                        onClick = {
                            selectedMethod = "Upay"
                            currentStep = 3
                        },
                        modifier = Modifier.weight(1f)
                    )

                    DepositGatewayCard(
                        name = "বিকাশ এজেন্ট",
                        logoColor = Color(0xFF81D4FA),
                        logoText = "bKash",
                        badgeText = "🎁 1.8% Cashback",
                        subText = "Min: 300 BDT",
                        onClick = {
                            selectedMethod = "bKash Agent"
                            currentStep = 3
                        },
                        modifier = Modifier.weight(1f)
                    )

                    DepositGatewayCard(
                        name = "নগদ এজেন্ট",
                        logoColor = Color(0xFFFFAB91),
                        logoText = "নগদ",
                        badgeText = "🎁 1.3% Cashback",
                        subText = "Min: 300 BDT",
                        onClick = {
                            selectedMethod = "Nagad Agent"
                            currentStep = 3
                        },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(30.dp))

                // Security Note at Bottom
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = Color(0xFF78909C),
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Your payment is secured with 256-bit encryption",
                        fontSize = 12.sp,
                        color = Color(0xFF78909C)
                    )
                }
            }
        }

        // STEP 3: Enter Transaction ID & Instructions
        else if (currentStep == 3) {
            val amtVal = amountText.toDoubleOrNull() ?: 100.0
            val userName = userProfile?.displayName ?: "Md TUSHAR"

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                // Top Circular Back Arrow
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE0F7FA))
                        .clickable { currentStep = 2 },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color(0xFF00897B),
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Summary Card with Gateway Logo
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = userName,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF546E7A)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = String.format(Locale.US, "%.2f BDT", amtVal),
                                fontSize = 26.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF00897B)
                            )
                        }

                        // Selected Gateway Logo Badge
                        val gatewayBg = when {
                            selectedMethod.contains("bKash") -> Color(0xFFE91E63)
                            selectedMethod.contains("Nagad") -> Color(0xFFE65100)
                            selectedMethod.contains("Rocket") -> Color(0xFF7B1FA2)
                            else -> Color(0xFFFBC02D)
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(gatewayBg)
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = selectedMethod,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Input 1: Transaction ID
                Text(
                    text = "Transaction ID",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF475569)
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = transactionId,
                    onValueChange = { transactionId = it },
                    placeholder = { Text("Enter your transaction ID", color = Color(0xFF94A3B8), fontSize = 14.sp) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = Color(0xFF00897B),
                        unfocusedBorderColor = Color(0xFFE2E8F0),
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Input 2: Sender Phone Number
                Text(
                    text = "Sender Phone Number",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF475569)
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = senderPhone,
                    onValueChange = { senderPhone = it },
                    placeholder = { Text("Enter sender phone number (01XXXXXXXXX)", color = Color(0xFF94A3B8), fontSize = 14.sp) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = Color(0xFF00897B),
                        unfocusedBorderColor = Color(0xFFE2E8F0),
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                )

                Spacer(modifier = Modifier.height(18.dp))

                // Verify Payment Button
                Button(
                    onClick = {
                        if (transactionId.trim().isEmpty() || senderPhone.trim().isEmpty()) {
                            Toast.makeText(context, "অনুগ্রহ করে Transaction ID এবং Sender Phone Number প্রদান করুন", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        onSubmitDeposit(selectedMethod, amtVal, senderPhone, transactionId)
                        Toast.makeText(context, "পেমেন্ট ভেরিফিকেশন সফল হয়েছে! ১২ ঘণ্টার মধ্যে ব্যালেন্স যোগ হবে।", Toast.LENGTH_LONG).show()
                        onBack()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00897B)),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text(
                        text = "Verify Payment",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Instructions Card
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        // Step 1
                        InstructionRow(
                            stepNumber = "1",
                            content = {
                                Text(
                                    text = "Dial *247# or open the $selectedMethod app.",
                                    fontSize = 13.sp,
                                    color = Color(0xFF334155)
                                )
                            }
                        )

                        // Step 2
                        InstructionRow(
                            stepNumber = "2",
                            content = {
                                Row {
                                    Text(text = "Choose: ", fontSize = 13.sp, color = Color(0xFF334155))
                                    Text(
                                        text = "Send Money",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF00897B)
                                    )
                                }
                            }
                        )

                        // Step 3
                        InstructionRow(
                            stepNumber = "3",
                            content = {
                                Column {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(text = "Enter the Number: ", fontSize = 13.sp, color = Color(0xFF334155))
                                        Text(
                                            text = agentNumber,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF00897B)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        CopyPillButton(textToCopy = agentNumber, context = context)
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))

                                    // Warning Box
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(Color(0xFFFFF8E1))
                                            .border(1.dp, Color(0xFFFFE082), RoundedCornerShape(8.dp))
                                            .padding(10.dp)
                                    ) {
                                        Text(
                                            text = "⚠️ সতর্কতা : উপরের দেওয়া ডিপোজিট নাম্বারটি প্রতি ৩০ মিনিট পর পর পরিবর্তন করা হয়। তাই টাকা পাঠানোর আগে অবশ্যই উপরের নাম্বারটি কপি করুন অথবা ভালোভাবে মিলিয়ে নিশ্চিত হয়ে তারপর টাকা পাঠান। পুরনো বা সেভ করা নাম্বারে টাকা পাঠালে সেই টাকা আপনার একাউন্টে যোগ হবে না।",
                                            fontSize = 11.sp,
                                            color = Color(0xFF795548),
                                            lineHeight = 16.sp
                                        )
                                    }
                                }
                            }
                        )

                        // Step 4
                        InstructionRow(
                            stepNumber = "4",
                            content = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = "Enter the Amount: ", fontSize = 13.sp, color = Color(0xFF334155))
                                    Text(
                                        text = String.format(Locale.US, "%.2f BDT", amtVal),
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF00897B)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    CopyPillButton(textToCopy = "${amtVal.toInt()}", context = context)
                                }
                            }
                        )

                        // Step 5
                        InstructionRow(
                            stepNumber = "5",
                            content = {
                                Text(
                                    text = "Now enter your $selectedMethod PIN to confirm.",
                                    fontSize = 13.sp,
                                    color = Color(0xFF334155)
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun InstructionRow(
    stepNumber: String,
    content: @Composable () -> Unit
) {
    Row(
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(22.dp)
                .clip(CircleShape)
                .background(Color(0xFFE0F7FA)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stepNumber,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF00897B)
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Box(modifier = Modifier.weight(1f)) {
            content()
        }
    }
}

@Composable
private fun CopyPillButton(textToCopy: String, context: Context) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(Color(0xFFE0F7FA))
            .clickable {
                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("Copied", textToCopy)
                clipboard.setPrimaryClip(clip)
                Toast.makeText(context, "$textToCopy কপি করা হয়েছে", Toast.LENGTH_SHORT).show()
            }
            .padding(horizontal = 10.dp, vertical = 3.dp)
    ) {
        Text(
            text = "Copy",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF00897B)
        )
    }
}

@Composable
private fun DepositGatewayCard(
    name: String,
    logoColor: Color,
    logoText: String,
    badgeText: String? = null,
    subText: String? = null,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(120.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(Color.White)
            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(14.dp))
            .clickable { onClick() }
    ) {
        // Cashback Badge on Top
        badgeText?.let {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 14.dp, topEnd = 14.dp))
                    .background(Color(0xFFFFEBEE))
                    .padding(vertical = 3.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = it,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFD32F2F)
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (badgeText != null) {
                Spacer(modifier = Modifier.height(10.dp))
            }

            // Circular Logo Icon
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(logoColor),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = logoText,
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = name,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF334155),
                textAlign = TextAlign.Center
            )

            subText?.let {
                Text(
                    text = it,
                    fontSize = 9.sp,
                    color = Color(0xFF94A3B8)
                )
            }
        }
    }
}
