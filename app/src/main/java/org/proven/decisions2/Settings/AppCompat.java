package org.proven.decisions2.Settings;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

import org.proven.decisions2.Settings.LanguageManager;

public class AppCompat extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LanguageManager languageManager = new LanguageManager(this);
        languageManager.updateResource(languageManager.getLang());
    }
}
