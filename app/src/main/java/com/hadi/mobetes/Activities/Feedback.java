package com.hadi.mobetes.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hadi.mobetes.SharedPreferences.InitApplication;
import com.hadi.mobetes.R;
import com.hadi.mobetes.Helper.RequestHandler;

import org.w3c.dom.Text;

import java.util.HashMap;


public class Feedback extends AppCompatActivity
{
    String value = "";
    int currentDayNight;
    String serverurl= "https://hardkidz.000webhostapp.com/MoBetes";
    public static final String USERLOGIN = "USERLOGIN";
    Boolean Registered;

    @Override
    protected void onCreate( Bundle savedInstanceState)
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

        //set layout theme, must be put on top create
        /*if(AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES)
        {
            setTheme(R.style.darkTheme);
        }
        else
        {
            setTheme(R.style.AppTheme);
        }*/

        setContentView(R.layout.menu_feedback);
        setTitle("Feedback");

        //for x image on edittext
        final Drawable x = getResources().getDrawable(R.drawable.cancel);
        x.setBounds(0, 0, x.getIntrinsicWidth(), x.getIntrinsicHeight());

        currentDayNight = AppCompatDelegate.getDefaultNightMode(); //get the darkmode setting

        final EditText interfaces = findViewById(R.id.menu_feedback_interfaces);
        final EditText functions = findViewById(R.id.menu_feedback_functions);
        final EditText others = findViewById(R.id.menu_feedback_others);
        final TextView currentUser = findViewById(R.id.menu_feedback_currentUser);
        Button send = findViewById(R.id.menu_feedback_send_btn);
        //Button reset = findViewById(R.id.menu_feedback_reset_btn);

        //get the username from shared preference for to set username on header layout
        SharedPreferences userLogin = getSharedPreferences(USERLOGIN,MODE_PRIVATE);
        Registered = userLogin.getBoolean("Registered", false);
        if (Registered == true)
        {
            currentUser.setText(userLogin.getString("username",""));
        }
        else
        {
            currentUser.setText(null);
        }

        /*reset.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                interfaces.setText("");
                functions.setText("");
                others.setText("");
            }
        });*/


        send.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    if (currentUser.getText().toString().length()==0)
                    {
                        Toast.makeText(Feedback.this, "Please login first to send feedback", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        String interfaces_S = String.valueOf(interfaces.getText().toString());  //value that auto insert into textView must be put like this
                        String functions_S = functions.getText().toString();
                        String others_S = others.getText().toString();
                        String currentUser_S = currentUser.getText().toString();

                        if (interfaces_S.equals("") || functions_S.equals(""))
                        {
                            Toast.makeText(Feedback.this, "Incomplete value", Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            insertFeedback(currentUser_S,interfaces_S,functions_S,others_S);
                        }
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });




        //PUT X MARK WHEN THERE IS DATA ON EDITTEXT FOR TO RESET THE EDITTEXT

        //make reset button for interfaces edittext only when there is text inside
        interfaces.setText(value);
        interfaces.setCompoundDrawables(null, null, value.equals("") ? null : x, null);
        interfaces.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (interfaces.getCompoundDrawables()[2] == null)
                {
                    return false;
                }
                if (event.getAction() != MotionEvent.ACTION_UP)
                {
                    return false;
                }
                if (event.getX() > interfaces.getWidth() - interfaces.getPaddingRight() - x.getIntrinsicWidth())
                {
                    interfaces.setText("");
                    interfaces.setCompoundDrawables(null, null, null, null);
                }
                return false;
            }
        });
        interfaces.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                interfaces.setCompoundDrawables(null, null, interfaces.getText().toString().equals("") ? null : x, null);
            }

            @Override
            public void afterTextChanged(Editable arg0) { }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
        });

        //make reset button for functions edittext only when there is text inside
        functions.setText(value);
        functions.setCompoundDrawables(null, null, value.equals("") ? null : x, null);
        functions.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (functions.getCompoundDrawables()[2] == null)
                {
                    return false;
                }
                if (event.getAction() != MotionEvent.ACTION_UP)
                {
                    return false;
                }
                if (event.getX() > functions.getWidth() - functions.getPaddingRight() - x.getIntrinsicWidth())
                {
                    functions.setText("");
                    functions.setCompoundDrawables(null, null, null, null);
                }
                return false;
            }
        });
        functions.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                functions.setCompoundDrawables(null, null, functions.getText().toString().equals("") ? null : x, null);
            }

            @Override
            public void afterTextChanged(Editable arg0) { }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
        });

        //make reset button for others edittext only when there is text inside
        others.setText(value);
        others.setCompoundDrawables(null, null, value.equals("") ? null : x, null);
        others.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (others.getCompoundDrawables()[2] == null)
                {
                    return false;
                }
                if (event.getAction() != MotionEvent.ACTION_UP)
                {
                    return false;
                }
                if (event.getX() > others.getWidth() - others.getPaddingRight() - x.getIntrinsicWidth())
                {
                    others.setText("");
                    others.setCompoundDrawables(null, null, null, null);
                }
                return false;
            }
        });
        others.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                others.setCompoundDrawables(null, null, others.getText().toString().equals("") ? null : x, null);
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

        Intent menu = new Intent(this, Menu.class);
        menu.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(menu);
    }




    //CUSTOM METHOD

    //insert feedback on online database
    private void insertFeedback(final String currentUser_S, final String interfaces_S, final String functions_S, final String others_S)
    {
        class Insert extends AsyncTask<Void,Void,String>
        {
            @Override
            protected String doInBackground(Void... params)
            {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("currentUser_S",currentUser_S);
                hashMap.put("interfaces_S",interfaces_S);
                hashMap.put("functions_S",functions_S);
                hashMap.put("others_S",others_S);
                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest(serverurl +"/insertFeedback.php",hashMap);
                return s;
            }

            @Override
            protected void onPostExecute(String s)
            {
                super.onPostExecute(s);
                String[] sarray = s.split("\\s*,\\s*");
                String success = sarray[0];
                if (success.equals("success"))
                {
                    Toast.makeText(Feedback.this, "Success add feedback", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(Feedback.this, "Failed add feedback", Toast.LENGTH_LONG).show();
                }
            }
        }
        try
        {
            Insert insert = new Insert();
            insert.execute();
        }
        catch(Exception e){}
    }
}