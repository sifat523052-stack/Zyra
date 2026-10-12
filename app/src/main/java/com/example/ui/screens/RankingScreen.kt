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
import com.example.model.CpPair
import com.example.model.RankingUser
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.viewmodel.StarVibeUiState
import com.example.viewmodel.StarVibeViewModel

@Composable
fun RankingScreen(
    viewModel: StarVibeViewModel,
    uiState: StarVibeUiState,
    modifier: Modifier = Modifier
) {
    BackHandler { viewModel.closeSubScreen() }

    val rankingList = when (uiState.rankingTab) {
        "Wealth" -> StarVibeRepository.rankingWealth
        "Charm" -> StarVibeRepository.rankingCharm
        "Room" -> StarVibeRepository.rankingWealth.map { it.copy(title = "👑 VIP Party Room") }
        else -> StarVibeRepository.rankingCharm
    }

    val tabs = listOf("Wealth", "Charm", "Room", "CP")
    val periods = listOf("Today", "This Week", "This Month")

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
                    modifier = Modifier.testTag("ranking_back_button")
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
                Text(
                    text = "StarVibe Leaderboards",
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
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Category Tabs
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFF062624))
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    tabs.forEach { tab ->
                        val selected = uiState.rankingTab == tab
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .then(if (selected) Modifier.background(RoyalGoldBrush) else Modifier.background(Color.Transparent))
                                .clickable { viewModel.setRankingTab(tab) }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = tab,
                                color = if (selected) Color.Black else StarVibeTextSecondary,
                                fontSize = 12.sp,
                                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
                            )
                        }
                    }
                }
            }

            // Period Selector (Today / Week / Month)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    periods.forEach { period ->
                        val selected = uiState.rankingPeriod == period
                        Text(
                            text = period,
                            color = if (selected) StarVibeTurquoise else StarVibeTextMuted,
                            fontSize = 12.sp,
                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                            modifier = Modifier
                                .clickable { viewModel.setRankingPeriod(period) }
                                .padding(horizontal = 12.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            if (uiState.rankingTab == "CP") {
                // CP Couples List
                item {
                    Text(
                        text = "💍 Top StarVibe Couples (CP)",
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                items(StarVibeRepository.cpRanking) { cp ->
                    CpRankItemCard(cp = cp)
                }
            } else {
                // Top 3 Podium
                item {
                    val top1 = rankingList.getOrNull(0)
                    val top2 = rankingList.getOrNull(1)
                    val top3 = rankingList.getOrNull(2)

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(22.dp))
                            .background(
                                Brush.verticalGradient(
                                    listOf(Color(0xFF0C3D39), Color(0xFF052422))
                                )
                            )
                            .border(1.5.dp, RoyalGoldBrush, RoundedCornerShape(22.dp))
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            if (top2 != null) {
                                PodiumUserItem(user = top2.user, rank = 2, crown = "🥈", score = top2.score)
                            }
                            if (top1 != null) {
                                PodiumUserItem(user = top1.user, rank = 1, crown = "👑", score = top1.score, isFirst = true)
                            }
                            if (top3 != null) {
                                PodiumUserItem(user = top3.user, rank = 3, crown = "🥉", score = top3.score)
                            }
                        }
                    }
                }

                // Remaining Ranking List
                item {
                    Text(
                        text = "Top Rankings",
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                items(rankingList.drop(3)) { item ->
                    RankingRowItem(item = item)
                }
            }
        }
    }
}

@Composable
fun CpRankItemCard(cp: CpPair) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(Color(0xFF062927))
            .border(
                1.dp,
                if (cp.rank == 1) RoyalGoldBrush else Brush.linearGradient(
                    listOf(Color(0xFFFF4081).copy(alpha = 0.6f), Color(0xFF13534D))
                ),
                RoundedCornerShape(18.dp)
            )
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = when (cp.rank) {
                        1 -> "👑 #1"
                        2 -> "🥈 #2"
                        else -> "🥉 #3"
                    },
                    color = StarVibeGoldLight,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Black
                )

                // Dual Avatars
                Row(verticalAlignment = Alignment.CenterVertically) {
                    StarVibeAvatar(user = cp.user1, size = 38.dp, showCrown = false)
                    Text("❤️", fontSize = 12.sp, modifier = Modifier.padding(horizontal = 2.dp))
                    StarVibeAvatar(user = cp.user2, size = 38.dp, showCrown = false)
                }

                Column {
                    Text(
                        text = "${cp.user1.name} & ${cp.user2.name}",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${cp.cpTitle} • ${cp.cpDays} Days",
                        color = Color(0xFFFF80AB),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Text(
                text = "${cp.cpPoints / 1000}K CP",
                color = StarVibeGold,
                fontSize = 12.sp,
                fontWeight = FontWeight.Black
            )
        }
    }
}

@Composable
fun PodiumUserItem(
    user: com.example.model.User,
    rank: Int,
    crown: String,
    score: Long,
    isFirst: Boolean = false
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(crown, fontSize = if (isFirst) 22.sp else 18.sp)
        StarVibeAvatar(
            user = user,
            size = if (isFirst) 60.dp else 48.dp,
            showCrown = false
        )
        Text(
            text = user.name,
            color = Color.White,
            fontSize = if (isFirst) 13.sp else 11.sp,
            fontWeight = FontWeight.Bold
        )
        VipBadge(vipLevel = user.vipLevel)
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .then(if (isFirst) Modifier.background(RoyalGoldBrush) else Modifier.background(Color(0xFF0E4340)))
                .padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
            Text(
                text = "${score / 1000}K 🌟",
                color = if (isFirst) Color.Black else StarVibeGoldLight,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun RankingRowItem(item: RankingUser) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFF062927))
            .border(1.dp, Color(0xFF13534D), RoundedCornerShape(14.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "#${item.rank}",
            color = StarVibeGoldLight,
            fontSize = 14.sp,
            fontWeight = FontWeight.Black,
            modifier = Modifier.width(30.dp)
        )

        StarVibeAvatar(user = item.user, size = 42.dp, showCrown = false)

        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = item.user.name,
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(6.dp))
                VipBadge(vipLevel = item.user.vipLevel)
            }
            Text(
                text = item.title,
                color = StarVibeTextSecondary,
                fontSize = 11.sp
            )
        }

        Text(
            text = "${item.score / 1000}K 🌟",
            color = StarVibeTurquoise,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
