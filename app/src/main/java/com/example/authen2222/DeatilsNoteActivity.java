package com.example.authen2222;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DeatilsNoteActivity extends AppCompatActivity {

    private TextView note_title_edit;
    private TextView note_contents_edit;

    private FloatingActionButton floatingActionButton3_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        note_title_edit =  findViewById(R.id.note_title_deatils);
        note_contents_edit =  findViewById(R.id.note_contents_deatils);
        floatingActionButton3_edit =  findViewById(R.id.floatingActionButton3_edit);

        Intent data = getIntent();

        floatingActionButton3_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(view.getContext(), Edit_Note_Activity.class);
                intent.putExtra("title", data.getStringExtra("title"));
                intent.putExtra("content", data.getStringExtra("content"));
                intent.putExtra("noteId", data.getStringExtra("noteId"));

                Toast.makeText(getApplicationContext(), "Yes working", Toast.LENGTH_SHORT).show();

                startActivity(intent);

            }
        });


        note_contents_edit.setText(data.getStringExtra("content"));
        note_title_edit.setText(data.getStringExtra("title"));



    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}