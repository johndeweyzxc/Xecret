package com.johndeweydev.xecret

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.matcher.ViewMatchers.withId

class UpdateSecret {

  companion object {
    fun updateNameAndDescription(name: String) {
      onView(withId(R.id.materialCardViewNameAndDescriptionSecretInfoBottomDialog)).perform(
        click())
      onView(withId(R.id.textInputEditTextDialogNameInput)).perform(replaceText(
        "$name name updated")).perform(closeSoftKeyboard())
      onView(withId(R.id.textInputEditTextDialogDescriptionInput)).perform(replaceText(
        "$name description updated")).perform(closeSoftKeyboard())
      // Click the "SET" button in the dialog
      onView(withId(android.R.id.button1)).perform(click())
    }

    fun updateNote(note: String) {
      onView(withId(R.id.materialCardViewNoteSecretInfoBottomDialog)).perform(click())
      onView(withId(R.id.textInputEditTextDialogNoteInput)).perform(replaceText(
        "$note note updated")).perform(closeSoftKeyboard()).perform(closeSoftKeyboard())
      // Click the "SET" button in the dialog
      onView(withId(android.R.id.button1)).perform(click())
    }

    fun updateUsernameAndPassword(unameAndPasswd: String) {
      onView(withId(R.id.materialCardViewUsernameAndPasswordSecretInfoBottomDialog)).perform(
        click())
      onView(withId(R.id.textInputEditTextDialogUsernameInput)).perform(replaceText(
        "$unameAndPasswd username updated")).perform(closeSoftKeyboard())
      onView(withId(R.id.textInputEditTextDialogPasswordInput)).perform(replaceText(
        "$unameAndPasswd password updated")).perform(closeSoftKeyboard())
      // Click the "SET" button in the dialog
      onView(withId(android.R.id.button1)).perform(click())
    }

    fun addSecurityCode(code: String) {
      onView(withId(R.id.materialCardViewSecurityCodesSecretInfoBottomDialog)).perform(click())
      onView(withId(R.id.textInputEditTextDialogSecurityCodeInput)).perform(typeText(
        "$code code updated")).perform(closeSoftKeyboard())
      // Click the "ADD" button in the dialog
      onView(withId(android.R.id.button1)).perform(click())
    }

    fun addAssociatedEmailAccount(email: String) {
      onView(withId(R.id.materialCardViewAssociatedEmailsSecretInfoBottomDialog)).perform(click())
      onView(withId(R.id.textInputEditTextDialogEmailInput)).perform(typeText(
        "$email email updated")).perform(closeSoftKeyboard())
      // Click the "ADD" button in the dialog
      onView(withId(android.R.id.button1)).perform(click())
    }

    fun addAssociatedPhoneNumber() {
      onView(withId(R.id.materialCardViewAssociatedPhoneNumbersSecretInfoBottomDialog)).perform(
        click())
      onView(withId(R.id.textInputEditTextDialogPhoneNumberInput)).perform(typeText(
        "0000")).perform(closeSoftKeyboard())
      // Click "ADD" button in the dialog
      onView(withId(android.R.id.button1)).perform(click())
    }

    fun updateExtraInfo(info: String) {
      onView(withId(R.id.materialCardViewExtraInformationSecretInfoBottomDialog)).perform(click())
      onView(withId(R.id.textInputEditTextDialogExtraInfoInput)).perform(replaceText(
        "$info info automated")).perform(closeSoftKeyboard())
      // Click "SET" button in the dialog
      onView(withId(android.R.id.button1)).perform(click())
    }
  }
}