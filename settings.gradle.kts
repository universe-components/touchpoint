dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "touchpoint"
include(":touchpoint-protocol")
include(":touchpoint-protocol:java-test")
include(":touchpoint-spring")
include(":touchpoint-android")