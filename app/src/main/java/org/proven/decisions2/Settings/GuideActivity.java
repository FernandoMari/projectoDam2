package org.proven.decisions2.Settings;

import android.animation.LayoutTransition;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.transition.ChangeScroll;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.proven.decisions2.Friends.FriendsActivity;
import org.proven.decisions2.R;
import org.proven.decisions2.SocialInterface;

public class GuideActivity extends Activity {

    TextView tvDetailsElements, tvDetailsPenaltis, tvDetailsQuiz;
    LinearLayout layoutExpand, layoutImgElements, layoutExpandPenaltis, layoutImgPenaltis, layoutExpandQuiz, layoutImgQuiz;
    Button btHome, btSettings, btFriends;
    ImageView waterImg, fireImg, iceImg, robot, ball;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.guide_layout);
        //Initialize the elements
        initializeElements();

        btHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GuideActivity.this, SocialInterface.class));
                finishAndRemoveTask();
            }
        });

        btSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GuideActivity.this, SettingsActivity.class));
                finishAndRemoveTask();
            }
        });

        btFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GuideActivity.this, FriendsActivity.class));
                finishAndRemoveTask();
            }
        });
    }
    /*Initialize the elements*/
    private void initializeElements() {
        btHome = findViewById(R.id.btHome);
        btSettings = findViewById(R.id.btSettings);
        btFriends = findViewById(R.id.btFriends);

        tvDetailsElements = findViewById(R.id.tvDetails);
        waterImg = findViewById(R.id.waterImg);
        fireImg = findViewById(R.id.fireImg);
        iceImg = findViewById(R.id.iceImg);
        layoutExpand = findViewById(R.id.layoutExpand);
        layoutImgElements = findViewById(R.id.layoutImg);
        layoutExpand.getLayoutTransition().enableTransitionType(LayoutTransition.APPEARING);
        layoutImgElements.getLayoutTransition().enableTransitionType(LayoutTransition.APPEARING);

        tvDetailsPenaltis = findViewById(R.id.tvDetailsPenaltis);
        robot = findViewById(R.id.robot);
        ball = findViewById(R.id.ball);
        layoutExpandPenaltis = findViewById(R.id.layoutExpandPenaltis);
        layoutImgPenaltis = findViewById(R.id.layoutImgPenaltis);
        layoutExpandPenaltis.getLayoutTransition().enableTransitionType(LayoutTransition.APPEARING);
        layoutImgPenaltis.getLayoutTransition().enableTransitionType(LayoutTransition.APPEARING);

        tvDetailsQuiz = findViewById(R.id.tvDetailsQuiz);
        layoutExpandQuiz = findViewById(R.id.layoutExpandQuiz);
        layoutImgQuiz = findViewById(R.id.layoutImgQuiz);
        layoutExpandQuiz.getLayoutTransition().enableTransitionType(LayoutTransition.APPEARING);
        layoutImgQuiz.getLayoutTransition().enableTransitionType(LayoutTransition.APPEARING);
    }

    /*Method for the expand the game guide */
    public void expandElements(View view) {
        int v = (tvDetailsElements.getVisibility() == View.GONE)? View.VISIBLE: View.GONE;
        TransitionManager.beginDelayedTransition(layoutExpand, new ChangeScroll() {
        });
        tvDetailsElements.setVisibility(v);

        int l = (layoutImgElements.getVisibility() == View.GONE) ? View.VISIBLE : View.GONE;
        TransitionManager.beginDelayedTransition(layoutImgElements, new ChangeScroll());
        layoutImgElements.setVisibility(l);

    }

    public void expandPenaltis(View view) {
        int v = (tvDetailsPenaltis.getVisibility() == View.GONE)? View.VISIBLE: View.GONE;
        TransitionManager.beginDelayedTransition(layoutExpandPenaltis, new ChangeScroll() {
        });
        tvDetailsPenaltis.setVisibility(v);

        int l = (layoutImgPenaltis.getVisibility() == View.GONE) ? View.VISIBLE : View.GONE;
        TransitionManager.beginDelayedTransition(layoutImgPenaltis, new ChangeScroll());
        layoutImgPenaltis.setVisibility(l);

    }

    public void expandQuiz(View view) {
        int v = (tvDetailsQuiz.getVisibility() == View.GONE)? View.VISIBLE: View.GONE;
        TransitionManager.beginDelayedTransition(layoutExpandQuiz, new ChangeScroll() {
        });
        tvDetailsQuiz.setVisibility(v);

        int l = (layoutImgQuiz.getVisibility() == View.GONE) ? View.VISIBLE : View.GONE;
        TransitionManager.beginDelayedTransition(layoutImgQuiz, new ChangeScroll());
        layoutImgQuiz.setVisibility(l);

    }
}
