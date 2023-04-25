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

public class ChooseModality extends Activity {

    Button btHome, btSettings, btFriends, btPlayOnline, btPlayOffline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_modality);


        btHome = findViewById(R.id.btHome);
        btSettings = findViewById(R.id.btSettings);
        btFriends = findViewById(R.id.btFriends);
        btPlayOnline = findViewById(R.id.btPlayOnline);
        btPlayOffline = findViewById(R.id.btPlayOffline);


        btHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChooseModality.this, SocialInterface.class));
            }
        });

        btSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChooseModality.this, SettingsActivity.class));
            }
        });

        btFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChooseModality.this, FriendsActivity.class));
            }
        });
        btPlayOnline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChooseModality.this, PlayOnlineActivity.class));
            }
        });

        btPlayOffline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChooseModality.this, PlayOfflineActivity.class));
            }
        });
    }
}

