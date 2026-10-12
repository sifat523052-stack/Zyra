package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.StarVibeRepository
import com.example.model.PartyRoom
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.viewmodel.StarVibeUiState
import com.example.viewmodel.StarVibeViewModel
import com.example.viewmodel.SubScreen

@Composable
fun PartyScreen(
    viewModel: StarVibeViewModel,
    uiState: StarVibeUiState,
    modifier: Modifier = Modifier
) {
    val categories = listOf("All", "Popular", "Music", "Chill", "Karaoke", "Gaming", "Dating")
    val countries = listOf(
        Pair("Global", "🌐 All"),
        Pair("USA", "🇺🇸 USA"),
        Pair("UK", "🇬🇧 UK"),
        Pair("Bangladesh", "🇧🇩 Bangladesh"),
        Pair("India", "🇮🇳 India"),
        Pair("UAE", "🇦🇪 UAE"),
        Pair("Philippines", "🇵🇭 Philippines"),
        Pair("Saudi Arabia", "🇸🇦 Saudi Arabia")
    )

    var showHallOfFameDialog by remember { mutableStateOf(false) }
    var showPrizePoolDialog by remember { mutableStateOf(false) }
    var showLudoDialog by remember { mutableStateOf(false) }

    val filteredRooms = remember(
        uiState.rooms,
        uiState.selectedCategory,
        uiState.selectedCountry,
        uiState.searchQuery,
        uiState.topNavSelection
    ) {
        uiState.rooms.filter { room ->
            val matchesCategory = uiState.selectedCategory == "All" ||
                    room.category.equals(uiState.selectedCategory, ignoreCase = true) ||
                    (uiState.selectedCategory == "Popular" && room.isHot)

            val matchesCountry = uiState.selectedCountry == "Global" ||
                    room.countryCode.equals(uiState.selectedCountry, ignoreCase = true)

            val matchesSearch = uiState.searchQuery.isBlank() ||
                    room.title.contains(uiState.searchQuery, ignoreCase = true) ||
                    room.host.name.contains(uiState.searchQuery, ignoreCase = true) ||
                    room.tag.contains(uiState.searchQuery, ignoreCase = true)

            val matchesMine = if (uiState.topNavSelection == "Mine") {
                room.host.id == uiState.user.id || room.isFeatured
            } else true

            matchesCategory && matchesCountry && matchesSearch && matchesMine
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Color.Transparent,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.toggleCreateRoomDialog(true) },
                containerColor = StarVibeGold,
                contentColor = Color.Black,
                shape = CircleShape,
                modifier = Modifier.testTag("create_room_fab")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Create Room")
                    Text("Party", fontWeight = FontWeight.Black, fontSize = 14.sp)
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(bottom = 90.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Search Bar (expanded or regular)
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = uiState.searchQuery,
                        onValueChange = { viewModel.updateSearchQuery(it) },
                        placeholder = { Text("Search StarVibe rooms, hosts, #tags...", color = StarVibeTextMuted, fontSize = 12.sp) },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = StarVibeTurquoise) },
                        trailingIcon = {
                            if (uiState.searchQuery.isNotEmpty()) {
                                IconButton(onClick = { viewModel.updateSearchQuery("") }) {
                                    Icon(Icons.Default.Close, contentDescription = "Clear", tint = StarVibeTextMuted)
                                }
                            }
                        },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = StarVibeTurquoise,
                            unfocusedBorderColor = Color(0xFF134E4A),
                            focusedContainerColor = Color(0xCC062927),
                            unfocusedContainerColor = Color(0x99041F1E),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        shape = RoundedCornerShape(22.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("search_room_input")
                    )
                }
            }

            // Quick Feature Shortcuts Bar (Gift Ranking, CP Ranking, Family, Hall of Fame, Prize Pool)
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    HomeShortcutItem(
                        iconEmoji = "🎁",
                        label = "Gift Rank",
                        glowColor = StarVibeGold,
                        onClick = { viewModel.openGiftRanking() }
                    )
                    HomeShortcutItem(
                        iconEmoji = "💍",
                        label = "CP Rank",
                        glowColor = Color(0xFFFF4081),
                        onClick = { viewModel.openCpRanking() }
                    )
                    HomeShortcutItem(
                        iconEmoji = "🛡️",
                        label = "Family",
                        glowColor = StarVibeEmerald,
                        onClick = { viewModel.openSubScreen(SubScreen.FAMILY) }
                    )
                    HomeShortcutItem(
                        iconEmoji = "👑",
                        label = "Hall of Fame",
                        glowColor = StarVibeGoldLight,
                        onClick = { showHallOfFameDialog = true }
                    )
                    HomeShortcutItem(
                        iconEmoji = "💎",
                        label = "Prize Pool",
                        glowColor = StarVibeTurquoise,
                        onClick = { showPrizePoolDialog = true }
                    )
                }
            }

            // Large Promotional Banner Carousel
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .clip(RoundedCornerShape(22.dp))
                        .border(1.5.dp, RoyalGoldBrush, RoundedCornerShape(22.dp))
                        .background(Color(0xFF042422))
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_party_hero),
                        contentDescription = "StarVibe Party Lounge",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(145.dp),
                        contentScale = ContentScale.Crop
                    )

                    // Overlay with Crown & Prize announcement
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(145.dp)
                            .background(
                                Brush.verticalGradient(
                                    listOf(Color(0x30000000), Color(0xEE021716))
                                )
                            )
                            .padding(14.dp),
                        contentAlignment = Alignment.BottomStart
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("👑", fontSize = 16.sp)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "StarVibe Galaxy Gala",
                                        color = StarVibeGoldLight,
                                        fontSize = 17.sp,
                                        fontWeight = FontWeight.Black
                                    )
                                }
                                Text(
                                    text = "100,000 💎 Prize Pool • Ends in 03h:24m",
                                    color = StarVibeTurquoise,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(RoyalGoldBrush)
                                    .clickable {
                                        val firstRoom = uiState.rooms.firstOrNull()
                                        if (firstRoom != null) viewModel.enterVoiceRoom(firstRoom)
                                    }
                                    .padding(horizontal = 12.dp, vertical = 7.dp)
                            ) {
                                Text(
                                    text = "JOIN GALA 👑",
                                    color = Color.Black,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }
                        }
                    }
                }
            }

            // Interactive Mini-Games Showcase (Ludo Star Party & Game Center)
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Ludo Star Party Card
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(18.dp))
                            .background(
                                Brush.verticalGradient(
                                    listOf(Color(0xFF093936), Color(0xFF052725))
                                )
                            )
                            .border(1.dp, StarVibeTurquoise.copy(alpha = 0.6f), RoundedCornerShape(18.dp))
                            .clickable { showLudoDialog = true }
                            .padding(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF0B4642))
                                    .border(1.dp, StarVibeTurquoise, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("🎯", fontSize = 20.sp)
                            }
                            Column {
                                Text(
                                    text = "Ludo Party",
                                    color = Color.White,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "4-Player Voice Clash",
                                    color = StarVibeTurquoise,
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }

                    // Game Center Card
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(18.dp))
                            .background(
                                Brush.verticalGradient(
                                    listOf(Color(0xFF2C2203), Color(0xFF072B28))
                                )
                            )
                            .border(1.dp, RoyalGoldBrush, RoundedCornerShape(18.dp))
                            .clickable { viewModel.openSubScreen(SubScreen.GAME_CENTER) }
                            .padding(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF453606))
                                    .border(1.dp, StarVibeGold, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("🎲", fontSize = 20.sp)
                            }
                            Column {
                                Text(
                                    text = "Game Center",
                                    color = StarVibeGoldLight,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Wheel & Dice Spin",
                                    color = StarVibeTextSecondary,
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }
                }
            }

            // Top Ranking Dual Showcase (Gift Ranking & CP Ranking Podium Previews)
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Gift Ranking Mini Podium
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(18.dp))
                            .background(Color(0xFF062826))
                            .border(1.dp, RoyalGoldBrush, RoundedCornerShape(18.dp))
                            .clickable { viewModel.openGiftRanking() }
                            .padding(12.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("🎁", fontSize = 14.sp)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Gift Ranking",
                                        color = StarVibeGoldLight,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Text("More >", color = StarVibeTurquoise, fontSize = 10.sp)
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text("👑", fontSize = 16.sp)
                                StarVibeAvatar(
                                    user = StarVibeRepository.sampleUsers[0],
                                    size = 32.dp,
                                    showCrown = false
                                )
                                Column {
                                    Text(
                                        text = StarVibeRepository.sampleUsers[0].name,
                                        color = Color.White,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text("8.9M 🌟", color = StarVibeGold, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }

                    // CP Ranking Mini Podium
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(18.dp))
                            .background(Color(0xFF062826))
                            .border(1.dp, Color(0xFFFF4081).copy(alpha = 0.6f), RoundedCornerShape(18.dp))
                            .clickable { viewModel.openCpRanking() }
                            .padding(12.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("💍", fontSize = 14.sp)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "CP Ranking",
                                        color = Color(0xFFFF80AB),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Text("More >", color = StarVibeTurquoise, fontSize = 10.sp)
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text("💖", fontSize = 14.sp)
                                StarVibeAvatar(
                                    user = StarVibeRepository.sampleUsers[0],
                                    size = 28.dp,
                                    showCrown = false
                                )
                                Text("❤️", fontSize = 10.sp)
                                StarVibeAvatar(
                                    user = StarVibeRepository.sampleUsers[3],
                                    size = 28.dp,
                                    showCrown = false
                                )
                                Column {
                                    Text(
                                        text = "12.8M CP",
                                        color = Color(0xFFFF80AB),
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Mega Prize Pool Strip
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            Brush.horizontalGradient(
                                listOf(Color(0xFF073834), Color(0xFF135750), Color(0xFF073834))
                            )
                        )
                        .border(1.dp, StarVibeTurquoise.copy(alpha = 0.7f), RoundedCornerShape(16.dp))
                        .clickable { showPrizePoolDialog = true }
                        .padding(horizontal = 14.dp, vertical = 10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text("💎", fontSize = 20.sp)
                            Column {
                                Text(
                                    text = "StarVibe Mega Prize Pool",
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "${StarVibeRepository.prizePoolDiamonds} Diamonds Jackpots",
                                    color = StarVibeGoldLight,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(StarVibeTurquoise)
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text("Rules & Claim", color = Color.Black, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Country / Region Filter Tabs
            item {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "🌍 Party by Country & Region",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(countries) { (code, label) ->
                            val isSelected = uiState.selectedCountry == code
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(14.dp))
                                    .then(
                                        if (isSelected) {
                                            Modifier
                                                .background(RoyalGoldBrush)
                                                .border(1.dp, StarVibeGoldLight, RoundedCornerShape(14.dp))
                                        } else {
                                            Modifier
                                                .background(Color(0xFF072F2D))
                                                .border(0.5.dp, Color(0xFF13534D), RoundedCornerShape(14.dp))
                                        }
                                    )
                                    .clickable { viewModel.setCountryFilter(code) }
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                                    .testTag("country_$code")
                            ) {
                                Text(
                                    text = label,
                                    color = if (isSelected) Color.Black else StarVibeTextSecondary,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Black else FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }

            // Category Filter Tabs
            item {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(categories) { cat ->
                        val isSelected = uiState.selectedCategory == cat
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .then(
                                    if (isSelected) {
                                        Modifier
                                            .background(
                                                Brush.horizontalGradient(
                                                    listOf(StarVibeTurquoise, StarVibeEmerald)
                                                )
                                            )
                                    } else {
                                        Modifier.background(Color(0xFF062826))
                                    }
                                )
                                .border(
                                    1.dp,
                                    if (isSelected) StarVibeTurquoise else Color(0xFF124B47),
                                    RoundedCornerShape(14.dp)
                                )
                                .clickable { viewModel.selectCategory(cat) }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                                .testTag("cat_$cat")
                        ) {
                            Text(
                                text = cat,
                                color = if (isSelected) Color.Black else StarVibeTextSecondary,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            )
                        }
                    }
                }
            }

            // Section Header: Featured Live Voice Party Rooms
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("👑", fontSize = 16.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (uiState.topNavSelection == "Mine") "Mine: Followed & Hosted Rooms" else "Featured Voice Party Rooms",
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Text(
                        text = "${filteredRooms.size} Live",
                        color = StarVibeTurquoise,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Room Cards
            items(filteredRooms) { room ->
                FeaturedPartyRoomCard(
                    room = room,
                    onClick = { viewModel.enterVoiceRoom(room) }
                )
            }
        }
    }

    // Dialogs
    if (showHallOfFameDialog) {
        HallOfFameDialog(onDismiss = { showHallOfFameDialog = false })
    }

    if (showPrizePoolDialog) {
        PrizePoolDialog(
            diamonds = StarVibeRepository.prizePoolDiamonds,
            onDismiss = { showPrizePoolDialog = false },
            onClaim = {
                viewModel.showToast("Claimed 500 Daily Prize Pool Bonus! 💎")
                showPrizePoolDialog = false
            }
        )
    }

    if (showLudoDialog) {
        LudoGameDialog(
            onDismiss = { showLudoDialog = false },
            onPlay = {
                viewModel.showToast("Joined Ludo Voice Match with 4 party members! 🎯")
                showLudoDialog = false
            }
        )
    }

    // Create Room Dialog
    if (uiState.showCreateRoomDialog) {
        CreateRoomDialog(
            onDismiss = { viewModel.toggleCreateRoomDialog(false) },
            onCreate = { title, category, tag ->
                viewModel.createRoom(title, category, tag)
            }
        )
    }
}

@Composable
fun HomeShortcutItem(
    iconEmoji: String,
    label: String,
    glowColor: Color,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .size(46.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        listOf(glowColor.copy(alpha = 0.25f), Color(0xFF042422))
                    )
                )
                .border(1.5.dp, glowColor.copy(alpha = 0.8f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(iconEmoji, fontSize = 22.sp)
        }
        Text(
            text = label,
            color = Color.White,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1
        )
    }
}

@Composable
fun FeaturedPartyRoomCard(
    room: PartyRoom,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF083330), Color(0xFF052422))
                )
            )
            .border(
                1.2.dp,
                if (room.isHot) RoyalGoldBrush else Brush.linearGradient(
                    listOf(Color(0xFF13534D), Color(0xFF093734))
                ),
                RoundedCornerShape(20.dp)
            )
            .clickable(onClick = onClick)
            .padding(14.dp)
            .testTag("room_card_${room.id}")
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            // Header Row: Host Avatar, Title, Country Flag, Live count
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    StarVibeAvatar(
                        user = room.host,
                        size = 46.dp,
                        showCrown = true,
                        isSpeaking = room.micSeats.firstOrNull()?.isSpeaking == true
                    )

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = room.title,
                                color = Color.White,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        Spacer(modifier = Modifier.height(2.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = room.countryFlag,
                                fontSize = 12.sp
                            )
                            Text(
                                text = room.host.name,
                                color = StarVibeTextSecondary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                            VipBadge(vipLevel = room.host.vipLevel)
                        }
                    }
                }

                // Live Listener Badge
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0x80004D40))
                        .border(0.5.dp, StarVibeTurquoise, RoundedCornerShape(12.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    MicWaveIndicator()
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = "${room.onlineCount}",
                        color = StarVibeTurquoise,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Middle: Room Category, Tag & Charm Points
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF0E4340))
                            .padding(horizontal = 7.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = room.category,
                            color = StarVibeTurquoise,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF1B4D3E))
                            .padding(horizontal = 7.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = room.tag,
                            color = StarVibeEmerald,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("🌟", fontSize = 11.sp)
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = "${room.charmPoints}",
                        color = StarVibeGoldLight,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // Bottom Mic Seats Preview
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0x80031918))
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "Mics:",
                        color = StarVibeTextMuted,
                        fontSize = 11.sp
                    )

                    room.micSeats.take(5).forEach { seat ->
                        if (seat.user != null) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(Color(seat.user.avatarBgColor))
                                    .border(
                                        1.dp,
                                        if (seat.isSpeaking) StarVibeTurquoise else Color.Transparent,
                                        CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(seat.user.avatarEmoji, fontSize = 12.sp)
                            }
                        } else {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF072B28)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.MicNone,
                                    contentDescription = "Empty",
                                    tint = Color(0xFF1A5A54),
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Join Party",
                        color = StarVibeGold,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Icon(
                        Icons.Default.ChevronRight,
                        contentDescription = "Join",
                        tint = StarVibeGold,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun HallOfFameDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF042422),
        shape = RoundedCornerShape(22.dp),
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("👑", fontSize = 22.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text("StarVibe Hall of Fame", color = StarVibeGoldLight, fontWeight = FontWeight.Black)
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "Honoring the greatest emperors and superstars of StarVibe!",
                    color = StarVibeTextSecondary,
                    fontSize = 12.sp
                )

                StarVibeRepository.hallOfFame.forEach { legend ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color(0xFF062927))
                            .border(1.dp, RoyalGoldBrush, RoundedCornerShape(14.dp))
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "#${legend.rank}",
                            color = StarVibeGold,
                            fontWeight = FontWeight.Black,
                            fontSize = 14.sp
                        )
                        StarVibeAvatar(user = legend.user, size = 36.dp, showCrown = true)
                        Column(modifier = Modifier.weight(1f)) {
                            Text(legend.user.name, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Text("${legend.badgeTitle} • ${legend.honoraryYear}", color = StarVibeTextMuted, fontSize = 10.sp)
                        }
                        Text(legend.totalCharmWon, color = StarVibeTurquoise, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = StarVibeGold),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Close", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        }
    )
}

