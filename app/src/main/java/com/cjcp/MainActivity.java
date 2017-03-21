package com.cjcp;

import android.*;
import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.annotation.ColorInt;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.tsengvn.typekit.Typekit;
import com.tsengvn.typekit.TypekitContextWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

@SuppressLint("SetJavaScriptEnabled")
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, TabHost.OnTabChangeListener, AbsListView.OnScrollListener{
    public static final String TAG = "MAINACTIVITY";
    private static final String TAG_RESULTS = "result";
    private static final String TAG_IMAGE = "wr_file";
    private static final String TAG_ID = "wr_id";
    private static final String TAG_NAME = "wr_subject";
    private static final String TAG_HIT = "wr_hit";
    private static final String TAG_RANK = "starRank";
    private static final String TAG_COMMENT = "wr_comment";
    private static final String TAG_MENU_TOTAL = "menuTotal";
    private static final String TAG_ROOM = "room";
    private static final String TAG_CAFE = "cafe";
    private static final String TAG_HAIR = "hair";
    private static final String TAG_SERVICE = "service";
    private static final String TAG_PERCENT = "percent";
    private static final String TAG_NEW = "icon_new";
    private static final String TAG_TOTAL_PRICE = "price";
    private static final String TAG_LOC = "loc";

    private static final String URL = "http://cjcp.kr/data/file/main/";
    private static final String[] PERMISSIONS = new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.CHANGE_WIFI_STATE, Manifest.permission.INTERNET, Manifest.permission.WAKE_LOCK};

    static ScrollView slider;
    static DrawerLayout drawer, main_drawer;

    static MenuItem speechMenu, explorerMenu, searchMenu;
    private ImageView imageView,alignImg1,alignImg2,alignImg3,titlelogo,freebg;
    private ListView list,list1, list2, list3, list4, list5;
    static LinearLayout inc_main, inc_help, inc_intro, inc_inquiry, inc_push,inc_free;
    //static ScrollView ;
    static View[] views = null; // inc_main 외의 모든 루트뷰 배열
    static Button sendbtn, searchBtn, locBtn, locAlignBtn, hitAlignBtn, signAlignBtn, loc1,loc2,loc3,loc4,hot1,hot2,hot3,hot4,hot5,hot6,hot7,hot8,hot9,hot10,hot11,resetBtn;
    static ToggleButton pushToggleBtn;
    static TextView vi_today, vi_total, vi_now,listEmpty1,listEmpty2,listEmpty3,listEmpty4,listEmpty5;
    private String token, schtxt="null", saveSchTxt, loadText, myJSON, next, caname, vi_sum, dis,regStatus;
    public String menuTotal, room, cafe, hair, service, locText="", hotText="null", cateText="null",wr_hit="";
    private double lat,lng;
    EditText name, tel1,tel2 ,tel3;
    Editable company, num1, num2, num3;
    WebView webview, introView, inquiryView, bannerView, latestView;
    SharedPreferences mPref;
    EditText searchText;
    String[] vi_totals, prices;
    int page, pos , width;
    View Header;
    JSONArray peoples = null;
    TabHost.TabSpec tabSpec1, tabSpec2, tabSpec3, tabSpec4, tabSpec5;
    TabHost tabHost;
    TabLayout tabLayout;
    ViewPager viewPager;
    ArrayList<HashMap<String, String>> personList;
    boolean lastitemVisibleFlag = false;        //화면에 리스트의 마지막 아이템이 보여지는지 체크
    LocationManager manager;
    MainListViewAdapter mainListViewAdapter;
    FloatingActionButton mFloatingButton;
    public static String dataurl = "http://cjcp.kr/android_list.php?";
    CountDownLatch latch ;
    HttpPushTask pushTask;
    HttpTask httpTask;
    private BackPressCloseHandler backPressCloseHandler;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Typekit.getInstance().addNormal(Typekit.createFromAsset(this,"Roboto-Regular.ttf")).addBold(Typekit.createFromAsset(this,"Roboto-Bold.ttf"));
        latch = new CountDownLatch(20);
        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
        width = dm.widthPixels;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        DrawerLayout.DrawerListener myDrawerListener = new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {
                int state = newState;
                if (slider.getVisibility() == View.VISIBLE && state == 2) {
                    Util.closeLayer();
                }
            }
        };//드로어버튼 이벤트

        drawer.addDrawerListener(myDrawerListener);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mPref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        bannerView = (WebView) findViewById(R.id.bannerView);
        WebViewInit(bannerView);
        bannerView.loadUrl("http://cjcp.kr/android_banner.php");

        webview = (WebView) findViewById(R.id.help_webview);
        introView = (WebView) findViewById(R.id.intro_webview);
        inquiryView = (WebView) findViewById(R.id.inquiryWebview);

        initView();
        initButtonListener();

        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
            }
        }
        getToken();

        pushToggleBtn = (ToggleButton) findViewById(R.id.toggleButton);
        pushToggleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pushToggleBtn.isChecked()){
                    PushUpdateAsyncTask task = new PushUpdateAsyncTask();
                    task.getData(token,"1");
                    pushToggleBtn.setBackgroundResource(R.drawable.toggle);
                    Toast.makeText(MainActivity.this, "푸시알림 승인", Toast.LENGTH_SHORT).show();
                }else{
                    PushUpdateAsyncTask task = new PushUpdateAsyncTask();
                    task.getData(token,"2");
                    pushToggleBtn.setBackgroundResource(R.drawable.toggle_off);
                    Toast.makeText(MainActivity.this, "푸시알림 해제", Toast.LENGTH_SHORT).show();
                }
            }
        });

        initAdapter();
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchText = (EditText) findViewById(R.id.searchEditbox);
                Editable search = searchText.getText();
                schtxt = search.toString().replace(" ", "%20");
                pos=0;
                Util.closeLayer();
                dataReload();
            }
        });


        LocationImpl myLoc = new LocationImpl();
        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER,100,1,myLoc);
        if(manager.getLastKnownLocation(LocationManager.GPS_PROVIDER)==null)
            manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,100,1,myLoc);
        Location loc = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Location loc2 = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (loc != null) {
            lat = loc.getLatitude();
            lng = loc.getLongitude();
            Toast.makeText(MainActivity.this, "현재위치정보갱신", Toast.LENGTH_SHORT).show();
        } else if (loc2 != null) {
            lat = loc2.getLatitude();
            lng = loc2.getLongitude();
            Toast.makeText(MainActivity.this, "현재위치정보갱신", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "현재위치정보를 갱산할 수 없습니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
        }
        manager.removeUpdates(myLoc);
        getData(dataurl+"&regId="+token);

        Header = getLayoutInflater().inflate(R.layout.list_header,null,false);
        latestView = (WebView)Header.findViewById(R.id.listlatest);
        locBtn = (Button)Header.findViewById(R.id.locBtn);
        locAlignBtn = (Button)Header.findViewById(R.id.locAlignBtn);
        hitAlignBtn = (Button)Header.findViewById(R.id.hitAlignBtn);
        signAlignBtn = (Button)Header.findViewById(R.id.signAlignBtn);
        alignImg1 = (ImageView)Header.findViewById(R.id.alignImg1);
        alignImg2 = (ImageView)Header.findViewById(R.id.alignImg2);
        alignImg3 = (ImageView)Header.findViewById(R.id.alignImg3);
        locBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationImpl myLoc = new LocationImpl();
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }else{
                    manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, myLoc);
                }

                Location loc = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                Location loc2 = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (loc != null) {
                    lat = loc.getLatitude();
                    lng = loc.getLongitude();
                    Toast.makeText(MainActivity.this, "현재위치정보갱신", Toast.LENGTH_SHORT).show();
                } else if (loc2 != null) {
                    lat = loc2.getLatitude();
                    lng = loc2.getLongitude();
                    Toast.makeText(MainActivity.this, "현재위치정보갱신", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "현재위치정보를 갱산할 수 없습니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                }
                manager.removeUpdates(myLoc);
            }
        });
        locAlignBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pos=0;
                if((double)lat != 0 && (double)lng!=0) {
                    locAlignBtn.setTextColor(Color.parseColor("#ea5959"));
                    hitAlignBtn.setTextColor(Color.parseColor("#000000"));
                    signAlignBtn.setTextColor(Color.parseColor("#000000"));
                    alignImg1.setBackgroundResource(R.drawable.listcheck_on);
                    alignImg2.setBackgroundResource(R.drawable.listcheck_off);
                    alignImg3.setBackgroundResource(R.drawable.listcheck_off);
                    list.setSelection(0);
                    wr_hit = "cate";
                    dataReload();
                }else{
                    Toast.makeText(MainActivity.this, "현재위치정보가 없습니다. \r\n 현재위치버튼으로 위치정보를 갱신하세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        hitAlignBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pos=0;
                locAlignBtn.setTextColor(Color.parseColor("#000000"));
                hitAlignBtn.setTextColor(Color.parseColor("#ea5959"));
                signAlignBtn.setTextColor(Color.parseColor("#000000"));
                alignImg1.setBackgroundResource(R.drawable.listcheck_off);
                alignImg2.setBackgroundResource(R.drawable.listcheck_on);
                alignImg3.setBackgroundResource(R.drawable.listcheck_off);
                list.setSelection(0);
                wr_hit = "wr_hit";
                dataReload();

                //getData(dataurl+"&hit=wr_hit");
            }
        });
        signAlignBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pos=0;
                locAlignBtn.setTextColor(Color.parseColor("#000000"));
                hitAlignBtn.setTextColor(Color.parseColor("#000000"));
                signAlignBtn.setTextColor(Color.parseColor("#ea5959"));
                alignImg1.setBackgroundResource(R.drawable.listcheck_off);
                alignImg2.setBackgroundResource(R.drawable.listcheck_off);
                alignImg3.setBackgroundResource(R.drawable.listcheck_on);
                list.setSelection(0);
                wr_hit = "wr_new";
                dataReload();
                //getData(dataurl+"&hit=wr_new");
            }
        });
        WebViewInit(latestView);
        latestView.loadUrl("http://cjcp.kr/latest_android.php");
        latestView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                list.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        list1.addHeaderView(Header);
        list2.addHeaderView(Header);
        list3.addHeaderView(Header);
        list4.addHeaderView(Header);
        list5.addHeaderView(Header);
        listEvent();
        mFloatingButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        mFloatingButton.setBackgroundResource(R.color.colorPrimary);
        mFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.smoothScrollToPositionFromTop(0,0,10);
            }
        });
        //tabinit();
    }

    public void initView(){
        main_drawer = (DrawerLayout)findViewById(R.id.main_drawer);
        slider = (ScrollView)findViewById(R.id.slider);
        titlelogo = (ImageView)findViewById(R.id.titlelogo);
        titlelogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*startActivity(new Intent(MainActivity.this,MainActivity.class));
                finish();*/
                cateText="null";
                schtxt="null";
                hotText="null";
                caname=null;
                wr_hit="";
                pos=0;
                dataReload();
            }
        });
        list1 = (ListView) findViewById(R.id.listmain1);
        list2 = (ListView) findViewById(R.id.listmain2);
        list3 = (ListView) findViewById(R.id.listmain3);
        list4 = (ListView) findViewById(R.id.listmain4);
        list5 = (ListView) findViewById(R.id.listmain5);
        list = list1;
        tabHost = (TabHost) findViewById(R.id.tabhost);
        tabHost.setup();
        inc_main = (LinearLayout)findViewById(R.id.inc_main);
        inc_help = (LinearLayout)findViewById(R.id.inc_help);
        inc_intro = (LinearLayout)findViewById(R.id.inc_intro);
        inc_inquiry = (LinearLayout)findViewById(R.id.inc_inquiry);
        inc_free = (LinearLayout)findViewById(R.id.inc_free);
        freebg = (ImageView)findViewById(R.id.free_bg);
        freebg.measure(width,12);
        inc_push = (LinearLayout)findViewById(R.id.inc_push);
        sendbtn = (Button)findViewById(R.id.send_btn);
        searchBtn = (Button)findViewById(R.id.searchBtn);
        searchText = (EditText) findViewById(R.id.searchEditbox);
        searchText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus==false){
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(searchText.getWindowToken(), 0);
                }
            }
        });
        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(v.getId()==R.id.searchEditbox && actionId == EditorInfo.IME_ACTION_DONE){
                    searchText = (EditText) findViewById(R.id.searchEditbox);
                    Editable search = searchText.getText();
                    schtxt = search.toString().replace(" ", "%20");
                    Util.closeLayer();
                    dataReload();
                }
                return false;
            }
        });
        vi_today = (TextView) findViewById(R.id.vi_today);
        vi_total = (TextView) findViewById(R.id.vi_total);
        vi_now = (TextView) findViewById(R.id.vi_now);
        loc1 = (Button) findViewById(R.id.loc1);
        loc2 = (Button) findViewById(R.id.loc2);
        loc3 = (Button) findViewById(R.id.loc3);
        loc4 = (Button) findViewById(R.id.loc4);
        hot1 = (Button) findViewById(R.id.hot1);
        hot2 = (Button) findViewById(R.id.hot2);
        hot3 = (Button) findViewById(R.id.hot3);
        hot4 = (Button) findViewById(R.id.hot4);
        hot5 = (Button) findViewById(R.id.hot5);
        hot6 = (Button) findViewById(R.id.hot6);
        hot7 = (Button) findViewById(R.id.hot7);
        hot8 = (Button) findViewById(R.id.hot8);
        hot9 = (Button) findViewById(R.id.hot9);
        hot10 = (Button) findViewById(R.id.hot10);
        hot11 = (Button) findViewById(R.id.hot11);
        name = (EditText) findViewById(R.id.editText);
        tel1 = (EditText) findViewById(R.id.editText2);
        tel2 = (EditText) findViewById(R.id.editText3);
        tel3 = (EditText) findViewById(R.id.editText4);
        name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus==false){
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(name.getWindowToken(), 0);
                }
            }
        });
        tel1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus==false){
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(tel1.getWindowToken(), 0);
                }
            }
        });
        tel2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus==false){
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(tel2.getWindowToken(), 0);
                }
            }
        });
        tel3.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus==false){
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(tel3.getWindowToken(), 0);
                }
            }
        });
        listEmpty1 = (TextView) findViewById(R.id.listEmpty1);
        listEmpty2 = (TextView) findViewById(R.id.listEmpty2);
        listEmpty3 = (TextView) findViewById(R.id.listEmpty3);
        listEmpty4 = (TextView) findViewById(R.id.listEmpty4);
        listEmpty5 = (TextView) findViewById(R.id.listEmpty5);
        resetBtn = (Button) findViewById(R.id.resetBtn);
        backPressCloseHandler = new BackPressCloseHandler(this);
        sendbtn.setOnClickListener(this);
        views = new View[]{inc_main, inc_help, inc_intro,inc_inquiry,inc_free,inc_push};
    }

    @Override
    public void onClick(View v) {
        EditText name = (EditText) findViewById(R.id.editText);
        EditText tel1 = (EditText) findViewById(R.id.editText2);
        EditText tel2 = (EditText) findViewById(R.id.editText3);
        EditText tel3 = (EditText) findViewById(R.id.editText4);
        company = name.getText();
        num1 = tel1.getText();
        num2 = tel2.getText();
        num3 = tel3.getText();
        if(company.length() == 0){
            Toast.makeText(this, "업체명을 입력하세요", Toast.LENGTH_SHORT).show();
        }else if(num1.length() == 0){
            Toast.makeText(this, "연락처 앞자리를 입력하세요", Toast.LENGTH_SHORT).show();
        }else if(num2.length() == 0){
            Toast.makeText(this, "연락처를 입력하세요", Toast.LENGTH_SHORT).show();
        }else if(num3.length() == 0){
            Toast.makeText(this, "연락처를 입력하세요", Toast.LENGTH_SHORT).show();
        }else {
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

    public void initAdapter(){
        ListView listview ;
        ListViewAdapter adapter;

        // Adapter 생성
        adapter = new ListViewAdapter() ;

        // 리스트뷰 참조 및 Adapter달기
        listview = (ListView) findViewById(R.id.left_menu);
        listview.setAdapter(adapter);
        listview.setDivider(null);

        /*adapter.addItem(ContextCompat.getDrawable(this, R.mipmap.ic_cancel),"", "");*/
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.categorize),"메인으로", "※ 홈화면으로 이동합니다.");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.user_manual),"사용방법", "※ 청주쿠폰을 이용하는 방법에 대한 설명입니다.");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.employee_card),"회사소개", "※ 회사에 대한 설명입니다.");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.shop),"무료입점요청", "※ 청주쿠폰에 입점 등록을 하실 수 있습니다.");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.handshake),"지사문의", "※ 전국 가맹점 진행에 대한 설명입니다.");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.toggle),"푸시설정", "※ 푸시알림을 설정합니다.");

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListViewItem item = (ListViewItem) parent.getItemAtPosition(position);
                String title = item.getTitle();
                if(title.equals("메인으로") || title.equals("")){
                    //Util.openPage(inc_main);
                    /*Intent intent = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(intent);*/
                    hotText = "null";
                    cateText = "null";
                    schtxt = "null";
                    caname=null;
                    wr_hit="";
                    Util.closeLayer();
                    dataReload();
                }else if(title.equals("사용방법")){
                    WebViewInit(webview);
                    webview.loadUrl("http://cjcp.kr/img/guide.png");
                    Util.openPage(inc_help);
                }
                else if(title.equals("회사소개")){
                    WebViewInit(introView);
                    introView.loadUrl("http://cjcp.kr/img/intro.png");
                    Util.openPage(inc_intro);
                }
                else if(title.equals("무료입점요청")){
                    Util.openPage(inc_free);
                }
                else if(title.equals("지사문의")){
                    WebViewInit(inquiryView);
                    inquiryView.loadUrl("http://cjcp.kr/img/inquiry.png");
                    Util.openPage(inc_inquiry);
                }
                else if(title.equals("푸시설정")){
                    Util.openPage(inc_push);
                }
                drawer.closeDrawer(GravityCompat.START);
            }
        });
    }

    // BackPress(백키) 처리
    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (slider.getVisibility() == View.VISIBLE) {
                Util.closeLayer();
            } else {
                View opened = null;
                for(View page:views) {
                    if(page.getVisibility() == View.VISIBLE) {
                        opened = page;
                        break;
                    }
                }
                if (opened != null) {
                    if(opened.getId() != inc_main.getId()) {
                        Util.openPage(inc_main);
                    }else {
                        backPressCloseHandler.onBackPressed();
                    }
                }else {
                    backPressCloseHandler.onBackPressed();
                    //finish();
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        //speechMenu = menu.findItem(R.id.action_speak);
        //explorerMenu = menu.findItem(R.id.action_explorer);
        //searchMenu = menu.findItem(R.id.action_search);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_select :
                if (slider.getVisibility() == View.VISIBLE) {
                    Util.closeLayer();
                } else {
                    Util.openLayer();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private class AndroidBridge{
        @JavascriptInterface
        public void showBanner(String imgUrl){
            Intent intent = new Intent(MainActivity.this, bannerActivity.class);
            intent.putExtra("link", imgUrl);
            startActivity(intent);
        }
        @JavascriptInterface
        public void detailView(String wr_id,String lat, String lng){
            Intent intent = new Intent(MainActivity.this, DetailViewActivity.class);
            intent.putExtra("wr_id", wr_id);
            intent.putExtra("lat", Double.parseDouble(lat));
            intent.putExtra("lng", Double.parseDouble(lng));
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
        webView.addJavascriptInterface(new AndroidBridge(), "android");
    }

    class HttpTask extends AsyncTask<String,Void,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            latch.countDown();
            startActivity(new Intent(MainActivity.this,Dialog.class));
        }

        @Override
        protected void onCancelled(String s) {
            super.onCancelled(s);
        }

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection conn = null;
            try {
                URL url = new URL("http://cjcp.kr/m/bbs/write_update.php");
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setConnectTimeout(60);
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));

                writer.write(
                        "&name="+company+"&phone="+num1+"&phone1="+num2+"&phone2="+num3+"&ca_name=무료업체등록&bo_table=inquiry"
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
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                if(conn!=null)
                    conn.disconnect();
            }
            return null;
        }
    }

    class HttpPushTask extends AsyncTask<String,Void,String>{
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            latch.countDown();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection conn = null;
            try {
                URL url = new URL("http://cjcp.kr/m/insert_regid.php");
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setConnectTimeout(60);
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));

                writer.write(
                        "&regID="+token
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
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                if(conn!=null)
                    conn.disconnect();
            }
            return null;
        }
    }

    public void getToken(){
        try {
            token = FirebaseInstanceId.getInstance().getToken();
            pushTask = new HttpPushTask();
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB) {
                pushTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }else {
                pushTask.execute();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }



    @Override/*    private void searchTextSave(String searchText){
        SharedPreferences pref = getSharedPreferences("SearchText", Activity.MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();
        edit.putString("text",searchText);
        edit.commit();
    }
    private String searchTextLoad(){
        SharedPreferences pref = getSharedPreferences("SearchText", Activity.MODE_PRIVATE);
        saveSchTxt = pref.getString("text","");
        return saveSchTxt;
    }*/
    protected void onRestart() {
        super.onRestart();

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    @Override
    protected void onDestroy() {
        SharedPreferences.Editor edit = mPref.edit();
        edit.clear();
        edit.commit();
        try {
            if (httpTask.getStatus() == AsyncTask.Status.RUNNING) {
                httpTask.cancel(true);
            }
            if (pushTask.getStatus() == AsyncTask.Status.RUNNING) {
                pushTask.cancel(true);
            }
        }catch (Exception e){

        }
        super.onDestroy();
    }

    public void getData(String url){
        class GetDataJSON extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                try {
                String uri = params[0];
                BufferedReader bufferedReader = null;
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
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String result){
                myJSON=result;
                showList();
                latch.countDown();
            }



        }
        GetDataJSON g = new GetDataJSON();
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB){
            g.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,url);
        }else {
            g.execute(url);
        }
    }

    protected void showList(){
        MainListViewAdapter adapter;
        // Adapter 생성
        adapter = new MainListViewAdapter() ;
        // 리스트뷰 참조 및 Adapter달기
        list.setAdapter(adapter);
        if(width <= 480){
            list.setDividerHeight(4);
        }else {
            list.setDividerHeight(10);
        }
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            peoples = jsonObj.getJSONArray(TAG_RESULTS);
            String menuTotal="0",room="0",cafe="0",hair="0",service="0";

            for(int i=0;i<peoples.length();i++){
                JSONObject c = peoples.getJSONObject(i);
                String wr_id = c.getString(TAG_ID);
                String wr_subject = c.getString(TAG_NAME);
                String wr_hit = c.getString(TAG_HIT);
                String wr_file = c.getString(TAG_IMAGE);
                String comment = c.getString(TAG_COMMENT);
                comment = "("+comment+")";
                String rank = c.getString(TAG_RANK);

                menuTotal = c.getString("menuTotal");
                room = c.getString("room");
                cafe = c.getString("cafe");
                hair = c.getString("hair");
                service = c.getString("service");
                switch (rank){
                    case "5":
                        rank = "★★★★★";
                        break;
                    case "4":
                        rank = "★★★★☆";
                        break;
                    case "3":
                        rank = "★★★☆☆";
                        break;
                    case "2":
                        rank = "★★☆☆☆";
                        break;
                    case "1":
                        rank = "★☆☆☆☆";
                        break;
                    default:
                        rank = "☆☆☆☆☆";
                        break;
                }
                String price = c.getString(TAG_TOTAL_PRICE);
                prices = price.split("@");
                NumberFormat nf = NumberFormat.getInstance();
                nf.setMaximumIntegerDigits(10);

                if(prices.length == 2){
                    String price1 = nf.format(Integer.parseInt(prices[0].trim()));
                    String price2 = nf.format(Integer.parseInt(prices[1].trim()));
                    price = price1+"원@"+ price2 + "원";
                }else{
                    price = prices[0]+ "@ ";
                }
                cateText = c.getString("cate");
                hotText = c.getString("location");
                saveSchTxt = c.getString("searchTxt");
                String percent = c.getString(TAG_PERCENT);
                next = c.getString("next");
                vi_sum = c.getString("vi_sum");
                double lats = Double.parseDouble(c.getString("lat"));
                double lngs = Double.parseDouble(c.getString("lng"));
                lats = Double.parseDouble(String.format("%.4f",lats));
                lngs = Double.parseDouble(String.format("%.4f",lngs));
                dis = "";

                double distance;

                if(lat!=0 && lng !=0 ) {
                    lat = Double.parseDouble(String.format("%.4f",lat));
                    lng = Double.parseDouble(String.format("%.4f",lng));

                    distance = calDistance(lat,lng,lats,lngs);
                    dis = String.format("%.0f",distance);
                    dis = setMeters(dis);
                }else{
                    dis = "";
                }
                String urls = URL+wr_file;
                String flag = "";
                if(c.getString("icon_new").equals("true")){
                    flag = "http://cjcp.kr/skin/board/main/img/flag_new.png";
                }else if(c.getString("icon_new").equals("trues")){
                    flag = "http://cjcp.kr/skin/board/main/img/premium.png";
                }else if(c.getString("icon_new").equals("ad")){
                    flag = "http://cjcp.kr/skin/board/main/img/ad.png";
                }
                adapter.addItem(wr_subject,price,rank,comment,dis,percent,wr_hit+"명",wr_id,urls,flag, Double.parseDouble(c.getString("lat")),Double.parseDouble(c.getString("lng")));
                regStatus = c.getString("regstatus");
            }
            this.menuTotal = menuTotal;
            this.cafe = cafe;
            this.service = service;
            this.room = room;
            this.hair = hair;

            vi_totals = vi_sum.split("/");
            vi_total.setText(vi_totals[0]+"명");
            vi_today.setText(vi_totals[1]+"명");
            vi_now.setText(vi_totals[2]+"명");

            if(pos!=0){
                list.setSelection(pos+1);
            }

            if(tabHost.getTabWidget().getTabCount()==0){
                tabinit(menuTotal,cafe,hair,room,service);
            }
            if(regStatus.equals("2")){
                pushToggleBtn.setBackgroundResource(R.drawable.toggle_off);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onTabChanged(String tabId) {
        try {
            list=null;
            page=0;
            pos=0;
            /*String dataurls = "";
            if(!cateText.equals("null")){
                dataurls += "&cate="+cateText;
            }
            if(!hotText.equals("null")){
                dataurls += "&location="+hotText;
            }
            if(!schtxt.equals("null")){
                dataurls += "&searchTxt="+schtxt;
            }*/


            TabWidget widget = tabHost.getTabWidget();

            switch (tabId){
                case "tab1":
                    latestView.loadUrl("http://cjcp.kr/latest_android.php");
                    for(int i = 0; i < widget.getChildCount(); i++) {
                        View v = widget.getChildAt(i);
                        if(i==0) {
                            v.setBackgroundResource(R.drawable.menu_background);
                            TextView tv = (TextView)v.findViewById(android.R.id.title);
                            tv.setTextColor(getResources().getColor(R.color.colorPrimary));
                            tv.setTypeface(Typekit.createFromAsset(this,"Roboto-Bold.ttf"));
                        }else{
                            v.setBackgroundResource(R.drawable.btn_normal);
                            TextView tv = (TextView)v.findViewById(android.R.id.title);
                            tv.setTextColor(Color.BLACK);
                            tv.setTypeface(Typekit.createFromAsset(this,"Roboto-Regular.ttf"));
                        }
                    }
                    list = (ListView) findViewById(R.id.listmain1);
                    tabHost.getTabWidget().focusCurrentTab(tabHost.getCurrentTab());
                    caname = "&ca_name=전체";
                    dataReload();
                    //dataurls="";
                    listEvent();
                    break;
                case "tab2":
                    latestView.loadUrl("http://cjcp.kr/latest_android.php?cate=맛집");
                    for(int i = 0; i < widget.getChildCount(); i++) {
                        View v = widget.getChildAt(i);
                        if(i==1) {
                            v.setBackgroundResource(R.drawable.menu_background);
                            TextView tv = (TextView)v.findViewById(android.R.id.title);
                            tv.setTextColor(getResources().getColor(R.color.colorPrimary));
                            tv.setTypeface(Typekit.createFromAsset(this,"Roboto-Bold.ttf"));
                        }else{
                            v.setBackgroundResource(R.drawable.btn_normal);
                            TextView tv = (TextView)v.findViewById(android.R.id.title);
                            tv.setTextColor(Color.BLACK);
                            tv.setTypeface(Typekit.createFromAsset(this,"Roboto-Regular.ttf"));
                        }
                    }
                    View v2 = widget.getChildAt(1);
                    v2.setBackgroundResource(R.drawable.menu_background);
                    list = list2;
                    caname = "&ca_name=맛집";
                    dataReload();
                    //dataurls="";
                    listEvent();
                    break;
                case "tab3":
                    latestView.loadUrl("http://cjcp.kr/latest_android.php?cate=뷰티");
                    for(int i = 0; i < widget.getChildCount(); i++) {
                        View v = widget.getChildAt(i);
                        if(i==2) {
                            v.setBackgroundResource(R.drawable.menu_background);
                            TextView tv = (TextView)v.findViewById(android.R.id.title);
                            tv.setTextColor(getResources().getColor(R.color.colorPrimary));
                            tv.setTypeface(Typekit.createFromAsset(this,"Roboto-Bold.ttf"));
                        }else{
                            v.setBackgroundResource(R.drawable.btn_normal);
                            TextView tv = (TextView)v.findViewById(android.R.id.title);
                            tv.setTextColor(Color.BLACK);
                            tv.setTypeface(Typekit.createFromAsset(this,"Roboto-Regular.ttf"));
                        }
                    }
                    View v3 = widget.getChildAt(2);
                    v3.setBackgroundResource(R.drawable.menu_background);
                    list = list3;
                    caname = "&ca_name=뷰티";
                    dataReload();
                    //dataurls="";
                    listEvent();
                    break;
                case "tab4":
                    latestView.clearCache(true);
                    latestView.clearHistory();
                    latestView.loadUrl("http://cjcp.kr/latest_android.php?cate=숙박");
                    for(int i = 0; i < widget.getChildCount(); i++) {
                        View v = widget.getChildAt(i);
                        if(i==3) {
                            v.setBackgroundResource(R.drawable.menu_background);
                            TextView tv = (TextView)v.findViewById(android.R.id.title);
                            tv.setTextColor(getResources().getColor(R.color.colorPrimary));
                            tv.setTypeface(Typekit.createFromAsset(this,"Roboto-Bold.ttf"));
                        }else{
                            v.setBackgroundResource(R.drawable.btn_normal);
                            TextView tv = (TextView)v.findViewById(android.R.id.title);
                            tv.setTextColor(Color.BLACK);
                            tv.setTypeface(Typekit.createFromAsset(this,"Roboto-Regular.ttf"));
                        }
                    }
                    View v4 = widget.getChildAt(3);
                    v4.setBackgroundResource(R.drawable.menu_background);
                    list = list4;
                    caname = "&ca_name=숙박";
                    dataReload();
                    //dataurls="";
                    listEvent();
                    break;
                case "tab5":
                    latestView.loadUrl("http://cjcp.kr/latest_android.php?cate=서비스");
                    for(int i = 0; i < widget.getChildCount(); i++) {
                        View v = widget.getChildAt(i);
                        if(i==4) {
                            v.setBackgroundResource(R.drawable.menu_background);
                            TextView tv = (TextView)v.findViewById(android.R.id.title);
                            tv.setTextColor(getResources().getColor(R.color.colorPrimary));
                            tv.setTypeface(Typekit.createFromAsset(this,"Roboto-Bold.ttf"));
                        }else{
                            v.setBackgroundResource(R.drawable.btn_normal);
                            TextView tv = (TextView)v.findViewById(android.R.id.title);
                            tv.setTextColor(Color.BLACK);
                            tv.setTypeface(Typekit.createFromAsset(this,"Roboto-Regular.ttf"));
                        }
                    }
                    View v5 = widget.getChildAt(4);
                    v5.setBackgroundResource(R.drawable.menu_background);
                    list = list5;
                    caname = "&ca_name=서비스";
                    dataReload();
                    //dataurls="";
                    listEvent();
                    break;
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }



    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if(scrollState == 1){
            mFloatingButton.hide();
        }else if(scrollState==0){
            mFloatingButton.show();
        }
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastitemVisibleFlag) {
            page= page + 30;
            int nextnum = Integer.valueOf(next);
            pos = list.getFirstVisiblePosition();
            String dataurls = "";
            if(!cateText.equals("null")){
                dataurls = "&cate="+cateText;
            }
            if(!hotText.equals("null")){
                dataurls = "&location="+hotText;
            }
            if(!schtxt.equals("null")){
                dataurls += "&searchTxt="+schtxt;
            }

            if ((lat != 0 && lng != 0) && wr_hit.equals("cate")) {
                dataurls +=  "&lat=" + lat + "&lng=" + lng + "&checkLoc=1";
            } else if (wr_hit.equals("wr_hit") || wr_hit.equals("wr_new")) {
                dataurls +=  "&hit=" + wr_hit;
            }
            if(caname!=null){
                dataurls = dataurls + caname;
            }
            if(nextnum != 0) {
                getData(dataurl+dataurls+"&page=" + page);
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        lastitemVisibleFlag = (totalItemCount > 0) && (firstVisibleItem + visibleItemCount >= totalItemCount);
    }



    public String setMeters(String dis){
        String result = "";
        String result2 = "";
        if(dis.length() < 4){
            result = dis+"m";
        }else if(dis.length() >= 4 ){
            result = dis.substring(0,dis.length()-3);
            result = result +"."+ dis.substring(1,dis.length()-2)+"km";
        }else if(dis.length()==0){
            result = "";
        }
        return result;
    }

    public void tabinit(String menuTotal, String cafe, String hair, String room, String service){
        tabSpec1 = tabHost.newTabSpec("tab1");
        String str = "전체("+menuTotal+")";
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(str);
        spannableStringBuilder.setSpan(new AbsoluteSizeSpan(50),0,2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tabSpec1.setIndicator(spannableStringBuilder);
        tabSpec1.setContent(R.id.tab1);
        tabHost.addTab(tabSpec1);
        tabSpec2 = tabHost.newTabSpec("tab2");
        String str2 = "맛집("+cafe+")";
        SpannableStringBuilder spannableStringBuilder2 = new SpannableStringBuilder(str2);
        spannableStringBuilder2.setSpan(new AbsoluteSizeSpan(18),0,1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tabSpec2.setIndicator(spannableStringBuilder2);
        tabSpec2.setContent(R.id.tab2);
        tabHost.addTab(tabSpec2);
        tabSpec3 = tabHost.newTabSpec("tab3");
        String str3 = "뷰티("+hair+")";
        SpannableStringBuilder spannableStringBuilder3 = new SpannableStringBuilder(str3);
        spannableStringBuilder3.setSpan(new AbsoluteSizeSpan(18),0,1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tabSpec3.setIndicator(spannableStringBuilder3);
        tabSpec3.setContent(R.id.tab3);
        tabHost.addTab(tabSpec3);
        tabSpec4 = tabHost.newTabSpec("tab4");
        String str4 = "숙박("+room+")";
        SpannableStringBuilder spannableStringBuilder4 = new SpannableStringBuilder(str4);
        spannableStringBuilder4.setSpan(new AbsoluteSizeSpan(18),0,1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tabSpec4.setIndicator(spannableStringBuilder4);
        tabSpec4.setContent(R.id.tab4);
        tabHost.addTab(tabSpec4);
        tabSpec5 = tabHost.newTabSpec("tab5");
        String str5 = "서비스("+service+")";
        SpannableStringBuilder spannableStringBuilder5 = new SpannableStringBuilder(str5);
        spannableStringBuilder5.setSpan(new AbsoluteSizeSpan(18),0,2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tabSpec5.setIndicator(spannableStringBuilder5);
        tabSpec5.setContent(R.id.tab5);
        tabHost.addTab(tabSpec5);

        tabHost.setOnTabChangedListener(this);
        tabHost.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        TabWidget widget = tabHost.getTabWidget();
        for(int i=0 ; i< widget.getTabCount();i++) {
            View v = widget.getChildAt(i);
            TextView tv = (TextView)v.findViewById(android.R.id.title);
            if(i==0) {
                v.setBackgroundResource(R.drawable.menu_background);
                tv.setTextColor(getResources().getColor(R.color.colorPrimary));
                tv.setTypeface(Typekit.createFromAsset(this,"Roboto-Bold.ttf"));
            }else{
                v.setBackgroundResource(R.drawable.btn_normal);
                tv.setTextColor(Color.BLACK);
                tv.setTypeface(Typekit.createFromAsset(this,"Roboto-Regular.ttf"));
            }
            if(width < 540){
                tv.setTextSize(12);
            }
            v.setPadding(1,1,1,1);
        }
    }
    public void listEvent(){
        list.setOnScrollListener(this);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MainListViewItem item = (MainListViewItem) parent.getItemAtPosition(position);
                String wr_id = item.getWr_id();
                double latitude = item.getLat();
                double longitude = item.getLng();
                Intent intent = new Intent(MainActivity.this,DetailViewActivity.class);
                intent.putExtra("wr_id", wr_id);
                intent.putExtra("lat", latitude);
                intent.putExtra("lng", longitude);
                startActivity(intent);
            }
        });
    }

    public void listHeaderinit(){
        View Header = getLayoutInflater().inflate(R.layout.list_header,null,false);
        latestView = (WebView)Header.findViewById(R.id.listlatest);
        locBtn = (Button)Header.findViewById(R.id.locBtn);
        locAlignBtn = (Button)Header.findViewById(R.id.locAlignBtn);
        hitAlignBtn = (Button)Header.findViewById(R.id.hitAlignBtn);
        signAlignBtn = (Button)Header.findViewById(R.id.signAlignBtn);
        WebViewInit(latestView);
        latestView.loadUrl("http://cjcp.kr/latest_android.php");
        list.addHeaderView(Header);
    }

    public void initButtonListener(){
        loc1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cateText = "상당";
                hotText="null";
                pos=0;
                //getData(dataurl+cateText);
                Util.closeLayer();
                dataReload();
            }
        });
        loc2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cateText = "흥덕";
                hotText="null";
                pos=0;
                //getData(dataurl+cateText);
                Util.closeLayer();
                dataReload();
            }
        });
        loc3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cateText = "청원";
                pos=0;
                hotText="null";
                //getData(dataurl+cateText);
                Util.closeLayer();
                dataReload();
            }
        });
        loc4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cateText = "서원";
                hotText="null";
                pos=0;
                //getData(dataurl+cateText);
                Util.closeLayer();
                dataReload();
            }
        });
        hot1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cateText = "null";
                hotText = "복대";
                pos=0;
                //getData(dataurl+hotText);
                Util.closeLayer();
                dataReload();
            }
        });
        hot2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cateText = "null";
                hotText = "오창";
                pos=0;
                //getData(dataurl+hotText);
                Util.closeLayer();
                dataReload();
            }
        });
        hot3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cateText = "null";
                hotText = "봉명";
                pos=0;
                //getData(dataurl+hotText);
                Util.closeLayer();
                dataReload();
            }
        });
        hot4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cateText = "null";
                hotText = "율량";
                pos=0;
                //getData(dataurl+hotText);
                Util.closeLayer();
                dataReload();
            }
        });
        hot5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cateText = "null";
                hotText = "충북대";
                pos=0;
                //getData(dataurl+hotText);
                Util.closeLayer();
                dataReload();
            }
        });
        hot6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cateText = "null";
                hotText = "성안길";
                pos=0;
                //getData(dataurl+hotText);
                Util.closeLayer();
                dataReload();
            }
        });
        hot7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cateText = "null";
                hotText = "청주대";
                pos=0;
                //getData(dataurl+hotText);
                Util.closeLayer();
                dataReload();
            }
        });
        hot8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cateText = "null";
                hotText = "서원대";
                pos=0;
                //getData(dataurl+hotText);
                Util.closeLayer();
                dataReload();
            }
        });
        hot9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cateText = "null";
                hotText = "터미널";
                pos=0;
                //getData(dataurl+hotText);
                Util.closeLayer();
                dataReload();
            }
        });
        hot10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cateText = "null";
                hotText = "산남";
                pos=0;
                //getData(dataurl+hotText);
                Util.closeLayer();
                dataReload();
            }
        });
        hot11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cateText = "null";
                hotText = "금천";
                pos=0;
                //getData(dataurl+hotText);
                Util.closeLayer();
                dataReload();
            }
        });
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hotText = "null";
                cateText = "null";
                schtxt = "null";
                caname=null;
                pos=0;
                wr_hit="";
                Util.closeLayer();
                dataReload();
            }
        });
    }

    //검색필터
    public void dataReload(){
        try {
            if (httpTask.getStatus() == AsyncTask.Status.RUNNING) {
                httpTask.cancel(true);
            }
            if (pushTask.getStatus() == AsyncTask.Status.RUNNING) {
                pushTask.cancel(true);
            }
        }catch (Exception e){

        }
        latch = new CountDownLatch(20);
        Util.openPage(inc_main);
        String dataurls = "";
        if(!cateText.equals("null") && hotText.equals("null")){
            dataurls = "&cate="+cateText;
        }
        if(!hotText.equals("null") && cateText.equals("null")){
            dataurls = "&location="+hotText;
        }
        if(!schtxt.equals("null")){
            dataurls += "&searchTxt="+schtxt;
        }

        if ((lat != 0 && lng != 0) && wr_hit.equals("cate")) {
            dataurls +=  "&lat=" + lat + "&lng=" + lng + "&checkLoc=1";
        } else if (wr_hit.equals("wr_hit") || wr_hit.equals("wr_new")) {
            dataurls +=  "&hit=" + wr_hit;
        }
        if(caname!=null){
            dataurls = dataurls + caname;
        }
        getData(dataurl+dataurls);
        dataurls = "";
    }

    public double calDistance(double lat1, double lon1, double lat2, double lon2){

        double theta, dist;
        theta = lon1 - lon2;
        dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);

        dist = dist * 60 * 1.1515;
        dist = dist * 1.609344;    // 단위 mile 에서 km 변환.
        dist = dist * 1000.0;      // 단위  km 에서 m 로 변환

        return dist;
    }

    private double deg2rad(double deg){
        return (double)(deg * Math.PI / (double)180d);
    }

    private double rad2deg(double rad){
        return (double)(rad * (double)180d / Math.PI);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }
}