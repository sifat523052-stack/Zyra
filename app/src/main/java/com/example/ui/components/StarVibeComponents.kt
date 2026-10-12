package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.GiftItem
import com.example.model.User
import com.example.ui.theme.*
import com.example.viewmodel.MainTab

// Royal Gold Gradient
val RoyalGoldBrush = Brush.linearGradient(
    colors = listOf(StarVibeGoldLight, StarVibeGold, StarVibeGoldDark)
)

// Turquoise Emerald Gradient
val EmeraldTurquoiseBrush = Brush.linearGradient(
    colors = listOf(StarVibeTurquoise, StarVibeTealDark, StarVibeEmerald)
)

// Deep Background Gradient
val StarVibeBgBrush = Brush.verticalGradient(
    colors = listOf(
        Color(0xFF021716),
        Color(0xFF042120),
        Color(0xFF031615),
        Color(0xFF010E0D)
    )
)

@Composable
fun StarVibeTopHeader(
    selectedTopNav: String,
    onTopNavSelected: (String) -> Unit,
    diamonds: Int,
    coins: Long,
    onTopUpClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = Color(0xEE031C1A)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
        ) {
            // Upper Bar: Brand Logo & Wallet Balances
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // App Brand
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .border(1.5.dp, RoyalGoldBrush, CircleShape)
                            .background(
                                Brush.radialGradient(
                                    listOf(StarVibeTurquoise.copy(alpha = 0.5f), Color(0xFF042826))
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "👑", fontSize = 18.sp)
                    }

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "StarVibe",
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 0.5.sp
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text("✨", fontSize = 12.sp)
                        }
                        Text(
                            text = "Connect • Chat • Party • Shine",
                            color = StarVibeGoldLight,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                // Wallet Balances (Diamonds & Coins)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Diamonds
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFF083330))
                            .border(1.dp, StarVibeTurquoise.copy(alpha = 0.6f), RoundedCornerShape(16.dp))
                            .clickable { onTopUpClick() }
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("💎", fontSize = 11.sp)
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "$diamonds",
                            color = StarVibeTurquoise,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Box(
                            modifier = Modifier
                                .size(14.dp)
                                .clip(CircleShape)
                                .background(StarVibeGold),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("+", color = Color.Black, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    // Coins
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFF072B28))
                            .border(1.dp, StarVibeGold.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
                            .padding(horizontal = 7.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("🪙", fontSize = 11.sp)
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "${coins / 1000}K",
                            color = StarVibeGoldLight,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Lower Navigation Row: Mine, Party, Game, Search
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val topNavItems = listOf(
                    Triple("Mine", "👑", "Mine"),
                    Triple("Party", "⭐", "Party"),
                    Triple("Game", "🎲", "Game"),
                    Triple("Search", "🔍", "Search")
                )

                topNavItems.forEach { (navKey, iconEmoji, label) ->
                    val isSelected = selectedTopNav == navKey
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(18.dp))
                            .then(
                                if (isSelected) {
                                    Modifier
                                        .background(
                                            Brush.horizontalGradient(
                                                listOf(
                                                    StarVibeGold.copy(alpha = 0.25f),
                                                    StarVibeTurquoise.copy(alpha = 0.35f)
                                                )
                                            )
                                        )
                                        .border(1.5.dp, RoyalGoldBrush, RoundedCornerShape(18.dp))
                                } else {
                                    Modifier
                                        .background(Color(0xFF062624))
                                        .border(0.5.dp, Color(0xFF13534D), RoundedCornerShape(18.dp))
                                }
                            )
                            .clickable { onTopNavSelected(navKey) }
                            .padding(horizontal = 16.dp, vertical = 7.dp)
                            .testTag("top_nav_$navKey"),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(iconEmoji, fontSize = 13.sp)
                            Text(
                                text = label,
                                color = if (isSelected) StarVibeGoldLight else StarVibeTextSecondary,
                                fontSize = 13.sp,
                                fontWeight = if (isSelected) FontWeight.Black else FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun VipBadge(vipLevel: Int, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(
                Brush.horizontalGradient(
                    listOf(Color(0xFFFFB300), Color(0xFFFF8F00))
                )
            )
            .border(0.5.dp, Color(0xFFFFE082), RoundedCornerShape(8.dp))
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text("👑", fontSize = 9.sp)
            Text(
                text = "VIP $vipLevel",
                color = Color.Black,
                fontSize = 10.sp,
                fontWeight = FontWeight.Black
            )
        }
    }
}

@Composable
fun LevelBadge(level: Int, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(Color(0xFF00BFA5))
            .padding(horizontal = 5.dp, vertical = 2.dp)
    ) {
        Text(
            text = "Lv.$level",
            color = Color.White,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun StarVibeAvatar(
    user: User,
    size: Dp = 52.dp,
    showCrown: Boolean = true,
    isSpeaking: Boolean = false,
    onClick: () -> Unit = {}
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (isSpeaking) 1.12f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(400, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "speakingPulse"
    )

    Box(
        modifier = Modifier
            .size(size + 10.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        // Outer pulsing ring if speaking
        if (isSpeaking) {
            Box(
                modifier = Modifier
                    .size(size + 8.dp)
                    .scale(pulseScale)
                    .border(2.dp, StarVibeTurquoise, CircleShape)
            )
        }

        // Avatar Core
        Box(
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
                .border(2.dp, if (user.vipLevel >= 5) RoyalGoldBrush else Brush.linearGradient(listOf(StarVibeTurquoise, StarVibeEmerald)), CircleShape)
                .background(Color(user.avatarBgColor)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = user.avatarEmoji,
                fontSize = (size.value * 0.48f).sp
            )
        }

        // Crown on top for VIP
        if (showCrown && user.vipLevel >= 5) {
            Text(
                text = "👑",
                fontSize = (size.value * 0.35f).sp,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 2.dp, y = (-4).dp)
            )
        }
    }
}

@Composable
fun MicWaveIndicator(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "wave")
    val bar1 by infiniteTransition.animateFloat(
        initialValue = 6f, targetValue = 18f,
        animationSpec = infiniteRepeatable(tween(350), RepeatMode.Reverse),
        label = "b1"
    )
    val bar2 by infiniteTransition.animateFloat(
        initialValue = 16f, targetValue = 8f,
        animationSpec = infiniteRepeatable(tween(420), RepeatMode.Reverse),
        label = "b2"
    )
    val bar3 by infiniteTransition.animateFloat(
        initialValue = 8f, targetValue = 20f,
        animationSpec = infiniteRepeatable(tween(300), RepeatMode.Reverse),
        label = "b3"
    )

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .width(2.5.dp)
                .height(bar1.dp)
                .background(StarVibeTurquoise, RoundedCornerShape(2.dp))
        )
        Box(
            modifier = Modifier
                .width(2.5.dp)
                .height(bar2.dp)
                .background(StarVibeEmerald, RoundedCornerShape(2.dp))
        )
        Box(
            modifier = Modifier
                .width(2.5.dp)
                .height(bar3.dp)
                .background(StarVibeGold, RoundedCornerShape(2.dp))
        )
    }
}

