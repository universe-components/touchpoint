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
            maven {
                url = uri("https://repo.akka.io/maven")
            }
        }
    }
    api(libs.spring.context)
    testImplementation(libs.junit)
    testImplementation(libs.springboot.test)
}

tasks.jar {
    manifest {
        attributes["Automatic-Module-Name"] = "touchpoint.spring"
    }
}