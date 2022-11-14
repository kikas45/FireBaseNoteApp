package com.example.authen2222;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private EditText edt_emails_main;
    private EditText edit_password_main;
    private TextView btn_login_main;
    private TextView text_forget_password_main;
    private TextView go_to_sign_up_main;

    private FirebaseAuth firebaseAuth;

    private ProgressBar progressBar_main;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        edt_emails_main = findViewById(R.id.edt_emails_main);
        edit_password_main = findViewById(R.id.edit_password_main);
        btn_login_main = findViewById(R.id.btn_login_main);
        text_forget_password_main = findViewById(R.id.text_forget_password_main);
        go_to_sign_up_main = findViewById(R.id.go_to_sign_up_main);
        progressBar_main = findViewById(R.id.progressBar_main);


        firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();


        /// if the user is already login in
        if (firebaseUser!=null){

            finish();
            startActivity(new Intent(getApplicationContext(), NoteActivity.class));

        }

        go_to_sign_up_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getApplicationContext(), SignUp.class));

            }
        });

        text_forget_password_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ForgetPassword.class));
            }
        });

        btn_login_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user_mail = edt_emails_main.getText().toString().trim();
                String user_password = edit_password_main.getText().toString().trim();

    /*            if (user_mail.isEmpty() || user_password.isEmpty()){
                    Toast.makeText(getApplicationContext(), "All field are required", Toast.LENGTH_SHORT).show();
                    edt_emails_main.setError("Email is Invalid");
                    edt_emails_main.setError("Password is Invalid");

                }*/


                boolean isValiDated = valiDatemyIds(user_mail, user_password);
                // if is Validated is False
                if (!isValiDated) {
                    return;
                }
                else {
                    /// login the user

                    progressBar_main.setVisibility(View.VISIBLE);

                    firebaseAuth.signInWithEmailAndPassword(user_mail, user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){

                                // since forebase will always cratee an acccount, we neeed to check a verifed email before it can login
                                // this will prevent a single email used  for mutiple accoount

                                checckMailVerification();

                            }else {
                             //   Toast.makeText(getApplicationContext(), "Account Does not Exist", Toast.LENGTH_SHORT).show();
                                Toast.makeText(getApplicationContext(), ""+ Objects.requireNonNull(task.getException()).getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                progressBar_main.setVisibility(View.INVISIBLE);
                            }
                        }
                    });


                }
                
            }
        });



    }

    private void checckMailVerification() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if (Objects.requireNonNull(firebaseUser).isEmailVerified()){
            Toast.makeText(getApplicationContext(), "Logged In", Toast.LENGTH_SHORT).show();
            finish();
            Intent intent  = new Intent(getApplicationContext(), NoteActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

        }else {

            Toast.makeText(getApplicationContext(), "Verify your email first", Toast.LENGTH_SHORT).show();
            progressBar_main.setVisibility(View.INVISIBLE);
            firebaseAuth.signOut();

        }

    }

    private boolean valiDatemyIds(String email, String pass) {
        //// lwt create a method to Validate the Data, this returns True or False

        // we check if the email is valid or not
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edt_emails_main.setError("Email is Invalid");
            return false;  //  i.e return nothing after the error
        }

        if (pass.length() < 6) {
            edit_password_main.setError("Password is Invalid");
            return false;  //  i.e return nothing after the error
        }


        //// the (3) three things above is true, then Return TRUE

        return true;
    }

}