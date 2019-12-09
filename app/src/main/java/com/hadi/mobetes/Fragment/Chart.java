package com.hadi.mobetes.Fragment;


import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDelegate;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.hadi.mobetes.Model.BSL_data;
import com.hadi.mobetes.R;
import com.hadi.mobetes.SharedPreferences.InitApplication;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import lecho.lib.hellocharts.formatter.AxisValueFormatter;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

import static android.content.Context.MODE_PRIVATE;

public class Chart extends Fragment
{
    SQLiteDatabase data_db;
    private ArrayList<HashMap<String, String>> listdata;
    Boolean Registered;
    public static final String USERLOGIN = "USERLOGIN";

    public Chart() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chart, container, false);
    }

    //what the application do when the fragment is start
    @Override
    public void onStart()
    {
        super.onStart();
    }

    //what the application do when the fragment is use
    @Override
    public void onActivityCreated (Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        listdata = new ArrayList<>();
        createdb();
        loaddata();

        //x is melintang
        //y is menegak

    }

    //SQLITE DATABASE

    //create or open a database using sqllite
    public void createdb()
    {
        data_db = this.getActivity().openOrCreateDatabase("data_db",MODE_PRIVATE,null);
        String sqlcreate = "CREATE TABLE IF NOT EXISTS DATA(DATAID INTEGER PRIMARY KEY AUTOINCREMENT, CURRENTUSER VARCHAR, BSL VARCHAR, CONDITION VARCHAR, MEAL VARCHAR, DATE VARCHAR, TIME VARCHAR, NOTE VARCHAR, TYPE VARCHAR);";
        data_db.execSQL(sqlcreate);
    }

    //load Data from sqllite database
    public void loaddata()
    {
        final TextView currentUser = getActivity().findViewById(R.id.fragment_chart_currentUser);

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
        String currentUser_S = currentUser.getText().toString();

        //Toast.makeText(getActivity(), currentUser_S, Toast.LENGTH_SHORT).show();
        String sqlsearch = "SELECT DATE, TIME, BSL FROM DATA WHERE CURRENTUSER = '"+currentUser_S+"'";
        Cursor c = data_db.rawQuery(sqlsearch, null);
        //listdata.clear();
        if (c.getCount() > 0)
        {
            c.moveToFirst();
            for (int i = 0; i < c.getCount(); i++)
            {
                String date = c.getString(c.getColumnIndex("DATE"));
                String time = c.getString(c.getColumnIndex("TIME"));
                String bsl = c.getString(c.getColumnIndex("BSL"));
                float i_bsl = Float.parseFloat(bsl);

                /*HashMap<String, String> datalist = new HashMap<>();
                datalist.put("date", date);
                datalist.put("time", time);
                datalist.put("bsl", bsl);
                listdata.add(datalist);*/

                c.moveToNext();

                ArrayList<Float>bsl_i = new ArrayList<>();
                bsl_i.add(i_bsl);

                ArrayList<String>date_time_i = new ArrayList<>();
                date_time_i.add(date);
                date_time_i.add(time);

                //Float[] bsl_i = {i_bsl};
                //String[] date_time = {date+time};

                Toast.makeText(getActivity(), date_time_i.toString()+""+bsl_i.toString(), Toast.LENGTH_SHORT).show();
                graph(bsl_i,date_time_i);
            }
            c.moveToPrevious();
        }
    }

    public void graph(ArrayList<Float> bsl_i, ArrayList<String> date_time_i)
    {
        LineChartView lineChartView = getActivity().findViewById(R.id.graph);

        List bslL = new ArrayList();
        List date_timeL = new ArrayList();

        Line line = new Line(bslL).setColor(Color.parseColor("#4267B2"));

        for (int i = 0; i < date_time_i.size(); i++)
        {
            date_timeL.add(i, new AxisValue(i).setLabel(date_time_i.get(i)));

            //Toast.makeText(getActivity(), date_timeL.toString(), Toast.LENGTH_SHORT).show();
        }

        for (int i = 0; i < bsl_i.size(); i++)
        {
            bslL.add(new PointValue(i, bsl_i.get(i)));
            //Toast.makeText(getActivity(), bslL.toString(), Toast.LENGTH_SHORT).show();
        }

        List lines = new ArrayList();
        lines.add(line);

        LineChartData data = new LineChartData();

        data.setLines(lines);
        lineChartView.setLineChartData(data);

        Axis axis = new Axis();
        axis.setValues(date_timeL);
        data.setAxisXBottom(axis);

        Axis yAxis = new Axis();
        data.setAxisYLeft(yAxis);


        //get DayNight mode from shared preference in InitApplication.java
        if (InitApplication.getInstance().isNightModeEnabled())
        {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

            axis.setTextColor(Color.WHITE);
            yAxis.setTextColor(Color.WHITE);
        }
        else
        {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

            axis.setTextColor(Color.BLACK);
            yAxis.setTextColor(Color.BLACK);
        }

        axis.setTextSize(16);
        axis.setName("Date and Time");

        yAxis.setTextSize(16);
        yAxis.setName("Blood Sugar Level");

        Viewport viewport = new Viewport(lineChartView.getMaximumViewport());
        viewport.top = 20;
        viewport.bottom = 0;
        lineChartView.setMaximumViewport(viewport);
        lineChartView.setCurrentViewport(viewport);
    }
}
