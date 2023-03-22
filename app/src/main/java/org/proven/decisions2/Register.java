package org.proven.decisions2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Register extends AppCompatActivity {

    EditText inputEmail, inputPassword, inputConfirmPasword;
    Button btnRegister;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        /*Initialize the variables*/
        inputEmail = findViewById(R.id.etMail);
        inputPassword = findViewById(R.id.etPassword);
        inputConfirmPasword = findViewById(R.id.etConfirmPassword);
        btnRegister = findViewById(R.id.btAccept);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        /*The "btnRegister" button uses a click event listener that is defined by an anonymous class, which implements the View.OnClickListener interface.
         *The anonymous class has an onClick() method that is called when the button is clicked.
         *Inside the onClick() method, the PerforAuth() method is called, which has the register logic.*/
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PerforAuth();
            }
        });

    }

    /*Method with the logic of sing up into the application*/
    private void PerforAuth() {
        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();
        String confirmPassword = inputConfirmPasword.getText().toString();
        /*Check If the email does not match the pattern, an error message is displayed in the email input field.*/
        if (!email.matches(emailPattern)) {
            inputEmail.setError("Enter conntext Email");
        } else if (email.equals(email)) {
            inputEmail.setError("Email exist");

            /*Check if the password is valid. If the password is less than 6 characters or empty, an error message is displayed in the password input field.*/
        } else if (password.isEmpty() || password.length() < 6) {
            inputPassword.setError("Enter Proper Password");
            /*Check if the password is equal to the entered password*/
        } else if (!password.equals(confirmPassword)) {
            inputConfirmPasword.setError("Password Not matched Both field");
        } else {
            progressDialog.setMessage("Please Wait While Register...");
            progressDialog.setTitle("Register");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                /*The createUserWithEmailAndPassword() method returns an asynchronous task that completes with an AuthResult authentication result. The addOnCompleteListener() method is used to add an event listener to this task that is called when the task completes.*/
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        sendUserToNextActivity();
                        Toast.makeText(Register.this, "Registration Succesful", Toast.LENGTH_SHORT).show();

                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(Register.this, "" + task.getException(), Toast.LENGTH_SHORT).show();

                    }
                }
            });


        }

    }

    /*Method to return to the login interface*/
    private void sendUserToNextActivity() {
        Intent intent = new Intent(Register.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}