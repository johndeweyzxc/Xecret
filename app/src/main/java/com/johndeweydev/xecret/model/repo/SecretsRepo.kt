package com.johndeweydev.xecret.model.repo

import android.util.Log
import com.johndeweydev.xecret.api.secretsdb.SecretsDao
import com.johndeweydev.xecret.model.data.SecretData
import com.johndeweydev.xecret.model.data.SecretEntity
import java.util.Date


class SecretsRepo(private val secretsDao: SecretsDao) {

  init {
    Log.w("dev-log", "SecretsRepo: Created new instance")
  }

  fun getAllSecrets(): ArrayList<SecretData> {
    return convertFromEntitiesToData(ArrayList(secretsDao.getAllSecrets()))
  }

  fun addNewSecret(secretData: SecretData) {

    val secretEntity = convertFromDataToEntity(secretData)
    Log.d("dev-log", "SecretsRepo.addNewSecret: Adding new secret ${secretEntity.name}")
    secretEntity.createdAt = Date()
    secretEntity.updatedAt = Date()
    secretsDao.addNewSecret(secretEntity)
  }

  fun updateSecret(secretData: SecretData) {
    val secretEntity = convertFromDataToEntity(secretData)
    Log.d("dev-log", "SecretsRepo.updateSecret: Updating secret ${secretEntity.name}" +
            ", key is ${secretEntity.uid}")
    secretEntity.updatedAt = Date()
    secretsDao.updateSecret(secretEntity)
  }

  fun deleteSecret(secretData: SecretData) {
    val secretEntity = convertFromDataToEntity(secretData)
    Log.d("dev-log", "SecretsRepo.deleteSecret: Deleting secret ${secretEntity.name}")
    secretsDao.deleteSecret(secretEntity)
  }

  fun searchDatabase(searchQuery: String): ArrayList<SecretData> {
    return convertFromEntitiesToData(ArrayList(secretsDao.searchDatabase(searchQuery)))
  }

  private fun convertFromEntitiesToData(
    entities: ArrayList<SecretEntity>
  ): ArrayList<SecretData> {
    val data = ArrayList<SecretData>()

    entities.forEach {
      val secretData = SecretData(
        uid = it.uid,
        name = it.name,
        description = it.description,
        notes = it.notes,
        userName = it.userName,
        password = it.password,
        extraPasswordsOrSecurityCodes = it.extraPasswordsOrSecurityCodes,
        associatedEmails = it.associatedEmails,
        usingEmailForTwoFA = it.usingEmailForTwoFA,
        associatedPhoneNumbers = it.associatedPhoneNumbers,
        usingPhoneNumberForTwoFA = it.usingPhoneNumberForTwoFA,
        extras = it.extras,
        createdAt = it.createdAt,
        updatedAt = it.updatedAt,
        deletedAt = it.deletedAt)
      data.add(secretData)
    }
    return data
  }

  private fun convertFromDataToEntity(secretData: SecretData): SecretEntity {
    return SecretEntity(
      uid = secretData.uid,
      name = secretData.name,
      description = secretData.description,
      notes = secretData.notes,
      userName = secretData.userName,
      password = secretData.password,
      extraPasswordsOrSecurityCodes = secretData.extraPasswordsOrSecurityCodes,
      associatedEmails = secretData.associatedEmails,
      usingEmailForTwoFA = secretData.usingEmailForTwoFA,
      associatedPhoneNumbers = secretData.associatedPhoneNumbers,
      usingPhoneNumberForTwoFA = secretData.usingPhoneNumberForTwoFA,
      extras = secretData.extras,
      createdAt = secretData.createdAt,
      updatedAt = secretData.updatedAt,
      deletedAt = secretData.deletedAt)
  }
}