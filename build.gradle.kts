import com.diffplug.gradle.spotless.SpotlessExtension
import org.gradle.api.tasks.SourceSetContainer
import ru.vyarus.gradle.plugin.animalsniffer.AnimalSnifferExtension
import ru.vyarus.gradle.plugin.animalsniffer.signature.AnimalSnifferSignatureExtension

buildscript {
    dependencies {
        classpath(libs.androidPlugin)
        classpath(libs.kotlin.gradlePlugin)
        classpath(libs.mavenPublishPlugin)
        classpath(libs.animalSnifferPlugin)
        classpath(libs.spotlessPlugin)
        classpath(libs.googleJavaFormat)
    }
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
    }
}

subprojects {
    tasks.withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
    }

    plugins.withId("java-library") {
//        plugins.apply("ru.vyarus.animalsniffer")
////        dependencies {
//            extensions.configure<AnimalSnifferExtension>("animalsniffer") {
//                sourceSets = listOf(extensions.getByType(SourceSetContainer::class.java).getByName("main"))
//            }
//            extensions.configure<AnimalSnifferSignatureExtension>("animalsnifferSignature") {
//                signatures("org.codehaus.mojo.signature:java18:1.0@signature")
//            }
//            if (project.path == ":touchpoint-android") {
//                extensions.configure<AnimalSnifferSignatureExtension>("animalsnifferSignature") {
//                    signatures("net.sf.androidscents.signature:android-api-level-21:5.0.1_r2@signature")
//                }
//            }
////        }

        plugins.apply("com.diffplug.spotless")
        extensions.configure<SpotlessExtension>("spotless") {
            java {
                googleJavaFormat(libs.googleJavaFormat.get().version)
                    .formatJavadoc(false)
                removeUnusedImports()
                target("src/*/java*/**/*.java")
            }
        }
    }

}