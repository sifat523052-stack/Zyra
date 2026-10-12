package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.StarVibeRepository
import com.example.model.WalletPackage
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.viewmodel.StarVibeUiState
import com.example.viewmodel.StarVibeViewModel

@Composable
fun WalletScreen(
    viewModel: StarVibeViewModel,
    uiState: StarVibeUiState,
    modifier: Modifier = Modifier
) {
    BackHandler { viewModel.closeSubScreen() }

    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .background(Color(0xFF031615))
    ) {
        // Top Bar
        Surface(color = Color(0xFF062725)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { viewModel.closeSubScreen() },
                    modifier = Modifier.testTag("wallet_back_button")
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
                Text(
                    text = "StarVibe Wallet",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 12.dp, bottom = 40.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Balance Cards
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            Brush.verticalGradient(
                                listOf(Color(0xFF0C3D38), Color(0xFF062826))
                            )
                        )
                        .border(1.5.dp, RoyalGoldBrush, RoundedCornerShape(20.dp))
                        .padding(18.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(
                            text = "My Balances",
                            color = StarVibeTextSecondary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("💎", fontSize = 22.sp)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "${uiState.diamonds}",
                                        color = StarVibeTurquoise,
                                        fontSize = 26.sp,
                                        fontWeight = FontWeight.Black
                                    )
                                }
                                Text("Star Diamonds", color = Color.White, fontSize = 12.sp)
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("🪙", fontSize = 20.sp)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "${uiState.coins}",
                                        color = StarVibeGoldLight,
                                        fontSize = 22.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Text("Star Coins", color = Color.White, fontSize = 12.sp)
                            }
                        }

                        Divider(color = Color(0xFF13534D))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Exchange Coins to Diamonds",
                                color = StarVibeTextSecondary,
                                fontSize = 12.sp
                            )
                            Button(
                                onClick = { viewModel.showToast("Converted 10,000 Coins to 100 Diamonds! 💎") },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0E4340)),
                                shape = RoundedCornerShape(10.dp),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                modifier = Modifier.height(30.dp)
                            ) {
                                Text("Exchange", color = StarVibeTurquoise, fontSize = 11.sp)
                            }
                        }
                    }
                }
            }

            // Top-up Packs Title
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "💎 Star Diamond Packs",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Instant VIP Credit",
                        color = StarVibeGoldLight,
                        fontSize = 11.sp
                    )
                }
            }

            // Wallet Packages List
            items(StarVibeRepository.walletPackages) { pack ->
                WalletPackageCard(
                    pack = pack,
                    onBuy = { viewModel.topUpDiamonds(pack) }
                )
            }

            // Transaction History Summary
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFF062725))
                        .border(1.dp, Color(0xFF134E4A), RoundedCornerShape(16.dp))
                        .padding(14.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "Recent Transactions",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        TransactionHistoryRow("Top Up Diamond Pack (1,200)", "+1,500 💎", StarVibeEmerald)
                        TransactionHistoryRow("Sent Star Castle Gift in Room #1", "-9,999 💎", Color(0xFFFF5252))
                        TransactionHistoryRow("Voice Room Host Charm Reward", "+15,000 🪙", StarVibeGold)
                    }
                }
            }
        }
    }
}

@Composable
fun WalletPackageCard(
    pack: WalletPackage,
    onBuy: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF072B29))
            .border(1.dp, if (pack.isBestValue) RoyalGoldBrush else Brush.linearGradient(listOf(Color(0xFF134E4A), Color(0xFF0B3835))), RoundedCornerShape(16.dp))
            .clickable(onClick = onBuy)
            .padding(14.dp)
            .testTag("pack_${pack.id}")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF0C3D39))
                        .border(1.dp, StarVibeTurquoise.copy(alpha = 0.5f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("💎", fontSize = 22.sp)
                }

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "${pack.diamonds} Diamonds",
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        if (pack.bonusDiamonds > 0) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(StarVibeBadgeRed)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "+${pack.bonusDiamonds} Bonus",
                                    color = Color.White,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Text(
                        text = "Total ${pack.diamonds + pack.bonusDiamonds} 💎 + ${pack.diamonds} VIP EXP",
                        color = StarVibeTextSecondary,
                        fontSize = 11.sp
                    )
                }
            }

            Button(
                onClick = onBuy,
                colors = ButtonDefaults.buttonColors(containerColor = StarVibeGold),
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
            ) {
                Text(
                    text = pack.priceUsd,
                    color = Color.Black,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun TransactionHistoryRow(title: String, amount: String, color: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, color = StarVibeTextSecondary, fontSize = 12.sp)
        Text(amount, color = color, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}
