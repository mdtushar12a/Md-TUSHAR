package com.example.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.models.LeaderboardItem
import kotlinx.coroutines.flow.Flow

@Dao
interface LeaderboardDao {
    @Query("SELECT * FROM leaderboard ORDER BY rank ASC")
    fun getLeaderboard(): Flow<List<LeaderboardItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<LeaderboardItem>)

    @Query("SELECT COUNT(*) FROM leaderboard")
    suspend fun getCount(): Int

    @Query("DELETE FROM leaderboard")
    suspend fun deleteAll()
}
