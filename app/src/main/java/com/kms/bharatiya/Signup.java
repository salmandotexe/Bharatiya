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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firestore.v1.WriteResult;

import java.util.HashMap;
import java.util.Map;

public class Signup extends AppCompatActivity {
    EditText First;
    EditText Sec;
    EditText Email;
    EditText Phone;
    EditText Pass;
    EditText add;
    EditText cp;
    public int j;
    public String num;
    Button bitter;
    public String Fullname;
    public String text;
    FirebaseAuth auth;
    DatabaseReference rootref = FirebaseDatabase.getInstance().getReference();
    DatabaseReference rootref2= rootref.child("Users");

    DatabaseReference rootref1;

    DatabaseReference myref = FirebaseDatabase.getInstance().getReference();
    DatabaseReference rootref4= myref.child("Count");

    String viral;
    String viral1;
    String viral2;
    String viral3;
    DatabaseReference conref1;
    DatabaseReference conref2;
    DatabaseReference conref3;
    DatabaseReference conref4;

    private FirebaseFirestore fdb = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        bitter = findViewById(R.id.bottol);
        First = findViewById(R.id.first_name);
        Sec = findViewById(R.id.sec_name);
        Email = findViewById(R.id.Email);
        Phone = findViewById(R.id.Phone_number);
        Pass = findViewById(R.id.editText2);
        cp = findViewById(R.id.Confirm_password);
        add = findViewById(R.id.address);
        auth = FirebaseAuth.getInstance();
        Fullname = First.getText().toString() + " " + Sec.getText().toString();
        rootref4.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                text = dataSnapshot.getValue(String.class);

                j=Integer.parseInt(text);
                j=j+1;
                text=Integer.toString(j);
                num=text;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        bitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(First.getText().toString()) || TextUtils.isEmpty(Sec.getText().toString())||
                        TextUtils.isEmpty(Email.getText().toString()) || TextUtils.isEmpty(Pass.getText().toString())||
                        TextUtils.isEmpty(cp.getText().toString()) || TextUtils.isEmpty(Phone.getText().toString())||
                        TextUtils.isEmpty(add.getText().toString())){
                    Toast.makeText(Signup.this,"Please enter fields properly",Toast.LENGTH_LONG).show();

                }else {
                    boolean compa= Pass.getText().toString().equals(cp.getText().toString());
                    if(compa==true){
                        ProgressDialog p = new ProgressDialog(Signup.this);
                        p.setTitle("Signing Up");
                        p.setMessage("Seconds to Goodness");
                        p.show();

                        //Firebase part start
                        Map<String,Object> msg = new HashMap<>();
                        msg.put("temp","temp");
                        fdb.collection("users").document(Email.getText().toString()).set(msg);
                        //Firebase part end.

                        auth.createUserWithEmailAndPassword(Email.getText().toString(),Pass.getText().toString())
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if(task.isSuccessful()){

                                            Toast.makeText(Signup.this,"Registered",Toast.LENGTH_LONG).show();
                                            Intent i = new Intent(Signup.this, Login.class);

                                            startActivity(i);
                                        }
                                        else
                                        {
                                            Toast.makeText(Signup.this," not Registered",Toast.LENGTH_LONG).show();
                                            Intent i = new Intent(Signup.this, Signup.class);

                                            startActivity(i);
                                        }
                                    }
                                });

                        Fullname = First.getText().toString() + " " + Sec.getText().toString();
                        viral = Fullname;
                        viral1 = Email.getText().toString();
                        viral2 = Phone.getText().toString();
                        viral3 = add.getText().toString();


                        rootref1= rootref2.child(num);

                        conref1 = rootref1.child("Name");
                        conref1.setValue(viral);
                        conref2 = rootref1.child("Email");
                        conref2.setValue(viral1);
                        conref3 = rootref1.child("Phone");
                        conref3.setValue(viral2);
                        conref4 = rootref1.child("Address");
                        conref4.setValue(viral3);
                        rootref4.setValue(num);





                    }
                    else {
                        Toast.makeText(Signup.this,"Password do not match",Toast.LENGTH_LONG).show();
                    }




                }
            }


        });
    }}