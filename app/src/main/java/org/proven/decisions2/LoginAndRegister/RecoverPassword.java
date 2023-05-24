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
import android.widget.Toast;

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

    EditText inputemail, inputRecovery, inputNewPassword, inputConfirmpassword;
    String email, newPassword, recoveryToken, confirmPassword;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    SecureConnection secureConnection = new SecureConnection();

    String url = "http://5.75.251.56:7070/recover-password";
    //String url="http://5.75.251.56:8443/recover-password";
    //String url="http://5.75.251.56:7070/recover-password";

    String url2 = "http://5.75.251.56:7070/reset-password";
    //String url2="http://5.75.251.56:8443/reset-password";
    //String url2="http://5.75.251.56:7070/reset-password";

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
                recoveryPasswordWithToken();

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
        inputRecovery = findViewById(R.id.etTemporalCode);
        inputNewPassword = findViewById(R.id.etNewPassword);
        inputConfirmpassword = findViewById(R.id.etConfirmPassword);
    }


    private void initRecoverPass() {
        email = inputemail.getText().toString();
        // Realizar la comprobación del correo electrónico
        if (email.isEmpty() || !email.matches(emailPattern)) {
            // El campo de correo electrónico está vacío o no tiene el formato correcto
            inputemail.setError(getString(R.string.format_email));
            return; // Salir de la función sin continuar con las comprobaciones
        }
        // Llamar al método getPassword() solo después de la comprobación exitosa del correo electrónico
        new getPassword().execute();

    }

    private void recoveryPasswordWithToken() {
        recoveryToken = inputRecovery.getText().toString();
        newPassword = inputNewPassword.getText().toString();
        confirmPassword = inputConfirmpassword.getText().toString();
        // Comprobar si el token de recuperación es válido
        if (recoveryToken.isEmpty()) {
            // El token de recuperación no es válido
            inputRecovery.setError("Invalid recovery token");
            return; // Salir de la función sin continuar con las comprobaciones
        }

        // Comprobar el resto de los campos y realizar las comprobaciones adicionales aquí
        if (newPassword.isEmpty() || newPassword.length() < 4) {
            // El campo de nueva contraseña está vacío o no cumple los requisitos
            inputNewPassword.setError(getString(R.string.password_correct));
            return; // Salir de la función sin continuar con las comprobaciones
        }

        if (!newPassword.equals(confirmPassword)) {
            // La nueva contraseña y la confirmación de contraseña no coinciden
            inputConfirmpassword.setError(getString(R.string.equal_password));
            return; // Salir de la función sin continuar con las comprobaciones
        }
        // Todas las comprobaciones son exitosas, llamar al método recoveryPassword()
        new recoveryPassword().execute();
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
            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            RequestBody requestBody = RequestBody.create(mediaType, "mail=" + email);

            Request request = new Request.Builder().url(url).post(requestBody).addHeader("content-type", "application/x-www-form-urlencoded").addHeader("cache-control", "no-cache").build();

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
            String textWithoutQuotes = responseData.replace("\"", "");

            if (responseData.equals("Invalid email")){
                inputemail.setError("email invalid");
            }
            else if (textWithoutQuotes == "" || textWithoutQuotes.isEmpty()) {
                // No se recibió ningún código de recuperación válido
                // Aquí puedes manejar la lógica para mostrar un mensaje de error o realizar otras acciones si es necesario
            } else {
                // El correo electrónico es válido y se recibió un código de recuperación
                MailSender sender = new MailSender();
                sender.setmRecipient(email);
                sender.setmSubject("Contraseña olvidada");
                sender.setmMessage("<html><body style=\\\"text-align: center;\\\">\n" + "        " +
                        "<h1>Restablecer contraseña</h1>\n" + "        " +
                        "<p>Su código para restablecer su contraseña es: " + textWithoutQuotes + "</p>\n" + "      " +
                        "  </body></html>");

                sender.execute();
            }
        }
    }

    private class recoveryPassword extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            OkHttpClient client = new OkHttpClient();

            // Construir los parámetros de la solicitud
            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            String requestBody = "recoveryToken=" + recoveryToken + "&newPassword=" + newPassword;

            Request request = new Request.Builder()
                    .url(url2)
                    .post(RequestBody.create(mediaType, requestBody))
                    .addHeader("content-type", "application/x-www-form-urlencoded")
                    .build();

            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    // La solicitud fue exitosa, puedes obtener el cuerpo de la respuesta
                    return response.body().string();
                } else {
                    // La solicitud no fue exitosa, manejar el error apropiadamente
                    return null;
                }
            } catch (IOException e) {
                e.printStackTrace();
                // Manejar la excepción de conexión o lectura/escritura de datos
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result != null) {
                // La solicitud fue exitosa, puedes hacer algo con la respuesta
                Toast.makeText(RecoverPassword.this, "Password updated successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(RecoverPassword.this, MainActivity.class));
                System.out.println("Respuesta del servidor: " + result);
            } else {
                // La solicitud no fue exitosa, manejar el error apropiadamente
                Toast.makeText(RecoverPassword.this, "Invalid recovery token", Toast.LENGTH_SHORT).show();
            }
        }
    }


}

