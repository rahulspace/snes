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

public class Custom_offers extends BaseAdapter {


    private Context Context;
    ArrayList<String> name;
    ArrayList<String> manufacture;
    ArrayList<String> rate;
    ArrayList<String> offerdetails;
    ArrayList<String> offerfrom;
    ArrayList<String> offerdate;
    ArrayList<String> image;

    public Custom_offers(Context applicationContext, ArrayList<String> name, ArrayList<String> manufacture,  ArrayList<String> rate,  ArrayList<String> offerdetails, ArrayList<String> offerfrom, ArrayList<String> offerdate,ArrayList<String> image) {

        this.Context=applicationContext;
        this.name=name;
        this.manufacture=manufacture;
        this.rate=rate;
        this.offerdetails=offerdetails;
        this.offerfrom=offerfrom;
        this.offerdate=offerdate;
        this.image=image;
    }






    @Override
    public int getCount() {

        return name.size();
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
            gridView=inflator.inflate(R.layout.custom_offers, null);

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


        tv1.setText(name.get(position));
        tv2.setText(manufacture.get(position));
        tv3.setText("Rate:"+rate.get(position));
        tv4.setText(offerdetails.get(position));
        tv5.setText(offerfrom.get(position)+"-"+offerdate.get(position));
        SharedPreferences sh=PreferenceManager.getDefaultSharedPreferences(Context);
        String im=image.get(position).replace(" ","%20");
        String path="http://"+sh.getString("ip","192.168.0.0")+"/shopping/photos/"+im;
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
