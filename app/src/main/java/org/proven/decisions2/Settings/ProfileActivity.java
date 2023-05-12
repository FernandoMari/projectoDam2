package org.proven.decisions2.Settings;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.proven.decisions2.Friends.FriendsActivity;
import org.proven.decisions2.R;
import org.proven.decisions2.SocialInterface;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ProfileActivity extends Activity {
    //Button for the navigate in the change profile or in the app
    Button btFriends, btHome, btSettings, btAccept, btCancel;
    //inputUsername is to insert the username the user,
    EditText inputUsername;
    //Url for the http post request for the change username in the app
    String url = "http://143.47.249.102:7070/swichPasswordOrName";
    //new username for the user and token the user for the login in the app
    String token, newUsername;

//Falta capar que el nombre de usuario no sea uno que exista en la base de datos
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_layout);
        /*Initialize the elements*/
        initializeElements();
        //call the method
        readUser();


        btHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, SocialInterface.class));
            }
        });

        btFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, FriendsActivity.class));
            }
        });

        btSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(ProfileActivity.this, SettingsActivity.class));
            }
        });
        btAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeUsername();
            }
        });
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, SettingsActivity.class));
                finish();
            }
        });
    }

    /*Initialize the elements*/
    private void initializeElements() {
        inputUsername = findViewById(R.id.etUsername3);
        btHome = findViewById(R.id.btHome);
        btFriends = findViewById(R.id.btFriends);
        btSettings = findViewById(R.id.btSettings);
        btAccept = findViewById(R.id.btConfirm);
        btCancel = findViewById(R.id.btCancel);

    }

    /* Method to instantiate the UsernameChangeTask and start it */
    private void getFriends(String token) {
        new UsernameChangeTask().execute(token);
    }

    /*Method to change username by asking the user for the new username*/
    private void changeUsername() {
        newUsername = inputUsername.getText().toString();
        //check the new username is empty
        if (newUsername.isEmpty()) {
            inputUsername.setError("Enter new username");
        } else {
            //call the method for execute de asyncTask
            getFriends(token);
            //go back to activity settings
            startActivity(new Intent(ProfileActivity.this, SettingsActivity.class));

        }

    }

    /*Method to execute post change username for user*/
    private class UsernameChangeTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            token = params[0];
            OkHttpClient client = new OkHttpClient();
            // Change username
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody requestBody = RequestBody.create(mediaType, "newValue=" + newUsername + "&paramether=username");
            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .addHeader("content-type", "application/json")
                    .addHeader("Authorization", token)
                    .build();

            // Send HTTP POST
            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    return true;
                } else {
                    return false;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                Toast.makeText(getApplicationContext(), "Change username " + newUsername, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Error: Please enter a valid username", Toast.LENGTH_SHORT).show();
            }
        }
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
