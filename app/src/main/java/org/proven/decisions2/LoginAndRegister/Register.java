package org.proven.decisions2.LoginAndRegister;

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

import androidx.appcompat.app.AppCompatActivity;

import org.proven.decisions2.R;
import org.proven.decisions2.Settings.EmailSettings.MailSender;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Register extends AppCompatActivity {
    //inputEmail is to insert the email the user, inputUsername is to insert the name of the user, inputPassword is to insert the password of the user
    EditText inputEmail, inputusername, inputPassword, inputConfirmPasword;
    //Button for the confirm the register
    Button btnRegister;
    //Correct format for email
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    //progressDialog is for the dialog in the register
    ProgressDialog progressDialog;
    //email the user, username the user, password the user
    String email, username, password;
    //Url for the http post request for the register in the app
    String url = "http://143.47.249.102:7070/register";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        /*Initialize the elements*/
        initializeElements();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //call the method for the register
                PerforAuth();
                //Change the checkbox in false
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
        if (!email.matches(emailPattern) || email.isEmpty()) {
            inputEmail.setError(getString(R.string.format_email));
            //Check the username is empty
        } else if (username.isEmpty()) {
            inputusername.setError(getString(R.string.username_empty));
            //Check that the password is empty or the length is correct
        } else if (password.isEmpty() || password.length() < 4) {
            inputPassword.setError(getString(R.string.password_correct));
            //Check that the password is equals
        } else if (!password.equals(confirmPassword)) {
            inputConfirmPasword.setError(getString(R.string.equal_password));
        } else {
            //Dialog for the correct register
            progressDialog.setMessage("Please Wait While Register...");
            progressDialog.setTitle("Register");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            //call the method for execute de asyncTask
            http();

        }

    }

    /*Method to instantiate the asyncTask of the HttpTask and execute it*/
    private void http() {
        new HttpTask().execute();
        //Call the MailSender class to be able to send an email
        MailSender sender = new MailSender(email, "Email or password change confirmation", "Your email or password has been successfully changed."


        );
        sender.execute();
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
                    .url(url).post(requestBody).addHeader("content-type", "application/json").addHeader("cache-control", "no-cache").build();

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
                if (responseData.equalsIgnoreCase("user exists")) {
                    System.out.println("Respuesta" + responseData);
                    inputusername.setError(getString(R.string.username_exists));
                    progressDialog.dismiss();
                    registerSuccessful = false;
                }
                if (registerSuccessful) {
                    progressDialog.dismiss();
                    // redirects the user to the next activity
                    sendUserToNextActivity();
                    Toast.makeText(Register.this, getString(R.string.register_succesful), Toast.LENGTH_SHORT).show();
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