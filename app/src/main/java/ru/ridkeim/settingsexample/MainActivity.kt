package ru.ridkeim.settingsexample

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.core.graphics.ColorUtils
import androidx.preference.PreferenceManager

class MainActivity : AppCompatActivity(){

    private lateinit var textView: TextView
    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        prefs = PreferenceManager.getDefaultSharedPreferences(this)
        textView = findViewById(R.id.textView)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onResume() {
        super.onResume()
        updateTextColor()
    }

    private fun updateTextColor(){
        val keyOfColors = mapOf(
            "text_color_black" to Color.BLACK,
            "text_color_red" to Color.RED,
            "text_color_green" to Color.GREEN,
            "text_color_blue" to Color.BLUE
        )
        var currentColor = Color.BLACK
        var colorsCount = 0
        var colorInitialized = false
        for ((key,color) in keyOfColors){
            if(prefs.getBoolean(key,false)){
                currentColor = if(colorInitialized){
                    val ratio = 1F/(++colorsCount)
                    ColorUtils.blendARGB(currentColor, color, ratio)
                } else {
                    colorInitialized = true
                    colorsCount++
                    color
                }
            }
        }
        if(colorsCount == 0){
            textView.setTextColor(R.attr.colorOnPrimary)
        }else{
            textView.setTextColor(currentColor)
        }

    }

    @SuppressLint("QueryPermissionsNeeded")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.settingsMenuItem -> {
                Intent(this, SettingsActivity::class.java).also {
                   it.resolveActivity(packageManager)?.also { _ ->
                       startActivity(it)
                   }
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}