package org.proven.decisions2.Games;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.proven.decisions2.R;

public class ElementsGameOnline extends Activity {

    // Declare a CountDownTimer variable called countDownTimer
    CountDownTimer countDownTimer;

    // Declare CardView variables called btWater, btFire, btIce, machine, and player
    CardView btWater, btFire, btIce, machine, player;

    // Declare a View.OnClickListener variable called listener
    View.OnClickListener listener;

    // Declare a TextView variable called tvResult
    TextView tvResult;

    // Declare integer variables called election, rival, and value.
    int election, rival, value;

    String playerName;
    String roomName;
    String role;
    String message;

    FirebaseDatabase database;
    DatabaseReference messageRef;
    DatabaseReference hostEle;
    DatabaseReference onExit;

    Boolean charge = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the layout for this activity to elements_animation_layout.xml
        setContentView(R.layout.elements_animation_layout);

        // Use a Handler to delay the execution of some code
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // After 6.5 seconds, change the layout to elements_game_layout.xml
                setContentView(R.layout.elements_game_layout);
                charge = true;

                //Init multiplayer elements
                earlyerInit();

                // Call initializeElements() method to initialize the game
                initializeElements();

                // Call initCrono() method to initialize the timer
                initCrono();

                // Set the foreground drawable of the machine ImageView to question.png
                machine.setForeground(ContextCompat.getDrawable(ElementsGameOnline.this, R.drawable.question));
            }
        }, 6500); // Wait for 6.5 seconds before executing the code inside the Runnable
    }

    // This method initializes the countdown timer
    private void initCrono() {
        // Create a new instance of CountDownTimer and set its duration and tick interval
        countDownTimer = new CountDownTimer(15000, 1000) {
            // This method is called every tick and updates the remaining time on the UI
            @Override
            public void onTick(long time) {
                // Calculate the remaining seconds and update the text view with the result
                long segPendiente = time / 1000;
                tvResult.setText(getString(R.string.time) + ": " + segPendiente);
            }

            // This method is called when the timer finishes
            @Override
            public void onFinish() {
                btWater.setEnabled(false);
                btFire.setEnabled(false);
                btIce.setEnabled(false);

                // Generate a random number to select an element for the rival
                if(rival != 0 && election != 0){
                    checkWin(election,rival);
                }else{
                    if(rival == 0 && election == 0){
                        tvResult.setText(R.string.select_element);
                    } else if(rival == 0 || election == 0) {
                        tvResult.setText(R.string.one_player_dont_select);
                    }
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            restartGame();
                        }
                    }, 3000); // 3000 milisegundos = 3 segundos

                }
            }
        }.start(); // Start the countdown timer
    }

    // Initialize the views and set the click listener for the element buttons
    public void initializeElements(){
        btWater = findViewById(R.id.btWater);
        btFire = findViewById(R.id.btFire);
        btIce = findViewById(R.id.btIce);
        tvResult = findViewById(R.id.crono);
        machine = findViewById(R.id.machine);
        player = findViewById(R.id.player);
        database = FirebaseDatabase.getInstance();
        setOnClickListener();
        instanciateListener();

        onExit = database.getReference("rooms/"+roomName+"/status");
        setOnExit();
    }

    public void earlyerInit(){

        SharedPreferences preferences = getSharedPreferences("PREFS",0);
        playerName = preferences.getString("playerName","");

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            roomName = extras.getString("roomName");

            if(roomName.equals(playerName)){
                role="host";
            }else{
                role="guest";
            }
        }
    }

    // Set the click listener for the element buttons
    public void instanciateListener(){
        btWater.setOnClickListener(listener);
        btFire.setOnClickListener(listener);
        btIce.setOnClickListener(listener);
    }

    // This method sets an OnClickListener for the buttons btWater, btFire, and btIce.
    public void setOnClickListener(){
        // A new View.OnClickListener object is created and assigned to the listener variable.
        listener= new View.OnClickListener() {
            @Override
            // The onClick method of the View.OnClickListener interface is overridden.
            public void onClick(View v) {
                // If the id of the clicked view is equal to the id of btWater, the election variable is set to 2.
                if(v.getId() == btWater.getId()){
                    election = 2;

                    sendDepending(2);
                    disableBt(2);
                    // The checkWin method is called with the election variable as its parameter.
                    //checkWin(election);
                }
                // If the id of the clicked view is equal to the id of btFire, the election variable is set to 1.
                if(v.getId() == btFire.getId()){
                    election = 1;

                    sendDepending(1);
                    disableBt(1);
                    // The checkWin method is called with the election variable as its parameter.
                    //checkWin(election);
                }

                // If the id of the clicked view is equal to the id of btIce, the election variable is set to 3.
                if(v.getId() == btIce.getId()){
                    election = 3;

                    sendDepending(3);
                    disableBt(3);
                    // The checkWin method is called with the election variable as its parameter.
                    //checkWin(election);
                }
            }
        };
    }

    private void sendDepending(int i){
        if(role.equals("guest")){
            messageRef = database.getReference("rooms/"+roomName+"/message");
            message = role+":"+i;
            addRoomEventListener();
            messageRef.setValue(message);

        }else if(role.equals("host")){
            hostEle = database.getReference("rooms/"+roomName+"/hostele");
            message = role+":"+i;
            getHostEventListener();
            hostEle.setValue(message);
        }
    }

    private void addRoomEventListener(){
        messageRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(role.equals("guest")){
                    if (snapshot.getValue(String.class) != null){
                        hostEle = database.getReference("rooms/"+roomName+"/hostele");
                        getHostEventListener();
                    }
                }else if(role.equals("host")){
                    // capar null
                    if(snapshot.getValue(String.class) != null){
                        String alter = snapshot.getValue(String.class).replace("guest:","");
                        if(alter.matches("\\d+") && election != 0){
                            //aqui tengo que quiza crear una variable
                            rival = Integer.parseInt(alter);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getHostEventListener(){
        hostEle.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(role.equals("guest")){
                    if(snapshot.getValue(String.class) != null){
                        String alter = snapshot.getValue(String.class).replace("host:","");
                        if(alter.matches("\\d+") && election != 0){
                            //aqui tengo que quiza crear una variable
                            rival = Integer.parseInt(alter);
                        }
                    }
                }else if(role.equals("host")){
                    messageRef = database.getReference("rooms/"+roomName+"/message");
                    addRoomEventListener();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    // This method is used to check if the user has won or lost the game
    // It takes an integer argument "election" which represents the user's choice of element
    public void checkWin(int electio, int rivae){
        countDownTimer.cancel();
        // Generates a random number to represent the rival's choice of element


        // Sets the drawable ID for the rival's element based on the randomly generated number
        int machineDrawableId = 0;
        switch (rivae) {
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

// Sets the drawable ID for the user's chosen element based on the argument passed to the method
        int playerDrawableId = 0;
        switch (electio) {
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

        // Sets the foreground of the machine ImageView to the drawable of the rival's chosen element
        machine.setForeground(ContextCompat.getDrawable(ElementsGameOnline.this, machineDrawableId));

        // Sets the foreground of the player ImageView to the drawable of the user's chosen element
        player.setForeground(ContextCompat.getDrawable(ElementsGameOnline.this, playerDrawableId));

        // Determines the outcome of the game based on the user's choice and the rival's choice
        if(election == 1 && rival == 2 || election == 2 && rival == 3 || election == 3 && rival == 1){
            // If the user loses, sets the text of the tvResult TextView to "Defeat"
            tvResult.setText(R.string.defeat);

            // Sets the value of "value" to 1
            value = 1;

            // Creates a new Handler and posts a delayed Runnable to start the ResultGame Activity after a 2 second delay
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(ElementsGameOnline.this, ResultGame.class);
                    intent.putExtra("result", value);
                    startActivity(intent);
                    finish();
                    deleteRoom(roomName);
                }
            }, 2000); // 2000 milliseconds = 2 seconds delay
        }else if(election == 2 && rival == 1 || election == 3 && rival == 2 || election == 1 && rival == 3){
            // If the user wins, sets the text of the tvResult TextView to "Victory"
            tvResult.setText(R.string.victory);

            // Sets the value of "value" to 0
            value = 0;

            // Creates a new Handler and posts a delayed Runnable to start the ResultGame Activity after a 2 second delay
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(ElementsGameOnline.this, ResultGame.class);
                    intent.putExtra("result", value);
                    startActivity(intent);
                    finish();
                    deleteRoom(roomName);
                }
            }, 2000); // 2000 milliseconds = 2 seconds delay
        }else {
            // If the game is a draw, sets the text of the tvResult TextView to "Draw"
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

    // This method restarts the game by enabling all the buttons, resetting the result text view, initializing the chronometer,
    // resetting the player and machine foregrounds, and setting the value to zero.
    private void restartGame() {
        btWater.setEnabled(true);
        btFire.setEnabled(true);
        btIce.setEnabled(true);

        int color = getResources().getColor(R.color.white);
        btFire.setCardBackgroundColor(color);
        btIce.setCardBackgroundColor(color);
        btWater.setCardBackgroundColor(color);

        tvResult.setText("");
        initCrono();
        player.setForeground(null);
        machine.setForeground(ContextCompat.getDrawable(ElementsGameOnline.this, R.drawable.question));
        value = 0;
    }

    // This method overrides the default behavior of the back button press in the activity
    @Override
    public void onBackPressed() {
        if (charge){
            // Cancels the countdown timer associated with the activity
            countDownTimer.cancel();
            // Calls the parent class method to handle the back button press
            deleteRoom(roomName);
            super.onBackPressed();
            finish();
        }
    }

    private void disableBt(int id){
        btWater.setEnabled(false);
        btFire.setEnabled(false);
        btIce.setEnabled(false);

        int color = getResources().getColor(R.color.light_blue);

        if(id == 1){
            btFire.setCardBackgroundColor(color);
        }else if(id == 2){
            btWater.setCardBackgroundColor(color);
        }else if(id == 3){
            btIce.setCardBackgroundColor(color);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        onExit = database.getReference("rooms/"+roomName+"/status");
        message = "exited";
        onExit.setValue(message);
        onExit.setValue(message);
    }



    private void setOnExit(){
        value=5;
        onExit.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                System.out.println(snapshot.getValue(String.class));
                if(snapshot.getValue(String.class) != null){
                    System.out.println(snapshot.getValue(String.class));
                    if(snapshot.getValue(String.class).equals("exited")){
                        System.out.println("Open Result Game: Lost Connection");
                        Intent intent = new Intent(ElementsGameOnline.this, ResultGame.class);
                        intent.putExtra("result", value);
                        startActivity(intent);
                        finish();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Cositas");
            }
        });
    }

    private void deleteRoom(String roomName) {
        database.getReference("rooms/" + roomName).removeValue();
    }
}