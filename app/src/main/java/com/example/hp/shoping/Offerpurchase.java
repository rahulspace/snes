package com.example.hp.shoping;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.preference.DialogPreference;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Offerpurchase extends AppCompatActivity implements RatingBar.OnRatingBarChangeListener {

    TextView tv_name,tv_pid,tv_manf,tv_quantity,tv_offer,tv_offerdetails,tv_rate;
    Button b4;
    RatingBar avg_rating,user_rating;
    ProgressDialog pd;
    ProgressDialog pd2;
    int flag=0;
    JSONParser jsonParser;
    JSONObject jsonObject;
    String product_id="";
    String login_id="",rating="",url1="";
    String offer_id="",prate,discount,pimage,pname,qty;
    String threshold;
    ImageView i;
    float ab,bc,cd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offerpurchase);
        tv_name=(TextView) findViewById(R.id.textView9);
        tv_pid=(TextView) findViewById(R.id.textView7);
        tv_manf=(TextView) findViewById(R.id.textView13);
        tv_rate=(TextView) findViewById(R.id.textView11);
        tv_quantity=(TextView) findViewById(R.id.textView15);
        avg_rating=(RatingBar) findViewById(R.id.ratingBar1);
        user_rating=(RatingBar) findViewById(R.id.ratingBar2);
        tv_offer=(TextView) findViewById(R.id.textView22);
        tv_offerdetails=(TextView) findViewById(R.id.textView24);
        i=(ImageView) findViewById(R.id.imageView2);
        b4=(Button) findViewById(R.id.button4); 
        avg_rating.setEnabled(false);
        product_id=Viewingpurchase.product_id.get(Viewingpurchase.selected_offer);
        tv_name.setText(Viewingpurchase.name.get(Viewingpurchase.selected_offer));
        tv_pid.setText(Viewingpurchase.product_id.get(Viewingpurchase.selected_offer));
        tv_rate.setText(Viewingpurchase.rate.get(Viewingpurchase.selected_offer));
        tv_manf.setText(Viewingpurchase.manufacture.get(Viewingpurchase.selected_offer));
        tv_quantity.setText(Viewingpurchase.qty.get(Viewingpurchase.selected_offer));
        tv_offer.setText(Viewingpurchase.offername.get(Viewingpurchase.selected_offer));
        tv_offerdetails.setText(Viewingpurchase.details.get(Viewingpurchase.selected_offer));
        pname=Viewingpurchase.name.get(Viewingpurchase.selected_offer);
        pimage=Viewingpurchase.image.get(Viewingpurchase.selected_offer);
        prate=Viewingpurchase.rate.get(Viewingpurchase.selected_offer);
        discount=Viewingpurchase.details.get(Viewingpurchase.selected_offer);




        SharedPreferences sharedPreferences=PreferenceManager.getDefaultSharedPreferences(Offerpurchase.this);
        url1= "http://"+sharedPreferences.getString("ip","192.168.0.0")+"/shopping/android_connections/";
        String im=Viewingpurchase.image.get(Viewingpurchase.selected_offer).replace(" ","%20");
        String path="http://"+sharedPreferences.getString("ip","192.168.0.0")+"/shopping/photos/"+im;
        Log.d("img========",path);
        try {
            Picasso.with(Offerpurchase.this)
                    .load(path)
                    .placeholder(R.drawable.ic_menu_camera)
                    .error(R.drawable.ic_menu_camera).into(i);

        } catch (Exception e) {
            // TODO: handle exception
        }
        new DatauploadServiceOffer().execute();

        user_rating.setOnRatingBarChangeListener(this);
        SharedPreferences sh= PreferenceManager.getDefaultSharedPreferences(Offerpurchase.this);
        login_id=sh.getString("login_id","");
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                
                AlertDialog.Builder alert=new AlertDialog.Builder(Offerpurchase.this);
                final EditText ed_quntity=new EditText(Offerpurchase.this);
                ed_quntity.setInputType(InputType.TYPE_CLASS_NUMBER);
                alert.setTitle("Add to Cart");
                alert.setMessage("Enter Quantity...");
                alert.setView(ed_quntity);
                
                alert.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(Offerpurchase.this, ed_quntity.getText().toString(), Toast.LENGTH_SHORT).show();

                                String qty=ed_quntity.getText().toString();
                                ab=Float.parseFloat( prate);
                                bc=Float.parseFloat(qty);
                                float offer=Float.parseFloat(tv_offerdetails.getText().toString());
                                float tot=(ab-((ab*offer)/100))*bc;
                                SharedPreferences sh= PreferenceManager.getDefaultSharedPreferences(Offerpurchase.this);
                                threshold=sh.getString("threshold","");
                                cd=Float.parseFloat( threshold);
                                float availqty=Float.parseFloat(tv_quantity.getText().toString());
                                if(availqty<bc){

                                    Toast.makeText(Offerpurchase.this, "Quantity not available...!!", Toast.LENGTH_SHORT).show();
                                }else {
                                    if ((cd - tot) < 0) {
                                        Toast.makeText(Offerpurchase.this, "Amount Exceeds your Threshold", Toast.LENGTH_SHORT).show();
                                    } else {
                                        addToCart(login_id, product_id, ab+"", offer+"", tv_name.getText().toString(), pimage, qty);
                                        Toast.makeText(Offerpurchase.this, "Quantity Added..", Toast.LENGTH_SHORT).show();
                                        Intent res = new Intent(getApplicationContext(), Viewingpurchase.class);
                                        startActivity(res);
                                    }
                                }
                            }
                        });


                        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(Offerpurchase.this, "cancel clicked", Toast.LENGTH_SHORT).show();
                                    }
                                });
                AlertDialog alrt = alert.create();
                alrt.show();
            }
        });
    }

    @Override
    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

        if(ratingBar==user_rating&&!this.rating.equalsIgnoreCase("")&&!this.rating.equalsIgnoreCase(Float.toString(rating))&&flag==1){
            this.rating=rating+"";
           
            new DatauploadService2().execute();
        }

    }


    class DatauploadServiceOffer extends AsyncTask<String,String,String>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd2=new ProgressDialog(Offerpurchase.this);
            pd2.setMessage("Loading....");
            pd2.show();;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.equals("0"))
            {
                Toast.makeText(Offerpurchase.this, "Error..", Toast.LENGTH_SHORT).show();
            }
            else

            {
                try {

                    String avg_rate=jsonObject.getString("avg_rating");
                    avg_rating.setRating(Float.parseFloat(avg_rate));
                    String user_rate=jsonObject.getString("user_rating");
                    rating=user_rate;
                    user_rating.setRating(Float.parseFloat(user_rate));
                }catch (Exception e){
                    pd2.dismiss();
                }
            }
            pd2.dismiss();
            flag=1;
        }

        @Override
        protected String doInBackground(String... params) {
            String offerUrl= url1+"rating.php";
            String result="0";
            try
            {

                List<NameValuePair> para=new ArrayList<>();
                para.add(new BasicNameValuePair("product_id",product_id));
                para.add(new BasicNameValuePair("login_id",login_id));
                jsonParser=new JSONParser();
                jsonObject=null;
                jsonObject=jsonParser.makeHttpRequest(offerUrl,"GET",para);
                Log.d("res========",jsonObject.toString());
                result=jsonObject.getString("status");

            }
            catch (Exception ex)
            {
                Log.d("err====",ex.toString());

            }
            return result;
        }
    }


    class DatauploadService2 extends AsyncTask<String,String,String>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd=new ProgressDialog(Offerpurchase.this);
            pd.setMessage("Loading....");
            pd.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.equals("0")) {
                Toast.makeText(Offerpurchase.this, "Error,Try again...!", Toast.LENGTH_SHORT).show();
            } else {
                try{

                    String avg_rate=jsonObject.getString("avg_rating");
                    avg_rating.setRating(Float.parseFloat(avg_rate));
                }catch (Exception e){}
                Toast.makeText(Offerpurchase.this, "Rating added Sucessfully...!", Toast.LENGTH_SHORT).show();
            }
            pd.dismiss();
        }

        @Override
        protected String doInBackground(String... params) {
            String url = url1+"add_rating.php";
            String result = "0";
            try {
                List<NameValuePair> para = new ArrayList<>();
                para.add(new BasicNameValuePair("login_id", login_id));
                para.add(new BasicNameValuePair("product_id", product_id));
                para.add(new BasicNameValuePair("rating", rating));

                JSONParser jsonParser = new JSONParser();
                jsonObject=null;
                jsonObject = jsonParser.makeHttpRequest(url, "GET", para);
                result = jsonObject.getString("status");
            } catch (Exception ex) {
                Log.d("err====", ex.toString());
            }
            return result;
        }

    }

    private void addToCart(String login_id,String product_id,String rate,String discount,String pname,String pimage,String quantity){

        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        String curDate=simpleDateFormat.format(new Date());

        SQLiteDatabase sqd=openOrCreateDatabase("Shopping", SQLiteDatabase.CREATE_IF_NECESSARY,null);

        String qry="create table if not exists cart(cart_id integer primary key autoincrement,login_id text,product_id text, product_name text,rate text,discount text, image text,quantity text,date text)";
        sqd.execSQL(qry);

        ContentValues cv=new ContentValues();
        cv.put("login_id",login_id);
        cv.put("product_id",product_id);

        cv.put("product_name",pname);
        cv.put("image",pimage);
        cv.put("rate",rate);
        cv.put("quantity",quantity);
        cv.put("discount",discount);
        cv.put("date",curDate);
        sqd.insert("cart",null,cv);
        sqd.close();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent=new Intent(getApplicationContext(),Viewingpurchase.class);
        startActivity(intent);
    }
}





