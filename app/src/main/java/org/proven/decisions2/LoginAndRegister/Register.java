package org.proven.decisions2.LoginAndRegister;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import org.proven.decisions2.R;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Register extends AppCompatActivity {

    EditText inputEmail, inputusername, inputPassword, inputConfirmPasword;
    Button btnRegister;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;

    String email, username, password;

    String url="http://143.47.249.102:7070/register";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        /*Initialize the elements*/
        initializeElements();


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PerforAuth(); //call the method for the register
                SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("cbremember", "false");
                editor.apply();

            }
        });

    }

    /*Initialize the elements*/
    private void initializeElements() {
        inputEmail = findViewById(R.id.etMail);
        inputusername = findViewById(R.id.etUsername);
        inputPassword = findViewById(R.id.etPassword);
        inputConfirmPasword = findViewById(R.id.etConfirmPassword);
        btnRegister = findViewById(R.id.btAccept);
        progressDialog = new ProgressDialog(this);
    }

    /*Method to register by asking the user for the email, username and password*/
    private void PerforAuth() {
        email = inputEmail.getText().toString();
        username = inputusername.getText().toString();
        password = inputPassword.getText().toString();
        String confirmPassword = inputConfirmPasword.getText().toString();
        //Check the email if it contains the elements of an email correctly
        if (!email.matches(emailPattern)) {
            inputEmail.setError("Enter conntext Email");
            //Check the username is empty
            if (username.isEmpty()) {
                inputusername.setError("Enter username please");
            }
            //Check that the password is empty or the length is correct
        } else if (password.isEmpty() || password.length() < 4) {
            inputPassword.setError("Enter Proper Password");
            //Check that the password is equals
        } else if (!password.equals(confirmPassword)) {
            inputConfirmPasword.setError("Password Not matched Both field");
        } else {
            progressDialog.setMessage("Please Wait While Register...");
            progressDialog.setTitle("Register");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            //call the method
            http();

        }

    }

    /*Method to instantiate the asyncTask of the HttpTask and execute it*/
    private void http() {
        new HttpTask().execute();
    }

    /*Method to execute the post requests for the register*/
    private class HttpTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody requestBody = RequestBody.create(mediaType, "mail=" + email + "&username=" + username + "&password=" + password);

            Request request = new Request.Builder()
                    //.url("http://5.75.251.56:7070/register")
                    .url(url)
                    .post(requestBody)
                    .addHeader("content-type", "application/json")
                    .addHeader("cache-control", "no-cache")
                    .build();

            try {
                Response response = client.newCall(request).execute();
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String responseData) {
            if (responseData != null) {
                Log.d("TAG", "Response data: " + responseData);
                //Parse the response data to check if register was successful
                boolean registerSuccessful = true;
                if (responseData.equalsIgnoreCase("user exists"))
                    System.out.println("Respuesta" + responseData);
                if (registerSuccessful) {
                    progressDialog.dismiss();
                    // redirects the user to the next activity
                    sendUserToNextActivity();
                    Toast.makeText(Register.this, "Register Succesful", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    /*Method to go to the next activity*/
    private void sendUserToNextActivity() {
        Intent intent = new Intent(Register.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_left, R.anim.slide_out_right);
    }

}