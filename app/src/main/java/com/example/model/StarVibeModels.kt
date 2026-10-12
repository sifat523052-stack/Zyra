package com.example.model

import androidx.compose.ui.graphics.Color

data class User(
    val id: String,
    val name: String,
    val starVibeId: String,
    val avatarEmoji: String,
    val avatarBgColor: Long = 0xFF00BFA5,
    val vipLevel: Int = 1,
    val level: Int = 10,
    val wealthLevel: Int = 8,
    val charmLevel: Int = 12,
    val followingCount: Int = 45,
    val followersCount: Int = 230,
    val visitorsCount: Int = 1890,
    val bio: String = "Life is a party! Shine with StarVibe ✨",
    val isOnline: Boolean = true
)

data class MicSeat(
    val seatIndex: Int,
    val user: User? = null,
    val isMuted: Boolean = false,
    val isSpeaking: Boolean = false,
    val isLocked: Boolean = false
)

data class PartyRoom(
    val id: String,
    val title: String,
    val host: User,
    val category: String, // Music, Chill, Gaming, Dating, Karaoke
    val tag: String,
    val onlineCount: Int,
    val isHot: Boolean = false,
    val isFeatured: Boolean = false,
    val charmPoints: Long = 52000,
    val countryCode: String = "Global",
    val countryFlag: String = "🌐",
    val countryName: String = "Global",
    val announcement: String = "Welcome to our StarVibe VIP lounge! Keep it positive & respect the mic 👑",
    val micSeats: List<MicSeat> = emptyList()
)

data class GiftItem(
    val id: String,
    val name: String,
    val priceDiamonds: Int,
    val iconEmoji: String,
    val rarity: String, // Normal, Rare, Epic, Royal Mythic
    val color: Long = 0xFFFFD700
)

data class RoomChatMessage(
    val id: String,
    val senderName: String,
    val senderVip: Int,
    val message: String,
    val isSystem: Boolean = false,
    val giftSent: GiftItem? = null,
    val time: String = "Just now"
)

data class DiscoverMoment(
    val id: String,
    val author: User,
    val text: String,
    val audioTitle: String? = null,
    val timeAgo: String,
    val likesCount: Int,
    val commentsCount: Int,
    val tags: List<String>,
    val isLiked: Boolean = false
)

data class DirectMessage(
    val id: String,
    val isMe: Boolean,
    val text: String,
    val time: String
)

data class Conversation(
    val id: String,
    val user: User,
    val lastMessage: String,
    val time: String,
    val unreadCount: Int = 0,
    val isOfficial: Boolean = false
)

data class VipTier(
    val level: Int,
    val title: String,
    val expRequired: Int,
    val badgeColor: Long,
    val perks: List<String>
)

data class RankingUser(
    val rank: Int,
    val user: User,
    val score: Long,
    val title: String
)

data class CpPair(
    val rank: Int,
    val user1: User,
    val user2: User,
    val cpPoints: Long,
    val cpTitle: String,
    val cpDays: Int = 128
)

data class HallOfFameUser(
    val rank: Int,
    val user: User,
    val badgeTitle: String,
    val honoraryYear: String,
    val totalCharmWon: String
)

data class FamilyGroup(
    val id: String,
    val name: String,
    val tag: String,
    val level: Int,
    val membersCount: Int,
    val maxMembers: Int,
    val totalCharm: Long,
    val leaderName: String,
    val announcement: String
)

data class ShopItem(
    val id: String,
    val name: String,
    val category: String, // Frame, Entrance, Bubble, MicRing
    val priceDiamonds: Int,
    val iconEmoji: String,
    val isPurchased: Boolean = false,
    val isEquipped: Boolean = false,
    val description: String
)

data class WalletPackage(
    val id: String,
    val diamonds: Int,
    val bonusDiamonds: Int,
    val priceUsd: String,
    val isBestValue: Boolean = false
)
