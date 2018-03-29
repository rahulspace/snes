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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Billing extends AppCompatActivity {
    EditText e1,e2,e3;
    Button b1;
    String payment_type="Card",acc_number,pin="",amount="",payment_id;
    SharedPreferences sharedPreferences;
    ProgressDialog pd;
    String url="";
    JSONObject JD;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing);
        e1=(EditText) findViewById(R.id.editText18);
        e2=(EditText) findViewById(R.id.editText19);
        e3=(EditText) findViewById(R.id.editText20);
        b1=(Button) findViewById(R.id.button8);

        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(Billing.this);
        payment_id=sharedPreferences.getString("payment_id","0");
        amount=sharedPreferences.getString("amount","0");
        e3.setText(amount);
        e3.setEnabled(false);

        url= "http://"+sharedPreferences.getString("ip","192.168.0.0")+"/shopping/android_connections/payment.php";
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                acc_number=e1.getText().toString();
                pin=e2.getText().toString();
                new DataUploadService().execute();

            }
        });
    }

    class DataUploadService extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(Billing.this);
            pd.setMessage("Loading..");
            pd.show();

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


            if(s.equalsIgnoreCase("error")){

                Toast.makeText(Billing.this, "Some thing went wrong...!!", Toast.LENGTH_SHORT).show();
            }
            else if(s.equalsIgnoreCase("invalid")){

                Toast.makeText(Billing.this, "Invalid Credentials..", Toast.LENGTH_SHORT).show();
            }
            else if(s.equalsIgnoreCase("paid")){

                Toast.makeText(Billing.this, "Transaction Sucesss....", Toast.LENGTH_SHORT).show();
                Intent res=new Intent(getApplicationContext(),Transaction.class);
                startActivity(res);

            }
            else if(s.equalsIgnoreCase("no balance")){

                Toast.makeText(Billing.this, "Insufficent Balance...", Toast.LENGTH_SHORT).show();

            }


            pd.dismiss();
        }


        @Override
        protected String doInBackground(String... params) {
            String result="error";
            try {
                List<NameValuePair> pa = new ArrayList<>();
                pa.add(new BasicNameValuePair("payment_id",payment_id));
                pa.add(new BasicNameValuePair("payment_type",payment_type));
                pa.add(new BasicNameValuePair("accno",acc_number));
                pa.add(new BasicNameValuePair("pin",pin));
                pa.add(new BasicNameValuePair("amount",amount));

                JSONParser JS = new JSONParser();
                JD = JS.makeHttpRequest(url, "GET", pa);
                result=JD.getString("status");
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

        Intent intent=new Intent(getApplicationContext(),Billing.class);
        startActivity(intent);
    }

}
