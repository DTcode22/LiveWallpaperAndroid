// Pattern.kt
package com.example.livewallpaper

import android.graphics.Canvas
import android.graphics.Paint
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

sealed class Pattern {
    abstract fun draw(canvas: Canvas, paint: Paint, time: Float)
    
    class Pattern1 : Pattern() {
        override fun draw(canvas: Canvas, paint: Paint, time: Float) {
            val width = canvas.width.toFloat()
            val height = canvas.height.toFloat()
            
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
    }
    
    class Pattern2 : Pattern() {
        override fun draw(canvas: Canvas, paint: Paint, time: Float) {
            val width = canvas.width.toFloat()
            val height = canvas.height.toFloat()
            
            for (i in 0..40000) {
                val x = i % 400
                val y = i / 400
                
                val k = x/16f - 12.5f
                val d = -5 * abs(sin(k/3)*sin(y/24f))
                val q = x/4f - y/3f + 60f + (sin(time) + d*3 + 3)*k*sin(d*3 + time + sin(d))
                val c = d/2 + time/8
                
                val px = q * 0.7f * cos(c) + 200
                val py = (q + y/2f - d*19) * 0.7f * sin(c) + 200
                
                canvas.drawPoint(
                    px * width / 400f,
                    py * height / 400f,
                    paint
                )
            }
        }
    }
}




