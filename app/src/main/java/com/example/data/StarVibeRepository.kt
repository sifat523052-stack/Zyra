package com.example.data

import com.example.model.*

object StarVibeRepository {

    val currentUser = User(
        id = "u_me",
        name = "Royal Alex",
        starVibeId = "SV-778899",
        avatarEmoji = "👑",
        avatarBgColor = 0xFF00BFA5,
        vipLevel = 6,
        level = 45,
        wealthLevel = 38,
        charmLevel = 42,
        followingCount = 128,
        followersCount = 3840,
        visitorsCount = 12900,
        bio = "Connect • Chat • Party • Shine ✨ StarVibe VIP Noble",
        isOnline = true
    )

    val gifts = listOf(
        GiftItem("g1", "Star Castle", 9999, "🏰", "Royal Mythic", 0xFFFFD700),
        GiftItem("g2", "Royal Crown", 5200, "👑", "Royal Mythic", 0xFFFFC107),
        GiftItem("g3", "Diamond Yacht", 3000, "🛥️", "Epic", 0xFF00E5FF),
        GiftItem("g4", "Galaxy Comet", 1200, "☄️", "Epic", 0xFF7C4DFF),
        GiftItem("g5", "Emerald Ring", 520, "💍", "Rare", 0xFF00E676),
        GiftItem("g6", "Golden Mic", 99, "🎙️", "Rare", 0xFFFFD54F),
        GiftItem("g7", "Star Rose", 10, "🌹", "Normal", 0xFFFF4081),
        GiftItem("g8", "Lucky Vibe", 1, "⭐", "Normal", 0xFF64FFDA)
    )

    val sampleUsers = listOf(
        User("u1", "Queen Sophia", "SV-100201", "👸", 0xFF8E24AA, vipLevel = 8, level = 58, wealthLevel = 65, charmLevel = 70),
        User("u2", "Star DJ Leo", "SV-204192", "🎧", 0xFF00ACC1, vipLevel = 5, level = 39, wealthLevel = 34, charmLevel = 45),
        User("u3", "Melody Luna", "SV-310482", "🎤", 0xFFD81B60, vipLevel = 7, level = 50, wealthLevel = 48, charmLevel = 60),
        User("u4", "Prince Marcus", "SV-119283", "🤴", 0xFFFFA000, vipLevel = 9, level = 72, wealthLevel = 88, charmLevel = 85),
        User("u5", "Nova Blaze", "SV-440291", "🔥", 0xFFE53935, vipLevel = 4, level = 28, wealthLevel = 22, charmLevel = 30),
        User("u6", "Cupid Chloe", "SV-551092", "💖", 0xFFFF4081, vipLevel = 6, level = 41, wealthLevel = 35, charmLevel = 52)
    )

    const val prizePoolDiamonds: Long = 1850420L

