package org.proven.decisions2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.fragment.app.FragmentActivity;

import org.proven.decisions2.Friends.FriendsActivity;
import org.proven.decisions2.Games.ChooseModality;
import org.proven.decisions2.Settings.SettingsActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


public class SocialInterface extends FragmentActivity {
    private VerticalViewPager viewPager;
    private ViewPagerAdapter viewAdapter;
    //The buttons to navigate in the app
    Button btFriends, btDecisions, btSettings;
    //User authentication token
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_interface);
        //Initialize the elements
        initializeElements();
        //call the method
        readUser();
        btFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SocialInterface.this, FriendsActivity.class);
                readUser();
                Log.d("TAG", "userIdSocial: " + token);
                startActivity(intent);
                finish();

            }
        });
        btDecisions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SocialInterface.this, ChooseModality.class));
            }
        });

        btSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SocialInterface.this, SettingsActivity.class);
                Log.d("TAG", "userIdSocial: " + token);
                startActivity(intent);
                finish();
            }
        });
    }

    /*Initialize the elements*/
    private void initializeElements() {
        viewPager = (VerticalViewPager) findViewById(R.id.viewPager);
        viewAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewAdapter);
        btFriends = findViewById(R.id.btFriends);
        btDecisions = findViewById(R.id.btDecisions);
        btSettings = findViewById(R.id.btSettings);
    }

    /*Method to read the login token for use in the activity*/
    private void readUser() {
        File filename = new File(getFilesDir(), "token.txt");
        try {
            FileReader reader = new FileReader(filename);
            BufferedReader bufferedReader = new BufferedReader(reader);
            token = bufferedReader.readLine();
            bufferedReader.close();
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


}