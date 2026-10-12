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
import com.example.model.VipTier
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.viewmodel.StarVibeUiState
import com.example.viewmodel.StarVibeViewModel
import com.example.viewmodel.SubScreen

@Composable
fun VipScreen(
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
                    modifier = Modifier.testTag("vip_back_button")
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
                Text(
                    text = "StarVibe VIP Center",
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
            // Current VIP Status Card
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(22.dp))
                        .background(
                            Brush.verticalGradient(
                                listOf(Color(0xFF2C2203), Color(0xFF0C3835))
                            )
                        )
                        .border(2.dp, RoyalGoldBrush, RoundedCornerShape(22.dp))
                        .padding(18.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(54.dp)
                                        .clip(CircleShape)
                                        .background(RoyalGoldBrush),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("👑", fontSize = 28.sp)
                                }

                                Column {
                                    Text(
                                        text = "VIP ${uiState.vipLevel} Royal Noble",
                                        color = StarVibeGoldLight,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Black
                                    )
                                    Text(
                                        text = "Active Status • Valid Forever",
                                        color = StarVibeTextSecondary,
                                        fontSize = 12.sp
                                    )
                                }
                            }

                            Button(
                                onClick = { viewModel.openSubScreen(SubScreen.WALLET) },
                                colors = ButtonDefaults.buttonColors(containerColor = StarVibeGold),
                                shape = RoundedCornerShape(12.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text("Recharge", color = Color.Black, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        // Progress Bar
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "EXP: ${uiState.vipExp} / 60,000",
                                    color = StarVibeTextSecondary,
                                    fontSize = 11.sp
                                )
                                Text(
                                    text = "Next: VIP 7 Sovereign",
                                    color = StarVibeTurquoise,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            LinearProgressIndicator(
                                progress = { (uiState.vipExp.toFloat() / 60000f).coerceIn(0f, 1f) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(4.dp)),
                                color = StarVibeGold,
                                trackColor = Color(0xFF093330)
                            )
                        }
                    }
                }
            }

            // Privileges Highlights
            item {
                Text(
                    text = "👑 Exclusive Noble Privileges",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    VipPrivilegePill("🐉 Golden Dragon", "Full room entrance", Modifier.weight(1f))
                    VipPrivilegePill("👑 Royal Crown", "Glowing avatar frame", Modifier.weight(1f))
                    VipPrivilegePill("💬 Emerald Bubble", "Shimmering chat", Modifier.weight(1f))
                }
            }

            // All VIP Tiers Ladder
            item {
                Text(
                    text = "🌟 VIP Tier Ladder (VIP 1 - VIP 9)",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            items(StarVibeRepository.vipTiers) { tier ->
                VipTierCard(
                    tier = tier,
                    isCurrent = tier.level == uiState.vipLevel
                )
            }
        }
    }
}

@Composable
fun VipPrivilegePill(title: String, subtitle: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFF072927))
            .border(1.dp, Color(0xFF13534D), RoundedCornerShape(14.dp))
            .padding(10.dp)
    ) {
        Column {
            Text(title, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(2.dp))
            Text(subtitle, color = StarVibeTextMuted, fontSize = 10.sp)
        }
    }
}

@Composable
fun VipTierCard(
    tier: VipTier,
    isCurrent: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF062826))
            .border(
                1.dp,
                if (isCurrent) RoyalGoldBrush else Brush.linearGradient(listOf(Color(0xFF134E4A), Color(0xFF0A3330))),
                RoundedCornerShape(16.dp)
            )
            .padding(14.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    VipBadge(vipLevel = tier.level)
                    Text(
                        text = tier.title,
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                if (isCurrent) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(StarVibeEmerald)
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text("CURRENT", color = Color.Black, fontSize = 9.sp, fontWeight = FontWeight.Black)
                    }
                } else {
                    Text(
                        text = "${tier.expRequired} EXP",
                        color = StarVibeGoldLight,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                tier.perks.forEach { perk ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("✨", fontSize = 11.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = perk,
                            color = StarVibeTextSecondary,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }
    }
}
