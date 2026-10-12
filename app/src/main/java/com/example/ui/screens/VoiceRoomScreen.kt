package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.*
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.StarVibeRepository
import com.example.model.GiftItem
import com.example.model.MicSeat
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.viewmodel.StarVibeUiState
import com.example.viewmodel.StarVibeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VoiceRoomScreen(
    viewModel: StarVibeViewModel,
    uiState: StarVibeUiState,
    modifier: Modifier = Modifier
) {
    val room = uiState.activeRoom ?: return
    BackHandler {
        viewModel.exitVoiceRoom()
    }

    var messageText by remember { mutableStateOf("") }
    val chatListState = rememberLazyListState()
    var showSoundFxMenu by remember { mutableStateOf(false) }
    var isFollowingHost by remember { mutableStateOf(false) }
    var showGamePicker by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.roomMessages.size) {
        if (uiState.roomMessages.isNotEmpty()) {
            chatListState.animateScrollToItem(uiState.roomMessages.size - 1)
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF02211F),
                        Color(0xFF042F2D),
                        Color(0xFF031E1D),
                        Color(0xFF011413)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            // Ornate Room Top Header
            Surface(
                color = Color(0xCC021C1A),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Back & Room Info
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        IconButton(
                            onClick = { viewModel.exitVoiceRoom() },
                            modifier = Modifier
                                .size(36.dp)
                                .testTag("exit_room_button")
                        ) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Exit Room",
                                tint = Color.White
                            )
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = room.title,
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            Text(
                                text = "ID: ${room.id.replace("room_", "8880")} • ${room.countryFlag} ${room.countryName}",
                                color = StarVibeTurquoise,
                                fontSize = 10.sp
                            )
                        }
                    }

                    // Live Listener Count & Close Actions
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        // Live Listener Wave
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color(0xFF073834))
                                .border(1.dp, StarVibeTurquoise, RoundedCornerShape(14.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            MicWaveIndicator()
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${room.onlineCount}",
                                color = StarVibeTurquoise,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // Close button
                        IconButton(
                            onClick = { viewModel.exitVoiceRoom() },
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Color(0x40FF5252))
                        ) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "Close",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }

            // Ornate VIP Host Spotlight & Header Profile Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 4.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color(0xFF073834), Color(0xFF0F4A45), Color(0xFF073834))
                        )
                    )
                    .border(1.2.dp, RoyalGoldBrush, RoundedCornerShape(18.dp))
                    .padding(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Host Avatar & Profile Info
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(contentAlignment = Alignment.TopEnd) {
                            StarVibeAvatar(
                                user = room.host,
                                size = 48.dp,
                                showCrown = true,
                                isSpeaking = room.micSeats.firstOrNull()?.isSpeaking == true
                            )
                        }

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = room.host.name,
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Black
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                VipBadge(vipLevel = room.host.vipLevel)
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = "Host ID: ${room.host.starVibeId}",
                                    color = StarVibeTextSecondary,
                                    fontSize = 10.sp
                                )
                                Text("•", color = StarVibeTextMuted, fontSize = 10.sp)
                                Text(
                                    text = "🌟 ${room.charmPoints} Charm",
                                    color = StarVibeGoldLight,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    // Top Room Contributor Avatars & Follow Host
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Follow button
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .then(if (isFollowingHost) Modifier.background(Color(0xFF0C413D)) else Modifier.background(RoyalGoldBrush))
                                .clickable {
                                    isFollowingHost = !isFollowingHost
                                    viewModel.showToast(if (isFollowingHost) "Followed Host 👑" else "Unfollowed Host")
                                }
                                .padding(horizontal = 10.dp, vertical = 5.dp)
                        ) {
                            Text(
                                text = if (isFollowingHost) "Following" else "+ Follow",
                                color = if (isFollowingHost) StarVibeTurquoise else Color.Black,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black
                            )
                        }
                    }
                }
            }

            // Royal Announcement Ticker
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 2.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0x60032321))
                    .border(0.5.dp, Color(0xFF13534D), RoundedCornerShape(10.dp))
                    .padding(horizontal = 10.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("📢", fontSize = 11.sp)
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = room.announcement,
                    color = StarVibeGoldLight,
                    fontSize = 11.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
            }

            // Five-Column Layout for Numbered Circular Mic Seats (10 Seats Total)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 6.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(Color(0xEE06302D), Color(0xEE032220))
                        )
                    )
                    .border(1.2.dp, Color(0xFF165551), RoundedCornerShape(20.dp))
                    .padding(vertical = 10.dp, horizontal = 6.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    // Row 1: Seats 1 to 5 (Five columns)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        for (i in 0..4) {
                            val seat = room.micSeats.getOrNull(i) ?: MicSeat(i)
                            FiveColumnMicSeat(
                                seatNumber = i + 1,
                                seat = seat,
                                isMe = uiState.mySeatIndex == i,
                                isMuted = if (uiState.mySeatIndex == i) uiState.isMuted else seat.isMuted,
                                onClick = { viewModel.takeOrLeaveMicSeat(i) }
                            )
                        }
                    }

                    // Row 2: Seats 6 to 10 (Five columns)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        for (i in 5..9) {
                            val seat = room.micSeats.getOrNull(i) ?: MicSeat(i)
                            FiveColumnMicSeat(
                                seatNumber = i + 1,
                                seat = seat,
                                isMe = uiState.mySeatIndex == i,
                                isMuted = if (uiState.mySeatIndex == i) uiState.isMuted else seat.isMuted,
                                onClick = { viewModel.takeOrLeaveMicSeat(i) }
                            )
                        }
                    }
                }
            }

            // Live Chat Area
            LazyColumn(
                state = chatListState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
                contentPadding = PaddingValues(vertical = 6.dp)
            ) {
                items(uiState.roomMessages) { msg ->
                    if (msg.giftSent != null) {
                        // Luxury Gift Broadcast Banner
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    Brush.horizontalGradient(
                                        listOf(Color(0xB3FFD700), Color(0x80FF8F00))
                                    )
                                )
                                .border(1.dp, StarVibeGoldLight, RoundedCornerShape(12.dp))
                                .padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(msg.giftSent.iconEmoji, fontSize = 22.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "${msg.senderName} sent ${msg.giftSent.name} 👑",
                                    color = Color.Black,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Black
                                )
                                Text(
                                    text = "+${msg.giftSent.priceDiamonds * 10} Room Charm 🌟",
                                    color = Color(0xFF3E2723),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    } else if (msg.isSystem) {
                        // System / Entrance Announcement
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0x60004D40))
                                .border(0.5.dp, StarVibeTurquoise.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                                .padding(horizontal = 8.dp, vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "👑 ${msg.message}",
                                color = StarVibeGoldLight,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    } else {
                        // Normal chat speech bubble
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0x8803201F))
                                .border(0.5.dp, Color(0xFF13534D), RoundedCornerShape(12.dp))
                                .padding(horizontal = 10.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            VipBadge(vipLevel = msg.senderVip)
                            Text(
                                text = "${msg.senderName}:",
                                color = StarVibeTurquoise,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = msg.message,
                                color = Color.White,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }

            // Bottom Controls Bar (Microphone, Messages, Gifts, Games, Sound FX)
            Surface(
                color = Color(0xF5021514),
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.navigationBars)
            ) {
                Column(modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)) {
                    // Sound FX Bar (when expanded)
                    if (showSoundFxMenu) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 6.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFF073330))
                                .border(1.dp, StarVibeTurquoise, RoundedCornerShape(12.dp))
                                .padding(6.dp),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            SoundFxItem("👏 Claps") { viewModel.triggerSoundEffect("Applause 👏") }
                            SoundFxItem("🎉 Cheer") { viewModel.triggerSoundEffect("Cheer 🎉") }
                            SoundFxItem("😂 Laugh") { viewModel.triggerSoundEffect("Laugh 😂") }
                            SoundFxItem("🎺 Fanfare") { viewModel.triggerSoundEffect("Fanfare 🎺") }
                            SoundFxItem("💋 Kiss") { viewModel.triggerSoundEffect("Sweet Kiss 💋") }
                        }
                    }

                    // Main Action Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        // Microphone Toggle
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(
                                    if (uiState.mySeatIndex != null) {
                                        if (uiState.isMuted) Color(0xFFE53935) else StarVibeEmerald
                                    } else Color(0xFF0E4340)
                                )
                                .border(1.dp, if (uiState.mySeatIndex != null && !uiState.isMuted) RoyalGoldBrush else Brush.linearGradient(listOf(StarVibeTurquoise, Color.Transparent)), CircleShape)
                                .clickable {
                                    if (uiState.mySeatIndex != null) {
                                        viewModel.toggleMicMute()
                                    } else {
                                        viewModel.showToast("Tap an open mic seat above to take the mic! 🎙️")
                                    }
                                }
                                .testTag("mic_toggle_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (uiState.isMuted) Icons.Default.MicOff else Icons.Default.Mic,
                                contentDescription = "Mic Toggle",
                                tint = Color.White,
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        // Chat Input Field
                        OutlinedTextField(
                            value = messageText,
                            onValueChange = { messageText = it },
                            placeholder = { Text("Chat in room...", color = StarVibeTextMuted, fontSize = 12.sp) },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = StarVibeTurquoise,
                                unfocusedBorderColor = Color(0xFF134E4A),
                                focusedContainerColor = Color(0xFF062927),
                                unfocusedContainerColor = Color(0xFF062927)
                            ),
                            shape = RoundedCornerShape(20.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp)
                                .testTag("room_chat_input"),
                            trailingIcon = {
                                if (messageText.isNotBlank()) {
                                    IconButton(
                                        onClick = {
                                            viewModel.sendRoomChatMessage(messageText)
                                            messageText = ""
                                        }
                                    ) {
                                        Icon(
                                            Icons.AutoMirrored.Filled.Send,
                                            contentDescription = "Send",
                                            tint = StarVibeTurquoise,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }
                        )

                        // Sound Effects Trigger
                        IconButton(
                            onClick = { showSoundFxMenu = !showSoundFxMenu },
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF083330))
                                .border(1.dp, Color(0xFF13534D), CircleShape)
                        ) {
                            Text("🔊", fontSize = 17.sp)
                        }

                        // Games Button
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF332605))
                                .border(1.dp, StarVibeGold, CircleShape)
                                .clickable { showGamePicker = true }
                                .testTag("room_games_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("🎲", fontSize = 18.sp)
                        }

                        // Luxury Gift Button
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(RoyalGoldBrush)
                                .border(1.5.dp, Color.White, CircleShape)
                                .clickable { viewModel.toggleGiftSheet(true) }
                                .testTag("open_gifts_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("🎁", fontSize = 22.sp)
                        }
                    }
                }
            }
        }

        // Luxury Gift Pop Celebration Overlay
        if (uiState.activeGiftAnimation != null) {
            LuxuryGiftCelebrationOverlay(
                gift = uiState.activeGiftAnimation,
                onDismiss = { /* auto dismisses */ }
            )
        }
    }

    // Gift Picker Bottom Sheet
    if (uiState.showGiftSheet) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.toggleGiftSheet(false) },
            containerColor = Color(0xFF032220),
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .windowInsetsPadding(WindowInsets.navigationBars)
            ) {
                // Header with Diamond Balance
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("🎁", fontSize = 18.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Send Royal Gift",
                            color = StarVibeGoldLight,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF093734))
                            .border(1.dp, StarVibeTurquoise, RoundedCornerShape(12.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text("💎", fontSize = 12.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${uiState.diamonds}",
                            color = StarVibeTurquoise,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Gifts Grid (4x2)
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    val giftChunks = StarVibeRepository.gifts.chunked(4)
                    giftChunks.forEach { rowGifts ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            rowGifts.forEach { gift ->
                                GiftPickerCard(
                                    gift = gift,
                                    onClick = { viewModel.sendGift(gift) },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

    // Mini-Game Picker Dialog
    if (showGamePicker) {
        AlertDialog(
            onDismissRequest = { showGamePicker = false },
            containerColor = Color(0xFF042422),
            shape = RoundedCornerShape(22.dp),
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("🎲", fontSize = 20.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Voice Party Mini-Games", color = StarVibeGoldLight, fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Button(
                        onClick = {
                            showGamePicker = false
                            viewModel.spinLuckyWheel()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = StarVibeTurquoise),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("🎡 Spin Lucky Star Wheel (20 💎)", color = Color.Black, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = {
                            showGamePicker = false
                            viewModel.rollDice()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = StarVibeGold),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("🎲 Roll Star Dice Clash", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showGamePicker = false }) {
                    Text("Close", color = StarVibeTextMuted)
                }
            }
        )
    }
}

@Composable
fun FiveColumnMicSeat(
    seatNumber: Int,
    seat: MicSeat,
    isMe: Boolean,
    isMuted: Boolean,
    onClick: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "micPulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (seat.isSpeaking && !isMuted) 1.15f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(400, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp, horizontal = 2.dp)
            .testTag("mic_seat_${seat.seatIndex}")
    ) {
        Box(
            modifier = Modifier.size(54.dp),
            contentAlignment = Alignment.Center
        ) {
            // Glowing pulsing ring if speaking
            if (seat.isSpeaking && !isMuted) {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .scale(pulseScale)
                        .border(2.dp, StarVibeTurquoise, CircleShape)
                )
            }

            // Circular Gold Seat Frame
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(
                        if (seat.user != null) Color(seat.user.avatarBgColor)
                        else Color(0xFF072C29)
                    )
                    .border(
                        1.5.dp,
                        if (isMe) RoyalGoldBrush
                        else if (seat.user != null) Brush.linearGradient(listOf(StarVibeGold, StarVibeEmerald))
                        else Brush.linearGradient(listOf(Color(0xFF1A5A54), Color(0xFF0C3D39))),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (seat.user != null) {
                    Text(seat.user.avatarEmoji, fontSize = 22.sp)
                } else {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Take Seat",
                        tint = StarVibeTurquoise,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            // Numbered Seat Badge (Top-Left corner)
            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .offset(x = 1.dp, y = (-2).dp)
                    .size(17.dp)
                    .clip(CircleShape)
                    .background(
                        if (seat.user != null) RoyalGoldBrush else Brush.linearGradient(listOf(Color(0xFF0C3D39), Color(0xFF052422)))
                    )
                    .border(0.5.dp, StarVibeGoldLight, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "$seatNumber",
                    color = if (seat.user != null) Color.Black else StarVibeGoldLight,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Black
                )
            }

            // Mute Indicator Badge (Bottom-Right corner)
            if (isMuted && seat.user != null) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .offset(x = (-1).dp, y = 1.dp)
                        .size(16.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE53935)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.MicOff,
                        contentDescription = "Muted",
                        tint = Color.White,
                        modifier = Modifier.size(10.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(2.dp))

        // Participant Information below seat
        Text(
            text = if (isMe) "You" else seat.user?.name ?: "Seat $seatNumber",
            color = if (isMe) StarVibeGoldLight else if (seat.user != null) Color.White else StarVibeTextMuted,
            fontSize = 10.sp,
            fontWeight = if (seat.user != null) FontWeight.Bold else FontWeight.Normal,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        if (seat.user != null) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color(0xFF0E4340))
                    .padding(horizontal = 3.dp, vertical = 1.dp)
            ) {
                Text(
                    text = "V${seat.user.vipLevel}",
                    color = StarVibeGoldLight,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun GiftPickerCard(
    gift: GiftItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .padding(4.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFF062C2A))
            .border(1.dp, Color(gift.color).copy(alpha = 0.5f), RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp, horizontal = 4.dp)
            .testTag("gift_${gift.id}"),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Text(gift.iconEmoji, fontSize = 26.sp)
            Text(
                text = gift.name,
                color = Color.White,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("💎", fontSize = 9.sp)
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = "${gift.priceDiamonds}",
                    color = StarVibeGoldLight,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun SoundFxItem(name: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFF0E4340))
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(name, color = StarVibeTurquoise, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
    }
}

