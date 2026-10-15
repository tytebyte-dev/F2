package app.f2.data

/**
 * Статичные данные игры (оборудование, темы видео, достижения)
 * Перенесены из веб-версии «Симулятор жизни Ютубера»
 */

data class Equipment(
    val id: Int,
    val name: String,
    val price: Double,
    val bonus: Int,          // секунды для ПК, подписчики для микро, просмотры для камеры
    val imageRes: String     // имя drawable без расширения
)

data class VideoTheme(
    val id: Int,
    val name: String
)

data class Achievement(
    val id: String,
    val title: String,
    val requiredSubs: Long,
    val imageRes: String
)

object GameCatalog {

    val computers = listOf(
        Equipment(1, "Старый ПК", 0.0, 0, "artline"),
        Equipment(2, "Lenovo IdeaCentre", 150.0, 20, "lenovo"),
        Equipment(3, "ASRock DeskMini", 350.0, 40, "asrock"),
        Equipment(4, "Apple Mac Mini", 800.0, 50, "apple"),
        Equipment(5, "Artline Gaming", 1200.0, 60, "artline")
    )

    val cameras = listOf(
        Equipment(1, "Веб-камера", 0.0, 0, "ulanzi"),
        Equipment(2, "Ulanzi Webcam", 80.0, 50, "ulanzi"),
        Equipment(3, "JVC Compact", 200.0, 120, "jvc"),
        Equipment(4, "Action Camera", 350.0, 200, "actioncamera"),
        Equipment(5, "SportsCam Pro", 600.0, 350, "sportscam")
    )

    val microphones = listOf(
        Equipment(1, "Встроенный микрофон", 0.0, 0, "mic_generic"),
        Equipment(2, "Esperanza Mic", 40.0, 15, "esperanza"),
        Equipment(3, "Real-El Studio", 120.0, 40, "real_el"),
        Equipment(4, "Noisy Pro", 280.0, 80, "noisy"),
        Equipment(5, "YW-001 Condenser", 500.0, 150, "yw_001")
    )

    val videoThemes = listOf(
        VideoTheme(1, "Пранки"),
        VideoTheme(2, "Клипы"),
        VideoTheme(3, "Пародии"),
        VideoTheme(4, "Шоу"),
        VideoTheme(5, "Игры"),
        VideoTheme(6, "Обзоры"),
        VideoTheme(7, "Лайфстайл"),
        VideoTheme(8, "Стримы")
    )

    val achievements = listOf(
        Achievement("subs_100", "Первая сотня подписчиков", 100, "dost_100"),
        Achievement("subs_1000", "Тысячная аудитория", 1000, "dost_1000"),
        Achievement("subs_50000", "50 тысяч поклонников", 50000, "dost_50000"),
        Achievement("subs_100k", "Серебряная кнопка", 100000, "dost_100k"),
        Achievement("subs_gal", "Подтверждённый канал", 100000, "gal"),
        Achievement("subs_05", "Канал-полумиллионник", 500000, "dost_0_5"),
        Achievement("subs_1kk", "Золотая кнопка", 1_000_000, "dost_1kk"),
        Achievement("subs_10kk", "Бриллиантовая кнопка", 10_000_000, "dost_10kk"),
        Achievement("subs_50kk", "Рубиновая кнопка", 50_000_000, "dost_50kk"),
        Achievement("subs_100kk", "Красная Бриллиантовая кнопка", 100_000_000, "dost_100kk")
    )

    // Бонусы темы канала (упрощённо)
    val channelThemes = listOf(
        ChannelTheme(0, "Без темы", 0, 0, 0.0),
        ChannelTheme(1, "Развлечения", 10, 20, 50.0),
        ChannelTheme(2, "Игры", 5, 40, 80.0),
        ChannelTheme(3, "Музыка", 25, 10, 60.0),
        ChannelTheme(4, "Образование", 15, 30, 100.0),
        ChannelTheme(5, "Влоги", 20, 25, 70.0)
    )
}

data class ChannelTheme(
    val id: Int,
    val name: String,
    val bonusSubs: Int,
    val bonusViews: Int,
    val price: Double
)
