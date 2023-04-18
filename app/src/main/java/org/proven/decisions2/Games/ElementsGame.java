package org.proven.decisions2.Games;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import org.proven.decisions2.R;

import java.util.Random;

public class ElementsGame extends Activity {

    boolean isTimerFinished = false;
    CountDownTimer countDownTimer;
    CardView btWater, btFire, btIce, machine, player;
    View.OnClickListener listener;
    TextView tvResult;
    int election, rival, value;
    private boolean isElementsGameLayoutLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the elements_animation_layout only if elements_game_layout is not loaded yet
        if (!isElementsGameLayoutLoaded) {
            setContentView(R.layout.elements_animation_layout);

            // Delay the loading of the elements_game_layout by 6.5 seconds
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    isElementsGameLayoutLoaded = true;

                    // Load the elements_game_layout and call init()
                    setContentView(R.layout.elements_game_layout);
                    init();
                    initCrono();
                    machine.setForeground(ContextCompat.getDrawable(ElementsGame.this, R.drawable.question));
                }
            }, 6500); // 6500 milliseconds = 6.5 seconds delay
        } else {
            setContentView(R.layout.elements_game_layout);
            init();
            initCrono();
            machine.setForeground(ContextCompat.getDrawable(ElementsGame.this, R.drawable.question));
        }
    }

    // Initialize and start the countdown timer for 30 seconds
    private void initCrono(){
        countDownTimer = new CountDownTimer(16000, 1000){

            @Override
            public void onTick(long time) {
                long segPendiente=time/1000;
                tvResult.setText(getString(R.string.time)+": "+segPendiente);
            }

            // If the timer finishes, randomly select an element for the rival and check for a win
            @Override
            public void onFinish() {
                Random rand = new Random();
                election = rand.nextInt(3) + 1;
                checkWin(election);
            }
        }.start();
    }

    // Initialize the views and set the click listener for the element buttons
    public void init(){
        btWater = findViewById(R.id.btWater);
        btFire = findViewById(R.id.btFire);
        btIce = findViewById(R.id.btIce);
        tvResult = findViewById(R.id.crono);
        machine = findViewById(R.id.machine);
        player = findViewById(R.id.player);
        setOnClickListener();
        instanciateListener();
    }

    // Set the click listener for the element buttons
    public void instanciateListener(){
        btWater.setOnClickListener(listener);
        btFire.setOnClickListener(listener);
        btIce.setOnClickListener(listener);
    }

    // Define the click listener for the element buttons and check for a win
    public void setOnClickListener(){
        listener= new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId() == btWater.getId()){
                    election = 2;
                    checkWin(election);
                }

                if(v.getId() == btFire.getId()){
                    election = 1;
                    checkWin(election);
                }
                if(v.getId() == btIce.getId()){
                    election = 3;
                    checkWin(election);
                }
            }
        };
    }

    // Check for a win, set the text view, and delay loading the ResultGame activity
    public void checkWin(int election){
        countDownTimer.cancel();
        Random rand = new Random();
        rival  = rand.nextInt(3)+1;
        int machineDrawableId = 0;
        int playerDrawableId = 0;
        switch (rival) {
            case 1:
                machineDrawableId = R.drawable.fire_element;
                break;
            case 2:
                machineDrawableId = R.drawable.water_element;
                break;
            case 3:
                machineDrawableId = R.drawable.ice_element;
                break;
        }
        switch (election) {
            case 1:
                playerDrawableId = R.drawable.fire_element;
                break;
            case 2:
                playerDrawableId = R.drawable.water_element;
                break;
            case 3:
                playerDrawableId = R.drawable.ice_element;
                break;
        }
        machine.setForeground(ContextCompat.getDrawable(ElementsGame.this, machineDrawableId));
        player.setForeground(ContextCompat.getDrawable(ElementsGame.this, playerDrawableId));

        if(election == 1 && rival == 2 || election == 2 && rival == 3 || election == 3 && rival == 1){
            tvResult.setText(R.string.defeat);
            value = 1;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(ElementsGame.this, ResultGame.class);
                    intent.putExtra("result", value);
                    startActivity(intent);
                    finishAndRemoveTask();
                }
            }, 2000); // 2000 milliseconds = 2 seconds delay
        }else if(election == 2 && rival == 1 || election == 3 && rival == 2 || election == 1 && rival == 3){
            tvResult.setText(R.string.victory);
            value = 0;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(ElementsGame.this, ResultGame.class);
                    intent.putExtra("result", value);
                    startActivity(intent);
                    finishAndRemoveTask();
                }
            }, 2000); // 2000 milliseconds = 2 seconds delay
        }else {
            tvResult.setText(R.string.draw);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    restartGame(); //call the method for the restart
                    return;
                }
            }, 2000); // 2000 milliseconds = 2 seconds delay

        }

        btWater.setEnabled(false);
        btFire.setEnabled(false);
        btIce.setEnabled(false);

    }
    /* Method for the restart the game */
    private void restartGame() {
        btWater.setEnabled(true);
        btFire.setEnabled(true);
        btIce.setEnabled(true);
        tvResult.setText("");
        initCrono();
        player.setForeground(null);
        machine.setForeground(ContextCompat.getDrawable(ElementsGame.this, R.drawable.question));
        value = 0;
    }
}
