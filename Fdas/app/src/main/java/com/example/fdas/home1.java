package com.example.fdas;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class home1 extends AppCompatActivity {

  ImageView btnwatersam, btnsoilsam, btnsrvy,fddas,wtre,account;
 CardView setting;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activityhome);
        btnwatersam = findViewById(R.id.home);
//        setting = findViewById(R.id.setting);
//
//
//        setting.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getApplicationContext(),PredictionActivity.class));
//            }
//        });

        btnwatersam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent samr = new Intent(home1.this, Database.class);
                startActivity(samr);
                Toast.makeText(getApplicationContext(),"work",Toast.LENGTH_SHORT).show();
            }
        });
        fddas=findViewById(R.id.fmaps);
        fddas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sam = new Intent(home1.this, Fdas.class);
                startActivity(sam);
                Toast.makeText(getApplicationContext(),"open",Toast.LENGTH_SHORT).show();
            }

        });
        btnsrvy=findViewById(R.id.sudata);
        btnsrvy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sam = new Intent(home1.this, surveydata1.class);
                startActivity(sam);
                Toast.makeText(getApplicationContext(),"Hello",Toast.LENGTH_SHORT).show();
            }
        });
        btnsoilsam=findViewById(R.id.smple);
        btnsoilsam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sam = new Intent(home1.this, soilsample1.class);
                startActivity(sam);
                Toast.makeText(getApplicationContext(),"working Now",Toast.LENGTH_SHORT).show();
            }
        });
        wtre=findViewById(R.id.water);
        wtre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sam = new Intent(home1.this, watersample1.class);
                startActivity(sam);
                Toast.makeText(getApplicationContext(),"Wao",Toast.LENGTH_SHORT).show();
            }
        });
        account=findViewById(R.id.accounts);
        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sam = new Intent(home1.this, PredictionActivity.class);
                startActivity(sam);

            }
        });

    }

}





