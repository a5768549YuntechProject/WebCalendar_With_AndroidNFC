//定義package名稱
package com.example.express_nfc;

//匯入AlertDialog庫
import androidx.appcompat.app.AlertDialog;
//匯入AppCompatActivity庫
import androidx.appcompat.app.AppCompatActivity;

//匯入PendingIntent庫
import android.app.PendingIntent;
//匯入DialogInterface庫
import android.content.DialogInterface;
//匯入Intent庫
import android.content.Intent;
//匯入IntentFilter庫
import android.content.IntentFilter;
//匯入NfcAdapter庫
import android.nfc.NfcAdapter;
//匯入MifareClassic庫
import android.nfc.tech.MifareClassic;
//匯入Bundle庫
import android.os.Bundle;
//匯入Html庫
import android.text.Html;
//匯入View庫
import android.view.View;
//匯入WebSettings庫
import android.webkit.WebSettings;
//匯入WebView庫
import android.webkit.WebView;
//匯入WebViewClient庫
import android.webkit.WebViewClient;
//匯入Button庫
import android.widget.Button;
//匯入TextView庫
import android.widget.TextView;

//匯入volley Request支持庫
import com.android.volley.Request;
//匯入volley RequestQueue支持庫
import com.android.volley.RequestQueue;
//匯入volley Response支持庫
import com.android.volley.Response;
//匯入volley VolleyError支持庫
import com.android.volley.VolleyError;
//匯入volley toolboxJsonObjectRequest支持庫
import com.android.volley.toolbox.JsonObjectRequest;
//匯入volley toolboxStringRequest支持庫
import com.android.volley.toolbox.StringRequest;
//匯入volley toolboxVolley支持庫
import com.android.volley.toolbox.Volley;

//匯入JSONObject庫
import org.json.JSONObject;

//匯入HashMap庫
import java.util.HashMap;
//匯入Map庫
import java.util.Map;

//宣告繼承AppCompatActivity的MainActivity function
public class MainActivity extends AppCompatActivity {

    //宣告型態NfcAdapter的變數mAdapter
    private NfcAdapter mAdapter;
    //宣告型態PendingIntent的變數mPendingIntent
    private PendingIntent mPendingIntent;
    //宣告型態IntentFilter[]的變數mIntentFilters
    private IntentFilter[] mIntentFilters;
    //宣告型態MifareClassic的變數mfc
    private MifareClassic mfc;
    //宣告型態String[][]的變數mTechList
    private String[][] mTechList;
    //宣告型態StringBuilder的變數ex_id
    private StringBuilder ex_id= new StringBuilder();

    //宣告型態TextView的變數labelID
    private TextView labelID;
    //宣告型態TextView的變數idView
    private TextView idView;
    //宣告型態TextView的變數responseView
    private TextView responseView;
    //宣告型態Button的變數buttonPost
    private Button buttonPost;
    //宣告型態Button的變數buttonGetLast
    private Button buttonGetLast;
    //宣告型態Button的變數buttonGetWithID
    private Button buttonGetWithID;
    //宣告型態Button的變數buttonDelete
    private Button buttonDelete;
    //宣告型態WebView的變數listWebView
    private WebView listWebView;
    //宣告型態WebView的變數calendarWebView
    private WebView calendarWebView;

    //宣告型態String的變數ip
    public final String ip = "http://192.168.43.254:3000/api/";
    //宣告型態String的變數listIp
    public final String listIp = "http://192.168.43.254:3000/list";
    //宣告型態String的變數calendarIp
    public final String calendarIp = "http://192.168.43.254:3000/calendar";

