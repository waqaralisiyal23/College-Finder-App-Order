package com.codewithwaqar.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button userLoginButton;
    private TextView dontHaveAnAccountTV;
    private Button adminLoginButton;

    private ProgressDialog progressDialog;

    private FirebaseAuth mAuth;
//    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = (EditText) findViewById(R.id.login_email_field);
        passwordEditText = (EditText) findViewById(R.id.login_password_field);
        userLoginButton = (Button) findViewById(R.id.user_login_button);
        adminLoginButton = (Button) findViewById(R.id.admin_login_button);
        dontHaveAnAccountTV = (TextView) findViewById(R.id.dont_have_an_account_tv);

        progressDialog = new ProgressDialog(LoginActivity.this);
        mAuth = FirebaseAuth.getInstance();

//        mAuthListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                FirebaseUser user = firebaseAuth.getCurrentUser();
//                if (user != null) {
//                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intent);
//                    finish();
//                }
//            }
//        };

        userLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if(TextUtils.isEmpty(email) && TextUtils.isEmpty(password))
                    Toast.makeText(LoginActivity.this,
                            "Please enter your email and password", Toast.LENGTH_SHORT).show();
                else if(TextUtils.isEmpty(email))
                    Toast.makeText(LoginActivity.this, "Please enter your email",
                            Toast.LENGTH_SHORT).show();
                else if(TextUtils.isEmpty(password))
                    Toast.makeText(LoginActivity.this, "Please enter your password",
                            Toast.LENGTH_SHORT).show();
                else {
                    progressDialog.setTitle("Login");
                    progressDialog.setMessage("Authenticating, Please wait...");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();

                    loginUser(email, password);
                }
            }
        });

        adminLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if(TextUtils.isEmpty(email) && TextUtils.isEmpty(password))
                    Toast.makeText(LoginActivity.this,
                            "Please enter your email and password", Toast.LENGTH_SHORT).show();
                else if(TextUtils.isEmpty(email))
                    Toast.makeText(LoginActivity.this, "Please enter your email",
                            Toast.LENGTH_SHORT).show();
                else if(TextUtils.isEmpty(password))
                    Toast.makeText(LoginActivity.this, "Please enter your password",
                            Toast.LENGTH_SHORT).show();
                else {
                    progressDialog.setTitle("Login");
                    progressDialog.setMessage("Authenticating, Please wait...");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();

                    loginAdmin(email, password);
                }
            }
        });

        dontHaveAnAccountTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    private void loginAdmin(final String email, final String password) {
        DocumentReference docRef = FirebaseFirestore.getInstance().collection("admin")
                .document("adminData");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    String originalEmail = document.get("email").toString();
                    String originalPassword = document.get("password").toString();

                    if(email.equals(originalEmail) && password.equals(originalPassword)){
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, "Login Successful",
                                Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(LoginActivity.this, AdminHomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, "Invalid email or password",
                                Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Error: "+
                            task.getException().toString(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void loginUser(String email, String password) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "Login Successful",
                                    Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                        else{
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "Error: "+
                                    task.getException().toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        mAuth.addAuthStateListener(mAuthListener);
//    }
//
//    @Override
//    protected void onStop() {
//        if(mAuthListener!=null){
//            mAuth.removeAuthStateListener(mAuthListener);
//        }
//        super.onStop();
//    }
}