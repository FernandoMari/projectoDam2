package org.proven.decisions2.Games;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
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

import java.util.Random;

public class PenaltiesGameOnline extends Activity {

    // Declaration of CardView buttons to touch to play and CountDownTimer to the time
    CardView btDown, btRight, btLeft, btRightUp, btLeftUp;
    CountDownTimer countDownTimer;

    // Declaration of integer variables for keeping score and round count
    int electionPlayer, electionMachine, golPlayer = 0, golMachine = 0, round = 0,round2 = 0, value = 0;

    // Declaration of TextViews for displaying score, timer and game result
    TextView tvTimer, tvGolsPlayer, tvGolsMachine, tvResult;

    // Boolean variables to keep track of player and goalie turn, game finish and win/loss state
    boolean turnPlayer, turnGoalie, finish = false, win = false, lose = false;

    // Declaration of ImageViews for penalties points, robot, ball and other game elements
    ImageView p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, robot, ball;

    String playerName;
    String roomName;
    String role;
    String message;

    FirebaseDatabase database;
    DatabaseReference guestEle;
    DatabaseReference hostEle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.penaltis_animation_layout);

        // Delayed post to run code after 6.5 seconds
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                // Set content view to the main layout
                setContentView(R.layout.penaltis_layout);


                // Initialize UI elements
                instantiateElements();

                // Set initial scores
                tvGolsMachine.setText(""+golMachine);
                tvGolsPlayer.setText(""+golPlayer);

                // Initialize turn and timer
                //initTurn();
                earlyerInit();
                initCrono();

                // Set up button listeners
                btDown.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendDepending(3);
                        electionPlayer = 3;
                        disable(3);
                    }
                });

                btRight.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendDepending(5);
                        electionPlayer = 5;
                        disable(5);
                    }
                });
                btLeft.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendDepending(2);
                        electionPlayer = 2;
                        disable(2);
                    }
                });

                btRightUp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendDepending(4);
                        electionPlayer = 4;
                        disable(4);
                    }
                });

                btLeftUp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendDepending(1);
                        electionPlayer = 1;
                        disable(1);
                    }
                });
            }
        }, 6500); // 6500 milliseconds = 6.5 seconds delay
    }

    // This method is responsible for initializing all the elements of the UI.
    private void instantiateElements() {
        // Finding the views with their respective IDs and assigning them to their corresponding variables.
        btDown = findViewById(R.id.down);
        btRight = findViewById(R.id.right);
        btLeft = findViewById(R.id.left);
        btRightUp = findViewById(R.id.right_up);
        btLeftUp = findViewById(R.id.left_up);
        tvTimer = findViewById(R.id.timer);
        tvGolsPlayer = findViewById(R.id.gols1);
        tvGolsMachine = findViewById(R.id.gols2);
        tvResult = findViewById(R.id.result);

        database = FirebaseDatabase.getInstance();

        // Finding the views of the penalties points, the robot, and the ball and assigning them to their corresponding variables.
        p1 = findViewById(R.id.p1);
        p2 = findViewById(R.id.p2);
        p3 = findViewById(R.id.p3);
        p4 = findViewById(R.id.p4);
        p5 = findViewById(R.id.p5);
        p6 = findViewById(R.id.p6);
        p7 = findViewById(R.id.p7);
        p8 = findViewById(R.id.p8);
        p9 = findViewById(R.id.p9);
        p10 = findViewById(R.id.p10);
        robot = findViewById(R.id.robot);
        ball = findViewById(R.id.ball);
    }

    public void earlyerInit(){
        SharedPreferences preferences = getSharedPreferences("PREFS",0);
        playerName = preferences.getString("playerName","");

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            roomName = extras.getString("roomName");

            if(roomName.equals(playerName)){
                role="host";

                turnPlayer = true;
                // Setting the text of a TextView to a string resource with the message "You are player"
                tvResult.setText(getString(R.string.you_are_player));
            }else{
                role="guest";

                turnGoalie = true;
                // Setting the text of a TextView to a string resource with the message "You are goalkeeper"
                tvResult.setText(getString(R.string.you_are_goalkeeper));
            }
        }
    }

    private void sendDepending(int i){
        if(role.equals("guest")){
            guestEle = database.getReference("rooms/"+roomName+"/message");
            message = role+":"+i;
            addRoomEventListener();
            guestEle.setValue(message);

        }else if(role.equals("host")){
            hostEle = database.getReference("rooms/"+roomName+"/hostele");
            message = role+":"+i;
            getHostEventListener();
            hostEle.setValue(message);
        }
    }

    private void forceNonSelected(){
        guestEle = database.getReference("rooms/"+roomName+"/message");
        message = "guest:"+0;
        electionMachine=0;
        guestEle.setValue(message);

        hostEle = database.getReference("rooms/"+roomName+"/hostele");
        message = "host:"+0;
        electionPlayer=0;
        hostEle.setValue(message);
    }

    private void addRoomEventListener(){
        guestEle.addValueEventListener(new ValueEventListener() {
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
                        if(alter.matches("\\d+") && electionPlayer != 0){

                            electionMachine = Integer.parseInt(alter);
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
                        if(alter.matches("\\d+") && electionPlayer != 0){
                            //aqui tengo que quiza crear una variable
                            electionMachine = Integer.parseInt(alter);
                        }
                    }
                }else if(role.equals("host")){
                    guestEle = database.getReference("rooms/"+roomName+"/message");
                    addRoomEventListener();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    // Method to initialize the countdown timer
    private void initCrono(){
        // Create a new CountDownTimer object with a duration of 15 seconds and a tick interval of 1 second
        countDownTimer = new CountDownTimer(2000, 1000){

            @Override
            // Method to be called on each tick of the countdown timer
            public void onTick(long time) {
                // Calculate the remaining time in seconds
                long segPendiente=time/1000;
                // Set the text of the tvTimer TextView to show the remaining time in seconds
                tvTimer.setText(getString(R.string.time)+": "+segPendiente);
            }

            @Override
            // Method to be called when the countdown timer finishes
            public void onFinish() {
                // Create a new Random object
                Random rand = new Random();
                int ra = rand.nextInt(5)+1;

                if(electionMachine != 0 && electionPlayer != 0){
                    checkTirada();
                    checkWinDelayed();
                    restartColor();
                    System.out.println("HOST: "+electionPlayer);
                    System.out.println("GUEST: "+electionMachine);

                    // Switch the turn between the player and the goalie.
                    if (role.equals("host")) {
                        if (turnPlayer) {
                            turnPlayer = false;
                            turnGoalie = true;
                            tvResult.setText(R.string.you_are_goalkeeper);
                        }
                        if (turnGoalie) {
                            turnPlayer = true;
                            turnGoalie = false;
                            tvResult.setText(R.string.you_are_player);
                        }

                    }
                    if (role.equals("guest")){
                        if (turnPlayer){
                            turnPlayer = false;
                            turnGoalie = true;
                            tvResult.setText(R.string.you_are_goalkeeper);
                        }
                        if (turnGoalie){
                            turnPlayer = true;
                            turnGoalie = false;
                            tvResult.setText(R.string.you_are_player);
                        }

                    }
                    System.out.println("HOST ROL " + tvResult.getText());
                    System.out.println("GUEST ROL " + tvResult.getText());

                }else if(electionPlayer == 0 && electionMachine == 0){
                    //sendDepending(ra);
                    reset();
                    System.out.println("Both no select");
                    System.out.println("HOST: "+electionPlayer);
                    System.out.println("GUEST: "+electionMachine);
                }else if(electionMachine == 0){
                    //sendDepending(ra);
                    restartColor();
                    reset();
                    System.out.println("Guest no select");
                    System.out.println("HOST: "+electionPlayer);
                    System.out.println("GUEST: "+electionMachine);
                }else if (electionPlayer == 0){
                    //sendDepending(ra);
                    restartColor();
                    reset();
                    System.out.println("Host no select");
                    System.out.println("HOST: "+electionPlayer);
                    System.out.println("GUEST: "+electionMachine);
                }
                forceNonSelected();

            }
        }.start();

    }


    // Method to check the outcome of the player's move
    public void checkTirada(){
        // Disable all buttons
        btDown.setEnabled(false);
        btRight.setEnabled(false);
        btLeft.setEnabled(false);
        btRightUp.setEnabled(false);
        btLeftUp.setEnabled(false);
        // Cancel the countdown timer
        countDownTimer.cancel();
        // Hide the ball ImageView
        ball.setVisibility(View.INVISIBLE);
        // Check if it's the player's turn
        if(role.equals("host")){
            if (turnGoalie) {
                // Increase the round counter
                round++;
                // Set the robot ImageView to show the player's chosen element
                setRobot();
                // Set the ball ImageView to the player's position
                setBall();
                // Check if the player's element is different from the machine player's element
                if (electionPlayer != electionMachine) {
                    // Increase the machine player's score
                    golMachine++;
                    System.out.println("Guest goals: " + golMachine);
                    // Set the text of the tvGolsMachine TextView to show the machine player's score
                    tvGolsMachine.setText(Integer.toString(golMachine));
                    // Set the gol ImageView to show the machine player's goal
                    setGol(round);
                } else {
                    // Set the error ImageView to show the machine player's error
                    setError(round);
                }
            }else if (turnPlayer){
                // Increase the round counter
                round2++;
                // Set the robot ImageView to show the player's chosen element
                setRobot();
                // Set the ball ImageView to the player's position
                setBall();
                // Check if the player's element is different from the machine player's element
                if (electionPlayer != electionMachine) {
                    // Increase the machine player's score
                    golPlayer++;
                    System.out.println("Host goals: " + golPlayer);
                    // Set the text of the tvGolsMachine TextView to show the machine player's score
                    tvGolsPlayer.setText(Integer.toString(golPlayer));
                    // Set the gol ImageView to show the machine player's goal
                    setGol(round2);
                } else {
                    // Set the error ImageView to show the machine player's error
                    setError(round2);
                }
            }
        }else if(role.equals("guest")){
            if (turnGoalie) {
                // Increase the round counter
                round2++;
                // Set the robot ImageView to show the player's chosen element
                setRobot();
                // Set the ball ImageView to the player's position
                setBall();
                // Check if the player's element is different from the machine player's element
                if (electionPlayer != electionMachine) {
                    // Increase the host player's score
                    golPlayer++;
                    System.out.println("Host goals: " + golPlayer);
                    // Set the text of the tvGolsMachine TextView to show the machine player's score
                    tvGolsMachine.setText(Integer.toString(golPlayer));
                    // Set the gol ImageView to show the machine player's goal
                    setGol(round2);
                } else {
                    // Set the error ImageView to show the machine player's error
                    setError(round2);
                }
            }else if (turnPlayer){
                // Increase the round counter
                round++;
                // Set the robot ImageView to show the player's chosen element
                setRobot();
                // Set the ball ImageView to the player's position
                setBall();
                // Check if the player's element is different from the machine player's element
                if (electionPlayer != electionMachine) {
                    // Increase the machine player's score
                    golMachine++;
                    System.out.println("Guest goals: " + golMachine);
                    // Set the text of the tvGolsMachine TextView to show the machine player's score
                    tvGolsPlayer.setText(Integer.toString(golMachine));
                    // Set the gol ImageView to show the machine player's goal
                    setGol(round);
                } else {
                    // Set the error ImageView to show the machine player's error
                    setError(round);
                }
            }
        }
    }

    // This method uses a Handler to delay the execution of checkwin() by 1000 milliseconds
    public void checkWinDelayed(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkwin();
            }
        }, 1000); // delay in milliseconds
    }

    // This method sets the background resource of the corresponding ImageView based on the value of the round and turnPlayer variables
    public void setGol(int round){
        if(round==1 && turnPlayer){
            p1.setBackgroundResource(R.drawable.ic_circlecheck_green);
        }else if (round==2 && turnPlayer){
            p2.setBackgroundResource(R.drawable.ic_circlecheck_green);
        }else if (round==3 && turnPlayer) {
            p3.setBackgroundResource(R.drawable.ic_circlecheck_green);
        }else if (round==4 && turnPlayer){
            p4.setBackgroundResource(R.drawable.ic_circlecheck_green);
        }else if (round==5 && turnPlayer) {
            p5.setBackgroundResource(R.drawable.ic_circlecheck_green);
        }
        if(round==1 && turnGoalie){
            p6.setBackgroundResource(R.drawable.ic_circlecheck_green);
        }else if (round==2 && turnGoalie){
            p7.setBackgroundResource(R.drawable.ic_circlecheck_green);
        }else if (round==3 && turnGoalie) {
            p8.setBackgroundResource(R.drawable.ic_circlecheck_green);
        }else if (round==4 && turnGoalie){
            p9.setBackgroundResource(R.drawable.ic_circlecheck_green);
        }else if (round==5 && turnGoalie) {
            p10.setBackgroundResource(R.drawable.ic_circlecheck_green);
        }
    }

    // This method sets an error in the UI for a given round and player turn.
    public void setError(int round){
        // If it's the player's turn, set the appropriate background resource based on the round number.
        if(round==1 && turnPlayer){
            p1.setBackgroundResource(R.drawable.ic_circlecheck_red);
        }else if (round==2 && turnPlayer){
            p2.setBackgroundResource(R.drawable.ic_circlecheck_red);
        }else if (round==3 && turnPlayer) {
            p3.setBackgroundResource(R.drawable.ic_circlecheck_red);
        }else if (round==4 && turnPlayer){
            p4.setBackgroundResource(R.drawable.ic_circlecheck_red);
        }else if (round==5 && turnPlayer) {
            p5.setBackgroundResource(R.drawable.ic_circlecheck_red);
        }
        // If it's the goalie's turn, set the appropriate background resource based on the round number.
        if(round==1 && turnGoalie){
            p6.setBackgroundResource(R.drawable.ic_circlecheck_red);
        }else if (round==2 && turnGoalie){
            p7.setBackgroundResource(R.drawable.ic_circlecheck_red);
        }else if (round==3 && turnGoalie) {
            p8.setBackgroundResource(R.drawable.ic_circlecheck_red);
        }else if (round==4 && turnGoalie){
            p9.setBackgroundResource(R.drawable.ic_circlecheck_red);
        }else if (round==5 && turnGoalie) {
            p10.setBackgroundResource(R.drawable.ic_circlecheck_red);
        }
    }

    // This method sets the image resource of the robot based on the selected machine or player and turn
    public void setRobot() {
        if (electionMachine == 1 && turnPlayer) {
            // Set the image resource to the robot facing left and up
            robot.setImageResource(R.drawable.robot_left_up);
        } else if (electionMachine == 2 && turnPlayer) {
            // Set the image resource to the robot facing left
            robot.setImageResource(R.drawable.robot_left);
        } else if (electionMachine == 3 && turnPlayer) {
            // Set the image resource to the robot facing up
            robot.setImageResource(R.drawable.robot_up);
        } else if (electionMachine == 4 && turnPlayer) {
            // Set the image resource to the robot facing right and up
            robot.setImageResource(R.drawable.robot_right_up);
        } else if (electionMachine == 5 && turnPlayer) {
            // Set the image resource to the robot facing right
            robot.setImageResource(R.drawable.robot_right);
        }
        if (electionPlayer == 1 && turnGoalie) {
            // Set the image resource to the robot facing left and up
            robot.setImageResource(R.drawable.robot_left_up);
        } else if (electionPlayer == 2 && turnGoalie) {
            // Set the image resource to the robot facing left
            robot.setImageResource(R.drawable.robot_left);
        } else if (electionPlayer == 3 && turnGoalie) {
            // Set the image resource to the robot facing up
            robot.setImageResource(R.drawable.robot_up);
        } else if (electionPlayer == 4 && turnGoalie) {
            // Set the image resource to the robot facing right and up
            robot.setImageResource(R.drawable.robot_right_up);
        } else if (electionPlayer == 5 && turnGoalie) {
            // Set the image resource to the robot facing right
            robot.setImageResource(R.drawable.robot_right);
        }
    }

    // This method sets the ball on the selected button based on the chosen player or machine and the current turn.
    public void setBall(){
        // If the chosen machine is 1 and it's the goalie's turn, set the ball on the top-left button.
        if (electionMachine==1 && turnGoalie){
            btLeftUp.setForeground(ContextCompat.getDrawable(this, R.drawable.ball));
        }
        // If the chosen machine is 2 and it's the goalie's turn, set the ball on the left button.
        else if (electionMachine==2 && turnGoalie){
            btLeft.setForeground(ContextCompat.getDrawable(this, R.drawable.ball));
        }
        // If the chosen machine is 3 and it's the goalie's turn, set the ball on the bottom button.
        else if (electionMachine==3 && turnGoalie){
            btDown.setForeground(ContextCompat.getDrawable(this, R.drawable.ball));
        }
        // If the chosen machine is 4 and it's the goalie's turn, set the ball on the top-right button.
        else if (electionMachine==4 && turnGoalie){
            btRightUp.setForeground(ContextCompat.getDrawable(this, R.drawable.ball));
        }
        // If the chosen machine is 5 and it's the goalie's turn, set the ball on the right button.
        else if (electionMachine==5 && turnGoalie){
            btRight.setForeground(ContextCompat.getDrawable(this, R.drawable.ball));
        }
        // If the chosen player is 1 and it's the player's turn, set the ball on the top-left button.
        if (electionPlayer==1 && turnPlayer){
            btLeftUp.setForeground(ContextCompat.getDrawable(this, R.drawable.ball));
        }
        // If the chosen player is 2 and it's the player's turn, set the ball on the left button.
        else if (electionPlayer==2 && turnPlayer){
            btLeft.setForeground(ContextCompat.getDrawable(this, R.drawable.ball));
        }
        // If the chosen player is 3 and it's the player's turn, set the ball on the bottom button.
        else if (electionPlayer==3 && turnPlayer){
            btDown.setForeground(ContextCompat.getDrawable(this, R.drawable.ball));
        }
        // If the chosen player is 4 and it's the player's turn, set the ball on the top-right button.
        else if (electionPlayer==4 && turnPlayer){
            btRightUp.setForeground(ContextCompat.getDrawable(this, R.drawable.ball));
        }
        // If the chosen player is 5 and it's the player's turn, set the ball on the right button.
        else if (electionPlayer==5 && turnPlayer){
            btRight.setForeground(ContextCompat.getDrawable(this, R.drawable.ball));
        }
    }


    // This method checks if the player wins or loses the penalty shootout game
    private void checkwin() {
        // Reset the game state
        reset();

        // Check if the player wins or loses by 4 goals or more difference
        if (golPlayer - golMachine >= 4) {
            finish = true;
            tvResult.setText(R.string.you_win);
            win = true;
        } else if(golMachine - golPlayer >= 4){
            finish = true;
            tvResult.setText(R.string.you_lost);
            lose = true;
        }
        // Check if the player wins or loses by 3 goals or more difference
        if (golPlayer - golMachine >= 3) {
            finish = true;
            tvResult.setText(R.string.you_win);
            win = true;
        } else if(golMachine - golPlayer >= 3){
            finish = true;
            tvResult.setText(R.string.you_lost);
            lose = true;
        }
        // Check if the player wins or loses at the end of the 5 rounds
        if (round==5 || round2==5){
            if (golPlayer == 5 && golMachine < 4){
                tvResult.setText(R.string.you_win);
                finish = true;
                win = true;
            }else if (golMachine == 5 && golPlayer < 4){
                tvResult.setText(R.string.you_lost);
                finish = true;
                lose = true;
            }
            if (golPlayer - golMachine >= 2) {
                finish = true;
                tvResult.setText(R.string.you_win);
                win = true;
            } else if(golMachine - golPlayer >= 2){
                finish = true;
                tvResult.setText(R.string.you_lost);
                lose = true;
            }
            if (golPlayer - golMachine >= 3) {
                finish = true;
                tvResult.setText(R.string.you_win);
                win = true;
            } else if(golMachine - golPlayer >= 3){
                finish = true;
                tvResult.setText(R.string.you_lost);
                lose = true;
            }
            if (round==5 && round2==5){
                if (golPlayer - golMachine >= 1) {
                    finish = true;
                    tvResult.setText(R.string.you_win);
                    win = true;
                } else if(golMachine - golPlayer >= 1){
                    finish = true;
                    tvResult.setText(R.string.you_lost);
                    lose = true;
                }
            }
        }

        // Check if the player wins or loses after 5 rounds
        if (round > 5 || round2 > 5) {
            if (golPlayer - golMachine >= 1) {
                finish = true;
                tvResult.setText(R.string.you_win);
                win = true;
            } else if(golMachine - golPlayer >= 1){
                finish = true;
                tvResult.setText(R.string.you_lost);
                lose = true;
            }
        }

        // Disable all buttons and stop the countdown timer if the game is finished
        if (finish){
            btDown.setEnabled(false);
            btRight.setEnabled(false);
            btLeft.setEnabled(false);
            btRightUp.setEnabled(false);
            btLeftUp.setEnabled(false);
            countDownTimer.cancel();
            tvTimer.setVisibility(View.GONE);
        }

        // Launch the result activity after 2 seconds delay if the player wins or loses
        if (win){
            value = 0;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(PenaltiesGameOnline.this, ResultGame.class);
                    intent.putExtra("result", value);
                    startActivity(intent);
                    finish();
                }
            }, 2000); // 2000 milliseconds = 2 seconds delay
        }else if (lose) {
            value = 1;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(PenaltiesGameOnline.this, ResultGame.class);
                    intent.putExtra("result", value);
                    startActivity(intent);
                    finish();
                }
            }, 2000); // 2000 milliseconds = 2 seconds delay
        }
    }

    // This method is used to reset the game and start a new round.
    public void reset(){
        // Start the countdown timer for the new round.
        countDownTimer.start();

        // Enable all the buttons for the player to control the robot.
        btDown.setEnabled(true);
        btRight.setEnabled(true);
        btLeft.setEnabled(true);
        btRightUp.setEnabled(true);
        btLeftUp.setEnabled(true);

        // Set the robot image to the default "down" direction.
        robot.setImageResource(R.drawable.robot_down);

        // Set the foreground images of the buttons to their default values.
        btLeftUp.setForeground(ContextCompat.getDrawable(this, R.drawable.left_up));
        btLeft.setForeground(ContextCompat.getDrawable(this, R.drawable.left));
        btDown.setForeground(ContextCompat.getDrawable(this, R.drawable.stay));
        btRightUp.setForeground(ContextCompat.getDrawable(this, R.drawable.right_up));
        btRight.setForeground(ContextCompat.getDrawable(this, R.drawable.right));

        // Make the ball visible again.
        ball.setVisibility(View.VISIBLE);


    }

    public void disable(int op){
        btDown.setEnabled(false);
        btRight.setEnabled(false);
        btLeft.setEnabled(false);
        btRightUp.setEnabled(false);
        btLeftUp.setEnabled(false);

        int color = getResources().getColor(R.color.light_blue);

        if(op == 1){
            btLeftUp.setCardBackgroundColor(color);
        }else if(op == 2){
            btLeft.setCardBackgroundColor(color);
        }else if(op == 3){
            btDown.setCardBackgroundColor(color);
        }else if(op == 4){
            btRightUp.setCardBackgroundColor(color);
        }else if(op == 5){
            btRight.setCardBackgroundColor(color);
        }
    }

    public void restartColor(){
        int color = getResources().getColor(R.color.white);

        btLeftUp.setCardBackgroundColor(color);
        btLeft.setCardBackgroundColor(color);
        btDown.setCardBackgroundColor(color);
        btRightUp.setCardBackgroundColor(color);
        btRight.setCardBackgroundColor(color);
    }

    // This method overrides the default behavior of the back button press in the activity
    @Override
    public void onBackPressed() {
        // Cancels the countdown timer associated with the activity
        countDownTimer.cancel();
        // Calls the parent class method to handle the back button press
        super.onBackPressed();
    }
}