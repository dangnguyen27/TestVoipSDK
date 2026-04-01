package com.interits.voipsdktest;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Random;
import java.util.UUID;

//import com.auth0.jwt.JWT;
//import com.auth0.jwt.exceptions.JWTDecodeException;
//import com.auth0.jwt.interfaces.DecodedJWT;
public class User {

    private String token;
    private String uid; //User unique id

    public User() {
    }
    public static String INFO_FILE = "com.gmobile.sdktest.user.txt";

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public static User load(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences(
                INFO_FILE, Context.MODE_PRIVATE);
        User userInfo = new User();
        userInfo.setToken(sharedPref.getString("token",null));
        userInfo.setUid(sharedPref.getString("uid", null));
        if(userInfo.uid==null){
            userInfo.setUid(UUID.randomUUID().toString());
            userInfo.save(context);
        }
        return  userInfo;
    }

    public void save(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences(
                INFO_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("token", token);
        editor.putString("uid", uid);
        editor.apply();
    }

    @Override
    public String toString() {
        return "User{" +
                "token='" + token + '\'' +
                ", uid='" + uid + '\'' +
                '}';
    }
}
