// WallpaperSettingsActivity.kt
package com.example.livewallpaper

import android.os.Bundle
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity

class WallpaperSettingsActivity : AppCompatActivity() {
    private lateinit var wallpaperManager: WallpaperManager
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wallpaper_settings)
        
        wallpaperManager = WallpaperManager(this)
        
        val radioGroup = findViewById<RadioGroup>(R.id.patternRadioGroup)
        
        // Set current selection
        radioGroup.check(radioGroup.getChildAt(wallpaperManager.getCurrentPattern()).id)
        
        // Handle selection changes
        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            val index = group.indexOfChild(group.findViewById(checkedId))
            wallpaperManager.setCurrentPattern(index)
        }
    }
}
