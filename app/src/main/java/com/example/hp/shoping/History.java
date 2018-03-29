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
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class History extends AppCompatActivity implements AdapterView.OnItemClickListener {
    ListView l1;
    ProgressDialog pd;
    JSONObject JD;
    //public static ArrayList<String> payment_id, sale_no, amount, date;

    public static String pid[],sno[],amt[],date[];

    String login_id="";
    String sale_no="",url="";

    public static int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        l1 = (ListView) findViewById(R.id.list1);

        SharedPreferences sh= PreferenceManager.getDefaultSharedPreferences(History.this);
        login_id=sh.getString("login_id","");
        url= "http://"+sh.getString("ip","192.168.0.0")+"/shopping/android_connections/history.php";
        l1.setOnItemClickListener(this);

        new DataUploadService().execute();

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        pos=position;
        Intent res=new Intent(getApplicationContext(),Historynew.class);
        startActivity(res);

    }

    class DataUploadService extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(History.this);
            pd.setMessage("Loading..");
            pd.show();

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                if (JD != null) {
                    String status = JD.getString("success");
                    if (status.equals("1")) {
                        JSONArray jsonArray = JD.getJSONArray("products");
                        //login_id=new ArrayList<>();
//                        payment_id = new ArrayList<>();
//                        sale_no = new ArrayList<>();
//                        amount = new ArrayList<>();
//                        date = new ArrayList<>();

                        pid=new String[jsonArray.length()];
                        sno=new String[jsonArray.length()];
                        amt=new String[jsonArray.length()];
                        date=new String[jsonArray.length()];

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsobj = jsonArray.getJSONObject(i);
//                            payment_id.add(jsobj.getString("payment_id"));
//                            sale_no.add(jsobj.getString("saleno"));
//                            amount.add(jsobj.getString("amt"));
//                            date.add(jsobj.getString("date"));

                            pid[i]=jsobj.getString("payment_id");
                            sno[i]=jsobj.getString("saleno");
                            amt[i]=jsobj.getString("amt");
                            date[i]=jsobj.getString("date");
                            Log.d("+++++++++++++", date.toString());

                        }


                         // ArrayAdapter<String>ad=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,date);
                        l1.setAdapter(new CustomHistory(getApplicationContext(),date,amt));

                       // l1.setAdapter(new Custom_offers(getApplicationContext(), payment_id, sale_no, amount, date, offerfrom, offerdate, image));

                    } else {
                        Toast.makeText(History.this, "No products found....", Toast.LENGTH_SHORT).show();
                    }

                }

            } catch (Exception ex) {

            }
            pd.dismiss();
        }


        @Override
        protected String doInBackground(String... params) {
            String url = "http://192.168.43.18/shopping/android_connections/history.php";

            try {
                List<NameValuePair> pa = new ArrayList<>();
                pa.add(new BasicNameValuePair("login_id",login_id));
                pa.add(new BasicNameValuePair("saleno",sale_no));
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

        Intent intent=new Intent(getApplicationContext(),Home.class);
        startActivity(intent);
    }
}
