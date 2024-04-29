package com.example.music_app1;

import android.content.Context;
import android.content.SharedPreferences;

public class Helper {
        public static String TOKEN_PREFS_NAME = "TokenPrefs";
        public static String KEY_UID_USER = "uid_user";
        public static String KEY_NAME = "name";
        public static String KEY_IS_PREMIUM = "is_premium";

        public static void saveUser(Context context, String uidUser, String name, Boolean isPremium ){
            SharedPreferences sharedPreferences = context.getSharedPreferences(TOKEN_PREFS_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(KEY_UID_USER, uidUser);
            editor.putString(KEY_NAME, name);
            editor.putBoolean(KEY_IS_PREMIUM, isPremium);
            editor.apply();
        }

    public static String getKeyUidUser(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(TOKEN_PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_UID_USER, null);
    }

    public static String getKeyName(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(TOKEN_PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_NAME, null);
    }

    public static Boolean getKeyIsPremium(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(TOKEN_PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(KEY_IS_PREMIUM, false);
    }
    public static void clearUser(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(TOKEN_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(KEY_UID_USER);
        editor.remove(KEY_NAME);
        editor.remove(KEY_IS_PREMIUM);
        editor.apply();
    }
}
