package com.example.RoachSmasher;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceManager;
 
/*___________________________________________________________________
|
| Class: PrefsFragmentSettings
|__________________________________________________________________*/
public class PrefsFragmentSettings extends PreferenceFragment implements OnSharedPreferenceChangeListener 
{
    final static String TAG = "PrefsFragmentSettings";
    
	/*___________________________________________________________________
	|
	| Function: PrefsFragmentSettings (constructor) 
	|__________________________________________________________________*/
    public PrefsFragmentSettings () 
    { 
    }
    
	/*___________________________________________________________________
	|
	| Function: onCreate 
	|__________________________________________________________________*/
	@Override
    public void onCreate (Bundle savedInstanceState) 
    {
    	super.onCreate(savedInstanceState);   	
    	// Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.prefs_fragment_settings);
    }
	
	/*___________________________________________________________________
	|
	| Function: onResume 
	|__________________________________________________________________*/
    @Override
    public void onResume() 
    {
    	super.onResume();

	    // Set up a listener whenever a key changes 
	    getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this); 

    	// Set up a click listener
    	Preference pref;
    	pref = getPreferenceScreen().findPreference("prefs_highscore");
    	int highscore;
    	
    	//SharedPreferences sharedPreferences=PreferenceManager.getDefaultSharedPreferences(null);
    	
    	SharedPreferences sharedPreferences=getPreferenceScreen().getSharedPreferences();
    	highscore=sharedPreferences.getInt("prefs_highscore", 0);
    	String s=""+ highscore;
    	pref.setSummary(s);;
 		    
    }

	/*___________________________________________________________________
	|
	| Function: onSharedPreferenceChanged 
	|__________________________________________________________________*/
	public void onSharedPreferenceChanged (SharedPreferences sharedPreferences, String key)  
	{ 	
		/*if (key.equals("prefs_music_enabled_background")) {		
			boolean b = sharedPreferences.getBoolean("prefs_music_enabled_background", true);
			if (b == false) {
				if (Assets.mp != null)
					Assets.mp.setVolume(0, 0);
			}
			else {
				if (Assets.mp != null)
					Assets.mp.setVolume(1, 1);				
			}
		}*/
	}
}