package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Conversation
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.viewmodel.StarVibeUiState
import com.example.viewmodel.StarVibeViewModel

@Composable
fun MessageScreen(
    viewModel: StarVibeViewModel,
    uiState: StarVibeUiState,
    modifier: Modifier = Modifier
) {
    if (uiState.activeConversation != null) {
        ConversationDetailView(
            conversation = uiState.activeConversation,
            uiState = uiState,
            onBack = { viewModel.closeSubScreen() },
            onSend = { viewModel.sendDirectMessage(it) }
        )
        return
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 8.dp, bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Quick Notice / Action Cards (Notifications, Invites, Mentions)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                QuickNoticeBox(
                    icon = "👑",
                    title = "Official",
                    subtitle = "System notices",
                    bgBrush = RoyalGoldBrush,
                    modifier = Modifier.weight(1f),
                    onClick = {
                        val official = uiState.conversations.find { it.isOfficial }
                        if (official != null) viewModel.openConversation(official)
                    }
                )
                QuickNoticeBox(
                    icon = "💌",
                    title = "Party Invites",
                    subtitle = "3 new rooms",
                    bgBrush = EmeraldTurquoiseBrush,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.showToast("No pending party room invites ✨") }
                )
                QuickNoticeBox(
                    icon = "🔔",
                    title = "Mentions",
                    subtitle = "Room chat",
                    bgBrush = Brush.linearGradient(listOf(Color(0xFF7C4DFF), Color(0xFFBA68C8))),
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.showToast("All room mentions are up to date! 🔔") }
                )
            }
        }

        // Section Title
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Direct Chats & Friends",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Mark all read",
                    color = StarVibeTurquoise,
                    fontSize = 12.sp,
                    modifier = Modifier.clickable { viewModel.showToast("All messages marked as read") }
                )
            }
        }

        // Conversations list
        items(uiState.conversations) { conv ->
            ConversationItemRow(
                conv = conv,
                onClick = { viewModel.openConversation(conv) }
            )
        }
    }
}

@Composable
fun QuickNoticeBox(
    icon: String,
    title: String,
    subtitle: String,
    bgBrush: Brush,
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
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(bgBrush),
                contentAlignment = Alignment.Center
            ) {
                Text(icon, fontSize = 18.sp)
            }
            Text(
                text = title,
                color = Color.White,
                fontSize = 13.sp,
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

@Composable
fun ConversationItemRow(
    conv: Conversation,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF062624))
            .border(1.dp, if (conv.isOfficial) RoyalGoldBrush else Brush.linearGradient(listOf(Color(0xFF104441), Color(0xFF0B3331))), RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(12.dp)
            .testTag("conv_${conv.id}"),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(contentAlignment = Alignment.BottomEnd) {
            StarVibeAvatar(user = conv.user, size = 46.dp, showCrown = conv.user.vipLevel >= 5)
            if (conv.user.isOnline) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .clip(CircleShape)
                        .background(StarVibeEmerald)
                        .border(1.5.dp, Color(0xFF062624), CircleShape)
                )
            }
        }

        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = conv.user.name,
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    VipBadge(vipLevel = conv.user.vipLevel)
                }
                Text(
                    text = conv.time,
                    color = StarVibeTextMuted,
                    fontSize = 11.sp
                )
            }

            Spacer(modifier = Modifier.height(3.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = conv.lastMessage,
                    color = StarVibeTextSecondary,
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                if (conv.unreadCount > 0) {
                    Box(
                        modifier = Modifier
                            .size(18.dp)
                            .clip(CircleShape)
                            .background(StarVibeBadgeRed),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${conv.unreadCount}",
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ConversationDetailView(
    conversation: Conversation,
    uiState: StarVibeUiState,
    onBack: () -> Unit,
    onSend: (String) -> Unit
) {
    BackHandler { onBack() }
    var inputText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    LaunchedEffect(uiState.directMessages.size) {
        if (uiState.directMessages.isNotEmpty()) {
            listState.animateScrollToItem(uiState.directMessages.size - 1)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .background(Color(0xFF031615))
    ) {
        // Chat Header
        Surface(color = Color(0xFF062725)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }

                StarVibeAvatar(user = conversation.user, size = 38.dp, showCrown = false)
                Spacer(modifier = Modifier.width(10.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = conversation.user.name,
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        VipBadge(vipLevel = conversation.user.vipLevel)
                    }
                    Text(
                        text = if (conversation.user.isOnline) "Online • StarVibe Room Host" else "Offline",
                        color = if (conversation.user.isOnline) StarVibeEmerald else StarVibeTextMuted,
                        fontSize = 11.sp
                    )
                }

                IconButton(onClick = {}) {
                    Icon(Icons.Default.MoreVert, contentDescription = "Menu", tint = Color.White)
                }
            }
        }

        // Messages List
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 14.dp),
            contentPadding = PaddingValues(vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(uiState.directMessages) { msg ->
                val isMe = msg.isMe
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start
                ) {
                    Box(
                        modifier = Modifier
                            .widthIn(max = 280.dp)
                            .clip(
                                RoundedCornerShape(
                                    topStart = 16.dp,
                                    topEnd = 16.dp,
                                    bottomStart = if (isMe) 16.dp else 4.dp,
                                    bottomEnd = if (isMe) 4.dp else 16.dp
                                )
                            )
                            .background(
                                if (isMe) EmeraldTurquoiseBrush else Brush.linearGradient(
                                    listOf(Color(0xFF093734), Color(0xFF0F4743))
                                )
                            )
                            .border(
                                1.dp,
                                if (isMe) StarVibeTurquoise else Color(0xFF1A5A54),
                                RoundedCornerShape(16.dp)
                            )
                            .padding(horizontal = 12.dp, vertical = 9.dp)
                    ) {
                        Column {
                            Text(
                                text = msg.text,
                                color = if (isMe) Color.Black else Color.White,
                                fontSize = 13.sp,
                                fontWeight = if (isMe) FontWeight.SemiBold else FontWeight.Normal
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = msg.time,
                                color = if (isMe) Color(0x99000000) else StarVibeTextMuted,
                                fontSize = 9.sp,
                                modifier = Modifier.align(Alignment.End)
                            )
                        }
                    }
                }
            }
        }

        // Quick Emoji Reaction Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF062220))
                .padding(horizontal = 12.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            val emojis = listOf("👑", "💎", "✨", "🔥", "🎙️", "❤️")
            emojis.forEach { emoji ->
                Text(
                    text = emoji,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .clickable { onSend(emoji) }
                        .padding(4.dp)
                )
            }
        }

        // Chat Input Row
        Surface(
            color = Color(0xFF041A19),
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.navigationBars)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    placeholder = { Text("Type a message...", color = StarVibeTextMuted, fontSize = 13.sp) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = StarVibeTurquoise,
                        unfocusedBorderColor = Color(0xFF134E4A),
                        focusedContainerColor = Color(0xFF082D2B),
                        unfocusedContainerColor = Color(0xFF082D2B)
                    ),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("chat_input_field")
                )

                IconButton(
                    onClick = {
                        if (inputText.isNotBlank()) {
                            onSend(inputText)
                            inputText = ""
                        }
                    },
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(RoyalGoldBrush)
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Send",
                        tint = Color.Black
                    )
                }
            }
        }
    }
}
