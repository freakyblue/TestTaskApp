package de.jonny_seitz.simplelibrary;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.support.annotation.Nullable;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setTitle(R.string.settings);

        Fragment fragment = new SettingsScreen();
        FragmentTransaction fragmentTransaction =  getFragmentManager().beginTransaction();
        if (savedInstanceState == null) {
            fragmentTransaction
                    .add(R.id.settings_layout, fragment, "settings")
                    .commit();
        }
    }

    public static class SettingsScreen extends PreferenceFragment {

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings);

            SharedPreferences sharedPreferences =
                    getActivity().getSharedPreferences("warehouse", Context.MODE_PRIVATE);
            final SharedPreferences.Editor editor = sharedPreferences.edit();

            findPreference("pref_library_title").setDefaultValue(sharedPreferences.getString(
                    "library_name",
                    (String) getText(R.string.default_title_library_name)
            ));
            findPreference("pref_library_title")
                    .setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                        @Override
                        public boolean onPreferenceChange(Preference preference, Object o) {
                            editor.putString("library_name", o.toString());
                            editor.apply();
                            return true;
                        }//onPreferenceChange
                    });
        }

    }

}
