package com.mitteloupe.randomgenexample.generator

import com.mitteloupe.randomgen.FieldDataProvider
import com.mitteloupe.randomgen.RandomGen
import com.mitteloupe.randomgen.fielddataprovider.ConcatenateFieldDataProvider
import com.mitteloupe.randomgen.fielddataprovider.ExplicitFieldDataProvider
import com.mitteloupe.randomgen.fielddataprovider.GenericListFieldDataProvider
import com.mitteloupe.randomgen.fielddataprovider.IntFieldDataProvider
import com.mitteloupe.randomgen.fielddataprovider.PaddedFieldDataProvider
import com.mitteloupe.randomgenexample.data.person.Gender
import com.mitteloupe.randomgenexample.data.person.Occupation
import com.mitteloupe.randomgenexample.data.person.Person
import java.util.Random

/**
 * Created by Eran Boudjnah on 28/08/2018.
 */
class PersonGeneratorFactory {
	val newPersonGenerator: RandomGen<Person>
		get() {
			val random = Random()

			return RandomGen.Builder<Person>()
				.ofClass(Person::class.java)
				.withField("gender")
				.returning(Gender::class.java)
				.withField("name")
				.returning(newFullNameFieldDataProvider(random))
				.withField("age")
				.returning(18, 70)
				.withField("occupation")
				.returning(Occupation::class.java)
				.withField("phoneNumber")
				.returning(newPhoneFieldDataProvider(random))
				.build()
		}

	private fun newFullNameFieldDataProvider(random: Random): FieldDataProvider<Person, String> {
		val maleNameFieldDataProvider = GenericListFieldDataProvider<Person, String>(random, listOf("Dave", "George", "Jack", "James", "John", "Oliver", "Russel", "Steve"))
		val femaleNameFieldDataProvider = GenericListFieldDataProvider<Person, String>(random, listOf("Amanda", "Chloe", "Clair", "Daisy", "Jane", "Jessica", "Maya", "Sarah"))
		val lastNameFieldDataProvider = GenericListFieldDataProvider<Person, String>(random, listOf("Brown", "Davis", "Johnson", "Jones", "Miller", "Smith", "Williams", "Wilson"))
		val maleConcatenateFieldDataProvider = ConcatenateFieldDataProvider(maleNameFieldDataProvider, lastNameFieldDataProvider, delimiter = " ")
		val femaleConcatenateFieldDataProvider = ConcatenateFieldDataProvider(femaleNameFieldDataProvider, lastNameFieldDataProvider, delimiter = " ")

		return object : FieldDataProvider<Person, String> {
			override fun generate(instance: Person?): String {
				return if (instance?.gender == Gender.MALE) {
					maleConcatenateFieldDataProvider.generate(instance)

				} else {
					femaleConcatenateFieldDataProvider.generate(instance)
				}
			}
		}
	}

	private fun newPhoneFieldDataProvider(random: Random): FieldDataProvider<Person, String> {

		val plusProvider = ExplicitFieldDataProvider<Person, String>("+")
		val phoneCountryFieldDataProvider = IntFieldDataProvider<Person>(random, 1, 99)
		val spaceProvider = ExplicitFieldDataProvider<Person, String>(" ")
		val number3DigitsProvider = getFieldDataProviderWith3Digits(random, 3, 999)
		val number5DigitsProvider = getFieldDataProviderWith3Digits(random, 5, 99999)

		return ConcatenateFieldDataProvider(
			plusProvider, phoneCountryFieldDataProvider,
			spaceProvider, number3DigitsProvider,
			spaceProvider, number3DigitsProvider,
			spaceProvider, number5DigitsProvider
		)
	}

	private fun getFieldDataProviderWith3Digits(
		random: Random,
		length: Int,
		maximumValue: Int
	): PaddedFieldDataProvider<Person> {
		return PaddedFieldDataProvider(length, "0",
			IntFieldDataProvider(random, 0, maximumValue))
	}
}
