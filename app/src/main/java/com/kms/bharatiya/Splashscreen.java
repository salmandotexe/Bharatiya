package com.kms.bharatiya;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

public class Splashscreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {

                Intent i = new Intent(Splashscreen.this, Login.class);

                startActivity(i);
                finish();

            }
        },5000);
    }
}