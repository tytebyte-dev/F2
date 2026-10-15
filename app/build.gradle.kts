import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

// Настройки приложения берутся из f2.properties в корне репозитория.
val f2 = Properties().apply {
    rootProject.file("f2.properties").inputStream().use { load(it) }
}

fun f2(key: String): String =
    f2.getProperty(key)?.trim()?.takeIf { it.isNotEmpty() }
        ?: throw GradleException("f2.properties: не задан параметр $key")

android {
    // namespace фиксирован (для R и классов), applicationId настраивается через PACKAGE_NAME
    namespace = "app.f2"
    compileSdk = 35

    defaultConfig {
        applicationId = f2("PACKAGE_NAME")
        minSdk = 21
        targetSdk = 35
        versionCode = f2("VERSION_CODE").toInt()
        versionName = f2("VERSION_NAME")
        resValue("string", "app_name", f2("APP_NAME"))
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }

    buildFeatures {
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

// Debug-сборки считаются пре-релизом: versionName = PRE_VERSION_NAME
androidComponents {
    onVariants { variant ->
        if (variant.buildType == "debug") {
            variant.outputs.forEach { it.versionName.set(f2("PRE_VERSION_NAME")) }
        }
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.15.0")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
}
