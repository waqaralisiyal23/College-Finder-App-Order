package com.codewithwaqar.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import adapters.InstituteAdapter;
import adapters.MyInstituteAdapter;
import models.Institute;

public class AdminViewInstitutesActivity extends AppCompatActivity {

    private EditText searchEditText;
//    private Button searchButton;
    private RecyclerView recyclerView;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference institutesRef = db.collection("institutes");

//    private InstituteAdapter adapter;
    private MyInstituteAdapter adapter;
    private List<Institute> instituteList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_institutes);

//        searchButton = (Button) findViewById(R.id.admin_view_institutes_search_button);
        searchEditText = (EditText) findViewById(R.id.admin_view_institutes_search_et);
        recyclerView = (RecyclerView) findViewById(R.id.admin_view_institutes_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(AdminViewInstitutesActivity.this));

//        setupRecyclerView();
        _fetchData();

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

//        searchButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(AdminViewInstitutesActivity.this, AdminSearchActivity.class));
//            }
//        });
    }

    private void _fetchData(){
        institutesRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    instituteList.clear();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Institute institute = new Institute(
                                document.get("id").toString(),
                                document.get("imageUrl").toString(),
                                document.get("name").toString(),
                                document.get("address").toString(),
                                document.get("type").toString(),
                                (List<String>) document.get("departments"),
                                document.get("url").toString()
                        );
                        instituteList.add(institute);
                    }
                    adapter = new MyInstituteAdapter(AdminViewInstitutesActivity.this,
                            instituteList, true);
                    recyclerView.setAdapter(adapter);
                }
                else{
                    Toast.makeText(AdminViewInstitutesActivity.this, "Error: "+
                            task.getException().toString(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void filter(String searchedTerm){
        List<Institute> filteredList = new ArrayList<>();
        for(Institute institute : instituteList){
            if(institute.getName().toLowerCase().contains(searchedTerm.toLowerCase())){
                filteredList.add(institute);
            }
        }

        adapter.filterList(filteredList);
    }

//    private void setupRecyclerView(){
//        Query query = institutesRef.orderBy("name");
//
//        FirestoreRecyclerOptions<Institute> options = new FirestoreRecyclerOptions.Builder<Institute>()
//                .setQuery(query, Institute.class)
//                .build();
//
//        adapter = new InstituteAdapter(AdminViewInstitutesActivity.this, options, true);
//        recyclerView.setAdapter(adapter);
//    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        adapter.startListening();
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        adapter.stopListening();
//    }
}