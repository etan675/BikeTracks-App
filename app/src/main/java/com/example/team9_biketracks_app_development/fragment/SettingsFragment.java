package com.example.team9_biketracks_app_development.fragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;


import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;

import com.example.team9_biketracks_app_development.R;

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }
}
