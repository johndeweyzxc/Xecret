import java.util.Properties
import java.io.FileInputStream

plugins {
  id("com.android.application")
  id("org.jetbrains.kotlin.android")
  id("com.google.devtools.ksp")
}

android {
  namespace = "com.johndeweydev.xecret"
  compileSdk = 34

  defaultConfig {
    applicationId = "com.johndeweydev"
    minSdk = 24
    targetSdk = 34
    versionCode = 1
    versionName = "1.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  signingConfigs {
    val keystoreProperties = rootProject.file("keystore.properties")
    val properties = Properties()
    properties.load(FileInputStream(keystoreProperties))

    getByName("debug") {
      storeFile = file(properties.getProperty("debugStoreFile"))
      storePassword = properties.getProperty("debugPassword")
      keyAlias = properties.getProperty("debugKeyAlias")
      keyPassword  = properties.getProperty("debugKeyPassword")
    }

    create("release") {
      storeFile = file(properties.getProperty("releaseStoreFile"))
      storePassword = properties.getProperty("releasePassword")
      keyAlias = properties.getProperty("releaseKeyAlias")
      keyPassword  = properties.getProperty("releaseKeyPassword")
    }
  }

  buildTypes {
    debug {
      signingConfig = signingConfigs.getByName("debug")
      isMinifyEnabled = false
      applicationIdSuffix = ".debug.xecret"
    }

    release {
      signingConfig = signingConfigs.getByName("release")
      isMinifyEnabled = false
      applicationIdSuffix = ".release.xecret"
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"),
        "proguard-rules.pro")
    }
  }
  buildFeatures {
    viewBinding = true
    buildConfig = true
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }
  kotlinOptions {
    jvmTarget = "1.8"
  }
}

dependencies {
  implementation("androidx.core:core-ktx:1.12.0")
  implementation("androidx.appcompat:appcompat:1.6.1")
  implementation("com.google.android.material:material:1.10.0")
  implementation("androidx.constraintlayout:constraintlayout:2.1.4")
  implementation("androidx.navigation:navigation-fragment-ktx:2.7.5")
  implementation("androidx.navigation:navigation-ui-ktx:2.7.5")
  implementation("androidx.room:room-runtime:2.6.1")
  implementation("androidx.room:room-runtime:2.6.1")
  implementation("com.google.code.gson:gson:2.10.1")
  implementation("it.xabaras.android:recyclerview-swipedecorator:1.4")
  testImplementation("junit:junit:4.13.2")
  androidTestImplementation("androidx.test.ext:junit:1.1.5")
  androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

  debugImplementation("com.squareup.leakcanary:leakcanary-android:2.12")

  ksp("androidx.room:room-compiler:2.6.1")
}