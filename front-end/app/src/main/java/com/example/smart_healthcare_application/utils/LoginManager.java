package com.example.smart_healthcare_application.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.smart_healthcare_application.models.User;
import com.google.gson.Gson;

public class LoginManager {

    private static final String PREF_NAME = "HealthcareApp_LoginPrefs";
    private static final String KEY_USER = "key_user_data";

    private static LoginManager instance;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Gson gson;

    private LoginManager(Context context) {
        sharedPreferences = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        gson = new Gson();
    }

    public static LoginManager getInstance(Context context) {
        if (instance == null) {
            synchronized (LoginManager.class) {
                if (instance == null) {
                    instance = new LoginManager(context);
                }
            }
        }
        return instance;
    }

    public void saveLoginSession(User user) {
        String userJson = gson.toJson(user);
        editor.putString(KEY_USER, userJson);
        editor.apply();
    }

    public User getUser() {
        String userJson = sharedPreferences.getString(KEY_USER, null);
        if (userJson != null) {
            return gson.fromJson(userJson, User.class);
        }
        return null;
    }

    public boolean isLoggedIn() {
        return sharedPreferences.contains(KEY_USER);
    }

    public void logout() {
        editor.clear();
        editor.apply();
    }
}