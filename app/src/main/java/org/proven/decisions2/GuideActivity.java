package org.proven.decisions2;

import android.animation.LayoutTransition;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.ChangeBounds;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GuideActivity extends Activity {

    TextView tvDetails;
    LinearLayout layoutExpand, layoutImg;
    Button btHome, btSettings, btFriends;
    ImageView waterImg, fireImg, iceImg;

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
        tvDetails = findViewById(R.id.tvDetails);
        waterImg = findViewById(R.id.waterImg);
        fireImg = findViewById(R.id.fireImg);
        iceImg = findViewById(R.id.iceImg);
        layoutExpand = findViewById(R.id.layoutExpand);
        layoutImg = findViewById(R.id.layoutImg);
        layoutExpand.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        layoutImg.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
    }

    /*Method for the expand the game guide */
    public void expand(View view) {
        int v = (tvDetails.getVisibility() == View.GONE) ? View.VISIBLE : View.GONE;
        TransitionManager.beginDelayedTransition(layoutExpand, new AutoTransition());
        tvDetails.setVisibility(v);

        int l = (layoutImg.getVisibility() == View.GONE) ? View.VISIBLE : View.GONE;
        TransitionManager.beginDelayedTransition(layoutImg, new ChangeBounds());
        layoutImg.setVisibility(l);

    }
}
