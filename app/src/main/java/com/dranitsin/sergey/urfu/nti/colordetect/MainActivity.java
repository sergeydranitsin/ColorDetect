package com.dranitsin.sergey.urfu.nti.colordetect;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import org.jsoup.Jsoup;

public class MainActivity extends AppCompatActivity {
    String data[];
    Integer Red;
    Integer Green;
    Integer Blue;
    Window window;
    Context context;
    public TextView NumderRGB;
    TextView NumderHEX;
    TextView NumderHSV;
    View ColorView;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);

        Red = 142;
        Green = 36;
        Blue = 170;


        NumderRGB = findViewById(R.id.textView20);
        NumderHEX = findViewById(R.id.textView22);
        //NumderHSV = findViewById(R.id.textView24);
        ColorView = findViewById(R.id.ColorView);

        @SuppressLint("HandlerLeak") final Handler handler = new Handler(){
            public void handleMessage(Message msg){
                Bundle bundle = msg.getData();
                String[] string = bundle.getStringArray("msg");

                Red = Integer.valueOf(string != null ? string[0] : null);
                Green = Integer.valueOf(string != null ? string[1] : null);
                Blue = Integer.valueOf(string != null ? string[2] : null);

                ColorView.setBackgroundColor(Color.rgb(Red,Green,Blue));
                window = getWindow();
                if(Red >16 && Green>16 && Blue>16){
                    window.setStatusBarColor(Color.rgb(Red-15,Green-15,Blue-15));
                }
                else {
                    window.setStatusBarColor(Color.rgb(Red+40,Green+40,Blue+40));
                }

                NumderRGB.setText("("+Red+","+Green+","+Blue+")");
                NumderHEX.setText("#"+Integer.toHexString(Red)+Integer.toHexString(Green)+Integer.toHexString(Blue));

            }
        };



        View.OnClickListener ClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        Message msg = handler.obtainMessage();
                        Bundle bundle = new Bundle();
                        bundle.putStringArray("msg", getColor());
                        msg.setData(bundle);
                        handler.sendMessage(msg);
                    }
                });
                t.start();
            }
        };
        ColorView.setOnClickListener(ClickListener);



        ColorView.setBackgroundColor(Color.rgb(Red,Green,Blue));
        window = getWindow();
        if(Red >16 && Green>16 && Blue>16){
            window.setStatusBarColor(Color.rgb(Red-15,Green-15,Blue-15));
        }
        else {
            window.setStatusBarColor(Color.rgb(Red+40,Green+40,Blue+40));
        }
        NumderRGB.setText("("+Red+","+Green+","+Blue+")");
        NumderHEX.setText("#"+Integer.toHexString(Red)+Integer.toHexString(Green)+Integer.toHexString(Blue));


        /*ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()){
            WifiConnetction = networkInfo.getType() == ConnectivityManager.TYPE_WIFI;
        }
        if(WifiConnetction) {}
        */



    }
    public String[] getColor(){
        try {
            Log.i("Check", "Check");
            String page = "http://192.168.4.1/data";
            org.jsoup.nodes.Document doc = Jsoup.connect(page).get();
            String body = doc.text();
            Log.i("Color", body);
            data = body.split(" ");
        } catch(Exception e) {
            e.printStackTrace();
        }
        return data;
    }
}
