package com.mitteloupe.randomgenktexample.data.generator

import com.mitteloupe.randomgenkt.FieldDataProvider
import com.mitteloupe.randomgenkt.builder.RandomGenBuilder
import com.mitteloupe.randomgenkt.fielddataprovider.ConcatenateFieldDataProvider
import com.mitteloupe.randomgenkt.fielddataprovider.ExplicitFieldDataProvider
import com.mitteloupe.randomgenkt.fielddataprovider.ExpressionFieldDataProvider
import com.mitteloupe.randomgenkt.fielddataprovider.GenericListFieldDataProvider
import com.mitteloupe.randomgenkt.fielddataprovider.IntFieldDataProvider
import com.mitteloupe.randomgenkt.fielddataprovider.PaddedFieldDataProvider
import com.mitteloupe.randomgenktexample.data.model.person.Gender
import com.mitteloupe.randomgenktexample.data.model.person.Occupation
import com.mitteloupe.randomgenktexample.data.model.person.Person
import java.util.Random
import javax.inject.Inject

class PersonGeneratorFactory @Inject constructor() {
    val newPersonGenerator by lazy {
        val random = Random()

        RandomGenBuilder<Person>()
            .ofKotlinClass<Person>()
            .withField("gender")
            .returning<Gender>()
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
        val maleNameFieldDataProvider = GenericListFieldDataProvider<Person, String>(
            random,
            listOf("Dave", "George", "Jack", "James", "John", "Oliver", "Russel", "Steve")
        )
        val femaleNameFieldDataProvider = GenericListFieldDataProvider<Person, String>(
            random,
            listOf("Amanda", "Chloe", "Clair", "Daisy", "Jane", "Jessica", "Maya", "Sarah")
        )
        val lastNameFieldDataProvider = GenericListFieldDataProvider<Person, String>(
            random,
            listOf("Brown", "Davis", "Johnson", "Jones", "Miller", "Smith", "Williams", "Wilson")
        )
        val maleConcatenateFieldDataProvider = ConcatenateFieldDataProvider(
            maleNameFieldDataProvider,
            lastNameFieldDataProvider,
            delimiter = " "
        )
        val femaleConcatenateFieldDataProvider = ConcatenateFieldDataProvider(
            femaleNameFieldDataProvider,
            lastNameFieldDataProvider,
            delimiter = " "
        )

        return ExpressionFieldDataProvider { instance ->
            when (instance?.gender) {
                Gender.MALE -> maleConcatenateFieldDataProvider
                else -> femaleConcatenateFieldDataProvider
            }(instance)
        }
    }

    private fun newPhoneFieldDataProvider(random: Random): FieldDataProvider<Person, String> {
        val plusProvider = ExplicitFieldDataProvider<Person, String>("+")
        val phoneCountryFieldDataProvider = IntFieldDataProvider<Person>(random, 1, 99)
        val spaceProvider = ExplicitFieldDataProvider<Person, String>(" ")
        val number3DigitsProvider = getFieldDataProviderWith3Digits(random, 3, 999)
        val number5DigitsProvider = getFieldDataProviderWith3Digits(random, 5, 99999)

        return ConcatenateFieldDataProvider(
            plusProvider,
            phoneCountryFieldDataProvider,
            spaceProvider,
            number3DigitsProvider,
            spaceProvider,
            number3DigitsProvider,
            spaceProvider,
            number5DigitsProvider
        )
    }

    private fun getFieldDataProviderWith3Digits(
        random: Random,
        length: Int,
        maximumValue: Int
    ) = PaddedFieldDataProvider<Person>(length, "0", IntFieldDataProvider(random, 0, maximumValue))
}
