plugins {
    id("java-library")
    id("com.vanniktech.maven.publish")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    api(project(":touchpoint-protocol")) {
        repositories {
            maven {
                url = uri("https://jitpack.io")
            }
        }
    }
    api(libs.spring.context)
    testImplementation(libs.junit)
}

tasks.jar {
    manifest {
        attributes["Automatic-Module-Name"] = "touchpoint.spring"
    }
}