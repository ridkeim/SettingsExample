package ru.ridkeim.settingsexample

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.commit
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import ru.ridkeim.settingsexample.preffragments.SettingsFragment

class SettingsActivity : AppCompatActivity(), PreferenceFragmentCompat.OnPreferenceStartFragmentCallback {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        val rootPreferenceTitle = resources.getString(R.string.root_preference_title)
        if (savedInstanceState == null) {
            val settingsFragment = SettingsFragment().apply {
                arguments = bundleOf("title" to rootPreferenceTitle)
            }
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                replace(R.id.settings,settingsFragment)
            }
        }
        supportActionBar?.title = rootPreferenceTitle
        supportFragmentManager.addOnBackStackChangedListener{
            updateTitle()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun updateTitle(){
        val settingsFragment = supportFragmentManager.findFragmentById(R.id.settings) as PreferenceFragmentCompat
        settingsFragment.preferenceScreen.title?.let {
            supportActionBar?.title = if (it.isEmpty()){
                resources.getString(R.string.title_activity_settings)
            } else{
                it
            }
        }
        Log.d(SettingsActivity::class.java.canonicalName,"updateTitle")
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                if(supportFragmentManager.backStackEntryCount == 0){
                    finish()
                }else{
                    supportFragmentManager.popBackStack()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPreferenceStartFragment(caller: PreferenceFragmentCompat, pref: Preference): Boolean {
        val args = pref.extras
        val fragment = supportFragmentManager.fragmentFactory.instantiate(
            classLoader,
            pref.fragment)
        fragment.arguments = args
        args.putCharSequence("title", pref.title)
        fragment.setTargetFragment(caller, 0)
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.settings,fragment)
            addToBackStack(null)
        }
        return true
    }
}