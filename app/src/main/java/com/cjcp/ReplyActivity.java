package com.cjcp;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Pc on 2016-12-30.
 */

public class ReplyActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private static final String TAG = "REPLYACTIVITY";
    ListView list;
    String myJSON, wr_id,userName,starRank,wrContent,signDate,wrCommentReply ,starrank,replyPassword,replyContents;
    GetDataJSON g;
    JSONArray peoples = null;
    TextView starRankSelect;
    EditText replyPass,replyContent;
    Spinner starRankSpinner;
    Button replySendBtn;
    HttpTask httpTask;
    String[] starRankSel = {"★★★★★","★★★★☆","★★★☆☆","★★☆☆☆","★☆☆☆☆","☆☆☆☆☆"};
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitvity_replylist);

        Intent intent = getIntent();
        wr_id = intent.getStringExtra("wr_id");

        getData("http://cjcp.kr/android_reply.php?wr_id=" + wr_id);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("댓글보기");
        actionBar.setHomeButtonEnabled(true);
        initView();

        //starRankSpinner.setOnItemClickListener(this);

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
                replyPassword = String.valueOf(replyPass.getText());
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
                if(replyPassword.length()==0){
                    Toast.makeText(ReplyActivity.this, "패스워드를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }else if(replyContent.length()==0){
                    Toast.makeText(ReplyActivity.this, "댓글 내용을 입력해주세요.", Toast.LENGTH_SHORT).show();
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
    }

    class HttpTask extends AsyncTask<String,Void,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            startActivity(new Intent(ReplyActivity.this,DialogReply.class));
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
                        "&bo_table=main&wr_name=익명&wr_content="+replyContents+"&wr_password="+replyPassword+"&wr_3="+starrank+"&w=c&wr_id="+wr_id
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
                Log.d(TAG,"result" + sb.toString());
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
        getData("http://cjcp.kr/android_reply.php?wr_id=" + wr_id);
        super.onPostResume();
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void initView(){
        list = (ListView)findViewById(R.id.replyList);
        starRankSelect = (TextView) findViewById(R.id.starRankSelect);
        starRankSpinner = (Spinner) findViewById(R.id.starRankSpinner);
        replySendBtn = (Button) findViewById(R.id.replySendBtn);
        replyPass = (EditText) findViewById(R.id.replyPass);
        replyContent = (EditText) findViewById(R.id.replyContent);
    }

    public void getData(String url){
        g = new ReplyActivity.GetDataJSON();
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB) {
            g.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,url);
        }else {
            g.execute();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        starRankSelect.setText(starRankSel[position]);
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
        ReplyListViewAdapter adapter = new ReplyListViewAdapter();

        list.setAdapter(adapter);
        list.setDividerHeight(4);
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            peoples = jsonObj.getJSONArray("result");

            for(int i = 0 ; i<peoples.length() ; i++){
                JSONObject c = peoples.getJSONObject(i);
                userName = c.getString("user");
                starRank = c.getString("starRank");
                signDate = c.getString("signDate");
                wrContent = c.getString("wr_content");
                wrCommentReply = c.getString("wr_comment_reply");
                adapter.addItem(userName,signDate,wrContent,starRank,wrCommentReply,9);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }
}
