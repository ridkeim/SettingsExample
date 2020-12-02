package ru.ridkeim.settingsexample

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat

class SettingsActivity : AppCompatActivity(), PreferenceFragmentCompat.OnPreferenceStartFragmentCallback {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        supportFragmentManager.addOnBackStackChangedListener{
            updateTitle()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun updateTitle(){
        val settingsFragment = supportFragmentManager.findFragmentById(R.id.settings) as PreferenceFragmentCompat
        if(settingsFragment.preferenceScreen.title != null){
            supportActionBar?.title = settingsFragment.preferenceScreen.title
        }else{
            supportActionBar?.title = resources.getString(R.string.title_activity_settings)
        }
        Log.d(SettingsActivity::class.java.canonicalName,"updateTitle")
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                if (supportFragmentManager.backStackEntryCount > 0) {
                    supportFragmentManager.popBackStack()
                    return true
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