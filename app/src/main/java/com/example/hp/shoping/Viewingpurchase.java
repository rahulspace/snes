package com.example.hp.shoping;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Viewingpurchase extends AppCompatActivity implements AdapterView.OnItemClickListener {
    ListView l1;
    ProgressDialog pd;
    JSONObject JD;
    public static ArrayList<String> product_id,name,manufacture,details,offername,offerfrom,offerdate,offer_id,rate,image,qty;
    public  static int selected_offer=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewingpurchase);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        l1=(ListView) findViewById(R.id.list1);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        });
        new DataUploadService().execute();
        l1.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        selected_offer=position;
        Intent res=new Intent(getApplicationContext(),Offerpurchase.class);
        startActivity(res);
    }

    class DataUploadService extends AsyncTask<String,String,String>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd=new ProgressDialog(Viewingpurchase.this);
            pd.setMessage("Loading..");
            pd.show();

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                if(JD!=null){
                    String status=JD.getString("success");
                    if(status.equals("1")){
                        JSONArray jsonArray=JD.getJSONArray("products");
                        product_id=new ArrayList<>();
                        name=new ArrayList<>();
                        details=new ArrayList<>();
                        manufacture=new ArrayList<>();
                        offername=new ArrayList<>();
                        offerfrom=new ArrayList<>();
                        offerdate=new ArrayList<>();
                        offer_id=new ArrayList<>();
                        rate=new ArrayList<>();
                        image=new ArrayList<>();
                        qty=new ArrayList<>();
                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject jsobj=jsonArray.getJSONObject(i);
                            product_id.add(jsobj.getString("pid"));
                            name.add(jsobj.getString("name"));
                            manufacture.add(jsobj.getString("manufacture"));
                            details.add(jsobj.getString("details"));
                            offerfrom.add(jsobj.getString("offerfrom"));
                            offerdate.add(jsobj.getString("offerdate"));
                            offername.add(jsobj.getString("offername"));
                            offer_id.add(jsobj.getString("oid"));
                            rate.add(jsobj.getString("rate"));
                            image.add(jsobj.getString("image"));
                            qty.add(jsobj.getString("qty"));
                        }
                        l1.setAdapter(new Custom_offers(getApplicationContext(),name,manufacture,rate,details,offerfrom,offerdate,image));

                    }
                    else{
                        Toast.makeText(Viewingpurchase.this, "No products found....", Toast.LENGTH_SHORT).show();
                    }

                }

            }catch (Exception ex){
                Log.d("er ofr====",ex.toString());
            }
            pd.dismiss();
        }

        @Override
        protected String doInBackground(String... params)
        {
            SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(Viewingpurchase.this);
        String url="http://"+sharedPreferences.getString("ip","192.168.0.0")+"/shopping/android_connections/view_offer.php";

            try {
                List<NameValuePair> pa=new ArrayList<>();
                JSONParser JS=new JSONParser();
                JD=JS.makeHttpRequest(url,"GET",pa);

                Log.d("res====",JD.toString());

            }catch(Exception ex)
            {
                Log.d("err==========",ex.toString());
            }


            return "ok";

        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent=new Intent(getApplicationContext(),Home.class);
        startActivity(intent);
    }
}
