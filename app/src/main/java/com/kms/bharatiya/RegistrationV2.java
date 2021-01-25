package com.kms.bharatiya;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegistrationV2 extends AppCompatActivity {
    private EditText house_area, rent, flatsize, floorr, bed, bath;
    private ImageButton upload, takepic;
    private Button registerbutton;
    private TextView regi, disclaimer;
    public int j;
    public  String text;
    public  String num;
    public  String num2;
    public double lat ;
    public double lon;



    DatabaseReference rootref = FirebaseDatabase.getInstance().getReference();
    DatabaseReference rootref2 =rootref.child("House");
    DatabaseReference rootref1;

    DatabaseReference myref = FirebaseDatabase.getInstance().getReference();
    DatabaseReference rootref4= myref.child("Hcount");

    DatabaseReference conref1;
    DatabaseReference conref2;
    DatabaseReference conref3;
    DatabaseReference conref4;
    DatabaseReference conref5;
    DatabaseReference conref6;
    DatabaseReference conref7;
    DatabaseReference conref8;
    DatabaseReference conref9;
    String harea ;
    String em ;
    String rnt;
    String fsize;
    String floor;
    String nbed ;
    String nbath ;
    String lj ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_v2);
        Intent iin= getIntent();
        Bundle b = iin.getExtras();
        if(b!=null)
        {
            int h;
            lj =(String) b.get("Mail");
        }
        house_area= findViewById(R.id.area);
        rent= findViewById(R.id.house_num);
        //roadnum= findViewById(R.id.road_num);
        flatsize= findViewById(R.id.flat_size);
        floorr= findViewById(R.id.floor);
        bed= findViewById(R.id.bedroom);
        bath= findViewById(R.id.bathroom);
        upload=  findViewById(R.id.imageButton3);
        takepic=  findViewById(R.id.imageButton4);
        registerbutton=findViewById(R.id.register);
        regi=  findViewById(R.id.reg);
        disclaimer= findViewById(R.id.textView4);

        rootref4.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                text = dataSnapshot.getValue(String.class);

                j=Integer.parseInt(text);
                j=j+1;
                text=Integer.toString(j);
                num=text;
                num2=num;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        registerbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                harea = house_area.getText().toString();
                rnt = rent.getText().toString();
                //rnum = roadnum.getText().toString();
                fsize = flatsize.getText().toString();
                floor = floorr.getText().toString();
                nbed = bed.getText().toString();
                nbath = bath.getText().toString();
                lat = MainActivity.currentPosition.getLatitude();
                lon = MainActivity.currentPosition.getLongitude();


                em = lj;


                rootref1= rootref2.child(num2);
                conref1 = rootref1.child("Address");
                conref1.setValue(harea);
                conref2 = rootref1.child("Rent");
                conref2.setValue(rnt);

                conref3 = rootref1.child("Email");
                conref3.setValue(em);
                conref4 = rootref1.child("Flat Size");
                conref4.setValue(fsize);
                conref5 = rootref1.child("Floor");
                conref5.setValue(floor);
                conref6 = rootref1.child("Bedroom");
                conref6.setValue(nbed);
                conref7 = rootref1.child("Bathroom");
                conref7.setValue(nbath);
                conref8 = rootref1.child("LAT");
                conref8.setValue(lat);
                conref9 = rootref1.child("LONG");
                conref9.setValue(lon);
                rootref4.setValue(num2);

                Intent i = new Intent(RegistrationV2.this, RegSuccess.class);
                startActivity(i);


            }
        });

        disclaimer.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v)
            {
                startActivity(new Intent(RegistrationV2.this, RegSuccess.class));
            }
        });


    }


    private Boolean validate()
    {
        Boolean result = false;
        return result;

    }
}
