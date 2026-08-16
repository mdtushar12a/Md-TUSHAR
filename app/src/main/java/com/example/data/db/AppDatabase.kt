package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.dao.JoinedMatchDao
import com.example.data.dao.LeaderboardDao
import com.example.data.dao.TournamentDao
import com.example.data.dao.UserDao
import com.example.data.dao.WalletDao
import com.example.data.models.JoinedMatch
import com.example.data.models.LeaderboardItem
import com.example.data.models.Tournament
import com.example.data.models.UserProfile
import com.example.data.models.WalletTransaction

@Database(
    entities = [
        UserProfile::class,
        Tournament::class,
        JoinedMatch::class,
        WalletTransaction::class,
        LeaderboardItem::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun tournamentDao(): TournamentDao
    abstract fun joinedMatchDao(): JoinedMatchDao
    abstract fun walletDao(): WalletDao
    abstract fun leaderboardDao(): LeaderboardDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "games_club_bd.db"
                ).fallbackToDestructiveMigration()
                 .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
