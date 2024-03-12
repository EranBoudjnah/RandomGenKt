package com.mitteloupe.randomgenktexample.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.mitteloupe.randomgenktexample.R
import com.mitteloupe.randomgenktexample.data.model.person.Gender
import com.mitteloupe.randomgenktexample.data.model.person.Person
import com.mitteloupe.randomgenktexample.format.StringFormatter.formatEnumValue

class PersonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private val iconFemale: View by lazy { findViewById(R.id.icon_female) }
    private val iconMale: View by lazy { findViewById(R.id.icon_male) }
    private val textAgeValue: TextView by lazy { findViewById(R.id.text_age_value) }
    private val textNameValue: TextView by lazy { findViewById(R.id.text_name_value) }
    private val textOccupationValue: TextView by lazy { findViewById(R.id.text_occupation_value) }
    private val textPhoneValue: TextView by lazy { findViewById(R.id.text_phone_value) }

    init {
        inflate(context, R.layout.view_person, this)

        val viewPadding = resources.getDimensionPixelSize(R.dimen.person_view_padding)
        setBackgroundColor(ContextCompat.getColor(context, R.color.blank))
        setPadding(viewPadding, viewPadding, viewPadding, viewPadding)
    }

    fun setPerson(person: Person) {
        with(person) {
            iconFemale.visibility = if (gender == Gender.FEMALE) View.VISIBLE else View.GONE
            iconMale.visibility = if (gender == Gender.MALE) View.VISIBLE else View.GONE

            textNameValue.text = name
            textAgeValue.text = resources.getQuantityString(R.plurals.person_age, age)
            textOccupationValue.text = formatEnumValue(occupation)
            textPhoneValue.text = context.getString(R.string.person_phone_number, phoneNumber)
        }
    }
}
