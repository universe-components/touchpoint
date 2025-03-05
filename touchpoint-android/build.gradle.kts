plugins {
    id("com.android.library")
}

android {
    namespace = "com.universe.touchpoint"
    compileSdk = 35

    defaultConfig {
        minSdk = 26
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    api(project(":touchpoint-protocol")) {
        repositories {
            maven {
                url = uri("https://jitpack.io")
            }
        }
    }
    compileOnly(libs.android)
    implementation(libs.replugin.plugin.lib)
    implementation(libs.replugin.host.lib)
    testImplementation(libs.junit)
}