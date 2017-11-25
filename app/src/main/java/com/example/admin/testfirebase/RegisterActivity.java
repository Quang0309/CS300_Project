package com.example.admin.testfirebase;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    EditText txtEmail, txtPass, txtPass2;
    Button btnReg;
    ProgressDialog dialog;
    RadioButton btnCheck;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signuplayout);
        init();
        mAuth = FirebaseAuth.getInstance();
        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = ProgressDialog.show(RegisterActivity.this, "",
                        "Loading. Please wait...", true);
                if (txtEmail.getText().toString().isEmpty() || txtPass.getText().toString().isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Input can not be empty.",
                            Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    return;
                }
                if (!txtPass.getText().toString().equals(txtPass2.getText().toString()))
                {
                    Toast.makeText(getApplicationContext(), "Retype password is not correct",
                            Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    return;
                }
                if(!btnCheck.isChecked())
                {
                    Toast.makeText(getApplicationContext(), "You must agree to the Terms of Use and Privacy Policy.",
                            Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    return;
                }
                Register();

            }
        });
    }
    private void Register() {
        mAuth.createUserWithEmailAndPassword(txtEmail.getText().toString(), txtPass.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        dialog.dismiss();
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(RegisterActivity.this, "Register successfully",
                                    Toast.LENGTH_SHORT).show();
                            finish();

                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }
    private void init() {
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtPass = (EditText) findViewById(R.id.txtPass);
        txtPass2 =(EditText) findViewById(R.id.txtPass2);
        btnReg =(Button) findViewById(R.id.btnReg);
        btnCheck = (RadioButton) findViewById(R.id.btnCheck);
    }
}
