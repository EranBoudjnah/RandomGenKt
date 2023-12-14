package com.mitteloupe.randomgenktexample.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.Rect
import android.graphics.RectF
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.text.Html
import android.text.Html.FROM_HTML_MODE_COMPACT
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.mitteloupe.randomgenktexample.R
import com.mitteloupe.randomgenktexample.data.model.planet.Material
import com.mitteloupe.randomgenktexample.data.model.planet.Planet
import com.mitteloupe.randomgenktexample.data.model.planet.PlanetarySystem
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.sin

private const val ANIMATION_DELAY_MILLIS: Long = 40
private const val PLANET_DATA_DELAY_MILLIS: Long = 3000

private const val ASPECT_RATIO = 0.7f
private const val RING_ASPECT_RATIO = 0.5f
private const val RING_TO_STAR_RATIO = 1.4f

private const val RADIANS_TO_DEGREES = Math.PI.toFloat() / 180f

class PlanetarySystemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private val planetAnimations = mutableListOf<PlanetAnimation>()
    private lateinit var planetarySystem: PlanetarySystem
    private var planetsCount = 0

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var visualSize = 0f
    private val visualCenter = PointF()
    private var orbitRingSpacing = 0f

    private val planetCenter = PointF()
    private var planetSize = 0f

    private val planetRingRect = RectF()

    private val backgroundColor: Int by lazy { resources.getColor(R.color.primary, null) }

    private val renderFrame: View
    private val renderFrameBounds = Rect()
    private val renderBounds = RectF()

    private val starAgeTextView: TextView by lazy { findViewById(R.id.text_star_age_value) }
    private val starDiameterTextView: TextView by lazy {
        findViewById(R.id.text_star_diameter_value)
    }
    private val starMassTextView: TextView by lazy { findViewById(R.id.text_star_mass_value) }
    private val starPlanetsCountTextView: TextView by lazy {
        findViewById(R.id.text_planets_count_value)
    }

    private val selectedPlanetLabels: View by lazy {
        findViewById(R.id.planetary_system_selected_labels)
    }
    private val starPlanetNameTextView: TextView by lazy {
        findViewById(R.id.text_planet_name_value)
    }
    private val starPlanetDiameterTextView: TextView by lazy {
        findViewById(R.id.text_planet_diameter_value)
    }
    private val starPlanetMassTextView: TextView by lazy {
        findViewById(R.id.text_planet_mass_value)
    }
    private val starPlanetOrbitalPeriodTextView: TextView by lazy {
        findViewById(R.id.text_planet_orbital_period_value)
    }
    private val starRotationPeriodTextView: TextView by lazy {
        findViewById(R.id.text_planet_rotation_period_value)
    }
    private val starMoonsTextView: TextView by lazy { findViewById(R.id.text_planet_moons_value) }
    private val starRingsTextView: TextView by lazy { findViewById(R.id.text_planet_rings_value) }
    private val starAtmosphereTextView: TextView by lazy {
        findViewById(R.id.text_planet_atmosphere_value)
    }

    private val selectedPlanetViews by lazy {
        setOf(
            starPlanetNameTextView,
            starPlanetDiameterTextView,
            starPlanetMassTextView,
            starPlanetOrbitalPeriodTextView,
            starRotationPeriodTextView,
            starMoonsTextView,
            starRingsTextView,
            starAtmosphereTextView,
            selectedPlanetLabels
        )
    }

    private lateinit var animationTimeHandler: TimeHandler
    private lateinit var planetTimeHandler: TimeHandler

    private var planetDataIndex = 0

    init {
        View.inflate(context, R.layout.view_planetary_system, this)

        setWillNotDraw(false)

        renderFrame = findViewById(R.id.render_frame)

        initTimers()
    }

    private fun initTimers() {
        animationTimeHandler = TimeHandler(ANIMATION_DELAY_MILLIS) { invalidate() }
        animationTimeHandler.start()

        planetTimeHandler = TimeHandler(PLANET_DATA_DELAY_MILLIS) {
            planetDataIndex = (planetDataIndex + 1) % planetarySystem.planets.size
            populatePlanetTextViews(planetarySystem.planets[planetDataIndex], planetDataIndex + 1)
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        renderFrame.getDrawingRect(renderFrameBounds)
        renderFrameBounds.offset(renderFrame.left, renderFrame.top)
        renderBounds.set(renderFrameBounds)

        val widthX = renderBounds.width()
        val heightX = renderBounds.height()
        visualSize = max(widthX, heightX)
        visualCenter.set(
            renderBounds.centerX(),
            renderBounds.centerY()
        )
    }

    fun setPlanetarySystem(planetarySystem: PlanetarySystem) {
        this.planetarySystem = planetarySystem
        val planets = planetarySystem.planets
        planetsCount = planets.size
        orbitRingSpacing = 0.35f / planetsCount.toFloat()

        setPlanetAnimations(planets)

        populateStarTextViews(planetarySystem)

        planetTimeHandler.stop()

        if (planetarySystem.planets.isNotEmpty()) {
            showPlanetTextViews()
            planetDataIndex = -1
            planetTimeHandler.start()
        } else {
            hidePlanetTextViews()
        }

        invalidate()
    }

    private fun setPlanetAnimations(planets: List<Planet>) {
        planetAnimations.clear()

        repeat(planetsCount) { index ->
            planetAnimations.add(getPlanetAnimation(planets[index]))
        }
    }

    private fun getPlanetAnimation(planet: Planet): PlanetAnimation {
        val planetAnimation = PlanetAnimation(planet)
        planetAnimation.angle = Math.random().toFloat() * 360f
        planetAnimation.velocity = 25f / planet.orbitalPeriodYears

        planetAnimation.determineRotationDirection(planet)
        return planetAnimation
    }

    private fun PlanetAnimation.determineRotationDirection(planet: Planet) {
        if (planet.moons % 2 == 0) {
            velocity = -velocity
        }
    }

    private fun populateStarTextViews(planetarySystem: PlanetarySystem) {
        val resources = resources

        starAgeTextView.text =
            resources.getString(R.string.star_age_value, planetarySystem.starAgeBillionYears)
        starDiameterTextView.text =
            resources.getString(R.string.star_diameter_value, planetarySystem.starDiameterSunRadii)
        starMassTextView.text =
            resources.getString(R.string.star_mass_value, planetarySystem.starSolarMass)
        starPlanetsCountTextView.text = planetarySystem.planets.size.toString()
    }

    private fun hidePlanetTextViews() {
        selectedPlanetViews.forEach { view ->
            view.isVisible = false
        }
    }

    private fun showPlanetTextViews() {
        selectedPlanetViews.forEach { view ->
            view.isVisible = true
        }
    }

    private fun populatePlanetTextViews(planet: Planet, position: Int) {
        val resources = resources

        starPlanetNameTextView.text = resources.getString(R.string.planet_name, position)
        starPlanetDiameterTextView.text =
            resources.getString(R.string.planet_diameter_value, planet.diameterEarthRatio)
        starPlanetMassTextView.text =
            resources.getString(R.string.planet_mass_value, planet.solarMass)
        starPlanetOrbitalPeriodTextView.text =
            resources.getString(R.string.planet_orbital_period_value, planet.orbitalPeriodYears)
        starRotationPeriodTextView.text =
            resources.getString(R.string.planet_rotation_period_value, planet.rotationPeriodDays)
        starMoonsTextView.text = planet.moons.toString()
        starRingsTextView.text =
            if (planet.hasRings) {
                resources.getString(R.string.planet_has_rings)
            } else {
                resources.getString(
                    R.string.planet_no_rings
                )
            }
        starAtmosphereTextView.text =
            Html.fromHtml(getMaterialsFormatted(planet.atmosphere), FROM_HTML_MODE_COMPACT)
    }

    private fun getMaterialsFormatted(materials: List<Material>): String {
        if (materials.isEmpty()) {
            return "N/A"
        }

        val stringBuilder = StringBuilder()
        materials.forEach { material ->
            stringBuilder
                .append(getMaterialFormatted(material))
                .append("<br/>")
        }
        return stringBuilder.toString()
    }

    private fun getMaterialFormatted(material: Material): String {
        val stringBuilder = StringBuilder()
        for (elementCompound in material.compounds) {
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

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.save()
        canvas.clipRect(renderBounds)

        drawBackground(canvas)

        drawOrbits(canvas)

        advanceAllPlanetAnimations()

        canvas.drawStarAndPlanets()

        canvas.restore()
    }

    private fun drawBackground(canvas: Canvas) {
        with(paint) {
            style = Paint.Style.FILL
            color = backgroundColor
        }

        canvas.drawRect(renderBounds, paint)
    }

    private fun Canvas.drawStar() {
        drawCircle(
            this,
            visualCenter,
            planetarySystem.starDiameterSunRadii.toFloat() / 10f,
            Color.argb(192, 255, 255, 255)
        )
    }

    private fun Canvas.drawStarAndPlanets() {
        var drawnStar = false
        planetAnimations.map { planetAnimation ->
            planetAnimation to planetAnimation.planetY()
        }.sortedBy { planetAnimationAndY -> planetAnimationAndY.second }
            .forEach { planetAnimationAndY ->
                if (planetAnimationAndY.second > 0 && !drawnStar) {
                    drawStar()
                    drawnStar = true
                }
                drawPlanet(planetAnimationAndY.first)
            }

        if (!drawnStar) {
            drawStar()
        }
    }

    private fun drawOrbits(canvas: Canvas) {
        with(paint) {
            style = Paint.Style.STROKE
            strokeWidth = 0f
            color = 0x5fffffff
        }

        repeat(planetsCount) { index ->
            drawOrbitRing(canvas, index)
        }
    }

    private fun drawOrbitRing(canvas: Canvas, position: Int) {
        val relativeRadius = 0.1f + orbitRingSpacing * position
        val orbitRingWidth = visualSize * relativeRadius
        val orbitRingHeight = orbitRingWidth * ASPECT_RATIO
        planetRingRect.set(
            visualCenter.x - orbitRingWidth,
            visualCenter.y - orbitRingHeight,
            visualCenter.x + orbitRingWidth,
            visualCenter.y + orbitRingHeight
        )
        canvas.drawArc(planetRingRect, 0f, 360f, false, paint)
    }

    private fun Canvas.drawPlanet(planetAnimation: PlanetAnimation) {
        updatePlanetCenter(planetAnimation)

        val planet = planetAnimation.planet
        planetSize = planet.diameterEarthRatio * 1.5f

        val position = planetarySystem.planets.indexOf(planet)
        drawCircle(
            this,
            planetCenter,
            planetSize,
            if (position == planetDataIndex) -0x1 else -0x5f4f01
        )

        if (planet.hasRings) {
            drawPlanetRing()
        }
    }

    private fun drawCircle(canvas: Canvas, center: PointF, size: Float, color: Int) {
        paint.style = Paint.Style.FILL
        paint.color = color

        canvas.drawCircle(center.x, center.y, size, paint)
    }

    private fun updatePlanetCenter(planetAnimation: PlanetAnimation) {
        planetCenter.set(
            sin((planetAnimation.angle * RADIANS_TO_DEGREES).toDouble()).toFloat() *
                planetAnimation.actualRadius(),
            planetAnimation.planetY()
        )
        planetCenter.offset(visualCenter.x, visualCenter.y)
    }

    private fun PlanetAnimation.planetY(): Float {
        return (-cos((angle * RADIANS_TO_DEGREES).toDouble())).toFloat() *
            actualRadius() * ASPECT_RATIO
    }

    private fun PlanetAnimation.actualRadius(): Float {
        val position = planetarySystem.planets.indexOf(planet)
        val relativeRadius = 0.1f + orbitRingSpacing * position
        return visualSize * relativeRadius
    }

    private fun advanceAllPlanetAnimations() {
        for (i in 0 until planetsCount) {
            advancePlanetAnimation(i)
        }
    }

    private fun advancePlanetAnimation(position: Int) {
        val planetAnimation = planetAnimations[position]
        planetAnimation.angle += planetAnimation.velocity
        while (planetAnimation.angle >= 360) planetAnimation.angle -= 360f
        while (planetAnimation.angle < 0) planetAnimation.angle += 360f
    }

    private fun Canvas.drawPlanetRing() {
        val ringWidth = planetSize * RING_TO_STAR_RATIO
        val ringHeight = ringWidth * RING_ASPECT_RATIO

        planetRingRect.set(
            planetCenter.x - ringWidth,
            planetCenter.y - ringHeight,
            planetCenter.x + ringWidth,
            planetCenter.y + ringHeight
        )

        paint.strokeWidth = planetSize * 0.25f
        paint.style = Paint.Style.STROKE

        drawArc(planetRingRect, 0f, 360f, false, paint)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        animationTimeHandler.start()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()

        animationTimeHandler.stop()
    }

    private class TimeHandler(
        private val delayMilliseconds: Long,
        private val runnable: Runnable
    ) : Handler(Looper.getMainLooper()) {
        private var message: Message? = null

        override fun handleMessage(msg: Message) {
            val message = Message.obtain()
            this.message = message
            sendMessageDelayed(message, delayMilliseconds)
            runnable.run()
        }

        fun start() {
            if (message != null) return

            val message = Message.obtain()
            this.message = message
            sendMessage(message)
        }

        fun stop() {
            message?.let { message ->
                removeMessages(message.what)
                this.message = null
            }
        }
    }

    private data class PlanetAnimation(
        val planet: Planet,
        var velocity: Float = 0f,
        var angle: Float = 0f
    )
}
