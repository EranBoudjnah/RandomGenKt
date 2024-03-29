import android.view.View
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.Visibility
import androidx.test.espresso.matcher.ViewMatchers.isDisplayingAtLeast
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.Description
import org.hamcrest.Matcher

object EspressoMatchers {
    /**
     * Checks that a [BottomNavigationView] contains an item with provided id and that it is
     * checked.
     *
     * @param id of the item to find in the [BottomNavigationView]
     * @return a matcher that returns true if the item was found and checked, false otherwise
     */
    fun hasCheckedItem(id: Int): Matcher<View> =
        object : BoundedMatcher<View, BottomNavigationView>(
            BottomNavigationView::class.java
        ) {
            private var checkedIds: MutableSet<Int> = HashSet()
            private var itemFound = false
            private var triedMatching = false

            override fun matchesSafely(navigationView: BottomNavigationView): Boolean {
                triedMatching = true

                val menu = navigationView.menu
                for (i in 0 until menu.size()) {
                    val item = menu.getItem(i)
                    if (item.isChecked) {
                        checkedIds.add(item.itemId)
                    }
                    if (item.itemId == id) {
                        itemFound = true
                    }
                }
                return checkedIds.contains(id)
            }

            override fun describeTo(description: Description) {
                if (!triedMatching) {
                    description.appendText("BottomNavigationView")
                    return
                }

                description
                    .appendText("BottomNavigationView to have a checked item with id=")
                    .appendValue(id)

                if (itemFound) {
                    description
                        .appendText(", but selection was=")
                        .appendValue(checkedIds)
                } else {
                    description
                        .appendText(", but it doesn't have an item with such an id")
                }
            }
        }

    fun isMostlyVisible(): Matcher<View> = allOf(
        withEffectiveVisibility(Visibility.VISIBLE),
        isDisplayingAtLeast(90)
    )
}
