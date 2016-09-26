package com.segunfamisa.auth0.demo;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefUtils {

    private String PREFS = "prefs_auth_demo";

    private String PREFS_ACCESS_TOKEN = "prefs_access_token";
    private String PREFS_REFRESH_TOKEN = "prefs_refresh_token";
    private String PREFS_ID_TOKEN = "prefs_id_token";

    private final Context context;
    private final SharedPreferences prefs;

    public PrefUtils(Context context) {
        this.context = context;
        prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    public void saveCredentials(String accessToken, String refreshToken, String idToken) {
        saveAccessToken(accessToken);
        saveRefreshToken(refreshToken);
        saveIdToken(idToken);
    }

    public void clearCredentials() {
        saveCredentials(null, null, null);
    }

    private void saveAccessToken(String accessToken) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PREFS_ACCESS_TOKEN, accessToken);
        editor.commit();
    }

    private void saveRefreshToken(String refreshToken) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PREFS_REFRESH_TOKEN, refreshToken);
        editor.commit();
    }

    private void saveIdToken(String idToken) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PREFS_ID_TOKEN, idToken);
        editor.commit();
    }

    public String getAccessToken() {
        return prefs.getString(PREFS_ACCESS_TOKEN, null);
    }

    public String getRefreshToken() {
        return prefs.getString(PREFS_REFRESH_TOKEN, null);
    }

    public String getIdToken() {
        return prefs.getString(PREFS_ID_TOKEN, null);
    }
}
