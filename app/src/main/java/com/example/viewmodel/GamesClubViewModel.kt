package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.db.AppDatabase
import com.example.data.models.JoinedMatch
import com.example.data.models.LeaderboardItem
import com.example.data.models.Tournament
import com.example.data.models.UserProfile
import com.example.data.models.WalletTransaction
import com.example.data.repository.GamesClubRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class GamesClubViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: GamesClubRepository

    val userProfile: StateFlow<UserProfile?>
    val tournaments: StateFlow<List<Tournament>>
    val joinedMatches: StateFlow<List<JoinedMatch>>
    val transactions: StateFlow<List<WalletTransaction>>
    val leaderboard: StateFlow<List<LeaderboardItem>>

    val selectedCategory = MutableStateFlow("ALL")

    val snackbarMessage = MutableStateFlow<String?>(null)

    // Dialog states
    val joinModalTournament = MutableStateFlow<Tournament?>(null)
    val showDepositModal = MutableStateFlow(false)
    val showWithdrawModal = MutableStateFlow(false)
    val showProfileModal = MutableStateFlow(false)
    val showSupportModal = MutableStateFlow(false)

    init {
        val db = AppDatabase.getDatabase(application)
        repository = GamesClubRepository(db)

        viewModelScope.launch {
            repository.seedInitialDataIfNeeded()
            while (true) {
                delay(30_000L)
                repository.settleFinishedMatchesAndDistributePrize()
            }
        }

        userProfile = repository.userProfile.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

        tournaments = repository.tournaments.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        joinedMatches = repository.joinedMatches.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        transactions = repository.transactions.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        leaderboard = repository.leaderboard.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    }

    val filteredTournaments: StateFlow<List<Tournament>> = combine(
        tournaments,
        selectedCategory
    ) { list, category ->
        when {
            category.equals("ALL", ignoreCase = true) -> list
            category.equals("Free", ignoreCase = true) -> list.filter { it.entryFee == 0 }
            else -> {
                list.filter {
                    it.gameMode.equals(category, ignoreCase = true) ||
                    it.title.contains(category, ignoreCase = true) ||
                    it.gameMode.contains(category, ignoreCase = true)
                }
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun setCategory(category: String) {
        selectedCategory.value = category
    }

    fun openJoinModal(tournament: Tournament) {
        joinModalTournament.value = tournament
    }

    fun closeJoinModal() {
        joinModalTournament.value = null
    }

    fun joinSelectedTournament(inGameName: String, inGameUid: String) {
        val tournament = joinModalTournament.value ?: return
        viewModelScope.launch {
            val result = repository.joinTournament(tournament, inGameName, inGameUid)
            result.onSuccess { msg ->
                snackbarMessage.value = msg
                closeJoinModal()
            }.onFailure { err ->
                snackbarMessage.value = err.message ?: "Failed to join tournament"
            }
        }
    }

    fun joinTournamentDirect(
        tournament: Tournament,
        inGameName: String,
        inGameUid: String,
        onResult: (Boolean, String) -> Unit
    ) {
        viewModelScope.launch {
            val result = repository.joinTournament(tournament, inGameName, inGameUid)
            result.onSuccess { msg ->
                snackbarMessage.value = msg
                onResult(true, msg)
            }.onFailure { err ->
                val errorMsg = err.message ?: "Failed to join tournament"
                snackbarMessage.value = errorMsg
                onResult(false, errorMsg)
            }
        }
    }

    fun submitDeposit(method: String, amount: Double, number: String, trxId: String) {
        if (amount < 10) {
            snackbarMessage.value = "Minimum deposit amount is ৳10 BDT"
            return
        }
        if (trxId.isBlank()) {
            snackbarMessage.value = "Please enter TrxID / Transaction Reference"
            return
        }
        viewModelScope.launch {
            val res = repository.addDepositRequest(method, amount, number, trxId)
            res.onSuccess { msg ->
                snackbarMessage.value = msg
                showDepositModal.value = false
            }
        }
    }

    fun submitWithdrawal(method: String, amount: Double, accountNumber: String) {
        if (amount < 50) {
            snackbarMessage.value = "Minimum withdrawal amount is ৳50 BDT"
            return
        }
        if (accountNumber.isBlank()) {
            snackbarMessage.value = "Please enter your $method account number"
            return
        }
        viewModelScope.launch {
            val res = repository.requestWithdrawal(method, amount, accountNumber)
            res.onSuccess { msg ->
                snackbarMessage.value = msg
                showWithdrawModal.value = false
            }.onFailure { err ->
                snackbarMessage.value = err.message ?: "Withdrawal failed"
            }
        }
    }

    fun loginWithPhone(phone: String) {
        viewModelScope.launch {
            repository.loginWithPhone(phone)
            snackbarMessage.value = "স্বাগতম! সফলভাবে লগ ইন সম্পন্ন হয়েছে।"
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
            showProfileModal.value = false
            snackbarMessage.value = "লগ আউট সম্পন্ন হয়েছে।"
        }
    }

    fun saveProfile(displayName: String, inGameName: String, inGameUid: String, phone: String) {
        viewModelScope.launch {
            repository.updateProfile(displayName, inGameName, inGameUid, phone)
            snackbarMessage.value = "Profile updated successfully!"
            showProfileModal.value = false
        }
    }

    fun resetAllData() {
        viewModelScope.launch {
            repository.resetAllDataToZero()
            snackbarMessage.value = "সকল ডাটা ০ থেকে রিসেট করা হয়েছে! (All data reset to 0)"
            showProfileModal.value = false
        }
    }

    fun updateRoomCredentials(tournamentId: Long, roomId: String, roomPassword: String) {
        viewModelScope.launch {
            repository.updateRoomCredentials(tournamentId, roomId, roomPassword)
            snackbarMessage.value = "রুম আইডি ও পাসওয়ার্ড সফলভাবে আপডেট করা হয়েছে!"
        }
    }

    fun deductBalance(amount: Double) {
        viewModelScope.launch {
            repository.deductBalance(amount)
        }
    }

    fun clearSnackbar() {
        snackbarMessage.value = null
    }
}
