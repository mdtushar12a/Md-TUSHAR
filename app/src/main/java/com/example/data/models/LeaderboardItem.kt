package com.example.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "leaderboard")
data class LeaderboardItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val rank: Int,
    val playerName: String,
    val playerUid: String,
    val matchesWon: Int,
    val totalKills: Int,
    val totalEarningsBdt: Double
)