    fun getInitialPartyRooms(): List<PartyRoom> {
        val u = sampleUsers
        return listOf(
            PartyRoom(
                id = "room_1",
                title = "👑 VIP Golden Night Lounge",
                host = u[0],
                category = "Music",
                tag = "#RoyalMusic",
                onlineCount = 1240,
                isHot = true,
                isFeatured = true,
                countryCode = "USA",
                countryFlag = "🇺🇸",
                countryName = "USA",
                charmPoints = 184500,
                announcement = "Tonight's StarVibe Gala! Send Star Castle to trigger room shower! 🌟",
                micSeats = listOf(
                    MicSeat(0, u[0], isMuted = false, isSpeaking = true),
                    MicSeat(1, u[1], isMuted = false, isSpeaking = false),
                    MicSeat(2, u[2], isMuted = false, isSpeaking = true),
                    MicSeat(3, u[3], isMuted = false, isSpeaking = false),
                    MicSeat(4, u[4], isMuted = false, isSpeaking = false),
                    MicSeat(5, u[5], isMuted = false, isSpeaking = false),
                    MicSeat(6, null),
                    MicSeat(7, null),
                    MicSeat(8, null),
                    MicSeat(9, null)
                )
            ),
            PartyRoom(
                id = "room_2",
                title = "✨ Late Night Acoustic & Chill",
                host = u[1],
                category = "Chill",
                tag = "#LateNightVibes",
                onlineCount = 890,
                isHot = true,
                isFeatured = true,
                countryCode = "UK",
                countryFlag = "🇬🇧",
                countryName = "UK",
                charmPoints = 92000,
                micSeats = listOf(
                    MicSeat(0, u[1], isMuted = false, isSpeaking = true),
                    MicSeat(1, u[4], isMuted = false, isSpeaking = false),
                    MicSeat(2, u[2], isMuted = false, isSpeaking = false),
                    MicSeat(3, null),
                    MicSeat(4, null),
                    MicSeat(5, null),
                    MicSeat(6, null),
                    MicSeat(7, null),
                    MicSeat(8, null),
                    MicSeat(9, null)
                )
            ),
            PartyRoom(
                id = "room_3",
                title = "🎤 Star Karaoke Galaxy Finals",
                host = u[2],
                category = "Karaoke",
                tag = "#LiveSinging",
                onlineCount = 2650,
                isHot = true,
                isFeatured = true,
                countryCode = "Bangladesh",
                countryFlag = "🇧🇩",
                countryName = "Bangladesh",
                charmPoints = 320000,
                micSeats = listOf(
                    MicSeat(0, u[2], isMuted = false, isSpeaking = true),
                    MicSeat(1, u[5], isMuted = false, isSpeaking = false),
                    MicSeat(2, u[3], isMuted = false, isSpeaking = false),
                    MicSeat(3, u[0], isMuted = false, isSpeaking = false),
                    MicSeat(4, null),
                    MicSeat(5, null),
                    MicSeat(6, null),
                    MicSeat(7, null),
                    MicSeat(8, null),
                    MicSeat(9, null)
                )
            ),
            PartyRoom(
                id = "room_4",
                title = "💎 Royal High Rollers Room",
                host = u[3],
                category = "Popular",
                tag = "#VIPOnly",
                onlineCount = 3120,
                isHot = true,
                isFeatured = true,
                countryCode = "UAE",
                countryFlag = "🇦🇪",
                countryName = "UAE",
                charmPoints = 780000,
                micSeats = listOf(
                    MicSeat(0, u[3], isMuted = false, isSpeaking = true),
                    MicSeat(1, u[0], isMuted = false, isSpeaking = false),
                    MicSeat(2, u[1], isMuted = false, isSpeaking = false),
                    MicSeat(3, null),
                    MicSeat(4, null),
                    MicSeat(5, null),
                    MicSeat(6, null),
                    MicSeat(7, null),
                    MicSeat(8, null),
                    MicSeat(9, null)
                )
            ),
            PartyRoom(
                id = "room_5",
                title = "🔥 Cyber Party & Gaming Talk",
                host = u[4],
                category = "Gaming",
                tag = "#SquadUp",
                onlineCount = 740,
                isHot = false,
                isFeatured = false,
                countryCode = "India",
                countryFlag = "🇮🇳",
                countryName = "India",
                charmPoints = 45000,
                micSeats = listOf(
                    MicSeat(0, u[4], isMuted = false, isSpeaking = true),
                    MicSeat(1, null),
                    MicSeat(2, null),
                    MicSeat(3, null),
                    MicSeat(4, null),
                    MicSeat(5, null),
                    MicSeat(6, null),
                    MicSeat(7, null),
                    MicSeat(8, null),
                    MicSeat(9, null)
                )
            ),
            PartyRoom(
                id = "room_6",
                title = "💫 Heart to Heart Dating Lounge",
                host = u[5],
                category = "Dating",
                tag = "#CPMatch",
                onlineCount = 980,
                isHot = false,
                isFeatured = false,
                countryCode = "Saudi Arabia",
                countryFlag = "🇸🇦",
                countryName = "Saudi Arabia",
                charmPoints = 88000,
                micSeats = listOf(
                    MicSeat(0, u[5], isMuted = false, isSpeaking = true),
                    MicSeat(1, null),
                    MicSeat(2, null),
                    MicSeat(3, null),
                    MicSeat(4, null),
                    MicSeat(5, null),
                    MicSeat(6, null),
                    MicSeat(7, null),
                    MicSeat(8, null),
                    MicSeat(9, null)
                )
            ),
            PartyRoom(
                id = "room_7",
                title = "🌴 Manila Tropical Acoustic Beats",
                host = u[1],
                category = "Music",
                tag = "#PinoyParty",
                onlineCount = 1120,
                isHot = true,
                isFeatured = false,
                countryCode = "Philippines",
                countryFlag = "🇵🇭",
                countryName = "Philippines",
                charmPoints = 125000,
                micSeats = listOf(
                    MicSeat(0, u[1], isMuted = false, isSpeaking = true),
                    MicSeat(1, null),
                    MicSeat(2, null),
                    MicSeat(3, null),
                    MicSeat(4, null),
                    MicSeat(5, null),
                    MicSeat(6, null),
                    MicSeat(7, null),
                    MicSeat(8, null),
                    MicSeat(9, null)
                )
            )
        )
    }

