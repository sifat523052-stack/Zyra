package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.*
import com.example.ui.screens.*
import com.example.ui.theme.*
import com.example.viewmodel.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StarVibeTheme {
                StarVibeApp()
            }
        }
    }
}

@Composable
fun StarVibeApp(
    viewModel: StarVibeViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(StarVibeBgBrush)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Top Header: Visible only on main tabs
            if (uiState.subScreen == SubScreen.NONE) {
                StarVibeTopHeader(
                    selectedTopNav = uiState.topNavSelection,
                    onTopNavSelected = { viewModel.setTopNav(it) },
                    diamonds = uiState.diamonds,
                    coins = uiState.coins,
                    onTopUpClick = { viewModel.openSubScreen(SubScreen.WALLET) }
                )
            }

            // Main Content Area
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                when (uiState.subScreen) {
                    SubScreen.VOICE_ROOM -> {
                        VoiceRoomScreen(viewModel = viewModel, uiState = uiState)
                    }
                    SubScreen.WALLET -> {
                        WalletScreen(viewModel = viewModel, uiState = uiState)
                    }
                    SubScreen.VIP -> {
                        VipScreen(viewModel = viewModel, uiState = uiState)
                    }
                    SubScreen.RANKING, SubScreen.HALL_OF_FAME -> {
                        RankingScreen(viewModel = viewModel, uiState = uiState)
                    }
                    SubScreen.FAMILY -> {
                        FamilyScreen(viewModel = viewModel, uiState = uiState)
                    }
                    SubScreen.SHOP -> {
                        ShopScreen(viewModel = viewModel, uiState = uiState)
                    }
                    SubScreen.GAME_CENTER, SubScreen.LUDO_GAME -> {
                        GameCenterScreen(viewModel = viewModel, uiState = uiState)
                    }
                    SubScreen.SETTINGS -> {
                        SettingsScreen(viewModel = viewModel, uiState = uiState)
                    }
                    SubScreen.PRIZE_POOL -> {
                        WalletScreen(viewModel = viewModel, uiState = uiState)
                    }
                    SubScreen.CHAT_DETAIL -> {
                        MessageScreen(viewModel = viewModel, uiState = uiState)
                    }
                    SubScreen.NONE -> {
                        when (uiState.currentTab) {
                            MainTab.PARTY -> PartyScreen(viewModel = viewModel, uiState = uiState)
                            MainTab.DISCOVER -> DiscoverScreen(viewModel = viewModel, uiState = uiState)
                            MainTab.MESSAGE -> MessageScreen(viewModel = viewModel, uiState = uiState)
                            MainTab.MY -> MyProfileScreen(viewModel = viewModel, uiState = uiState)
                        }
                    }
                }
            }

            // Bottom Navigation Bar: Visible when on main tabs
            if (uiState.subScreen == SubScreen.NONE) {
                StarVibeBottomNavBar(
                    selectedTab = uiState.currentTab,
                    onTabSelected = { viewModel.setTab(it) }
                )
            }
        }

        // Global Toast Notification
        AnimatedVisibility(
            visible = uiState.toastMessage != null,
            enter = fadeIn() + slideInVertically { it / 2 },
            exit = fadeOut() + slideOutVertically { it / 2 },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 90.dp)
        ) {
            uiState.toastMessage?.let { msg ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xE6052826))
                        .border(1.dp, StarVibeTurquoise, RoundedCornerShape(20.dp))
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                        .testTag("global_toast")
                ) {
                    Text(
                        text = msg,
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
