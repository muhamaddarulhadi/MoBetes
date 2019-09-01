package com.hadi.mobetes.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.widget.TextView;

import com.hadi.mobetes.SharedPreferences.InitApplication;
import com.hadi.mobetes.R;

public class Symptoms extends AppCompatActivity
{
    TextView symptoms;
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

        /*//set layout theme, must be put on top create
        if(AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES)
        {
            setTheme(R.style.darkTheme);
        }
        else
        {
            setTheme(R.style.AppTheme);
        }*/

        setContentView(R.layout.symptoms);
        setTitle("Symptoms");

        currentDayNight = AppCompatDelegate.getDefaultNightMode(); //get the darkmode setting

        symptoms = findViewById(R.id.symptoms);
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

        Intent menu = new Intent(this, Menu.class);
        //menu.setFlags(menu.getFlags() | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        menu.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(menu);
    }
}
