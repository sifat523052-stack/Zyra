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
import com.example.model.ShopItem
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.viewmodel.StarVibeUiState
import com.example.viewmodel.StarVibeViewModel

@Composable
fun ShopScreen(
    viewModel: StarVibeViewModel,
    uiState: StarVibeUiState,
    modifier: Modifier = Modifier
) {
    BackHandler { viewModel.closeSubScreen() }

    val categories = listOf("Frame", "Entrance", "Bubble", "MicRing")
    val filteredItems = uiState.shopItems.filter { it.category == uiState.shopCategory }

    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .background(Color(0xFF031615))
    ) {
        // Top Header
        Surface(color = Color(0xFF062725)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = { viewModel.closeSubScreen() },
                        modifier = Modifier.testTag("shop_back_button")
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                    Text(
                        text = "StarVibe Luxury Shop",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF0A3734))
                        .border(1.dp, StarVibeTurquoise.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
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
                    categories.forEach { cat ->
                        val selected = uiState.shopCategory == cat
                        val label = when (cat) {
                            "Frame" -> "Frames 👑"
                            "Entrance" -> "Entrances 🐉"
                            "Bubble" -> "Bubbles 💬"
                            else -> "Mic Rings 🎙️"
                        }
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .then(if (selected) Modifier.background(RoyalGoldBrush) else Modifier.background(Color.Transparent))
                                .clickable { viewModel.setShopCategory(cat) }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = label,
                                color = if (selected) Color.Black else StarVibeTextSecondary,
                                fontSize = 11.sp,
                                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
                            )
                        }
                    }
                }
            }

            // Items List
            items(filteredItems) { item ->
                ShopItemRowCard(
                    item = item,
                    onAction = { viewModel.purchaseShopItem(item) }
                )
            }
        }
    }
}

@Composable
fun ShopItemRowCard(
    item: ShopItem,
    onAction: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(Color(0xFF062927))
            .border(
                1.dp,
                if (item.isEquipped) RoyalGoldBrush else Brush.linearGradient(listOf(Color(0xFF13534D), Color(0xFF0B3330))),
                RoundedCornerShape(18.dp)
            )
            .padding(14.dp)
            .testTag("shop_item_${item.id}")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF0B3835))
                        .border(1.5.dp, if (item.isEquipped) StarVibeGold else StarVibeTurquoise, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(item.iconEmoji, fontSize = 24.sp)
                }

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = item.name,
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        if (item.isEquipped) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(StarVibeEmerald)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text("EQUIPPED", color = Color.Black, fontSize = 8.sp, fontWeight = FontWeight.Black)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = item.description,
                        color = StarVibeTextSecondary,
                        fontSize = 11.sp,
                        lineHeight = 15.sp
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            if (item.isEquipped) {
                Button(
                    onClick = onAction,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0A3A37)),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                    modifier = Modifier.height(34.dp)
                ) {
                    Text("Unequip", color = StarVibeTextSecondary, fontSize = 11.sp)
                }
            } else if (item.isPurchased) {
                Button(
                    onClick = onAction,
                    colors = ButtonDefaults.buttonColors(containerColor = StarVibeTurquoise),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 4.dp),
                    modifier = Modifier.height(34.dp)
                ) {
                    Text("Equip", color = Color.Black, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            } else {
                Button(
                    onClick = onAction,
                    colors = ButtonDefaults.buttonColors(containerColor = StarVibeGold),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                    modifier = Modifier.height(34.dp)
                ) {
                    Text(
                        text = "💎 ${item.priceDiamonds}",
                        color = Color.Black,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
