package org.proven.decisions2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

public class ChargingGame extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.elements_animation_layout);
        //Duration for the game screen
        final int Duration = 4000;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //move on to the next activity
                Intent intent = new Intent(ChargingGame.this, SocialInterface.class);
                startActivity(intent);
                finish();

            }
        },Duration);
    }
}
