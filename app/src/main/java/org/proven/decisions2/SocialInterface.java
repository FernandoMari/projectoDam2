package org.proven.decisions2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.fragment.app.FragmentActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


public class SocialInterface extends FragmentActivity {
        private VerticalViewPager viewPager;
        private ViewPagerAdapter viewAdapter;

        Button btFriends, btDecisions, btSettings;
        String username;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_social_interface);


            viewPager = (VerticalViewPager) findViewById(R.id.viewPager);
            viewAdapter = new ViewPagerAdapter(getSupportFragmentManager());
            viewPager.setAdapter(viewAdapter);
            btFriends = findViewById(R.id.btFriends);
            btDecisions = findViewById(R.id.btDecisions);
            btSettings = findViewById(R.id.btSettings);
//            Bundle extras = getIntent().getExtras();
//            if (extras != null) {
//                username = extras.getString("id");
//                Log.d("TAG", "userIdSocial: " + username);
//            }
            readUser(username);
            btFriends.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SocialInterface.this, FriendsActivity.class);
                    intent.putExtra("id", username);
                    readUser(username);
                    Log.d("TAG", "userIdSocial: " + username);
                    startActivity(intent);
                    finish();

                }
            });
            btDecisions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(SocialInterface.this, BeforePlayActivity.class));
                }
            });

            btSettings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SocialInterface.this, SettingsActivity.class);
                    intent.putExtra("id", username);
                    Log.d("TAG", "userIdSocial: " + username);
                    startActivity(intent);
                    finish();
                }
            });
        }

    private void readUser(String userId) {
        File filename = new File(getFilesDir(), "username.txt");
        try {
            FileReader reader = new FileReader(filename);
            BufferedReader bufferedReader = new BufferedReader(reader);
            username = bufferedReader.readLine();
            bufferedReader.close();
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


}