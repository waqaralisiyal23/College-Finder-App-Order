package com.codewithwaqar.myapplication.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.codewithwaqar.myapplication.AdminViewInstitutesActivity;
import com.codewithwaqar.myapplication.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import adapters.InstituteAdapter;
import adapters.MyInstituteAdapter;
import models.Institute;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class UniversityFragment extends Fragment {

    private EditText searchEditText;
    private RecyclerView recyclerView;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference institutesRef = db.collection("institutes");

//    private InstituteAdapter adapter;
    private MyInstituteAdapter adapter;
    private List<Institute> instituteList = new ArrayList<>();

    public UniversityFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_university, container, false);

        searchEditText = (EditText) view.findViewById(R.id.university_fragment_search_et);
        recyclerView = (RecyclerView) view.findViewById(R.id.university_fragment_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

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

        return view;
    }

    private void _fetchData(){
        institutesRef.whereEqualTo("type", "university").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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
                    adapter = new MyInstituteAdapter(getContext(),
                            instituteList, false);
                    recyclerView.setAdapter(adapter);
                }
                else{
                    Toast.makeText(getContext(), "Error: "+
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
//        Query query = institutesRef.whereEqualTo("type", "university");
//
//        FirestoreRecyclerOptions<Institute> options = new FirestoreRecyclerOptions.Builder<Institute>()
//                .setQuery(query, Institute.class)
//                .build();
//
//        adapter = new InstituteAdapter(getContext(), options, false);
//        recyclerView.setAdapter(adapter);
//    }
//
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