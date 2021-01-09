package com.codewithwaqar.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adapters.MyDepartmentAdapter;
import models.Institute;

public class AdminUpdateDataActivity extends AppCompatActivity {

    public static final int PICK_IMAGE = 2;

    private Toolbar toolbar;
    private ImageView imageView;
    private EditText nameEditText;
    private EditText addressEditText;
    private EditText urlEditText;
    private RecyclerView recyclerView;
    private EditText departmentEditText;
    private TextView addDepartmentTV;
    private Button updateButton;
    private RadioButton universityRadioButton;
    private RadioButton collegeRadioButton;

    private Uri imageUri = null;
    private ProgressDialog progressDialog;

    private String id;
    private String type = "university";

    private MyDepartmentAdapter adapter;
    private List<String> departmentList;

    private String previousImageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_update_data);

        toolbar = (Toolbar) findViewById(R.id.admin_update_data_toolbar);
        imageView = (ImageView) findViewById(R.id.update_data_image_view);
        nameEditText = (EditText) findViewById(R.id.update_data_name_field);
        addressEditText = (EditText) findViewById(R.id.update_data_address_field);
        urlEditText = (EditText) findViewById(R.id.update_data_url_field);
        departmentEditText = (EditText) findViewById(R.id.add_update_department_field);
        addDepartmentTV = (TextView) findViewById(R.id.admin_update_data_department_add_tv);
        updateButton = (Button) findViewById(R.id.update_data_update_button);
        universityRadioButton = (RadioButton) findViewById(R.id.update_data_university_rbtn);
        collegeRadioButton = (RadioButton) findViewById(R.id.update_data_college_rbtn);

        id = getIntent().getStringExtra("id");

        recyclerView = (RecyclerView) findViewById(R.id.admin_update_data_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(AdminUpdateDataActivity.this));

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Update Institute");

        progressDialog = new ProgressDialog(AdminUpdateDataActivity.this);
        departmentList = new ArrayList<>();

        fetchData();

        addDepartmentTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String departmentName = departmentEditText.getText().toString();
                if(TextUtils.isEmpty(departmentName)){
                    Toast.makeText(AdminUpdateDataActivity.this,
                            "Please enter department name", Toast.LENGTH_SHORT).show();
                }
                else{
                    departmentList.add(departmentName);
                    adapter.notifyDataSetChanged();
                    departmentEditText.setText("");
                }
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditText.getText().toString();
                String address = addressEditText.getText().toString();
                String url = urlEditText.getText().toString();

                if(TextUtils.isEmpty(name) && TextUtils.isEmpty(address))
                    Toast.makeText(AdminUpdateDataActivity.this,
                            "Please enter name and address", Toast.LENGTH_SHORT).show();
                else if(TextUtils.isEmpty(name))
                    Toast.makeText(AdminUpdateDataActivity.this, "Please enter name",
                            Toast.LENGTH_SHORT).show();
                else if(TextUtils.isEmpty(address))
                    Toast.makeText(AdminUpdateDataActivity.this, "Please enter address",
                            Toast.LENGTH_SHORT).show();
                else if(TextUtils.isEmpty(url))
                    Toast.makeText(AdminUpdateDataActivity.this, "Please enter url",
                            Toast.LENGTH_SHORT).show();
                else{
                    progressDialog.setTitle("Updating Data");
                    progressDialog.setMessage("Please wait...");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();

                    if(imageUri!=null)
                        uploadImage(name, address, url);
                    else
                        updateData(null, name, address, url);
                }
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });
    }

    private void fetchData(){
        final DocumentReference docRef = FirebaseFirestore.getInstance().collection("institutes")
                .document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    previousImageUrl = document.get("imageUrl").toString();
                    String name = document.get("name").toString();
                    String address = document.get("address").toString();
                    String instituteType = document.get("type").toString();
                    String url = document.get("url").toString();

                    if(document.contains("departments"))
                        departmentList = (List<String>) document.get("departments");

                    Picasso.get().load(previousImageUrl).placeholder(R.drawable.placeholder_image)
                            .into(imageView);

                    nameEditText.setText(name);
                    addressEditText.setText(address);
                    urlEditText.setText(url);
                    if(instituteType.equals("college")) {
                        collegeRadioButton.setChecked(true);
                        type = "college";
                    }
                    else {
                        universityRadioButton.setChecked(true);
                        type = "university";
                    }

                    adapter = new MyDepartmentAdapter(AdminUpdateDataActivity.this, departmentList);
                    recyclerView.setAdapter(adapter);
                }
                else{
                    Toast.makeText(AdminUpdateDataActivity.this, "Error: "+
                            task.getException().toString(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==PICK_IMAGE && resultCode==RESULT_OK && data!=null){
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
    }

    private void uploadImage(final String name, final String address, final String url){
        // Create a storage reference from our app
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();

        Date currentTime = Calendar.getInstance().getTime();
        final StorageReference imageRef = storageRef.child("images").child(imageUri.getLastPathSegment()+currentTime);
        UploadTask uploadTask = imageRef.putFile(imageUri);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return imageRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    updateData(downloadUri.toString(), name, address, url);
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(AdminUpdateDataActivity.this, "Error: "+
                            task.getException().toString(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void updateData(final String imageUrl, final String name, final String address, final String url) {

        final List<String> departments = adapter.getDepartmentList();

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("name", name);
        dataMap.put("address", address);
        dataMap.put("type", type);
        dataMap.put("url", url);
        if(imageUrl!=null)
            dataMap.put("imageUrl", imageUrl);
        if(departments.size()>0)
            dataMap.put("departments", departments);

        FirebaseFirestore.getInstance()
                .collection("institutes")
                .document(id)
                .update(dataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            progressDialog.dismiss();
                            Toast.makeText(AdminUpdateDataActivity.this, "Data Updated",
                                    Toast.LENGTH_LONG).show();
//                            Intent intent = new Intent(AdminUpdateDataActivity.this, AdminHomeActivity.class);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                            startActivity(intent);
//                            finish();
                            Institute institute = new Institute();
                            institute.setId(id);
                            if(imageUrl!=null)
                                institute.setImageUrl(imageUrl);
                            else
                                institute.setImageUrl(previousImageUrl);
                            institute.setName(name);
                            institute.setAddress(address);
                            institute.setType(type);
                            if(departments.size()>0)
                                institute.setDepartmentList(departments);
                            else
                                institute.setDepartmentList(null);

                            Intent intent = new Intent(AdminUpdateDataActivity.this, ViewInstituteDetailActivity.class);
                            intent.putExtra("institute", institute);
                            intent.putExtra("added_now", true);
                            startActivity(intent);
                        }
                        else{
                            progressDialog.dismiss();
                            Toast.makeText(AdminUpdateDataActivity.this, "Error: "+
                                    task.getException().toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.update_data_university_rbtn:
                if (checked)
                    type = "university";
                break;
            case R.id.update_data_college_rbtn:
                if (checked)
                    type = "college";
                break;
        }
    }
}