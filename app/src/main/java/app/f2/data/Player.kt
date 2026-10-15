package app.f2.data

/**
 * Состояние игрока (локальное сохранение)
 */
data class Player(
    val channelName: String = "Мой канал",
    val gender: Int = 1,                 // 1 = муж, 0 = жен
    var dollars: Double = 50.0,          // стартовые деньги
    var subs: Long = 0,
    var views: Long = 0,
    var auditor: Long = 0,
    var computerId: Int = 1,
    var cameraId: Int = 1,
    var mikroId: Int = 1,
    var channelThemeId: Int = 0,
    var lastVideoTime: Long = 0L,        // System.currentTimeMillis()
    var totalVideos: Int = 0,
    var prankCount: Int = 0,
    var clipCount: Int = 0,
    var parodyCount: Int = 0,
    var showCount: Int = 0,
    var gameCount: Int = 0,
    var otherCount: Int = 0
) {
    fun computer(): Equipment =
        GameCatalog.computers.find { it.id == computerId } ?: GameCatalog.computers[0]

    fun camera(): Equipment =
        GameCatalog.cameras.find { it.id == cameraId } ?: GameCatalog.cameras[0]

    fun mikro(): Equipment =
        GameCatalog.microphones.find { it.id == mikroId } ?: GameCatalog.microphones[0]

    fun channelTheme(): ChannelTheme =
        GameCatalog.channelThemes.find { it.id == channelThemeId } ?: GameCatalog.channelThemes[0]

    /** Базовый кулдаун 900 сек минус бонус компьютера */
    fun cooldownSeconds(): Int {
        val base = 900
        val bonus = computer().bonus
        return (base - bonus).coerceAtLeast(60)
    }

    fun canRecordVideo(now: Long = System.currentTimeMillis()): Boolean {
        if (lastVideoTime == 0L) return true
        val elapsed = (now - lastVideoTime) / 1000
        return elapsed >= cooldownSeconds()
    }

    fun remainingCooldown(now: Long = System.currentTimeMillis()): Long {
        if (lastVideoTime == 0L) return 0
        val elapsed = (now - lastVideoTime) / 1000
        val left = cooldownSeconds() - elapsed
        return left.coerceAtLeast(0)
    }

    fun formatMoney(): String = "$%.1f".format(dollars)

    fun formatSubs(): String = formatNumber(subs)
    fun formatViews(): String = formatNumber(views)
    fun formatAuditor(): String = formatNumber(auditor)

    companion object {
        fun formatNumber(n: Long): String {
            return when {
                n >= 1_000_000_000 -> "%.1f млрд".format(n / 1_000_000_000.0)
                n >= 1_000_000 -> "%.1f млн.".format(n / 1_000_000.0)
                n >= 1_000 -> "%.1f тыс.".format(n / 1_000.0)
                else -> n.toString()
            }
        }

        fun formatTime(seconds: Long): String {
            if (seconds <= 0) return "0 сек."
            val h = seconds / 3600
            val m = (seconds % 3600) / 60
            val s = seconds % 60
            return buildString {
                if (h > 0) append("$h ч. ")
                if (m > 0) append("$m мин. ")
                if (s > 0 || (h == 0L && m == 0L)) append("$s сек.")
            }.trim()
        }
    }
}
