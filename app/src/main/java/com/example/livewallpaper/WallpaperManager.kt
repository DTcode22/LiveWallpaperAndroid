// WallpaperManager.kt
package com.example.livewallpaper

import android.content.Context
import android.content.SharedPreferences

class WallpaperManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(
        "wallpaper_prefs",
        Context.MODE_PRIVATE
    )

    fun getCurrentPattern(): Int = prefs.getInt("current_pattern", 0)

    fun setCurrentPattern(index: Int) {
        prefs.edit().putInt("current_pattern", index).apply()
    }

    companion object {
        val patterns = listOf(
            Pattern.Pattern1(),
            Pattern.Pattern2()
        )
    }
}