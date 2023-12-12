package com.mitteloupe.randomgenktexample.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.mitteloupe.randomgenktexample.R
import com.mitteloupe.randomgenktexample.data.model.person.Gender
import com.mitteloupe.randomgenktexample.data.model.person.Person
import com.mitteloupe.randomgenktexample.format.StringFormatter.formatEnumValue

class PersonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private val contentWrapper: View by lazy { findViewById(R.id.content_wrapper) }
    private val iconFemale: View by lazy { findViewById(R.id.icon_female) }
    private val iconMale: View by lazy { findViewById(R.id.icon_male) }
    private val textAgeValue: TextView by lazy { findViewById(R.id.text_age_value) }
    private val textNameValue: TextView by lazy { findViewById(R.id.text_name_value) }
    private val textOccupationValue: TextView by lazy { findViewById(R.id.text_occupation_value) }
    private val textPhoneValue: TextView by lazy { findViewById(R.id.text_phone_value) }

    private val maleColor: Int by lazy { resources.getColor(R.color.male, null) }
    private val femaleColor: Int by lazy { resources.getColor(R.color.female, null) }

    init {
        inflate(context, R.layout.view_person, this)
    }

    fun setPerson(person: Person) {
        with(person) {
            iconFemale.visibility = if (gender == Gender.FEMALE) View.VISIBLE else View.GONE
            iconMale.visibility = if (gender == Gender.MALE) View.VISIBLE else View.GONE

            contentWrapper.setBackgroundColor(if (gender == Gender.MALE) maleColor else femaleColor)

            textNameValue.text = name
            textAgeValue.text = age.toString()
            textOccupationValue.text = formatEnumValue(occupation)
            textPhoneValue.text = phoneNumber
        }
    }
}
