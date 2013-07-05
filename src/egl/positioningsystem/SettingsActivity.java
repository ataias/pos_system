package egl.positioningsystem;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;

public class SettingsActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener {
	
	public static final String KEY_PREF_HOST = "prefHost";
	public static final String KEY_PREF_PORT = "prefPort";
	
    @SuppressWarnings("deprecation")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key){
		// TODO Auto-generated method stub
		if (key.equals(KEY_PREF_HOST)) {
			 @SuppressWarnings("deprecation")
			Preference connectionPref = findPreference(key);
	         // Set summary to be the user-description for the selected value
	         connectionPref.setSummary(sharedPreferences.getString(key, ""));
		}
	}
}