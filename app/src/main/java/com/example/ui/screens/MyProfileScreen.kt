package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.viewmodel.StarVibeUiState
import com.example.viewmodel.StarVibeViewModel
import com.example.viewmodel.SubScreen

@Composable
fun MyProfileScreen(
    viewModel: StarVibeViewModel,
    uiState: StarVibeUiState,
    modifier: Modifier = Modifier
) {
    val user = uiState.user

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 10.dp, bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Profile Card
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(22.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(Color(0xFF0C3B37), Color(0xFF062423))
                        )
                    )
                    .border(1.5.dp, RoyalGoldBrush, RoundedCornerShape(22.dp))
                    .padding(16.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    // Avatar & Names
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        StarVibeAvatar(user = user, size = 68.dp, showCrown = true)

                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = user.name,
                                    color = Color.White,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                VipBadge(vipLevel = uiState.vipLevel)
                            }

                            Spacer(modifier = Modifier.height(2.dp))

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.clickable {
                                    viewModel.showToast("Copied StarVibe ID: ${user.starVibeId} 📋")
                                }
                            ) {
                                Text(
                                    text = "ID: ${user.starVibeId}",
                                    color = StarVibeTurquoise,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    Icons.Default.ContentCopy,
                                    contentDescription = "Copy",
                                    tint = StarVibeTurquoise,
                                    modifier = Modifier.size(13.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "Connect • Chat • Party • Shine ✨",
                                color = StarVibeTextSecondary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    // Level Badges Row
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        LevelBadge(level = user.level)
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFFF57C00))
                                .padding(horizontal = 5.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "Wealth Lv.${user.wealthLevel}",
                                color = Color.White,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFFE91E63))
                                .padding(horizontal = 5.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "Charm Lv.${user.charmLevel}",
                                color = Color.White,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Social Stats Row (Following, Followers, Visitors)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0x80031817))
                            .padding(vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        StatCounterItem("Following", "${user.followingCount}")
                        StatCounterItem("Followers", "${user.followersCount}")
                        StatCounterItem("Visitors", "${user.visitorsCount}")
                    }
                }
            }
        }

        // Diamond & Coins Wallet Card
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color(0xFF093936), Color(0xFF104A46))
                        )
                    )
                    .border(1.dp, StarVibeTurquoise.copy(alpha = 0.6f), RoundedCornerShape(18.dp))
                    .padding(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("💎", fontSize = 16.sp)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "${uiState.diamonds}",
                                    color = StarVibeTurquoise,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                            Text("Star Diamonds", color = StarVibeTextSecondary, fontSize = 11.sp)
                        }

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("🪙", fontSize = 16.sp)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "${uiState.coins}",
                                    color = StarVibeGoldLight,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                            Text("Star Coins", color = StarVibeTextSecondary, fontSize = 11.sp)
                        }
                    }

                    Button(
                        onClick = { viewModel.openSubScreen(SubScreen.WALLET) },
                        colors = ButtonDefaults.buttonColors(containerColor = StarVibeGold),
                        shape = RoundedCornerShape(14.dp),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                        modifier = Modifier.testTag("my_topup_button")
                    ) {
                        Text("Top Up", color = Color.Black, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Services & Feature Hub
        item {
            Text(
                text = "👑 StarVibe VIP & Features",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // 2-Column Grid of Navigation Hub Cards
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    FeatureHubCard(
                        icon = "💰",
                        title = "Wallet",
                        subtitle = "Diamonds & Cash",
                        accentColor = StarVibeTurquoise,
                        modifier = Modifier.weight(1f),
                        testTag = "hub_wallet",
                        onClick = { viewModel.openSubScreen(SubScreen.WALLET) }
                    )
                    FeatureHubCard(
                        icon = "👑",
                        title = "VIP Center",
                        subtitle = "Privileges & Crown",
                        accentColor = StarVibeGold,
                        modifier = Modifier.weight(1f),
                        testTag = "hub_vip",
                        onClick = { viewModel.openSubScreen(SubScreen.VIP) }
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    FeatureHubCard(
                        icon = "🏆",
                        title = "Ranking",
                        subtitle = "Wealth & Charm",
                        accentColor = Color(0xFFFFB300),
                        modifier = Modifier.weight(1f),
                        testTag = "hub_ranking",
                        onClick = { viewModel.openSubScreen(SubScreen.RANKING) }
                    )
                    FeatureHubCard(
                        icon = "🛡️",
                        title = "Family",
                        subtitle = "Clan & Guild Wars",
                        accentColor = StarVibeEmerald,
                        modifier = Modifier.weight(1f),
                        testTag = "hub_family",
                        onClick = { viewModel.openSubScreen(SubScreen.FAMILY) }
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    FeatureHubCard(
                        icon = "🛍️",
                        title = "Shop",
                        subtitle = "Frames & Entrances",
                        accentColor = Color(0xFFBA68C8),
                        modifier = Modifier.weight(1f),
                        testTag = "hub_shop",
                        onClick = { viewModel.openSubScreen(SubScreen.SHOP) }
                    )
                    FeatureHubCard(
                        icon = "🎲",
                        title = "Game Center",
                        subtitle = "Lucky Wheel & Dice",
                        accentColor = Color(0xFFFF5252),
                        modifier = Modifier.weight(1f),
                        testTag = "hub_games",
                        onClick = { viewModel.openSubScreen(SubScreen.GAME_CENTER) }
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    FeatureHubCard(
                        icon = "⚙️",
                        title = "Settings",
                        subtitle = "Audio & Account",
                        accentColor = StarVibeTextSecondary,
                        modifier = Modifier.weight(1f),
                        testTag = "hub_settings",
                        onClick = { viewModel.openSubScreen(SubScreen.SETTINGS) }
                    )
                    FeatureHubCard(
                        icon = "✨",
                        title = "My Badges",
                        subtitle = "Honor & Medals",
                        accentColor = StarVibeGoldLight,
                        modifier = Modifier.weight(1f),
                        testTag = "hub_badges",
                        onClick = { viewModel.showToast("All VIP Noble Medals are Active! 🎖️") }
                    )
                }
            }
        }
    }
}

@Composable
fun StatCounterItem(label: String, count: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = count,
            color = Color.White,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            color = StarVibeTextMuted,
            fontSize = 11.sp
        )
    }
}

@Composable
fun FeatureHubCard(
    icon: String,
    title: String,
    subtitle: String,
    accentColor: Color,
    modifier: Modifier = Modifier,
    testTag: String,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF062927))
            .border(1.dp, Color(0xFF13534D), RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(14.dp)
            .testTag(testTag)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(accentColor.copy(alpha = 0.15f))
                    .border(1.dp, accentColor.copy(alpha = 0.4f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(icon, fontSize = 20.sp)
            }

            Column {
                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = subtitle,
                    color = StarVibeTextMuted,
                    fontSize = 10.sp
                )
            }
        }
    }
}
