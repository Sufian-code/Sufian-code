package com.example.fdas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Database extends AppCompatActivity {
CardView tfCv,tuCv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);

        tfCv=findViewById(R.id.tfCv);
        tuCv=findViewById(R.id.tuCv);



        tfCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),TotalFarmsActivity.class));
            }
        });


        tuCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),TotalUsersActivity.class));
            }
        });
    }
}