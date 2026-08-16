package com.example.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.models.JoinedMatch
import kotlinx.coroutines.flow.Flow

@Dao
interface JoinedMatchDao {
    @Query("SELECT * FROM joined_matches ORDER BY joinedTimestamp DESC")
    fun getAllJoinedMatches(): Flow<List<JoinedMatch>>

    @Query("SELECT EXISTS(SELECT 1 FROM joined_matches WHERE tournamentId = :tournamentId LIMIT 1)")
    fun isJoined(tournamentId: Long): Flow<Boolean>

    @Query("SELECT * FROM joined_matches WHERE tournamentId = :tournamentId LIMIT 1")
    suspend fun getJoinedMatchByTournament(tournamentId: Long): JoinedMatch?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(match: JoinedMatch): Long

    @Query("DELETE FROM joined_matches")
    suspend fun deleteAll()
}