    val vipTiers = listOf(
        VipTier(1, "VIP 1 Knight", 500, 0xFF4CAF50, listOf("Bronze Star Badge", "Custom Chat Bubble", "Room Entrance Notification")),
        VipTier(2, "VIP 2 Baron", 1500, 0xFF00BCD4, listOf("Silver Mic Ring", "Exclusive Star Emojis", "5% Bonus Exp")),
        VipTier(3, "VIP 3 Viscount", 4000, 0xFF009688, listOf("Emerald Profile Frame", "Voice Room Priority", "Kick Immunity Lv1")),
        VipTier(4, "VIP 4 Count", 10000, 0xFF3F51B5, listOf("Golden Dragon Entrance", "Animated Room Banner", "10% Top-up Bonus")),
        VipTier(5, "VIP 5 Marquess", 25000, 0xFF9C27B0, listOf("Royal Velvet Chat Bubble", "Custom Room Background", "Mic Ring Pulse")),
        VipTier(6, "VIP 6 Royal Noble", 60000, 0xFFFFB300, listOf("Golden Crown Avatar Frame", "Star Castle Gift Unlock", "Invisible Room Entry Toggle")),
        VipTier(7, "VIP 7 Sovereign Archduke", 150000, 0xFFFF7043, listOf("Mythic Phoenix Entrance", "Personal VIP Manager", "Global Banner Broadcast")),
        VipTier(8, "VIP 8 Grand Monarch", 400000, 0xFFE91E63, listOf("Diamond Aura Frame", "Permanent Custom Room ID", "Top Leaderboard Ribbon")),
        VipTier(9, "VIP 9 Royal Emperor", 1000000, 0xFFFFD700, listOf("Golden Star Sovereign Halo", "Exclusive 3D Dragon Gift", "Full Platform Supreme Status"))
    )

    val rankingWealth = listOf(
        RankingUser(1, sampleUsers[3], 9540000, "👑 Supreme Emperor"),
        RankingUser(2, sampleUsers[0], 7820000, "💎 Golden Queen"),
        RankingUser(3, currentUser, 3420000, "⭐ Royal Noble"),
        RankingUser(4, sampleUsers[2], 2100000, "🎙️ Melody Star"),
        RankingUser(5, sampleUsers[1], 1540000, "🎧 Beat Master"),
        RankingUser(6, sampleUsers[5], 980000, "💖 Charm Angel")
    )

    val rankingCharm = listOf(
        RankingUser(1, sampleUsers[0], 8920000, "🌹 Goddess of Charm"),
        RankingUser(2, sampleUsers[2], 7450000, "🎤 Star Nightingale"),
        RankingUser(3, sampleUsers[5], 6120000, "💫 Sweetheart Icon"),
        RankingUser(4, sampleUsers[3], 4980000, "👑 Golden Prince"),
        RankingUser(5, currentUser, 3820000, "✨ Rising Star"),
        RankingUser(6, sampleUsers[1], 2150000, "🎧 Party Host")
    )

    val cpRanking = listOf(
        CpPair(1, sampleUsers[0], sampleUsers[3], 12840000, "💍 Eternal Emperors", 365),
        CpPair(2, sampleUsers[5], sampleUsers[1], 8420000, "💖 Star Soulmates", 210),
        CpPair(3, sampleUsers[2], currentUser, 5910000, "✨ Cosmic Harmony", 145)
    )

    val hallOfFame = listOf(
        HallOfFameUser(1, sampleUsers[3], "Supreme Emperor", "2026 Season 1", "45.2M 🌟"),
        HallOfFameUser(2, sampleUsers[0], "Queen of Radiance", "2025 All-Star", "38.6M 🌟"),
        HallOfFameUser(3, sampleUsers[2], "Galaxy Melody Legend", "2025 Gala", "29.4M 🌟")
    )

    val families = listOf(
        FamilyGroup("f1", "Royal Dynasty", "DYNASTY", 9, 98, 100, 15400000, "Prince Marcus", "The premier elite club of StarVibe! Daily wars & VIP rooms 👑"),
        FamilyGroup("f2", "Emerald Star Clan", "EMERALD", 8, 85, 100, 11200000, "Queen Sophia", "Home of top singers and party lovers. Shine bright together ✨"),
        FamilyGroup("f3", "Golden Phoenix Guild", "PHOENIX", 7, 72, 80, 8900000, "Star DJ Leo", "Beats, live concerts, and party vibes 24/7 🔥")
    )

