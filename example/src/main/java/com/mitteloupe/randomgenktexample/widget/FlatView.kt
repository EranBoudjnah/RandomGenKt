package com.mitteloupe.randomgenktexample.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.widget.FrameLayout
import com.mitteloupe.randomgenktexample.R
import com.mitteloupe.randomgenktexample.data.model.flat.DivisionType
import com.mitteloupe.randomgenktexample.data.model.flat.Flat
import com.mitteloupe.randomgenktexample.data.model.flat.Room
import com.mitteloupe.randomgenktexample.utils.StringFormatter.formatEnumValue

/**
 * Created by Eran Boudjnah on 26/08/2018.
 */
class FlatView
@JvmOverloads
constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private var flat: Flat? = null

    private val paint = Paint()
    private val boundingWalls = RectF()

    init {
        setWillNotDraw(false)
        setUpBackground()
        setUpPaint()
    }

    fun setFlat(flat: Flat) {
        this.flat = flat

        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val flatWidth = (width - 100).toFloat()
        val flatHeight = (height - 100).toFloat()
        boundingWalls.set(50f, 50f, flatWidth + 50, flatHeight + 50)
        drawWall(canvas, boundingWalls)

        drawWallOrRoomType(canvas, flat!!.rooms, boundingWalls)
    }

    private fun setUpBackground() {
        background = ColorDrawable(resources.getColor(R.color.primary, null))
    }

    private fun setUpPaint() = with(paint) {
        color = -0x1
        textAlign = Paint.Align.CENTER
        textSize = 24f
    }

    private fun drawWallOrRoomType(canvas: Canvas, room: Room, boundingWalls: RectF) {
        if (!room.isDivided()) {
            writeRoomType(canvas, room, boundingWalls)
            return
        }

        val firstInnerWalls = RectF()
        val secondInnerWalls = RectF()

        when (room.divisionType) {
            DivisionType.HORIZONTAL -> {
                room.divideHorizontally(boundingWalls, firstInnerWalls, secondInnerWalls)
                drawWallOrRoomType(canvas, room.firstRoom!!, firstInnerWalls)
                drawWallOrRoomType(canvas, room.secondRoom!!, secondInnerWalls)
                drawWall(canvas, secondInnerWalls)
            }

            else -> {
                room.divideVertically(boundingWalls, firstInnerWalls, secondInnerWalls)
                drawWallOrRoomType(canvas, room.firstRoom!!, firstInnerWalls)
                drawWallOrRoomType(canvas, room.secondRoom!!, secondInnerWalls)
                drawWall(canvas, secondInnerWalls)
            }
        }
    }

    private fun writeRoomType(canvas: Canvas, room: Room, boundingWalls: RectF) {
        paint.style = Paint.Style.FILL
        canvas.drawText(
            formatEnumValue(room.roomType),
            boundingWalls.centerX(),
            boundingWalls.centerY(),
            paint
        )
    }

    private fun drawWall(canvas: Canvas, wall: RectF) {
        paint.style = Paint.Style.STROKE
        canvas.drawRect(wall, paint)
    }
}

fun Room.isDivided() = this.firstRoom != null && this.secondRoom != null

fun Room.divideHorizontally(boundingWalls: RectF, firstInnerWalls: RectF, secondInnerWalls: RectF) {
    val totalHeight = boundingWalls.height()
    with(firstInnerWalls) {
        left = boundingWalls.left
        right = boundingWalls.right
        top = boundingWalls.top
        bottom = boundingWalls.top + totalHeight * this@divideHorizontally.divisionRatio
    }

    with(secondInnerWalls) {
        left = firstInnerWalls.left
        right = firstInnerWalls.right
        top = firstInnerWalls.bottom
        bottom = boundingWalls.bottom
    }
}

fun Room.divideVertically(boundingWalls: RectF, firstInnerWalls: RectF, secondInnerWalls: RectF) {
    val totalWidth = boundingWalls.width()
    with(firstInnerWalls) {
        top = boundingWalls.top
        bottom = boundingWalls.bottom
        left = boundingWalls.left
        right = boundingWalls.left + totalWidth * this@divideVertically.divisionRatio
    }

    with(secondInnerWalls) {
        top = firstInnerWalls.top
        bottom = firstInnerWalls.bottom
        left = firstInnerWalls.right
        right = boundingWalls.right
    }
}
