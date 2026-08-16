package com.example.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wallet_transactions")
data class WalletTransaction(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val type: String, // "DEPOSIT", "WITHDRAWAL", "ENTRY_FEE", "WINNINGS"
    val amount: Double,
    val paymentMethod: String, // "bKash", "Nagad", "Rocket", "Match Prize", "Entry Fee"
    val accountNumber: String = "",
    val trxId: String = "",
    val status: String = "SUCCESS", // "SUCCESS", "PENDING", "FAILED"
    val timestamp: Long = System.currentTimeMillis()
)
