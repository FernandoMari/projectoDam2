package org.proven.decisions2.Settings;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.proven.decisions2.Friends.FriendsActivity;
import org.proven.decisions2.LoginAndRegister.MainActivity;
import org.proven.decisions2.R;
import org.proven.decisions2.SocialInterface;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class SettingsActivity extends Activity {
    //The buttons to navigate in the app and in the settings menu
    Button btFriends, btHome, btProfile, btPassword, btCreators, btLanguage, btGuide, btlogout, btEmail;
    //User authentication token
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        //Initialize the elements
        initializeElements();
        //call the method
        readUser();

        btHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, SocialInterface.class));
                readUser();
                finish();
            }
        });

        btFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, FriendsActivity.class));
                finish();
            }
        });

        btProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readUser();
                startActivity(new Intent(SettingsActivity.this, ProfileActivity.class));
                finish();
            }
        });

        btPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readUser();
                startActivity(new Intent(SettingsActivity.this, PasswordActivity.class));
                finish();
            }
        });

        btCreators.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readUser();
                startActivity(new Intent(SettingsActivity.this, CreatorsActivity.class));
            }
        });

        btEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, EmailActivity.class));
            }
        });

        btLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readUser();
                startActivity(new Intent(SettingsActivity.this, LanguageActivity.class));
                finish();
            }
        });
        btGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, GuideActivity.class));
            }
        });
        btlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, MainActivity.class));
                //change the checkbox in false for the logout the app
                SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("cbremember", "false");
                editor.apply();
                finishAffinity();
            }
        });


    }

    /*Initialize the elements*/
    private void initializeElements() {
        btHome = findViewById(R.id.btHome);
        btFriends = findViewById(R.id.btFriends);
        btProfile = findViewById(R.id.btProfile);
        btPassword = findViewById(R.id.btPassword);
        btCreators = findViewById(R.id.btCreators);
        btLanguage = findViewById(R.id.btLanguage);
        btlogout = findViewById(R.id.btLogout);
        btGuide = findViewById(R.id.btExplication);
        btEmail = findViewById(R.id.btEmail);

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

