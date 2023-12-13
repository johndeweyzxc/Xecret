package com.johndeweydev.xecret

import android.os.SystemClock
import android.support.test.uiautomator.UiDevice
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.swipeUp
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class MainTest {

  private fun populateNameAndDescription(name: String, description: String) {
    onView(withId(R.id.materialCardViewSetNameAndDescriptionCreateSecretBottomDialog))
      .perform(click())
    onView(withId(R.id.textInputEditTextDialogNameInput)).perform(
      typeText("$name name automated")).perform(closeSoftKeyboard())
    onView(withId(R.id.textInputEditTextDialogDescriptionInput)).perform(
      typeText("$description description automated")).perform(closeSoftKeyboard())
    // Click the "SET" button in the dialog
    onView(withId(android.R.id.button1)).perform(click())
  }

  private fun populateNote(note: String) {
    onView(withId(R.id.materialCardViewSetNoteCreateSecretBottomDialog)).perform(click())
    onView(withId(R.id.textInputEditTextDialogNoteInput)).perform(
      typeText("$note note automated")).perform(closeSoftKeyboard())
    // Click the "SET" button in the dialog
    onView(withId(android.R.id.button1)).perform(click())
  }

  private fun populateUsernameAndPassword(username: String, password: String) {
    onView(withId(R.id.materialCardViewSetUsernameAndPasswordCreateSecretBottomDialog)).perform(
      click())
    onView(withId(R.id.textInputEditTextDialogUsernameInput)).perform(
      typeText("$username username automated")).perform(closeSoftKeyboard())
    onView(withId(R.id.textInputEditTextDialogPasswordInput)).perform(
      typeText("$password password automated")).perform(closeSoftKeyboard())
    // Click the "SET" button in the dialog
    onView(withId(android.R.id.button1)).perform(click())
  }

  private fun populateSecurityCodes(code: String) {
    for (i in 1..3) {
      onView(withId(R.id.materialCardViewAddSecurityCodesCreateSecretBottomDialog)).perform(click())
      onView(withId(R.id.textInputEditTextDialogSecurityCodeInput)).perform(
        typeText("$code $i automated")).perform(closeSoftKeyboard())
      // Click the "ADD" button in the dialog
      onView(withId(android.R.id.button1)).perform(click())
    }
  }

  private fun populateAssociatedAccounts(email: String) {
    for (i in 1..3) {
      onView(withId(R.id.materialCardViewAddEmailCreateSecretBottomDialog)).perform(click())
      onView(withId(R.id.textInputEditTextDialogEmailInput)).perform(
        typeText("$email $i automated")).perform(closeSoftKeyboard())
      // Click the "ADD" button in the dialog
      onView(withId(android.R.id.button1)).perform(click())
    }
  }

  private fun populateAssociatedPhoneNumbers(phoneNumber: String) {
    for (i in 1..3) {
      onView(withId(R.id.materialCardViewAddPhoneNumberCreateSecretBottomDialog)).perform(click())
      onView(withId(R.id.textInputEditTextDialogPhoneNumberInput)).perform(
        typeText("${i}${phoneNumber}")).perform(closeSoftKeyboard())
      // Click "ADD" button in the dialog
      onView(withId(android.R.id.button1)).perform(click())
    }
  }

  private fun populateExtraInfo() {
    onView(withId(R.id.materialCardViewAddExtraInformationCreateSecretBottomDialog))
      .perform(click())
    onView(withId(R.id.textInputEditTextDialogExtraInfoInput)).perform(
      typeText("Extra info automated")).perform(closeSoftKeyboard())
    // Click "SET" button in the dialog
    onView(withId(android.R.id.button1)).perform(click())
  }

  @Test
  fun mainTest() {
    ActivityScenario.launch(MainActivity::class.java)
    val testData = listOf("App 1", "App 2", "App 3")
    val testDataSecurityCodes = listOf("1111", "2222", "3333")
    val testDataPhoneNumbers = listOf("123", "1234", "12345")

    testData.forEachIndexed { index, element ->
      // Click the floating action button
      onView(withId(R.id.floatingActionButtonSecretList)).perform(click())
      // CREATE A SECRET
      onView(withId(R.id.materialCardViewSetNameAndDescriptionCreateSecretBottomDialog)).perform(
        swipeUp())
      SystemClock.sleep(2000)
      populateNameAndDescription(element, element)
      populateNote(element)
      populateUsernameAndPassword(element, element)

      SystemClock.sleep(1000)
      // Android smart lock will appear
      val uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
      val currentPackageName = uiDevice.currentPackageName
      if (currentPackageName != "com.johndeweydev.debug.xecret") {
        uiDevice.pressBack()
      }

      populateSecurityCodes(testDataSecurityCodes[index])
      populateAssociatedAccounts(element)
      populateAssociatedPhoneNumbers(testDataPhoneNumbers[index])
      populateExtraInfo()
      onView(withId(R.id.buttonCreateSecretBottomDialog)).perform(click())
      onView(withText("$element name automated")).check(matches(isDisplayed()))
    }
  }
}