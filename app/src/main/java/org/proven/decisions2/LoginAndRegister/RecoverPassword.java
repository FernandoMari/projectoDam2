package org.proven.decisions2.LoginAndRegister;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import org.proven.decisions2.R;
import org.proven.decisions2.SecureConnection;
import org.proven.decisions2.Settings.EmailSettings.MailSender;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RecoverPassword extends Activity {

    Button btSendEmail, btLogin, btAccept;

    LinearLayout layoutIntroduceCode, layoutGeneral;

    EditText inputemail;
    String email;

    SecureConnection secureConnection = new SecureConnection();

    String url = "http://5.75.251.56:7070/recover-password";

    String url2="http://5.75.251.56:7070/reset-password";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recover_password_layout);

        initElements();

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RecoverPassword.this, MainActivity.class));
            }
        });

        btSendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutIntroduceCode.setVisibility(View.VISIBLE);
                initRecoverPass();
                ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) layoutGeneral.getLayoutParams();
                layoutParams.setMargins(0, 0, 0, 0);
                layoutGeneral.setLayoutParams(layoutParams);

                layoutGeneral.setGravity(Gravity.CENTER);
            }
        });

        btAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void initElements() {
        btSendEmail = findViewById(R.id.btSendEmail);
        btLogin = findViewById(R.id.btLogin);
        layoutIntroduceCode = findViewById(R.id.layoutIntroduceCode);
        layoutGeneral = findViewById(R.id.layoutGeneral);
        btAccept = findViewById(R.id.btAccept);
        inputemail = findViewById(R.id.etMail);
    }


    private void initRecoverPass() {
        // Obtener el texto introducido por el usuario
        email = inputemail.getText().toString();
        // Hacer algo con el texto introducido
        // ...

        new getPassword().execute();
    }


    private class getPassword extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            OkHttpClient client = secureConnection.getClient();
            //Confirm the username and password the user
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody requestBody = RequestBody.create(mediaType, "mail=" + email);

            Request request = new Request.Builder()
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

        //check that the login is correct and check if the credentials are correct or incorrect
        @Override
        protected void onPostExecute(String responseData) {
            String textWithoutQuotes = responseData.replace("\"", "");

            if (textWithoutQuotes == "" || textWithoutQuotes.isEmpty()) {

            } else {
                MailSender sender = new MailSender();
                sender.setmRecipient(email);
                sender.setmSubject("Contraseña olvidada");
                sender.setmMessage("<html><body style=\\\"text-align: center;\\\">\n" +
                        "        <h1>Contraseña</h1>\n" +
                        "        <p>Su token de recuperacion es: " + textWithoutQuotes + "</p>\n" +
                        "        </body></html>");

                sender.execute();
            }
        }
    }



}

