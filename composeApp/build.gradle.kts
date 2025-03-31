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
    alias(libs.plugins.ksp)
//    alias(libs.plugins.room)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    sourceSets.commonMain {
//        languageSettings.optIn("kotlin.experimental.ExperimentalObjCName")
        kotlin.srcDir("build/generated/ksp/metadata")
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
            implementation("androidx.core:core-ktx:1.15.0")
            implementation(project.dependencies.platform(libs.firebase.bom))
            implementation(libs.firebase.crashlyticsKtx)
            implementation(libs.koin.android)
            implementation(libs.koin.androidx.compose)
            implementation(libs.play.services.auth)
            implementation("com.jakewharton.threetenabp:threetenabp:1.4.6")
            implementation("com.google.android.material:material:1.12.0")
            implementation("com.google.maps.android:maps-compose:6.1.0")

        }

        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.material3)
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
            implementation(libs.firebase.gitlive.database)
            implementation(libs.github.kevinnzou.webview)
            implementation(libs.cafe.adriel.voyager.navigator)
            implementation(libs.cafe.adriel.voyager.tab)
            implementation(libs.cafe.adriel.voyager.transitions)
            implementation(libs.cafe.adriel.voyager.screenmodel)
            implementation(libs.decompose)
            implementation(libs.androidx.room.runtime)
            implementation(libs.sqlite.bundled)
            implementation("io.ktor:ktor-utils:3.0.1")
            implementation("media.kamel:kamel-image:1.0.3")
            implementation("io.github.qdsfdhvh:image-loader:1.10.0")
            implementation("network.chaintech:kmp-date-time-picker:1.0.5")
//            implementation("com.google.android.gms:play-services-maps:18.2.0")


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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}
//room {
//    schemaDirectory("$projectDir/schemas")
//}
ksp {
    arg("room.schemaLocation", "${projectDir}/schemas")
}
dependencies {
    implementation(libs.androidx.material3.android)
    implementation(libs.places)
    debugImplementation(compose.uiTooling)
    listOf(
        "kspAndroid",
        // "kspJvm",
        "kspIosSimulatorArm64",
        "kspIosX64",
        "kspIosArm64",
        "kspCommonMainMetadata"
    ).forEach {
        add(it, libs.androidx.room.compiler)
    }
//    add("kspAndroid", libs.androidx.room.compiler)
//    add("kspIosSimulatorArm64", libs.androidx.room.compiler)
//    add("kspIosX64", libs.androidx.room.compiler)
//    add("kspIosArm64", libs.androidx.room.compiler)
//    add("kspCommonMainMetadata", libs.androidx.room.compiler)

}


//tasks.withType<org.jetbrains.kotlin.gradle.dsl.KotlinCompile<*>>().configureEach {
//    if (name != "kspCommonMainKotlinMetadata" ) {
//        dependsOn("kspCommonMainKotlinMetadata")
//    }
//}

//tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
//    kotlinOptions.freeCompilerArgs += "-Xexpect-actual-classes"
//}