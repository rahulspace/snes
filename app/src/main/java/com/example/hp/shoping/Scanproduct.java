package com.example.hp.shoping;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.content.DialogInterface;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Scanproduct extends AppCompatActivity implements RatingBar.OnRatingBarChangeListener {

    int flg=0;
    TextView tv_pid,tv_pname,tv_rate,tv_manfacture,tv_qty,tv_offer;
    Button b1_scan,b2_cart;
    RatingBar r1,r2;
    ImageView i1;
    String contents="";
    float ab,bc,cd;
    String threshold;
    String login_id="",product_id="";
    String prate,discount,pname,pimage,manufacture,url="",qty,rating="";
    TextView t1;
    String th;

    ProgressDialog pd;
    JSONObject JD=null;
    String offer="";
    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanproduct);
        tv_pid=(TextView) findViewById(R.id.textView7);
        tv_pname=(TextView) findViewById(R.id.textView9);
        tv_rate=(TextView) findViewById(R.id.textView11);
        tv_manfacture=(TextView) findViewById(R.id.textView13);
        tv_qty=(TextView) findViewById(R.id.textView15);
        b1_scan=(Button) findViewById(R.id.button3);
        b2_cart=(Button) findViewById(R.id.button4);
        r1=(RatingBar) findViewById(R.id.ratingBar1);
        r2=(RatingBar) findViewById(R.id.ratingBar2);
        i1=(ImageView) findViewById(R.id.imageView2);
        t1=(TextView) findViewById(R.id.textView41);
        tv_offer=(TextView) findViewById(R.id.textView44);
        SharedPreferences sh1=PreferenceManager.getDefaultSharedPreferences(Scanproduct.this);
        th=sh1.getString("threshold","");
        t1.setText(th+" remaining");
        SharedPreferences sh=PreferenceManager.getDefaultSharedPreferences(Scanproduct.this);
        login_id=sh.getString("login_id","");
        url= "http://"+sh.getString("ip","192.168.0.0")+"/shopping/android_connections/scan_result.php";

        r2.setOnRatingBarChangeListener(this);
        r1.setEnabled(false);
        b1_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // contents="product_7";
                new DataUploadService().execute();
                scanQR();
            }
        });
        b2_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert=new AlertDialog.Builder(Scanproduct.this);
                final EditText ed_quntity=new EditText(Scanproduct.this);
                ed_quntity.setInputType(InputType.TYPE_CLASS_NUMBER);
                alert.setTitle("Add to Cart");
                alert.setMessage("Enter Quantity...");
                alert.setView(ed_quntity);

                alert.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(Scanproduct.this, ed_quntity.getText().toString(), Toast.LENGTH_SHORT).show();
                        String qty=ed_quntity.getText().toString();

                        try {
                            ab=Float.parseFloat( tv_rate.getText().toString());
                            bc=Float.parseFloat( qty);
                            float offers=Float.parseFloat(offer);
                            float tot=(ab-((ab*offers)/100))*bc;

                            SharedPreferences sh= PreferenceManager.getDefaultSharedPreferences(Scanproduct.this);
                            threshold=sh.getString("threshold","");
                            cd=Float.parseFloat( threshold);
                            Log.d("rerrrrrr===",ab+":"+bc+":"+tot+":"+cd);
                            float availQty=Float.parseFloat(tv_qty.getText().toString());
                            if(availQty<(Float.parseFloat(qty))){
                                Toast.makeText(Scanproduct.this, "Quantity not available....!", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                if (cd - tot < 0) {
                                    Toast.makeText(Scanproduct.this, "Amount Exceeds your Threshold", Toast.LENGTH_SHORT).show();
                                } else {

                                    SharedPreferences.Editor editor = sh.edit();
                                    editor.putString("threshold", (cd - tot) + "");
                                    editor.commit();
                                    t1.setText((cd - tot) + " remaining");
                                    addToCart(login_id, product_id, ab+"", offer, tv_pname.getText().toString(), pimage, qty);
                                    Toast.makeText(Scanproduct.this, "Quantity Added..", Toast.LENGTH_SHORT).show();
                                    Intent in=new Intent(getApplicationContext(),Scanproduct.class);
                                    startActivity(in);
                                }
                            }
                        }catch (Exception e){

                            Log.d("err adding==+++",e.toString());
                        }



                    }
                });


                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(Scanproduct.this, "cancel clicked", Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog alrt = alert.create();
                alrt.show();
            }
        });


    }

    public void scanQR() {
        try {

            Intent intent = new Intent(ACTION_SCAN);
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
            startActivityForResult(intent, 0);
        } catch (ActivityNotFoundException anfe) {
            showDialog(Scanproduct.this, "No Scanner Found", "Download a scanner code activity?", "Yes", "No").show();
        }
    }
    private static AlertDialog showDialog(final Activity act, CharSequence title, CharSequence message, CharSequence buttonYes, CharSequence buttonNo) {
        AlertDialog.Builder downloadDialog = new AlertDialog.Builder(act);
        downloadDialog.setTitle(title);
        downloadDialog.setMessage(message);
        downloadDialog.setPositiveButton(buttonYes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Uri uri = Uri.parse("market://search?q=pname:" + "com.google.zxing.client.android");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    act.startActivity(intent);
                } catch (ActivityNotFoundException anfe) {

                }
            }
        });
        downloadDialog.setNegativeButton(buttonNo, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        return downloadDialog.show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
               // Toast.makeText(this, ""+contents, Toast.LENGTH_SHORT).show();
                new DataUploadService().execute();
            }
        }
    }

    @Override
    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

            this.rating=rating+"";
        if(!tv_rate.getText().toString().trim().equals("")){

            new DatauploadService2().execute();
        }
    }

    class DataUploadService extends AsyncTask<String,String,String>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd=new ProgressDialog(Scanproduct.this);
            pd.setMessage("Loading..");
          //  pd.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if(s.equals("0")){
                Toast.makeText(Scanproduct.this, "No result found....", Toast.LENGTH_SHORT).show();
            }
            else{

                    flg=1;
                    try{

                        Log.d("resss====",JD.toString());
                        tv_pid.setText(JD.getString("product_id"));
                        tv_pname.setText(JD.getString("product_name"));
                        tv_manfacture.setText(JD.getString("manuf"));
                        tv_rate.setText(JD.getString("rate"));
                        tv_qty.setText(JD.getString("qty"));
                        qty=JD.getString("qty");
                        offer=JD.getString("offers");
                        tv_offer.setText("Offer: "+offer+"%");
                        try {
                            r1.setRating(Float.parseFloat(JD.getString("avg_rating")));
                        }catch (Exception e){
                            r1.setRating(0);
                        }

                        try {

                            r2.setRating(Float.parseFloat(JD.getString("user_rating")));

                        }catch (Exception e){

                            r1.setRating(0);
                        }
                        SharedPreferences sharedPreferences=PreferenceManager.getDefaultSharedPreferences(Scanproduct.this);
                        String img=JD.getString("image").replace(" ","%20");
                        String path="http://"+sharedPreferences.getString("ip","192.168.0.0")+"/shopping/photos/"+img;
                        Log.d("img========"+JD.getString("product_id"),path);


                        product_id=tv_pid.getText().toString();
                        pname=tv_pname.getText().toString();
                        manufacture=tv_manfacture.getText().toString();
                        prate=tv_rate.getText().toString();
                        pimage=JD.getString("image");

                        try {
                            Picasso.with(Scanproduct.this)
                                    .load(path)
                                    .placeholder(R.drawable.ic_menu_camera)
                                    .error(R.drawable.ic_menu_camera).into(i1);

                        } catch (Exception e) {
                            // TODO: handle exception
                            Log.d("err333====",e.toString());
                        }
                    }catch (Exception e){
                        Log.d("err4444====",e.toString());
                    }
            }
           // pd.dismiss();
        }

        @Override
        protected String doInBackground(String... params) {
            String result="0";
            SharedPreferences sh= PreferenceManager.getDefaultSharedPreferences(Scanproduct.this);
            String login_id=sh.getString("login_id","");
            try {
                List<NameValuePair> param = new ArrayList<>();
                param.add(new BasicNameValuePair("qrcode", contents));
                param.add(new BasicNameValuePair("login_id", login_id));
                JSONParser jp = new JSONParser();
                JD = jp.makeHttpRequest(url, "GET", param);
                result = JD.getString("status");
            }
            catch(Exception ex)
            {
                Log.d("err====", ex.toString());
            }


            return result;
        }
    }

    private void addToCart(String login_id,String product_id,String rate,String discount,String pname,String pimage,String quantity){

        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        String curDate=simpleDateFormat.format(new Date());

        SQLiteDatabase sqd=openOrCreateDatabase("Shopping", SQLiteDatabase.CREATE_IF_NECESSARY,null);

        String qry = "create table if not exists cart(cart_id integer primary key autoincrement,login_id text,product_id text, product_name text,rate text,discount text, image text,quantity text,date text)";
        sqd.execSQL(qry);

        ContentValues cv=new ContentValues();
        cv.put("login_id",login_id);
        cv.put("product_id",product_id);
        cv.put("quantity",quantity);
        cv.put("product_name",pname);
        cv.put("image",pimage);
        cv.put("rate",rate);
        cv.put("discount",discount);
        cv.put("date",curDate);
        sqd.insert("cart",null,cv);
        sqd.close();
    }


    class DatauploadService2 extends AsyncTask<String,String,String>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd=new ProgressDialog(Scanproduct.this);
            pd.setMessage("Loading....");
            Log.d("ererere","1234512345");
            product_id=tv_pid.getText().toString();
          //  pd.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.equals("0")) {
               // Toast.makeText(Scanproduct.this, "Error,Try again...!", Toast.LENGTH_SHORT).show();
            } else {
                try{

                    r1.setRating(Float.parseFloat(s));
                }catch (Exception e){}
                Toast.makeText(Scanproduct.this, "Rating added Sucessfully...!", Toast.LENGTH_SHORT).show();
            }
           // pd.dismiss();
        }

        @Override
        protected String doInBackground(String... params) {

            SharedPreferences sh=PreferenceManager.getDefaultSharedPreferences(Scanproduct.this);
            login_id=sh.getString("login_id","");
            String url1= "http://"+sh.getString("ip","192.168.0.0")+"/shopping/android_connections/scan_add_rating.php";

            String result = "0";
            try {
                List<NameValuePair> para = new ArrayList<>();
                para.add(new BasicNameValuePair("login_id", login_id));
                para.add(new BasicNameValuePair("product_id", product_id));
                para.add(new BasicNameValuePair("rating", rating));

                JSONParser jsonParser = new JSONParser();
                JSONObject jsonObject=null;
                jsonObject = jsonParser.makeHttpRequest(url1, "GET", para);
                result = jsonObject.getString("rating");
            } catch (Exception ex) {
                Log.d("err==11111==", ex.toString());
            }
            return result;
        }

    }

    public  void viewRating(){

        SharedPreferences sh=PreferenceManager.getDefaultSharedPreferences(Scanproduct.this);
        login_id=sh.getString("login_id","");
        String url1= "http://"+sh.getString("ip","192.168.0.0")+"/shopping/android_connections/avg_rating.php.php";

        String result = "0";
        try {
            List<NameValuePair> para = new ArrayList<>();
            para.add(new BasicNameValuePair("product_id", product_id));

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject=null;
            jsonObject = jsonParser.makeHttpRequest(url1, "GET", para);
            result = jsonObject.getString("rating");
            r1.setRating(Float.parseFloat(result));
        } catch (Exception ex) {
            Log.d("err====", ex.toString());
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent i=new Intent(Scanproduct.this,Home.class);
        startActivity(i);
    }
}
