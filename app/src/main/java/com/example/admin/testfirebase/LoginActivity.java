package com.example.admin.testfirebase;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    EditText txtEmail,txtPass;
    Button btnSign;
    ProgressDialog dialog;
    TextView txtReg;
    ImageView imgSee;
    boolean isReadable = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signinlayout);
        mAuth = FirebaseAuth.getInstance();
        init();

        txtReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);

            }
        });
        btnSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = ProgressDialog.show(LoginActivity.this, "",
                        "Loading. Please wait...", true);
                if (!txtEmail.getText().toString().isEmpty() && !txtPass.getText().toString().isEmpty())
                    SignIn();
                else {
                    dialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Email and Password can not be empty",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });
        imgSee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isReadable)
                    txtPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                else
                    txtPass.setTransformationMethod(null);
                isReadable = !isReadable;

            }
        });
    }

    private void SignIn() {
        mAuth.signInWithEmailAndPassword(txtEmail.getText().toString(), txtPass.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        dialog.dismiss();
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(LoginActivity.this, "Authentication failed, please check your Email and Password",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }



    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser!=null)
        {
            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
    private void init() {
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtPass = (EditText) findViewById(R.id.txtPass);
        imgSee = (ImageView) findViewById(R.id.imgSee);
        btnSign = (Button) findViewById(R.id.btnSign);
        txtReg = (TextView) findViewById(R.id.txtReg);
    }

    public void Onclick_HideKeyBoard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
    }
}
