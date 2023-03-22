package org.proven.decisions2;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    /*Instantiate the variables*/
    EditText inputEmail, inputPassword;
    Button btLogin, btRegister;
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);
        /*Initialize the variables*/
        btLogin = findViewById(R.id.btLogin);
        btRegister = findViewById(R.id.btRegister);
        inputEmail = findViewById(R.id.etUsername);
        inputPassword = findViewById(R.id.etPassword);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        /*The "btLogin" button uses a click event listener that is defined by an anonymous class, which implements the View.OnClickListener interface.
         *The anonymous class has an onClick() method that is called when the button is clicked.
         *Inside the onClick() method, the perforLogin() method is called, which has the login logic.*/
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                perforLogin();
            }
        });
        /*The "btRegister" button  also uses a click listener that is defined by an anonymous class, which also implements the View.OnClickListener interface.
         *The anonymous class has an onClick() method that is called when the button is clicked.
         *Inside the onClick() method, an object of the Intent class is created and the current activity (MainActivity) and the register activity (Register) are passed as parameters.
         *The startActivity() method is then called to start the registration activity.*/
        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Register.class));
            }
        });
    }

    /*Method with the logic of logging into the application.
     * The method gets the values entered for the email and password, and then validates the email to ensure it is in the proper format using an emailPattern regular expression.*/
    private void perforLogin() {
        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();
        /*Check If the email does not match the pattern, an error message is displayed in the email input field.*/
        if (!email.matches(emailPattern)) {
            inputEmail.setError("Enter conntext Email");
       /*Check if the password is valid. If the password is less than 6 characters or empty, an error message is displayed in the password input field.*/
        } else if (password.isEmpty() || password.length() < 6) {
            inputPassword.setError("Enter Proper Password");
        } else {
            progressDialog.setMessage("Please Wait While Login...");
            progressDialog.setTitle("Login");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            /*The email and password are valid, a progress message is displayed and the signInWithEmailAndPassword() method is called to perform the sign-in using the mAuth object (presumably an instance of the FirebaseAuth class).*/
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                /*The signInWithEmailAndPassword() method returns an asynchronous task that completes with an AuthResult authentication result. The addOnCompleteListener() method is used to add an event listener to this task that is called when the task completes.*/
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        sendUserToNextActivity();
                        Toast.makeText(MainActivity.this, "Login Succesful", Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this, "" + task.getException(), Toast.LENGTH_SHORT).show();

                    }
                }
            });


        }
    }

    /*Method to go to the social interface*/
    private void sendUserToNextActivity() {
        Intent intent = new Intent(MainActivity.this, SocialInterface.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}