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

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.proven.decisions2.AppCompat;
import org.proven.decisions2.R;
import org.proven.decisions2.SocialInterface;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompat {
    EditText inputUsername, inputPassword;
    Button btLogin, btRegister;
    ProgressDialog progressDialog;

    String username, password, token;
    CheckBox cbRemember;
    String filename = "token.txt";
    String url = "http://143.47.249.102:7070/login";
    String url2 = "http://143.47.249.102:7070/getUserToken";
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
                perforLogin();//call the method for the login
            }
        });
        cbRemember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()) {
                    SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("cbremember", "true");
                    editor.apply();

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
        if (checkbox.equals("true")) {
            startActivity(new Intent(MainActivity.this, SocialInterface.class));
            finish();
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
            saveUser();
            http();
        }
    }

    /*Method to go to the next activity*/
    private void sendUserToNextActivity() {
        Intent intent = new Intent(MainActivity.this, SocialInterface.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    /*Method to instantiate the asyncTask of the HttpTask and execute it*/
    private void http() {
        new HttpTask().execute();
        new getToken().execute();

    }

    /*Method to execute the post requests for the login*/
    private class HttpTask extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Please Wait While Login...");
            progressDialog.setTitle("Login");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody requestBody = RequestBody.create(mediaType, "username=" + username + "&password=" + password);

            Request request = new Request.Builder()
                    //.url("http://5.75.251.56:7070/login")
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
            String textoSinComillas = responseData.replace("\"", "");
            if (textoSinComillas != null) {
                if (textoSinComillas.equals("Credenciales o usuario incorrecto!!!")) {
                    loginSuccessful = false;
                } else {
                    token = textoSinComillas;
                    System.out.println(token);
                    saveUser();
                    loginSuccessful = true;
                }

                Log.d("TAG", "Response data: " + textoSinComillas);
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

    /*Method to execute the post requests for thMethod to execute post requests for login and get token */
    private class getToken extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Please Wait While Login...");
            progressDialog.setTitle("Login");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody requestBody = RequestBody.create(mediaType, "username=" + username + "&password=" + password);

            Request request = new Request.Builder()
                    //.url("http://5.75.251.56:7070/login")
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
            String textoSinComillas = responseData.replace("\"", "");
            if (textoSinComillas != null) {
                if (textoSinComillas.equals("Credenciales o usuario incorrecto!!!")) {
                    loginSuccessful = false;
                } else {
                    token = textoSinComillas;
                    System.out.println(token);
                    saveUser();
                    loginSuccessful = true;
                }
                Log.d("TAG", "Response data: " + textoSinComillas);
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