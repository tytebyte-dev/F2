package app.f2.data

import android.content.Context
import android.content.SharedPreferences
import kotlin.random.Random

/**
 * Локальное хранилище и игровая логика (офлайн)
 */
class GameRepository(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("youtuber_sim", Context.MODE_PRIVATE)

    fun hasPlayer(): Boolean = prefs.contains("channel_name")

    fun loadPlayer(): Player {
        if (!hasPlayer()) return Player()
        return Player(
            channelName = prefs.getString("channel_name", "Мой канал") ?: "Мой канал",
            gender = prefs.getInt("gender", 1),
            dollars = prefs.getFloat("dollars", 50f).toDouble(),
            subs = prefs.getLong("subs", 0),
            views = prefs.getLong("views", 0),
            auditor = prefs.getLong("auditor", 0),
            computerId = prefs.getInt("computer_id", 1),
            cameraId = prefs.getInt("camera_id", 1),
            mikroId = prefs.getInt("mikro_id", 1),
            channelThemeId = prefs.getInt("theme_id", 0),
            lastVideoTime = prefs.getLong("last_video_time", 0),
            totalVideos = prefs.getInt("total_videos", 0),
            prankCount = prefs.getInt("prank_count", 0),
            clipCount = prefs.getInt("clip_count", 0),
            parodyCount = prefs.getInt("parody_count", 0),
            showCount = prefs.getInt("show_count", 0),
            gameCount = prefs.getInt("game_count", 0),
            otherCount = prefs.getInt("other_count", 0)
        )
    }

    fun savePlayer(p: Player) {
        prefs.edit().apply {
            putString("channel_name", p.channelName)
            putInt("gender", p.gender)
            putFloat("dollars", p.dollars.toFloat())
            putLong("subs", p.subs)
            putLong("views", p.views)
            putLong("auditor", p.auditor)
            putInt("computer_id", p.computerId)
            putInt("camera_id", p.cameraId)
            putInt("mikro_id", p.mikroId)
            putInt("theme_id", p.channelThemeId)
            putLong("last_video_time", p.lastVideoTime)
            putInt("total_videos", p.totalVideos)
            putInt("prank_count", p.prankCount)
            putInt("clip_count", p.clipCount)
            putInt("parody_count", p.parodyCount)
            putInt("show_count", p.showCount)
            putInt("game_count", p.gameCount)
            putInt("other_count", p.otherCount)
            apply()
        }
    }

    fun createPlayer(channelName: String, gender: Int): Player {
        val p = Player(channelName = channelName.trim().ifEmpty { "Мой канал" }, gender = gender)
        savePlayer(p)
        return p
    }

    /**
     * Запись видео — основная механика из cabinet.php
     * rand(1,150) + bonus_subs + mikro.bonus  → подписчики
     * rand(50,700) + bonus_view + camera.bonus → просмотры
     * rand(100,1500)/10 → деньги
     */
    data class VideoResult(
        val themeName: String,
        val newSubs: Int,
        val newViews: Int,
        val income: Double,
        val success: Boolean,
        val message: String
    )

    fun recordVideo(themeId: Int): VideoResult {
        val player = loadPlayer()
        if (!player.canRecordVideo()) {
            val left = player.remainingCooldown()
            return VideoResult("", 0, 0, 0.0, false,
                "Вы уже отсняли видео. Подождите ещё ${Player.formatTime(left)}")
        }

        val theme = GameCatalog.videoThemes.find { it.id == themeId }
            ?: GameCatalog.videoThemes[0]
        val chTheme = player.channelTheme()
        val cam = player.camera()
        val mic = player.mikro()

        val randSubs = Random.nextInt(1, 151)
        val randViews = Random.nextInt(50, 701)
        val randMoney = Random.nextInt(100, 1501) / 10.0

        val gainedSubs = randSubs + chTheme.bonusSubs + mic.bonus
        val gainedViews = randViews + chTheme.bonusViews + cam.bonus
        val income = randMoney

        player.subs += gainedSubs
        player.views += gainedViews
        player.dollars += income
        player.auditor = (player.subs * 0.7 + player.views * 0.01).toLong().coerceAtLeast(player.auditor)
        player.lastVideoTime = System.currentTimeMillis()
        player.totalVideos++

        when (themeId) {
            1 -> player.prankCount++
            2 -> player.clipCount++
            3 -> player.parodyCount++
            4 -> player.showCount++
            5 -> player.gameCount++
            else -> player.otherCount++
        }

        savePlayer(player)

        return VideoResult(
            themeName = theme.name,
            newSubs = gainedSubs,
            newViews = gainedViews,
            income = income,
            success = true,
            message = "Ролик «${theme.name}» успешно записан!\n" +
                    "На вас подписалось $gainedSubs, а ролик собрал почти $gainedViews просмотров.\n" +
                    "Дополнительный доход: $${"%.1f".format(income)}"
        )
    }

    fun buyComputer(id: Int): Pair<Boolean, String> {
        val player = loadPlayer()
        val item = GameCatalog.computers.find { it.id == id } ?: return false to "Товар не найден"
        if (player.computerId == id) return false to "Уже куплено"
        if (player.dollars < item.price) return false to "Недостаточно денег"
        player.dollars -= item.price
        player.computerId = id
        savePlayer(player)
        return true to "Покупка прошла успешно"
    }

    fun buyCamera(id: Int): Pair<Boolean, String> {
        val player = loadPlayer()
        val item = GameCatalog.cameras.find { it.id == id } ?: return false to "Товар не найден"
        if (player.cameraId == id) return false to "Уже куплено"
        if (player.dollars < item.price) return false to "Недостаточно денег"
        player.dollars -= item.price
        player.cameraId = id
        savePlayer(player)
        return true to "Покупка прошла успешно"
    }

    fun buyMikro(id: Int): Pair<Boolean, String> {
        val player = loadPlayer()
        val item = GameCatalog.microphones.find { it.id == id } ?: return false to "Товар не найден"
        if (player.mikroId == id) return false to "Уже куплено"
        if (player.dollars < item.price) return false to "Недостаточно денег"
        player.dollars -= item.price
        player.mikroId = id
        savePlayer(player)
        return true to "Покупка прошла успешно"
    }

    fun buyChannelTheme(id: Int): Pair<Boolean, String> {
        val player = loadPlayer()
        val item = GameCatalog.channelThemes.find { it.id == id } ?: return false to "Тема не найдена"
        if (player.channelThemeId == id) return false to "Уже выбрано"
        if (player.dollars < item.price) return false to "Недостаточно денег"
        player.dollars -= item.price
        player.channelThemeId = id
        savePlayer(player)
        return true to "Тема канала изменена"
    }

    fun renameChannel(newName: String): Pair<Boolean, String> {
        val name = newName.trim()
        if (name.length < 2) return false to "Слишком короткое название"
        if (name.length > 32) return false to "Слишком длинное название"
        val player = loadPlayer()
        val updated = player.copy(channelName = name)
        savePlayer(updated)
        return true to "Название изменено"
    }

    fun resetProgress() {
        prefs.edit().clear().apply()
    }
}
