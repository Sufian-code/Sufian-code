package com.example.fdas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.fdas.Model.FarmModel;
import com.example.fdas.Model.GetAllFermsResponse;
import com.example.fdas.Model.GetAllUserResponse;
import com.example.fdas.Model.UserModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TotalFarmsActivity extends AppCompatActivity {
    RecyclerView rv;
    List<FarmModel> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_farms);

        rv=findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));


        Call<GetAllFermsResponse> call=RetrofitClient.getInstance().getApi().fetchAllFarms();
        call.enqueue(new Callback<GetAllFermsResponse>() {
            @Override
            public void onResponse(Call<GetAllFermsResponse> call, Response<GetAllFermsResponse> response) {
                if (response.isSuccessful()){
                    list=response.body().getFarms();
                    rv.setAdapter(new GetAllFarmsAdapter(getApplicationContext(),list));

                }
                else {
                    Toast.makeText(TotalFarmsActivity.this, " Api Error" , Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetAllFermsResponse> call, Throwable t) {
                Toast.makeText(TotalFarmsActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}