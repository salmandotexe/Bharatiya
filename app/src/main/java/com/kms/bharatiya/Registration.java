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

public class Registration extends AppCompatActivity {

    private EditText house_area, housenum, roadnum, flatsize, floorr, bed, bath;
    private ImageButton upload, takepic;
    private Button registerbutton;
    private TextView regi, disclaimer;
    public Registration() {
    }

    //private Button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        setupUIViews();

        registerbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()){
                    //upload data to the database
                }
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

    private void setupUIViews(){
        house_area= (EditText)findViewById(R.id.area);
        housenum= (EditText)findViewById(R.id.house_num);
        roadnum= (EditText)findViewById(R.id.road_num);
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

        String harea = house_area.getText().toString();
        String hnum = housenum.getText().toString();
        String rnum = roadnum.getText().toString();
        String fsize = flatsize.getText().toString();
        String floor = floorr.getText().toString();
        String nbed = bed.getText().toString();
        String nbath = bath.getText().toString();

        if(harea.isEmpty() && hnum.isEmpty() && rnum.isEmpty() && fsize.isEmpty() && floor.isEmpty() && nbed.isEmpty() && nbath.isEmpty()){
            Toast.makeText(this, "Please do not leave any columns blank", Toast.LENGTH_SHORT);
        } else{
            result = true;

        }
        return result;

    }
}