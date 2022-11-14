package com.example.authen2222;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class NoteActivity extends AppCompatActivity {

    private FloatingActionButton floatingActionButton, floatingActionButton_test;
    private FirebaseAuth firebaseAuth;

    //f  for fecthing the data

    private RecyclerView recyclerView;
    // StaggeredGridLayoutManager staggeredGridLayoutManager;

    LinearLayoutManager linearLayoutManager;


    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;
    FirestoreRecyclerAdapter<firebaseModel, NoteViewHolder> noteAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        firebaseAuth = FirebaseAuth.getInstance();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();


        floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton_test = findViewById(R.id.floatingActionButton_test);

        getSupportActionBar().setTitle("All Notes");

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), CreateNoteActivity.class));
            }
        });


        floatingActionButton_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getEmail();

                Toast.makeText(getApplicationContext()  , "" + name, Toast.LENGTH_SHORT).show();
            }
        });

        /// setting Up the Firebase

        ///  Query query = firebaseFirestore.collection("notes").document(firebaseUser.getEmail()).collection("myNotes").orderBy("title", Query.Direction.ASCENDING );

        Query query = firebaseFirestore.collection("notes").document(Objects.requireNonNull(firebaseUser.getEmail())).collection("myNotes").orderBy("title", Query.Direction.ASCENDING);


        ///    Query query = firebaseFirestore.collection(Objects.requireNonNull(firebaseUser.getEmail())).orderBy("title", Query.Direction.ASCENDING );


        FirestoreRecyclerOptions<firebaseModel> allUsers = new FirestoreRecyclerOptions.Builder<firebaseModel>().setQuery(query, firebaseModel.class).build();

        noteAdapter = new FirestoreRecyclerAdapter<firebaseModel, NoteViewHolder>(allUsers) {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            protected void onBindViewHolder(@NonNull NoteViewHolder holder, int position, @NonNull firebaseModel model) {


                ImageView ImageView_Popupmenu = holder.itemView.findViewById(R.id.imageView_more);

                int colourCode = getRandomColor();

                holder.mNotes.setBackgroundColor(holder.itemView.getResources().getColor(colourCode, null));

                holder.note_title.setText(model.getTitle());
                holder.note_contents.setText(model.getContent());


                String docId = noteAdapter.getSnapshots().getSnapshot(position).getId();


                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        /// we move to detail aactivity 

                        Intent intent = new Intent(view.getContext(), DeatilsNoteActivity.class);

                        intent.putExtra("title", model.getTitle());
                        intent.putExtra("content", model.getContent());
                        intent.putExtra("noteId", docId);
                        view.getContext().startActivity(intent);

                        Toast.makeText(getApplicationContext(), "this is Clicked", Toast.LENGTH_SHORT).show();
                    }
                });

                ImageView_Popupmenu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);

                        popupMenu.setGravity(Gravity.END);

                        popupMenu.getMenu().add("Edit").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {

                                Intent intent = new Intent(view.getContext(), DeatilsNoteActivity.class);

                                intent.putExtra("title", model.getTitle());
                                intent.putExtra("content", model.getContent());
                                intent.putExtra("noteId", docId);

                                view.getContext().startActivity(intent);
                                return false;
                            }
                        });

                        popupMenu.getMenu().add("Delete").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {


                              DocumentReference documentReference = firebaseFirestore.collection("notes").document(Objects.requireNonNull(firebaseUser.getEmail())).collection("myNotes").document(docId);
                                documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                       if (task.isSuccessful()){
                                           Toast.makeText(view.getContext(), "Delete", Toast.LENGTH_SHORT).show();
                                       }else {
                                           Toast.makeText(view.getContext(), "failed", Toast.LENGTH_SHORT).show();
                                       }
                                    }
                                });



                                return false;
                            }
                        });


                        popupMenu.getMenu().add("Create").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {

                                Toast.makeText(view.getContext(), "Delete", Toast.LENGTH_SHORT).show();
                                return false;
                            }
                        });

                        popupMenu.show();

                        //  Toast.makeText(getApplicationContext(), "Yes working", Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @NonNull
            @Override
            public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note_layout, parent, false);
                return new NoteViewHolder(view);
            }
        };

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        //   staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(noteAdapter);


    }

    private int getRandomColor() {

        /// let create and array to store the color and list them

        List<Integer> colorCodes = new ArrayList<>();
        colorCodes.add(R.color.one);
        colorCodes.add(R.color.two);
        colorCodes.add(R.color.three);
        colorCodes.add(R.color.four);
        colorCodes.add(R.color.five);
        colorCodes.add(R.color.teal_200);
        colorCodes.add(R.color.teal_700);
        colorCodes.add(R.color.black);

        Random random = new Random();
        int number = random.nextInt(colorCodes.size());

        return colorCodes.get(number);
    }


    public class NoteViewHolder extends RecyclerView.ViewHolder {

        private TextView note_title;
        private TextView note_contents;
        private ConstraintLayout mNotes;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            note_title = itemView.findViewById(R.id.note_title);
            note_contents = itemView.findViewById(R.id.note_contents);
            mNotes = itemView.findViewById(R.id.mNotes);

        }
    }

    ;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.logout:

                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));

        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onStart() {
        super.onStart();
        noteAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (recyclerView == null) {
            noteAdapter.stopListening();
        }
    }
}