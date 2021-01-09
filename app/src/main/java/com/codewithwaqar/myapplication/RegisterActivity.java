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

public class RegisterActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private Button registerButton;
    private TextView alreadyHaveAnAccountTV;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailEditText = (EditText) findViewById(R.id.register_email_field);
        passwordEditText = (EditText) findViewById(R.id.register_password_field);
        confirmPasswordEditText = (EditText) findViewById(R.id.register_confirm_password_field);
        registerButton = (Button) findViewById(R.id.user_register_button);
        alreadyHaveAnAccountTV = (TextView) findViewById(R.id.already_have_an_account_tv);

        progressDialog = new ProgressDialog(RegisterActivity.this);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String confirmPassword = confirmPasswordEditText.getText().toString();

                if(TextUtils.isEmpty(email) && TextUtils.isEmpty(password))
                    Toast.makeText(RegisterActivity.this,
                            "Please enter your email and password", Toast.LENGTH_SHORT).show();
                else if(TextUtils.isEmpty(email))
                    Toast.makeText(RegisterActivity.this, "Please enter your email",
                            Toast.LENGTH_SHORT).show();
                else if(TextUtils.isEmpty(password))
                    Toast.makeText(RegisterActivity.this, "Please enter your password",
                            Toast.LENGTH_SHORT).show();
                else if(TextUtils.isEmpty(confirmPassword))
                    Toast.makeText(RegisterActivity.this, "Please enter confirm password",
                            Toast.LENGTH_SHORT).show();
                else if(!password.equals(confirmPassword))
                    Toast.makeText(RegisterActivity.this, "Please enter same password to confirm",
                            Toast.LENGTH_SHORT).show();
                else if(password.length()<8)
                    Toast.makeText(RegisterActivity.this, "Password must contain atleast 8 characters",
                            Toast.LENGTH_SHORT).show();
                else {
                    progressDialog.setTitle("Register");
                    progressDialog.setMessage("Please wait...");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();

                    register(email, password);
                }
            }
        });

        alreadyHaveAnAccountTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
    }

    private void register(String email, String password) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressDialog.dismiss();
                    Toast.makeText(RegisterActivity.this, "Registered Successfully",
                            Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                else{
                    progressDialog.dismiss();
                    Toast.makeText(RegisterActivity.this, "Error: "+
                                    task.getException().toString(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}