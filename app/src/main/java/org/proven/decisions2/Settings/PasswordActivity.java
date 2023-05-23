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

public class PasswordActivity extends Activity {
    EditText inputActualPassword, inputNewPassword, inputConfirmNewPassword;
    Button btFriends, btHome, btSettings, btAccept, btCancel;
    //    String url = "http://143.47.249.102:7070/switchPassword";
    String url = "http://5.75.251.56:7070/switchPassword";
    String token, actualPassword, newPassword, confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.password_layout);
        /*Initialize the elements*/
        initializeElements();
        //call the method
        readUser();


        btHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PasswordActivity.this, SocialInterface.class));
            }
        });

        btFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PasswordActivity.this, FriendsActivity.class));
            }
        });

        btSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PasswordActivity.this, SettingsActivity.class));
            }
        });

        btAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //call the method for the change password
                changePassword();
            }
        });
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PasswordActivity.this, SettingsActivity.class));
                finish();
            }
        });
    }

    /*Initialize the elements*/
    private void initializeElements() {
        btHome = findViewById(R.id.btHome);
        btFriends = findViewById(R.id.btFriends);
        btSettings = findViewById(R.id.btSettings);
        inputActualPassword = findViewById(R.id.etActualPassword);
        inputNewPassword = findViewById(R.id.etNewPassword);
        inputConfirmNewPassword = findViewById(R.id.etNewConfPassword);
        btAccept = findViewById(R.id.btConfirm);
        btCancel = findViewById(R.id.btCancel);

    }

    /* Method to instantiate the  PasswordChangeTask and start it */
    private void changesPassword(String token) {
        new PasswordChangeTask().execute(token);
    }

    /*Method to change password by asking the user for the actual password, new password and confirm password*/
    private void changePassword() {
        actualPassword = inputActualPassword.getText().toString();
        newPassword = inputNewPassword.getText().toString();
        confirmPassword = inputConfirmNewPassword.getText().toString();
        //Check that the actual password is equal
        if (!actualPassword.matches(actualPassword)) {
            inputActualPassword.setError(getString(R.string.enter_actual_password));
            //Check the actual password is empty
        } else if (actualPassword.isEmpty()) {
            inputActualPassword.setError(getString(R.string.password_empty));
            //Check that the new password is empty or the length is correct
        } else if (newPassword.isEmpty() || newPassword.length() < 4) {
            inputNewPassword.setError(getString(R.string.password_correct));
            //Check that the password is equals
        } else if (!newPassword.equals(confirmPassword)) {
            inputConfirmNewPassword.setError(getString(R.string.equal_password));
        } else {
            //call the method for execute de asyncTask
            changesPassword(token);

        }

    }

    /*Method to execute post change password for user*/
    private class PasswordChangeTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            token = params[0];

            System.out.println("Nueva contraseña: " + newPassword);
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody requestBody = RequestBody.create(mediaType, "newValue=" + newPassword + "&paramether=password" + "&currentPassword=" + actualPassword);

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
                    return response.body().string();
                } else {
                    return null;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                if (result.equals("Password change successful")) {
                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                    // go to next activity SettingsActivity
                    startActivity(new Intent(PasswordActivity.this, SettingsActivity.class));
                } else {
                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                    inputActualPassword.setError(getString(R.string.enter_actual_password));
                }
            } else {
                Toast.makeText(getApplicationContext(), "Error occurred", Toast.LENGTH_SHORT).show();
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
