package org.proven.decisions2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class ChargingScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charging_screen);
        /*Duration of the splash screen*/
        final int Duration = 1000;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /*pass from activity to another, in this case to the mainActivity where the login is located*/
                Intent intent = new Intent(ChargingScreen.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, Duration);
    }
}