package com.mitteloupe.randomgenexample.data.generator

import com.mitteloupe.randomgenkt.RandomGen
import com.mitteloupe.randomgenexample.data.model.flat.DivisionType
import com.mitteloupe.randomgenexample.data.model.flat.Flat
import com.mitteloupe.randomgenexample.data.model.flat.Room
import com.mitteloupe.randomgenexample.data.model.flat.RoomType

import java.util.Random
import javax.inject.Inject

/**
 * Created by Eran Boudjnah on 19/08/2018.
 */
class FlatGeneratorFactory
@Inject
constructor(private val random: Random) {

	val newFlatGenerator: RandomGen<Flat>
		get() = RandomGen.Builder<Flat>()
			.withProvider { Flat(newRoom()) }
			.withField("rooms")
			.returning(newContainerRoomRandomGen())
			.build()

	private fun newContainerRoomRandomGen() =
		RandomGen.Builder<Room>()
			.ofClass<Room>()
			.onGenerate(object : RandomGen.OnGenerateCallback<Room> {
				override fun onGenerate(generatedInstance: Room) {
					val divisionType = if (random.nextBoolean()) DivisionType.HORIZONTAL else DivisionType.VERTICAL
					val roomsRemaining = random.nextInt(7) + 1
					splitRooms(generatedInstance, roomsRemaining - 1, divisionType)
				}
			})
			.withField("roomType")
			.returning(RoomType::class.java)
			.build()

	private fun newRoomRandomGen() =
		RandomGen.Builder<Room>()
			.ofClass<Room>()
			.withField("roomType")
			.returning(RoomType::class.java)
			.build()

	private fun newRoom() = Room(RoomType.LIVING_ROOM, DivisionType.NONE, 0f, null, null)

	private fun splitRoomUsingRandomGen(room: Room, roomRandomGen: RandomGen<Room>, divisionType: DivisionType) =
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
