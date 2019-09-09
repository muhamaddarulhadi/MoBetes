package com.hadi.mobetes.Fragment;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.hadi.mobetes.Activities.Menu;
import com.hadi.mobetes.R;

import java.util.Calendar;
import java.util.InputMismatchException;

import static android.content.Context.MODE_PRIVATE;

public class Insert_bsl extends Fragment
{
    String value = "";
    SQLiteDatabase data_db;
    public static final String USERLOGIN = "USERLOGIN";
    Boolean Registered;
    //private ArrayList<HashMap<String, String>> listdata;

    public Insert_bsl() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_insert_bsl, container, false);
    }

    //what the application do when the fragment is start
    @Override
    public void onStart()
    {
        super.onStart();
        //setHasOptionsMenu(true); //show the 3 dot menu //not use because already put on Menu.java
        createdb(); //calling the method to create or open the database
    }

    //what the application do when the fragment is use
    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        //for x image on edittext
        final Drawable x = getResources().getDrawable(R.drawable.cancel);
        x.setBounds(0, 0, x.getIntrinsicWidth(), x.getIntrinsicHeight());

        final Double zero = 0.0;

        final TextView currentUser = getActivity().findViewById(R.id.fragment_insert_bsl_currentUser);
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

        final EditText bsl = getActivity().findViewById(R.id.fragment_insert_bsl_bsl);
        bsl.requestFocus();

        final Spinner condition = getActivity().findViewById(R.id.fragment_insert_bsl_condition_spinner);
        //set the spinner to use the setting on the spinner_item layout
        ArrayAdapter adapter1 = ArrayAdapter.createFromResource(this.getActivity(), R.array.condition, R.layout.spinner_item);
        condition.setAdapter(adapter1);

        final Spinner mood = getActivity().findViewById(R.id.fragment_insert_bsl_mood_spinner);
        //set the spinner to use the setting on the spinner_item layout
        ArrayAdapter adapter2 = ArrayAdapter.createFromResource(this.getActivity(), R.array.mood, R.layout.spinner_item);
        mood.setAdapter(adapter2);

        final EditText note = getActivity().findViewById(R.id.fragment_insert_bsl_note);

        //when user click the date text view, it will show the date picker dialog
        final TextView date = getActivity().findViewById(R.id.fragment_insert_bsl_date);
        date.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                //final DatePickerDialog datePickerDialog = new DatePickerDialog(Insert_bsl.this.getActivity(), R.style.DialogTheme ,new DatePickerDialog.OnDateSetListener()
                final DatePickerDialog datePickerDialog = new DatePickerDialog(Insert_bsl.this.getActivity() ,new DatePickerDialog.OnDateSetListener()
                {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day)
                    {
                        month+=1;
                        date.setText(day + "/" + month + "/" + year);
                    }}, year, month, dayOfMonth);

                /*datePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "CANCEL", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        if (which == DialogInterface.BUTTON_NEGATIVE)
                        {
                            if (date.getText().toString().equals(""))
                            {
                                date.setText("");
                                datePickerDialog.dismiss();
                            }
                        }
                    }
                });*/

                datePickerDialog.show();
            }
        });

        //when user click the time text view, it will show the time picker dialog
        final TextView time = getActivity().findViewById(R.id.fragment_insert_bsl_time);
        time.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                final Calendar calendar = Calendar.getInstance();
                int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                final boolean is24HourView = false;

                TimePickerDialog timePickerDialog = new TimePickerDialog(Insert_bsl.this.getActivity() ,new TimePickerDialog.OnTimeSetListener()
                //TimePickerDialog timePickerDialog = new TimePickerDialog(Insert_bsl.this.getActivity(),AlertDialog.THEME_DEVICE_DEFAULT_LIGHT ,new TimePickerDialog.OnTimeSetListener()
                //TimePickerDialog timePickerDialog = new TimePickerDialog(Insert_bsl.this.getActivity(), R.style.DialogTheme ,new TimePickerDialog.OnTimeSetListener()
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

                        time.setText(hourOfDay + " : " + mm_precede + minute + AM_PM);

                    }
                }, hourOfDay, minute, is24HourView);

                timePickerDialog.show();
            }
        });

        //button reset
        /*final Button reset = getActivity().findViewById(R.id.fragment_insert_bsl_reset_btn);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bsl.setText(null);
                date.setText(null);
                time.setText(null);
                condition.setSelection(0);
                mood.setSelection(0);
                note.setText(null);
            }
        });*/

        //button add
        final Button add = getActivity().findViewById(R.id.fragment_insert_bsl_add_btn);
        add.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    String currentUser_S = String.valueOf(currentUser.getText().toString());
                    double bsl_D = new Double (bsl.getText().toString());
                    String bsl_D_S = String.valueOf(bsl_D);
                    String condition_S = String.valueOf(condition.getSelectedItem().toString());
                    String date_S = String.valueOf(date.getText().toString());
                    String time_S = String.valueOf(time.getText().toString());
                    TextView type = getActivity().findViewById(R.id.fragment_insert_bsl_type);

                    if (currentUser_S.equals(""))
                    {
                        Toast.makeText(getActivity(), "Please login to add data", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        if (date_S.equals("") || time_S.equals("") || bsl_D == zero || bsl_D_S.equals(""))
                        {
                            Toast.makeText(getActivity(), "Incomplete Data", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            if (condition_S.equals("Fasting"))
                            {
                                if (bsl_D < 3.0) //severe
                                {
                                    severe();
                                    type.setText("Severe (Low)");
                                }
                                else if (bsl_D >= 3.0 && bsl_D <= 3.9) //mild
                                {
                                    mild();
                                    type.setText("Mild (Low)");
                                }
                                else if (bsl_D >= 4.0 && bsl_D <= 5.6) //normal  //must be 4.4
                                {
                                    normal();
                                    type.setText("Normal");
                                }
                                else if (bsl_D >= 5.7 && bsl_D <= 6.9) //prediabetic
                                {
                                    prediabetic();
                                    type.setText("Slightly High");
                                    //type.setText("Prediabetic (Slightly High)");
                                }
                                else if (bsl_D >= 7.0) //diabetic
                                {
                                    diabetic();
                                    type.setText("High");
                                    //type.setText("Diabetic (High)");
                                }
                            }

                            else if (condition_S.equals("2 hour after meal"))
                            {
                                if (bsl_D < 3.0) //severe
                                {
                                    severe();
                                    type.setText("Severe (Low)");
                                }
                                else if (bsl_D >= 3.0 && bsl_D <= 3.9) //mild
                                {
                                    mild();
                                    type.setText("Mild (Low)");
                                }
                                else if (bsl_D >= 4.0 && bsl_D <= 7.0) //normal  //must be 4.4
                                {
                                    normal();
                                    type.setText("Normal");
                                }
                                else if (bsl_D >= 7.1 && bsl_D <= 10.9) //prediabetic
                                {
                                    prediabetic();
                                    type.setText("Slightly High");
                                    //type.setText("Prediabetic (Slightly High)");
                                }
                                else if (bsl_D >= 11.0) //diabetic
                                {
                                    diabetic();
                                    type.setText("High");
                                    //type.setText("Diabetic (High)");
                                }
                            }

                            savedata();
                        }
                    }
                }
                catch (NumberFormatException e)
                {
                    Toast.makeText(getActivity(), "BSL must be in correct value", Toast.LENGTH_SHORT).show();
                }
                catch (InputMismatchException e)
                {
                    Toast.makeText(getActivity(), "Insert correct value", Toast.LENGTH_SHORT).show();
                }
            }
        });


        //make reset button for bsl edittext only when there is text inside
        bsl.setText(value);
        bsl.setCompoundDrawables(null, null, value.equals("") ? null : x, null);
        bsl.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (bsl.getCompoundDrawables()[2] == null)
                {
                    return false;
                }
                if (event.getAction() != MotionEvent.ACTION_UP)
                {
                    return false;
                }
                if (event.getX() > bsl.getWidth() - bsl.getPaddingRight() - x.getIntrinsicWidth())
                {
                    bsl.setText("");
                    bsl.setCompoundDrawables(null, null, null, null);
                }
                return false;
            }
        });
        bsl.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                bsl.setCompoundDrawables(null, null, bsl.getText().toString().equals("") ? null : x, null);
            }

            @Override
            public void afterTextChanged(Editable arg0) { }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
        });

        //make reset button for note edittext only when there is text inside
        note.setText(value);
        note.setCompoundDrawables(null, null, value.equals("") ? null : x, null);
        note.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (note.getCompoundDrawables()[2] == null)
                {
                    return false;
                }
                if (event.getAction() != MotionEvent.ACTION_UP)
                {
                    return false;
                }
                if (event.getX() > note.getWidth() - note.getPaddingRight() - x.getIntrinsicWidth())
                {
                    note.setText("");
                    note.setCompoundDrawables(null, null, null, null);
                }
                return false;
            }
        });
        note.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                note.setCompoundDrawables(null, null, note.getText().toString().equals("") ? null : x, null);
            }

            @Override
            public void afterTextChanged(Editable arg0) { }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
        });
    }




    //CUSTOM METHOD

    //alert dialog
    private void mild()
    {
        String title = "Caution !";
        String ab = "Your blood sugar level is too mild (Low), take appropriate action to increase your blood sugar level";
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this.getActivity());
        alertDialogBuilder
                .setTitle(Html.fromHtml("<font color='#FB4748'>"+title+"</font>"))
                .setMessage(ab)
                .setCancelable(false)
                .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        alertDialogBuilder.setCancelable(true);
                    }
                });

        alertDialogBuilder.show();

        //for android kitkat/4 and below
        //show alert dialog box with changing title line separator colour
        /*Dialog d = alertDialogBuilder.show();
        int dividerId = d.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
        View divider = d.findViewById(dividerId);
        divider.setBackgroundColor(getResources().getColor(R.color.redLight));*/
    }

    //alert dialog
    private void severe()
    {
        String title = "Warning !";
        String ab = "Your blood sugar level is too severe (Low), send to hospital as soon as possible";
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this.getActivity());
        alertDialogBuilder
                .setTitle(Html.fromHtml("<font color='#FB4748'>"+title+"</font>"))
                .setMessage(ab)
                .setCancelable(false)
                .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        alertDialogBuilder.setCancelable(true);
                    }
                });

        alertDialogBuilder.show();

        //for android kitkat/4 and below
        //show alert dialog box with changing title line separator colour
        /*Dialog d = alertDialogBuilder.show();
        int dividerId = d.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
        View divider = d.findViewById(dividerId);
        divider.setBackgroundColor(getResources().getColor(R.color.redLight));*/
    }

    //alert dialog
    private void normal()
    {
        String title = "Congrats !";
        String ab = "Your blood sugar level is normal";
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this.getActivity());
        alertDialogBuilder
                .setTitle(Html.fromHtml("<font color='#FB4748'>"+title+"</font>"))
                .setMessage(ab)
                .setCancelable(false)
                .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        alertDialogBuilder.setCancelable(true);
                    }
                });

        alertDialogBuilder.show();

        //for android kitkat/4 and below
        //show alert dialog box with changing title line separator colour
        /*Dialog d = alertDialogBuilder.show();
        int dividerId = d.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
        View divider = d.findViewById(dividerId);
        divider.setBackgroundColor(getResources().getColor(R.color.redLight));*/
    }

    //alert dialog
    private void prediabetic()
    {
        String title = "Caution !";
        String ab = "Your blood sugar is slightly high, take appropriate action to decrease your blood sugar level";
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this.getActivity());
        alertDialogBuilder
                .setTitle(Html.fromHtml("<font color='#FB4748'>"+title+"</font>"))
                .setMessage(ab)
                .setCancelable(false)
                .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        alertDialogBuilder.setCancelable(true);
                    }
                });

        alertDialogBuilder.show();

        //for android kitkat/4 and below
        //show alert dialog box with changing title line separator colour
        /*Dialog d = alertDialogBuilder.show();
        int dividerId = d.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
        View divider = d.findViewById(dividerId);
        divider.setBackgroundColor(getResources().getColor(R.color.redLight));*/
    }

    //alert dialog
    private void diabetic()
    {
        String title = "Warning !";
        String ab = "Your blood sugar is too high, take appropriate action to decrease your blood sugar level";
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this.getActivity());
        alertDialogBuilder
                .setTitle(Html.fromHtml("<font color='#FB4748'>"+title+"</font>"))
                .setMessage(ab)
                .setCancelable(false)
                .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        alertDialogBuilder.setCancelable(true);
                    }
                });

        alertDialogBuilder.show();

        //for android kitkat/4 and below
        //show alert dialog box with changing title line separator colour
        /*Dialog d = alertDialogBuilder.show();
        int dividerId = d.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
        View divider = d.findViewById(dividerId);
        divider.setBackgroundColor(getResources().getColor(R.color.redLight));*/
    }




    //SQLITE DATABASE

    //create or open a database using sqllite
    public void createdb()
    {
        data_db = this.getActivity().openOrCreateDatabase("data_db",MODE_PRIVATE,null);
        String sqlcreate = "CREATE TABLE IF NOT EXISTS DATA(DATAID INTEGER PRIMARY KEY AUTOINCREMENT, CURRENTUSER VARCHAR, BSL VARCHAR, CONDITION VARCHAR, MOOD VARCHAR, DATE VARCHAR, TIME VARCHAR, NOTE VARCHAR, TYPE VARCHAR);";
        data_db.execSQL(sqlcreate);
    }

    //save Data into sqlite database
    private void savedata()
    {
        final EditText bsl = getActivity().findViewById(R.id.fragment_insert_bsl_bsl);
        final Spinner condition = getActivity().findViewById(R.id.fragment_insert_bsl_condition_spinner);
        final Spinner mood = getActivity().findViewById(R.id.fragment_insert_bsl_mood_spinner);
        final EditText date = getActivity().findViewById(R.id.fragment_insert_bsl_date);
        final EditText time = getActivity().findViewById(R.id.fragment_insert_bsl_time);
        final EditText note = getActivity().findViewById(R.id.fragment_insert_bsl_note);
        final TextView type = getActivity().findViewById(R.id.fragment_insert_bsl_type);
        final TextView currentUser = getActivity().findViewById(R.id.fragment_insert_bsl_currentUser);

        double bsl1 = Double.parseDouble(bsl.getText().toString());
        String condition1 = condition.getSelectedItem().toString();
        String mood1 = mood.getSelectedItem().toString();
        String date1 = date.getText().toString();
        String time1 = time.getText().toString();
        String note1 = note.getText().toString();
        String type1 = type.getText().toString();
        String currentUser1 = currentUser.getText().toString();

        try
        {
            String sqlsave = "INSERT INTO DATA(CURRENTUSER,BSL,CONDITION,MOOD,DATE,TIME,NOTE,TYPE)VALUES('"+currentUser1+"','"+bsl1+"','"+condition1+"','"+mood1+"','"+date1+"','"+time1+"','"+note1+"','"+type1+"');";
            data_db.execSQL(sqlsave);
            Toast.makeText(this.getActivity(), "Success add BSL", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e)
        {
            Toast.makeText(this.getActivity(), "Error add BSL", Toast.LENGTH_SHORT).show();
            Log.e("DB",e.toString());
        }
    }




    //REVISION CODE

    /*public void Data()
    {
        String bsl = getActivity().findViewById(R.id.fragment_insert_bsl_bsl).toString();
        String condition = getActivity().findViewById(R.id.fragment_insert_bsl_condition_spinner).toString();
        String mood = getActivity().findViewById(R.id.fragment_insert_bsl_mood_spinner).toString();
        String date = getActivity().findViewById(R.id.fragment_insert_bsl_date).toString();
        String time = getActivity().findViewById(R.id.fragment_insert_bsl_time).toString();
        String note = getActivity().findViewById(R.id.fragment_insert_bsl_note).toString();
    }*/
}
