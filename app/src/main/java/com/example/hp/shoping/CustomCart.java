package com.example.hp.shoping;

/**
 * Created by hp on 24-08-2017.
 */

import com.squareup.picasso.Picasso;

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
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomCart extends BaseAdapter {


    private Context Context;
    ArrayList<String> product_name;
    ArrayList<String> rate;
    ArrayList<String> qty;
    ArrayList<String> dist;
    ArrayList<String> image;

    public CustomCart(Context applicationContext, ArrayList<String> name,   ArrayList<String> rate,  ArrayList<String> quantity, ArrayList<String> discount,ArrayList<String> image) {

        this.Context=applicationContext;
        this.product_name=name;
        this.rate=rate;
        this.qty=quantity;
        this.dist=discount;
        this.image=image;
    }






    @Override
    public int getCount() {

        return product_name.size();
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
            gridView=inflator.inflate(R.layout.activity_custom_cart, null);

        }
        else
        {
            gridView=(View)convertview;

        }

        TextView tv1=(TextView)gridView.findViewById(R.id.textView1);
        TextView tv2=(TextView)gridView.findViewById(R.id.textView2);
        TextView tv3=(TextView)gridView.findViewById(R.id.textView3);
        TextView tv4=(TextView)gridView.findViewById(R.id.textView4);
        TextView tv5=(TextView)gridView.findViewById(R.id.textView5);
        ImageView img=(ImageView)gridView.findViewById(R.id.imageView1);
        tv1.setTextColor(Color.BLACK);
        tv2.setTextColor(Color.BLACK);
        tv3.setTextColor(Color.BLACK);
        tv4.setTextColor(Color.BLACK);
        tv5.setTextColor(Color.BLACK);


        tv1.setText("Product_name:"+product_name.get(position));
        //tv2.setText(manufacture.get(position));
        tv2.setText("Rate:"+rate.get(position));
        tv3.setText("Quantity:"+qty.get(position));
        tv4.setText("Discount:"+dist.get(position));
        SharedPreferences sh=PreferenceManager.getDefaultSharedPreferences(Context);
        String imgw=image.get(position).replace(" ","%20");
        String path="http://"+sh.getString("ip","192.168.0.0")+"/shopping/photos/"+imgw;
        Log.d("img========",path);
        try {
            Picasso.with(Context)
                    .load(path)
                    .placeholder(R.drawable.ic_menu_camera)
                    .error(R.drawable.ic_menu_camera).into(img);

        } catch (Exception e) {
            // TODO: handle exception
        }

        return gridView;
    }



}
