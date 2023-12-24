package com.johndeweydev.xecret.model.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "secrets")
data class SecretEntity (
  @PrimaryKey val uid: String,

  @ColumnInfo(name = "flag") val flag: String,
  @ColumnInfo(name = "name") val name: String,
  @ColumnInfo(name = "description") val description: String,
  @ColumnInfo(name = "notes") val notes: String,
  @ColumnInfo(name = "userName") val userName: String,
  @ColumnInfo(name = "password") val password: String,
  @ColumnInfo(
    name = "extraPasswordsOrSecurityCodes") val extraPasswordsOrSecurityCodes: ArrayList<String>,

  @ColumnInfo(name = "associatedEmails") val associatedEmails: ArrayList<String>,
  @ColumnInfo(name = "usingEmailForTwoFA") val usingEmailForTwoFA: Boolean,
  @ColumnInfo(name = "associatedPhoneNumbers") val associatedPhoneNumbers: ArrayList<String>,
  @ColumnInfo(name = "usingPhoneNumberForTwoFA") val usingPhoneNumberForTwoFA: Boolean,

  @ColumnInfo(name = "extras") val extras: String,

  @ColumnInfo(name = "createdAt") var createdAt: Date?,
  @ColumnInfo(name = "updatedAt") var updatedAt: Date?,
  @ColumnInfo(name = "deletedAt") var deletedAt: Date?
)