package com.johndeweydev.xecret.api.secretsdb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.johndeweydev.xecret.model.data.SecretEntity

// TODO Implement: Custom query to set a date for the deletedAt attribute to temporarily
//  delete a secret

@Dao
interface SecretsDao {

  @Query("SELECT * FROM secrets")
  fun getAllSecrets(): List<SecretEntity>
  @Insert(onConflict = OnConflictStrategy.IGNORE)
  fun addNewSecret(secretEntity: SecretEntity)
  @Update
  fun updateSecret(secretEntity: SecretEntity)
  @Delete
  fun deleteSecret(secretEntity: SecretEntity)
  @Query("SELECT * FROM secrets WHERE name LIKE :searchQuery OR description LIKE :searchQuery")
  fun searchDatabase(searchQuery: String): List<SecretEntity>
}