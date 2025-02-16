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
            strokeWidth = 3f
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
                strokeWidth = 3f
                color = Color.rgb(180, 180, 180)
            }

            for (i in 0..360 step 2) {
                val angle = i * PI.toFloat() / 180
                val radius = 100 + 50 * sin(time + i / 30f)

                for (j in 0..20) {
                    val r = radius + j * 5 * sin(time / 2 + i / 50f)
                    val turbulence = 20 * sin(time * 2 + i / 20f)

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
                strokeWidth = 4f
                color = Color.rgb(160, 160, 180)
            }

            for (i in 0..3000 step 3) {
                val theta = i * 0.1f
                val spiral = theta / 30f
                val wave = sin(theta / 10f + time * 2)

                val r = spiral * 20 * wave
                val x = centerX + r * cos(theta + time)
                val y = centerY + r * sin(theta + time) * 0.6f

                paint.alpha = (20 * abs(wave)).toInt().coerceIn(5, 20)
                paint.strokeWidth = 2f + 2f * abs(wave)

                canvas.drawPoint(x, y, paint)
            }
        }

        private fun drawAuroraWaves(canvas: Canvas) {
            val width = canvas.width.toFloat()
            val height = canvas.height.toFloat()
            val centerX = width / 2
            val centerY = height / 2

            paint.apply {
                alpha = 15
                strokeWidth = 3f
                color = Color.rgb(170, 170, 170)
            }

            for (x in -100..100 step 2) {
                for (y in -100..100 step 4) {
                    val distortion = sin(x / 20f + time) * cos(y / 20f + time)
                    val wave = sin(y / 15f + time * 2) * 50

                    val px = centerX + x * 3 + wave * distortion
                    val py = centerY + y * 2 + 20 * sin(x / 30f + time * 3)

                    paint.alpha = (15 * abs(distortion)).toInt().coerceIn(5, 15)
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
                alpha = 18
                strokeWidth = 3.5f
                color = Color.rgb(165, 165, 165)
            }

            for (i in 0..2000 step 2) {
                val angle = i * 0.03f
                val radius = 150 * sin(angle / 10f + time)

                val turbulence = 30 * cos(angle * 2 + time * 3)
                val flow = 20 * sin(angle / 2 + time * 2)

                val x = centerX + radius * cos(angle + time) + turbulence
                val y = centerY + radius * sin(angle + time) * 0.7f + flow

                paint.alpha = (18 * abs(sin(angle + time))).toInt().coerceIn(5, 18)
                canvas.drawPoint(x, y, paint)
            }
        }

        private fun drawEtherealMist(canvas: Canvas) {
            val width = canvas.width.toFloat()
            val height = canvas.height.toFloat()
            val centerX = width / 2
            val centerY = height / 2

            paint.apply {
                alpha = 12
                strokeWidth = 4f
                color = Color.rgb(175, 175, 175)
            }

            for (i in 0..1800 step 3) {
                val theta = i * 0.1f
                val baseRadius = 100 + 50 * sin(theta / 20f + time)

                for (j in 0..3) {
                    val radius = baseRadius + j * 20 * sin(time + theta / 10f)
                    val drift = 15 * cos(theta + time * 2 + j)

                    val x = centerX + radius * cos(theta + time + j / 2f) + drift
                    val y = centerY + radius * sin(theta + time + j / 2f) * 0.8f

                    paint.alpha = (12 - j * 2).coerceIn(5, 12)
                    canvas.drawPoint(x, y, paint)
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