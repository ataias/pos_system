package egl.positioningsystem;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class SettingsActivity extends PreferenceActivity{
	
	public static final String KEY_PREF_HOST = "prefHost";
	public static final String KEY_PREF_PORT = "prefPort";
	public static final String KEY_PREF_CONNECTION_SYNC_FREQUENCY = "connectionPrefSyncFrequency";
	public static final String KEY_PREF_SCREEN_SYNC_FREQUENCY = "screenPrefSyncFrequency";
    @SuppressWarnings("deprecation")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}