package org.proven.decisions2.Settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;

import java.util.Locale;

public class LanguageManager
{

    //"ct" is short for "Context" and refers to an Android context object. A context in Android provides access to system resources and allows you to perform actions within an app such as starting an activity or creating a view. The "ct" variable is used in this class to access the app's resources and configuration.
    private Context ct;

    //"sharedPreferences" is an Android class that provides a way to store and retrieve key-value pairs of data. It is typically used to store app settings or user preferences. In this class, the "sharedPreferences" variable is used to store and retrieve the app's language preference, using a key-value pair with the key "lang". The "MODE_PRIVATE" parameter in the "getSharedPreferences" method indicates that the preferences should only be accessible within the app and not by other apps or users.
    private SharedPreferences sharedPreferences;

    // This is a constructor that takes a Context object as a parameter
    public LanguageManager(Context ctx){
        // Set the ct variable to the value passed in
        ct=ctx;
        // Initialize the sharedPreferences variable with a "LANG" key and a private mode
        sharedPreferences = ct.getSharedPreferences("LANG", Context.MODE_PRIVATE);
    }

    // This is a method that updates the app's language resources
    public void updateResource(String code){
        // Create a new Locale object with the provided code
        Locale locale = new Locale(code);
        // Set the default Locale to the new Locale object
        Locale.setDefault(locale);
        // Get the app's resources
        Resources resources = ct.getResources();
        // Get the app's configuration
        Configuration configuration = resources.getConfiguration();
        // Set the app's locale configuration to the new Locale object
        configuration.locale = locale;
        // Update the app's resources configuration with the new configuration
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        // Set the current language code in shared preferences
        setLang(code);
    }

    // This is a method that returns the app's current language code
    public String getLang(){
        // Return the value stored in shared preferences under the "lang" key or "en" if it doesn't exist
        return sharedPreferences.getString("lang", "en");
    }

    // This is a method that sets the app's current language code in shared preferences
    public void setLang(String code){
        // Create a new editor for the shared preferences object
        SharedPreferences.Editor editor = sharedPreferences.edit();
        // Put the new language code in the editor
        editor.putString("lang", code);
        // Commit the changes to shared preferences
        editor.commit();
    }
}
