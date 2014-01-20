package com.mtechcomm.mtechword;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by Peter on 9/24/13.
 */
public class GameConfig extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);

    }
}
