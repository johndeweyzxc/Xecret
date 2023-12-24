package com.johndeweydev.xecret

import android.os.SystemClock
import android.support.test.uiautomator.UiDevice
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeLeft
import androidx.test.espresso.action.ViewActions.swipeUp
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.contrib.NavigationViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import com.johndeweydev.xecret.view.secretlistfragment.SecretListRVAdapter
import org.hamcrest.core.IsNot.not
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class MainTest {

  @Test
  fun toolBarTest() {
    ActivityScenario.launch(MainActivity::class.java)
    onView(withId(R.id.drawerLayoutMain)).perform(DrawerActions.open())
    SystemClock.sleep(1500)
    onView(withId(R.id.navigationViewMain)).perform(NavigationViewActions.navigateTo(
      R.id.allSecretsToolBarDrawerMenu))
    SystemClock.sleep(1500)
    onView(withId(R.id.navigationViewMain)).perform(NavigationViewActions.navigateTo(
      R.id.starredToolBarDrawerMenu))
    SystemClock.sleep(1500)
    onView(withId(R.id.navigationViewMain)).perform(NavigationViewActions.navigateTo(
      R.id.trashToolBarDrawerMenu))
    SystemClock.sleep(1500)
  }

  @Test
  fun testDeletion() {
    ActivityScenario.launch(MainActivity::class.java)
    val testData = listOf("App 1")
    val testDataSecurityCodes = listOf("1111")
    val testDataPhoneNumbers = listOf("123")
    testData.forEachIndexed { index, element ->
      createSecret(index, element, testDataSecurityCodes, testDataPhoneNumbers)
    }
    onView(withId(R.id.recyclerViewSecretList)).perform(RecyclerViewActions
      .actionOnItemAtPosition<SecretListRVAdapter.SecretAdapterViewHolder>(0, swipeLeft()))
    SystemClock.sleep(5000)

    // Verify that the item is in the trash list
    onView(withId(R.id.drawerLayoutMain)).perform(DrawerActions.open())
    SystemClock.sleep(1000)
    onView(withId(R.id.navigationViewMain)).perform(NavigationViewActions.navigateTo(
      R.id.trashToolBarDrawerMenu))
    SystemClock.sleep(1000)
    onView(withText("App 1 name automated")).check(matches(isDisplayed()))

    // Verify that the item is not in the secrets list
    onView(withId(R.id.drawerLayoutMain)).perform(DrawerActions.open())
    SystemClock.sleep(1000)
    onView(withId(R.id.navigationViewMain)).perform(NavigationViewActions.navigateTo(
      R.id.allSecretsToolBarDrawerMenu))
    SystemClock.sleep(1000)
    onView(withId(R.id.recyclerViewSecretList)).check(matches(not(withText(
      "App 1 name automated"))))
  }

  @Test
  fun testUpdation() {
    ActivityScenario.launch(MainActivity::class.java)
    val testData = listOf("App 1", "App 2")
    val testDataSecurityCodes = listOf("1111", "2222")
    val testDataPhoneNumbers = listOf("123", "1234")
    testData.forEachIndexed { index, element ->
      createSecret(index, element, testDataSecurityCodes, testDataPhoneNumbers)
    }
    updateSecret()
  }

  private fun updateSecret() {
    val name = "App 2"

    // Click the second item in the recycler view list
    onView(withId(R.id.recyclerViewSecretList)).perform(RecyclerViewActions
      .actionOnItemAtPosition<SecretListRVAdapter.SecretAdapterViewHolder>(1, click()))
    // UPDATE A SECRET
    onView(withId(R.id.materialCardViewNameAndDescriptionSecretInfoBottomDialog)).perform(
      swipeUp())
    SystemClock.sleep(2000)
    UpdateSecret.updateNameAndDescription(name)
    UpdateSecret.updateNote(name)
    UpdateSecret.updateUsernameAndPassword(name)

    smartLockAppears()

    UpdateSecret.addSecurityCode(name)
    UpdateSecret.addAssociatedEmailAccount(name)
    UpdateSecret.addAssociatedPhoneNumber()
    UpdateSecret.updateExtraInfo(name)
  }

  @Test
  fun testCreation() {
    ActivityScenario.launch(MainActivity::class.java)
    val testData = listOf("App 1", "App 2", "App 3")
    val testDataSecurityCodes = listOf("1111", "2222", "3333")
    val testDataPhoneNumbers = listOf("123", "1234", "12345")

    testData.forEachIndexed { index, element ->
      createSecret(index, element, testDataSecurityCodes, testDataPhoneNumbers)
    }
  }

  private fun createSecret(
    index: Int, element: String, testDataSecurityCodes: List<String>,
    testDataPhoneNumbers: List<String>
  ) {
    // Click the floating action button
    onView(withId(R.id.floatingActionButtonSecretList)).perform(click())
    // CREATE A SECRET
    onView(withId(R.id.materialCardViewSetNameAndDescriptionCreateSecretBottomDialog)).perform(
      swipeUp())
    SystemClock.sleep(2000)
    PopulateSecret.populateNameAndDescription(element, element)
    PopulateSecret.populateNote(element)
    PopulateSecret.populateUsernameAndPassword(element, element)

    smartLockAppears()

    PopulateSecret.populateSecurityCodes(testDataSecurityCodes[index])
    PopulateSecret.populateAssociatedAccounts(element)
    PopulateSecret.populateAssociatedPhoneNumbers(testDataPhoneNumbers[index])
    PopulateSecret.populateExtraInfo()

    // If error occurs, this is probably because the screen is small. To fix the error, uncomment
    // the code below
    // onView(withId(R.id.materialCardViewAddPhoneNumberCreateSecretBottomDialog)).perform(swipeUp())

    SystemClock.sleep(1500)
    onView(withId(R.id.buttonCreateSecretBottomDialog)).perform(click())
    SystemClock.sleep(1500)
    onView(withText("$element name automated")).check(matches(isDisplayed()))
  }

  private fun smartLockAppears() {
    SystemClock.sleep(1000)
    // Android smart lock will appear
    val uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
    val currentPackageName = uiDevice.currentPackageName
    if (currentPackageName != "com.johndeweydev.debug.xecret") {
      uiDevice.pressBack()
    }
  }
}