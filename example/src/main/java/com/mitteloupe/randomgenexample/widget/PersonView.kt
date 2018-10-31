package com.mitteloupe.randomgenexample.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.mitteloupe.randomgenexample.R
import com.mitteloupe.randomgenexample.data.model.person.Gender
import com.mitteloupe.randomgenexample.data.model.person.Person

/**
 * Created by Eran Boudjnah on 18/08/2018.
 */
class PersonView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {
	private val iconFemaleView: View
	private val iconMaleView: View
	private val nameView: TextView
	private val ageView: TextView
	private val occupationView: TextView
	private val phoneNumberView: TextView

	init {
		View.inflate(context, R.layout.view_person, this)

		iconFemaleView = findViewById(R.id.icon_female)
		iconMaleView = findViewById(R.id.icon_male)
		nameView = findViewById(R.id.text_name_value)
		ageView = findViewById(R.id.text_age_value)
		occupationView = findViewById(R.id.text_occupation_value)
		phoneNumberView = findViewById(R.id.text_phone_value)
	}

	fun setPerson(person: Person) {
		with (person) {
			iconFemaleView.visibility = if (gender == Gender.FEMALE) View.VISIBLE else View.GONE
			iconMaleView.visibility = if (gender == Gender.MALE) View.VISIBLE else View.GONE

			nameView.text = name
			ageView.text = age.toString()
			occupationView.text = formattedOccupation()
			phoneNumberView.text = phoneNumber
		}
	}
}

private fun Person.formattedOccupation(): String {
	val words = this.occupation.toString()
		.split("_".toRegex())
		.dropLastWhile { it.isEmpty() }
		.toTypedArray()
	val stringBuilder = StringBuilder()
	words.forEachIndexed {
		index, word ->
		stringBuilder
			.append(word.first().toUpperCase())
			.append(word.substring(1).toLowerCase())

		if (index < words.size - 1) {
			stringBuilder.append(" ")
		}
	}

	return stringBuilder.toString()
}
