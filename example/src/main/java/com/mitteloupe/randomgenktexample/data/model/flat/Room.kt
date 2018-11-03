package com.mitteloupe.randomgenktexample.data.model.flat

/**
 * Created by Eran Boudjnah on 26/08/2018.
 */
data class Room(
	val roomType: RoomType,
	val divisionType: DivisionType,
	val divisionRatio: Float,
	val firstRoom: Room?,
	val secondRoom: Room?
)
