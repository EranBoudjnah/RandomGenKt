package com.mitteloupe.randomgenexample.generator

import com.mitteloupe.randomgen.RandomGen
import com.mitteloupe.randomgenexample.data.flat.DivisionType
import com.mitteloupe.randomgenexample.data.flat.Flat
import com.mitteloupe.randomgenexample.data.flat.Room
import com.mitteloupe.randomgenexample.data.flat.RoomType

import java.util.Random

/**
 * Created by Eran Boudjnah on 19/08/2018.
 */
class FlatGeneratorFactory(private val random: Random) {

	val newFlatGenerator: RandomGen<Flat>
		get() = RandomGen.Builder<Flat>()
			.withProvider { Flat(newRoom()) }
			.withField("rooms")
			.returning(newContainerRoomRandomGen())
			.build()

	private fun newContainerRoomRandomGen(): RandomGen<Room> {
		return RandomGen.Builder<Room>()
			.ofClass(Room::class.java)
			.onGenerate(object : RandomGen.OnGenerateCallback<Room> {
				override fun onGenerate(pGeneratedInstance: Room) {
					val divisionType = if (random.nextBoolean()) DivisionType.HORIZONTAL else DivisionType.VERTICAL
					val roomsRemaining = random.nextInt(7) + 1
					splitRooms(pGeneratedInstance, roomsRemaining - 1, divisionType)
				}
			})
			.withField("roomType")
			.returning(RoomType::class.java)
			.build()
	}

	private fun newRoomRandomGen(): RandomGen<Room> {
		return RandomGen.Builder<Room>()
			.ofClass(Room::class.java)
			.withField("roomType")
			.returning(RoomType::class.java)
			.build()
	}

	private fun newRoom(): Room {
		return Room(RoomType.LIVING_ROOM, DivisionType.NONE, 0f, null, null)
	}

	private fun splitRoomUsingRandomGen(room: Room, roomRandomGen: RandomGen<Room>, divisionType: DivisionType) {
		RandomGen.Builder<Room>()
			.withProvider(getSpecificRoomProvider(room))
			.withField("firstRoom")
			.returning(roomRandomGen)
			.withField("secondRoom")
			.returning(roomRandomGen)
			.withField("divisionType")
			.returningExplicitly<Any>(divisionType)
			.withField("divisionRatio")
			.returning(0.25f, 0.75f)
			.build()
			.generate()
	}

	private fun getSpecificRoomProvider(room: Room) = { room }

	private fun splitRooms(room: Room, roomsRemaining: Int, lastDivisionType: DivisionType) {
		if (roomsRemaining == 0) return

		val newDivisionType = if (DivisionType.HORIZONTAL === lastDivisionType) DivisionType.VERTICAL else DivisionType.HORIZONTAL
		splitRoomUsingRandomGen(room, newRoomRandomGen(), newDivisionType)

		val newRoomsRemaining = roomsRemaining - 1

		val leftRoomsRemaining = if (newRoomsRemaining != 0) random.nextInt(newRoomsRemaining + 1) else 0
		val rightRoomsRemaining = newRoomsRemaining - leftRoomsRemaining

		splitRooms(room.firstRoom!!, leftRoomsRemaining, newDivisionType)
		splitRooms(room.secondRoom!!, rightRoomsRemaining, newDivisionType)
	}
}
