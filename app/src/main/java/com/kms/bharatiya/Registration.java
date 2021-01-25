package com.kms.bharatiya;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Registration extends AppCompatActivity {

    private EditText house_area, housenum, roadnum, flatsize, floorr, bed, bath;
    private ImageButton upload, takepic;
    private Button registerbutton;
    private TextView regi, disclaimer;
    public Registration() {
    }

    DatabaseReference rootref = FirebaseDatabase.getInstance().getReference();
    DatabaseReference rootref1 =rootref.child("add");

    DatabaseReference conref1;
    DatabaseReference conref2;
    DatabaseReference conref3;
    DatabaseReference conref4;
    DatabaseReference conref5;
    DatabaseReference conref6;
    DatabaseReference conref7;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        setupUIViews();
        String harea = house_area.getText().toString();
        String hnum = housenum.getText().toString();
        String rnum = roadnum.getText().toString();
        String fsize = flatsize.getText().toString();
        String floor = floorr.getText().toString();
        String nbed = bed.getText().toString();
        String nbath = bath.getText().toString();

        registerbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                conref1 = rootref1.child("Area");
                conref1.setValue(harea);
                conref2 = rootref1.child("House Number ");
                conref2.setValue(hnum);
                conref3 = rootref1.child("Road Number");
                conref3.setValue(rnum);
                conref4 = rootref1.child("Flat Size");
                conref4.setValue(fsize);
                conref5 = rootref1.child("Floor");
                conref5.setValue(floor);
                conref6 = rootref1.child("Bedroom");
                conref6.setValue(nbed);
                conref7 = rootref1.child("Bathroom");
                conref7.setValue(nbath);


            }
        });

        disclaimer.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v)
            {
                startActivity(new Intent(Registration.this, MainActivity.class));
            }
        });


    }

    public void setupUIViews(){
        house_area= (EditText)findViewById(R.id.area);
        housenum= (EditText)findViewById(R.id.house_num);
        //roadnum= (EditText)findViewById(R.id.road_num);
        flatsize= (EditText)findViewById(R.id.flat_size);
        floorr= (EditText)findViewById(R.id.floor);
        bed= (EditText)findViewById(R.id.bedroom);
        bath= (EditText)findViewById(R.id.bathroom);
        upload= (ImageButton) findViewById(R.id.imageButton3);
        takepic= (ImageButton) findViewById(R.id.imageButton4);
        registerbutton= (Button)findViewById(R.id.register);
        regi= (TextView) findViewById(R.id.reg);
        disclaimer= (TextView) findViewById(R.id.textView4);


    }
    private Boolean validate()
    {
        Boolean result = false;
        return result;

    }
}
