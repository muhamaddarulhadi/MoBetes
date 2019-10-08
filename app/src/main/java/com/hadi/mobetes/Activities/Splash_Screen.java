package com.hadi.mobetes.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Constraints;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.hadi.mobetes.R;
import com.hadi.mobetes.SharedPreferences.InitApplication;

public class Splash_Screen extends AppCompatActivity
{

    ImageView logo;
    private static int SPLASH_TIME_OUT = 3000; //asal 3000, nanti ubah balik yang nih

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        ConstraintLayout bgElement = findViewById(R.id.background);

        //get DayNight mode from shared preference in InitApplication.java
        if (InitApplication.getInstance().isNightModeEnabled())
        {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            bgElement.setBackgroundColor(getResources().getColor(R.color.greyNight));
        }
        else
        {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            bgElement.setBackgroundColor(getResources().getColor(R.color.white));
        }

        logo = findViewById(R.id.logo_splash);

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                Intent splash = new Intent(Splash_Screen.this, Menu.class);
                startActivity(splash);
                finish();
            }
        },SPLASH_TIME_OUT);

        //set animation for logo
        Animation myanim = AnimationUtils.loadAnimation(this,R.anim.move_logo);
        logo.startAnimation(myanim);

        //hide notification bar
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

    }
}
