package com.johndeweydev.xecret.api.secretsdb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.johndeweydev.xecret.model.data.SecretEntity

// TODO Implement: Starred functionality, permanent deletion functionality

@Dao
interface SecretsDao {

  @Query("SELECT * FROM secrets WHERE deletedAt is NULL")
  fun getAllNonTemporarilyDeletedSecrets(): List<SecretEntity>
  @Insert(onConflict = OnConflictStrategy.IGNORE)
  fun addNewSecret(secretEntity: SecretEntity)
  @Update
  fun updateSecret(secretEntity: SecretEntity)
  @Delete
  fun deleteSecret(secretEntity: SecretEntity)
  @Query("SELECT * FROM secrets WHERE name LIKE :searchQuery OR description LIKE :searchQuery")
  fun searchDatabase(searchQuery: String): List<SecretEntity>
  @Query("SELECT * FROM secrets WHERE deletedAt is not NULL")
  fun getAllTemporaryDeletedSecret(): List<SecretEntity>
}