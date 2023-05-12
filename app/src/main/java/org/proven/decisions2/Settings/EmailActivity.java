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

public class EmailActivity extends Activity {
    //inputActualEmail is to insert the email the user, inputNewEmail is to insert the new email of the user, inputActualPassword is to insert the password of the user
    EditText inputActualEmail, inputNewEmail, inputActualPassword;
    //Button for the navigate in the change email or in the app
    Button btFriends, btHome, btSettings, btAccept, btCancel;
    //Correct format for email
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    //actual email the user, new email the user, actual password the user,
    String token, actualEmail, newEmail, actualPassword;
    //Url for the http post request for the change email in the app
    String url = "http://143.47.249.102:7070/swichEmail";

    //Capar que el correo no sea el mismo que introduce nuevamente
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.email_layout);
        //Initialize the elements
        initializeElements();
        //call the method
        readUser();

        btHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EmailActivity.this, SocialInterface.class));
            }
        });

        btFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EmailActivity.this, FriendsActivity.class));
            }
        });

        btSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EmailActivity.this, SettingsActivity.class));
            }
        });

        btAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //call the method for the change email
                changeEmail();
            }
        });

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EmailActivity.this, SettingsActivity.class));
                finish();
            }
        });
    }

    /*Initialize the elements*/
    private void initializeElements() {
        btHome = findViewById(R.id.btHome);
        btFriends = findViewById(R.id.btFriends);
        btSettings = findViewById(R.id.btSettings);
        btAccept = findViewById(R.id.btConfirm);
        btCancel = findViewById(R.id.btCancel);
        inputActualEmail = findViewById(R.id.etActualEmail);
        inputNewEmail = findViewById(R.id.etNewEmail);
        inputActualPassword = findViewById(R.id.etActualPassword);

    }


    /*Method to change email by asking the user for the actual email, new email and actual password*/
    private void changeEmail() {
        actualEmail = inputActualEmail.getText().toString();
        newEmail = inputNewEmail.getText().toString();
        actualPassword = inputActualPassword.getText().toString();
        //Check the email if it contains the elements of an email correctly
        if (!actualEmail.matches(emailPattern)) {
            inputActualEmail.setError("Enter correct format Email");
            //Check the actual email is empty
        } else if (actualEmail.isEmpty()) {
            inputActualEmail.setError("Enter your email");
            //Check the email if it contains the elements of an email correctly
        } else if (!newEmail.matches(emailPattern)) {
            inputNewEmail.setError("Enter correct format Email");
            //Check the password is empty
        } else if (actualPassword.isEmpty()) {
            inputActualPassword.setError("Enter your password actual");
        } else {
            //call the method for execute de asyncTask
            changesEmail(token);
            //go back to activity settings
            startActivity(new Intent(EmailActivity.this, SettingsActivity.class));
        }
    }

    /* Method to instantiate the EmailChangeTask and start it */
    private void changesEmail(String token) {
        new EmailChangeTask().execute(token);
    }


    /*Method to execute post change email for user*/
    private class EmailChangeTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            token = params[0];
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody requestBody = RequestBody.create(mediaType, "newMail=" + newEmail);

            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .addHeader("content-type", "application/json")
                    .addHeader("Authorization", token)
                    .build();

            // Send HTTP POST friend request
            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    return "Change email";
                } else {
                    return "Error change email";
                }
            } catch (IOException e) {
                e.printStackTrace();
                return "Error change email";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
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
