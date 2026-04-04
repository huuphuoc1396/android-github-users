plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.kotlinx.kover)
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions { jvmTarget = "1.8" }

    buildFeatures { buildConfig = true }
}

dependencies {
    implementation(project(":core:network"))
    implementation(project(":core:security"))

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
}
