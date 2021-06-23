package com.example.fdas;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.fdas.Model.LoginModel;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class login1 extends AppCompatActivity {

    EditText emailEt, passwordEt;
    public Button btnlogin;
    Toolbar LoginToolBar;
    Prefrences prefrences;



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login1);
        emailEt = (EditText) findViewById(R.id.txtusername);
        passwordEt = (EditText) findViewById(R.id.txtpassword);
        btnlogin = (Button) findViewById(R.id.button);

        prefrences=new Prefrences(getApplicationContext());



        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (emailEt.getText().toString().trim().length() > 0 && passwordEt.getText().toString().trim().length() > 0){

                   loginUser();

                }else {
                    Toast.makeText(login1.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    private void loginUser() {
        Call<LoginModel> call=RetrofitClient.getInstance().getApi().login(emailEt.getText().toString().trim(),
                passwordEt.getText().toString().trim());

        call.enqueue(new Callback<LoginModel>() {
            @Override
            public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
             LoginModel loginResponse=response.body();
                if (response.isSuccessful()){
                    if (loginResponse.getError().equals("200")){
                        Toast.makeText(login1.this, loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        prefrences.saveUser(loginResponse.getUser());
                        goToHome();
                    }
                    else {

                        Toast.makeText(login1.this, loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
                else {
                    Toast.makeText(login1.this, loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<LoginModel> call, Throwable t) {
                Toast.makeText(login1.this, " "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void goToHome() {

        Intent intent=new Intent(getApplicationContext(),home1.class);
        startActivity(intent);
        finish();
    }


    @Override
    protected void onStart() {
        super.onStart();
        if(prefrences.isLoggedn()){
            goToHome();
        }
    }
}

