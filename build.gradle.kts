import com.diffplug.gradle.spotless.SpotlessExtension
import com.diffplug.gradle.spotless.SpotlessPlugin
import shadow.bundletool.com.android.tools.r8.internal.li

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.spotless) apply false
}

val ktlintVersion = libs.versions.ktlint.get()

subprojects{
    apply<SpotlessPlugin>()
    configure<SpotlessExtension> {
        kotlin {
            target("src/**/*.kt")
            targetExclude("build/**/*.kt")
            ktlint(ktlintVersion)
        }
        kotlinGradle {
            target("*.kts")
            ktlint(ktlintVersion)
        }
    }
}
