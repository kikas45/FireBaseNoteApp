package com.example.authen2222;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Edit_Note_Activity extends AppCompatActivity {

    Intent data;

    EditText editText_title, editText_content;
    FloatingActionButton floating_edit_text;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();



        editText_title = findViewById(R.id.editText_title);
        editText_content = findViewById(R.id.editText_content);
        floating_edit_text = findViewById(R.id.floating_edit_text);

        data = getIntent();

        String note_title = data.getStringExtra("title");
        String note_content = data.getStringExtra("content");

        editText_title.setText(note_title);
        editText_content.setText(note_content);



        floating_edit_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  Toast.makeText(getApplicationContext(), "Saved ", Toast.LENGTH_SHORT).show();

                String newTitle = editText_title.getText().toString();
                String newContent = editText_content.getText().toString();

                if (newTitle.isEmpty() || newContent.isEmpty()){

                    Toast.makeText(getApplicationContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
                    
                }else {

                    // we only need the firebase document Id to edit the document
                    DocumentReference    documentReference = firebaseFirestore.collection("notes").document(Objects.requireNonNull(firebaseUser.getEmail())).collection("myNotes").document(data.getStringExtra("noteId"));

                    Map <String, Object> new_notes = new HashMap<>();
                    new_notes.put("title", newTitle);
                    new_notes.put("content", newContent);
                    documentReference.set(new_notes).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                            Toast.makeText(getApplicationContext(), "Note is Updated", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(getApplicationContext(), DeatilsNoteActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                            view.getContext().startActivity(intent);


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(getApplicationContext(), "Failed to be Updated", Toast.LENGTH_SHORT).show();


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