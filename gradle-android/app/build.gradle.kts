plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}


val natives = configurations.create("natives")

android {
    namespace = "com.example.application"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.application"
        minSdk = 33
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.etopaysdk)
    natives(variantOf(libs.etopaysdk) { classifier("natives-android") })

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}

// Task to unzip the natives (and drop the "android" directory) into the jniLibs folder
tasks.register<Copy>("unzipNatives") {
    from(zipTree(natives.singleFile)) {
        include("android/**")
        eachFile {
            relativePath = RelativePath(true, *relativePath.segments.drop(1).toTypedArray())
        }
        includeEmptyDirs = false
    }
    into("src/main/jniLibs")
}

// Make the unzipNatives task run before building
tasks.named("preBuild") {
    dependsOn("unzipNatives")
}
