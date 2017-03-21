package com.cjcp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.nhn.android.maps.NMapActivity;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapOverlayItem;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.nmapmodel.NMapError;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;
import com.tsengvn.typekit.Typekit;
import com.tsengvn.typekit.TypekitContextWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CountDownLatch;

public class DetailViewActivity extends NMapActivity implements AdapterView.OnItemClickListener, NMapView.OnMapViewTouchEventListener, NMapView.OnMapStateChangeListener{
    private static final String TAG = "DETAILVIEW";
    private static final String TAG_RESULTS = "result";
    private final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1000;
    private static final String URL = "http://cjcp.kr/data/file/main/";
    private final String CLIENT_ID = "vkNK6LjTdUleRIps7Gc1";
    private NMapView mMapView = null;
    private NMapController nMapController;
    private NMapOverlayItem mapItem;

    private String myJSON,myJSONReply, callNum, wr_subject,wr_file1,wr_file2,wr_file3,wr_file4,wr_file5,wr_file6,addr1,addr2,open1,open2,holiday,etc1,etc2,etc3,lat,lng,wr_id,wr_4;
    private String[] priceTxt, priceTotal,priceDetail;
    private LinearLayout priceView,imageArea,captionArea,webViewArea;
    private TextView subject,telView,open1View,open2View,holidayView,addr1View,addr2View,caption;
    private ImageView sampleImg;
    public double lats;
    public double lngs;
    private WebView mapView,imageWebView;
    private Button callBtn,replyBtn;
    int width;
    GetDataJSON g;
    GetDataJSONReply gReply;
    ScrollView mainView;
    Toolbar toolbar;
    ListView list;
    String userName,starRank,wrContent,signDate,wrCommentReply ,starrank,replyPassword,replyContents;
    JSONArray peoples = null;
    TextView starRankSelect;
    EditText replyPass,replyContent;
    Spinner starRankSpinner;
    Button replySendBtn;
    HttpTask httpTask;
    String[] starRankSel = {"★★★★★","★★★★☆","★★★☆☆","★★☆☆☆","★☆☆☆☆","☆☆☆☆☆"};
    CountDownLatch latch ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view);
        latch = new CountDownLatch(20);
        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
        width = dm.widthPixels;

        final Intent intent = getIntent();
        wr_id = intent.getStringExtra("wr_id");
        lats = intent.getDoubleExtra("lat",lats);
        lngs = intent.getDoubleExtra("lng",lngs);
        initView();
        getData("http://cjcp.kr/android_view.php?wr_id=" + wr_id);

        imageWebView = (WebView)findViewById(R.id.subImage);
        getDataReply("http://cjcp.kr/android_reply.php?wr_id=" + wr_id);

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.starrank_item,starRankSel);

        adapter.setDropDownViewResource(R.layout.starrank_drop_item);
        starRankSpinner.setAdapter(adapter);

        starRankSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                starRankSelect.setText(adapter.getItem(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        replySendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                starrank = String.valueOf(starRankSelect.getText());
                //replyPassword = String.valueOf(replyPass.getText());
                replyContents = String.valueOf(replyContent.getText());
                switch (starrank){
                    case "★★★★★":
                        starrank = "5";
                        break;
                    case "★★★★☆":
                        starrank = "4";
                        break;
                    case "★★★☆☆":
                        starrank = "3";
                        break;
                    case "★★☆☆☆":
                        starrank = "2";
                        break;
                    case "★☆☆☆☆":
                        starrank = "1";
                        break;
                    case "☆☆☆☆☆":
                        starrank = "0";
                        break;
                }
                /*if(replyPassword.length()==0){
                    Toast.makeText(DetailViewActivity.this, "패스워드를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }else*/ if(replyContent.length()==0){
                    Toast.makeText(DetailViewActivity.this, "댓글 내용을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }else{
                    httpTask = new HttpTask();
                    try {
                        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB) {
                            httpTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        }else {
                            httpTask.execute();
                        }
                    } catch (Exception e) {

                    }
                }

            }
        });

        mMapView = (NMapView) findViewById(R.id.mMapView);

        mMapView.setClientId(CLIENT_ID);
        mMapView.setOnMapViewTouchEventListener(this);
        mMapView.setOnMapStateChangeListener(this);
        mMapView.setClickable(true);
        mMapView.setScalingFactor(3.0f,true);
        nMapController = mMapView.getMapController();
        NMapViewerResourceProvider nMapViewerResourceProvider = new NMapViewerResourceProvider(this);

        NMapOverlayManager mapOverlayManager = new NMapOverlayManager(this,mMapView, nMapViewerResourceProvider);

        int markId = NMapPOIflagType.PIN;

        NMapPOIdata data = new NMapPOIdata(1, nMapViewerResourceProvider);
        data.beginPOIdata(1);
        data.addPOIitem(lngs,lats,"",markId,0);
        data.endPOIdata();

        NMapPOIdataOverlay poIdataOverlay = mapOverlayManager.createPOIdataOverlay(data,null);

        poIdataOverlay.showAllPOIdata(0);

    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if(g.getStatus() == AsyncTask.Status.RUNNING){
                    g.cancel(true);
                }
                finish();
                return true;
            case R.id.action_call:
                callPhone();
                break;
        }
        return super.onOptionsItemSelected(item);
   }

    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case MY_PERMISSIONS_REQUEST_CALL_PHONE: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    callPhone();
                } else {
                    Toast.makeText(this, "NOT GRANTED", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_call,menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void getData(String url){
        g = new GetDataJSON();
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB) {
            g.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,url);
        }else {
            g.execute();
        }
    }

    @Override
    public void onLongPress(NMapView nMapView, MotionEvent motionEvent) {
    }

    @Override
    public void onLongPressCanceled(NMapView nMapView) {
    }

    @Override
    public void onTouchDown(NMapView nMapView, MotionEvent motionEvent) {
        Log.d(TAG, "onTouchDown: ");
        nMapView.requestDisallowInterceptTouchEvent(false);
        mainView.requestDisallowInterceptTouchEvent(true);
    }

    @Override
    public void onTouchUp(NMapView nMapView, MotionEvent motionEvent) {
        Log.d(TAG, "onTouchUp: ");
        mainView.requestDisallowInterceptTouchEvent(true);
        nMapView.requestDisallowInterceptTouchEvent(false);
    }

    @Override
    public void onScroll(NMapView nMapView, MotionEvent motionEvent, MotionEvent motionEvent1) {
        Log.d(TAG, "onScroll: ");
        nMapView.requestDisallowInterceptTouchEvent(false);
        mainView.requestDisallowInterceptTouchEvent(false);
    }

    @Override
    public void onSingleTapUp(NMapView nMapView, MotionEvent motionEvent) {
        Log.d(TAG, "onSingleTapUp: ");
    }

    @Override
    public void onMapInitHandler(NMapView nMapView, NMapError nMapError) {
        if (nMapError == null) { // success
            nMapController.setMapCenter(new NGeoPoint(lngs, lats), 10);
        } else { // fail
            Log.e(TAG, "onMapInitHandler: error=" + nMapError.toString());
        }
    }

    @Override
    public void onMapCenterChange(NMapView nMapView, NGeoPoint nGeoPoint) {
        Log.d(TAG, "onMapCenterChange: ");
        nMapView.requestDisallowInterceptTouchEvent(false);
        nMapController.setMapCenter(new NGeoPoint(lngs, lats), nMapController.getZoomLevel());
    }

    @Override
    public void onMapCenterChangeFine(NMapView nMapView) {
        Log.d(TAG, "onMapCenterChangeFine: ");
    }

    @Override
    public void onZoomLevelChange(NMapView nMapView, int i) {
        Log.d(TAG, "onZoomLevelChange: ");
    }

    @Override
    public void onAnimationStateChange(NMapView nMapView, int i, int i1) {
        Log.d(TAG, "onAnimationStateChange: ");
    }

    public class GetDataJSON extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            String uri = params[0];

            BufferedReader bufferedReader = null;
            try {
                java.net.URL url = new URL(uri);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();

                StringBuilder sb = new StringBuilder();

                bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream(),"UTF-8"));

                String json;
                while((json = bufferedReader.readLine())!= null){
                    sb.append(json+"\n");
                }
                return sb.toString().trim();

            }catch(Exception e){
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result){
            myJSON=result;
            setData();
        }
    }

    @SuppressLint("NewApi")
    protected void setData(){
        priceView = (LinearLayout) findViewById(R.id.priceView);
        imageArea = (LinearLayout) findViewById(R.id.imageArea);
        telView = (TextView) findViewById(R.id.tel);
        open1View = (TextView) findViewById(R.id.open1);
        open2View = (TextView) findViewById(R.id.open2);
        holidayView = (TextView) findViewById(R.id.holiday);
        addr1View = (TextView) findViewById(R.id.addr1);
        addr2View = (TextView) findViewById(R.id.addr2);
        subject = (TextView) findViewById(R.id.subject);
        caption = (TextView) findViewById(R.id.captionDesc);
        captionArea = (LinearLayout) findViewById(R.id.captionArea);
        callBtn = (Button) findViewById(R.id.callBtn);
        sampleImg = (ImageView) findViewById(R.id.sampleImg);
        Glide.with(this).load(R.mipmap.sample).into(sampleImg);
        try {
            JSONArray jsonArray = new JSONArray(myJSON);

            for(int i = 0 ; i<jsonArray.length() ; i++){
                JSONObject c = jsonArray.getJSONObject(i);
                wr_subject = c.getString("wr_subject");
                callNum = c.getString("tel");
                priceTxt = c.getString("pricetext").split("@");
                priceTotal = c.getString("totalPrice").split("@");
                priceDetail = c.getString("priceDetail").split("@");
                wr_file1 = c.getString("wr_file1");
                wr_file2 = c.getString("wr_file2");
                wr_file3 = c.getString("wr_file3");
                wr_file4 = c.getString("wr_file4");
                wr_file5 = c.getString("wr_file5");
                wr_file6 = c.getString("wr_file6");
                addr1 = c.getString("addr1");
                addr2 = c.getString("addr2");
                open1 = c.getString("open01");
                open2 = c.getString("open02");
                holiday = c.getString("holiday");
                etc1 = c.getString("etc1");
                etc2 = c.getString("etc2");
                etc3 = c.getString("etc3");
                lat = c.getString("latitude");
                lng = c.getString("longitude");
                wr_4 = c.getString("views");
            }
            /*ActionBar actionBar = getActionBar();
            actionBar.setTitle(wr_subject);*/

            subject.setText(wr_subject);
            telView.setText("전  화 : "+callNum);
            open1View.setText("주  중 : "+open1);
            open2View.setText("주  말 : "+open2);
            holidayView.setText("휴  일 : "+holiday);
            addr1View.setText("주소(구) : "+addr1);
            addr2View.setText("주소(신) : "+addr2);
            caption.setText(etc2);

            callBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callPhone();
                }
            });

            if(etc3.length()!=0){
                Button linkBtn = new Button(DetailViewActivity.this);
                linkBtn.setText(etc3);
                linkBtn.setBackgroundResource(R.color.colorPrimaryDark);
                linkBtn.setTextColor(Color.WHITE);
                linkBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        Uri u = Uri.parse(etc1);
                        i.setData(u);
                        startActivity(i);
                    }
                });
                captionArea.addView(linkBtn);
            }
            DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();

            int width = dm.widthPixels;
            if(wr_file1.length()!=0){
                ImageView imageView = (ImageView) findViewById(R.id.thumbnail);
                ViewGroup.LayoutParams param = imageView.getLayoutParams();
                param.width = (int) (width/1.6);
                imageView.setLayoutParams(param);
                Glide.with(this).load(URL+wr_file1).into(imageView);
            }
            if(wr_file2.length() !=0 && wr_file3.length() ==0){
                LinearLayout linearLayout = (LinearLayout) findViewById(R.id.thumbArea1);
                linearLayout.setVisibility(View.VISIBLE);
                ImageView imageView = (ImageView) findViewById(R.id.thumbnail1);
                Glide.with(this).load(URL+wr_file2).into(imageView);
            }if(wr_file2.length() !=0 && wr_file3.length() !=0){
                LinearLayout linearLayout = (LinearLayout) findViewById(R.id.thumbArea1);
                linearLayout.setVisibility(View.VISIBLE);
                ImageView imageView = (ImageView) findViewById(R.id.thumbnail1);
                ImageView imageView2 = (ImageView) findViewById(R.id.thumbnail2);
                Glide.with(this).load(URL+wr_file2).into(imageView);
                Glide.with(this).load(URL+wr_file3).into(imageView2);
            }
            if(wr_file4.length() !=0 && wr_file5.length() ==0){
                LinearLayout linearLayout = (LinearLayout) findViewById(R.id.thumbArea2);
                linearLayout.setVisibility(View.VISIBLE);
                ImageView imageView = (ImageView) findViewById(R.id.thumbnail3);
                Glide.with(this).load(URL+wr_file4).into(imageView);
            }if(wr_file4.length() !=0 && wr_file5.length() !=0){
                LinearLayout linearLayout = (LinearLayout) findViewById(R.id.thumbArea2);
                linearLayout.setVisibility(View.VISIBLE);
                ImageView imageView = (ImageView) findViewById(R.id.thumbnail3);
                ImageView imageView2 = (ImageView) findViewById(R.id.thumbnail4);
                Glide.with(this).load(URL+wr_file4).into(imageView);
                Glide.with(this).load(URL+wr_file5).into(imageView2);
            }
            webViewArea = (LinearLayout) findViewById(R.id.webViewArea);
            if("x".equals(wr_file6)){
                webViewArea.setVisibility(View.GONE);
            }else{
                WebViewInit(imageWebView);
                webViewArea.setVisibility(View.VISIBLE);
                imageWebView.loadUrl(URL+wr_file6);
            }
            Log.d(TAG, "setData: wr_4 ? " + wr_4);
            if(!wr_4.equals("1")) {
                for (int j = 0; j< priceTxt.length; j++) {
                    TextView tv1 = new TextView(DetailViewActivity.this);
                    LinearLayout lv1 = new LinearLayout(DetailViewActivity.this);
                    lv1.setOrientation(LinearLayout.HORIZONTAL);
                    tv1.setText(priceTxt[j]);
                    tv1.setTextColor(Color.BLACK);
                    tv1.setTextSize(12);
                    tv1.setEllipsize(TextUtils.TruncateAt.END);
                    tv1.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
                    tv1.setGravity(Gravity.FILL_VERTICAL);
                    tv1.setTypeface(Typekit.createFromAsset(this,"Roboto-Regular.ttf"));

                    TextView tv2 = new TextView(DetailViewActivity.this);
                    TextView tv3 = new TextView(DetailViewActivity.this);
                    TextView tv4 = new TextView(DetailViewActivity.this);

                    tv2.setText(priceTotal[j]);
                    tv2.setTextColor(Color.parseColor("#CCCCCC"));
                    tv2.setTextSize(12);
                    tv2.setGravity(Gravity.RIGHT|Gravity.CENTER_VERTICAL);
                    tv2.setPaintFlags(tv2.getPaintFlags()|Paint.STRIKE_THRU_TEXT_FLAG);
                    tv2.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT,1f));
                    tv2.setTypeface(Typekit.createFromAsset(this,"Roboto-Regular.ttf"));
                    tv4.setText(" → ");
                    tv4.setTextColor(Color.parseColor("#CCCCCC"));
                    tv4.setTextSize(12);
                    tv4.setGravity(Gravity.RIGHT|Gravity.CENTER_VERTICAL);
                    tv4.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT,1.9f));
                    tv4.setTypeface(Typekit.createFromAsset(this,"Roboto-Regular.ttf"));
                    tv3.setText(priceDetail[j]);
                    tv3.setTextColor(getResources().getColor(R.color.colorPrimary));
                    tv3.setTextSize(14);
                    tv3.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
                    tv3.setTypeface(null, Typeface.BOLD);
                    tv3.setGravity(Gravity.RIGHT|Gravity.CENTER_VERTICAL);
                    tv3.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT,1.2f));
                    tv3.setTypeface(Typekit.createFromAsset(this,"Roboto-Regular.ttf"));
                    LinearLayout priceList = new LinearLayout(DetailViewActivity.this);
                    priceList.setOrientation(LinearLayout.VERTICAL);
                    priceList.setBackgroundResource(R.drawable.title_line);
                    if(width > 480) {
                        tv1.setPadding(0, 20, 0, 20);
                        priceList.setPadding(10,30,10,30);
                    }else{
                        tv1.setPadding(0, 8, 0, 8);
                        priceList.setPadding(5,10,5,10);
                    }
                    priceList.addView(tv1);
                    lv1.addView(tv2);
                    lv1.addView(tv4);
                    lv1.addView(tv3);
                    priceList.addView(lv1);
                    priceView.addView(priceList);
                    //priceView.addView(lv1);
                }
            }else{
                for (int j= 0; j < priceTxt.length; j++) {
                    TextView tv1 = new TextView(DetailViewActivity.this);
                    tv1.setText(priceTxt[j]);
                    tv1.setTextColor(Color.BLACK);
                    tv1.setTextSize(13);
                    tv1.setWidth(priceView.getWidth());
                    tv1.setMaxLines(1);
                    tv1.setEllipsize(TextUtils.TruncateAt.END);
                    //tv1.setPadding(10, 10, 10, 10);
                    tv1.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    tv1.setGravity(Gravity.CENTER_VERTICAL);
                    tv1.setTypeface(Typekit.createFromAsset(this,"Roboto-Regular.ttf"));
                    LinearLayout priecList = new LinearLayout(DetailViewActivity.this);
                    priecList.setBackgroundResource(R.drawable.title_line);
                    priecList.setPadding(10,25,10,25);
                    priecList.addView(tv1);
                    priceView.addView(priecList);
                }
            }
            lats = Double.parseDouble(lat);
            lngs = Double.parseDouble(lng);
            toolbar.setTitle(wr_subject);
            toolbar.setNavigationIcon(R.mipmap.ic_close);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            /*mapView = (WebView) findViewById(R.id.mapView);
            WebViewInit(mapView);
            mapView.loadUrl("http://cjcp.kr/android_map.php?lat="+lat+"&lng="+lng);*/
            g.cancel(true);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void callPhone() {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + callNum));
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
        } else {
            startActivity(intent);
        }
    }

    @SuppressLint("JavascriptInterface")
    public void WebViewInit(WebView webView){
        webView.setWebChromeClient(new WebChromeClient());
        WebSettings webSetting = webView.getSettings();
        webSetting.setSupportZoom(true);
        webSetting.setBuiltInZoomControls(true);
        webSetting.setDisplayZoomControls(false);
        webSetting.setUseWideViewPort(true);
        webSetting.setLoadWithOverviewMode(true);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setJavaScriptEnabled(true);
        webSetting.setCacheMode(WebSettings.LOAD_NO_CACHE);
        //webView.addJavascriptInterface(new MainActivity.AndroidBridge(), "android");
    }

    class HttpTask extends AsyncTask<String,Void,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            startActivity(new Intent(DetailViewActivity.this,DialogReply.class));
        }

        @Override
        protected void onCancelled(String s) {
            super.onCancelled(s);
        }

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection conn = null;
            try {
                URL url = new URL("http://cjcp.kr/m/bbs/write_comment_update.php");
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setConnectTimeout(60);
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));

                writer.write(
                        "&bo_table=main&wr_name=익명&wr_content="+replyContents+"&wr_password=dmd551221&wr_3="+starrank+"&w=c&wr_id="+wr_id
                );
                writer.flush();
                writer.close();
                os.close();

                conn.connect();

                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));

                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = br.readLine()) != null){
                    if(sb.length() > 0){
                        sb.append("\n");
                    }
                    sb.append(line);
                }
                Log.d(TAG, "doInBackground: " + sb.toString());
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                if(conn!=null)
                    conn.disconnect();
            }
            return null;
        }
    }

    @Override
    protected void onPostResume() {
        getDataReply("http://cjcp.kr/android_reply.php?wr_id=" + wr_id);
        super.onPostResume();
    }

    public void initView(){
        mainView = (ScrollView)findViewById(R.id.activity_detail_view);
        list = (ListView)findViewById(R.id.replyList);
        toolbar = (Toolbar) findViewById(R.id.toolbar4);
        starRankSelect = (TextView) findViewById(R.id.starRankSelect);
        starRankSpinner = (Spinner) findViewById(R.id.starRankSpinner);
        replySendBtn = (Button) findViewById(R.id.replySendBtn);
        replyPass = (EditText) findViewById(R.id.replyPass);
        replyContent = (EditText) findViewById(R.id.replyContent);
    }

    public void getDataReply(String url){
        gReply = new GetDataJSONReply();
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB) {
            gReply.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,url);
        }else {
            gReply.execute();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        starRankSelect.setText(starRankSel[position]);
    }

    public class GetDataJSONReply extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            String uri = params[0];

            BufferedReader bufferedReader = null;
            try {
                java.net.URL url = new URL(uri);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();

                StringBuilder sb = new StringBuilder();

                bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream(),"UTF-8"));

                String json;
                while((json = bufferedReader.readLine())!= null){
                    sb.append(json+"\n");
                }
                return sb.toString().trim();

            }catch(Exception e){
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result){
            myJSONReply=result;
            setDataReply();
        }
    }

    @SuppressLint("NewApi")
    protected void setDataReply(){
        ReplyListViewAdapter adapter = new ReplyListViewAdapter();

        DisplayMetrics dm = getResources().getDisplayMetrics();
        int size = Math.round(5 * dm.density);

        list.setAdapter(adapter);
        list.setDividerHeight(0);
        try {
            JSONObject jsonObj = new JSONObject(myJSONReply);
            peoples = jsonObj.getJSONArray("result");

            for(int i = 0 ; i<peoples.length() ; i++){
                JSONObject c = peoples.getJSONObject(i);
                userName = c.getString("user");
                starRank = c.getString("starRank");
                signDate = c.getString("signDate");
                wrContent = c.getString("wr_content");
                wrCommentReply = c.getString("wr_comment_reply");
                adapter.addItem(userName,signDate,wrContent,starRank,wrCommentReply,size);
            }
            setListViewHeightBasedOnChildren(list);

        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ReplyListViewAdapter listAdapter = (ReplyListViewAdapter) listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);

        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

       ViewGroup.LayoutParams params = listView.getLayoutParams();
        /*DisplayMetrics dm = listView.getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        if(width>480) {
            params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1)) + 600;
        }else{
            params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1))+ 200;
        }*/
        params.height = totalHeight + (listView.getDividerHeight() * listAdapter.getCount())+20;
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }
}