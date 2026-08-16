package com.example.data.repository

import com.example.data.db.AppDatabase
import com.example.data.models.JoinedMatch
import com.example.data.models.LeaderboardItem
import com.example.data.models.Tournament
import com.example.data.models.UserProfile
import com.example.data.models.WalletTransaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class GamesClubRepository(private val db: AppDatabase) {
    val userProfile: Flow<UserProfile?> = db.userDao().getUserProfile()
    val tournaments: Flow<List<Tournament>> = db.tournamentDao().getAllTournaments()
    val joinedMatches: Flow<List<JoinedMatch>> = db.joinedMatchDao().getAllJoinedMatches()
    val transactions: Flow<List<WalletTransaction>> = db.walletDao().getAllTransactions()
    val leaderboard: Flow<List<LeaderboardItem>> = db.leaderboardDao().getLeaderboard()

    fun isJoined(tournamentId: Long): Flow<Boolean> = db.joinedMatchDao().isJoined(tournamentId)

    suspend fun deductBalance(amount: Double): Result<Unit> {
        val user = db.userDao().getUserProfileOnce() ?: return Result.failure(Exception("User not found"))
        if (user.balance < amount) {
            return Result.failure(Exception("Insufficient balance"))
        }
        val newBalance = user.balance - amount
        db.userDao().updateProfile(user.copy(balance = newBalance))
        return Result.success(Unit)
    }

    suspend fun resetAllDataToZero() {
        db.userDao().deleteAll()
        db.tournamentDao().deleteAll()
        db.joinedMatchDao().deleteAll()
        db.walletDao().deleteAll()
        db.leaderboardDao().deleteAll()

        // Clean user profile starting at 0
        val uniqueGamerUid = (10000000..99999999).random().toString()
        db.userDao().insertOrUpdate(
            UserProfile(
                id = 1,
                displayName = "Pro Gamer",
                email = "gamer@gamesclub.bd",
                inGameName = "Gamer_BD",
                inGameUid = uniqueGamerUid,
                phone = "01700000000",
                balance = 0.0,
                isLoggedIn = true,
                totalMatchesPlayed = 0,
                totalKills = 0,
                totalEarnings = 0.0
            )
        )
        seedTournamentsOnly()
    }

    private fun formatMatchTime(timeMillis: Long): String {
        val calNow = Calendar.getInstance()
        val calMatch = Calendar.getInstance().apply { timeInMillis = timeMillis }
        val timeFormat = SimpleDateFormat("hh:mm a", Locale.ENGLISH)
        val timeStr = timeFormat.format(Date(timeMillis))

        return when {
            calNow.get(Calendar.DAY_OF_YEAR) == calMatch.get(Calendar.DAY_OF_YEAR) &&
            calNow.get(Calendar.YEAR) == calMatch.get(Calendar.YEAR) -> "Today, $timeStr"

            calNow.get(Calendar.DAY_OF_YEAR) + 1 == calMatch.get(Calendar.DAY_OF_YEAR) &&
            calNow.get(Calendar.YEAR) == calMatch.get(Calendar.YEAR) -> "Tomorrow, $timeStr"

            else -> {
                val dateFormat = SimpleDateFormat("dd MMM, hh:mm a", Locale.ENGLISH)
                dateFormat.format(Date(timeMillis))
            }
        }
    }

    suspend fun seedTournamentsOnly() {
        db.tournamentDao().deleteAll()
        db.leaderboardDao().deleteAll()

        val allTournaments = mutableListOf<Tournament>()
        val currentTime = System.currentTimeMillis()
        val baseStartTime = currentTime + 10 * 60 * 1000L // First match starts in 10 mins

        val mapsList = listOf("Bermuda", "Purgatory", "Kalahari", "Alpine", "NexTerra")

        // -------------------------------------------------------------
        // 1. BR MATCH (30 Matches: #101 to #130) - 1 hour apart, offset 0 min
        // -------------------------------------------------------------
        val brFees = listOf(0, 10, 15, 20, 25, 30, 40, 50)
        val brTypes = listOf("Solo", "Duo", "Squad")
        val mode0Offset = 0 * 10 * 60 * 1000L
        for (i in 0 until 30) {
            val matchNum = 101 + i
            val startMillis = baseStartTime + mode0Offset + (i * 60 * 60 * 1000L)
            val mapName = mapsList[i % mapsList.size]
            val type = brTypes[i % brTypes.size]
            val totalSlots = if (type == "Squad") 48 else 50
            val fee = brFees[i % brFees.size]
            val perKill = if (fee == 0) 2 else minOf(5, (fee / 5).coerceAtLeast(3))
            val prizePool = if (fee == 0) 100 else (totalSlots * fee * 0.60).toInt()

            allTournaments.add(
                Tournament(
                    title = "BR Match $type $mapName #$matchNum",
                    gameMode = "BR MATCH",
                    mapName = mapName,
                    prizePool = prizePool,
                    entryFee = fee,
                    perKillPrize = perKill,
                    totalSlots = totalSlots,
                    joinedSlots = 0,
                    matchTime = formatMatchTime(startMillis),
                    status = "UPCOMING",
                    roomId = if (i == 0) "FFBD-8821" else "",
                    roomPassword = if (i == 0) "4412" else "",
                    rulesNote = "BR $type Battle. 60% Prize Pool distributed. Max ৳5 per kill.",
                    startTimestamp = startMillis
                )
            )
        }

        // -------------------------------------------------------------
        // 2. CS 4 VS 4 (30 Matches: #201 to #230) - 1 hour apart, offset 10 min
        // -------------------------------------------------------------
        val csFees = listOf(0, 15, 20, 25, 30, 40, 50, 60)
        val mode1Offset = 1 * 10 * 60 * 1000L
        for (i in 0 until 30) {
            val matchNum = 201 + i
            val startMillis = baseStartTime + mode1Offset + (i * 60 * 60 * 1000L)
            val mapName = mapsList[i % mapsList.size]
            val totalSlots = 8
            val fee = csFees[i % csFees.size]
            val perKill = if (fee == 0) 2 else minOf(5, (fee / 10).coerceAtLeast(3))
            val prizePool = if (fee == 0) 50 else (totalSlots * fee * 0.60).toInt()

            allTournaments.add(
                Tournament(
                    title = "CS 4v4 Clash Battle #$matchNum",
                    gameMode = "CS 4 VS 4",
                    mapName = mapName,
                    prizePool = prizePool,
                    entryFee = fee,
                    perKillPrize = perKill,
                    totalSlots = totalSlots,
                    joinedSlots = 0,
                    matchTime = formatMatchTime(startMillis),
                    status = "UPCOMING",
                    roomId = if (i == 0) "CS-4401" else "",
                    roomPassword = if (i == 0) "1122" else "",
                    rulesNote = "CS 4 VS 4 Tournament. 60% Prize Pool to winners.",
                    startTimestamp = startMillis
                )
            )
        }

        // -------------------------------------------------------------
        // 3. PRO LEAGUE (30 Matches: #301 to #330) - 1 hour apart, offset 20 min
        // -------------------------------------------------------------
        val proFees = listOf(0, 25, 30, 40, 50, 60, 80, 100)
        val mode2Offset = 2 * 10 * 60 * 1000L
        for (i in 0 until 30) {
            val matchNum = 301 + i
            val startMillis = baseStartTime + mode2Offset + (i * 60 * 60 * 1000L)
            val mapName = mapsList[i % mapsList.size]
            val totalSlots = if (i % 2 == 0) 48 else 50
            val fee = proFees[i % proFees.size]
            val perKill = if (fee == 0) 2 else 5 // Max ৳5 kill
            val prizePool = if (fee == 0) 100 else (totalSlots * fee * 0.60).toInt()

            allTournaments.add(
                Tournament(
                    title = "Pro League Championship #$matchNum",
                    gameMode = "PRO LEAGUE",
                    mapName = mapName,
                    prizePool = prizePool,
                    entryFee = fee,
                    perKillPrize = perKill,
                    totalSlots = totalSlots,
                    joinedSlots = 0,
                    matchTime = formatMatchTime(startMillis),
                    status = "UPCOMING",
                    roomId = "",
                    roomPassword = "",
                    rulesNote = "Pro League Tournament. 60% Prize Pool. Strict anti-cheat rules.",
                    startTimestamp = startMillis
                )
            )
        }

        // -------------------------------------------------------------
        // 4. SURVIVAL (30 Matches: #401 to #430) - 1 hour apart, offset 30 min
        // -------------------------------------------------------------
        val survFees = listOf(0, 10, 15, 20, 25, 30, 40, 50)
        val mode3Offset = 3 * 10 * 60 * 1000L
        for (i in 0 until 30) {
            val matchNum = 401 + i
            val startMillis = baseStartTime + mode3Offset + (i * 60 * 60 * 1000L)
            val mapName = mapsList[i % mapsList.size]
            val totalSlots = 50
            val fee = survFees[i % survFees.size]
            val perKill = if (fee == 0) 2 else minOf(5, (fee / 5).coerceAtLeast(3))
            val prizePool = if (fee == 0) 60 else (totalSlots * fee * 0.60).toInt()

            allTournaments.add(
                Tournament(
                    title = "Survival Royale $mapName #$matchNum",
                    gameMode = "SURVIVAL",
                    mapName = mapName,
                    prizePool = prizePool,
                    entryFee = fee,
                    perKillPrize = perKill,
                    totalSlots = totalSlots,
                    joinedSlots = 0,
                    matchTime = formatMatchTime(startMillis),
                    status = "UPCOMING",
                    roomId = "",
                    roomPassword = "",
                    rulesNote = "Survival Match. Last team standing wins big.",
                    startTimestamp = startMillis
                )
            )
        }

        // -------------------------------------------------------------
        // 5. CLASH SQUAD (30 Matches: #501 to #530) - 1 hour apart, offset 40 min
        // -------------------------------------------------------------
        val csRanksFees = listOf(0, 15, 20, 25, 30, 40, 50)
        val mode4Offset = 4 * 10 * 60 * 1000L
        for (i in 0 until 30) {
            val matchNum = 501 + i
            val startMillis = baseStartTime + mode4Offset + (i * 60 * 60 * 1000L)
            val mapName = mapsList[i % mapsList.size]
            val totalSlots = if (i % 2 == 0) 8 else 16
            val fee = csRanksFees[i % csRanksFees.size]
            val perKill = if (fee == 0) 2 else minOf(5, (fee / 6).coerceAtLeast(3))
            val prizePool = if (fee == 0) 50 else (totalSlots * fee * 0.60).toInt()

            allTournaments.add(
                Tournament(
                    title = "Clash Squad Ranked #$matchNum",
                    gameMode = "CLASH SQUAD",
                    mapName = mapName,
                    prizePool = prizePool,
                    entryFee = fee,
                    perKillPrize = perKill,
                    totalSlots = totalSlots,
                    joinedSlots = 0,
                    matchTime = formatMatchTime(startMillis),
                    status = "UPCOMING",
                    roomId = "",
                    roomPassword = "",
                    rulesNote = "Clash Squad Match. 60% Prize Pool distribution.",
                    startTimestamp = startMillis
                )
            )
        }

        // -------------------------------------------------------------
        // 6. LONE WOLF (30 Matches: #601 to #630) - 1 hour apart, offset 50 min
        // -------------------------------------------------------------
        val loneFees = listOf(0, 10, 15, 20, 25, 30, 40, 50)
        val mode5Offset = 5 * 10 * 60 * 1000L
        for (i in 0 until 30) {
            val matchNum = 601 + i
            val startMillis = baseStartTime + mode5Offset + (i * 60 * 60 * 1000L)
            val totalSlots = 2
            val fee = loneFees[i % loneFees.size]
            val perKill = if (fee == 0) 2 else minOf(5, (fee / 5).coerceAtLeast(2))
            val prizePool = if (fee == 0) 30 else (totalSlots * fee * 0.60).toInt()

            allTournaments.add(
                Tournament(
                    title = "Lone Wolf 1v1 Duel #$matchNum",
                    gameMode = "LONE WOLF",
                    mapName = "Iron Cage",
                    prizePool = prizePool,
                    entryFee = fee,
                    perKillPrize = perKill,
                    totalSlots = totalSlots,
                    joinedSlots = 0,
                    matchTime = formatMatchTime(startMillis),
                    status = "UPCOMING",
                    roomId = "",
                    roomPassword = "",
                    rulesNote = "Lone Wolf 1v1 Duel (2 Slots). Winner takes 60% pool.",
                    startTimestamp = startMillis
                )
            )
        }

        // -------------------------------------------------------------
        // 7. HEADSHOT ONLY (30 Matches: #701 to #730) - 1 hour apart, offset 0 min (+10 min staggered)
        // -------------------------------------------------------------
        val hsFees = listOf(0, 15, 20, 25, 30, 40, 50)
        val mode6Offset = 6 * 10 * 60 * 1000L
        for (i in 0 until 30) {
            val matchNum = 701 + i
            val startMillis = baseStartTime + mode6Offset + (i * 60 * 60 * 1000L)
            val mapName = mapsList[i % mapsList.size]
            val totalSlots = if (i % 2 == 0) 50 else 20
            val fee = hsFees[i % hsFees.size]
            val perKill = if (fee == 0) 2 else 5 // Max ৳5 kill
            val prizePool = if (fee == 0) 60 else (totalSlots * fee * 0.60).toInt()

            allTournaments.add(
                Tournament(
                    title = "Headshot Only Master #$matchNum",
                    gameMode = "HEADSHOT ONLY",
                    mapName = mapName,
                    prizePool = prizePool,
                    entryFee = fee,
                    perKillPrize = perKill,
                    totalSlots = totalSlots,
                    joinedSlots = 0,
                    matchTime = formatMatchTime(startMillis),
                    status = "UPCOMING",
                    roomId = "",
                    roomPassword = "",
                    rulesNote = "Headshot Only Tournament. Precision shots only.",
                    startTimestamp = startMillis
                )
            )
        }

        // -------------------------------------------------------------
        // 8. LOSE TO WIN (30 Matches: #801 to #830) - 1 hour apart, offset 70 min
        // -------------------------------------------------------------
        val ltwFees = listOf(0, 10, 15, 20, 25, 30, 40)
        val mode7Offset = 7 * 10 * 60 * 1000L
        for (i in 0 until 30) {
            val matchNum = 801 + i
            val startMillis = baseStartTime + mode7Offset + (i * 60 * 60 * 1000L)
            val mapName = mapsList[i % mapsList.size]
            val totalSlots = 50
            val fee = ltwFees[i % ltwFees.size]
            val perKill = if (fee == 0) 2 else minOf(5, (fee / 5).coerceAtLeast(3))
            val prizePool = if (fee == 0) 60 else (totalSlots * fee * 0.60).toInt()

            allTournaments.add(
                Tournament(
                    title = "Lose To Win Reverse Battle #$matchNum",
                    gameMode = "LOSE TO WIN",
                    mapName = mapName,
                    prizePool = prizePool,
                    entryFee = fee,
                    perKillPrize = perKill,
                    totalSlots = totalSlots,
                    joinedSlots = 0,
                    matchTime = formatMatchTime(startMillis),
                    status = "UPCOMING",
                    roomId = "",
                    roomPassword = "",
                    rulesNote = "Reverse Elimination Tournament. 60% Prize Pool to players.",
                    startTimestamp = startMillis
                )
            )
        }

        // -------------------------------------------------------------
        // 9. LUDO KING (30 Matches: #901 to #930) - 1 hour apart, offset 80 min
        // -------------------------------------------------------------
        val ludoFees = listOf(0, 10, 15, 20, 25, 30, 40, 50)
        val ludoBoards = listOf("Classic Board", "Quick Board", "Master Board")
        val mode8Offset = 8 * 10 * 60 * 1000L
        for (i in 0 until 30) {
            val matchNum = 901 + i
            val startMillis = baseStartTime + mode8Offset + (i * 60 * 60 * 1000L)
            val is1v1 = i % 2 == 0
            val totalSlots = if (is1v1) 2 else 4
            val fee = ludoFees[i % ludoFees.size]
            val boardName = ludoBoards[i % ludoBoards.size]
            val prizePool = if (fee == 0) 25 else (totalSlots * fee * 0.60).toInt()

            allTournaments.add(
                Tournament(
                    title = "Ludo King ${if (is1v1) "1v1 Classic" else "4-Player Battle"} #$matchNum",
                    gameMode = "LUDO KING",
                    mapName = boardName,
                    prizePool = prizePool,
                    entryFee = fee,
                    perKillPrize = 0,
                    totalSlots = totalSlots,
                    joinedSlots = 0,
                    matchTime = formatMatchTime(startMillis),
                    status = "UPCOMING",
                    roomId = if (i == 0) "LUDO-8812" else "",
                    roomPassword = "",
                    rulesNote = "Ludo King Tournament ($totalSlots Slots). 60% Prize Pool to winner.",
                    startTimestamp = startMillis
                )
            )
        }

        db.tournamentDao().insertAll(allTournaments)

        // Leaderboard starting at 0
        val cleanLeaders = listOf(
            LeaderboardItem(rank = 1, playerName = "Viper_King_BD", playerUid = "109823412", matchesWon = 0, totalKills = 0, totalEarningsBdt = 0.0),
            LeaderboardItem(rank = 2, playerName = "Pro_Sniper_BD", playerUid = "220918349", matchesWon = 0, totalKills = 0, totalEarningsBdt = 0.0),
            LeaderboardItem(rank = 3, playerName = "Cyber_Booyah", playerUid = "559102934", matchesWon = 0, totalKills = 0, totalEarningsBdt = 0.0)
        )
        db.leaderboardDao().insertAll(cleanLeaders)
    }

    suspend fun seedInitialDataIfNeeded() {
        val user = db.userDao().getUserProfileOnce()
        if (user == null) {
            val uniqueGamerUid = (10000000..99999999).random().toString()
            db.userDao().insertOrUpdate(
                UserProfile(
                    id = 1,
                    displayName = "Pro Gamer",
                    email = "gamer@gamesclub.bd",
                    inGameName = "Gamer_BD",
                    inGameUid = uniqueGamerUid,
                    phone = "01700000000",
                    balance = 0.0,
                    isLoggedIn = true,
                    totalMatchesPlayed = 0,
                    totalKills = 0,
                    totalEarnings = 0.0
                )
            )
        } else if (user.inGameUid.isBlank() || user.inGameUid == "10000000" || user.inGameUid == "1849204812") {
            val newUniqueUid = (10000000..99999999).random().toString()
            db.userDao().updateProfile(user.copy(inGameUid = newUniqueUid))
        }

        val tCount = db.tournamentDao().getCount()
        val firstTournament = db.tournamentDao().getTournamentById(1)
        val currentTime = System.currentTimeMillis()
        if (tCount < 270 || (firstTournament != null && firstTournament.startTimestamp < currentTime - 15 * 60 * 1000L)) {
            seedTournamentsOnly()
        }

        settleFinishedMatchesAndDistributePrize()
    }

    suspend fun updateRoomCredentials(tournamentId: Long, roomId: String, roomPassword: String) {
        db.tournamentDao().updateRoomCredentials(tournamentId, roomId, roomPassword)
    }

    suspend fun settleFinishedMatchesAndDistributePrize() {
        val currentTime = System.currentTimeMillis()
        val activeMatches = db.tournamentDao().getActiveTournamentsList()
        for (tournament in activeMatches) {
            // If match ended (more than 40 minutes after start time)
            if (tournament.startTimestamp > 0 && currentTime > tournament.startTimestamp + 40 * 60 * 1000L) {
                // Check if current user participated in this match
                val userJoined = db.joinedMatchDao().getJoinedMatchByTournament(tournament.id)
                if (userJoined != null) {
                    val prizeShare = if (tournament.prizePool > 0) {
                        (tournament.prizePool * 0.40).coerceAtLeast(tournament.entryFee * 1.5).coerceAtLeast(15.0)
                    } else 10.0
                    val killReward = tournament.perKillPrize * 2.0
                    val totalReward = prizeShare + killReward

                    db.userDao().addBalance(totalReward)
                    db.walletDao().insertTransaction(
                        WalletTransaction(
                            type = "PRIZE_POOL",
                            amount = totalReward,
                            paymentMethod = "Prize Reward",
                            accountNumber = tournament.title,
                            trxId = "WIN-${System.currentTimeMillis().toString().takeLast(6)}",
                            status = "SUCCESS"
                        )
                    )
                }
                // Mark match completed & prize pool distributed -> removes slot from active feed
                db.tournamentDao().markCompleted(tournament.id, "প্রাইজ ডিস্ট্রিবিউশন সম্পন্ন")
            }
        }
    }

    suspend fun joinTournament(
        tournament: Tournament,
        inGameName: String,
        inGameUid: String
    ): Result<String> {
        val currentTime = System.currentTimeMillis()
        if (tournament.startTimestamp > 0 && currentTime >= tournament.startTimestamp) {
            return Result.failure(Exception("ম্যাচ ইতিমধ্যে শুরু হয়ে গেছে! আর জয়েন করা সম্ভব নয়।"))
        }

        val user = db.userDao().getUserProfileOnce() ?: return Result.failure(Exception("User not found"))

        if (user.balance < tournament.entryFee && tournament.entryFee > 0) {
            return Result.failure(Exception("আপনার অ্যাকাউন্টে পর্যাপ্ত ব্যালেন্স নেই! ম্যাচে জয়েন করতে কমপক্ষে ৳${tournament.entryFee} ব্যালেন্স প্রয়োজন। দয়া করে আগে ডিপোজিট করুন।"))
        }

        val updatedSlots = db.tournamentDao().incrementJoinedSlots(tournament.id)
        if (updatedSlots <= 0) {
            return Result.failure(Exception("এই ম্যাচটির সব স্লট ফুল হয়ে গেছে!"))
        }

        if (tournament.entryFee > 0) {
            db.userDao().deductBalance(tournament.entryFee.toDouble())
            db.walletDao().insertTransaction(
                WalletTransaction(
                    type = "ENTRY_FEE",
                    amount = tournament.entryFee.toDouble(),
                    paymentMethod = "Entry Fee",
                    accountNumber = tournament.title,
                    trxId = "GCBD-JOIN-${System.currentTimeMillis().toString().takeLast(6)}",
                    status = "SUCCESS"
                )
            )
        }

        db.joinedMatchDao().insert(
            JoinedMatch(
                tournamentId = tournament.id,
                tournamentTitle = tournament.title,
                gameMode = tournament.gameMode,
                matchTime = tournament.matchTime,
                entryFee = tournament.entryFee,
                inGameName = inGameName,
                inGameUid = inGameUid,
                slotNumber = tournament.joinedSlots + 1
            )
        )

        return Result.success("Successfully joined ${tournament.title}! Check 'My Matches' for Room details.")
    }

    suspend fun addDepositRequest(
        method: String,
        amount: Double,
        senderNumber: String,
        trxId: String
    ): Result<String> {
        db.walletDao().insertTransaction(
            WalletTransaction(
                type = "DEPOSIT",
                amount = amount,
                paymentMethod = method,
                accountNumber = senderNumber,
                trxId = trxId,
                status = "SUCCESS" // Automatically credit in app for smooth user experience
            )
        )
        db.userDao().addBalance(amount)
        return Result.success("Deposit of ৳$amount BDT via $method successful!")
    }

    suspend fun requestWithdrawal(
        method: String,
        amount: Double,
        accountNumber: String
    ): Result<String> {
        val user = db.userDao().getUserProfileOnce() ?: return Result.failure(Exception("User not found"))
        if (user.balance < amount) {
            return Result.failure(Exception("Insufficient balance to withdraw ৳$amount BDT."))
        }

        db.userDao().deductBalance(amount)
        db.walletDao().insertTransaction(
            WalletTransaction(
                type = "WITHDRAWAL",
                amount = amount,
                paymentMethod = method,
                accountNumber = accountNumber,
                trxId = "WD-${System.currentTimeMillis().toString().takeLast(6)}",
                status = "PENDING"
            )
        )
        return Result.success("Withdrawal request for ৳$amount BDT submitted. Processing within 1-12 hours.")
    }

    suspend fun loginWithPhone(phone: String) {
        val current = db.userDao().getUserProfileOnce()
        if (current == null) {
            val uniqueUid = (10000000..99999999).random().toString()
            db.userDao().insertOrUpdate(
                UserProfile(
                    id = 1,
                    displayName = "Gamer BD",
                    email = "gamer@gamesclub.bd",
                    inGameName = "Gamer_BD",
                    inGameUid = uniqueUid,
                    phone = phone,
                    balance = 0.0,
                    isLoggedIn = true,
                    totalMatchesPlayed = 0,
                    totalKills = 0,
                    totalEarnings = 0.0
                )
            )
        } else {
            db.userDao().updateProfile(
                current.copy(
                    phone = phone,
                    isLoggedIn = true
                )
            )
        }
    }

    suspend fun logout() {
        val current = db.userDao().getUserProfileOnce() ?: return
        db.userDao().updateProfile(current.copy(isLoggedIn = false))
    }

    suspend fun updateProfile(
        displayName: String,
        inGameName: String,
        inGameUid: String,
        phone: String
    ) {
        val current = db.userDao().getUserProfileOnce() ?: return
        db.userDao().updateProfile(
            current.copy(
                displayName = displayName,
                inGameName = inGameName,
                inGameUid = inGameUid,
                phone = phone,
                isLoggedIn = true
            )
        )
    }
}
