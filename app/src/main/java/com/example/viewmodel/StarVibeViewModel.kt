package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.StarVibeRepository
import com.example.model.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

enum class MainTab {
    PARTY, DISCOVER, MESSAGE, MY
}

enum class SubScreen {
    NONE, VOICE_ROOM, WALLET, VIP, RANKING, FAMILY, SHOP, GAME_CENTER, SETTINGS, CHAT_DETAIL, HALL_OF_FAME, PRIZE_POOL, LUDO_GAME
}

data class StarVibeUiState(
    val currentTab: MainTab = MainTab.PARTY,
    val subScreen: SubScreen = SubScreen.NONE,
    val topNavSelection: String = "Party", // Mine, Party, Game, Search
    val selectedCategory: String = "All",
    val selectedCountry: String = "Global", // Global, USA, UK, Bangladesh, India, UAE, Philippines, Saudi Arabia
    val isSearchExpanded: Boolean = false,
    val searchQuery: String = "",
    val rooms: List<PartyRoom> = emptyList(),
    val activeRoom: PartyRoom? = null,
    val roomMessages: List<RoomChatMessage> = emptyList(),
    val isMicActive: Boolean = false,
    val isMuted: Boolean = false,
    val mySeatIndex: Int? = null,
    val activeGiftAnimation: GiftItem? = null,
    val showGiftSheet: Boolean = false,
    val user: User = StarVibeRepository.currentUser,
    val diamonds: Int = 28450,
    val coins: Long = 142000L,
    val vipLevel: Int = 6,
    val vipExp: Int = 48500,
    val rankingTab: String = "Wealth", // Wealth, Charm, Room, CP
    val rankingPeriod: String = "Today", // Today, Week, Month
    val shopItems: List<ShopItem> = StarVibeRepository.shopItems,
    val shopCategory: String = "Frame", // Frame, Entrance, Bubble, MicRing
    val discoverMoments: List<DiscoverMoment> = StarVibeRepository.discoverMoments,
    val conversations: List<Conversation> = StarVibeRepository.conversations,
    val activeConversation: Conversation? = null,
    val directMessages: List<DirectMessage> = emptyList(),
    val wheelSpinAngle: Float = 0f,
    val isWheelSpinning: Boolean = false,
    val wheelWonPrize: String? = null,
    val diceValues: Pair<Int, Int>? = null,
    val isRollingDice: Boolean = false,
    val toastMessage: String? = null,
    val showCreateRoomDialog: Boolean = false
)

class StarVibeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(StarVibeUiState())
    val uiState: StateFlow<StarVibeUiState> = _uiState.asStateFlow()

    init {
        _uiState.update { it.copy(rooms = StarVibeRepository.getInitialPartyRooms()) }
    }

    fun setTab(tab: MainTab) {
        _uiState.update { it.copy(currentTab = tab, subScreen = SubScreen.NONE) }
    }

    fun setTopNav(selection: String) {
        when (selection) {
            "Mine" -> {
                _uiState.update { it.copy(topNavSelection = "Mine", currentTab = MainTab.PARTY, subScreen = SubScreen.NONE) }
                showToast("Viewing Mine: Your Rooms & Following 👑")
            }
            "Party" -> {
                _uiState.update { it.copy(topNavSelection = "Party", currentTab = MainTab.PARTY, subScreen = SubScreen.NONE) }
            }
            "Game" -> {
                _uiState.update { it.copy(topNavSelection = "Game", subScreen = SubScreen.GAME_CENTER) }
            }
            "Search" -> {
                _uiState.update { it.copy(isSearchExpanded = !it.isSearchExpanded) }
            }
        }
    }

    fun setCountryFilter(country: String) {
        _uiState.update { it.copy(selectedCountry = country) }
    }

    fun openHallOfFame() {
        _uiState.update { it.copy(subScreen = SubScreen.HALL_OF_FAME) }
    }

    fun openPrizePool() {
        _uiState.update { it.copy(subScreen = SubScreen.PRIZE_POOL) }
    }

    fun openLudoGame() {
        _uiState.update { it.copy(subScreen = SubScreen.LUDO_GAME) }
    }

    fun openCpRanking() {
        _uiState.update { it.copy(subScreen = SubScreen.RANKING, rankingTab = "CP") }
    }

    fun openGiftRanking() {
        _uiState.update { it.copy(subScreen = SubScreen.RANKING, rankingTab = "Charm") }
    }

    fun openSubScreen(screen: SubScreen) {
        _uiState.update { it.copy(subScreen = screen) }
    }

    fun closeSubScreen() {
        _uiState.update { it.copy(subScreen = SubScreen.NONE) }
    }

    fun selectCategory(category: String) {
        _uiState.update { it.copy(selectedCategory = category) }
    }

    fun updateSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun enterVoiceRoom(room: PartyRoom) {
        val initialMessages = listOf(
            RoomChatMessage("m1", "StarVibe Official", 9, "👑 Welcome to ${room.title}! Connect • Chat • Party • Shine!", isSystem = true),
            RoomChatMessage("m2", room.host.name, room.host.vipLevel, "Welcome everyone to our live room! Take a mic or enjoy the music 🎶"),
            RoomChatMessage("m3", "StarVibe Bot", 9, "✨ VIP 6 Royal Alex entered riding Golden Dragon!", isSystem = true)
        )
        _uiState.update {
            it.copy(
                activeRoom = room,
                subScreen = SubScreen.VOICE_ROOM,
                roomMessages = initialMessages,
                isMicActive = false,
                isMuted = false,
                mySeatIndex = null
            )
        }
    }

    fun exitVoiceRoom() {
        _uiState.update {
            it.copy(
                activeRoom = null,
                subScreen = SubScreen.NONE,
                isMicActive = false,
                mySeatIndex = null
            )
        }
    }

    fun takeOrLeaveMicSeat(seatIndex: Int) {
        val current = _uiState.value
        val room = current.activeRoom ?: return

        if (current.mySeatIndex == seatIndex) {
            // Leave seat
            val updatedSeats = room.micSeats.map {
                if (it.seatIndex == seatIndex) it.copy(user = null, isSpeaking = false) else it
            }
            _uiState.update {
                it.copy(
                    mySeatIndex = null,
                    isMicActive = false,
                    activeRoom = room.copy(micSeats = updatedSeats)
                )
            }
            showToast("Left Mic seat ${seatIndex + 1}")
        } else {
            // Take seat if empty
            val targetSeat = room.micSeats.find { it.seatIndex == seatIndex }
            if (targetSeat?.user == null) {
                val updatedSeats = room.micSeats.map {
                    if (it.seatIndex == seatIndex) it.copy(user = current.user, isSpeaking = !current.isMuted)
                    else if (it.seatIndex == current.mySeatIndex) it.copy(user = null, isSpeaking = false)
                    else it
                }
                _uiState.update {
                    it.copy(
                        mySeatIndex = seatIndex,
                        isMicActive = true,
                        activeRoom = room.copy(micSeats = updatedSeats)
                    )
                }
                showToast("You are now on Mic seat ${seatIndex + 1} 🎙️")
            } else {
                showToast("Seat ${seatIndex + 1} is occupied by ${targetSeat.user.name}")
            }
        }
    }

    fun toggleMicMute() {
        val current = _uiState.value
        val newMute = !current.isMuted
        val seatIdx = current.mySeatIndex
        val room = current.activeRoom

        val updatedSeats = room?.micSeats?.map {
            if (it.seatIndex == seatIdx) it.copy(isMuted = newMute, isSpeaking = !newMute) else it
        } ?: emptyList()

        _uiState.update {
            it.copy(
                isMuted = newMute,
                activeRoom = room?.copy(micSeats = updatedSeats)
            )
        }
        showToast(if (newMute) "Microphone Muted 🔇" else "Microphone Live 🎙️")
    }

    fun sendRoomChatMessage(text: String) {
        if (text.isBlank()) return
        val user = _uiState.value.user
        val newMsg = RoomChatMessage(
            id = "msg_${System.currentTimeMillis()}",
            senderName = user.name,
            senderVip = user.vipLevel,
            message = text
        )
        _uiState.update {
            it.copy(roomMessages = it.roomMessages + newMsg)
        }
    }

    fun triggerSoundEffect(name: String) {
        val user = _uiState.value.user
        val effectMsg = RoomChatMessage(
            id = "fx_${System.currentTimeMillis()}",
            senderName = user.name,
            senderVip = user.vipLevel,
            message = "played sound effect: $name 🔊",
            isSystem = true
        )
        _uiState.update {
            it.copy(roomMessages = it.roomMessages + effectMsg)
        }
        showToast("Triggered $name! 🎵")
    }

    fun toggleGiftSheet(show: Boolean) {
        _uiState.update { it.copy(showGiftSheet = show) }
    }

    fun sendGift(gift: GiftItem) {
        val current = _uiState.value
        if (current.diamonds < gift.priceDiamonds) {
            showToast("Not enough Diamonds! Please top up in Wallet 💎")
            return
        }

        val newDiamonds = current.diamonds - gift.priceDiamonds
        val addedCharm = gift.priceDiamonds * 10L
        val room = current.activeRoom

        val giftMessage = RoomChatMessage(
            id = "gift_${System.currentTimeMillis()}",
            senderName = current.user.name,
            senderVip = current.user.vipLevel,
            message = "sent ${gift.name} ${gift.iconEmoji} to the room!",
            giftSent = gift
        )

        _uiState.update {
            it.copy(
                diamonds = newDiamonds,
                activeGiftAnimation = gift,
                showGiftSheet = false,
                roomMessages = it.roomMessages + giftMessage,
                activeRoom = room?.copy(charmPoints = (room.charmPoints + addedCharm))
            )
        }

        viewModelScope.launch {
            delay(3500)
            _uiState.update { it.copy(activeGiftAnimation = null) }
        }
    }

    fun topUpDiamonds(pack: WalletPackage) {
        val totalAdded = pack.diamonds + pack.bonusDiamonds
        _uiState.update {
            it.copy(
                diamonds = it.diamonds + totalAdded,
                vipExp = it.vipExp + totalAdded
            )
        }
        showToast("Top-up successful! +$totalAdded Diamonds added 💎")
    }

    fun purchaseShopItem(item: ShopItem) {
        val current = _uiState.value
        if (item.isPurchased) {
            // Toggle equip
            val updated = current.shopItems.map {
                if (it.id == item.id) it.copy(isEquipped = !it.isEquipped)
                else if (it.category == item.category) it.copy(isEquipped = false)
                else it
            }
            _uiState.update { it.copy(shopItems = updated) }
            showToast(if (!item.isEquipped) "Equipped ${item.name} ✨" else "Unequipped ${item.name}")
            return
        }

        if (current.diamonds < item.priceDiamonds) {
            showToast("Need ${item.priceDiamonds} Diamonds to buy! 💎")
            return
        }

        val updated = current.shopItems.map {
            if (it.id == item.id) it.copy(isPurchased = true, isEquipped = true)
            else if (it.category == item.category) it.copy(isEquipped = false)
            else it
        }

        _uiState.update {
            it.copy(
                diamonds = it.diamonds - item.priceDiamonds,
                shopItems = updated
            )
        }
        showToast("Purchased and equipped ${item.name}! 👑")
    }

    fun setShopCategory(cat: String) {
        _uiState.update { it.copy(shopCategory = cat) }
    }

    fun setRankingTab(tab: String) {
        _uiState.update { it.copy(rankingTab = tab) }
    }

    fun setRankingPeriod(period: String) {
        _uiState.update { it.copy(rankingPeriod = period) }
    }

    fun spinLuckyWheel() {
        if (_uiState.value.isWheelSpinning) return
        val cost = 20
        if (_uiState.value.diamonds < cost) {
            showToast("Need 20 Diamonds to spin Lucky Star Wheel! 💎")
            return
        }

        _uiState.update {
            it.copy(
                diamonds = it.diamonds - cost,
                isWheelSpinning = true,
                wheelWonPrize = null
            )
        }

        viewModelScope.launch {
            val randomDegrees = 360f * 5 + Random.nextInt(0, 360)
            _uiState.update { it.copy(wheelSpinAngle = it.wheelSpinAngle + randomDegrees) }
            delay(2800)

            val prizes = listOf(
                "👑 500 Diamonds Jackpot!",
                "🌹 50 Star Roses",
                "💎 100 Diamonds",
                "⭐ 7-Day VIP 1 Pass",
                "🦄 Star Pegasus Entrance Trial",
                "🎙️ Golden Mic Gift"
            )
            val won = prizes.random()

            val bonus = if (won.contains("500 Diamonds")) 500 else if (won.contains("100 Diamonds")) 100 else 0
            _uiState.update {
                it.copy(
                    isWheelSpinning = false,
                    wheelWonPrize = won,
                    diamonds = it.diamonds + bonus
                )
            }
        }
    }

    fun rollDice() {
        if (_uiState.value.isRollingDice) return
        _uiState.update { it.copy(isRollingDice = true) }
        viewModelScope.launch {
            delay(800)
            val d1 = Random.nextInt(1, 7)
            val d2 = Random.nextInt(1, 7)
            val coinsWon = (d1 + d2) * 100L
            _uiState.update {
                it.copy(
                    isRollingDice = false,
                    diceValues = Pair(d1, d2),
                    coins = it.coins + coinsWon
                )
            }
            showToast("Rolled $d1 & $d2! Total: ${d1 + d2} 🎲 Won +$coinsWon Star Coins!")
        }
    }

    fun toggleLikeMoment(momentId: String) {
        _uiState.update { state ->
            val updated = state.discoverMoments.map { m ->
                if (m.id == momentId) {
                    val newLiked = !m.isLiked
                    m.copy(
                        isLiked = newLiked,
                        likesCount = if (newLiked) m.likesCount + 1 else m.likesCount - 1
                    )
                } else m
            }
            state.copy(discoverMoments = updated)
        }
    }

    fun openConversation(conv: Conversation) {
        val initialDms = if (conv.isOfficial) {
            listOf(
                DirectMessage("d1", false, "Welcome to StarVibe - Connect • Chat • Party • Shine!", "10:00 AM"),
                DirectMessage("d2", false, "👑 Enjoy premium voice rooms, VIP badges, and exciting live parties!", "10:01 AM")
            )
        } else {
            listOf(
                DirectMessage("d1", false, "Hey Alex, are you joining the gala tonight?", "11:20 AM"),
                DirectMessage("d2", true, "Yes! I'll be in room #1 soon. See you there!", "11:25 AM"),
                DirectMessage("d3", false, conv.lastMessage, conv.time)
            )
        }

        _uiState.update {
            it.copy(
                activeConversation = conv,
                subScreen = SubScreen.CHAT_DETAIL,
                directMessages = initialDms
            )
        }
    }

    fun sendDirectMessage(text: String) {
        if (text.isBlank()) return
        val newDm = DirectMessage(
            id = "dm_${System.currentTimeMillis()}",
            isMe = true,
            text = text,
            time = "Just now"
        )
        _uiState.update {
            it.copy(directMessages = it.directMessages + newDm)
        }
    }

    fun toggleCreateRoomDialog(show: Boolean) {
        _uiState.update { it.copy(showCreateRoomDialog = show) }
    }

    fun createRoom(title: String, category: String, tag: String) {
        val currentUser = _uiState.value.user
        val newRoom = PartyRoom(
            id = "custom_${System.currentTimeMillis()}",
            title = if (title.isBlank()) "👑 ${currentUser.name}'s VIP Party" else title,
            host = currentUser,
            category = category,
            tag = if (tag.startsWith("#")) tag else "#$tag",
            onlineCount = 1,
            isHot = true,
            charmPoints = 5000,
            micSeats = listOf(
                MicSeat(0, currentUser, isMuted = false, isSpeaking = true),
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
        _uiState.update {
            it.copy(
                rooms = listOf(newRoom) + it.rooms,
                showCreateRoomDialog = false
            )
        }
        enterVoiceRoom(newRoom)
        showToast("Created Party Room: ${newRoom.title} 🎉")
    }

    fun showToast(msg: String) {
        _uiState.update { it.copy(toastMessage = msg) }
        viewModelScope.launch {
            delay(2500)
            _uiState.update { if (it.toastMessage == msg) it.copy(toastMessage = null) else it }
        }
    }
}
