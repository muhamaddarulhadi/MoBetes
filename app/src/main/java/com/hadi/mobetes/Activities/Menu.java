package com.hadi.mobetes.Activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.SnackbarContentLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.DividerItemDecoration;
import android.text.Html;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hadi.mobetes.SharedPreferences.AppEula;
import com.hadi.mobetes.Fragment.Chart;
import com.hadi.mobetes.Fragment.Data;
import com.hadi.mobetes.Fragment.Insert_bsl;
import com.hadi.mobetes.Fragment.Reminder;
import com.hadi.mobetes.SharedPreferences.InitApplication;
import com.hadi.mobetes.R;

import org.w3c.dom.Text;


public class Menu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    SQLiteDatabase user_db = null;
    private BottomNavigationView mainNav;
    private DrawerLayout slide;
    private ActionBarDrawerToggle toggle;
    NavigationView navigationView;
    int currentDayNight;
    ImageView user;
    TextView username,userid;
    public static final String USERLOGIN = "USERLOGIN";
    Boolean Registered;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        createdb();

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

        setContentView(R.layout.menu2);
        //setTitle("MoBetes");

        currentDayNight = AppCompatDelegate.getDefaultNightMode(); //get the darkmode setting

        new AppEula(this).show(); //call the eula agreement

        //bottom navigation
        mainNav = findViewById(R.id.bottom_navigation);
        mainNav.setOnNavigationItemSelectedListener(navListener);
        mainNav.setItemTextAppearanceActive(R.style.BottomNavigation);  //set the style when bottom navigation is active
        //mainNav.setItemTextColor(ColorStateList.valueOf(getResources().getColor(R.color.redLight))); set all the text color


        //drawer layout
        slide = findViewById(R.id.drawer);
        //slide.setFitsSystemWindows(true);
        toggle = new ActionBarDrawerToggle(this,slide,R.string.open,R.string.close);
        /*{
            public void onDrawerOpened(View view)
            {
                super.onDrawerOpened(view);

                Boolean Registered;
                final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(Menu.this);
                Registered = sharedPref.getBoolean("Registered", false);
                if (Registered == true)
                {
                    username.setText(sharedPref.getString("username",""));
                }
            }
        };*/
        slide.addDrawerListener(toggle);
        toggle.syncState();

        //navigation view in drawer layout
        navigationView = findViewById(R.id.navView);
        navigationView.setNavigationItemSelectedListener(this);

        //set the header programmatically
        //View headerLayout = navigationView.getHeaderView(R.layout.header_slide_navigation);  upper version
        View headerLayout = navigationView.inflateHeaderView(R.layout.header_slide_navigation);  //lower version
        username = headerLayout.findViewById(R.id.header_slide_navigation_username);
        userid = headerLayout.findViewById(R.id.header_slide_navigation_userId);

        //get the username from shared preference for to set username on header layout
        SharedPreferences userLogin = getSharedPreferences(USERLOGIN,MODE_PRIVATE);
        Registered = userLogin.getBoolean("Registered", false);
        if (Registered == true)
        {
            username.setText(userLogin.getString("username",""));
        }
        else
        {
            username.setText(null);
        }

        /*if(AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES)
        {
            user.setBackgroundResource(R.drawable.baseline_account_circle_white_48);
        }
        else
        {
            user.setBackgroundResource(R.drawable.baseline_account_circle_black_48);
        }*/

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //show everything on action bar : TRUE or FALSE
        getSupportActionBar().setElevation(0); //remove shadow under action bar
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new Insert_bsl()).commit(); //when this layout appear, it will show insert bsl layout
        mainNav.setSelectedItemId(R.id.nav_insert_bsl);  //set the bottom navigation icon
    }

    //set actionbar title centred
    /*public void setTitle(String title)
    {
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView textView = new TextView(this);
        textView.setText(title);
        textView.setTextSize(30);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(getResources().getColor(R.color.white));
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(textView);
    }*/
    //when click back button, it close the drawer layout

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();

        if (slide.isDrawerOpen(GravityCompat.START))
        {
            slide.closeDrawers();
            return;
        }
    }

    //when press home button
    /*@Override
    protected void onUserLeaveHint()
    {
        this.onStateNotSaved();
    }*/

    //show the bottom navigation
    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener()
    {
        @Override
        public boolean onNavigationItemSelected(MenuItem menuItem)
        {
            Fragment selectedFragment = null;

            switch (menuItem.getItemId())
            {
                case R.id.nav_insert_bsl:
                    selectedFragment = new Insert_bsl();
                    break;
                case R.id.nav_data:
                    selectedFragment = new Data();
                    break;
                case R.id.nav_chart:
                    selectedFragment = new Chart();
                    break;
                case R.id.nav_reminder:
                    selectedFragment = new Reminder();
                    break;
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.main_container, selectedFragment).commit(); //show the fragment
            return true;
        }
    };

    //when the activity is restart
    /*@Override
    protected void onRestart()
    {
        super.onRestart();

        //get the darkmode setting and create it back
        if (currentDayNight != AppCompatDelegate.getDefaultNightMode());
        {
            recreate();
        }
    }*/

    //show updown menu
    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.updown_menu, menu);
        return true;
    }

    //on action bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        //toggle button for drawer layout
        if (toggle.onOptionsItemSelected(item))
        {
            return true;
        }

        //triple dot menu and menu inside drawer layout
        switch (item.getItemId())
        {
            case R.id.item1:
                Intent feedback = new Intent (this, Feedback.class);
                startActivity(feedback);
                return true;
            case R.id.item2:
                new AppEula(this).show2();
                return true;
            case R.id.item3:
                loadDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //when click on slide menu, this will happen
    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem)
    {
        switch(menuItem.getItemId())
        {
            case R.id.account:
                //get the username from shared preference for to see if want to logout or login
                SharedPreferences userLogin = getSharedPreferences(USERLOGIN,MODE_PRIVATE);
                Registered = userLogin.getBoolean("Registered", false);
                if (Registered == true)
                {
                    logout();
                }
                else
                {
                    Intent login = new Intent (this, Login.class);
                    startActivity(login);
                }
                break;

            case R.id.symptoms:
                Intent symptoms = new Intent (this, Symptoms.class);
                startActivity(symptoms);
                break;

            case R.id.treatmens:
                Intent treatments = new Intent (this, Treatments.class);
                startActivity(treatments);
                break;

            case R.id.tips:
                Intent tips = new Intent (this, Tips.class);
                startActivity(tips);
                break;

            case R.id.settings:
                Intent setting = new Intent (this, Settings.class);
                startActivity(setting);
                break;

            case R.id.github:
                loadGithub();
                //Toast.makeText(Menu.this, "TEST 5", Toast.LENGTH_SHORT).show();
                break;
        }

        slide.closeDrawer(GravityCompat.START);
        return true;
    }




    //CUSTOM METHOD

    //show about dialog by clicking the triple dot menu
    private void loadDialog()
    {
        String title = "About";
        String ab = "This application is a project that are build by Hadi for Project 2 subject. The title for this application is MoBetes. Copyright 2019. ";
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder
                //.setTitle(title)
                .setTitle(Html.fromHtml("<font color='#FB4748'>"+title+"</font>")) //change title colour
                .setMessage(ab)
                .setCancelable(false)
                .setPositiveButton("OK",new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog,int id)
                    {
                        alertDialogBuilder.setCancelable(true);
                    }
                });

        //show alert dialog box with changing title line separator colour
        alertDialogBuilder.show();
        /*Dialog d = alertDialogBuilder.show();
        int dividerId = d.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
        View divider = d.findViewById(dividerId);
        divider.setBackgroundColor(getResources().getColor(R.color.redLight));*/

        //show alert dialog box without changing the colour
        /*AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();*/
    }

    //confirmation Bottom dialog to open github page
    private void loadGithub()
    {
        final BottomSheetDialog bottomSheetDelete = new BottomSheetDialog(this);
        bottomSheetDelete.setContentView(R.layout.custom_bottom_dialog_open_github);
        bottomSheetDelete.show();

        Button open = bottomSheetDelete.findViewById(R.id.custom_bottom_dialog_open_github_open_btn);
        Button cancel = bottomSheetDelete.findViewById(R.id.custom_bottom_dialog_open_github_cancel_btn);

        open.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent open_github = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/muhamaddarulhadi/MoBetes"));
                startActivity(open_github);
                bottomSheetDelete.cancel();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                bottomSheetDelete.cancel();
            }
        });
    }

    //confirmation Bottom dialog to open github page
    private void logout()
    {
        final BottomSheetDialog bottomSheetDelete = new BottomSheetDialog(this);
        bottomSheetDelete.setContentView(R.layout.custom_bottom_dialog_logout);
        bottomSheetDelete.show();

        Button logout = bottomSheetDelete.findViewById(R.id.custom_bottom_dialog_logout_logout_btn);
        Button cancel = bottomSheetDelete.findViewById(R.id.custom_bottom_dialog_logout_cancel_btn);

        logout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SharedPreferences userLogin = getSharedPreferences(USERLOGIN,MODE_PRIVATE);
                SharedPreferences.Editor editor = userLogin.edit();
                editor.clear();
                editor.commit();
                bottomSheetDelete.cancel();
                recreate();
                mainNav.setSelectedItemId(R.id.nav_insert_bsl);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                bottomSheetDelete.cancel();
            }
        });
    }

    //show confirmation to open github page on browser   //NOT USE
    /*private void loadGithub()
    {
        String ab = "Open developer github profile?\nThis will leave MoBetes.";
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder
                .setMessage(ab)
                .setCancelable(false)
                .setNegativeButton("CANCEL",new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog,int id)
                    {
                        alertDialogBuilder.setCancelable(true);
                    }
                })
                .setPositiveButton("OPEN",new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog,int id)
                    {
                        Intent open_github = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/muhamaddarulhadi/MoBetes"));
                        startActivity(open_github);
                    }
                });

        //AlertDialog alertDialog = alertDialogBuilder.create();
        //alertDialog.show();

        Dialog d = alertDialogBuilder.show();
        int dividerId = d.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
        View divider = d.findViewById(dividerId);
        divider.setBackgroundColor(getResources().getColor(R.color.redLight));
    }*/




    //SQLITE DATABASE

    //create user database
    public void createdb()
    {
        user_db = this.openOrCreateDatabase("user_db",MODE_PRIVATE,null);
        String sqlcreate = "CREATE TABLE IF NOT EXISTS USER(USERID INTEGER PRIMARY KEY AUTOINCREMENT, USERNAME VARCHAR NOT NULL UNIQUE, EMAIL VARCHAR NOT NULL UNIQUE, PASSWORD VARCHAR NOT NULL);";
        user_db.execSQL(sqlcreate);
    }
}
