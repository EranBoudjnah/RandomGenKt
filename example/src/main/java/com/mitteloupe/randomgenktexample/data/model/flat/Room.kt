package com.mitteloupe.randomgenktexample.data.model.flat

data class Room(
    val roomType: RoomType,
    val divisionRatio: Float,
    val firstRoom: Room?,
    val secondRoom: Room?
) {
    val divisionType: DivisionType = DivisionType.NONE
}
