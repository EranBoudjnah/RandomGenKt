package com.mitteloupe.randomgenktexample.data.generator

import com.mitteloupe.randomgenkt.RandomGen
import com.mitteloupe.randomgenkt.builder.RandomGenBuilder
import com.mitteloupe.randomgenkt.model.DataProviderMap
import com.mitteloupe.randomgenktexample.data.model.flat.DivisionType
import com.mitteloupe.randomgenktexample.data.model.flat.Flat
import com.mitteloupe.randomgenktexample.data.model.flat.Room
import com.mitteloupe.randomgenktexample.data.model.flat.RoomType
import java.util.Random
import javax.inject.Inject

class FlatGeneratorFactory @Inject constructor(private val random: Random) {
    val newFlatGenerator by lazy {
        RandomGenBuilder<Flat>()
            .withProvider { Flat(newRoom()) }
            .withField("rooms")
            .returning(newContainerRoomRandomGen())
            .build()
    }

    private fun newContainerRoomRandomGen() = RandomGenBuilder<Room>()
        .ofKotlinClass<Room>()
        .onGenerate { generatedInstance ->
            val roomsRemaining = random.nextInt(7) + 1
            splitRooms(generatedInstance, roomsRemaining - 1, generatedInstance.divisionType)
        }
        .withField("divisionType")
        .returning(
            listOf(
                DivisionType.HORIZONTAL,
                DivisionType.VERTICAL
            )
        )
        .withField("roomType")
        .returning(RoomType::class.java)
        .build()

    private fun newRoomRandomGen() = RandomGenBuilder<Room>()
        .ofKotlinClass<Room>()
        .withField("roomType")
        .returning(RoomType::class)
        .build()

    private fun newRoom() = Room(RoomType.LIVING_ROOM, 0f, null, null)

    private fun splitRoomUsingRandomGen(
        room: Room,
        roomRandomGen: RandomGen<Room>,
        divisionType: DivisionType
    ) = RandomGenBuilder<Room>()
        .withProvider(getSpecificRoomProvider(room))
        .withField("firstRoom")
        .returning(roomRandomGen)
        .withField("secondRoom")
        .returning(roomRandomGen)
        .withField("divisionType")
        .returningExplicitly<Any>(divisionType)
        .withField("divisionRatio")
        .returning(0.25f, 0.75f)
        .build()()

    private fun getSpecificRoomProvider(
        room: Room
    ): (dataProviders: DataProviderMap<Room>) -> Room = { room }

    private fun splitRooms(room: Room, roomsRemaining: Int, lastDivisionType: DivisionType) {
        if (roomsRemaining == 0) return

        val newDivisionType = if (DivisionType.HORIZONTAL === lastDivisionType) {
            DivisionType.VERTICAL
        } else {
            DivisionType.HORIZONTAL
        }
        splitRoomUsingRandomGen(room, newRoomRandomGen(), newDivisionType)

        val newRoomsRemaining = roomsRemaining - 1

        val leftRoomsRemaining =
            if (newRoomsRemaining != 0) random.nextInt(newRoomsRemaining + 1) else 0
        val rightRoomsRemaining = newRoomsRemaining - leftRoomsRemaining

        splitRooms(room.firstRoom!!, leftRoomsRemaining, newDivisionType)
        splitRooms(room.secondRoom!!, rightRoomsRemaining, newDivisionType)
    }
}
