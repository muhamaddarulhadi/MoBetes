package com.hadi.mobetes.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hadi.mobetes.R;
import com.hadi.mobetes.SharedPreferences.InitApplication;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Forgot_Password extends AppCompatActivity
{
    SQLiteDatabase user_db = null;
    Properties emailProperties;
    Session mailSession;
    MimeMessage emailMessage;
    String value = "";
    int currentDayNight;
    EditText email,username;
    Button send;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //createdb();

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
        getSupportActionBar().setElevation(0); //remove shadow under action bar

        email = findViewById(R.id.fp_email);
        username = findViewById(R.id.fp_username);
        send = findViewById(R.id.send);

        send.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Toast.makeText(Forgot_Password.this, "Your email is "+email.getText(), Toast.LENGTH_SHORT).show();

                //getPassword();

            }
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

/*
    //CUSTOM METHOD

    public void setMailServerProperties()
    {
        String emailPort = "587";//gmail's smtp port

        emailProperties = System.getProperties();
        emailProperties.put("mail.smtp.port", emailPort);
        emailProperties.put("mail.smtp.auth", "true");
        emailProperties.put("mail.smtp.starttls.enable", "true");
    }

    public void createEmailMessage(String password, String email ) throws AddressException, MessagingException
    {
        String[] toEmails = { email };
        String emailSubject = "Your password from MoBetes application";
        String emailBody = "Password : "+password;

        mailSession = Session.getDefaultInstance(emailProperties, null);
        emailMessage = new MimeMessage(mailSession);

        for (int i = 0; i < toEmails.length; i++)
        {
            emailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmails[i]));
        }

        emailMessage.setSubject(emailSubject);
        emailMessage.setContent(emailBody, "text/html");//for a html email
        //emailMessage.setText(emailBody);// for a text email
    }

    public void sendEmail() throws AddressException, MessagingException
    {
        String emailHost = "smtp.gmail.com";
        String fromUser = "softwaretestingbyhadi";//just the id alone without @gmail.com
        String fromUserEmailPassword = "hadisoftwaretesting";

        Transport transport = mailSession.getTransport("smtp");

        transport.connect(emailHost, fromUser, fromUserEmailPassword);
        transport.sendMessage(emailMessage, emailMessage.getAllRecipients());
        transport.close();
        System.out.println("Email sent successfully.");
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
    public void getPassword()
    {
        final String email_S = email.getText().toString();
        final String username_S = username.getText().toString();

        try
        {
            String sqlsearch = "SELECT PASSWORD, EMAIL FROM USER WHERE USERNAME = '"+username_S+"' AND EMAIL = '"+email_S+"'";
            Cursor c = user_db.rawQuery(sqlsearch,null);

            if(c.getCount() > 0)
            {
                c.moveToFirst();
                for (int i=0;i<c.getCount();i++)
                {
                    String password = c.getString(c.getColumnIndex("PASSWORD"));
                    String email = c.getString(c.getColumnIndex("EMAIL"));

                    try
                    {
                        setMailServerProperties();
                        createEmailMessage(password,email);
                        sendEmail();
                    }
                    catch (AddressException e)
                    {
                        Toast.makeText(Forgot_Password.this, "Uncorrect email", Toast.LENGTH_SHORT).show();
                    }
                    catch (MessagingException e)
                    {
                        Toast.makeText(Forgot_Password.this, "Failed to sent", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            else
            {
                Toast.makeText(this, "Username and Email does not exist", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e)
        {
            Log.e("DB",e.toString());
        }
    }*/
}