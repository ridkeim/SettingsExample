package ru.ridkeim.settingsexample.preffragments

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import ru.ridkeim.settingsexample.R

class ColorSettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.color_preferences, rootKey)
        setPreferenceScreenTitleFromArguments()
    }
}