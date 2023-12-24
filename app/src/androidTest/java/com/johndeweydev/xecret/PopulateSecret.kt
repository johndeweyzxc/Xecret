package com.johndeweydev.xecret

import android.os.SystemClock
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.matcher.ViewMatchers.withId

class PopulateSecret {
  companion object {

    fun populateNameAndDescription(name: String, description: String) {
      onView(withId(R.id.materialCardViewSetNameAndDescriptionCreateSecretBottomDialog)).perform(
        click())
      onView(withId(R.id.textInputEditTextDialogNameInput)).perform(
        typeText("$name name automated")).perform(closeSoftKeyboard())
      onView(withId(R.id.textInputEditTextDialogDescriptionInput)).perform(
        typeText("$description description automated")).perform(closeSoftKeyboard())
      // Click the "SET" button in the dialog
      onView(withId(android.R.id.button1)).perform(click())
    }

    fun populateNote(note: String) {
      onView(withId(R.id.materialCardViewSetNoteCreateSecretBottomDialog)).perform(click())
      onView(withId(R.id.textInputEditTextDialogNoteInput)).perform(
        typeText("$note note automated")).perform(closeSoftKeyboard())
      // Click the "SET" button in the dialog
      onView(withId(android.R.id.button1)).perform(click())
    }

    fun populateUsernameAndPassword(username: String, password: String) {
      onView(withId(R.id.materialCardViewSetUsernameAndPasswordCreateSecretBottomDialog))
        .perform(click())
      onView(withId(R.id.textInputEditTextDialogUsernameInput)).perform(
        typeText("$username username automated")).perform(closeSoftKeyboard())
      onView(withId(R.id.textInputEditTextDialogPasswordInput)).perform(
        typeText("$password password automated")).perform(closeSoftKeyboard())
      // Click the "SET" button in the dialog
      onView(withId(android.R.id.button1)).perform(click())
    }

    fun populateSecurityCodes(code: String) {
      for (i in 1..3) {
        onView(withId(R.id.materialCardViewAddSecurityCodesCreateSecretBottomDialog)).perform(
          click())
        SystemClock.sleep(500)
        onView(withId(R.id.textInputEditTextDialogSecurityCodeInput)).perform(
          typeText("$code $i automated")).perform(closeSoftKeyboard())
        // Click the "ADD" button in the dialog
        onView(withId(android.R.id.button1)).perform(click())
      }
    }

    fun populateAssociatedAccounts(email: String) {
      for (i in 1..3) {
        onView(withId(R.id.materialCardViewAddEmailCreateSecretBottomDialog)).perform(click())
        SystemClock.sleep(500)
        onView(withId(R.id.textInputEditTextDialogEmailInput)).perform(
          typeText("$email $i automated")).perform(closeSoftKeyboard())
        // Click the "ADD" button in the dialog
        onView(withId(android.R.id.button1)).perform(click())
      }
    }

    fun populateAssociatedPhoneNumbers(phoneNumber: String) {
      for (i in 1..3) {
        onView(withId(R.id.materialCardViewAddPhoneNumberCreateSecretBottomDialog)).perform(click())
        SystemClock.sleep(500)
        onView(withId(R.id.textInputEditTextDialogPhoneNumberInput)).perform(
          typeText("${i}${phoneNumber}")).perform(closeSoftKeyboard())
        // Click "ADD" button in the dialog
        onView(withId(android.R.id.button1)).perform(click())
      }
    }

    fun populateExtraInfo() {
      onView(withId(R.id.materialCardViewAddExtraInformationCreateSecretBottomDialog)).perform(
        click())
      onView(withId(R.id.textInputEditTextDialogExtraInfoInput)).perform(
        typeText("Extra info automated")).perform(closeSoftKeyboard())
      // Click "SET" button in the dialog
      onView(withId(android.R.id.button1)).perform(click())
    }
  }
}