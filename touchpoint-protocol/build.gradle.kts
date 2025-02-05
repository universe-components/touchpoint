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
    kotlinOptions {
        jvmTarget = "17"
    }
    packagingOptions {
        pickFirst("**")
    }
}

fataar {
    transitive = true
}

publishing {
    repositories {
        mavenLocal()
    }
    publications {
        register<MavenPublication>("mavenJava") {
            groupId = "com.universe.touchpoint"
            version = "1.0.0"
            artifactId = "touchpoint-protocol"

            // 必须有这个，否则不会上传AAR包
            afterEvaluate {
                artifact(tasks.getByName("bundleReleaseAar"))
            }
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.room.compiler.processing)
    embed(libs.androidx.localbroadcast) {
        exclude(group = "androidx.annotation", module = "annotation")
    }
//    implementation(libs.material)
    embed(libs.replugin.plugin.lib)
    embed(libs.replugin.host.lib)
    embed(libs.kryo)
    implementation(libs.fury)
    embed(libs.eventbus)
    implementation(libs.incap)
    annotationProcessor(libs.incap.processor)
    implementation(libs.auto.service)
    annotationProcessor(libs.auto.service.annotations)
    implementation(libs.auto.value)
    implementation(libs.auto.value.annotations)
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
    api(libs.openai)
    api(libs.anthropic)
    api(libs.dubbo)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
