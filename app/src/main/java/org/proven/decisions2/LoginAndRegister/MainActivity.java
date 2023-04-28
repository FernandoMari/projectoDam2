package org.proven.decisions2.LoginAndRegister;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import org.proven.decisions2.R;
import org.proven.decisions2.Settings.AppCompat;
import org.proven.decisions2.SocialInterface;

import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompat {
    //inputUsername is to insert the name of the user, inputPassword is to insert the password of the user
    EditText inputUsername, inputPassword;
    //btLogin is for the login in the app and next Activity SocialInterface , btRegister is for the next Activity Register for the register in the app.
    Button btLogin, btRegister;
    //progressDialog is for the dialog in the login or and register
    ProgressDialog progressDialog;
    //username the user , password the user , token the user for the login in the app
    String username, password, token;
    //The CheckBox is used to allow the user to remember their session
    CheckBox cbRemember;
    //Filename the document name for save the token
    String filename = "token.txt";
    //Url for the http post request for the login in the app
    String url = "http://143.47.249.102:7070/login";
    //Url for the http post request for the getUserToken
    String url2 = "http://143.47.249.102:7070/getUserToken";
    //Create FileOutputStream for the save the document internal
    FileOutputStream outputStream;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);
        /*Initialize the elements*/
        initializeElements();
        /* Initialize the checkbox*/
        checkboxInitialize();

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //call the method for the login
                perforLogin();
            }
        });
        cbRemember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                //Check the compoundButton is checked
                if (compoundButton.isChecked()) {
                    SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("cbremember", "true");
                    editor.apply();
                    //Check the compoundButton is not checked
                } else if (!compoundButton.isChecked()) {
                    SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("cbremember", "false");
                    editor.apply();
                }
            }
        });

        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Register.class));
                overridePendingTransition(R.anim.slide_right, R.anim.slide_out_left);
            }
        });
    }

    /*Method to initialize the checkbox to keep me logged in*/
    private void checkboxInitialize() {
        SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
        String checkbox = preferences.getString("cbremember", "");
        //Check the combobox is in true
        if (checkbox.equals("true")) {
            startActivity(new Intent(MainActivity.this, SocialInterface.class));
            finish();
            //Check the combobox is in false
        } else if (checkbox.equals("false")) {
            Toast.makeText(this, "Please sign in", Toast.LENGTH_SHORT).show();
        }
    }

    /*Method to initialize the elements*/
    private void initializeElements() {
        btLogin = findViewById(R.id.btLogin);
        btRegister = findViewById(R.id.btRegister);
        inputUsername = findViewById(R.id.etUsername2);
        inputPassword = findViewById(R.id.etPassword);
        cbRemember = findViewById(R.id.cbRemember);
        progressDialog = new ProgressDialog(this);
    }

    /*Method to login by asking the user for the username and password*/
    private void perforLogin() {
        username = inputUsername.getText().toString();
        password = inputPassword.getText().toString();
        //Check the username is not equals
        if (!username.matches(username)) {
            inputUsername.setError("Enter conntext Username");
            //Check the username is empty
        } else if (username.isEmpty()) {
            inputUsername.setError("Enter conntext Username");
            //Check that the password is empty or the length is correct
        } else if (password.isEmpty() || password.length() < 4) {
            inputPassword.setError("Enter Proper Password");
        } else {
            //call the method for execute de asyncTask
            http();
        }
    }

    /*Method to go to the next activity*/
    private void sendUserToNextActivity() {
        Intent intent = new Intent(MainActivity.this, SocialInterface.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    /*Method to instantiate the asyncTask of the HttpTask and getToken and execute it*/
    private void http() {
        // execute the HttpTask
        new HttpTask().execute();
        // execute the getToken
        new getToken().execute();

    }

    /*Method to execute the post requests for the login*/
    private class HttpTask extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Dialog for the correct login
            progressDialog.setMessage("Please Wait While Login...");
            progressDialog.setTitle("Login");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            OkHttpClient client = new OkHttpClient();
            //Confirm the username and password the user
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody requestBody = RequestBody.create(mediaType, "username=" + username + "&password=" + password);

            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .addHeader("content-type", "application/json")
                    .addHeader("cache-control", "no-cache")
                    .addHeader("Authorization", "Bearer " + token)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        //check that the login is correct and check if the credentials are correct or incorrect
        @Override
        protected void onPostExecute(String responseData) {
            boolean loginSuccessful = false;
            String textWithoutQuotes = responseData.replace("\"", "");
            if (textWithoutQuotes != null) {
                if (textWithoutQuotes.equals("Credenciales o usuario incorrecto!!!")) {
                    inputUsername.setError("User not exists");
                    inputPassword.setError("password incorrect");
                    loginSuccessful = false;
                } else {
                    token = textWithoutQuotes;
                    System.out.println(token);
                    loginSuccessful = true;
                }

                Log.d("TAG", "Response data: " + textWithoutQuotes);
                //Parse the response data to check if login was successful
                if (loginSuccessful == true) {
                    progressDialog.dismiss();
                    // redirects the user to the next activity
                    sendUserToNextActivity();
                    Toast.makeText(MainActivity.this, "Login Succesfull", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this, "Wrong credentials or user", Toast.LENGTH_SHORT).show();
                }
            } else {
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, "error connection", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /*Method to execute the post requests for login and get token */
    private class getToken extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody requestBody = RequestBody.create(mediaType, "username=" + username + "&password=" + password);

            Request request = new Request.Builder()
                    .url(url2)
                    .post(requestBody)
                    .addHeader("content-type", "application/json")
                    .addHeader("cache-control", "no-cache")
                    .addHeader("Authorization", "Bearer " + token)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        //check that the login is correct and check if the credentials are correct or incorrect
        @Override
        protected void onPostExecute(String responseData) {
            boolean loginSuccessful = false;
            String textWithoutQuotes = responseData.replace("\"", "");
            if (textWithoutQuotes != null) {
                if (textWithoutQuotes.equals("Credenciales o usuario incorrecto!!!")) {
                    loginSuccessful = false;
                } else {
                    token = textWithoutQuotes;
                    System.out.println(token);
                    saveUser();
                    loginSuccessful = true;
                }
                Log.d("TAG", "Response data: " + textWithoutQuotes);
            }
        }
    }


    /*Method to save the token that logs in to be able to use it in other activities*/
    private void saveUser() {
        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(token.getBytes());
            outputStream.close();
        } catch (
                Exception e) {
            e.printStackTrace();
        }

    }


}