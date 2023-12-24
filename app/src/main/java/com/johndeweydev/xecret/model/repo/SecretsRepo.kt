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

  suspend fun getAllNonTemporarilyDeletedSecrets(): ArrayList<SecretData> {
    return convertFromEntitiesToData(ArrayList(secretsDao.getAllNonTemporarilyDeletedSecrets()))
  }

  suspend fun addNewSecret(secretData: SecretData) {
    val secretEntity = convertFromDataToEntity(secretData)
    Log.d("dev-log", "SecretsRepo.addNewSecret: Adding new secret [${secretEntity.name}]" +
            ", id is [${secretEntity.uid}]")
    secretEntity.createdAt = Date()
    secretEntity.updatedAt = Date()
    secretsDao.addNewSecret(secretEntity)
  }

  suspend fun updateSecret(secretData: SecretData): Date {
    val secretEntity = convertFromDataToEntity(secretData)
    Log.d("dev-log", "SecretsRepo.updateSecret: Updating secret [${secretEntity.name}]" +
            ", id is [${secretEntity.uid}]")
    val date = Date()
    secretEntity.updatedAt = date
    secretsDao.updateSecret(secretEntity)
    return date
  }

  suspend fun temporaryDeleteSecret(secretData: SecretData) {
    val secretEntity = convertFromDataToEntity(secretData)
    Log.d("dev-log", "SecretsRepo.temporaryDeleteSecret: Temporarily deleting secret " +
            "[${secretEntity.name}], id is [${secretEntity.uid}]")
    secretEntity.deletedAt = Date()
    secretsDao.updateSecret(secretEntity)
  }

  suspend fun getAllTemporarilyDeletedSecret(): ArrayList<SecretData> {
    return convertFromEntitiesToData(ArrayList(secretsDao.getAllTemporaryDeletedSecret()))
  }

  suspend fun deleteSecret(secretData: SecretData) {
    val secretEntity = convertFromDataToEntity(secretData)
    Log.d("dev-log", "SecretsRepo.deleteSecret: Deleting secret [${secretEntity.name}]" +
            ", id is [${secretEntity.uid}]")
    secretsDao.deleteSecret(secretEntity)
  }

  suspend fun searchDatabase(searchQuery: String): ArrayList<SecretData> {
    return convertFromEntitiesToData(ArrayList(secretsDao.searchDatabase(searchQuery)))
  }

  private fun convertFromEntitiesToData(
    entities: ArrayList<SecretEntity>
  ): ArrayList<SecretData> {
    val data = ArrayList<SecretData>()

    entities.forEach {
      val secretData = SecretData(
        uid = it.uid,
        flag = it.flag,
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
      flag = secretData.flag,
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