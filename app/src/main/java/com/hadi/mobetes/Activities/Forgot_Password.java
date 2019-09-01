package com.hadi.mobetes.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;

import com.hadi.mobetes.R;
import com.hadi.mobetes.SharedPreferences.InitApplication;

public class Forgot_Password extends AppCompatActivity
{
    String value = "";
    int currentDayNight;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //get DayNight mode from shared preference in InitApplication.java
        if (InitApplication.getInstance().isNightModeEnabled())
        {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            setTheme(R.style.darkTheme);
        }
        else
        {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            setTheme(R.style.AppTheme);
        }

        setContentView(R.layout.forgot_password);
        setTitle("Forgot Password");

        currentDayNight = AppCompatDelegate.getDefaultNightMode(); //get the darkmode setting


    }

    //when the application is restart
    @Override
    protected void onRestart()
    {
        super.onRestart();

        //get the darkmode setting and create it back
        if (currentDayNight != AppCompatDelegate.getDefaultNightMode());
        {
            recreate();
        }
    }

    //when click back on phone, go to this activity
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();

        Intent login = new Intent(this, Login.class);
        login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(login);

    }
}