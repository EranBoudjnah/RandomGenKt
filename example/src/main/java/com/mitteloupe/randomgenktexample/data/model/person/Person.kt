package com.mitteloupe.randomgenktexample.data.model.person

/**
 * Created by Eran Boudjnah on 13/08/2018.
 */
data class Person(
    val gender: Gender,
    val name: String,
    val age: Int,
    val occupation: Occupation,
    val phoneNumber: String
)
