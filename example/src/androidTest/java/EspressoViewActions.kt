import EspressoMatchers.isMostlyVisible
import android.content.res.Resources
import android.view.Menu
import android.view.View
import androidx.test.espresso.PerformException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isDisplayingAtLeast
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.util.HumanReadables
import androidx.test.espresso.util.TreeIterables
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.Matcher
import org.hamcrest.StringDescription
import java.util.concurrent.TimeoutException

/**
 * Created by Eran Boudjnah on 02/11/2018.
 */
object EspressoViewActions {
    fun bottomNavigateTo(menuItemId: Int): ViewAction {
        return object : ViewAction {
            override fun perform(uiController: UiController, view: View) {
                val navigationView = view as BottomNavigationView
                val menu = navigationView.menu
                if (menu.findItem(menuItemId) == null) {
                    throw PerformException.Builder()
                        .withActionDescription(this.description)
                        .withViewDescription(HumanReadables.describe(view))
                        .withCause(RuntimeException(getErrorMessage(menu, view)))
                        .build()
                }
                menu.performIdentifierAction(menuItemId, 0)
            }

            private fun getErrorMessage(menu: Menu, view: View): String {
                val newLine = System.getProperty("line.separator")
                val errorMessage = StringBuilder("Menu item was not found, available menu items:")
                    .append(newLine)

                for (position in 0 until menu.size()) {
                    errorMessage.append("[MenuItem] position=").append(position)
                    menu.getItem(position)?.let { menuItem ->
                        menuItem.title?.let { itemTitle ->
                            errorMessage
                                .append(", title=")
                                .append(itemTitle)
                        }

                        view.resources?.let { resources ->
                            val itemId = menuItem.itemId
                            try {
                                errorMessage.append(", id=")
                                val menuItemResourceName = resources.getResourceName(itemId)
                                errorMessage.append(menuItemResourceName)
                            } catch (nfe: Resources.NotFoundException) {
                                errorMessage.append("not found")
                            }
                        }
                        errorMessage.append(newLine)
                    }
                }
                return errorMessage.toString()
            }

            override fun getDescription() = "click on menu item with ID"

            override fun getConstraints() = allOf(
                isAssignableFrom(BottomNavigationView::class.java),
                isMostlyVisible()
            )
        }
    }

    fun waitForView(viewMatcher: Matcher<View>, milliseconds: Long): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> = allOf(
                withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE),
                isDisplayingAtLeast(90)
            )

            override fun getDescription(): String {
                val description = StringDescription()
                description.appendText("Wait for a matching view ")
                viewMatcher.describeTo(description)
                description.appendText(" for $milliseconds milliseconds.")
                return description.toString()
            }

            override fun perform(uiController: UiController, view: View) {
                uiController.loopMainThreadUntilIdle()
                val startTime = System.currentTimeMillis()
                val endTime = startTime + milliseconds

                do {
                    if (viewFoundByMatcher(view, viewMatcher)) return

                    uiController.loopMainThreadForAtLeast(50)
                } while (System.currentTimeMillis() < endTime)

                // timeout happens
                throw PerformException.Builder()
                    .withActionDescription(description)
                    .withViewDescription(HumanReadables.describe(view))
                    .withCause(TimeoutException())
                    .build()
            }
        }
    }

    private fun viewFoundByMatcher(view: View, viewMatcher: Matcher<View>): Boolean {
        for (child in TreeIterables.breadthFirstViewTraversal(view)) {
            // found view with required ID
            if (viewMatcher.matches(child)) {
                return true
            }
        }
        return false
    }
}
