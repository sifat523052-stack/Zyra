package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.viewmodel.StarVibeUiState
import com.example.viewmodel.StarVibeViewModel

@Composable
fun GameCenterScreen(
    viewModel: StarVibeViewModel,
    uiState: StarVibeUiState,
    modifier: Modifier = Modifier
) {
    BackHandler { viewModel.closeSubScreen() }

    val animatedRotation by animateFloatAsState(
        targetValue = uiState.wheelSpinAngle,
        animationSpec = tween(durationMillis = 2600, easing = FastOutSlowInEasing),
        label = "wheelRotation"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .background(Color(0xFF031615))
    ) {
        // Header
        Surface(color = Color(0xFF062725)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { viewModel.closeSubScreen() },
                    modifier = Modifier.testTag("games_back_button")
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
                Text(
                    text = "StarVibe Party Games",
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
            contentPadding = PaddingValues(top = 10.dp, bottom = 40.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Lucky Star Wheel Section
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(22.dp))
                        .background(
                            Brush.verticalGradient(
                                listOf(Color(0xFF0D3D39), Color(0xFF062624))
                            )
                        )
                        .border(1.5.dp, RoyalGoldBrush, RoundedCornerShape(22.dp))
                        .padding(16.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("🎡", fontSize = 20.sp)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Lucky Star Wheel",
                                    color = StarVibeGoldLight,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Text("20 💎 / Spin", color = StarVibeTurquoise, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }

                        // Wheel Graphic with center pin
                        Box(
                            modifier = Modifier
                                .size(190.dp)
                                .clip(CircleShape)
                                .border(3.dp, RoyalGoldBrush, CircleShape)
                                .background(Color(0xFF052B28)),
                            contentAlignment = Alignment.Center
                        ) {
                            // Rotating segments
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .rotate(animatedRotation),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("👑 500 💎", fontSize = 11.sp, color = StarVibeGold, modifier = Modifier.offset(y = (-65).dp), fontWeight = FontWeight.Bold)
                                Text("🌹 50 Rose", fontSize = 11.sp, color = StarVibeRose, modifier = Modifier.offset(x = 60.dp), fontWeight = FontWeight.Bold)
                                Text("💎 100 💎", fontSize = 11.sp, color = StarVibeTurquoise, modifier = Modifier.offset(y = 65.dp), fontWeight = FontWeight.Bold)
                                Text("⭐ VIP Pass", fontSize = 11.sp, color = StarVibeGoldLight, modifier = Modifier.offset(x = (-60).dp), fontWeight = FontWeight.Bold)
                            }

                            // Center Pointer & Spin button
                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(CircleShape)
                                    .background(RoyalGoldBrush)
                                    .border(2.dp, Color.White, CircleShape)
                                    .clickable(enabled = !uiState.isWheelSpinning) {
                                        viewModel.spinLuckyWheel()
                                    }
                                    .testTag("spin_wheel_button"),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (uiState.isWheelSpinning) "..." else "SPIN",
                                    color = Color.Black,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }
                        }

                        if (uiState.wheelWonPrize != null) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(StarVibeGold.copy(alpha = 0.2f))
                                    .border(1.dp, StarVibeGold, RoundedCornerShape(12.dp))
                                    .padding(horizontal = 14.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = "🎉 You Won: ${uiState.wheelWonPrize}",
                                    color = StarVibeGoldLight,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            // Star Dice Clash
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFF062927))
                        .border(1.dp, Color(0xFF13534D), RoundedCornerShape(20.dp))
                        .padding(16.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("🎲", fontSize = 20.sp)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Star Dice Clash",
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Text("Free Daily Roll", color = StarVibeEmerald, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }

                        // Dice Display
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val d1 = uiState.diceValues?.first ?: 6
                            val d2 = uiState.diceValues?.second ?: 6

                            Box(
                                modifier = Modifier
                                    .size(54.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(RoyalGoldBrush),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("$d1", fontSize = 28.sp, fontWeight = FontWeight.Black, color = Color.Black)
                            }

                            Spacer(modifier = Modifier.width(20.dp))

                            Box(
                                modifier = Modifier
                                    .size(54.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(RoyalGoldBrush),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("$d2", fontSize = 28.sp, fontWeight = FontWeight.Black, color = Color.Black)
                            }
                        }

                        Button(
                            onClick = { viewModel.rollDice() },
                            colors = ButtonDefaults.buttonColors(containerColor = StarVibeTurquoise),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(44.dp)
                                .testTag("roll_dice_button")
                        ) {
                            Text(
                                text = if (uiState.isRollingDice) "Rolling..." else "Roll Star Dice 🎲",
                                color = Color.Black,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // Other Voice Party Games
            item {
                Text(
                    text = "🎮 Live Room Voice Games",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    PartyGameCard(
                        icon = "🏴‍☠️",
                        title = "Greedy Pirate",
                        desc = "Open mystery chests with friends",
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.showToast("Greedy Pirate room game launched! 🏴‍☠️") }
                    )
                    PartyGameCard(
                        icon = "🎯",
                        title = "Ludo Party",
                        desc = "4-player voice room clash",
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.showToast("Ludo Party board loaded! 🎯") }
                    )
                }
            }
        }
    }
}

@Composable
fun PartyGameCard(
    icon: String,
    title: String,
    desc: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF062927))
            .border(1.dp, Color(0xFF13534D), RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(12.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(icon, fontSize = 26.sp)
            Text(title, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            Text(desc, color = StarVibeTextMuted, fontSize = 10.sp)
        }
    }
}
