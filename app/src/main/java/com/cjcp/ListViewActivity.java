package com.cjcp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Pc on 2016-04-11.
 */
@SuppressLint({"SetJavaScriptEnabled"})
public class ListViewActivity extends Activity {
    String myJSON;
    String URL = "http://cjcp.kr/data/file/main/";
    private static final String TAG_RESULTS="result";
    private static final String TAG_IMAGE = "wr_file";
    private static final String TAG_ID = "wr_id";
    private static final String TAG_NAME = "wr_subject";
    private static final String TAG_ADD ="wr_hit";

    JSONArray peoples = null;

    ArrayList<HashMap<String, String>> personList;

    private ListView list;
    private ImageView imageView;
    WebView WebImage;
    boolean lastitemVisibleFlag = false;        //화면에 리스트의 마지막 아이템이 보여지는지 체크

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        list = (ListView) findViewById(R.id.listView);
        imageView = (ImageView) findViewById(R.id.imageView2);
        personList = new ArrayList<HashMap<String,String>>();
        getData("http://cjcp.kr/android_list.php");

        list.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //현재 화면에 보이는 첫번째 리스트 아이템의 번호(firstVisibleItem) + 현재 화면에 보이는 리스트 아이템의 갯수(visibleItemCount)가 리스트 전체의 갯수(totalItemCount) -1 보다 크거나 같을때
                lastitemVisibleFlag = (totalItemCount > 0) && (firstVisibleItem + visibleItemCount >= totalItemCount);
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //OnScrollListener.SCROLL_STATE_IDLE은 스크롤이 이동하다가 멈추었을때 발생되는 스크롤 상태입니다.
                //즉 스크롤이 바닦에 닿아 멈춘 상태에 처리를 하겠다는 뜻
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastitemVisibleFlag) {
                    //TODO 화면이 바닦에 닿을때 처리
                    Log.d("TEST","끝!!");
                }
            }

        });

    }

    protected void showList(){
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            peoples = jsonObj.getJSONArray(TAG_RESULTS);

            for(int i=0;i<peoples.length();i++){
                JSONObject c = peoples.getJSONObject(i);
                //String wr_id = c.getString(TAG_ID);
                String wr_subject = c.getString(TAG_NAME);
                String wr_hit = c.getString(TAG_ADD);
                String wr_file = c.getString(TAG_IMAGE);
                //new ImageDownloader(imageView).execute(URL+wr_file);
                HashMap<String,String> persons = new HashMap<String,String>();
                //persons.put(TAG_ID,wr_id);
                persons.put(TAG_NAME,wr_subject);
                persons.put(TAG_ADD, wr_hit);
                persons.put(TAG_IMAGE, wr_file);
                personList.add(persons);

            }

            ListAdapter adapter = new SimpleAdapter(
                    ListViewActivity.this, personList, R.layout.item,
                    new String[]{TAG_ID,TAG_NAME,TAG_ADD,TAG_IMAGE},
                    new int[]{R.id.wr_subject, R.id.imageView2}
            );

            list.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    public void getData(String url){
        class GetDataJSON extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {

                String uri = params[0];

                BufferedReader bufferedReader = null;
                try {
                    java.net.URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

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
                showList();
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(url);
    }
}
