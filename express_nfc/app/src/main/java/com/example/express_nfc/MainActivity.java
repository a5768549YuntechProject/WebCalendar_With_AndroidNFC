package com.example.express_nfc;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.tech.MifareClassic;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private NfcAdapter mAdapter;
    private PendingIntent mPendingIntent;
    private IntentFilter[] mIntentFilters;
    private MifareClassic mfc;
    private String[][] mTechList;
    private StringBuilder ex_id= new StringBuilder();

    private TextView labelID;
    private TextView idView;
    private TextView responseView;
    private Button buttonPost;
    private Button buttonGetLast;
    private Button buttonGetWithID;
    private Button buttonDelete;
    private WebView listWebView;
    private WebView calendarWebView;



    public final String ip = "http://127.0.0.1:3000/";
    public final String listIp = "https://google.com";
    public final String calendarIp = "https://google.com";

    private void initViews() {
        labelID = findViewById(R.id.textView);
        idView = findViewById(R.id.textView2);
        responseView = findViewById(R.id.textView3);
        buttonPost = findViewById(R.id.button);
        buttonGetWithID = findViewById(R.id.button2);
        buttonGetLast = findViewById(R.id.button3);
        buttonDelete = findViewById(R.id.button4);
        calendarWebView = findViewById(R.id.webView);
        listWebView = findViewById(R.id.webView2);
        calendarWebView.setWebViewClient(new WebViewClient());
        listWebView.setWebViewClient(new WebViewClient());
        listWebView.loadUrl(listIp);
        calendarWebView.loadUrl(calendarIp);
    }

    public boolean scannedTag(String tag){
        boolean returnValue = true;
        if(tag.equals("00000000")){
            returnValue = false;
        }
        return returnValue;
    }

    public void alertMessage(String title,String message){
        AlertDialog.Builder alertDialog =
                new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    public void reflashList(String ip){
        listWebView.loadUrl(ip);
    }

    public void reflashCalendar(String ip){
        calendarWebView.loadUrl(ip);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        mAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mAdapter == null) {
            this.finish();
            return;
        }
        mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        IntentFilter filter = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
        try {
            filter.addDataType("*/*");
        } catch (IntentFilter.MalformedMimeTypeException e) {
            e.printStackTrace();
        }
        mIntentFilters = new IntentFilter[]{filter};
        mTechList = new String[][]{new String[]{MifareClassic.class.getName()}}; // 只處理 MifareClassic 的 tag

        buttonPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!scannedTag(idView.getText().toString())){
                    alertMessage("錯誤訊息","未感測到任何卡片，請感測卡片。");
                    return;
                }
                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                String url = ip + "schedules";  // Request a string response

                Map<String, String> map = new HashMap<>();
                map.put("tagid", idView.getText().toString());

                JSONObject postData = new JSONObject(map);

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postData, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject r) {
                        System.out.println(r);
                        responseView.setText("Http response about getting a web page " + url + " is:" + Html.fromHtml("<br><br>" + r));
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        responseView.setText("Something went wrong!");
                        error.printStackTrace();
                    }
                });
                queue.add(jsonObjectRequest);
            } //onClick(View v)
        }); // buttonGetLast.setOnClickListener(…)

        buttonGetWithID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = ip + "schedules";  // Request a string response
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) { // Result handling
                                responseView.setText(response);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {// Error handling
                        responseView.setText("Something went wrong!");
                        error.printStackTrace();
                    }
                });// Add the request to the queue
                Volley.newRequestQueue(MainActivity.this).add(stringRequest);
            } //onClick(View v)
        }); // buttonGetLast.setOnClickListener(…)

        buttonGetLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = ip + "schedules/last";  // Request a string response
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) { // Result handling
                                responseView.setText(response);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {// Error handling
                        responseView.setText("Something went wrong!");
                        error.printStackTrace();
                    }
                });// Add the request to the queue
                Volley.newRequestQueue(MainActivity.this).add(stringRequest);
            } //onClick(View v)
        }); // buttonGetLast.setOnClickListener(…)

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = ip + "schedules/last";  // Request a string response
                StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) { // Result handling
                                responseView.setText(response);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {// Error handling
                        responseView.setText("Something went wrong!");
                        error.printStackTrace();
                    }
                });// Add the request to the queue
                Volley.newRequestQueue(MainActivity.this).add(stringRequest);
            } //onClick(View v)
        }); // buttonGetLast.setOnClickListener(…)
    }

    protected void onResume() {
        super.onResume();
        mAdapter.enableForegroundDispatch(this, mPendingIntent,mIntentFilters, mTechList);
    }

    protected void onPause() {
        super.onPause();
        mAdapter.disableForegroundDispatch(this);
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (!NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction())) {
            return;
        }
        byte[] myNFCID = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
        ex_id = Converter.getHexString(myNFCID, myNFCID.length);
        idView.setText(ex_id); //只讀 tag 的 id
        responseView.setText("Tag is detected!");
    }
}