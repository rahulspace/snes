package com.example.hp.shoping;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class VerificationService extends Service {

    Handler handler;
    SharedPreferences sharedPreferences;
    String payment_id="",url="";
    JSONObject jsonObject;
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        handler=new Handler();
        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(VerificationService.this);
        url="http://"+sharedPreferences.getString("ip","192.168.0.0")+"/shopping/android_connections/verificationstatus.php";

        handler.post(runnable);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }

    public  Runnable runnable=new Runnable() {
        @Override
        public void run() {

            String result="0";
            try
            {
                payment_id=sharedPreferences.getString("payment_id","0");
                Log.d("pppppp--------",payment_id);
                List<NameValuePair> para=new ArrayList<>();
                para.add(new BasicNameValuePair("payment_id",payment_id));
                JSONParser jsonParser=new JSONParser();
                jsonObject=jsonParser.makeHttpRequest(url,"GET",para);
                result=jsonObject.getString("status");
                Log.d("service====",result);
                String payment_type=jsonObject.getString("accno");
                if(result.equals("verified")&&payment_type.equalsIgnoreCase("Card")){

                        Intent intent=new Intent(VerificationService.this,VerificationService.class);
                        stopService(intent);

                        Intent intent1 = new Intent(VerificationService.this, Billing.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent1);

                }
                else if(payment_type.equalsIgnoreCase("Cash")&&result.equals("paid")){

                    Intent intent=new Intent(VerificationService.this,VerificationService.class);
                    stopService(intent);

                    Intent intent1 = new Intent(VerificationService.this, Transaction.class);
                    intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent1);
                }
                else{
                    handler.postDelayed(runnable,2000);
                }
            }
            catch (Exception ex)
            {
                Log.d("Err ser--------",ex.toString());
            }

        }
    };
}
