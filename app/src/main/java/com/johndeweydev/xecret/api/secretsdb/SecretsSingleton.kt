package com.johndeweydev.xecret.api.secretsdb

import android.content.Context
import android.util.Log
import androidx.room.Room

class SecretsSingleton {

  private var secretsDatabase: SecretsDatabase? = null

  companion object {
    private var instance: SecretsSingleton? = null

    fun getInstance(): SecretsSingleton {
      if (instance == null) {
        instance = SecretsSingleton()
      }
      return instance as SecretsSingleton
    }
  }

  fun setSecretsDatabase(context: Context) {
   secretsDatabase = Room.databaseBuilder(
     context.applicationContext, SecretsDatabase::class.java, "secretsDatabase").build()
  }

  fun getSecretsDatabase(): SecretsDatabase? {
    return if (secretsDatabase != null) {
      secretsDatabase as SecretsDatabase
    } else {
      // Before using this method it is required to first create a new instance of this singleton
      // using getInstance then set the database using setSecretsDatabase
      Log.e("dev-log", "SecretsSingleton.getSecretsDatabase: " +
              "Instance of SecretsDatabase is not found in the singleton")
      null
    }
  }
}