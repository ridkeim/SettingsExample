package ru.ridkeim.settingsexample.preffragments

import androidx.preference.PreferenceFragmentCompat

fun PreferenceFragmentCompat.setPreferenceScreenTitleFromArguments(){
    if(preferenceScreen.title == null) {
        preferenceScreen.title = arguments?.getCharSequence("title")
    }
}