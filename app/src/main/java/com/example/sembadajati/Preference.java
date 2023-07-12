package com.example.sembadajati;

import android.content.Context;
import android.content.SharedPreferences;

public class Preference {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    private final String sessionLogin= "session_login";
    private final String userLogin= "user_login";

    public Preference(Context context) {
        pref = context.getSharedPreferences("MyPref", context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void setSessionLogin(boolean value) {
        editor.putBoolean(sessionLogin, value);
        editor.commit();
    }

    public boolean getSessionLogin() {
        return pref.getBoolean(sessionLogin, false);
    }

    public void setUserLogin(String value) {
        editor.putString(userLogin, value);
        editor.commit();
    }

    public String getUserLogin() {
        return pref.getString(userLogin, null);
    }

    public void clearAll() {
        editor.clear();
        editor.commit();
    }

}
