package com.example.authen2222;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CreateNoteActivity extends AppCompatActivity {

    private EditText createNotesTitles, createContents;
    private FloatingActionButton fb_save_notes;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore firebaseFirestore;
    private Toolbar tool_bar_create;
    private DocumentReference documentReference;

    private ProgressBar progressBar_createnote;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        fb_save_notes = findViewById(R.id.fb_save_notes);
        createNotesTitles = findViewById(R.id.creteTitleNotes);
        createContents = findViewById(R.id.createContentNotes);
        tool_bar_create = findViewById(R.id.tool_bar_create);
        progressBar_createnote = findViewById(R.id.progressBar_createnote);

        setSupportActionBar(tool_bar_create);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        fb_save_notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = createNotesTitles.getText().toString();
                String contents = createContents.getText().toString();

                if (title.isEmpty() || contents.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Both Field Requires filling", Toast.LENGTH_SHORT).show();
                } else {

                    progressBar_createnote.setVisibility(View.VISIBLE);
                    documentReference = firebaseFirestore.collection("notes").document(Objects.requireNonNull(firebaseUser.getEmail())).collection("myNotes").document();

                    //      documentReference = firebaseFirestore.collection(Objects.requireNonNull(firebaseUser.getEmail())).document();


                    Map<String, Object> note = new HashMap<>();
                    note.put("title", title);
                    note.put("content", contents);

                    documentReference.set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getApplicationContext(), "Note Created Sucessfully", Toast.LENGTH_SHORT).show();

                            finish();
                            Intent intent  = new Intent(getApplicationContext(), NoteActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            progressBar_createnote.setVisibility(View.INVISIBLE);


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Failed To Create", Toast.LENGTH_SHORT).show();
                            /// startActivity(new Intent(getApplicationContext(), NoteActivity.class));
                            progressBar_createnote.setVisibility(View.INVISIBLE);

                        }
                    });

                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}