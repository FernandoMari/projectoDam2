package org.proven.decisions2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    EditText inputUsername, inputPassword;
    Button btLogin, btRegister;
    ProgressDialog progressDialog;

    String username, password;
    String filename = "username.txt";
    FileOutputStream outputStream;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);
        /*Initialize the elements*/
        initializeElements();

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                perforLogin();
            }
        });

        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Register.class));
            }
        });
    }

    private void initializeElements() {
        btLogin = findViewById(R.id.btLogin);
        btRegister = findViewById(R.id.btRegister);
        inputUsername = findViewById(R.id.etUsername2);
        inputPassword = findViewById(R.id.etPassword);
        progressDialog = new ProgressDialog(this);
    }

    private void perforLogin() {
        username = inputUsername.getText().toString();
        password = inputPassword.getText().toString();

        if (!username.matches(username)) {
            inputUsername.setError("Enter conntext Username");
        } else if (username.isEmpty()) {
            inputUsername.setError("Enter conntext Username");
        } else if (password.isEmpty() || password.length() < 4) {
            inputPassword.setError("Enter Proper Password");
        } else {
            saveUser();
            http();
        }
    }

    private void sendUserToNextActivity() {
        Intent intent = new Intent(MainActivity.this, SocialInterface.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle bundle = new Bundle();
        bundle.putString("id", username);
        Log.d("TAG", "userIdMainActivity: " + username);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void http() {
        new HttpTask().execute();
    }


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
                    .url("http://5.75.251.56:7070/login")
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
            boolean loginSuccessful = false;
            String textoSinComillas = responseData.replace("\"", "");
            if (textoSinComillas != null) {
                if (textoSinComillas.equals("Credenciales o usuario incorrecto!!!")) {
                    loginSuccessful = false;
                } else {
                    loginSuccessful = true;
                }
                Log.d("TAG", "Response data: " + textoSinComillas);
                //Parse the response data to check if login was successful
                if (loginSuccessful == true) {
                    progressDialog.dismiss();
                    // redirige al usuario a la actividad siguiente
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


    private void saveUser() {
        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(username.getBytes());
            outputStream.close();
        } catch (
                Exception e) {
            e.printStackTrace();
        }


    }


}