plugins {
    id("kotlin-kapt")
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("com.google.dagger.hilt.android")
    id("com.kezong.fat-aar")
    id("maven-publish")
}

android {
    namespace = "com.universe.touchpoint"
    compileSdk = 34

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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    packagingOptions {
        pickFirst("**")
    }
}

fataar {
    transitive = true
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    embed(libs.androidx.localbroadcast) {
        exclude(group = "androidx.annotation", module = "annotation")
    }
//    implementation(libs.material)
    embed(libs.replugin.plugin.lib)
    embed(libs.replugin.host.lib)
    embed(libs.kryo)
    implementation(libs.fury)
    embed(libs.eventbus)
    implementation("com.google.dagger:hilt-android:2.51.1")
    kapt("com.google.dagger:hilt-android-compiler:2.51.1")
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

publishing {
    repositories {
        mavenLocal()
    }
    publications {
        register<MavenPublication>("mavenJava") {
            groupId = "com.universe.touchpoint"
            version = "1.0.0"
            artifactId = "touchpoint-lib"

            // 必须有这个，否则不会上传AAR包
            afterEvaluate {
                artifact(tasks.getByName("bundleReleaseAar"))
            }
        }
    }
}
