package com.example.ui.components

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Phone
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BkashPink
import com.example.ui.theme.NagadOrange
import com.example.ui.theme.RocketPurple
import com.example.ui.theme.SlateCardBg
import com.example.ui.theme.SlateCardBorder
import com.example.ui.theme.SlateDarkBg
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSecondary

@Composable
fun WithdrawDialog(
    userBalance: Double,
    onDismiss: () -> Unit,
    onSubmitWithdrawal: (method: String, amount: Double, accountNumber: String) -> Unit
) {
    var selectedMethod by remember { mutableStateOf("bKash") }
    var amountText by remember { mutableStateOf("100") }
    var accountNumber by remember { mutableStateOf("") }
    var errorMsg by remember { mutableStateOf<String?>(null) }

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
                text = "Withdraw Winnings (BDT)",
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontSize = 18.sp
            )
        },
        text = {
            Column {
                Text(
                    text = "Available Balance: ৳ ${userBalance.toInt()} BDT",
                    fontWeight = FontWeight.Bold,
                    color = Color.Green,
                    fontSize = 13.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Select Withdrawal Gateway:",
                    fontSize = 12.sp,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    WithdrawBadge("bKash", BkashPink, selectedMethod == "bKash") { selectedMethod = "bKash" }
                    WithdrawBadge("Nagad", NagadOrange, selectedMethod == "Nagad") { selectedMethod = "Nagad" }
                    WithdrawBadge("Rocket", RocketPurple, selectedMethod == "Rocket") { selectedMethod = "Rocket" }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Account Number Input
                OutlinedTextField(
                    value = accountNumber,
                    onValueChange = { accountNumber = it; errorMsg = null },
                    label = { Text("$selectedMethod Account Number") },
                    leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = selectedColor) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    singleLine = true,
                    modifier = Modifier
                        .testTag("withdraw_account_input")
                        .fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = selectedColor,
                        unfocusedBorderColor = SlateCardBorder,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Amount Input
                OutlinedTextField(
                    value = amountText,
                    onValueChange = { amountText = it; errorMsg = null },
                    label = { Text("Withdraw Amount (Min ৳50 BDT)") },
                    leadingIcon = { Icon(Icons.Default.AccountBalanceWallet, contentDescription = null, tint = selectedColor) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier
                        .testTag("withdraw_amount_input")
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
                    if (amt == null || amt < 50) {
                        errorMsg = "Minimum withdrawal amount is ৳50 BDT"
                        return@Button
                    }
                    if (amt > userBalance) {
                        errorMsg = "Amount exceeds your wallet balance!"
                        return@Button
                    }
                    if (accountNumber.isBlank()) {
                        errorMsg = "Please enter your $selectedMethod number"
                        return@Button
                    }
                    onSubmitWithdrawal(selectedMethod, amt, accountNumber)
                },
                modifier = Modifier.testTag("submit_withdraw_button"),
                colors = ButtonDefaults.buttonColors(containerColor = selectedColor)
            ) {
                Text(text = "REQUEST WITHDRAWAL", fontWeight = FontWeight.Bold, color = Color.White)
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
private fun WithdrawBadge(
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
