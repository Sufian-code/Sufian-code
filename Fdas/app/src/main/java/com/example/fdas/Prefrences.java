package com.example.fdas;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.fdas.Model.UserModel;

public class Prefrences {

    private static String PREF_NAME="Register_pref";
    private SharedPreferences sharedPreferences;
    Context context;
    private SharedPreferences.Editor editor;

    public Prefrences(Context context) {
        this.context = context;
    }

    public void saveUser(UserModel userModel){
        sharedPreferences=context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
        editor.putInt("id",userModel.getId());
        editor.putString("name",userModel.getName());
        editor.putString("email",userModel.getEmail());
        editor.putBoolean("logged",true);
        editor.apply();
    }

    public boolean isLoggedn(){
        sharedPreferences=context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
       return sharedPreferences.getBoolean("logged",false);
    }



}
