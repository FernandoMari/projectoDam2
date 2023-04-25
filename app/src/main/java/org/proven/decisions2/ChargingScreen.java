package org.proven.decisions2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import org.proven.decisions2.LoginAndRegister.MainActivity;

public class ChargingScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Set the window to fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Call the superclass onCreate method
        super.onCreate(savedInstanceState);

        //Set the content view to activity_charging_screen.xml
        setContentView(R.layout.activity_charging_screen);

        //Set the duration for the charging screen
        final int Duration = 4000;

        //Create a new Handler and post a delayed Runnable to it
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Create an Intent to move on to the next activity
                Intent intent = new Intent(ChargingScreen.this, MainActivity.class);
                //Start the new activity
                startActivity(intent);
                //Finish the current activity
                finish();
            }
        }, Duration);
    }
}