package com.example.livewallpaper

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Handler
import android.os.Looper
import android.service.wallpaper.WallpaperService
import android.view.SurfaceHolder
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.math.PI

class LiveWallpaperService : WallpaperService() {
    private lateinit var wallpaperManager: WallpaperManager

    override fun onCreate() {
        super.onCreate()
        wallpaperManager = WallpaperManager(this)
    }

    override fun onCreateEngine(): Engine = WallpaperEngine()

    private inner class WallpaperEngine : Engine() {
        private val handler = Handler(Looper.getMainLooper())
        private var visible = false
        private val paint = Paint().apply {
            color = Color.rgb(180, 180, 180)
            alpha = 25
            strokeWidth = 4f  // Increased stroke width for zoomed view
            style = Paint.Style.STROKE
            isAntiAlias = true
        }

        private var time = 0f

        private val drawRunner = object : Runnable {
            override fun run() {
                drawFrame()
                if (visible) {
                    handler.postDelayed(this, 1000 / 30)
                }
            }
        }

        private fun drawFrame() {
            val holder = surfaceHolder
            var canvas: Canvas? = null

            try {
                canvas = holder.lockCanvas()
                if (canvas != null) {
                    canvas.drawColor(Color.BLACK)
                    time += PI.toFloat() / 90f

                    when (wallpaperManager.getCurrentPattern()) {
                        0 -> drawSmokeVortex(canvas)
                        1 -> drawNebula(canvas)
                        2 -> drawAuroraWaves(canvas)
                        3 -> drawCosmicFlow(canvas)
                        4 -> drawEtherealMist(canvas)
                    }
                }
            } finally {
                if (canvas != null) {
                    holder.unlockCanvasAndPost(canvas)
                }
            }
        }

        private fun drawSmokeVortex(canvas: Canvas) {
            val width = canvas.width.toFloat()
            val height = canvas.height.toFloat()
            val centerX = width / 2
            val centerY = height / 2

            paint.apply {
                alpha = 15
                strokeWidth = 4f
                color = Color.rgb(180, 180, 180)
            }

            for (i in 0..360 step 2) {
                val angle = i * PI.toFloat() / 180
                val radius = 200 + 100 * sin(time + i / 30f)  // Increased radius

                for (j in 0..15) {  // Reduced iterations for more focused effect
                    val r = radius + j * 8 * sin(time / 2 + i / 50f)  // Increased spacing
                    val turbulence = 30 * sin(time * 2 + i / 20f)  // Increased turbulence

                    val x = centerX + r * cos(angle + time / 2) + turbulence * sin(time + i)
                    val y = centerY + r * sin(angle + time / 2) + turbulence * cos(time + i)

                    paint.alpha = (15 - j/2).coerceIn(5, 15)
                    canvas.drawPoint(x, y, paint)
                }
            }
        }

        private fun drawNebula(canvas: Canvas) {
            val width = canvas.width.toFloat()
            val height = canvas.height.toFloat()
            val centerX = width / 2
            val centerY = height / 2

            paint.apply {
                alpha = 20
                strokeWidth = 5f  // Increased for visibility
                color = Color.rgb(160, 160, 180)
            }

            for (i in 0..2000 step 3) {  // Reduced range for closer view
                val theta = i * 0.1f
                val spiral = theta / 20f  // Reduced divisor for tighter spiral
                val wave = sin(theta / 10f + time * 2)

                val r = spiral * 30 * wave  // Increased multiplication factor
                val x = centerX + r * cos(theta + time)
                val y = centerY + r * sin(theta + time) * 0.6f

                paint.alpha = (20 * abs(wave)).toInt().coerceIn(5, 20)
                paint.strokeWidth = 3f + 3f * abs(wave)  // Increased stroke variation

                canvas.drawPoint(x, y, paint)
            }
        }

        private fun drawAuroraWaves(canvas: Canvas) {
            val width = canvas.width.toFloat()
            val height = canvas.height.toFloat()
            val centerX = width / 2
            val centerY = height / 2

            paint.apply {
                alpha = 30  // Increased visibility
                strokeWidth = 4.5f
                color = Color.rgb(190, 190, 190)  // Lighter color
            }

            // Create full screen flowing waves
            for (x in -120..120 step 3) {  // Increased range
                for (y in -120..120 step 3) {  // More density
                    val distanceFromCenter = sqrt((x * x + y * y).toDouble()) / 120f
                    val wavePhase = distanceFromCenter * 2 + time

                    // Create multiple overlapping wave patterns
                    val wave1 = sin(x / 20f + time * 1.5f + y / 30f) * 100
                    val wave2 = cos(y / 25f + time * 2f + x / 40f) * 80
                    val wave3 = sin((x + y) / 40f + time) * 60

                    val combinedWave = wave1 + wave2 + wave3

                    val px = centerX + x * 6 + combinedWave * 0.5f
                    val py = centerY + y * 6 + combinedWave * 0.3f

                    // Vary opacity based on position and time
                    paint.alpha = (30 * (0.5 + 0.5 * sin(wavePhase))).toInt().coerceIn(15, 30)
                    canvas.drawPoint(px, py, paint)
                }
            }
        }

        private fun drawCosmicFlow(canvas: Canvas) {
            val width = canvas.width.toFloat()
            val height = canvas.height.toFloat()
            val centerX = width / 2
            val centerY = height / 2

            paint.apply {
                alpha = 35  // Increased visibility
                strokeWidth = 5f
                color = Color.rgb(200, 200, 200)  // Lighter color
            }

            // Create expanding spiral arms that fill the screen
            for (i in 0..2400 step 2) {  // More points for fuller coverage
                val angle = i * 0.05f
                val baseRadius = width * 0.8f  // Use screen width to scale

                // Create multiple spiral arms
                for (arm in 0..3) {
                    val armOffset = arm * PI.toFloat() / 2
                    val radius = (baseRadius * (0.2 + 0.8 * abs(sin(angle / 8f + time + armOffset))))

                    val turbulence = 60 * sin(angle / 4f + time * 2 + arm)
                    val flow = 50 * cos(angle / 3f + time * 1.5f + arm)

                    val x = centerX + radius * cos(angle + time + armOffset) + turbulence
                    val y = centerY + radius * sin(angle + time + armOffset) * 0.8f + flow

                    paint.alpha = (35 * abs(sin(angle / 2 + time))).toInt().coerceIn(20, 35)
                    canvas.drawPoint(x.toFloat(), y.toFloat(), paint)
                }
            }
        }

        private fun drawEtherealMist(canvas: Canvas) {
            val width = canvas.width.toFloat()
            val height = canvas.height.toFloat()
            val centerX = width / 2
            val centerY = height / 2

            paint.apply {
                alpha = 25  // Increased base visibility
                strokeWidth = 5f
                color = Color.rgb(195, 195, 195)  // Lighter color
            }

            // Create a full-screen ethereal mist effect
            for (layer in 0..4) {  // Multiple layers for depth
                val layerOffset = layer * PI.toFloat() / 2

                for (i in 0..1800 step 2) {
                    val theta = i * 0.1f
                    val baseRadius = width * 0.6f * (0.4 + 0.6 * abs(sin(theta / 30f + time + layer)))

                    // Create swirling patterns
                    val swirl = 40 * sin(theta / 20f + time * (1 + layer * 0.2f))
                    val drift = 80 * cos(theta / 15f + time * 1.5f + layerOffset)

                    val x = centerX + baseRadius * cos(theta + time * 0.5f + layerOffset) + drift
                    val y = centerY + baseRadius * sin(theta + time * 0.7f + layerOffset) * 0.9f + swirl

                    // Vary opacity based on position and layer
                    paint.alpha = (25 - layer * 3).coerceIn(15, 25)
                    paint.strokeWidth = 5f - layer * 0.5f

                    canvas.drawPoint(x.toFloat(), y.toFloat(), paint)
                }
            }
        }

        override fun onVisibilityChanged(visible: Boolean) {
            this.visible = visible
            if (visible) {
                handler.post(drawRunner)
            } else {
                handler.removeCallbacks(drawRunner)
            }
        }

        override fun onSurfaceDestroyed(holder: SurfaceHolder) {
            super.onSurfaceDestroyed(holder)
            visible = false
            handler.removeCallbacks(drawRunner)
        }

        override fun onSurfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
            super.onSurfaceChanged(holder, format, width, height)
            drawFrame()
        }
    }
}