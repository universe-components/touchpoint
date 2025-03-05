plugins {
    id("java-library")
    id("com.vanniktech.maven.publish")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    compileOnly(libs.android)
    implementation(libs.replugin.plugin.lib)
    implementation(libs.replugin.host.lib)
    api(libs.kryo)
    api(libs.fury)
    api(libs.moquette) {
        repositories {
            maven {
                url = uri("https://jitpack.io")
            }
        }
    }
//    api(libs.eventbus)
    api(libs.auto.service)
    annotationProcessor(libs.auto.service.annotations)
    api(libs.auto.value)
    api(libs.auto.value.annotations)
    api(libs.openai)
    api(libs.anthropic)
    api(libs.dubbo)
    api(libs.paho)
    api(libs.retrofit)
    api(libs.converter.gson)
    api(libs.gson)
    api(libs.commons.lang3)
    implementation(libs.jep)
    testImplementation(libs.junit)
}