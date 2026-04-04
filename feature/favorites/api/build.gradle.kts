plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.tyme.github.users.feature.favorites.api"
    compileSdk = libs.versions.compileSdk.get().toInt()
    defaultConfig { minSdk = libs.versions.minSdk.get().toInt() }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions { jvmTarget = "1.8" }

    flavorDimensions += "environment"
    productFlavors {
        create("dev") {}
        create("stag") {}
        create("prod") {}
    }
}

dependencies {
    implementation(libs.kotlinx.coroutines.android)
}
