import com.diffplug.gradle.spotless.SpotlessExtension
import org.gradle.api.tasks.SourceSetContainer
import ru.vyarus.gradle.plugin.animalsniffer.AnimalSnifferExtension

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
        plugins.apply("ru.vyarus.animalsniffer")
        extensions.configure<AnimalSnifferExtension>("animalsniffer") {
            sourceSets = listOf(extensions.getByType(SourceSetContainer::class.java).getByName("main"))
        }

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