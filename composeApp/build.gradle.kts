import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.googleServices)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.firebase.crashlytics)
    id("org.jetbrains.kotlin.native.cocoapods")

}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }


    cocoapods {
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        version = "1.0"
        ios.deploymentTarget = "16.0"

        noPodspec()

        framework {
            baseName = "signin"
            isStatic = true
        }

        pod("GoogleSignIn")
    }

    sourceSets {
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.core.splashscreen)
            implementation(project.dependencies.platform(libs.firebase.bom))
            implementation(libs.firebase.crashlyticsKtx)
            implementation(libs.koin.android)
            implementation(libs.koin.androidx.compose)
            implementation(libs.play.services.auth)
        }

        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(libs.datastore.preferences)
            implementation(libs.datastore)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.jetbrains.navigation.compose)
            implementation(libs.coil.compose)
            implementation(libs.coil.network.ktor)
            implementation(libs.viewmodel.compose)
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)

            implementation(libs.firebase.gitlive.common)
            implementation(libs.firebase.gitlive.auth)
            implementation("io.github.kevinnzou:compose-webview-multiplatform:1.9.40")


            val voyagerVersion = "1.1.0-beta03"
            implementation ("cafe.adriel.voyager:voyager-navigator:$voyagerVersion")
            implementation ("cafe.adriel.voyager:voyager-tab-navigator:$voyagerVersion")
            implementation ("cafe.adriel.voyager:voyager-transitions:$voyagerVersion")
            implementation("cafe.adriel.voyager:voyager-screenmodel:$voyagerVersion")


//            implementation("io.github.mirzemehdi:kmpauth-google:2.0.0")
//            implementation("io.github.mirzemehdi:kmpauth-uihelper:2.0.0")
        }
    }
}

android {
    namespace = "com.senior25.tzakar"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    signingConfigs {
        create("release") {
            keyAlias = "tzakarReminder"
            keyPassword = "tzakar123"
            storeFile  = file(rootProject.file("composeApp/keystore.jks"))
            storePassword=  "tzakar123"
        }
    }

    defaultConfig {
        applicationId = "com.senior25.tzakar"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("release")

        }
        getByName("debug") {
            signingConfig = signingConfigs.getByName("release")

        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.androidx.material3.android)
    debugImplementation(compose.uiTooling)
}