    //宣告private的function initViews
    private void initViews() {
        //將labelID的內容指定給id為textView的畫面內容
        labelID = findViewById(R.id.textView);
        //將idView的內容指定給id為textView2的畫面內容
        idView = findViewById(R.id.textView2);
        //將responseView的內容指定給id為textView3的畫面內容
        responseView = findViewById(R.id.textView3);
        //將buttonPost的內容指定給id為button的畫面內容
        buttonPost = findViewById(R.id.button);
        //將buttonGetWithID的內容指定給id為button2的畫面內容
        buttonGetWithID = findViewById(R.id.button2);
        //將buttonGetLast的內容指定給id為button3的畫面內容
        buttonGetLast = findViewById(R.id.button3);
        //將buttonDelete的內容指定給id為button4的畫面內容
        buttonDelete = findViewById(R.id.button4);
        //將calendarWebView的內容指定給id為webView的畫面內容
        calendarWebView = findViewById(R.id.webView);
        //將listWebView的內容指定給id為webView2的畫面內容
        listWebView = findViewById(R.id.webView2);

        //設定calendarWebView的setWebViewClient(用意為在app中開啟webview，而不是使用外部瀏覽器)
        calendarWebView.setWebViewClient(new WebViewClient());
        //設定listWebView的setWebViewClient(用意為在app中開啟webview，而不是使用外部瀏覽器)
        listWebView.setWebViewClient(new WebViewClient());

        //設定listWebView的loadUrl為listIp(內容為顯示網頁網址)
        listWebView.loadUrl(listIp);
        //設定calendarWebView的loadUrl為calendarIp(內容為顯示網頁網址)
        calendarWebView.loadUrl(calendarIp);

        //宣告型態WebSettings的變數listwebSettings並指派給listWebView的getSettings
        WebSettings listwebSettings = listWebView.getSettings();
        //設定listwebSettings將JavaScript設為true(開啟js支援)
        listwebSettings.setJavaScriptEnabled(true);

        //宣告型態WebSettings的變數calendarwebSettings並指派給calendarWebView的getSettings
        WebSettings calendarwebSettings = calendarWebView.getSettings();
        //設定calendarwebSettings將JavaScript設為true(開啟js支援)
        calendarwebSettings.setJavaScriptEnabled(true);
    }

    //宣告public的function scannedTag，要帶入型態為String的變數tag
    public boolean scannedTag(String tag){
        //宣告型態boolean的變數returnValue，預設值為true
        boolean returnValue = true;
        //若變數tag的值為000000(沒有感測任何tag)
        if(tag.equals("00000000")){
            //將returnValue設為false
            returnValue = false;
        }
        //回傳returnValue
        return returnValue;
    }

    //宣告public的function alertMessage，要帶入型態為String的變數title、型態為String的變數message
    public void alertMessage(String title,String message){
        //宣告型態AlertDialog.Builder的變數alertDialog
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        //設定alertDialog的tiile為變數title
        alertDialog.setTitle(title);
        //設定alertDialog的message為變數message
        alertDialog.setMessage(message);
        //設定alertDialog的畫面按鈕樣式
        alertDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
            //設定空宣告，實際上無作為
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        //設定alertDialog的取消模式為false
        alertDialog.setCancelable(false);
        //顯示alertDialog
        alertDialog.show();
    }

    //宣告public的function reflashList，要帶入型態為String的變數ip
    public void reflashList(String ip){
        //設定listWebView的loadUrl為變數ip
        listWebView.loadUrl(ip);
    }

    //宣告public的function reflashCalendar，要帶入型態為String的變數ip
    public void reflashCalendar(String ip){
        //設定calendarWebView的loadUrl為變數ip
        calendarWebView.loadUrl(ip);
    }

