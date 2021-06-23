package com.example.fdas.Interfaces;

import com.example.fdas.Model.GetAllFermsResponse;
import com.example.fdas.Model.GetAllUserResponse;
import com.example.fdas.Model.LoginModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiInterface {
    @FormUrlEncoded
    @POST("login.php")
    Call<LoginModel>  login(@Field("email") String email ,@Field("password") String password);



    @GET("fetchallusers.php")
    Call<GetAllUserResponse>  fetchAllUsers();

    @GET("fetchallfarms.php")
    Call<GetAllFermsResponse>  fetchAllFarms();
}
