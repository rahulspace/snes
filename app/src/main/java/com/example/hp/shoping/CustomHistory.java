package com.example.hp.shoping;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by hp on 18-09-2017.
 */


public class CustomHistory extends BaseAdapter {


    private android.content.Context Context;
    String[] date;
    String[] amount;

    public CustomHistory(Context applicationContext, String[] date,   String[] amount) {

        this.Context=applicationContext;
        this.date=date;
        this.amount=amount;
    }






    @Override
    public int getCount() {

        return date.length;
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertview, ViewGroup parent) {


        LayoutInflater inflator=(LayoutInflater)Context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        View gridView;
        if(convertview==null)
        {
            gridView=new View(Context);
            gridView=inflator.inflate(R.layout.custom_history, null);

        }
        else
        {
            gridView=(View)convertview;

        }

        TextView tv1=(TextView)gridView.findViewById(R.id.textView42);
        TextView tv2=(TextView)gridView.findViewById(R.id.textView43);

        tv1.setTextColor(Color.BLACK);
        tv2.setTextColor(Color.BLACK);



        tv1.setText(date[position]);

        tv2.setText(amount[position]);

        return gridView;
    }



}
