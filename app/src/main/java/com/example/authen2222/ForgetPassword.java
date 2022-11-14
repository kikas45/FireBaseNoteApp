package com.example.authen2222;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPassword extends AppCompatActivity {

    private EditText m_forget_password;
    private Button m_passowrd_recover_button;
    private TextView m_go_back_login;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        getSupportActionBar().hide();

        firebaseAuth = FirebaseAuth.getInstance();

        m_forget_password = findViewById(R.id.m_forget_password);
        m_passowrd_recover_button = findViewById(R.id.m_passowrd_recover_button);
        m_go_back_login = findViewById(R.id.m_go_back_login);


        m_go_back_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        m_passowrd_recover_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = m_forget_password.getText().toString().trim();
                
                if (email.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please Enter Your Email", Toast.LENGTH_SHORT).show();
                }else {

                    /// else we the set the recovery password email

                    firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(getApplicationContext(), "You can Recover Your password Via Sent Email", Toast.LENGTH_SHORT).show();
                                finish();
                                Intent intent  = new Intent(getApplicationContext(), MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }else {
                                Toast.makeText(getApplicationContext(), "Email is Wrong or Account does not Exist", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }

            }
        });

    }
}