@Composable
fun PrizePoolDialog(
    diamonds: Long,
    onDismiss: () -> Unit,
    onClaim: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF042422),
        shape = RoundedCornerShape(22.dp),
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("💎", fontSize = 22.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Mega Prize Pool", color = StarVibeTurquoise, fontWeight = FontWeight.Black)
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(RoyalGoldBrush)
                        .padding(14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("TOTAL JACKPOT POOL", color = Color.Black, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        Text("$diamonds 💎", color = Color.Black, fontSize = 22.sp, fontWeight = FontWeight.Black)
                    }
                }

                Text(
                    text = "• Top 1 Party Room host wins 40% of the jackpot!\n• Top 3 Gift senders share 35% of the pool!\n• All active voice room listeners share 25% daily bonus!",
                    color = StarVibeTextSecondary,
                    fontSize = 12.sp,
                    lineHeight = 18.sp
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onClaim,
                colors = ButtonDefaults.buttonColors(containerColor = StarVibeGold),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Claim Today's Share", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = StarVibeTextMuted)
            }
        }
    )
}

@Composable
fun LudoGameDialog(
    onDismiss: () -> Unit,
    onPlay: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF042422),
        shape = RoundedCornerShape(22.dp),
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("🎯", fontSize = 22.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Ludo Star Party", color = Color.White, fontWeight = FontWeight.Black)
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "Real-time 4-player voice room board game. Roll dice, knock out rivals, and win Star Coins & Diamonds!",
                    color = StarVibeTextSecondary,
                    fontSize = 12.sp
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Box(modifier = Modifier.size(40.dp).background(Color(0xFFE53935), CircleShape), contentAlignment = Alignment.Center) { Text("🔴") }
                    Box(modifier = Modifier.size(40.dp).background(Color(0xFF43A047), CircleShape), contentAlignment = Alignment.Center) { Text("🟢") }
                    Box(modifier = Modifier.size(40.dp).background(Color(0xFF1E88E5), CircleShape), contentAlignment = Alignment.Center) { Text("🔵") }
                    Box(modifier = Modifier.size(40.dp).background(StarVibeGold, CircleShape), contentAlignment = Alignment.Center) { Text("🟡") }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onPlay,
                colors = ButtonDefaults.buttonColors(containerColor = StarVibeTurquoise),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Start Match 🎲", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Close", color = StarVibeTextMuted)
            }
        }
    )
}

