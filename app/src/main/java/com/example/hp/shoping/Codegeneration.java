package com.example.hp.shoping;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Codegeneration extends AppCompatActivity {
    TextView t1;
    Button b1;
    String rn="ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    String lid = "";
    String[] cart_id;
    String ran_String="",url="";
    JSONObject cartElements;
    ProgressDialog pd;
    JSONObject jsonObject;
    TextView tv_notes;
    public static ArrayList<String> product_id,product_name,rate,discount,date,image,quantity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_codegeneration);
        t1=(TextView) findViewById(R.id.textView2);
        b1=(Button) findViewById(R.id.button7);
        tv_notes=(TextView)findViewById(R.id.textView46);

        tv_notes.setText("Note:click next button to upload your purchase details. And then verify products from sales");
        try {
            if(Build.VERSION.SDK_INT>9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
        }catch (Exception e){}
        SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(Codegeneration.this);
        lid=sh.getString("login_id", "");
        url= "http://"+sh.getString("ip","192.168.0.0")+"/shopping/android_connections/insert_cart.php";
        cartElements=new JSONObject();
        Random random=new Random();
        ran_String=lid;
        while(ran_String.length()<10){

            int random_number=random.nextInt(rn.length());
            ran_String+=rn.substring(random_number,random_number+1);
        }

        t1.setText(ran_String);

        serverUpload();

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatauploadService().execute();

                tv_notes.setText("Note:Please wait, app automatically navigate to payment section after the successful completion of verification process.");
                b1.setEnabled(false);
            }
        });
    }
    void serverUpload()
    {
        try {

            SQLiteDatabase sqd = openOrCreateDatabase("Shopping", SQLiteDatabase.CREATE_IF_NECESSARY, null);
            String qry = "create table if not exists cart(cart_id integer primary key autoincrement,login_id text,product_id text, product_name text,rate text,discount text, image text,quantity text,date text)";
            sqd.execSQL(qry);

            String qry1 = "select * from cart where login_id='" + lid + "'";
            Cursor c = sqd.rawQuery(qry1, null);
            JSONArray cartArray=new JSONArray();
            if (c.getCount() > 0) {

                cartElements.put("status","1");
                int i = 0;
                c.moveToFirst();
                do {

                    JSONObject jsonObject=new JSONObject();
                    jsonObject.put("product_id",c.getString(2));
                    jsonObject.put("rate",c.getString(4));
                    jsonObject.put("discount",c.getString(5));
                    jsonObject.put("quantity",c.getString(7));

                    cartArray.put(jsonObject);

                }while(c.moveToNext());

                cartElements.put("products",cartArray);
            }
            else{

                cartElements.put("status","0");
            }
            cartElements.put("login_id",lid);
            cartElements.put("Random",ran_String);
            sqd.close();
            Log.d("eeee=======",cartElements.toString());
        }catch (Exception e){

            Log.d("err++++++++++++++",e.toString());
        }
    }


    class DatauploadService extends AsyncTask<String,String,String>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd=new ProgressDialog(Codegeneration.this);
            pd.setMessage("Loading....");
            pd.show();;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {

                JSONObject jsonObject=new JSONObject(s);
                String payment_id=jsonObject.getString("payment_id");
                Log.d("paymentidd==1111111====",payment_id);
                if(payment_id.equals("0")){
                    Toast.makeText(Codegeneration.this, "Error....", Toast.LENGTH_SHORT).show();
                }
                else{
                    SharedPreferences sh=PreferenceManager.getDefaultSharedPreferences(Codegeneration.this);
                    SharedPreferences.Editor ed=sh.edit();
                    ed.putString("payment_id",payment_id);
                    ed.putString("amount",jsonObject.getString("amount"));
                    ed.commit();
                    Toast.makeText(Codegeneration.this, "Success...", Toast.LENGTH_SHORT).show();

                    try {
                        SQLiteDatabase sqd = openOrCreateDatabase("Shopping", SQLiteDatabase.CREATE_IF_NECESSARY, null);
                        String qry = "create table if not exists cart(cart_id integer primary key autoincrement,login_id text,product_id text, product_name text,rate text,discount text, image text,quantity text,date text)";
                        sqd.execSQL(qry);
                        sqd.execSQL("delete from cart where login_id='"+lid+"'");
                        sqd.close();
                    }catch (Exception e){}

                    Intent intent=new Intent(Codegeneration.this,VerificationService.class);
                    startService(intent);
                }
            }catch (Exception e){}
            pd.dismiss();
        }

        @Override
        protected String doInBackground(String... params) {
            String result="0";
            try
            {
               result= postJSONObject(url,cartElements);
            }
            catch (Exception ex)
            {
                Log.d("err====",ex.toString());

            }
            return result;
        }
    }



    public static String postJSONObject(String myurl, JSONObject parameters) {
        HttpURLConnection conn = null;
        try {
            StringBuffer response = null;
            URL url = new URL(myurl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            OutputStream out = new BufferedOutputStream(conn.getOutputStream());
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            writer.write(parameters.toString());
            writer.close();
            out.close();
            int responseCode = conn.getResponseCode();
            System.out.println("responseCode" + responseCode);
            switch (responseCode) {
                case 200:
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String inputLine;
                    response = new StringBuffer();
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();
                    return response.toString();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.disconnect();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        return null;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent=new Intent(getApplicationContext(),Viewingcart.class);
        startActivity(intent);
    }
}