    val shopItems = listOf(
        ShopItem("s1", "Emerald Crown Frame", "Frame", 1500, "👑", isPurchased = true, isEquipped = true, "Gleaming emerald & royal gold avatar frame with star shimmer"),
        ShopItem("s2", "Galactic Nebula Frame", "Frame", 2200, "🌌", isPurchased = false, isEquipped = false, "Cosmic violet and cyan rotating nebula ring"),
        ShopItem("s3", "Golden Dragon Entrance", "Entrance", 5000, "🐉", isPurchased = true, isEquipped = true, "Fly into voice rooms riding a fiery golden imperial dragon!"),
        ShopItem("s4", "Star Pegasus Entrance", "Entrance", 4000, "🦄", isPurchased = false, isEquipped = false, "Majestic glowing wings and stardust footsteps"),
        ShopItem("s5", "Emerald Shimmer Bubble", "Bubble", 800, "💬", isPurchased = true, isEquipped = true, "Luminous emerald gradient chat speech bubble"),
        ShopItem("s6", "Royal Gold Velvet Bubble", "Bubble", 1200, "✨", isPurchased = false, isEquipped = false, "Deep gold trimmed bubble with crown watermark"),
        ShopItem("s7", "Diamond Pulse Mic Ring", "MicRing", 2500, "💎", isPurchased = false, isEquipped = false, "Pulsing cyan diamond halo when you speak on mic"),
        ShopItem("s8", "Golden Starburst Mic Ring", "MicRing", 1800, "⭐", isPurchased = true, isEquipped = true, "Golden star sparks flare up to your voice beats")
    )

    val walletPackages = listOf(
        WalletPackage("p1", 100, 20, "$0.99"),
        WalletPackage("p2", 550, 110, "$4.99"),
        WalletPackage("p3", 1200, 300, "$9.99", isBestValue = true),
        WalletPackage("p4", 3800, 1000, "$29.99"),
        WalletPackage("p5", 6500, 2000, "$49.99"),
        WalletPackage("p6", 15000, 5000, "$99.99")
    )

    val discoverMoments = listOf(
        DiscoverMoment(
            id = "m1",
            author = sampleUsers[0],
            text = "What an unforgettable party tonight at StarVibe Galaxy Lounge! Thank you all for the love and Star Castles! 🏰✨ #StarVibeGala #RoyalNight",
            audioTitle = "Live Solo - Ocean of Stars (38s)",
            timeAgo = "15m ago",
            likesCount = 342,
            commentsCount = 48,
            tags = listOf("StarVibeGala", "LiveParty", "VIP"),
            isLiked = true
        ),
        DiscoverMoment(
            id = "m2",
            author = sampleUsers[2],
            text = "Late night karaoke session is on! Join room #3 for the grand acoustic duet 🎤🎶 Let's shine together!",
            audioTitle = "Acoustic Duet Preview (45s)",
            timeAgo = "1h ago",
            likesCount = 189,
            commentsCount = 22,
            tags = listOf("KaraokeStar", "SingWithMe")
        ),
        DiscoverMoment(
            id = "m3",
            author = sampleUsers[1],
            text = "Fresh cyber electronic mix dropped! Catch me live on the turntable later at 11 PM UTC! 🔥🎧",
            audioTitle = "Cyber Vibe Club Mix (60s)",
            timeAgo = "3h ago",
            likesCount = 512,
            commentsCount = 67,
            tags = listOf("CyberBeats", "NightOwls", "DJLife")
        )
    )

    val conversations = listOf(
        Conversation("c_official", User("sys", "StarVibe Official", "SV-OFFICIAL", "🌟", 0xFFFFD700, vipLevel = 9), "👑 Welcome to StarVibe 2.4! Connect • Chat • Party • Shine!", "10:30 AM", unreadCount = 1, isOfficial = true),
        Conversation("c1", sampleUsers[0], "Hey Alex! Loved your speech on the mic earlier 👑", "11:45 AM", unreadCount = 2),
        Conversation("c2", sampleUsers[2], "Are you joining the karaoke finals tonight?", "Yesterday"),
        Conversation("c3", sampleUsers[3], "The Family War starts in 2 hours, be ready!", "2d ago")
    )
}
