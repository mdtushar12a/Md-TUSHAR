package com.example.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey val id: Int = 1,
    val displayName: String = "Gamer",
    val email: String = "gamer@gamesclub.bd",
    val inGameName: String = "Tushar_FF",
    val inGameUid: String = "1849204812",
    val phone: String = "01700000000",
    val balance: Double = 100.0,
    val isLoggedIn: Boolean = true,
    val totalMatchesPlayed: Int = 12,
    val totalKills: Int = 45,
    val totalEarnings: Double = 380.0
)
