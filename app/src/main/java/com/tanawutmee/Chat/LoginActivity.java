package com.tanawutmee.Chat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.tanawutmee.Chat.utility.Utility;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnLogin;
    private Button btnLinkSignUp;
    private EditText etEmail;
    private EditText etPassword;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initInstance();
        setUpView();
        setupFirebaseAuth();
        checkAlreadyLoggedIn();

    }

    private void setUpView() {
        btnLogin.setOnClickListener(this);
        btnLinkSignUp.setOnClickListener(this);
    }

    private void setupFirebaseAuth() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void checkAlreadyLoggedIn() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            openActivity(MainActivity.class);
        }
    }

    private void initInstance() {
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLinkSignUp = (Button) findViewById(R.id.btnLinkSignUp);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        progressDialog = new ProgressDialog(LoginActivity.this);
    }

    private void openActivity(Class<?> cls) {
        startActivity(new Intent(this, cls));
        finish();
    }

    @Override
    public void onClick(View v) {
        if(v == btnLinkSignUp){
            openActivity(SignUpActivity.class);
        }else if (v == btnLogin){
            String username = etEmail.getText().toString();
            String password = etPassword.getText().toString();
            signIn(username,password);
        }
    }

    public void signIn(@NonNull String username, @NonNull String password){
        if(isUsernameAndPasswordValidated(username,password)){
            progressBarShow();
            firebaseAuth.signInWithEmailAndPassword(username,password)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            FirebaseUser user = authResult.getUser();
                            if(user != null){
                                progressBarHide();
                                openActivity(MainActivity.class);
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBarHide();
                            showToastMessage("Login Fail !!");
                        }
                    });
        }
    }

    private boolean isUsernameAndPasswordValidated(String username, String password) {
        if (Utility.isUsernameAndPasswordEmpty(username, password)) {
            showToastMessage(R.string.please_insert_username_password);
            return false;
        }
        if (Utility.isUsernameAndPasswordLessThan6Charactor(username, password)) {
            showToastMessage(R.string.username_password_contain_at_least_6_characters);
            return false;
        }

        return true;
    }

    private void showToastMessage(String message){
        Toast.makeText(LoginActivity.this,message,Toast.LENGTH_SHORT).show();
    }

    private void showToastMessage(int strResID){
        Toast.makeText(LoginActivity.this,strResID,Toast.LENGTH_SHORT).show();
    }

    private void progressBarShow(){
        progressDialog.setIndeterminate(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();
    }

    private  void progressBarHide(){
        progressDialog.dismiss();
    }


}

