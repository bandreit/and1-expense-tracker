package com.bandreit.expensetracker.ui.profile;

import android.os.Bundle;
import android.widget.Toast;

import androidx.preference.PreferenceFragmentCompat;

import com.bandreit.expensetracker.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }
}