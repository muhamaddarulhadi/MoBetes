  package com.hadi.mobetes.Fragment;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDelegate;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.hadi.mobetes.R;

import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;

public class Reminder extends Fragment
{
    String value = "";
    public static final String USERLOGIN = "USERLOGIN";
    Boolean Registered;

    public Reminder() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reminder, container, false);
    }

    //what the application do when the fragment is start
    @Override
    public void onStart()
    {
        super.onStart();
        setHasOptionsMenu(true); //show the + menu to add a reminder
    }

    //what the application do when the fragment is use
    @Override
    public void onActivityCreated (Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);





    }

    //make the add button exist on app bar
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.add_reminder_button, menu);
    }

    //what happen when the add button are clicked
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            //when the button is push, it will show a pop up to add a Reminder
            case R.id.add_remind:
                loadDialogAddReminder();
                //Toast.makeText(Reminder.this.getActivity(), "Add a Reminder", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }




    //CUSTOM METHOD

    //custom dialog box for to add Reminder
    private void loadDialogAddReminder()
    {
        final Dialog aboutDialog = new Dialog(this.getActivity(), R.style.dialog);
        aboutDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        aboutDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        aboutDialog.getWindow().setGravity(0);
        aboutDialog.setContentView(R.layout.dialog_add_reminder);

        //for x image on edittext
        final Drawable x = getResources().getDrawable(R.drawable.cancel);
        x.setBounds(0, 0, x.getIntrinsicWidth(), x.getIntrinsicHeight());

        //put this to set the background of dialog to follow the DayNight theme
        View v = aboutDialog.getWindow().getDecorView();
        //v.setBackgroundResource(android.R.color.transparent);

        if(AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES)
        {
            v.setBackgroundResource(R.drawable.dialog_round_corner_greylight);
            //aboutDialog.getContext().setTheme(R.style.darkTheme);
        }
        else
        {
            v.setBackgroundResource(R.drawable.dialog_round_corner_white);
            //aboutDialog.getContext().setTheme(R.style.AppTheme);
        }

        aboutDialog.show();

        //currentUser
        final TextView currentUser = aboutDialog.findViewById(R.id.dialog_add_reminder_currentUser);
        //get the username from shared preference for to set username on current user
        SharedPreferences userLogin = getActivity().getSharedPreferences(USERLOGIN,MODE_PRIVATE);
        Registered = userLogin.getBoolean("Registered", false);
        if (Registered == true)
        {
            currentUser.setText(userLogin.getString("username",""));
        }
        else
        {
            currentUser.setText(null);
        }

        //insert Reminder title
        final TextView darti = aboutDialog.findViewById(R.id.dialog_add_reminder_title);

        //make reset button for darti edittext only when there is text inside
        darti.setText(value);
        darti.setCompoundDrawables(null, null, value.equals("") ? null : x, null);
        darti.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (darti.getCompoundDrawables()[2] == null)
                {
                    return false;
                }
                if (event.getAction() != MotionEvent.ACTION_UP)
                {
                    return false;
                }
                if (event.getX() > darti.getWidth() - darti.getPaddingRight() - x.getIntrinsicWidth())
                {
                    darti.setText("");
                    darti.setCompoundDrawables(null, null, null, null);
                }
                return false;
            }
        });
        darti.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                darti.setCompoundDrawables(null, null, darti.getText().toString().equals("") ? null : x, null);
            }

            @Override
            public void afterTextChanged(Editable arg0) { }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
        });

        //when user click the date text view, it will show the date picker popup
        final TextView dard = aboutDialog.findViewById(R.id.dialog_add_reminder_date);
        dard.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(Reminder.this.getActivity(), R.style.DialogTheme ,new DatePickerDialog.OnDateSetListener()
                {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day)
                    {
                        month+=1;
                        dard.setText(day + "/" + month + "/" + year);
                    }}, year, month, dayOfMonth);

                datePickerDialog.show();

                //set the divider colour on date picker dialog
                /*int dividerId = datePickerDialog.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
                View divider = datePickerDialog.findViewById(dividerId);
                divider.setBackgroundColor(getResources().getColor(R.color.redLight));*/
            }
        });

        //when user click the date text view, it will show the time picker popup
        final TextView dart = aboutDialog.findViewById(R.id.dialog_add_reminder_time);
        dart.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                final Calendar calendar = Calendar.getInstance();
                int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                final boolean is24HourView = false;

                TimePickerDialog timePickerDialog = new TimePickerDialog(Reminder.this.getActivity(),R.style.DialogTheme ,new TimePickerDialog.OnTimeSetListener()
                {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute)
                    {
                        //to make the time set as user choose on time picker dialog
                        String AM_PM = " AM";
                        String mm_precede = "";

                        if (hourOfDay >= 12)
                        {
                            AM_PM = " PM";

                            if (hourOfDay >= 13 && hourOfDay < 24)
                            {
                                hourOfDay -= 12;
                            }
                            else
                            {
                                hourOfDay = 12;
                            }
                        }
                        else if (hourOfDay == 0)
                        {
                            hourOfDay = 12;
                        }

                        if (minute < 10)
                        {
                            mm_precede = "0";
                        }

                        dart.setText(hourOfDay + " : " + mm_precede + minute + AM_PM);
                        //art.setText(hourOfDay + " : " + minute);

                    }
                }, hourOfDay, minute, is24HourView);

                //timePickerDialog.setTitle(null);
                timePickerDialog.show();

                //set the divider colour on time picker dialog
                /*int dividerId = timePickerDialog.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
                View divider = timePickerDialog.findViewById(dividerId);
                divider.setBackgroundColor(getResources().getColor(R.color.grey));*/
            }
        });

        //insert Reminder notes
        final TextView darn = aboutDialog.findViewById(R.id.dialog_add_reminder_note);

        //make reset button for darn edittext only when there is text inside
        darn.setText(value);
        darn.setCompoundDrawables(null, null, value.equals("") ? null : x, null);
        darn.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (darn.getCompoundDrawables()[2] == null)
                {
                    return false;
                }
                if (event.getAction() != MotionEvent.ACTION_UP)
                {
                    return false;
                }
                if (event.getX() > darn.getWidth() - darn.getPaddingRight() - x.getIntrinsicWidth())
                {
                    darn.setText("");
                    darn.setCompoundDrawables(null, null, null, null);
                }
                return false;
            }
        });
        darn.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                darn.setCompoundDrawables(null, null, darn.getText().toString().equals("") ? null : x, null);
            }

            @Override
            public void afterTextChanged(Editable arg0) { }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
        });

        //when click the button reset, it will reset the insert Data
        /*final Button darcb = aboutDialog.findViewById(R.id.dialog_add_reminder_clear_btn);
        darcb.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                darti.setText(null);
                dard.setText(null);
                dart.setText(null);
                darn.setText(null);
            }
        });*/

        //when click the add button, it will save the Data on sqlite and update the reminder_list_view
        final Button darab = aboutDialog.findViewById(R.id.dialog_add_reminder_add_btn);
        darab.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                String darti_S = String.valueOf(darti.getText().toString());
                String dard_S = String.valueOf(dard.getText().toString());
                String dart_S = String.valueOf(dart.getText().toString());

                if (darti_S.equals("") || dard_S.equals("") || dart_S.equals(""))
                {
                    Toast.makeText(Reminder.this.getActivity(), "Incomplete Data", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(Reminder.this.getActivity(), "Reminder are set", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }




    //REVISION CODE

    public void Data()
    {
        String title = getActivity().findViewById(R.id.dialog_add_reminder_title).toString();
        String date = getActivity().findViewById(R.id.dialog_add_reminder_date).toString();
        String time = getActivity().findViewById(R.id.dialog_add_reminder_time).toString();
        String note = getActivity().findViewById(R.id.dialog_add_reminder_note).toString();
    }
}
