package com.example.fdas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.fdas.Model.GetAllUserResponse;
import com.example.fdas.Model.UserModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TotalUsersActivity extends AppCompatActivity {
RecyclerView rv;
List<UserModel> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_users);

        rv=findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));


        Call<GetAllUserResponse> call=RetrofitClient.getInstance().getApi().fetchAllUsers();
        call.enqueue(new Callback<GetAllUserResponse>() {
            @Override
            public void onResponse(Call<GetAllUserResponse> call, Response<GetAllUserResponse> response) {
                if (response.isSuccessful()){
                    list=response.body().getUsers();
                    rv.setAdapter(new AllUsersAdapter(getApplicationContext(),list));

                }
                else {
                    Toast.makeText(TotalUsersActivity.this, " Api Error" , Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetAllUserResponse> call, Throwable t) {
                Toast.makeText(TotalUsersActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}