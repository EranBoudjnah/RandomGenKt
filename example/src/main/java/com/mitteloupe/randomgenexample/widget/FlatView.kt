package com.mitteloupe.randomgenexample.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.RectF
import android.util.AttributeSet
import android.widget.FrameLayout
import com.mitteloupe.randomgenexample.R
import com.mitteloupe.randomgenexample.data.model.flat.DivisionType
import com.mitteloupe.randomgenexample.data.model.flat.Flat
import com.mitteloupe.randomgenexample.data.model.flat.Room

/**
 * Created by Eran Boudjnah on 26/08/2018.
 */
class FlatView
@JvmOverloads
constructor(context: Context,
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
		canvas.drawRect(boundingWalls, paint)

		drawWallOrRoomType(canvas, flat!!.rooms, boundingWalls)
	}

	private fun setUpBackground() {
		val drawable = resources.getDrawable(android.R.drawable.dialog_holo_light_frame, null)
		drawable.colorFilter = PorterDuffColorFilter(resources.getColor(R.color.primary, null), PorterDuff.Mode.MULTIPLY)
		background = drawable
	}

	private fun setUpPaint() {
		with(paint) {
			style = Paint.Style.STROKE
			color = -0x1
			textAlign = Paint.Align.CENTER
			textSize = 24f
		}
	}

	private fun drawWallOrRoomType(canvas: Canvas, room: Room, boundingWalls: RectF) {
		if (!room.isDivided()) {
			canvas.drawText(room.formattedType(), boundingWalls.centerX(), boundingWalls.centerY(), paint)
			return
		}

		val firstInnerWalls = RectF()
		val secondInnerWalls = RectF()

		when (room.divisionType) {
			DivisionType.HORIZONTAL -> {
				room.divideHorizontally(boundingWalls, firstInnerWalls, secondInnerWalls)
				drawWallOrRoomType(canvas, room.firstRoom!!, firstInnerWalls)
				drawWallOrRoomType(canvas, room.secondRoom!!, secondInnerWalls)
				canvas.drawRect(secondInnerWalls, paint)
			}

			else -> {
				room.divideVertically(boundingWalls, firstInnerWalls, secondInnerWalls)
				drawWallOrRoomType(canvas, room.firstRoom!!, firstInnerWalls)
				drawWallOrRoomType(canvas, room.secondRoom!!, secondInnerWalls)
				canvas.drawRect(secondInnerWalls, paint)
			}
		}
	}
}

fun Room.isDivided() =
	this.firstRoom != null && this.secondRoom != null

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

fun Room.formattedType(): String {
	val type = this.roomType.toString()
	val words = type.split("_".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
	val stringBuilder = StringBuilder()
	for (i in words.indices) {
		val word = words[i]
		stringBuilder.append(word.substring(0, 1).toUpperCase())
			.append(word.substring(1).toLowerCase())
		if (i < words.size - 1) {
			stringBuilder.append(" ")
		}
	}
	return stringBuilder.toString()
}
