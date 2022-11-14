package com.example.authen2222;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUp extends AppCompatActivity {

    private EditText password_signup;
    private EditText email_signup;
    private TextView btn_sign_up;
    private TextView login_text_signup;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        getSupportActionBar().hide();;

        firebaseAuth = FirebaseAuth.getInstance();

        email_signup = findViewById(R.id.email_signup);
        password_signup = findViewById(R.id.edit_password_signup);
        btn_sign_up = findViewById(R.id.btn_sign_up);
        login_text_signup = findViewById(R.id.text_want_to_login_signup);

        login_text_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        btn_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = email_signup.getText().toString().trim();
                String password = password_signup.getText().toString().trim();
                
                if (email.isEmpty() || password.isEmpty()){
                    Toast.makeText(getApplicationContext(), "All Fileds are Required", Toast.LENGTH_SHORT).show();
                }else if (password.length()<7){
                    Toast.makeText(getApplicationContext(), "Password needs to greater than 7 Digit", Toast.LENGTH_SHORT).show();
                }else {
                    /// register the user to data base

                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            
                            if (task.isSuccessful()){
                                Toast.makeText(getApplicationContext(), "Registered Sucessfuly", Toast.LENGTH_SHORT).show();
                                sendEmailVerification();
                            }else {
                                Toast.makeText(getApplicationContext(), "Unable to Register", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
            }
        });




    }

    ///send the email  for verification before user can lo gin
    private void sendEmailVerification() {

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser!=null){
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    Toast.makeText(getApplicationContext(), "verification Email is sent, Verify and Login Again", Toast.LENGTH_SHORT).show();
                    firebaseAuth.signOut();
                    finish();
                    startActivity(new Intent(getApplicationContext(),MainActivity.class ));

                }
            });
        }else {
            Toast.makeText(getApplicationContext(), "failed to send Verification email", Toast.LENGTH_SHORT).show();
        }

    }
}