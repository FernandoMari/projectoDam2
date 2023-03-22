package org.proven.decisions2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Register extends AppCompatActivity {

    EditText inputEmail,inputPassword,inputConfirmPasword;
    Button btnRegister;
    String emailPattern="[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        /*Initialize the variables*/
        inputEmail=findViewById(R.id.etMail);
        inputPassword=findViewById(R.id.etPassword);
        inputConfirmPasword=findViewById(R.id.etConfirmPassword);
        btnRegister=findViewById(R.id.btAccept);
        progressDialog=new ProgressDialog(this);
        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PerforAuth();
            }
        });

    }

    private void PerforAuth() {
        String email = inputEmail.getText().toString();
        String password=inputPassword.getText().toString();
        String confirmPassword = inputConfirmPasword.getText().toString();
        
        if(!email.matches(emailPattern)){
            inputEmail.setError("Enter conntext Email");
            
        } else if (password.isEmpty()||password.length()<6) {
            inputPassword.setError("Enter Proper Password");
            
        } else if (!password.equals(confirmPassword)) {
            inputConfirmPasword.setError("Password Not matched Both field");
        }else {
            progressDialog.setMessage("Please Wait While Register...");
            progressDialog.setTitle("Register");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        progressDialog.dismiss();
                        sendUserToNextActivity();
                        Toast.makeText(Register.this,"Registration Succesful",Toast.LENGTH_SHORT).show();

                    }else{
                        progressDialog.dismiss();
                        Toast.makeText(Register.this,""+task.getException(),Toast.LENGTH_SHORT).show();

                    }
                }
            });



        }

    }

    private void sendUserToNextActivity() {
        Intent intent=new Intent(Register.this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}