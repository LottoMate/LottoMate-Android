import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.jetbrains.kotlin.compose.compiler)
    alias(libs.plugins.google.devtools.ksp)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.jetbrains.kotlin.serialization)
    alias(libs.plugins.androidx.navigation.safeargs)
    alias(libs.plugins.jetbrains.kotlin.parcelize)
}

val localProperties = Properties()
localProperties.load(FileInputStream(rootProject.file("local.properties")))

android {
    namespace = "com.lottomate.lottomate"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.lottomate.lottomate"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        buildConfigField("String", "DATABASE_BASE_URL", localProperties.getProperty("DATABASE_BASE_URL"))
        buildConfigField("String", "MOCK_SERVER_URL", localProperties.getProperty("MOCK_SERVER_URL"))
        buildConfigField("String", "GOOGLE_OAUTH_CLIENT_ID", localProperties.getProperty("GOOGLE_OAUTH_CLIENT_ID"))
        manifestPlaceholders["NAVER_MAP_CLIENT_ID"] = localProperties.getProperty("NAVER_MAP_CLIENT_ID")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    viewBinding {
        enable = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    sourceSets["main"].assets.srcDirs("src/main/assets")
    composeCompiler {
        enableStrongSkippingMode = true
        includeSourceInformation = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.appcompat)
    implementation(libs.com.google.accompanist.systemuicontroller)

    // Compose
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.hilt.navigation.compose)

    // Hilt
    implementation(libs.hilt.android)
    implementation(libs.androidx.material3.android)
    ksp(libs.hilt.compiler)

    // Network
    implementation(libs.bundles.network)

    // Coil
    implementation(libs.io.coil.compose)
    implementation(libs.io.coil.gif)

    // Naver Maps
    implementation(libs.com.naver.maps)
    implementation(libs.io.github.fornewid.naver.map.compose)

    // GPS
    implementation(libs.google.gms.play.services.location)

    // Room
    implementation(libs.androidx.room)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

    // Oauth
    implementation(libs.google.gms.play.services.auth)
//    implementation(libs.google.identity.googleid)
//    implementation(libs.androidx.credentials.play.services.auth)
//    implementation(libs.androidx.credentials.credentials)
    implementation("androidx.browser:browser:1.5.0")

    // QR Scanner
    implementation(libs.com.journeyapps.zxing)

    // DataStore - Preference
    implementation(libs.androidx.datastore.preference)

    // Permission
    implementation(libs.com.google.accompanist.permissions)

    // Lottie
    implementation(libs.com.airbnb.lottie.compose)

    // Splash Screen
    implementation(libs.androidx.splash)

    // Logger
    implementation(libs.com.jakewharton.timber)

    testImplementation(libs.junit)
    testImplementation(libs.jetbrains.coroutines.test)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}