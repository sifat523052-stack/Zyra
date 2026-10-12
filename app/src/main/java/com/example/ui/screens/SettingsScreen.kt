package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.viewmodel.StarVibeUiState
import com.example.viewmodel.StarVibeViewModel

@Composable
fun SettingsScreen(
    viewModel: StarVibeViewModel,
    uiState: StarVibeUiState,
    modifier: Modifier = Modifier
) {
    BackHandler { viewModel.closeSubScreen() }

    var hdAudioEnabled by remember { mutableStateOf(true) }
    var noiseReductionEnabled by remember { mutableStateOf(true) }
    var stealthModeEnabled by remember { mutableStateOf(false) }
    var allowDmsEnabled by remember { mutableStateOf(true) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .background(Color(0xFF031615))
    ) {
        // Top Bar
        Surface(color = Color(0xFF062725)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { viewModel.closeSubScreen() },
                    modifier = Modifier.testTag("settings_back_button")
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
                Text(
                    text = "Settings",
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
            // Audio & Mic Settings
            item {
                Text(
                    text = "🎙️ Voice & Audio Settings",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                SettingsSectionBox {
                    SettingsSwitchRow(
                        title = "HD Audio Mode (48kHz)",
                        subtitle = "High fidelity party room acoustics",
                        checked = hdAudioEnabled,
                        onCheckedChange = { hdAudioEnabled = it }
                    )
                    Divider(color = Color(0xFF13534D))
                    SettingsSwitchRow(
                        title = "Noise Suppression",
                        subtitle = "AI background noise and echo cancellation",
                        checked = noiseReductionEnabled,
                        onCheckedChange = { noiseReductionEnabled = it }
                    )
                }
            }

            // Privacy & Room Stealth
            item {
                Text(
                    text = "🛡️ Privacy & Stealth",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                SettingsSectionBox {
                    SettingsSwitchRow(
                        title = "Invisible Room Entry",
                        subtitle = "VIP perk: hide entrance announcement",
                        checked = stealthModeEnabled,
                        onCheckedChange = {
                            stealthModeEnabled = it
                            viewModel.showToast(if (it) "Stealth Entry Activated 🕶️" else "Stealth Entry Disabled")
                        }
                    )
                    Divider(color = Color(0xFF13534D))
                    SettingsSwitchRow(
                        title = "Direct Message Requests",
                        subtitle = "Allow messages from anyone in voice rooms",
                        checked = allowDmsEnabled,
                        onCheckedChange = { allowDmsEnabled = it }
                    )
                }
            }

            // Account & Storage
            item {
                Text(
                    text = "👤 Account & Storage",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                SettingsSectionBox {
                    SettingsActionRow(
                        title = "StarVibe ID",
                        value = uiState.user.starVibeId,
                        onClick = { viewModel.showToast("StarVibe ID: ${uiState.user.starVibeId}") }
                    )
                    Divider(color = Color(0xFF13534D))
                    SettingsActionRow(
                        title = "Linked Google Account",
                        value = "Connected",
                        onClick = { viewModel.showToast("Account is securely connected to Google") }
                    )
                    Divider(color = Color(0xFF13534D))
                    SettingsActionRow(
                        title = "Clear Voice Room Cache",
                        value = "42.8 MB",
                        onClick = { viewModel.showToast("Voice Room cache cleared! 🧹") }
                    )
                }
            }

            // About StarVibe Branding Box
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFF062927))
                        .border(1.dp, RoyalGoldBrush, RoundedCornerShape(20.dp))
                        .padding(20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_starvibe_logo),
                            contentDescription = "StarVibe Logo",
                            modifier = Modifier
                                .size(72.dp)
                                .clip(CircleShape)
                                .border(2.dp, RoyalGoldBrush, CircleShape)
                        )

                        Text(
                            text = "StarVibe",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold
                        )

                        Text(
                            text = "Connect • Chat • Party • Shine",
                            color = StarVibeGoldLight,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                        Text(
                            text = "Version 2.4.0 (Build 2400)",
                            color = StarVibeTextMuted,
                            fontSize = 11.sp
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            Text(
                                text = "Terms of Service",
                                color = StarVibeTurquoise,
                                fontSize = 11.sp,
                                modifier = Modifier.clickable { viewModel.showToast("Terms of Service") }
                            )
                            Text(
                                text = "Privacy Policy",
                                color = StarVibeTurquoise,
                                fontSize = 11.sp,
                                modifier = Modifier.clickable { viewModel.showToast("Privacy Policy") }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsSectionBox(content: @Composable ColumnScope.() -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(Color(0xFF062826))
            .border(1.dp, Color(0xFF13534D), RoundedCornerShape(18.dp))
            .padding(14.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp), content = content)
    }
}

@Composable
fun SettingsSwitchRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            Text(subtitle, color = StarVibeTextMuted, fontSize = 11.sp)
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.Black,
                checkedTrackColor = StarVibeTurquoise,
                uncheckedTrackColor = Color(0xFF0E4340)
            )
        )
    }
}

@Composable
fun SettingsActionRow(
    title: String,
    value: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(title, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Medium)
        Text(value, color = StarVibeTurquoise, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}
