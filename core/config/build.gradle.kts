plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlinx.kover)
}

kotlin {
    compilerOptions {
        languageVersion = org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_2_0
    }
}

android {
    namespace = "com.tyme.github.users.core.config"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig { minSdk = libs.versions.minSdk.get().toInt() }

    flavorDimensions += "environment"
    productFlavors {
        create("dev") {
            buildConfigField("String", "BASE_URL", "\"https://api.github.com\"")
            buildConfigField("String", "BASE_DOMAIN", "\"api.github.com\"")
        }

        create("stag") {
            buildConfigField("String", "BASE_URL", "\"https://api.github.com\"")
            buildConfigField("String", "BASE_DOMAIN", "\"api.github.com\"")
        }

        create("prod") {
            buildConfigField("String", "BASE_URL", "\"https://api.github.com\"")
            buildConfigField("String", "BASE_DOMAIN", "\"api.github.com\"")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures { buildConfig = true }
}

dependencies {
    implementation(project(":core:network"))
    implementation(project(":core:security"))

    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
}
