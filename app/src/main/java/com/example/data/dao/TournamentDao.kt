package com.example.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.models.Tournament
import kotlinx.coroutines.flow.Flow

@Dao
interface TournamentDao {
    @Query("SELECT * FROM tournaments WHERE status != 'COMPLETED' ORDER BY startTimestamp ASC, id ASC")
    fun getAllTournaments(): Flow<List<Tournament>>

    @Query("SELECT * FROM tournaments WHERE status = 'COMPLETED' ORDER BY startTimestamp DESC")
    fun getCompletedTournaments(): Flow<List<Tournament>>

    @Query("SELECT * FROM tournaments WHERE id = :id LIMIT 1")
    suspend fun getTournamentById(id: Long): Tournament?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(tournaments: List<Tournament>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(tournament: Tournament): Long

    @Query("UPDATE tournaments SET joinedSlots = joinedSlots + 1 WHERE id = :id AND joinedSlots < totalSlots")
    suspend fun incrementJoinedSlots(id: Long): Int

    @Query("UPDATE tournaments SET status = 'COMPLETED', winnerInfo = :winnerInfo WHERE id = :id")
    suspend fun markCompleted(id: Long, winnerInfo: String)

    @Query("UPDATE tournaments SET roomId = :roomId, roomPassword = :roomPassword WHERE id = :id")
    suspend fun updateRoomCredentials(id: Long, roomId: String, roomPassword: String)

    @Query("SELECT * FROM tournaments WHERE status != 'COMPLETED' AND startTimestamp > 0")
    suspend fun getActiveTournamentsList(): List<Tournament>

    @Query("DELETE FROM tournaments WHERE id = :id")
    suspend fun deleteTournament(id: Long)

    @Query("SELECT COUNT(*) FROM tournaments")
    suspend fun getCount(): Int

    @Query("DELETE FROM tournaments")
    suspend fun deleteAll()
}
