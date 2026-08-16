package com.example.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tournaments")
data class Tournament(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val gameMode: String, // "Solo", "Duo", "Squad"
    val mapName: String, // "Bermuda", "Purgatory", "Kalahari", "Alpine"
    val prizePool: Int, // e.g. 500
    val entryFee: Int, // e.g. 20
    val perKillPrize: Int, // e.g. 10
    val totalSlots: Int, // e.g. 50
    val joinedSlots: Int, // e.g. 48
    val matchTime: String, // e.g. "Today, 9:00 PM"
    val status: String, // "UPCOMING", "ONGOING", "COMPLETED"
    val roomId: String = "",
    val roomPassword: String = "",
    val rulesNote: String = "No emulator / hacks allowed. Room ID given 15 mins before match time.",
    val winnerInfo: String = "",
    val startTimestamp: Long = 0L
)
