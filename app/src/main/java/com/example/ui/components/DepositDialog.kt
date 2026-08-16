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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BkashPink
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.NagadOrange
import com.example.ui.theme.RocketPurple
import com.example.ui.theme.SlateCardBg
import com.example.ui.theme.SlateCardBorder
import com.example.ui.theme.SlateDarkBg
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSecondary

@Composable
fun DepositDialog(
    onDismiss: () -> Unit,
    onSubmitDeposit: (method: String, amount: Double, senderNumber: String, trxId: String) -> Unit
) {
    val context = LocalContext.current
    var selectedMethod by remember { mutableStateOf("bKash") }
    var amountText by remember { mutableStateOf("100") }
    var senderNumber by remember { mutableStateOf("") }
    var trxId by remember { mutableStateOf("") }
    var errorMsg by remember { mutableStateOf<String?>(null) }

    val merchantNumbers = mapOf(
        "bKash" to "01610005046 (Personal)",
        "Nagad" to "01610005046 (Personal)",
        "Rocket" to "01610005046 (Personal)"
    )

    val selectedColor = when (selectedMethod) {
        "bKash" -> BkashPink
        "Nagad" -> NagadOrange
        else -> RocketPurple
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = SlateCardBg,
        title = {
            Text(
                text = "Add Money to Wallet (BDT)",
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontSize = 18.sp
            )
        },
        text = {
            Column {
                Text(
                    text = "Select Payment Gateway:",
                    fontSize = 12.sp,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(6.dp))

                // Payment Method Selector Badges
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    PaymentBadge("bKash", BkashPink, selectedMethod == "bKash") { selectedMethod = "bKash" }
                    PaymentBadge("Nagad", NagadOrange, selectedMethod == "Nagad") { selectedMethod = "Nagad" }
                    PaymentBadge("Rocket", RocketPurple, selectedMethod == "Rocket") { selectedMethod = "Rocket" }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Merchant Send Money Info Box
                val currentNumber = merchantNumbers[selectedMethod] ?: ""
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(selectedColor.copy(alpha = 0.15f))
                        .border(1.dp, selectedColor, RoundedCornerShape(10.dp))
                        .padding(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Send Money to $selectedMethod Number:",
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                            Text(
                                text = currentNumber,
                                fontWeight = FontWeight.Black,
                                color = Color.White,
                                fontSize = 14.sp
                            )
                        }
                        IconButton(
                            onClick = {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val numberOnly = currentNumber.split(" ").firstOrNull() ?: ""
                                val clip = ClipData.newPlainText("$selectedMethod Number", numberOnly)
                                clipboard.setPrimaryClip(clip)
                                Toast.makeText(context, "$selectedMethod number copied!", Toast.LENGTH_SHORT).show()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = "Copy Number",
                                tint = selectedColor
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Amount Input
                OutlinedTextField(
                    value = amountText,
                    onValueChange = { amountText = it; errorMsg = null },
                    label = { Text("Deposit Amount (BDT)") },
                    leadingIcon = { Icon(Icons.Default.Payment, contentDescription = null, tint = selectedColor) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier
                        .testTag("deposit_amount_input")
                        .fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = selectedColor,
                        unfocusedBorderColor = SlateCardBorder,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Sender Number Input
                OutlinedTextField(
                    value = senderNumber,
                    onValueChange = { senderNumber = it; errorMsg = null },
                    label = { Text("Sender Mobile Number") },
                    leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = selectedColor) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    singleLine = true,
                    modifier = Modifier
                        .testTag("sender_number_input")
                        .fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = selectedColor,
                        unfocusedBorderColor = SlateCardBorder,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                // TrxID Input
                OutlinedTextField(
                    value = trxId,
                    onValueChange = { trxId = it; errorMsg = null },
                    label = { Text("TrxID / Transaction Reference") },
                    leadingIcon = { Icon(Icons.Default.Receipt, contentDescription = null, tint = selectedColor) },
                    singleLine = true,
                    modifier = Modifier
                        .testTag("trx_id_input")
                        .fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = selectedColor,
                        unfocusedBorderColor = SlateCardBorder,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )

                errorMsg?.let { msg ->
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = msg, color = Color.Red, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val amt = amountText.toDoubleOrNull()
                    if (amt == null || amt < 10) {
                        errorMsg = "Minimum deposit amount is ৳10 BDT"
                        return@Button
                    }
                    if (trxId.isBlank()) {
                        errorMsg = "Please enter TrxID / Transaction ID"
                        return@Button
                    }
                    onSubmitDeposit(selectedMethod, amt, senderNumber, trxId)
                },
                modifier = Modifier.testTag("submit_deposit_button"),
                colors = ButtonDefaults.buttonColors(containerColor = selectedColor)
            ) {
                Text(text = "SUBMIT DEPOSIT", fontWeight = FontWeight.Bold, color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "CANCEL", color = TextMuted)
            }
        }
    )
}

@Composable
private fun PaymentBadge(
    name: String,
    brandColor: Color,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) brandColor else SlateDarkBg)
            .border(1.dp, if (isSelected) brandColor else SlateCardBorder, RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = name,
            fontWeight = FontWeight.Bold,
            color = if (isSelected) Color.White else TextSecondary,
            fontSize = 12.sp
        )
    }
}
