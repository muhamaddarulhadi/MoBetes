package com.hadi.mobetes.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hadi.mobetes.SharedPreferences.InitApplication;
import com.hadi.mobetes.R;

public class Login extends AppCompatActivity
{
    SQLiteDatabase user_db = null;
    String value = "";
    Button reset, login;
    EditText username,password;
    TextView register,forgot_password;
    CheckBox showNotPassword;
    int currentDayNight;
    public static final String USERLOGIN = "USERLOGIN";
    //public static final String REMEMBER_ME = "REMEMBER_ME";
    //Boolean Registered;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        createdb();  //create or open user database

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

        //set layout theme, must be put on top create
        /*if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
        {
            setTheme(R.style.darkTheme);
        }
        else
        {
            setTheme(R.style.AppTheme);
        }*/

        setContentView(R.layout.login);
        setTitle("Login");
        getSupportActionBar().setElevation(0); //remove shadow under action bar

        currentDayNight = AppCompatDelegate.getDefaultNightMode(); //get the darkmode setting

        //for x image on edittext
        final Drawable x = getResources().getDrawable(R.drawable.cancel);
        x.setBounds(0, 0, x.getIntrinsicWidth(), x.getIntrinsicHeight());

        //reset = findViewById(R.id.login_reset_btn);
        login = findViewById(R.id.login_login_btn);
        username = findViewById(R.id.login_username);
        password = findViewById(R.id.login_password);
        register = findViewById(R.id.login_register_btn);
        forgot_password = findViewById(R.id.login_forgotPassword_btn);
        showNotPassword = findViewById(R.id.login_showPassword_btn);

        //when click button login
        login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                login();
            }
        });

        //when click checkbox show password
        showNotPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if (isChecked)
                {
                    //show password
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else
                {
                    //hide password
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        //when click reset button, it will clear text inside it
        /*reset.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                username.setText("");
                password.setText("");
            }
        });*/

        //when click register, it will open register page
        register.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent register = new Intent(Login.this, Register.class);
                register.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(register);
            }
        });

        //when click forgot password, it will open forgot password page
        forgot_password.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent forgotPassword = new Intent(Login.this, Forgot_Password.class);
                forgotPassword.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(forgotPassword);
            }
        });




        //PUT X MARK WHEN THERE IS DATA ON EDITTEXT FOR TO RESET THE EDITTEXT

        //make reset button for username edittext only when there is text inside
        username.setText(value);
        username.setCompoundDrawables(null, null, value.equals("") ? null : x, null);
        username.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (username.getCompoundDrawables()[2] == null)
                {
                    return false;
                }
                if (event.getAction() != MotionEvent.ACTION_UP)
                {
                    return false;
                }
                if (event.getX() > username.getWidth() - username.getPaddingRight() - x.getIntrinsicWidth())
                {
                    username.setText("");
                    username.setCompoundDrawables(null, null, null, null);
                }
                return false;
            }
        });
        username.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                username.setCompoundDrawables(null, null, username.getText().toString().equals("") ? null : x, null);
            }

            @Override
            public void afterTextChanged(Editable arg0) { }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
        });

        //make reset button for password edittext only when there is text inside
        password.setText(value);
        password.setCompoundDrawables(null, null, value.equals("") ? null : x, null);
        password.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (password.getCompoundDrawables()[2] == null)
                {
                    return false;
                }
                if (event.getAction() != MotionEvent.ACTION_UP)
                {
                    return false;
                }
                if (event.getX() > password.getWidth() - password.getPaddingRight() - x.getIntrinsicWidth())
                {
                    password.setText("");
                    password.setCompoundDrawables(null, null, null, null);
                }
                return false;
            }
        });
        password.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                password.setCompoundDrawables(null, null, password.getText().toString().equals("") ? null : x, null);
            }

            @Override
            public void afterTextChanged(Editable arg0) { }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
        });
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

    //when click back on phone, start this activity
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();

        Intent menu = new Intent(this, Menu.class);
        menu.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(menu);
    }




    //SQLITE DATABASE

    //create user database
    public void createdb()
    {
        user_db = this.openOrCreateDatabase("user_db",MODE_PRIVATE,null);
        String sqlcreate = "CREATE TABLE IF NOT EXISTS USER(USERID INTEGER PRIMARY KEY AUTOINCREMENT, USERNAME VARCHAR NOT NULL UNIQUE, EMAIL VARCHAR NOT NULL UNIQUE, PASSWORD VARCHAR NOT NULL);";
        user_db.execSQL(sqlcreate);
    }

    //search registered user
    public void login()
    {
        String usernamedb = username.getText().toString();
        String passworddb = password.getText().toString();

        try
        {
            String sqlsearch = "SELECT * FROM USER WHERE USERNAME = '"+usernamedb+"' AND PASSWORD = '"+passworddb+"'";
            Cursor c = user_db.rawQuery(sqlsearch,null);

            if(c.getCount() > 0)
            {
                //store the username and password on shared preference
                SharedPreferences userLogin = getSharedPreferences(USERLOGIN,MODE_PRIVATE);  //set like this for to make multiple shared preference  //name like USERLOGIN can be change
                SharedPreferences.Editor editor = userLogin.edit();
                editor.putBoolean("Registered", true);
                editor.putString("username", usernamedb);
                editor.putString("password", passworddb);
                editor.apply();
                //editor.commit();

                Toast.makeText(this, "Login success", Toast.LENGTH_SHORT).show();
                Intent menu = new Intent(Login.this, Menu.class);
                startActivity(menu);
            }
            else
            {
                Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e)
        {
            Log.e("DB",e.toString());
        }
    }
}
