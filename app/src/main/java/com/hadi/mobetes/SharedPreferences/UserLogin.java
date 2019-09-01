package com.hadi.mobetes.SharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.hadi.mobetes.Model.User_data;

public class UserLogin
{
    User_data userData = new User_data();

    public boolean saveUsername(String username, Context context)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString(userData.getUsername(), username);
        prefsEditor.apply();
        return true;
    }

    public String getUsername(Context context)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(userData.getUsername(), null);
    }

    public boolean savePassword(String password, Context context)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString(userData.getPassword(), password);
        prefsEditor.apply();
        return true;
    }

    public String getPassword(Context context)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(userData.getPassword(), null);
    }

}
