package com.kms.bharatiya;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    Button b1;
    Button b2;
    EditText email;
    EditText pass;
    String v1;
    FirebaseAuth auth;
    DatabaseReference rootref = FirebaseDatabase.getInstance().getReference();
    DatabaseReference rootref1= rootref.child("Status");
    DatabaseReference conref1 = rootref1.child("stat");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        auth = FirebaseAuth.getInstance();
        b1 = findViewById(R.id.button1);
        b2 = findViewById(R.id.button2);
        email= findViewById(R.id.editText);
        pass= findViewById(R.id.editText3);
        v1 = email.getText().toString();

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this, Signup.class);

                startActivity(i);
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(TextUtils.isEmpty(email.getText().toString()) || TextUtils.isEmpty(pass.getText().toString()))
                {
                    Toast.makeText(Login.this,"Please enter fields properly",Toast.LENGTH_LONG).show();

                }
                else
                {
                    ProgressDialog p = new ProgressDialog(Login.this);
                    p.setTitle("Logging in");
                    p.setMessage("Seconds to Goodness");
                    p.show();


                    auth.signInWithEmailAndPassword(email.getText().toString(),pass.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful())
                                    {

                                        Intent i = new Intent(Login.this,MainActivity.class);
                                        i.putExtra("Mail",email.getText().toString());
                                        startActivity(i);



                                    }
                                    else
                                    {
                                        //Intent i = new Intent(Signup.this,Signup.class);
                                        // startActivity(i);
                                        Toast.makeText(Login.this," login Failed",Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
            }
        });
    }
}