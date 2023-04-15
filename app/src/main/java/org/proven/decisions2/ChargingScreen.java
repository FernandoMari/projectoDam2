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

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charging_screen);
        //Duration for the charging screen
        final int Duration = 4000;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //move on to the next activity
                Intent intent = new Intent(ChargingScreen.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        }, Duration);
    }
}