    //以下的子類別和父類別的方法一樣
    @Override
    //宣告protected的function onCreate，要帶入型態為Bundle的savedInstanceState
    protected void onCreate(Bundle savedInstanceState) {
        //呼叫父類別執行onCreate
        super.onCreate(savedInstanceState);
        //設定畫面為R.layout.activity_main
        setContentView(R.layout.activity_main);

        //執行initViews
        initViews();

        //將mAdapter指派為NfcAdapter的getDefaultAdapter(尋找預設的NFC晶片)
        mAdapter = NfcAdapter.getDefaultAdapter(this);
        //若mAdapter等於null(晶片不支援或不存在)
        if (mAdapter == null) {
            //結束程式
            this.finish();
            return;
        }

        //重啟Activity的方式為FLAG_ACTIVITY_SINGLE_TOP(當activity處於task頂部時，可以直接取代，直接onNewIntent)
        mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        //宣告型態IntentFilter的變數filter，篩出NfcAdapter.ACTION_TECH_DISCOVERED
        IntentFilter filter = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
        try {
            //加上datatype為*/*
            filter.addDataType("*/*");
        } catch (IntentFilter.MalformedMimeTypeException e) {
            //將取得的資料寫入logcat
            e.printStackTrace();
        }
        //宣告型態IntentFilter[]的變數，將filter資料放入，並指派給mIntentFilters
        mIntentFilters = new IntentFilter[]{filter};
        //宣告型態String[][]的變數，將ifareClassic.class.getName()資料放入，並指派給mTechList
        mTechList = new String[][]{new String[]{MifareClassic.class.getName()}}; // 只處理 MifareClassic 的 tag

        //設定buttonPost的setOnClickListener(按鈕事件)
        buttonPost.setOnClickListener(new View.OnClickListener() {
            //以下的子類別和父類別的方法一樣
            @Override
            //宣告public的function onClick，要帶入型態為View的v
            public void onClick(View v) {
                //若沒有感測到tag
                if(!scannedTag(idView.getText().toString())){
                    //發出alert
                    alertMessage("錯誤訊息","未感測到任何卡片，請感測卡片。");
                    //跳過此次事件
                    return;
                }
                //宣告型態為RequestQueue的queue，並指派為Volley的newRequestQueue
                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);

                //宣告型態為String的url，並預設值為變數ip+schedules(api路徑)
                String url = ip + "schedules";  // Request a string response

                //宣告型態為Map<String, String> 的map
                Map<String, String> map = new HashMap<>();
                //放入值到map中
                map.put("cardID", idView.getText().toString());

                //將map的資料轉為JSON格式
                JSONObject postData = new JSONObject(map);

                //宣告型態為JsonObjectRequest的jsonObjectRequest(Request格式為POST,帶入url及postData)
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postData, new Response.Listener<JSONObject>() {
                    //以下的子類別和父類別的方法一樣
                    @Override
                    //宣告public的function onResponse，要帶入型態為JSONObject的r(當取得資料時)
                    public void onResponse(JSONObject r) {
                        //將取得的資料寫入logcat
                        System.out.println(r);
                        //將取得的資料寫入responseView
                        responseView.setText("Http response about getting a web page " + url + " is:" + Html.fromHtml("<br><br>" + r));
                    }
                }, new Response.ErrorListener() {
                    //以下的子類別和父類別的方法一樣
                    @Override
                    //宣告public的function onErrorResponse，要帶入型態為VolleyError的error(當request失敗時)
                    public void onErrorResponse(VolleyError error) {
                        //設定responseView的text為Something went wrong!
                        responseView.setText("Something went wrong!");
                        //將取得的資料寫入logcat
                        error.printStackTrace();
                    }
                });
                //發送request
                queue.add(jsonObjectRequest);
                //更新list畫面
                reflashList(listIp);
                //更新calendar畫面
                reflashCalendar(calendarIp);
            } //onClick(View v)
        }); // buttonGetLast.setOnClickListener(…)

        //設定buttonGetWithID的setOnClickListener(按鈕事件)
        buttonGetWithID.setOnClickListener(new View.OnClickListener() {
            //以下的子類別和父類別的方法一樣
            @Override
            //宣告public的function onClick，要帶入型態為View的v
            public void onClick(View v) {
                //宣告型態為String的url，並預設值為變數ip+schedules(api路徑)
                String url = ip + "schedules";  // Request a string response
                //宣告型態為StringRequest的stringRequest(Request格式為GET,帶入url)
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            //以下的子類別和父類別的方法一樣
                            @Override
                            //宣告public的function onResponse，要帶入型態為String的response(當取得資料時)
                            public void onResponse(String response) { // Result handling
                                //將取得的資料寫入responseView
                                responseView.setText(response);
                            }
                        }, new Response.ErrorListener() {
                    //以下的子類別和父類別的方法一樣
                    @Override
                    //宣告public的function onErrorResponse，要帶入型態為VolleyError的error(當request失敗時)
                    public void onErrorResponse(VolleyError error) {// Error handling
                        //設定responseView的text為Something went wrong!
                        responseView.setText("Something went wrong!");
                        //將取得的資料寫入logcat
                        error.printStackTrace();
                    }
                });// Add the request to the queue
                //發送request
                Volley.newRequestQueue(MainActivity.this).add(stringRequest);
                //更新list畫面
                reflashList(listIp);
                //更新calendar畫面
                reflashCalendar(calendarIp);
            } //onClick(View v)
        }); // buttonGetLast.setOnClickListener(…)

        //設定buttonGetLast的setOnClickListener(按鈕事件)
        buttonGetLast.setOnClickListener(new View.OnClickListener() {
            //以下的子類別和父類別的方法一樣
            @Override
            //宣告public的function onClick，要帶入型態為View的v
            public void onClick(View v) {
                //宣告型態為String的url，並預設值為變數ip+schedules/last(api路徑)
                String url = ip + "schedules/last";  // Request a string response
                //宣告型態為StringRequest的stringRequest(Request格式為GET,帶入url)
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            //以下的子類別和父類別的方法一樣
                            @Override
                            //宣告public的function onResponse，要帶入型態為String的response(當取得資料時)
                            public void onResponse(String response) { // Result handling
                                //將取得的資料寫入responseView
                                responseView.setText(response);
                            }
                        }, new Response.ErrorListener() {
                    //以下的子類別和父類別的方法一樣
                    @Override
                    //宣告public的function onErrorResponse，要帶入型態為VolleyError的error(當request失敗時)
                    public void onErrorResponse(VolleyError error) {// Error handling
                        //設定responseView的text為Something went wrong!
                        responseView.setText("Something went wrong!");
                        //將取得的資料寫入logcat
                        error.printStackTrace();
                    }
                });// Add the request to the queue
                //發送request
                Volley.newRequestQueue(MainActivity.this).add(stringRequest);
            } //onClick(View v)
        }); // buttonGetLast.setOnClickListener(…)

        //設定buttonDelete的setOnClickListener(按鈕事件)
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            //以下的子類別和父類別的方法一樣
            @Override
            //宣告public的function onClick，要帶入型態為View的v
            public void onClick(View v) {
                //宣告型態為String的url，並預設值為變數ip+schedules(api路徑)
                String url = ip + "schedules";  // Request a string response
                //宣告型態為StringRequest的stringRequest(Request格式為DELETE,帶入url)
                StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url,
                        new Response.Listener<String>() {
                            //以下的子類別和父類別的方法一樣
                            @Override
                            //宣告public的function onResponse，要帶入型態為String的response(當取得資料時)
                            public void onResponse(String response) { // Result handling
                                //將取得的資料寫入responseView
                                responseView.setText(response);
                            }
                        }, new Response.ErrorListener() {
                    //以下的子類別和父類別的方法一樣
                    @Override
                    //宣告public的function onErrorResponse，要帶入型態為VolleyError的error(當request失敗時)
                    public void onErrorResponse(VolleyError error) {// Error handling
                        //設定responseView的text為Something went wrong!
                        responseView.setText("Something went wrong!");
                        //將取得的資料寫入logcat
                        error.printStackTrace();
                    }
                });// Add the request to the queue
                //發送request
                Volley.newRequestQueue(MainActivity.this).add(stringRequest);
                //更新list畫面
                reflashList(listIp);
                //更新calendar畫面
                reflashCalendar(calendarIp);
            } //onClick(View v)
        }); // buttonGetLast.setOnClickListener(…)
    }

    //暫停後復原方法
    protected void onResume() {
        super.onResume();
        //啟用NFC感應
        mAdapter.enableForegroundDispatch(this, mPendingIntent,mIntentFilters, mTechList);
    }
    //暫停方法
    protected void onPause() {
        super.onPause();
        //停用NFC感應
        mAdapter.disableForegroundDispatch(this);
    }

    //宣告protected的function onNewIntent，要帶入型態為Intent的變數intent
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //若沒有感測到，就結束這次事件
        if (!NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction())) {
            return;
        }
        //宣告型態為byte[]的myNFCID，取得EXTRA_ID
        byte[] myNFCID = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
        //呼叫Converter的getHexString，處理完後指派給ex_id
        ex_id = Converter.getHexString(myNFCID, myNFCID.length);
        //將id顯示到idView上
        idView.setText(ex_id); //只讀 tag 的 id
        //responseView的文字設為Tag is detected!
        responseView.setText("Tag is detected!");
    }
}