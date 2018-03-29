package com.example.hp.shoping;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class Transaction extends AppCompatActivity {
    String payment_id,url;
    ProgressDialog pd;
    JSONObject JD;
    Button B1;
    String ipadr="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        B1 = (Button) findViewById(R.id.button11);

        SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(Transaction.this);
        payment_id = sh.getString("payment_id", "");
        url = "http://" + sh.getString("ip", "192.168.0.0") + "/shopping/android_connections/invoice_download.php";
        ipadr=sh.getString("ip", "192.168.0.0");
        B1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DataUploadService().execute();
            }
        });
    }
        class DataUploadService extends AsyncTask<String, String, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pd = new ProgressDialog(Transaction.this);
                pd.setMessage("Loading..");
                pd.show();

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);


                if(s.equalsIgnoreCase("")){

                    Toast.makeText(Transaction.this, "invoice not available..!!", Toast.LENGTH_SHORT).show();
                }
                else {

                    try {
                        String filename=payment_id+".pdf";
                        byte[] bs= Base64.decode(s,android.util.Base64.DEFAULT);
                        File file = new File(Environment.getExternalStorageDirectory(),filename);
                        FileOutputStream fos;
                        fos = new FileOutputStream(file);
                        fos.write(bs);
                        fos.flush();
                        fos.close();
                        Toast.makeText(Transaction.this, "invoice downloaded successfully.......", Toast.LENGTH_SHORT).show();

                        Intent intent=new Intent(getApplicationContext(),Home.class);
                        startActivity(intent);
                    }catch (Exception e){

                        Log.d("err dnld",e.toString());
                    }
                }
                pd.dismiss();
            }


            @Override
            protected String doInBackground(String... params) {
                String result="error";
                try {
                    List<NameValuePair> pa = new ArrayList<>();
                    pa.add(new BasicNameValuePair("payment_id",payment_id));
                    pa.add(new BasicNameValuePair("ipadr",ipadr));

                    JSONParser JS = new JSONParser();
                    JD = JS.makeHttpRequest(url, "GET", pa);
                    result=JD.getString("result");
                    Log.d("res====", JD.toString());

                } catch (Exception ex) {
                    Log.d("err==========", ex.toString());
                }


                return result;

            }

        }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent=new Intent(getApplicationContext(),Home.class);
        startActivity(intent);
    }
}
