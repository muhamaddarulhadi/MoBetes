package com.hadi.mobetes.Fragment;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDelegate;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.hadi.mobetes.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.InputMismatchException;

import static android.content.Context.MODE_PRIVATE;

public class Data extends Fragment
{
    String value = "";
    //BSL_data bsl_data [];
    SQLiteDatabase data_db;
    private ArrayList<HashMap<String, String>> listdata;
    //ArrayAdapter<String> adapter;
    //ListView lvdata;
    SwipeMenuListView listView;
    //BSL_data all_data[];
    public static final String USERLOGIN = "USERLOGIN";
    Boolean Registered;

    public Data() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        //Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_data, container, false);  //not use because used another layout
        return inflater.inflate(R.layout.fragment_data_2, container, false);
    }

    //what the application do when the fragment is start
    @Override
    public void onStart()
    {
        super.onStart();
        createdb();

        final TextView currentUser = getActivity().findViewById(R.id.fragment_data_2_currentUser);

        //get the username from shared preference for to set username on current user
        SharedPreferences userLogin = getActivity().getSharedPreferences(USERLOGIN,MODE_PRIVATE);
        Registered = userLogin.getBoolean("Registered", false);
        if (Registered == true)
        {
            currentUser.setText(userLogin.getString("username",""));
            loaddata();
        }
        else
        {
            listView = getActivity().findViewById(R.id.listView);
            listView.setEmptyView(getActivity().findViewById(R.id.empty_list_item));
        }
    }

    //what the application do when the fragment is use
    @Override
    public void onActivityCreated (Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        listdata = new ArrayList<>();
        //lvdata = getActivity().findViewById(R.id.fragment_data_listview);  //NOT USE
        //lvdata.setEmptyView(getActivity().findViewById(R.id.empty_list_item));  //NOT USE

        listView = getActivity().findViewById(R.id.listView);
        listView.setEmptyView(getActivity().findViewById(R.id.empty_list_item));

        //when the list is click, user can only view
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                try
                {
                    String data = listView.getItemAtPosition(position).toString(); //get Data position on the list view
                    //Toast.makeText(Data.this.getActivity(), ""+data, Toast.LENGTH_LONG).show();  //test to view the Data array
                    String[] dataarray = data.split("\\s*,\\s*"); //split the Data to be multiline

                    //for android lolipop/5 and above
                    String data_id_temp = dataarray[0].replace("dataid=", "");
                    String data_id = data_id_temp.replace("{", "");
                    String data_time = dataarray[1].replace("time=", "");
                    String data_date = dataarray[2].replace("date=", "");
                    String data_currentUser = dataarray[3].replace("currentuser=", "");
                    String data_condition = dataarray[4].replace("condition=", "");
                    String data_mood = dataarray[5].replace("mood=", "");
                    String data_note = dataarray[6].replace("note=", "");
                    String data_type = dataarray[7].replace("type=", "");
                    String data_bsl_temp = dataarray[8].replace("bsl=", "");
                    String data_bsl = data_bsl_temp.replace("}", "");


                    //for android kitkat/4 and below
                    //String data_time_temp = dataarray[0].replace("time=", ""); //remove the word in target
                    //String data_time = data_time_temp.replace("{", "");
                    //String data_bsl = dataarray[1].replace("bsl=", "");
                    //String data_condition = dataarray[2].replace("condition=", "");
                    //String data_currentUser = dataarray[3].replace("currentuser=", "");
                    //String data_mood = dataarray[4].replace("mood=", "");
                    //String data_type = dataarray[5].replace("type=", "");
                    //String data_date = dataarray[6].replace("date=", "");
                    //String data_id = dataarray[7].replace("dataid=", "");
                    //String data_note_temp = dataarray[8].replace("note=", "");
                    //String data_note = data_note_temp.replace("}", "");

                    loadDialogOpenData(data_id,data_bsl,data_condition,data_currentUser,data_mood,data_date,data_time,data_note,data_type);
                }
                catch (NullPointerException e)
                {
                    Toast.makeText(Data.this.getActivity(), "No Data detected", Toast.LENGTH_LONG).show();
                }
            }
        });


        //method for the swipe layout
        SwipeMenuCreator creator = new SwipeMenuCreator()
        {
            @Override
            public void create(SwipeMenu menu)
            {
                //when swipe, show the open button
                SwipeMenuItem openItem = new SwipeMenuItem(getActivity().getApplicationContext());
                openItem.setBackground(new ColorDrawable(Color.rgb(96, 96, 96)));
                openItem.setWidth(170);
                openItem.setTitle("Update");
                openItem.setTitleSize(15);
                openItem.setTitleColor(Color.WHITE);
                openItem.setIcon(R.drawable.ic_edit_black_30dp);
                menu.addMenuItem(openItem);

                //when swipe, show the delete button
                SwipeMenuItem deleteItem = new SwipeMenuItem(getActivity().getApplicationContext());
                deleteItem.setBackground(new ColorDrawable(Color.rgb(251,71,72)));
                deleteItem.setWidth(170);
                deleteItem.setTitle("Delete");
                deleteItem.setTitleSize(15);
                deleteItem.setTitleColor(Color.WHITE);
                deleteItem.setIcon(R.drawable.ic_delete_black_30dp);
                menu.addMenuItem(deleteItem);
            }
        };

        //set creator
        listView.setMenuCreator(creator);

        //set each of the button job to do from creator
        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener()
        {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index)
            {
                switch (index)
                {
                    case 0:

                        //when click update button is click, user can update the Data
                        String data =  listView.getItemAtPosition(position).toString();
                        //Toast.makeText(Data.this.getActivity(), ""+Data, Toast.LENGTH_LONG).show();  //test to view the Data array
                        String[] dataarray = data.split("\\s*,\\s*");

                        //for android lolipop/5 and above
                        String data_id_temp = dataarray[0].replace("dataid=", "");
                        String data_id = data_id_temp.replace("{", "");
                        String data_time = dataarray[1].replace("time=", "");
                        String data_date = dataarray[2].replace("date=", "");
                        String data_currentUser = dataarray[3].replace("currentuser=", "");
                        String data_condition = dataarray[4].replace("condition=", "");
                        String data_mood = dataarray[5].replace("mood=", "");
                        String data_note = dataarray[6].replace("note=", "");
                        String data_type = dataarray[7].replace("type=", "");
                        String data_bsl_temp = dataarray[8].replace("bsl=", "");
                        String data_bsl = data_bsl_temp.replace("}", "");


                        //for android kitkat/4 and below
                        //String data_time_temp = dataarray[0].replace("time=", ""); //remove the word in target
                        //String data_time = data_time_temp.replace("{", "");
                        //String data_bsl = dataarray[1].replace("bsl=", "");
                        //String data_condition = dataarray[2].replace("condition=", "");
                        //String data_currentUser = dataarray[3].replace("currentuser=", "");
                        //String data_mood = dataarray[4].replace("mood=", "");
                        //String data_type = dataarray[5].replace("type=", "");
                        //String data_date = dataarray[6].replace("date=", "");
                        //String data_id = dataarray[7].replace("dataid=", "");
                        //String data_note_temp = dataarray[8].replace("note=", "");
                        //String data_note = data_note_temp.replace("}", "");

                        //Toast.makeText(Data.this.getActivity(), ""+data_note, Toast.LENGTH_LONG).show();  //test to view Data

                        loadDialogUpdateData(data_id,data_bsl,data_condition,data_currentUser,data_mood,data_date,data_time,data_note,data_type);

                        break;

                    case 1:

                        //when delete button is delete, it will delete the Data
                        String data1 =  listView.getItemAtPosition(position).toString();
                        //Toast.makeText(Data.this.getActivity(), ""+data1, Toast.LENGTH_LONG).show();  //test to view the Data array
                        String[] dataarray1 = data1.split("\\s*,\\s*");  //split each item inside Data array by row


                        //for android kitkat and below
                        /*String dataid1 = dataarray1[7].replace("dataid=","");  //set the location of the row
                        //Toast.makeText(Data.this.getActivity(), "Data ID:"+dataid1, Toast.LENGTH_LONG).show();  //test to view the dataid
                        delete(dataid1);  //bottom sheet delete dialog
                        //deletedataDialog(dataid1);  //NOT USE because this is the default dialog*/


                        //for android lolipop and above
                        String data_id_temp1 = dataarray1[0].replace("dataid=", "");
                        String data_id1 = data_id_temp1.replace("{", "");
                        delete(data_id1);

                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });
    }




    //SQLITE DATABASE

    //create or open the database using sqllite
    public void createdb()
    {
        data_db = this.getActivity().openOrCreateDatabase("data_db",MODE_PRIVATE,null);
        String sqlcreate = "CREATE TABLE IF NOT EXISTS DATA(DATAID INTEGER PRIMARY KEY AUTOINCREMENT, CURRENTUSER VARCHAR, BSL VARCHAR, CONDITION VARCHAR, MOOD VARCHAR, DATE VARCHAR, TIME VARCHAR, NOTE VARCHAR, TYPE VARCHAR);";
        data_db.execSQL(sqlcreate);
    }

    //load Data from sqllite database
    public void loaddata()
    {
        final TextView currentUser = getActivity().findViewById(R.id.fragment_data_2_currentUser);
        String currentUser_S = String.valueOf(currentUser.getText().toString());
        String sqlsearch = "SELECT DATAID,CURRENTUSER, BSL, CONDITION, MOOD, DATE, TIME, NOTE, TYPE FROM DATA WHERE CURRENTUSER = '"+currentUser_S+"'";
        Cursor c = data_db.rawQuery(sqlsearch,null);
        listdata.clear();
        if (c.getCount() > 0)
        {
            c.moveToFirst();
            for (int i=0;i<c.getCount();i++)
            {
                String dataid = c.getString(c.getColumnIndex("DATAID"));
                String currentuser = c.getString(c.getColumnIndex("CURRENTUSER"));
                String bsl = c.getString(c.getColumnIndex("BSL"));
                String condition = c.getString(c.getColumnIndex("CONDITION"));
                String mood = c.getString(c.getColumnIndex("MOOD"));
                String date = c.getString(c.getColumnIndex("DATE"));
                String time = c.getString(c.getColumnIndex("TIME"));
                String note = c.getString(c.getColumnIndex("NOTE"));
                String type = c.getString(c.getColumnIndex("TYPE"));
                HashMap<String, String> datalist = new HashMap<>();
                datalist.put("dataid",dataid);
                datalist.put("currentuser",currentuser);
                datalist.put("bsl",bsl);
                datalist.put("condition",condition);
                datalist.put("mood",mood);
                datalist.put("date",date);
                datalist.put("time",time);
                datalist.put("note",note);
                datalist.put("type",type);
                listdata.add(datalist);
                c.moveToNext();
            }
            ListAdapter adapter = new SimpleAdapter(
                    Data.this.getActivity(), listdata,
                    //R.layout.custom_list_data, new  //NOT USE
                    R.layout.custom_list_data_2, new
                    String[]{"dataid","currentuser","bsl","condition","mood","date","time","note","type"}, new
                    int[]{R.id.custom_list_data_id,R.id.custom_list_data_currentUser,R.id.custom_list_data_bsl,R.id.custom_list_data_condition,R.id.custom_list_data_mood,
                    R.id.custom_list_data_date,R.id.custom_list_data_time,R.id.custom_list_data_note,R.id.custom_list_data_type});
            //lvdata.setAdapter(adapter);  //NOT USE
            listView.setAdapter(adapter);
        }
    }




    //CUSTOM METHOD

    //confirmation Bottom dialog to delete data
    private void delete(final String dataid)
    {
        final BottomSheetDialog bottomSheetDelete = new BottomSheetDialog(this.getActivity());
        bottomSheetDelete.setContentView(R.layout.custom_bottom_dialog_delete_data);
        bottomSheetDelete.show();

        Button yes = bottomSheetDelete.findViewById(R.id.custom_bottom_dialog_delete_data_yes_btn);
        Button no = bottomSheetDelete.findViewById(R.id.custom_bottom_dialog_delete_data_no_btn);

        yes.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String sqldelete = "DELETE FROM DATA WHERE DATAID = '"+dataid+"'";
                data_db.execSQL(sqldelete);
                Toast.makeText(getActivity(), "Success delete Data", Toast.LENGTH_SHORT).show();
                loaddata();
                listView.setEmptyView(getActivity().findViewById(R.id.empty_list_item));
                bottomSheetDelete.cancel();
            }
        });

        no.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                bottomSheetDelete.cancel();
            }
        });
    }

    //delete row on listview  //NOT USE
    /*private void deletedataDialog(final String dataid)
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this.getActivity());
        alertDialogBuilder
                .setMessage("Are you sure you want to delete?")
                .setCancelable(false)
                .setPositiveButton("YES",new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog,int id)
                    {
                        String sqldelete = "DELETE FROM DATA WHERE DATAID = '"+dataid+"'";
                        data_db.execSQL(sqldelete);
                        Toast.makeText(getActivity(), "Success delete Data", Toast.LENGTH_SHORT).show();
                        loaddata();
                        listView.setEmptyView(getActivity().findViewById(R.id.empty_list_item));
                    }
                })
                .setNegativeButton("N0",new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog,int id)
                    {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }*/

    //load dialog to update Data
    private void loadDialogUpdateData(final String dataid, final String bsl, final String condition,final String currentUser, final String mood, final String date, final String time, final String note, final String type)
    {
        final Dialog aboutDialog = new Dialog(this.getActivity(), R.style.dialog);
        aboutDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        aboutDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        aboutDialog.getWindow().setGravity(0);
        aboutDialog.setContentView(R.layout.dialog_update_data);

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

        //for custom dialog box, must put aboutDialog.findViewById(), not getActivity().findViewById()
        final EditText time_Dialog = aboutDialog.findViewById(R.id.dialog_update_data_time);
        final EditText bsl_Dialog = aboutDialog.findViewById(R.id.dialog_update_data_bsl);
        final Spinner condition_Dialog = aboutDialog.findViewById(R.id.dialog_update_data_condition_spinner);
        final TextView currentUser_Dialog = aboutDialog.findViewById(R.id.dialog_update_data_currentUser);
        final Spinner mood_Dialog = aboutDialog.findViewById(R.id.dialog_update_data_mood_spinner);
        final TextView type_Dialog = aboutDialog.findViewById(R.id.dialog_update_data_type);
        final EditText date_Dialog = aboutDialog.findViewById(R.id.dialog_update_data_date);
        final TextView dataid_Dialog = aboutDialog.findViewById(R.id.dialog_update_data_dataid);
        final EditText note_Dialog = aboutDialog.findViewById(R.id.dialog_update_data_note);
        Button update_Dialog = aboutDialog.findViewById(R.id.dialog_update_data_update_btn);
        Button cancel_Dialog = aboutDialog.findViewById(R.id.dialog_update_data_cancel_btn);

        //set Data inside spinner to use the custom layout
        ArrayAdapter adapter1 = ArrayAdapter.createFromResource(this.getActivity(), R.array.condition, R.layout.spinner_item);
        condition_Dialog.setAdapter(adapter1);
        ArrayAdapter adapter2 = ArrayAdapter.createFromResource(this.getActivity(), R.array.mood, R.layout.spinner_item);
        mood_Dialog.setAdapter(adapter2);

        //set Data get from touch list view
        dataid_Dialog.setText(dataid);
        bsl_Dialog.setText(bsl);
        date_Dialog.setText(date);
        currentUser_Dialog.setText(currentUser);
        time_Dialog.setText(time);
        note_Dialog.setText(note);
        type_Dialog.setText(type);
        for (int i=0; i< condition_Dialog.getCount();i++)
        {
            if(condition_Dialog.getItemAtPosition(i).equals(condition))
            {
                condition_Dialog.setSelection(i);
            }
        }
        for (int u=0; u< mood_Dialog.getCount();u++)
        {
            if(mood_Dialog.getItemAtPosition(u).equals(mood))
            {
                mood_Dialog.setSelection(u);
            }
        }

        //when click date, will popup datepicker dialog
        date_Dialog.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(Data.this.getActivity() ,new DatePickerDialog.OnDateSetListener()
                {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day)
                    {
                        month+=1;
                        date_Dialog.setText(day + "/" + month + "/" + year);
                    }}, year, month, dayOfMonth);

                datePickerDialog.show();
            }
        });

        //when click time, will popup timepicker dialog
        time_Dialog.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                final Calendar calendar = Calendar.getInstance();
                int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                final boolean is24HourView = false;

                TimePickerDialog timePickerDialog = new TimePickerDialog(Data.this.getActivity(),new TimePickerDialog.OnTimeSetListener()
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

                        time_Dialog.setText(hourOfDay + " : " + mm_precede + minute + AM_PM);

                    }
                }, hourOfDay, minute, is24HourView);

                timePickerDialog.show();
            }
        });

        //when click cancel button, it will close the custom dialog box
        cancel_Dialog.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                aboutDialog.cancel();
            }
        });

        //when click update button, it will update the Data
        update_Dialog.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    String currentUser_S = String.valueOf(currentUser_Dialog.getText().toString());
                    String dataid_S = String.valueOf(dataid_Dialog.getText().toString());
                    double bsl_D = new Double (bsl_Dialog.getText().toString());
                    String bsl_D_S = String.valueOf(bsl_D);
                    String condition_S = String.valueOf(condition_Dialog.getSelectedItem().toString());
                    String mood_S = String.valueOf(mood_Dialog.getSelectedItem().toString());
                    String date_S = String.valueOf(date_Dialog.getText().toString());
                    String time_S = String.valueOf(time_Dialog.getText().toString());
                    String note_S = String.valueOf(note_Dialog.getText().toString());

                    if (date_S.equals("") || time_S.equals("") || bsl_D == 0 || bsl_D_S.equals("") || bsl_D == 0.0)
                    {
                        Toast.makeText(getActivity(), "Incomplete Data", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        if (condition_S.equals("Fasting"))
                        {
                            if (bsl_D < 3.0) //severe
                            {
                                type_Dialog.setText("Severe (Low)");
                            }
                            else if (bsl_D >= 3.0 && bsl_D <= 3.9) //mild
                            {
                                type_Dialog.setText("Mild (Low)");
                            }
                            else if (bsl_D >= 4.4 && bsl_D <= 5.6) //normal
                            {
                                type_Dialog.setText("Normal");
                            }
                            else if (bsl_D >= 5.7 && bsl_D <= 6.9) //prediabetic
                            {
                                type_Dialog.setText("Prediabetic (Slightly High)");
                            }
                            else if (bsl_D >= 7.0) //diabetic
                            {
                                type_Dialog.setText("Diabetic (High)");
                            }
                        }

                        else if (condition_S.equals("2 hour after meal"))
                        {
                            if (bsl_D < 3.0) //severe
                            {
                                type_Dialog.setText("Severe (Low)");
                            }
                            else if (bsl_D >= 3.0 && bsl_D <= 3.9) //mild
                            {
                                type_Dialog.setText("Mild (Low)");
                            }
                            else if (bsl_D >= 4.4 && bsl_D <= 7.0) //normal
                            {
                                type_Dialog.setText("Normal");
                            }
                            else if (bsl_D >= 7.1 && bsl_D <= 10.9) //prediabetic
                            {
                                type_Dialog.setText("Prediabetic (Slightly High)");
                            }
                            else if (bsl_D >= 11.0) //diabetic
                            {
                                type_Dialog.setText("Diabetic (High)");
                            }
                        }

                        String type_S = String.valueOf(type_Dialog.getText().toString());

                        //loadDialogUpdateDataConfirm(dataid_S,bsl_D_S,condition_S,mood_S,date_S,time_S,note_S,type_S);  //NOT USE, default dialog
                        update(dataid_S,bsl_D_S,condition_S,currentUser_S,mood_S,date_S,time_S,note_S,type_S);  //bottom sheet update dialog
                    }
                }
                catch (NumberFormatException e)
                {
                    Toast.makeText(getActivity(), "BSL must be in correct value", Toast.LENGTH_SHORT).show();
                    return;
                }
                catch (InputMismatchException e)
                {
                    Toast.makeText(getActivity(), "Insert correct value", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

        //make reset button for bsl_Dialog edittext only when there is text inside
        bsl_Dialog.setCompoundDrawables(null, null, value.equals(bsl) ? null : x, null);
        bsl_Dialog.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (bsl_Dialog.getCompoundDrawables()[2] == null)
                {
                    return false;
                }
                if (event.getAction() != MotionEvent.ACTION_UP)
                {
                    return false;
                }
                if (event.getX() > bsl_Dialog.getWidth() - bsl_Dialog.getPaddingRight() - x.getIntrinsicWidth())
                {
                    bsl_Dialog.setText("");
                    bsl_Dialog.setCompoundDrawables(null, null, null, null);
                }
                return false;
            }
        });
        bsl_Dialog.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                bsl_Dialog.setCompoundDrawables(null, null, bsl_Dialog.getText().toString().equals("") ? null : x, null);
            }

            @Override
            public void afterTextChanged(Editable arg0) { }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
        });

        //make reset button for note_Dialog edittext only when there is text inside
        note_Dialog.setCompoundDrawables(null, null, value.equals(note) ? null : x, null);
        note_Dialog.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (note_Dialog.getCompoundDrawables()[2] == null)
                {
                    return false;
                }
                if (event.getAction() != MotionEvent.ACTION_UP)
                {
                    return false;
                }
                if (event.getX() > note_Dialog.getWidth() - note_Dialog.getPaddingRight() - x.getIntrinsicWidth())
                {
                    note_Dialog.setText("");
                    note_Dialog.setCompoundDrawables(null, null, null, null);
                }
                return false;
            }
        });
        note_Dialog.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                note_Dialog.setCompoundDrawables(null, null, note_Dialog.getText().toString().equals("") ? null : x, null);
            }

            @Override
            public void afterTextChanged(Editable arg0) { }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
        });

    }

    //load dialog to open Data just for view
    private void loadDialogOpenData(final String dataid, final String bsl, final String condition,final String currentUser, final String mood, final String date, final String time, final String note, final String type)
    {
        final Dialog aboutDialog = new Dialog(this.getActivity(), R.style.dialog);
        aboutDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        aboutDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        aboutDialog.getWindow().setGravity(0);
        aboutDialog.setContentView(R.layout.dialog_open_data);

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

        //for custom dialog box, must put aboutDialog.findViewById(), not getActivity().findViewById()
        final TextView time_Dialog = aboutDialog.findViewById(R.id.dialog_open_data_time);
        final TextView bsl_Dialog = aboutDialog.findViewById(R.id.dialog_open_data_bsl);
        final TextView condition_Dialog = aboutDialog.findViewById(R.id.dialog_open_data_condition);
        final TextView currentUser_Dialog = aboutDialog.findViewById(R.id.dialog_open_data_currentUser);
        final TextView mood_Dialog = aboutDialog.findViewById(R.id.dialog_open_data_mood);
        final TextView type_Dialog = aboutDialog.findViewById(R.id.dialog_open_data_type);
        final TextView date_Dialog = aboutDialog.findViewById(R.id.dialog_open_data_date);
        final TextView dataid_Dialog = aboutDialog.findViewById(R.id.dialog_open_data_dataid);
        final TextView note_Dialog = aboutDialog.findViewById(R.id.dialog_open_data_note);
        Button done_dialog = aboutDialog.findViewById(R.id.dialog_open_data_done_btn);

        dataid_Dialog.setText(dataid);
        bsl_Dialog.setText(bsl);
        date_Dialog.setText(date);
        time_Dialog.setText(time);
        note_Dialog.setText(note);
        type_Dialog.setText(type);
        currentUser_Dialog.setText(currentUser);
        condition_Dialog.setText(condition);
        mood_Dialog.setText(mood);

        //close custom dialog box when click done button
        done_dialog.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                aboutDialog.cancel();
            }
        });
    }

    //confirmation for to update the Data //NOT USE
    /*private void loadDialogUpdateDataConfirm(final String dataid, final String bsl, final String condition, final String mood, final String date, final String time, final String note, final String type)
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this.getActivity());
        alertDialogBuilder
                .setMessage("Are you sure you want to update?")
                .setCancelable(false)
                .setPositiveButton("YES",new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog,int id)
                    {
                        String sqlupdate = "UPDATE DATA SET BSL = '"+bsl+"', CONDITION = '"+condition+"', MOOD = '"+mood+"', DATE = '"+date+"', TIME = '"+time+"', NOTE = '"+note+"', TYPE = '"+type+"' WHERE DATAID = '"+dataid+"'";
                        data_db.execSQL(sqlupdate);
                        loaddata();
                        Toast.makeText(getActivity(), "Success update Data", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("NO",new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog,int id)
                    {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }*/

    //confirmation Bottom dialog to update data
    private void update(final String dataid, final String bsl, final String condition,final String currentUser, final String mood, final String date, final String time, final String note, final String type)
    {
        final BottomSheetDialog bottomSheetUpdate = new BottomSheetDialog(this.getActivity());
        //View sheetView = getActivity().getLayoutInflater().inflate(R.layout.custom_bottom_dialog_update_data, null);
        bottomSheetUpdate.setContentView(R.layout.custom_bottom_dialog_update_data);
        bottomSheetUpdate.show();

        Button yes = bottomSheetUpdate.findViewById(R.id.custom_bottom_dialog_update_data_yes_btn);
        Button no = bottomSheetUpdate.findViewById(R.id.custom_bottom_dialog_update_data_no_btn);

        yes.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String sqlupdate = "UPDATE DATA SET BSL = '"+bsl+"', CONDITION = '"+condition+"', MOOD = '"+mood+"', DATE = '"+date+"', TIME = '"+time+"', NOTE = '"+note+"', TYPE = '"+type+"' WHERE DATAID = '"+dataid+"' AND CURRENTUSER = '"+currentUser+"'";
                data_db.execSQL(sqlupdate);
                loaddata();
                Toast.makeText(getActivity(), "Success update Data", Toast.LENGTH_SHORT).show();
                bottomSheetUpdate.cancel();
            }
        });

        no.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                bottomSheetUpdate.cancel();
            }
        });
    }




    //REVISION CODE

    /*
    kedudukan dataaray

    0 = time
    1 = bsl
    2 = condition
    3 = mood
    4 = type
    5 = date
    6 = dataid
    7 = note
    */
}
