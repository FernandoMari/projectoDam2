package org.proven.decisions2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CreatorsActivity extends Activity {

    Button btHome, btSettings, btFriends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.creators_layout);

        btHome = findViewById(R.id.btHome);
        btSettings = findViewById(R.id.btSettings);
        btFriends = findViewById(R.id.btFriends);

        btHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CreatorsActivity.this, SocialInterface.class));
            }
        });

        btSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CreatorsActivity.this, SettingsActivity.class));
            }
        });

        btFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CreatorsActivity.this, FriendsActivity.class));
            }
        });
    }
}
