package com.mitteloupe.randomgenexample.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.RectF
import android.os.Handler
import android.os.Message
import android.text.Html
import android.text.Html.FROM_HTML_MODE_COMPACT
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.mitteloupe.randomgenexample.R
import com.mitteloupe.randomgenexample.data.planet.Material
import com.mitteloupe.randomgenexample.data.planet.Planet
import com.mitteloupe.randomgenexample.data.planet.PlanetarySystem

private const val ANIMATION_DELAY_MILLIS: Long = 40
private const val PLANET_DATA_DELAY_MILLIS: Long = 3000

private const val ASPECT_RATIO = 0.7f
private const val RING_ASPECT_RATIO = 0.5f
private const val RING_TO_STAR_RATIO = 1.4f

/**
 * Created by Eran Boudjnah on 19/08/2018.
 */
class PlanetarySystemView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {

	private val planetAnimations = mutableListOf<PlanetAnimation>()
	private var planetarySystem: PlanetarySystem? = null
	private var planetsCount = 0

	private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

	private val visualRect = RectF()
	private var visualSize = 0f
	private val visualCenter = PointF()
	private var orbitRingSpacing = 0f

	private val planetCenter = PointF()
	private var planetSize = 0f

	private val planetRingRect = RectF()

	private val renderFrame: View
	private lateinit var starAgeTextView: TextView
	private lateinit var starDiameterTextView: TextView
	private lateinit var starMassTextView: TextView
	private lateinit var starPlanetsCountTextView: TextView

	private lateinit var starPlanetNameTextView: TextView
	private lateinit var starPlanetDiameterTextView: TextView
	private lateinit var starPlanetMassTextView: TextView
	private lateinit var starPlanetOrbitalPeriodTextView: TextView
	private lateinit var starRotationPeriodTextView: TextView
	private lateinit var starMoonsTextView: TextView
	private lateinit var starRingsTextView: TextView
	private lateinit var starAtmosphereTextView: TextView

	private lateinit var animationTimeHandler: TimeHandler
	private lateinit var planetTimeHandler: TimeHandler

	private var planetDataIndex = 0

	init {
		View.inflate(context, R.layout.view_planetary_system, this)

		setWillNotDraw(false)
		setUpBackground()

		renderFrame = findViewById(R.id.render_frame)

		initStarTextViews()

		initPlanetTextViews()

		initTimers()
	}

	private fun setUpBackground() {
		val drawable = resources.getDrawable(android.R.drawable.dialog_holo_light_frame, null)
		drawable.colorFilter = PorterDuffColorFilter(resources.getColor(R.color.colorPrimary, null), PorterDuff.Mode.MULTIPLY)
		background = drawable
	}

	private fun initTimers() {
		animationTimeHandler = TimeHandler(ANIMATION_DELAY_MILLIS, Runnable { invalidate() })
		animationTimeHandler.start()

		planetTimeHandler = TimeHandler(PLANET_DATA_DELAY_MILLIS, Runnable {
			planetDataIndex = (planetDataIndex + 1) % planetarySystem!!.planets.size
			populatePlanetTextViews(planetarySystem!!.planets[planetDataIndex], planetDataIndex + 1)
		})
	}

	private fun initStarTextViews() {
		starAgeTextView = findViewById(R.id.text_star_age_value)
		starDiameterTextView = findViewById(R.id.text_star_diameter_value)
		starMassTextView = findViewById(R.id.text_star_mass_value)
		starPlanetsCountTextView = findViewById(R.id.text_planets_count_value)
	}

	private fun initPlanetTextViews() {
		starPlanetNameTextView = findViewById(R.id.text_planet_name_value)
		starPlanetDiameterTextView = findViewById(R.id.text_planet_diameter_value)
		starPlanetMassTextView = findViewById(R.id.text_planet_mass_value)
		starPlanetOrbitalPeriodTextView = findViewById(R.id.text_planet_orbital_period_value)
		starRotationPeriodTextView = findViewById(R.id.text_planet_rotation_period_value)
		starMoonsTextView = findViewById(R.id.text_planet_moons_value)
		starRingsTextView = findViewById(R.id.text_planet_rings_value)
		starAtmosphereTextView = findViewById(R.id.text_planet_atmosphere_value)
	}

	override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
		super.onLayout(changed, left, top, right, bottom)

		visualRect.set(renderFrame.left.toFloat(), renderFrame.top.toFloat(), renderFrame.right.toFloat(), renderFrame.bottom.toFloat())

