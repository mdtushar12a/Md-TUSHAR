package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.DepositDialog
import com.example.ui.components.DeveloperProfileDialog
import com.example.ui.components.JoinTournamentDialog
import com.example.ui.components.ProfileDialog
import com.example.ui.components.ReferEarnDialog
import com.example.ui.components.SupportDialog
import com.example.ui.components.TopAppBarHeader
import com.example.ui.components.WithdrawDialog
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LeaderboardScreen
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.MyMatchesScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.RulesSupportScreen
import com.example.ui.screens.SelectModeScreen
import com.example.ui.screens.ModeMatchesScreen
import com.example.ui.screens.TournamentsScreen
import com.example.ui.screens.WalletScreen
import com.example.ui.screens.TopUpScreen
import com.example.ui.screens.ShopScreen
import com.example.ui.screens.WithdrawScreen
import com.example.ui.screens.DepositScreen
import com.example.ui.screens.JoinBattleScreen
import com.example.ui.theme.FieryRed
import com.example.ui.theme.FlameOrange
import com.example.ui.theme.GamesClubBDTheme
import com.example.ui.theme.GoldYellow
import com.example.ui.theme.SlateCardBg
import com.example.ui.theme.SlateDarkBg
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.GamesClubViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: GamesClubViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            GamesClubBDTheme {
                MainAppScreen(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainAppScreen(viewModel: GamesClubViewModel) {
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
    val filteredTournaments by viewModel.filteredTournaments.collectAsStateWithLifecycle()
    val allTournaments by viewModel.tournaments.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val joinedMatches by viewModel.joinedMatches.collectAsStateWithLifecycle()
    val transactions by viewModel.transactions.collectAsStateWithLifecycle()
    val leaderboard by viewModel.leaderboard.collectAsStateWithLifecycle()
    val snackbarMsg by viewModel.snackbarMessage.collectAsStateWithLifecycle()

    val joinModalTournament by viewModel.joinModalTournament.collectAsStateWithLifecycle()
    val showDepositModal by viewModel.showDepositModal.collectAsStateWithLifecycle()
    val showWithdrawModal by viewModel.showWithdrawModal.collectAsStateWithLifecycle()
    val showProfileModal by viewModel.showProfileModal.collectAsStateWithLifecycle()
    val showSupportModal by viewModel.showSupportModal.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }
    var selectedTab by remember { mutableIntStateOf(0) }
    var selectedGameModeTitle by remember { mutableStateOf("BR MATCH") }
    var showReferModal by remember { mutableStateOf(false) }
    var showDevModal by remember { mutableStateOf(false) }
    var selectedBattleTournament by remember { mutableStateOf<com.example.data.models.Tournament?>(null) }
    var previousTabForBattle by remember { mutableIntStateOf(0) }

    LaunchedEffect(snackbarMsg) {
        snackbarMsg?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearSnackbar()
        }
    }

    LaunchedEffect(joinModalTournament) {
        joinModalTournament?.let { t ->
            selectedBattleTournament = t
            previousTabForBattle = selectedTab
            selectedTab = 12
            viewModel.closeJoinModal()
        }
    }

    if (userProfile?.isLoggedIn == false) {
        Box(modifier = Modifier.fillMaxSize()) {
            LoginScreen(
                onLoginSuccess = { phone ->
                    viewModel.loginWithPhone(phone)
                },
                onOpenSupport = {
                    viewModel.showSupportModal.value = true
                }
            )

            if (showSupportModal) {
                SupportDialog(
                    onDismiss = { viewModel.showSupportModal.value = false }
                )
            }
        }
    } else {
        val context = androidx.compose.ui.platform.LocalContext.current

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = SlateDarkBg,
            topBar = {
                if (selectedTab != 0 && selectedTab != 1 && selectedTab != 3 && selectedTab != 6 && selectedTab != 7 && selectedTab != 8 && selectedTab != 9 && selectedTab != 10 && selectedTab != 11 && selectedTab != 12) {
                    TopAppBarHeader(
                        balance = userProfile?.balance ?: 0.0,
                        userName = userProfile?.displayName ?: "Gamer",
                        onOpenWallet = { selectedTab = 3 },
                        onOpenProfile = { selectedTab = 1 }
                    )
                }
            },
            bottomBar = {
                if (selectedTab != 12) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp, top = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            modifier = Modifier
                                .width(260.dp)
                                .height(56.dp)
                                .clip(RoundedCornerShape(30.dp))
                                .background(Color(0xFF131A26))
                                .border(1.dp, Color(0xFF232E42), RoundedCornerShape(30.dp))
                                .padding(4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Home Pill
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                                    .clip(RoundedCornerShape(26.dp))
                                    .background(if (selectedTab == 0) Color(0xFF2B364A) else Color.Transparent)
                                    .clickable { selectedTab = 0 },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Home,
                                    contentDescription = "Home",
                                    tint = if (selectedTab == 0) Color.White else Color(0xFF7A8B9E),
                                    modifier = Modifier.size(24.dp)
                                )
                            }

                            // Profile Pill
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                                    .clip(RoundedCornerShape(26.dp))
                                    .background(if (selectedTab == 1) Color(0xFF2B364A) else Color.Transparent)
                                    .clickable { selectedTab = 1 },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Profile",
                                    tint = if (selectedTab == 1) Color.White else Color(0xFF7A8B9E),
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }
                }
            },
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                when (selectedTab) {
                    0 -> HomeScreen(
                        balance = userProfile?.balance ?: 0.0,
                        onOpenWallet = { selectedTab = 3 },
                        onOpenSupport = { viewModel.showSupportModal.value = true },
                        onSelectFreeFire = { selectedTab = 6 },
                        onSelectLudo = {
                            selectedGameModeTitle = "LUDO KING"
                            selectedTab = 7
                        },
                        onOpenNotification = {
                            Toast.makeText(context, "আপনার ৪টি টুর্নামেন্ট নোটিফিকেশন রয়েছে", Toast.LENGTH_SHORT).show()
                        },
                        onOpenTopUp = { selectedTab = 8 },
                        onOpenShop = { selectedTab = 9 }
                    )
                    1 -> ProfileScreen(
                        userProfile = userProfile,
                        onOpenWallet = { selectedTab = 3 },
                        onOpenDeposit = { selectedTab = 11 },
                        onOpenStatements = { selectedTab = 3 },
                        onOpenLeaderboard = { selectedTab = 4 },
                        onOpenEditProfile = { viewModel.showProfileModal.value = true },
                        onOpenTerms = { viewModel.showSupportModal.value = true },
                        onOpenDeveloperProfile = { showDevModal = true },
                        onOpenRefer = { showReferModal = true },
                        onOpenNotification = {
                            Toast.makeText(context, "আপনার ৪টি টুর্নামেন্ট নোটিফিকেশন রয়েছে", Toast.LENGTH_SHORT).show()
                        }
                    )
                    2 -> TournamentsScreen(
                        tournaments = filteredTournaments,
                        joinedMatches = joinedMatches,
                        selectedCategory = selectedCategory,
                        onCategorySelect = { viewModel.setCategory(it) },
                        onJoinClick = { tournament ->
                            selectedBattleTournament = tournament
                            previousTabForBattle = 2
                            selectedTab = 12
                        },
                        onBackToHome = { selectedTab = 0 },
                        onUpdateRoomCredentials = { id, rId, rPass ->
                            viewModel.updateRoomCredentials(id, rId, rPass)
                        }
                    )
                    3 -> WalletScreen(
                        userProfile = userProfile,
                        transactions = transactions,
                        onOpenDeposit = { selectedTab = 11 },
                        onOpenWithdraw = { selectedTab = 10 },
                        onBack = { selectedTab = 0 },
                        onDeductBalance = { amount -> viewModel.deductBalance(amount) }
                    )
                    4 -> LeaderboardScreen(
                        leaders = leaderboard
                    )
                    5 -> MyMatchesScreen(
                        joinedMatches = joinedMatches,
                        tournaments = allTournaments,
                        onUpdateRoomCredentials = { id, rId, rPass ->
                            viewModel.updateRoomCredentials(id, rId, rPass)
                        }
                    )
                    6 -> SelectModeScreen(
                        tournaments = allTournaments,
                        onBack = { selectedTab = 0 },
                        onSelectMode = { mode ->
                            selectedGameModeTitle = mode.title
                            selectedTab = 7
                        }
                    )
                    7 -> ModeMatchesScreen(
                        modeTitle = selectedGameModeTitle,
                        tournaments = allTournaments,
                        joinedMatches = joinedMatches,
                        onBack = { selectedTab = 6 },
                        onJoinClick = { tournament ->
                            selectedBattleTournament = tournament
                            previousTabForBattle = 7
                            selectedTab = 12
                        },
                        onUpdateRoomCredentials = { id, rId, rPass ->
                            viewModel.updateRoomCredentials(id, rId, rPass)
                        }
                    )
                    8 -> TopUpScreen(
                        userBalance = userProfile?.balance ?: 0.0,
                        onBack = { selectedTab = 0 },
                        onOpenDeposit = { selectedTab = 11 },
                        onDeductBalance = { amount -> viewModel.deductBalance(amount) }
                    )
                    9 -> ShopScreen(
                        userBalance = userProfile?.balance ?: 0.0,
                        onBack = { selectedTab = 0 },
                        onOpenDeposit = { selectedTab = 11 },
                        onDeductBalance = { amount -> viewModel.deductBalance(amount) }
                    )
                    10 -> WithdrawScreen(
                        userProfile = userProfile,
                        onBack = { selectedTab = 3 },
                        onSubmitWithdrawal = { method, amount, accountNumber ->
                            viewModel.submitWithdrawal(method, amount, accountNumber)
                        }
                    )
                    11 -> DepositScreen(
                        userProfile = userProfile,
                        onBack = { selectedTab = 3 },
                        onSubmitDeposit = { method, amount, senderNumber, trxId ->
                            viewModel.submitDeposit(method, amount, senderNumber, trxId)
                        }
                    )
                    12 -> {
                        selectedBattleTournament?.let { tournament ->
                            val currentTournament = allTournaments.find { it.id == tournament.id } ?: tournament
                            JoinBattleScreen(
                                tournament = currentTournament,
                                userProfile = userProfile,
                                joinedMatches = joinedMatches,
                                onBack = { selectedTab = previousTabForBattle },
                                onOpenDeposit = { selectedTab = 11 },
                                onConfirmJoin = { ffName, ffUid ->
                                    viewModel.joinTournamentDirect(currentTournament, ffName, ffUid) { success, _ ->
                                        if (success) {
                                            selectedTab = 5
                                        }
                                    }
                                },
                                onUpdateRoomCredentials = { id, rId, rPass ->
                                    viewModel.updateRoomCredentials(id, rId, rPass)
                                }
                            )
                        }
                    }
                }

                // Dialogs
                joinModalTournament?.let { tournament ->
                    JoinTournamentDialog(
                        tournament = tournament,
                        userInGameName = userProfile?.inGameName ?: "",
                        userInGameUid = userProfile?.inGameUid ?: "",
                        userBalance = userProfile?.balance ?: 0.0,
                        onDismiss = { viewModel.closeJoinModal() },
                        onConfirm = { ffName, ffUid ->
                            viewModel.joinSelectedTournament(ffName, ffUid)
                        }
                    )
                }

                if (showDepositModal) {
                    DepositDialog(
                        onDismiss = { viewModel.showDepositModal.value = false },
                        onSubmitDeposit = { method, amount, number, trxId ->
                            viewModel.submitDeposit(method, amount, number, trxId)
                        }
                    )
                }

                if (showWithdrawModal) {
                    WithdrawDialog(
                        userBalance = userProfile?.balance ?: 0.0,
                        onDismiss = { viewModel.showWithdrawModal.value = false },
                        onSubmitWithdrawal = { method, amount, accountNumber ->
                            viewModel.submitWithdrawal(method, amount, accountNumber)
                        }
                    )
                }

                if (showProfileModal) {
                    ProfileDialog(
                        userProfile = userProfile,
                        onDismiss = { viewModel.showProfileModal.value = false },
                        onSaveProfile = { displayName, inGameName, inGameUid, phone ->
                            viewModel.saveProfile(displayName, inGameName, inGameUid, phone)
                        },
                        onResetData = { viewModel.resetAllData() },
                        onLogout = { viewModel.logout() }
                    )
                }

                if (showSupportModal) {
                    SupportDialog(
                        onDismiss = { viewModel.showSupportModal.value = false }
                    )
                }

                if (showReferModal) {
                    ReferEarnDialog(
                        userUid = userProfile?.inGameUid.takeIf { !it.isNullOrEmpty() } ?: "10829471",
                        onDismiss = { showReferModal = false }
                    )
                }

                if (showDevModal) {
                    DeveloperProfileDialog(
                        onDismiss = { showDevModal = false }
                    )
                }
            }
        }
    }
}

