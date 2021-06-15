package pablo.ad.recuperacionllamadascarmelo.view;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import pablo.ad.recuperacionllamadascarmelo.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
    }
}