package com.johndeweydev.xecret.api.secretsdb

import android.util.Log
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.johndeweydev.xecret.model.converters.Converter
import com.johndeweydev.xecret.model.data.SecretEntity

@Database(entities = [SecretEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converter::class)
abstract class SecretsDatabase: RoomDatabase() {
  abstract fun secretsDao(): SecretsDao

  init {
    Log.w("dev-log", "SecretsDatabase: Created new instance")
  }
}