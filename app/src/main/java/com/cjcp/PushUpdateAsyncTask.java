package com.cjcp;

import android.os.AsyncTask;
import android.os.Build;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Pc on 2017-01-02.
 */

public class PushUpdateAsyncTask {

    public void getData(String regid , String active){
        pushUpdateAsyncTask task = new pushUpdateAsyncTask();
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB){
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,regid,active);
        }else {
            task.execute(regid,active);
        }
    }

    class pushUpdateAsyncTask extends AsyncTask<String,Void,String>{
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(String... params) {
            String token = params[0];
            String active = params[1];
            HttpURLConnection conn = null;
            try {
                URL url = new URL("http://cjcp.kr/android_push_update.php");
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setConnectTimeout(60);
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));

                writer.write(
                        "&regID="+token+"&active="+active
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

}
