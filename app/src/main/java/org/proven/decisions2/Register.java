package org.proven.decisions2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;



import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class Register extends AppCompatActivity {

    EditText inputEmail, inputusername, inputPassword, inputConfirmPasword;
    Button btnRegister;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;

    String email, username, password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        /*Initialize the elements*/
        initializeElements();


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PerforAuth();
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

    private void PerforAuth() {
        email = inputEmail.getText().toString();
        username = inputusername.getText().toString();
        password = inputPassword.getText().toString();
        String confirmPassword = inputConfirmPasword.getText().toString();

        if (!email.matches(emailPattern)) {
            inputEmail.setError("Enter conntext Email");
            if (username.isEmpty()) {
                inputusername.setError("Enter username");
            }
        } else if (password.isEmpty() || password.length() < 4) {
            inputPassword.setError("Enter Proper Password");

        } else if (!password.equals(confirmPassword)) {
            inputConfirmPasword.setError("Password Not matched Both field");
        } else {
            progressDialog.setMessage("Please Wait While Register...");
            progressDialog.setTitle("Register");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            http();

        }

    }

    private void http() {
        new HttpTask().execute();
    }


    private class HttpTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody requestBody = RequestBody.create(mediaType, "mail=" + email + "&username=" + username + "&password=" + password);

            Request request = new Request.Builder()
                    .url("http://5.75.251.56:7070/register")
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
                    // redirige al usuario a la actividad siguiente
                    sendUserToNextActivity();
                    Toast.makeText(Register.this, "Register Succesful", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Muestra un mensaje de error si no se pudo obtener la respuesta
            }
        }
    }


    private void sendUserToNextActivity() {
        Intent intent = new Intent(Register.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}