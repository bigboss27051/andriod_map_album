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

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btnSignUp;
    private Button btnLinkLogin;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etConfirmPassword;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initInstance();
        setupView();
        setupFirebaseAuth();

    }

    private void initInstance() {
        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        btnLinkLogin = (Button) findViewById(R.id.btnLinkLogin);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etConfirmPassword = (EditText) findViewById(R.id.etConfirmPassword);
        progressDialog = new ProgressDialog(SignUpActivity.this);


    }

    private void setupView() {
        btnSignUp.setOnClickListener(this);
        btnLinkLogin.setOnClickListener(this);
    }

    private void setupFirebaseAuth() {
        firebaseAuth = FirebaseAuth.getInstance();
    }



    @Override
    public void onClick(View v) {
        if (v == btnSignUp){
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();
            String confirmPassword = etConfirmPassword.getText().toString();
            signUp(email,password,confirmPassword);
        }else if(v == btnLinkLogin){
            openActivity(LoginActivity.class);
        }
    }

    private void signUp(@NonNull String username, @NonNull String password,@NonNull String confirmPassword) {
        if (isUsernameAndPasswordValidated(username, password,confirmPassword) == true) {
            progressBarShow();
            firebaseAuth.createUserWithEmailAndPassword(username, password)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            FirebaseUser user = authResult.getUser();
                            if (user != null) {
                                showToastMessage("CREATE ACCOUNT SUCCESS !!");
                                progressBarHide();
                                openActivity(MainActivity.class);
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            showToastMessage("CREATE ACCOUNT FAIL !!");
                            progressBarHide();
                        }
                    });
        }
    }

    private boolean isUsernameAndPasswordValidated(String username, String password,String confirmPassword) {
        if (Utility.isUsernameAndPasswordEmpty(username, password)) {
            showToastMessage(R.string.please_insert_username_password);
            return false;
        }
        if (Utility.isUsernameAndPasswordLessThan6Charactor(username, password)) {
            showToastMessage(R.string.username_password_contain_at_least_6_characters);
            return false;
        }

        if (Utility.isPasswordAndConfirmPasswordValidated(password,confirmPassword)){
            showToastMessage(R.string.please_check_password);
            return false;
        }
        return true;
    }

    private void showToastMessage(String message){
        Toast.makeText(SignUpActivity.this,message,Toast.LENGTH_SHORT).show();
    }

    private void showToastMessage(int strResID){
        Toast.makeText(SignUpActivity.this,strResID,Toast.LENGTH_SHORT).show();
    }

    private void progressBarShow(){
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
    }

    private  void progressBarHide(){
        progressDialog.dismiss();
    }

    private void openActivity(Class<?> cls) {
        startActivity(new Intent(this, cls));
        finish();
    }

}