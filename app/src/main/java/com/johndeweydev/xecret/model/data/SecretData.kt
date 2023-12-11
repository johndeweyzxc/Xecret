package com.johndeweydev.xecret.model.data

import java.util.Date

data class SecretData (
  var uid: Int,

  var name: String,
  var description: String,
  var notes: String,
  var userName: String,
  var password: String,
  val extraPasswordsOrSecurityCodes: ArrayList<String>,

  val associatedEmails: ArrayList<String>,
  var usingEmailForTwoFA: Boolean,
  val associatedPhoneNumbers: ArrayList<String>,
  var usingPhoneNumberForTwoFA: Boolean,

  var extras: String,

  var createdAt: Date?,
  var updatedAt: Date?,
  var deletedAt: Date?
) : Cloneable {

  @Suppress("UNCHECKED_CAST")
  public override fun clone() = SecretData(
    uid,
    name,
    description,
    notes,
    userName,
    password,
    extraPasswordsOrSecurityCodes.clone() as ArrayList<String>,
    associatedEmails.clone() as ArrayList<String>,
    usingEmailForTwoFA,
    associatedPhoneNumbers.clone() as ArrayList<String>,
    usingPhoneNumberForTwoFA,
    extras,
    createdAt?.clone() as Date?,
    updatedAt?.clone() as Date?,
    deletedAt?.clone() as Date?)
}