@Composable
fun LuxuryGiftCelebrationOverlay(gift: GiftItem, onDismiss: () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition(label = "giftGlow")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(4000, easing = LinearEasing)),
        label = "rotate"
    )
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.95f, targetValue = 1.08f,
        animationSpec = infiniteRepeatable(tween(600), RepeatMode.Reverse),
        label = "scale"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.75f))
            .clickable(onClick = onDismiss),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Rotating Sunburst Aura
            Box(
                modifier = Modifier
                    .size(240.dp)
                    .rotate(rotation)
                    .background(
                        Brush.radialGradient(
                            listOf(
                                Color(gift.color).copy(alpha = 0.5f),
                                StarVibeTurquoise.copy(alpha = 0.2f),
                                Color.Transparent
                            )
                        ),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "✨",
                    fontSize = 40.sp,
                    modifier = Modifier.offset(x = 60.dp, y = (-60).dp)
                )
                Text(
                    text = "🌟",
                    fontSize = 32.sp,
                    modifier = Modifier.offset(x = (-70).dp, y = 50.dp)
                )
            }

            // Big Gift Icon
            Box(
                modifier = Modifier
                    .scale(scale)
                    .offset(y = (-140).dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = gift.iconEmoji, fontSize = 90.sp)
            }

            Column(
                modifier = Modifier.offset(y = (-100).dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = gift.name,
                    color = StarVibeGoldLight,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(RoyalGoldBrush)
                        .padding(horizontal = 14.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "💎 ${gift.priceDiamonds} Diamonds • ${gift.rarity}",
                        color = Color.Black,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "🌟 Room Charm +${gift.priceDiamonds * 10} 🌟",
                    color = StarVibeTurquoise,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
fun StarVibeBottomNavBar(
    selectedTab: MainTab,
    onTabSelected: (MainTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.navigationBars),
        color = Color(0xF0031817),
        tonalElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavTabItem(
                title = "Party",
                selected = selectedTab == MainTab.PARTY,
                selectedIcon = Icons.Filled.Mic,
                unselectedIcon = Icons.Outlined.Mic,
                testTag = "nav_party",
                onClick = { onTabSelected(MainTab.PARTY) }
            )
            NavTabItem(
                title = "Discover",
                selected = selectedTab == MainTab.DISCOVER,
                selectedIcon = Icons.Filled.Explore,
                unselectedIcon = Icons.Outlined.Explore,
                testTag = "nav_discover",
                onClick = { onTabSelected(MainTab.DISCOVER) }
            )
            NavTabItem(
                title = "Message",
                selected = selectedTab == MainTab.MESSAGE,
                selectedIcon = Icons.Filled.ChatBubble,
                unselectedIcon = Icons.Outlined.ChatBubbleOutline,
                testTag = "nav_message",
                badgeCount = 3,
                onClick = { onTabSelected(MainTab.MESSAGE) }
            )
            NavTabItem(
                title = "My",
                selected = selectedTab == MainTab.MY,
                selectedIcon = Icons.Filled.Person,
                unselectedIcon = Icons.Outlined.Person,
                testTag = "nav_my",
                onClick = { onTabSelected(MainTab.MY) }
            )
        }
    }
}

@Composable
fun NavTabItem(
    title: String,
    selected: Boolean,
    selectedIcon: ImageVector,
    unselectedIcon: ImageVector,
    testTag: String,
    badgeCount: Int = 0,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 6.dp)
            .testTag(testTag),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(contentAlignment = Alignment.TopEnd) {
            Icon(
                imageVector = if (selected) selectedIcon else unselectedIcon,
                contentDescription = title,
                tint = if (selected) StarVibeTurquoise else StarVibeTextMuted,
                modifier = Modifier.size(24.dp)
            )
            if (badgeCount > 0) {
                Box(
                    modifier = Modifier
                        .offset(x = 6.dp, y = (-4).dp)
                        .size(14.dp)
                        .clip(CircleShape)
                        .background(StarVibeBadgeRed),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "$badgeCount",
                        color = Color.White,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = title,
            color = if (selected) StarVibeGold else StarVibeTextMuted,
            fontSize = 11.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
        )
    }
}
