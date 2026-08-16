package com.example.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "joined_matches")
data class JoinedMatch(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val tournamentId: Long,
    val tournamentTitle: String,
    val gameMode: String,
    val matchTime: String,
    val entryFee: Int,
    val inGameName: String,
    val inGameUid: String,
    val slotNumber: Int,
    val joinedTimestamp: Long = System.currentTimeMillis()
)
