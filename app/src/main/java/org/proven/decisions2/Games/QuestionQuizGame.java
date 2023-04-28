package org.proven.decisions2.Games;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import org.proven.decisions2.R;
import org.proven.decisions2.Settings.AppCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class QuestionQuizGame extends AppCompat {

    private int value, questionNumber;
    private int lifes = 3;

    private CardView option1Button, option2Button, option3Button, option4Button, cardLifes;
    private TextView tv1, tv2, tv3, tv4, tvTimer, tvQuestion, tvCategory;
    ImageView life1, life2, life3;
    int totalQuestions;
    int currentQuestionIndex = 0;
    int selectedAnswer = 0;
    CountDownTimer countDownTimer;
    boolean finish = false, win = false, lose = false, charge=false;
    private ArrayList<Integer> shownQuestions = new ArrayList<Integer>();
    ConstraintLayout constraintLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_animation_layout);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setContentView(R.layout.quiz_layout);
                charge=true;
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
                    constraintLayout = findViewById(R.id.quizLayout);

                    AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
                    animationDrawable.setEnterFadeDuration(2500);
                    animationDrawable.setExitFadeDuration(5000);
                    animationDrawable.start();
                } else {
                    constraintLayout = findViewById(R.id.quizLayout);
                    constraintLayout.setBackgroundColor(getResources().getColor(R.color.dark_blue));
                }

                initElements();

                numRandomTotalQuestions();

                showQuestions();

                initCrono();
            }
        }, 6500); // 6500 milliseconds = 6.5 seconds delay
    }

    private void initElements() {
        option1Button = findViewById(R.id.btAnswer1);
        option2Button = findViewById(R.id.btAnswer2);
        option3Button = findViewById(R.id.btAnswer3);
        option4Button = findViewById(R.id.btAnswer4);

        tv1 = findViewById(R.id.tvAnswer1);
        tv2 = findViewById(R.id.tvAnswer2);
        tv3 = findViewById(R.id.tvAnswer3);
        tv4 = findViewById(R.id.tvAnswer4);
        tvTimer = findViewById(R.id.tvTimer);
        tvQuestion = findViewById(R.id.tvQuestion);
        tvCategory = findViewById(R.id.tvCategory);

        life1 = findViewById(R.id.life1);
        life2 = findViewById(R.id.life2);
        life3 = findViewById(R.id.life3);
        cardLifes = findViewById(R.id.cardLifes);
    }


    private void numRandomTotalQuestions(){
        Random random = new Random();
        totalQuestions = random.nextInt(6) + 5;
        if (totalQuestions>Question.question.length){
            totalQuestions=Question.question.length;
        }
    }


    private void loadRandomQuestions() {
        if (questionNumber == totalQuestions && lifes >= 1) {
            tvTimer.setText(getString(R.string.you_win));
            finish = true;
            win = true;
        } else if (lifes == 0) {
            tvTimer.setText(getString(R.string.you_lose));
            finish = true;
            lose = true;
        } else if (!finish) {
            Random random = new Random();
            int index;
            if (shownQuestions.size() >= Question.question.length) {
                // Ya no quedan preguntas nuevas, elegir de las preguntas ya mostradas
                index = shownQuestions.get(random.nextInt(shownQuestions.size()));
            } else {
                // Todavía quedan preguntas nuevas
                do {
                    index = random.nextInt(Question.question.length);
                } while (shownQuestions.contains(index));
                shownQuestions.add(index);
            }

            currentQuestionIndex = index;
            tvQuestion.setText(Question.question[currentQuestionIndex]);
            tv1.setText(Question.answers[currentQuestionIndex][0]);
            tv2.setText(Question.answers[currentQuestionIndex][1]);
            tv3.setText(Question.answers[currentQuestionIndex][2]);
            tv4.setText(Question.answers[currentQuestionIndex][3]);
            tvCategory.setText(Question.category[currentQuestionIndex]);
        }


    }


    private void initCrono(){
        countDownTimer = new CountDownTimer(30000, 1000){

            @Override
            public void onTick(long time) {
                long segPendiente=time/1000;
                tvTimer.setText(getString(R.string.time)+": "+segPendiente);
            }

            // If the timer finishes, randomly select an element for the rival and check for a win
            @Override
            public void onFinish() {
                Random rand = new Random();
                selectedAnswer = rand.nextInt(4) + 1;
                changeCardColor();
                checkAnswerDelayed();
            }
        }.start();
    }


    private void showQuestions() {
        resetButtons();

        // Actualizar el número de pregunta actual

        TextView questionNumberTextView = findViewById(R.id.tvQuestionNumber);
        questionNumberTextView.setText(questionNumber+"/"+ totalQuestions);

        loadRandomQuestions();

        // Establecer el OnClickListener para cada botón de opción de respuesta
        option1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stop();
                selectedAnswer=0;
                changeCardColor();
                checkAnswerDelayed();
            }
        });

        option2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stop();
                selectedAnswer=1;
                changeCardColor();
                checkAnswerDelayed();
            }
        });

        option3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stop();
                selectedAnswer=2;
                changeCardColor();
                checkAnswerDelayed();
            }
        });

        option4Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stop();
                selectedAnswer=3;
                changeCardColor();
                checkAnswerDelayed();
            }
        });

    }

    public void checkAnswerDelayed() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkAnswer();
            }
        }, 1000); // delay in milliseconds
    }

    private void checkAnswer() {


        // Mostrar la siguiente pregunta
         if (selectedAnswer == Question.correctAnswer[currentQuestionIndex] && !finish) {
            questionNumber++;
             currentQuestionIndex++;
             showQuestions();
             countDownTimer.start();
         } else if(selectedAnswer != Question.correctAnswer[currentQuestionIndex]){
            updatelifes();
            System.out.println("Lifes: "+ lifes);
             currentQuestionIndex++;
             showQuestions();
             countDownTimer.start();
         }

        System.out.println(questionNumber+""+totalQuestions);


        if (finish){
            // Disable all buttons
            option1Button.setEnabled(false);
            option2Button.setEnabled(false);
            option3Button.setEnabled(false);
            option4Button.setEnabled(false);

            // Stop the countdown timer
            countDownTimer.cancel();
        }

        if (win){
            value = 0;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(QuestionQuizGame.this, ResultGame.class);
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
                    Intent intent = new Intent(QuestionQuizGame.this, ResultGame.class);
                    intent.putExtra("result", value);
                    startActivity(intent);
                    finish();
                }
            }, 2000); // 2000 milliseconds = 2 seconds delay
        }
    }

    private void updatelifes(){
        lifes--;
        if (lifes==1){
            life3.setVisibility(View.GONE);
            life2.setVisibility(View.GONE);
        }else if (lifes==2){
            life3.setVisibility(View.GONE);
        }else if(lifes==0){
            life3.setVisibility(View.GONE);
            life2.setVisibility(View.GONE);
            life1.setVisibility(View.GONE);
            cardLifes.setVisibility(View.GONE);
        }
    }

    private void changeCardColor(){
        CardView selectedCardView = null;
        if (selectedAnswer == 0) {
            selectedCardView = findViewById(R.id.btAnswer1);
        } else if (selectedAnswer == 1) {
            selectedCardView = findViewById(R.id.btAnswer2);
        } else if (selectedAnswer == 2) {
            selectedCardView = findViewById(R.id.btAnswer3);
        } else if (selectedAnswer == 3){
            selectedCardView = findViewById(R.id.btAnswer4);
        }

        // Verificar si la respuesta seleccionada es la correcta
        boolean isCorrect = (selectedAnswer == Question.correctAnswer[currentQuestionIndex]);

        // Cambiar el color del CardView según si la respuesta es correcta o no
        int color = isCorrect ? Color.GREEN : Color.RED;
        selectedCardView.setCardBackgroundColor(color);
    }

    public void stop(){
        countDownTimer.cancel();
        option1Button.setEnabled(false);
        option2Button.setEnabled(false);
        option3Button.setEnabled(false);
        option4Button.setEnabled(false);
    }

    public void resetButtons(){

        option1Button.setEnabled(true);
        option2Button.setEnabled(true);
        option3Button.setEnabled(true);
        option4Button.setEnabled(true);

        option1Button.setCardBackgroundColor(getResources().getColor(android.R.color.white));
        option2Button.setCardBackgroundColor(getResources().getColor(android.R.color.white));
        option3Button.setCardBackgroundColor(getResources().getColor(android.R.color.white));
        option4Button.setCardBackgroundColor(getResources().getColor(android.R.color.white));
    }

    // This method overrides the default behavior of the back button press in the activity
    @Override
    public void onBackPressed() {
        if (charge){
            // Cancels the countdown timer associated with the activity
            countDownTimer.cancel();
            // Calls the parent class method to handle the back button press
            super.onBackPressed();
        }
    }
}
