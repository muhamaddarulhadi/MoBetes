package com.hadi.mobetes.Fragment;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.hadi.mobetes.R;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;

import static android.content.Context.MODE_PRIVATE;

public class Chart extends Fragment
{
    SQLiteDatabase data_db;
    private ArrayList<HashMap<String, String>> listdata;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");


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
        createdb();
        loaddata();
    }

    //what the application do when the fragment is use
    @Override
    public void onActivityCreated (Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        //x is melintang
        //y is menegak





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
        final TextView currentUser = getActivity().findViewById(R.id.fragment_chart_currentUser);
        String currentUser_S = String.valueOf(currentUser.getText().toString());

        String sqlsearch = "SELECT BSL, DATE FROM DATA WHERE CURRENTUSER = '"+currentUser_S+"'";
        Cursor c = data_db.rawQuery(sqlsearch,null);
        //listdata.clear();
        if (c.getCount() > 0)
        {
            c.moveToFirst();
            for (int i=0;i<c.getCount();i++)
            {
                GraphView graphView = getActivity().findViewById(R.id.graph);
                String bsl = c.getString(c.getColumnIndex("BSL"));
                String date = c.getString(c.getColumnIndex("DATE"));
                HashMap<String, String> datalist = new HashMap<>();
                datalist.put("bsl",bsl);
                datalist.put("date",date);
                listdata.add(datalist);
                c.moveToNext();

            }
        }
    }
}
