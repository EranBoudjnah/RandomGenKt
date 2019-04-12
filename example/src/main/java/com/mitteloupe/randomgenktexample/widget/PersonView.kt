package com.mitteloupe.randomgenktexample.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.mitteloupe.randomgenktexample.R
import com.mitteloupe.randomgenktexample.data.model.person.Gender
import com.mitteloupe.randomgenktexample.data.model.person.Person
import com.mitteloupe.randomgenktexample.utils.StringFormatter.formatEnumValue
import kotlinx.android.synthetic.main.view_person.view.content_wrapper as contentWrapper
import kotlinx.android.synthetic.main.view_person.view.icon_female as iconFemale
import kotlinx.android.synthetic.main.view_person.view.icon_male as iconMale
import kotlinx.android.synthetic.main.view_person.view.text_age_value as textAgeValue
import kotlinx.android.synthetic.main.view_person.view.text_name_value as textNameValue
import kotlinx.android.synthetic.main.view_person.view.text_occupation_value as textOccupationValue
import kotlinx.android.synthetic.main.view_person.view.text_phone_value as textPhoneValue

/**
 * Created by Eran Boudjnah on 18/08/2018.
 */
class PersonView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {
    private val maleColor: Int by lazy { resources.getColor(R.color.male, null) }
    private val femaleColor: Int by lazy { resources.getColor(R.color.female, null) }

    init {
        View.inflate(context, R.layout.view_person, this)
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
