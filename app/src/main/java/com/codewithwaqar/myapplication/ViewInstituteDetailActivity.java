package com.codewithwaqar.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import models.Institute;

public class ViewInstituteDetailActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView imageView;
    private TextView nameTV;
    private TextView addressTV;
    private TextView typeTV;
    private TextView departmentsTV;
    private TextView departmentListTV;

    private Institute institute;
    private boolean isAddedNow = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_institute_detail);

        toolbar = (Toolbar) findViewById(R.id.detail_institute_toolbar);
        imageView = (ImageView) findViewById(R.id.detail_institute_image_view);
        nameTV = (TextView) findViewById(R.id.detail_institute_name);
        addressTV = (TextView) findViewById(R.id.detail_institute_address);
        typeTV = (TextView) findViewById(R.id.detail_institute_type);
        departmentsTV = (TextView) findViewById(R.id.detail_institute_departments);
        departmentListTV = (TextView) findViewById(R.id.detail_institute_department_list);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Institute Detail");

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            institute = (Institute) bundle.get("institute");
            if(bundle.get("added_now")!=null)
                isAddedNow = bundle.getBoolean("added_now");
        }


        Picasso.get().load(institute.getImageUrl()).placeholder(R.drawable.placeholder_image)
                .into(imageView);
        nameTV.setText(institute.getName());
        addressTV.setText(institute.getAddress());
        typeTV.setText("Type: "+institute.getType());
        if(institute.getDepartmentList()!=null){
            departmentsTV.setVisibility(View.VISIBLE);
            departmentListTV.setVisibility(View.VISIBLE);
            for(int i=0; i<institute.getDepartmentList().size(); i++){
                departmentListTV.setText(departmentListTV.getText()+"- "+
                        institute.getDepartmentList().get(i)+"\n");
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(isAddedNow){
            Intent intent = new Intent(ViewInstituteDetailActivity.this, AdminHomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
    }
}