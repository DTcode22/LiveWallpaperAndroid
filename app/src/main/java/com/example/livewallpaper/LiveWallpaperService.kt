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
    override fun onCreateEngine(): Engine {
        return WallpaperEngine()
    }

    private inner class WallpaperEngine : Engine() {
        private val handler = Handler(Looper.getMainLooper())
        private var visible = false
        private val paint = Paint().apply {
            color = Color.WHITE
            alpha = 46
            strokeWidth = 2f
            style = Paint.Style.STROKE
        }
        private val backgroundPaint = Paint().apply {
            color = Color.rgb(6, 6, 6)
            alpha = 96
        }

        private var time = 0f
        private val drawRunner = object : Runnable {
            override fun run() {
                drawFrame()
                handler.postDelayed(this, 1000 / 30) // 30 FPS
            }
        }

        private fun animatePoint(x: Float, y: Float, width: Float, height: Float): Pair<Float, Float> {
            val k = x/8 - 12
            val e = y/8 - 12
            val mag = sqrt(k*k + e*e)
            val o = 2 - mag/3
            val d = -5 * abs(sin(k/2)*cos(e*.8f))

            val px = (x-d*k*4+d*k*sin(d+time))*.7f+k*o*2+130
            val py = (y-d*y/5+d*e*cos(d+time+o)*sin(time+d))*.7f+e*o+70

            // Scale to screen size
            return Pair(
                px * width / 200f,
                py * height / 200f
            )
        }

        private fun drawFrame() {
            val holder = surfaceHolder
            var canvas: Canvas? = null

            try {
                canvas = holder.lockCanvas()
                if (canvas != null) {
                    // Draw background
                    canvas.drawRect(0f, 0f, canvas.width.toFloat(),
                        canvas.height.toFloat(), backgroundPaint)

                    // Update time
                    time += Math.PI.toFloat() / 90f

                    // Draw points
                    val width = canvas.width.toFloat()
                    val height = canvas.height.toFloat()

                    for (y in 0..200 step 2) {
                        for (x in 0..200 step 2) {
                            val (px, py) = animatePoint(x.toFloat(), y.toFloat(), width, height)
                            canvas.drawPoint(px, py, paint)
                        }
                    }
                }
            } finally {
                if (canvas != null) {
                    holder.unlockCanvasAndPost(canvas)
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