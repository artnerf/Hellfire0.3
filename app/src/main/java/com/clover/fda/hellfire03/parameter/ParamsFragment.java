package com.clover.fda.hellfire03.parameter;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import com.clover.fda.hellfire03.R;

import java.util.Locale;

/**
 * Created by veselskyr on 10/03/15.
 */

public class ParamsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.getActivity());

        if (key.equals("language")) {

            String languageToLoad = prefs.getString(key, "");
            Preference language = findPreference(key);
            language.setTitle(languageToLoad);

            //ListPreference lp = (ListPreference)findPreference(key);
            //lp.setTitle(prefs.getString(key, ""));

            Locale locale = new Locale(languageToLoad);
            Locale.setDefault(locale);
            //DisplayMetrics dm = getResources().getDisplayMetrics();
            //Configuration config = getResources().getConfiguration();
            Configuration config = new Configuration();
            //config.locale = new Locale(languageToLoad);
            //config.locale = new Locale("de_DE");
            config.locale = locale;
            getResources().updateConfiguration(config,null);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.params);


        PreferenceCategory prefCat = (PreferenceCategory) findPreference("languageTitle");
        prefCat.setTitle(R.string.language_selection);


        //Preference customPref1 = findPreference("languageSelection");
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        //customPref1.setTitle(sp.getString("languageSelection", "-1"));

        ListPreference lp = (ListPreference) findPreference("language");
        lp.setDialogTitle("Sprache Dialogtitle");
        lp.setTitle(prefs.getString("language", ""));

        //Preference customPref = findPreference("checkbox_preference_1");
        //customPref.setSummary("new summary");
        //customPref.setTitle("new title");
    }

    @Override
    public void onResume() {
        super.onResume();
        PreferenceManager.getDefaultSharedPreferences(this.getActivity()).registerOnSharedPreferenceChangeListener(this);
        //getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

    }

    @Override
    public void onPause() {
        PreferenceManager.getDefaultSharedPreferences(this.getActivity()).unregisterOnSharedPreferenceChangeListener(this);
        //getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }
}
