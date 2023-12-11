package com.johndeweydev.xecret.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.johndeweydev.xecret.api.secretsdb.SecretsSingleton
import com.johndeweydev.xecret.model.data.SecretData
import com.johndeweydev.xecret.model.repo.SecretsRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class SecretsViewModel: ViewModel() {

  private var secretsRepo: SecretsRepo? = null

  var preUploadingSecret: SecretData? = null
  var preUploadingSecretCopy: SecretData? = null
  var selectedSecret: SecretData? = null
  var selectedSecretCopy: SecretData? = null

  private val totalSecurityCodes = MutableLiveData<Int?>(null)
  private val totalAssociatedEmails = MutableLiveData<Int?>(null)
  private val totalAssociatedPhoneNumbers = MutableLiveData<Int?>(null)

  private val secrets = MutableLiveData<ArrayList<SecretData>?>()
  private val newlyAddedSecret = MutableLiveData<SecretData?>()
  private val newlyUpdatedSecret = MutableLiveData<SecretData?>()

  init {
    Log.w("dev-log", "SecretsViewModel: Created new instance")
    val databaseInstance = SecretsSingleton.getInstance()
    val secretsDao = databaseInstance.getSecretsDatabase()?.secretsDao()
    if (secretsDao == null) {
      Log.e("dev-log", "SecretsViewModel: Secrets DAO is not initialized")
    } else {
      secretsRepo = SecretsRepo(secretsDao)
    }
  }

  fun getTotalSecurityCodes(): MutableLiveData<Int?> {
    return totalSecurityCodes
  }

  fun getTotalAssociatedEmails(): MutableLiveData<Int?> {
    return totalAssociatedEmails
  }

  fun getTotalAssociatedPhoneNumbers(): MutableLiveData<Int?> {
    return totalAssociatedPhoneNumbers
  }

  fun getSecrets(): MutableLiveData<ArrayList<SecretData>?> {
    return secrets
  }

  fun getNewlyAddedSecret(): MutableLiveData<SecretData?> {
    return newlyAddedSecret
  }

  fun getNewlyUpdatedSecret(): MutableLiveData<SecretData?> {
    return newlyUpdatedSecret
  }

  fun getAllSecrets() {
    viewModelScope.launch(Dispatchers.IO) {
      val result = async {
        secretsRepo?.getAllSecrets()
      }.await()
      if (result == null) {
        Log.w("dev-log", "SecretsViewModel.getAllSecrets: Got null result from the " +
                "repository")
        return@launch
      }
      secrets.postValue(result)
    }
  }

  fun addNewSecret(): String {
    if (preUploadingSecret?.equals(preUploadingSecretCopy) == true) {
      return "No changes have been made"
    } else if (preUploadingSecret?.name?.isEmpty() == true) {
      return "Name cannot be empty"
    } else if (preUploadingSecret?.description?.isEmpty() == true) {
      return "Description cannot be empty"
    }

    viewModelScope.launch(Dispatchers.IO) {
      if (preUploadingSecret != null) {
        secretsRepo?.addNewSecret(preUploadingSecret!!)
        newlyAddedSecret.postValue(preUploadingSecret)
      } else {
        Log.e("dev-log", "SecretViewModel.addNewSecret: preUploadingSecret is null")
      }
    }
    return "None"
  }

  fun updateSecret(): String {
    if (selectedSecret?.equals(selectedSecretCopy) == true) {
      return "No changes have been made"
    } else if (selectedSecret?.name?.isEmpty() == true) {
      return "Name cannot be empty"
    } else if (selectedSecret?.description?.isEmpty() == true) {
      return "Description cannot be empty"
    }

    viewModelScope.launch(Dispatchers.IO) {
      if (selectedSecret != null) {
        secretsRepo?.updateSecret(selectedSecret!!)
        newlyUpdatedSecret.postValue(selectedSecret)
      } else {
        Log.e("dev-log", "SecretViewModel.updateSecret: selectedSecret is null")
      }
    }
    return "None"
  }

  fun deleteSecret(secretData: SecretData?) {
    viewModelScope.launch(Dispatchers.IO) {
      if (secretData != null) {
        secretsRepo?.deleteSecret(secretData)
      } else {
        Log.e("dev-log", "SecretViewModel.deleteSecret: secretData is null")
      }
    }
  }

  fun searchDatabase(searchQuery: String) {
    viewModelScope.launch(Dispatchers.IO) {
      val result = async {
        secretsRepo?.searchDatabase(searchQuery)
      }.await()
      if (result == null) {
        Log.w("dev-log", "SecretsViewModel.searchDatabase: Got null result from the " +
                "repository")
        return@launch
      }
      secrets.postValue(result)
    }
  }
}