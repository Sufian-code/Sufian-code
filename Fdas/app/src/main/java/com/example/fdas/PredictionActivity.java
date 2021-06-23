package com.example.fdas;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.List;

public class PredictionActivity extends AppCompatActivity {
    List<String> itemList=new ArrayList<>();
    EditText ovEt,pvEt;
    MaterialSpinner spinner;
    TextView oTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prediction);

        ovEt=findViewById(R.id.ovEt);
        pvEt=findViewById(R.id.pvEt);
        oTv=findViewById(R.id.oTv);

        itemList.add("Rice ");
        itemList.add("wheat");


         spinner = (MaterialSpinner)findViewById(R.id.spinner);
        spinner.setItems(itemList);
    }

    public void calculateResult(View view) {

        if (ovEt.getText().toString().length()<1) {
            ovEt.setError("Please enter organic value");
            return;
        }
        if (pvEt.getText().toString().length()<1) {
            pvEt.setError("Please enter phosphorous value");
            return;
        }

        if (spinner.getText().toString().equals("wheat")){

            float ov=Float.valueOf(ovEt.getText().toString());
            float pv=Float.valueOf(pvEt.getText().toString());
            String soilCondition=null;
            if (ov<0.86&&pv<7.0){
                soilCondition="Poor";
            }
            else if (ov>=0.86&&ov<=1.29&&pv>=7.0&&pv<=21.0){
                soilCondition="Medium";
            }
            else if (ov>1.29&&pv>21.0){
                soilCondition="Fertile";
            }
            else if (ov<0.86&&pv>=7.0){
                soilCondition="Poor";
            }
            else if (ov>=0.86&&ov<=1.29&&pv<7.0||pv>21.0){
                soilCondition="Medium";
            }
            else if (ov>1.29&&pv<7.0||pv>7.0){
                soilCondition="Fertile";
            }



             if (soilCondition.equals("Poor")){
                 oTv.setText("Urea : 52 KG \nDAP : 46 KG");
             }
            else if (soilCondition.equals("Medium")){
                oTv.setText("Urea : 46 KG \nDAP : 34 KG");
            }
             else if (soilCondition.equals("Fertile")){
                 oTv.setText("Urea : 32 KG \nDAP : 23 KG");
             }



            Toast.makeText(this, ""+soilCondition, Toast.LENGTH_SHORT).show();





        }
        else if (spinner.getText().toString().equals("Rice")){




            float ov=Float.valueOf(ovEt.getText().toString());
            float pv=Float.valueOf(pvEt.getText().toString());
            String soilCondition=null;
            if (ov<0.86&&pv<7.0){
                soilCondition="Poor";
            }
            else if (ov>=0.86&&ov<=1.29&&pv>=7.0&&pv<=21.0){
                soilCondition="Medium";
            }
            else if (ov>1.29&&pv>21.0){
                soilCondition="Fertile";
            }
            else if (ov<0.86&&pv>=7.0){
                soilCondition="Poor";
            }
            else if (ov>=0.86&&ov<=1.29&&pv<7.0||pv>21.0){
                soilCondition="Medium";
            }
            else if (ov>1.29&&pv<7.0||pv>7.0){
                soilCondition="Fertile";
            }



            if (soilCondition.equals("Poor")){
                oTv.setText("Urea : 52 KG Per Ac \nDAP : 46 KG");
            }
            else if (soilCondition.equals("Medium")){
                oTv.setText("Urea : 46 KG \nDAP : 34 KG");
            }
            else if (soilCondition.equals("Fertile")){
                oTv.setText("Urea : 32 KG \nDAP : 23 KG");
            }



            Toast.makeText(this, ""+soilCondition, Toast.LENGTH_SHORT).show();

        }


    }
}