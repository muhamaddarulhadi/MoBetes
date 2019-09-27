package com.hadi.mobetes.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.SwitchCompat;
import android.widget.CompoundButton;

import com.hadi.mobetes.SharedPreferences.InitApplication;
import com.hadi.mobetes.R;

public class Settings extends AppCompatActivity
{
    SwitchCompat darkMode;

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

        //set layout theme, must be put on top contentview
        /*if(AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES)
        {
            setTheme(R.style.darkTheme);
        }
        else
        {
            setTheme(R.style.AppTheme);
        }*/

        setContentView(R.layout.settings);
        setTitle("Settings");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0); //remove shadow under action bar

        darkMode = findViewById(R.id.settings_darkMode_switch); //darkmode switch

        //if darkmode is on, it will toggle the switch to on
        if(AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES)
        {
            darkMode.setChecked(true);
        }

        //toggle the darkmode style
        darkMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if (isChecked)
                {
                    /*AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    getDelegate().applyDayNight();*/
                    InitApplication.getInstance().setIsNightModeEnabled(true);
                    Intent intent = getIntent();
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    finish();
                    startActivity(intent);
                }
                else
                {
                    /*AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    getDelegate().applyDayNight();*/
                    InitApplication.getInstance().setIsNightModeEnabled(false);
                    Intent intent = getIntent();
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    finish();
                    startActivity(intent);
                }
            }
        });
    }

    //when click back on phone, go to this activity
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();

        Intent menu = new Intent(this, Menu.class);
        menu.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(menu);

    }
}
