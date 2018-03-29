package com.example.hp.shoping;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Historynew extends AppCompatActivity implements AdapterView.OnItemClickListener {
    TextView t1,t2,t3,t4;
    ListView l1;
    public static ArrayList<String> product_id, pname,manufacture,rate,image,discount,quantity;
    ProgressDialog pd;
    JSONObject JD;
    String sale_no="",url="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historynew);
        t1=(TextView) findViewById(R.id.textView16);
        t2=(TextView) findViewById(R.id.textView17);
        t3=(TextView) findViewById(R.id.textView19);
        t4=(TextView) findViewById(R.id.textView20);
        l1=(ListView) findViewById(R.id.list3);
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(Historynew.this);
        url= "http://"+sharedPreferences.getString("ip","192.168.0.0")+"/shopping/android_connections/cartdetails.php";

                String p=History.pid[History.pos];
                 t1.setText(p);
            String p1=History.sno[History.pos];
            t2.setText(p1);
        sale_no=p1;
        String p3=History.amt[History.pos];
        t3.setText(p3);
        String p4=History.date[History.pos];
        t4.setText(p4);


        l1.setOnItemClickListener(this);
        new Historynew.DataUploadService2().execute();

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


    }

    class DataUploadService2 extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(Historynew.this);
            pd.setMessage("Loading..");
            pd.show();

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                if (JD != null) {
                    String status = JD.getString("success");
                    Log.d("ER======",JD.toString());
                    if (status.equals("1")) {
                        JSONArray jsonArray = JD.getJSONArray("products");
                        product_id=new ArrayList<String>();
                        pname=new ArrayList<String>();
                        manufacture=new ArrayList<String>();
                        rate=new ArrayList<String>();
                        image=new ArrayList<String>();
                        discount=new ArrayList<String>();
                        quantity=new ArrayList<String>();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsobj = jsonArray.getJSONObject(i);
                            product_id.add(jsobj.getString("product_id"));
                            pname.add(jsobj.getString("product_name"));
                            manufacture.add(jsobj.getString("manufacture"));
                            rate.add(jsobj.getString("rate"));
                            image.add(jsobj.getString("image"));
                            discount.add(jsobj.getString("discount"));
                            quantity.add(jsobj.getString("quantity"));

                        }
                        ArrayAdapter<String> ad=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,pname);
                        l1.setAdapter(new CustomCart(getApplicationContext(),pname,rate,quantity,discount,image));

                    } else {
                        Toast.makeText(Historynew.this, "No products found....", Toast.LENGTH_SHORT).show();
                    }

                }

            } catch (Exception ex) {

            }
            pd.dismiss();
        }


        @Override
        protected String doInBackground(String... params) {

            try {
                List<NameValuePair> pa = new ArrayList<>();
                Log.d("sale id ======",sale_no);
                pa.add(new BasicNameValuePair("sale_no",sale_no));
                JSONParser JS = new JSONParser();
                JD = JS.makeHttpRequest(url, "GET", pa);

                Log.d("res====", JD.toString());

            } catch (Exception ex) {
                Log.d("err==========", ex.toString());
            }


            return "ok";

        }

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent=new Intent(getApplicationContext(),History.class);
        startActivity(intent);
    }
}
