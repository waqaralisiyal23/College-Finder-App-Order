package com.codewithwaqar.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import adapters.InstituteAdapter;
import models.Institute;

public class AdminSearchActivity extends AppCompatActivity {

    private EditText searchEditText;
    private RecyclerView recyclerView;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference institutesRef = db.collection("institutes");

    private InstituteAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_search);

        searchEditText = (EditText) findViewById(R.id.admin_search_edit_text);
        recyclerView = (RecyclerView) findViewById(R.id.admin_search_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(AdminSearchActivity.this));

        setupRecyclerView("");

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()!=0){
                    Log.d("MyMessage: ", "MyMessage");
                    setupRecyclerView(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setupRecyclerView(String searchTerm){
        Query query = institutesRef.whereGreaterThanOrEqualTo("name", searchTerm);

        FirestoreRecyclerOptions<Institute> options = new FirestoreRecyclerOptions.Builder<Institute>()
                .setQuery(query, Institute.class)
                .build();

        adapter = new InstituteAdapter(AdminSearchActivity.this, options, true);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}