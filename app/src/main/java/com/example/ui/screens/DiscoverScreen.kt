package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.example.model.DiscoverMoment
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.viewmodel.StarVibeUiState
import com.example.viewmodel.StarVibeViewModel

@Composable
fun DiscoverScreen(
    viewModel: StarVibeViewModel,
    uiState: StarVibeUiState,
    modifier: Modifier = Modifier
) {
    val topics = listOf("#StarVibeGala", "#SingWithMe", "#VIPNight", "#NightOwls", "#BeatDroppers", "#CoupleVibe")
    var playingAudioId by remember { mutableStateOf<String?>("m1") }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Color.Transparent,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.showToast("Moment composer: Share your star voice with the world! ✨") },
                containerColor = StarVibeTurquoise,
                contentColor = Color.Black,
                shape = CircleShape,
                modifier = Modifier.testTag("post_moment_fab")
            ) {
                Icon(Icons.Default.Edit, contentDescription = "Post Moment")
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Star of the Day Spotlight
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            Brush.verticalGradient(
                                listOf(Color(0xFF0C3835), Color(0xFF062523))
                            )
                        )
                        .border(1.5.dp, RoyalGoldBrush, RoundedCornerShape(20.dp))
                        .padding(16.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("👑", fontSize = 18.sp)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Star Talent Spotlight",
                                    color = StarVibeGoldLight,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color(0xFF13534D))
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    text = "TODAY'S #1",
                                    color = StarVibeGold,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }
                        }

                        // Top 3 Stars Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            // Rank 2
                            SpotlightTalentItem(
                                user = StarVibeRepository.sampleUsers[2],
                                rank = 2,
                                crownEmoji = "🥈",
                                points = "74.5K"
                            )
                            // Rank 1 (Center, taller)
                            SpotlightTalentItem(
                                user = StarVibeRepository.sampleUsers[0],
                                rank = 1,
                                crownEmoji = "👑",
                                points = "89.2K",
                                isFirst = true
                            )
                            // Rank 3
                            SpotlightTalentItem(
                                user = StarVibeRepository.sampleUsers[5],
                                rank = 3,
                                crownEmoji = "🥉",
                                points = "61.2K"
                            )
                        }
                    }
                }
            }

            // Trending Topics Row
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "🔥 Hot Topics & Challenges",
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(topics) { topic ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(Color(0xFF093330))
                                    .border(1.dp, StarVibeTurquoise.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
                                    .clickable { viewModel.showToast("Browsing topic: $topic") }
                                    .padding(horizontal = 12.dp, vertical = 7.dp)
                            ) {
                                Text(
                                    text = topic,
                                    color = StarVibeTurquoise,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            }

            // Moments Feed Section Header
            item {
                Text(
                    text = "✨ Star Moments & Audio Clips",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            // Moments Feed Items
            items(uiState.discoverMoments) { moment ->
                DiscoverMomentCard(
                    moment = moment,
                    isPlaying = playingAudioId == moment.id,
                    onTogglePlay = {
                        playingAudioId = if (playingAudioId == moment.id) null else moment.id
                    },
                    onLikeClick = { viewModel.toggleLikeMoment(moment.id) },
                    onCommentClick = { viewModel.showToast("Comment on ${moment.author.name}'s moment 💬") }
                )
            }
        }
    }
}

@Composable
fun SpotlightTalentItem(
    user: com.example.model.User,
    rank: Int,
    crownEmoji: String,
    points: String,
    isFirst: Boolean = false
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(crownEmoji, fontSize = if (isFirst) 20.sp else 16.sp)
        StarVibeAvatar(
            user = user,
            size = if (isFirst) 54.dp else 44.dp,
            showCrown = false
        )
        Text(
            text = user.name,
            color = Color.White,
            fontSize = if (isFirst) 13.sp else 11.sp,
            fontWeight = FontWeight.Bold
        )
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .then(if (isFirst) Modifier.background(RoyalGoldBrush) else Modifier.background(Color(0xFF0E4340)))
                .padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
            Text(
                text = "🌟 $points",
                color = if (isFirst) Color.Black else StarVibeGoldLight,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun DiscoverMomentCard(
    moment: DiscoverMoment,
    isPlaying: Boolean,
    onTogglePlay: () -> Unit,
    onLikeClick: () -> Unit,
    onCommentClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(Color(0xFF062725))
            .border(1.dp, Color(0xFF134E4A), RoundedCornerShape(18.dp))
            .padding(14.dp)
            .testTag("moment_card_${moment.id}")
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            // Author row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StarVibeAvatar(user = moment.author, size = 42.dp, showCrown = true)
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = moment.author.name,
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            VipBadge(vipLevel = moment.author.vipLevel)
                        }
                        Text(
                            text = moment.timeAgo,
                            color = StarVibeTextMuted,
                            fontSize = 11.sp
                        )
                    }
                }
            }

            // Post Text
            Text(
                text = moment.text,
                color = StarVibeTextSecondary,
                fontSize = 13.sp,
                lineHeight = 18.sp
            )

            // Audio Player Bar if present
            if (moment.audioTitle != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(
                            Brush.horizontalGradient(
                                listOf(Color(0xFF093A36), Color(0xFF13534E))
                            )
                        )
                        .clickable(onClick = onTogglePlay)
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(if (isPlaying) StarVibeEmerald else StarVibeGold),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = "Play/Pause",
                                tint = Color.Black,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Text(
                            text = moment.audioTitle,
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    if (isPlaying) {
                        MicWaveIndicator()
                    }
                }
            }

            // Tags
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                moment.tags.forEach { tag ->
                    Text(
                        text = "#$tag",
                        color = StarVibeTurquoise,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Action counts row (Like, Comment, Share)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Like Button
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable(onClick = onLikeClick)
                        .padding(4.dp)
                ) {
                    Icon(
                        imageVector = if (moment.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Like",
                        tint = if (moment.isLiked) StarVibeRose else StarVibeTextMuted,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${moment.likesCount}",
                        color = if (moment.isLiked) StarVibeRose else StarVibeTextMuted,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                // Comment Button
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable(onClick = onCommentClick)
                        .padding(4.dp)
                ) {
                    Icon(
                        Icons.Default.ChatBubbleOutline,
                        contentDescription = "Comment",
                        tint = StarVibeTextMuted,
                        modifier = Modifier.size(17.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${moment.commentsCount}",
                        color = StarVibeTextMuted,
                        fontSize = 12.sp
                    )
                }

                // Share Button
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(4.dp)
                ) {
                    Icon(
                        Icons.Default.Share,
                        contentDescription = "Share",
                        tint = StarVibeTextMuted,
                        modifier = Modifier.size(17.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Share",
                        color = StarVibeTextMuted,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}