@Composable
fun CreateRoomDialog(
    onDismiss: () -> Unit,
    onCreate: (String, String, String) -> Unit
) {
    var roomTitle by remember { mutableStateOf("") }
    var selectedCat by remember { mutableStateOf("Music") }
    var tagText by remember { mutableStateOf("#PartyVibe") }
    val categories = listOf("Music", "Chill", "Karaoke", "Gaming", "Dating")

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF052A28),
        shape = RoundedCornerShape(24.dp),
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("👑", fontSize = 20.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Create Voice Party", color = Color.White, fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = roomTitle,
                    onValueChange = { roomTitle = it },
                    label = { Text("Room Title", color = StarVibeTextSecondary) },
                    placeholder = { Text("e.g. VIP Star Club Night", color = StarVibeTextMuted) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = StarVibeTurquoise,
                        unfocusedBorderColor = Color(0xFF175752)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Text("Category", color = StarVibeTextSecondary, fontSize = 12.sp)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(categories) { cat ->
                        val sel = selectedCat == cat
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .then(if (sel) Modifier.background(RoyalGoldBrush) else Modifier.background(Color(0xFF093734)))
                                .clickable { selectedCat = cat }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = cat,
                                color = if (sel) Color.Black else Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = tagText,
                    onValueChange = { tagText = it },
                    label = { Text("Room Tag", color = StarVibeTextSecondary) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = StarVibeTurquoise,
                        unfocusedBorderColor = Color(0xFF175752)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onCreate(roomTitle, selectedCat, tagText) },
                colors = ButtonDefaults.buttonColors(containerColor = StarVibeGold),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Start Party", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = StarVibeTextMuted)
            }
        }
    )
}

