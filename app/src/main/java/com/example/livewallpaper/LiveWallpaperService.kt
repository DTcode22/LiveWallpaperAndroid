// LiveWallpaperService.kt
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
            color = Color.rgb(200, 200, 255)
            alpha = 60
            strokeWidth = 2f
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
                    canvas.drawColor(Color.rgb(5, 5, 25))

                    time += Math.PI.toFloat() / 90f

                    when (wallpaperManager.getCurrentPattern()) {
                        0 -> drawPattern1(canvas)
                        1 -> drawPattern2(canvas)
                        2 -> drawPattern3(canvas)
                        3 -> drawPattern4(canvas)
                        4 -> drawPattern5(canvas)
                    }
                }
            } finally {
                if (canvas != null) {
                    holder.unlockCanvasAndPost(canvas)
                }
            }
        }

        private fun drawPattern1(canvas: Canvas) {
            val width = canvas.width.toFloat()
            val height = canvas.height.toFloat()

            // Adjusted paint for first pattern
            paint.apply {
                alpha = 100
                strokeWidth = 2.5f
                color = Color.WHITE
            }

            for (y in 0..200 step 2) {
                for (x in 0..200 step 2) {
                    val k = x/8f - 12f
                    val e = y/8f - 12f
                    val mag = sqrt(k*k + e*e)
                    val o = 2 - mag/3
                    val d = -5 * abs(sin(k/2)*cos(e*.8f))

                    val px = (x-d*k*4+d*k*sin(d+time))*.7f+k*o*2+130
                    val py = (y-d*y/5+d*e*cos(d+time+o)*sin(time+d))*.7f+e*o+70

                    canvas.drawPoint(
                        px * width / 200f,
                        py * height / 200f,
                        paint
                    )
                }
            }
        }

        private fun drawPattern2(canvas: Canvas) {
            val width = canvas.width.toFloat()
            val height = canvas.height.toFloat()

            // Adjusted paint for second pattern
            paint.apply {
                alpha = 100  // Much higher alpha for better visibility
                strokeWidth = 2f  // Thicker points
                color = Color.rgb(255, 255, 255)  // Pure white
            }

            // Center offset adjustments
            val centerOffsetX = width / 2
            val centerOffsetY = height / 2

            for (i in 0..40000 step 40) {  // Adjusted step for better density
                val x = i % 400
                val y = i / 400

                val k = x/16f - 12.5f
                val d = -5 * abs(sin(k/3)*sin(y/24f))
                val q = x/4f - y/3f + 60f + (sin(time) + d*3 + 3)*k*sin(d*3 + time + sin(d))
                val c = d/2 + time/8

                // Enhanced scaling and centering
                val px = (q * 0.9f * cos(c) + 200) * (width / 400f)
                val py = ((q + y/2f - d*19) * 0.9f * sin(c) + 200) * (height / 400f)

                // Additional visual enhancement - vary point size based on position
                paint.strokeWidth = 2f + abs(sin(c)) * 2f

                canvas.drawPoint(px, py, paint)
            }
        }

        private fun drawPattern3(canvas: Canvas) {
            val width = canvas.width.toFloat()
            val height = canvas.height.toFloat()

            paint.apply {
                alpha = 150
                strokeWidth = 2.5f
                color = Color.WHITE
            }

            for (i in 0..40000 step 40) {
                val x = i % 200
                val y = i / 200

                val k = x/8f - 12.5f
                val e = y/8f - 12f
                val mag = sqrt(k*k + e*e)
                val o = 3 - mag/3
                val d = -4 * (sin(k/2) * cos(e))

                val px = (x + e * cos(time) + d * k * sin(d + time)) * 0.7f + k * o + 130
                val py = (y - d * 19 + d * e * cos(d + time)) * 0.7f + 130

                canvas.drawPoint(
                    px * width / 200f,
                    py * height / 200f,
                    paint
                )
            }
        }

        private fun drawPattern4(canvas: Canvas) {
            val width = canvas.width.toFloat()
            val height = canvas.height.toFloat()

            paint.apply {
                alpha = 160
                strokeWidth = 3f
                color = Color.WHITE
            }

            for (i in 0..40000 step 35) {
                val x = i % 200
                val y = i / 200

                val k = x/8f - 12f
                val e = y/8f - 12f
                val mag = sqrt(k*k + e*e)
                val o = 2 - mag/3
                val d = -5 * abs(sin(k/2) * cos(e * 0.8f))

                val px = (x - d * k * 4 + d * k * sin(d + time)) * 0.7f + k * o * 2 + 130
                val py = (y - d * y/5 + d * e * cos(d + time + o) * sin(time + d)) * 0.7f + e * o + 70

                canvas.drawPoint(
                    px * width / 200f,
                    py * height / 200f,
                    paint
                )
            }
        }

        private fun drawPattern5(canvas: Canvas) {
            val width = canvas.width.toFloat()
            val height = canvas.height.toFloat()

            paint.apply {
                alpha = 180
                strokeWidth = 2.8f
                color = Color.WHITE
            }

            for (i in 0..40000 step 30) {
                val x = i % 200
                val y = i / 200

                val k = x/8f - 12.5f
                val e = y/8f - 12.5f
                val mag = sqrt(k*k + e*e)
                val o = mag * mag / 169f
                val d = 0.5f + 5 * cos(o)

                val sinColor = Math.pow((d * sin(k) * sin(time * 4 + e)).toDouble(), 2.0).toFloat()
                paint.alpha = (sinColor * 255).toInt().coerceIn(36, 255)

                val px = x + d * k * sin(d * 2 + o + time) + e * cos(e + time) + 100
                val py = o * 135 - y/4f - d * 6 * cos(d * 3 + o * 9 + time) + 125

                canvas.drawPoint(
                    px * width / 200f,
                    py * height / 200f,
                    paint
                )
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