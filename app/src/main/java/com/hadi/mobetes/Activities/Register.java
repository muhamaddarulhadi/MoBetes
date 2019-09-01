package com.hadi.mobetes.Activities;

import android.content.Intent;
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

import com.hadi.mobetes.R;
import com.hadi.mobetes.SharedPreferences.InitApplication;

public class Register extends AppCompatActivity
{
    SQLiteDatabase user_db = null;
    String value = "";
    int currentDayNight;
    Button reset, register;
    EditText username,email,password,confirm_password;
    TextView login,menu;
    CheckBox showNotPassword;
    //private ArrayList<HashMap<String, String>> listdata;

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

        setContentView(R.layout.register);
        setTitle("Register");

        currentDayNight = AppCompatDelegate.getDefaultNightMode(); //get the darkmode setting

        //for x image on edittext
        final Drawable x = getResources().getDrawable(R.drawable.cancel);
        x.setBounds(0, 0, x.getIntrinsicWidth(), x.getIntrinsicHeight());


        username = findViewById(R.id.register_username);
        email = findViewById(R.id.register_email);
        password = findViewById(R.id.register_password);
        confirm_password = findViewById(R.id.register_confirm_password);
        //reset = findViewById(R.id.register_reset_btn);
        register = findViewById(R.id.register_register_btn);
        login = findViewById(R.id.register_login_btn);
        menu = findViewById(R.id.register_menu_btn);
        showNotPassword = findViewById(R.id.register_showPassword_btn);


        //when click register button
        register.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    if (username.getText().toString().equals("") || email.getText().toString().equals("") || password.getText().toString().equals("") || confirm_password.getText().toString().equals(""))
                    {
                        Toast.makeText(Register.this, "Please insert value on each field", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        if (password.getText().toString().trim().matches(confirm_password.getText().toString().trim()))
                        {
                            //Toast.makeText(Register.this, "Password match", Toast.LENGTH_SHORT).show();
                            registerUser();
                        }
                        else
                        {
                            Toast.makeText(Register.this, "Password not match", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
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
                    confirm_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else
                {
                    //hide password
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    confirm_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        //when click go to main menu
        menu.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent menu = new Intent(Register.this, Menu.class);
                menu.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(menu);
            }
        });

        //reset button
        /*reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username.setText("");
                email.setText("");
                password.setText("");
                confirm_password.setText("");
            }
        });*/

        //when click already a member, go to login page
        login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent register = new Intent(Register.this, Login.class);
                register.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(register);
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

        //make reset button for email edittext only when there is text inside
        email.setText(value);
        email.setCompoundDrawables(null, null, value.equals("") ? null : x, null);
        email.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (email.getCompoundDrawables()[2] == null)
                {
                    return false;
                }
                if (event.getAction() != MotionEvent.ACTION_UP)
                {
                    return false;
                }
                if (event.getX() > email.getWidth() - email.getPaddingRight() - x.getIntrinsicWidth())
                {
                    email.setText("");
                    email.setCompoundDrawables(null, null, null, null);
                }
                return false;
            }
        });
        email.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                email.setCompoundDrawables(null, null, email.getText().toString().equals("") ? null : x, null);
            }

            @Override
            public void afterTextChanged(Editable arg0) { }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
        });

        //make reset button for confirm_password edittext only when there is text inside
        confirm_password.setText(value);
        confirm_password.setCompoundDrawables(null, null, value.equals("") ? null : x, null);
        confirm_password.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (confirm_password.getCompoundDrawables()[2] == null)
                {
                    return false;
                }
                if (event.getAction() != MotionEvent.ACTION_UP)
                {
                    return false;
                }
                if (event.getX() > confirm_password.getWidth() - confirm_password.getPaddingRight() - x.getIntrinsicWidth())
                {
                    confirm_password.setText("");
                    confirm_password.setCompoundDrawables(null, null, null, null);
                }
                return false;
            }
        });
        confirm_password.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                confirm_password.setCompoundDrawables(null, null, confirm_password.getText().toString().equals("") ? null : x, null);
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

    //when click back on phone, go to this activity
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();

        Intent login = new Intent(this, Login.class);
        login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(login);

    }




    //SQLITE DATABASE

    //create user database
    public void createdb()
    {
        user_db = this.openOrCreateDatabase("user_db",MODE_PRIVATE,null);
        String sqlcreate = "CREATE TABLE IF NOT EXISTS USER(USERID INTEGER PRIMARY KEY AUTOINCREMENT, USERNAME VARCHAR NOT NULL UNIQUE, EMAIL VARCHAR NOT NULL UNIQUE, PASSWORD VARCHAR NOT NULL);";
        user_db.execSQL(sqlcreate);
    }

    //register user database
    public void registerUser()
    {
        String usernamedb = username.getText().toString();
        String emaildb = email.getText().toString();
        String passworddb = password.getText().toString();

        try
        {
            String sqlregister = "INSERT INTO USER(USERNAME,EMAIL,PASSWORD) VALUES ('"+usernamedb+"','"+emaildb+"','"+passworddb+"');";
            user_db.execSQL(sqlregister);
            Toast.makeText(this, "Success register user", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e)
        {
            Toast.makeText(this, "Error register user. Username and email cannot be register twice", Toast.LENGTH_SHORT).show();
            Log.e("DB",e.toString());
        }
    }
}