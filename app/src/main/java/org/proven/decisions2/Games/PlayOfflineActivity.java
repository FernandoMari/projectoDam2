package org.proven.decisions2.Games;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.proven.decisions2.Friends.FriendsActivity;
import org.proven.decisions2.R;
import org.proven.decisions2.Settings.SettingsActivity;
import org.proven.decisions2.SocialInterface;

import java.util.Random;

public class PlayOfflineActivity extends Activity {

    Button btHome, btSettings, btFriends, btPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_offline_layout);


        btHome = findViewById(R.id.btHome);
        btSettings = findViewById(R.id.btSettings);
        btFriends = findViewById(R.id.btFriends);
        btPlay = findViewById(R.id.btPlay);

        btHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PlayOfflineActivity.this, SocialInterface.class));
            }
        });

        btSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PlayOfflineActivity.this, SettingsActivity.class));
            }
        });

        btFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PlayOfflineActivity.this, FriendsActivity.class));
            }
        });
        btPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random rand = new Random();
                int num = rand.nextInt(2);
                if (num == 0) {
                    startActivity(new Intent(PlayOfflineActivity.this, PenaltisGame.class));
                } else {
                    startActivity(new Intent(PlayOfflineActivity.this, ElementsGame.class));
                }
            }
        });
    }
}
