package ru.ridkeim.settingsexample

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.core.graphics.ColorUtils
import androidx.preference.PreferenceManager
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.InputStreamReader
import java.io.OutputStreamWriter

class MainActivity : AppCompatActivity(){

    companion object {
        private const val FILENAME = "sample.txt"
        private val TAG = MainActivity::class.qualifiedName
    }

    private lateinit var textEdit: TextView
    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        prefs = PreferenceManager.getDefaultSharedPreferences(this)
        textEdit = findViewById(R.id.textEdit)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onResume() {
        super.onResume()
        if(prefs.getBoolean(getString(R.string.pref_open_mode),false)){
            openFile()
        }
        updateTextColor()
        updateTextSize()
        updateStyle()
    }

    private fun updateStyle() {
        val typeface = when (prefs.getString(getString(R.string.pref_text_style), "")) {
            "bold" -> Typeface.BOLD
            "bold_italic" -> Typeface.BOLD_ITALIC
            "italic" -> Typeface.ITALIC
            else -> Typeface.NORMAL
        }
        textEdit.setTypeface(null,typeface)
    }

    private fun updateTextColor(){
        val keyOfColors = mapOf(
            getString(R.string.pref_text_color_black) to Color.BLACK,
            getString(R.string.pref_text_color_red) to Color.RED,
            getString(R.string.pref_text_color_green) to Color.GREEN,
            getString(R.string.pref_text_color_blue) to Color.BLUE
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
            textEdit.setTextColor(R.attr.colorOnPrimary)
        }else{
            textEdit.setTextColor(currentColor)
        }
    }

    private fun updateTextSize(){
        val sizeDefValueString = getString(R.string.text_size_def_value)
        val sizeDefValueFloat = sizeDefValueString.toFloat()
        val prefTextSize = try {
            prefs.getString(getString(R.string.pref_text_size),sizeDefValueString)?.toFloat() ?: sizeDefValueFloat
        } catch (e : NumberFormatException){
            prefs.edit().putString(getString(R.string.pref_text_size),sizeDefValueString).apply()
            sizeDefValueFloat
        }
        textEdit.textSize = prefTextSize
    }

    @SuppressLint("QueryPermissionsNeeded")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_settings -> {
                Intent(this, SettingsActivity::class.java).also {
                   it.resolveActivity(packageManager)?.also { _ ->
                       startActivity(it)
                   }
                }
                return true
            }
            R.id.action_open -> openFile()
            R.id.action_save -> saveFile()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveFile() {
        try {
            OutputStreamWriter(openFileOutput(FILENAME, 0)).use {
                it.write(textEdit.text.toString())
            }
        } catch (e : FileNotFoundException){
            e.printStackTrace()
            Log.d(TAG, "file $FILENAME not found")
        }
    }

    private fun openFile() {
        try {
            val stringBuilder = StringBuilder()
            BufferedReader(InputStreamReader(openFileInput(FILENAME))).useLines {
                it.forEach { line ->
                    stringBuilder.append("$line\n")
                }
            }
            textEdit.text = stringBuilder
        } catch (e : FileNotFoundException) {
            e.printStackTrace()
            Log.d(TAG, "file $FILENAME not found")
        }
    }

}