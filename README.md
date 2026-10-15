# F2

Заготовка Android-приложения с пустой страницей.

## Настройка

Все параметры приложения лежат в [`f2.properties`](f2.properties):

| Ключ | Назначение |
|---|---|
| `APP_NAME` | название приложения (`app_name`) |
| `PACKAGE_NAME` | `applicationId` |
| `VERSION_NAME` | версия release-сборки |
| `PRE_VERSION_NAME` | версия debug (пре-релиз) сборки |
| `VERSION_CODE` | `versionCode` |

## Темы

- Android 11+ (API 30+): `Theme.Material3.DayNight.NoActionBar` (`values-v30`)
- Android 10 и ниже: `Theme.MaterialComponents.DayNight.NoActionBar.Bridge` (`values`)

## Сборка

```
./gradlew assembleDebug assembleRelease
```

GitHub Actions (`.github/workflows/build-private.yml`) собирает APK и кэширует Gradle (зависимости, build cache) и промежуточные результаты сборки.