		val widthX = visualRect.right - visualRect.left
		val heightX = visualRect.bottom - visualRect.top
		visualSize = Math.max(widthX, heightX)
		visualCenter.set(
			(visualRect.left + visualRect.right) / 2f,
			(visualRect.top + visualRect.bottom) / 2f
		)
	}

	fun setPlanetarySystem(pPlanetarySystem: PlanetarySystem) {
		planetarySystem = pPlanetarySystem
		val planets = planetarySystem!!.planets
		planetsCount = planets.size
		orbitRingSpacing = 0.35f / planetsCount.toFloat()

		setPlanetAnimations(planets)

		populateStarTextViews(pPlanetarySystem)

		planetTimeHandler.stop()

		if (pPlanetarySystem.planets.isNotEmpty()) {
			showPlanetTextViews()
			planetDataIndex = -1
			planetTimeHandler.start()

		} else {
			hidePlanetTextViews()
		}

		invalidate()
	}

	private fun setPlanetAnimations(pPlanets: Array<Planet>) {
		planetAnimations.clear()

		repeat(planetsCount) { index ->
			planetAnimations.add(getPlanetAnimation(pPlanets[index]))
		}
	}

	private fun getPlanetAnimation(pPlanet: Planet): PlanetAnimation {
		val planetAnimation = PlanetAnimation()
		planetAnimation.angle = Math.random().toFloat() * 360f
		planetAnimation.velocity = 25f / pPlanet.orbitalPeriodYears

		// We need some fixed rule to determine the direction of rotation. This will do for a demo.
		if (pPlanet.moons % 2 == 0) planetAnimation.velocity = -planetAnimation.velocity
		return planetAnimation
	}

	private fun populateStarTextViews(pPlanetarySystem: PlanetarySystem) {
		val resources = resources

		starAgeTextView.text = resources.getString(R.string.star_age_value, pPlanetarySystem.starAgeBillionYears)
		starDiameterTextView.text = resources.getString(R.string.star_diameter_value, pPlanetarySystem.starDiameterSunRadii)
		starMassTextView.text = resources.getString(R.string.star_mass_value, pPlanetarySystem.starSolarMass)
		starPlanetsCountTextView.text = pPlanetarySystem.planets.size.toString()
	}

	private fun hidePlanetTextViews() {
		starPlanetNameTextView.visibility = View.INVISIBLE
		starPlanetDiameterTextView.visibility = View.INVISIBLE
		starPlanetMassTextView.visibility = View.INVISIBLE
		starPlanetOrbitalPeriodTextView.visibility = View.INVISIBLE
		starRotationPeriodTextView.visibility = View.INVISIBLE
		starMoonsTextView.visibility = View.INVISIBLE
		starRingsTextView.visibility = View.INVISIBLE
		starAtmosphereTextView.visibility = View.INVISIBLE
	}

	private fun showPlanetTextViews() {
		starPlanetNameTextView.visibility = View.VISIBLE
		starPlanetDiameterTextView.visibility = View.VISIBLE
		starPlanetMassTextView.visibility = View.VISIBLE
		starPlanetOrbitalPeriodTextView.visibility = View.VISIBLE
		starRotationPeriodTextView.visibility = View.VISIBLE
		starMoonsTextView.visibility = View.VISIBLE
		starRingsTextView.visibility = View.VISIBLE
		starAtmosphereTextView.visibility = View.VISIBLE
	}

	private fun populatePlanetTextViews(planet: Planet, position: Int) {
		val resources = resources

		starPlanetNameTextView.text = resources.getString(R.string.planet_name, position)
		starPlanetDiameterTextView.text = resources.getString(R.string.planet_diameter_value, planet.diameterEarthRatio)
		starPlanetMassTextView.text = resources.getString(R.string.planet_mass_value, planet.solarMass)
		starPlanetOrbitalPeriodTextView.text = resources.getString(R.string.planet_orbital_period_value, planet.orbitalPeriodYears)
		starRotationPeriodTextView.text = resources.getString(R.string.planet_rotation_period_value, planet.rotationPeriodDays)
		starMoonsTextView.text = planet.moons.toString()
		starRingsTextView.text = if (planet.hasRings) resources.getString(R.string.planet_has_rings) else resources.getString(R.string.planet_no_rings)
		starAtmosphereTextView.text = Html.fromHtml(getMaterialsFormatted(planet.atmosphere), FROM_HTML_MODE_COMPACT)
	}

	private fun getMaterialsFormatted(pMaterials: List<Material>): String {
		if (pMaterials.isEmpty()) {
			return "N/A"
		}

		val stringBuilder = StringBuilder()
		for (material in pMaterials) {
			stringBuilder
				.append(getMaterialFormatted(material))
				.append("<br/>")
		}
		return stringBuilder.toString()
	}

	private fun getMaterialFormatted(material: Material): String {
		val stringBuilder = StringBuilder()
		for (elementCompound in material.compound) {
			stringBuilder
				.append(elementCompound.first)
			if (elementCompound.second != 1) {
				stringBuilder
					.append("<small><sub>")
					.append(elementCompound.second)
					.append("</sub></small>")
			}
		}
		return stringBuilder.toString()
	}

	override fun onDraw(pCanvas: Canvas) {
		super.onDraw(pCanvas)

		if (planetarySystem == null) return

		drawOrbitRings(pCanvas)

		drawStar(pCanvas)

		advanceAllPlanetAnimations()

		drawPlanets(pCanvas)
	}

	private fun drawStar(canvas: Canvas) {
		drawCircle(canvas, visualCenter, planetarySystem!!.starDiameterSunRadii.toFloat() / 10f, -0x1)
	}

	private fun drawPlanets(canvas: Canvas) {
		repeat(planetsCount) { index ->
			drawPlanetAtIndex(canvas, index)
		}
	}

	private fun drawOrbitRings(canvas: Canvas) {
		with(paint) {
			style = Paint.Style.STROKE
			strokeWidth = 0f
			color = 0x7fffffff
		}

		repeat(planetsCount) { index ->
			drawOrbitRing(canvas, index)
		}
	}

	private fun drawOrbitRing(pCanvas: Canvas, pPosition: Int) {
		val relativeRadius = 0.1f + orbitRingSpacing * pPosition
		val orbitRingWidth = visualSize * relativeRadius
		val orbitRingHeight = orbitRingWidth * ASPECT_RATIO
		planetRingRect.set(
			visualCenter.x - orbitRingWidth, visualCenter.y - orbitRingHeight,
			visualCenter.x + orbitRingWidth, visualCenter.y + orbitRingHeight
		)
		pCanvas.drawArc(planetRingRect, 0f, 360f, false, paint)
	}

	private fun drawPlanetAtIndex(canvas: Canvas, pIndex: Int) {
		updatePlanetCenter(pIndex)

		val planet = planetarySystem!!.planets[pIndex]

		planetSize = planet.diameterEarthRatio * 1.5f

		drawCircle(canvas, planetCenter, planetSize, if (pIndex == planetDataIndex) -0x1 else -0x5f4f01)

		if (planet.hasRings) {
			drawPlanetRing(canvas)
		}
	}

	private fun drawCircle(canvas: Canvas, mCenter: PointF, pSize: Float, pColor: Int) {
		paint.style = Paint.Style.FILL
		paint.color = pColor

		canvas.drawCircle(mCenter.x, mCenter.y, pSize, paint)
	}

	private fun updatePlanetCenter(pPosition: Int) {
		val planetAnimation = planetAnimations[pPosition]
		val relativeRadius = 0.1f + orbitRingSpacing * pPosition
		val actualRadius = visualSize * relativeRadius
		planetCenter.set(
			Math.sin((planetAnimation.angle / 180f * 3.14f).toDouble()).toFloat() * actualRadius,
			(-Math.cos((planetAnimation.angle / 180f * 3.14f).toDouble())).toFloat() * actualRadius * ASPECT_RATIO
		)
		planetCenter.offset(visualCenter.x, visualCenter.y)
	}

	private fun advanceAllPlanetAnimations() {
		for (i in 0 until planetsCount) {
			advancePlanetAnimation(i)
		}
	}

	private fun advancePlanetAnimation(pPosition: Int) {
		val planetAnimation = planetAnimations[pPosition]
		planetAnimation.angle += planetAnimation.velocity
		while (planetAnimation.angle >= 360) planetAnimation.angle -= 360f
		while (planetAnimation.angle < 0) planetAnimation.angle += 360f
	}

	private fun drawPlanetRing(canvas: Canvas) {
		val ringWidth = planetSize * RING_TO_STAR_RATIO
		val ringHeight = ringWidth * RING_ASPECT_RATIO

		planetRingRect.set(
			planetCenter.x - ringWidth, planetCenter.y - ringHeight,
			planetCenter.x + ringWidth, planetCenter.y + ringHeight
		)

		paint.strokeWidth = planetSize * 0.25f
		paint.style = Paint.Style.STROKE

		canvas.drawArc(planetRingRect, 0f, 360f, false, paint)
	}

	override fun onAttachedToWindow() {
		super.onAttachedToWindow()

		animationTimeHandler.start()
	}

	override fun onDetachedFromWindow() {
		super.onDetachedFromWindow()

		animationTimeHandler.stop()
	}

	private class TimeHandler
	internal constructor(
		private val delayMilliseconds: Long,
		private val runnable: Runnable
	) : Handler() {
		private var message: Message? = null

		override fun handleMessage(msg: Message) {
			message = Message.obtain()
			sendMessageDelayed(message, delayMilliseconds)
			runnable.run()
		}

		internal fun start() {
			if (message != null) return

			message = Message.obtain()
			sendMessage(message)
		}

		internal fun stop() {
			if (message == null) return

			val what = message?.what
			what?.let { removeMessages(it) }
			message = null
		}
	}

	private data class PlanetAnimation(
		var velocity: Float = 0f,
		var angle: Float = 0f
	)
}
