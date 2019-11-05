package li.x1ang.remotesms.fragments

import android.os.Bundle
import android.support.v7.preference.EditTextPreference
import android.support.v7.preference.PreferenceFragmentCompat
import android.support.v7.preference.PreferenceManager
import android.text.TextUtils
import li.x1ang.remotesms.R

class SMSPreferenceFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

        arrayOf("dingtalk_endpoint", "sim0num", "sim1num").forEach {
            val pref = findPreference(it) as EditTextPreference
            pref.summary = sharedPreferences.getString(it, "")
            pref.setOnPreferenceChangeListener { preference, value ->
                preference.summary = value.toString()
                true
            }
        }
    }

}