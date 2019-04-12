
import EspressoMatchers.hasCheckedItem
import EspressoMatchers.isMostlyVisible
import EspressoViewActions.bottomNavigateTo
import EspressoViewActions.waitForView
import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.mitteloupe.randomgenktexample.R
import com.mitteloupe.randomgenktexample.ui.main.MainActivity
import org.hamcrest.CoreMatchers.allOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by Eran Boudjnah on 02/11/2018.
 */
@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun whenLaunchThenInstructionsAreVisible() {
        // When I launch the activity

        // Then
        iSeeTheInstructionsView()
    }

    @Test
    fun whenLaunchThenInvisibleNavigationItemSelected() {
        // When I launch the activity

        // Then
        iSeeNoNavigationItemSelected()
    }

    @Test
    fun whenNavigateToPersonThenPersonViewDisplayed() {
        // When
        iSelectANavigationItem(R.id.action_person)

        // Then
        iSeeAViewWithinThePager(R.id.person_view)
    }

    @Test
    fun whenNavigateToPlanetarySystemThenPlanetarySystemViewDisplayed() {
        // When
        iSelectANavigationItem(R.id.action_planetary_system)

        // Then
        iSeeAViewWithinThePager(R.id.planetary_system_view)
    }

    @Test
    fun whenNavigateToFlatThenFlatViewDisplayed() {
        // When
        iSelectANavigationItem(R.id.action_flat)

        // Then
        iSeeAViewWithinThePager(R.id.flat_view)
    }

    private fun iSeeTheInstructionsView() {
        onView(withText("Choose a data type below to generate a random instance.")).check(matches(isDisplayed()))
    }

    private fun iSeeNoNavigationItemSelected() {
        onView(withId(R.id.bottom_navigation)).check(matches(hasCheckedItem(R.id.action_none)))
    }

    private fun iSelectANavigationItem(menuItemId: Int) {
        onView(withId(R.id.bottom_navigation)).perform(bottomNavigateTo(menuItemId))
    }

    private fun iSeeAViewWithinThePager(viewId: Int) {
        onView(withId(R.id.content_container)).perform(waitForView(allOf<View>(withId(viewId), isMostlyVisible()), 1000))
    }
}