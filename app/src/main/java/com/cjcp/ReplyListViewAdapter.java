package com.cjcp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.Resource;

import java.util.ArrayList;

/**
 * Created by Pc on 2016-04-11.
 */
public class ReplyListViewAdapter extends BaseAdapter{
    private ArrayList<ReplyListViewItem> data = new ArrayList<ReplyListViewItem>() ;
    String star;

    public ReplyListViewAdapter(){

    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.replylist_item, parent, false);
        }

        TextView userName = (TextView)convertView.findViewById(R.id.username);
        TextView signDate = (TextView)convertView.findViewById(R.id.datetime);
        TextView wrContent = (TextView)convertView.findViewById(R.id.wr_content);
        //TextView starRank = (TextView)convertView.findViewById(R.id.starRank);
        ImageView replayImg = (ImageView)convertView.findViewById(R.id.replyImg);
        ReplyListViewItem listViewItem = data.get(position);
        try {
            userName.setText(listViewItem.getUserName());
            signDate.setText(listViewItem.getSignDate());
            wrContent.setText(listViewItem.getWrContent());
            if(listViewItem.getWrCommentReply().length()==0 || listViewItem.getUserName().equals("익명")) {
                userName.setText(listViewItem.getSignDate());
                signDate.setVisibility(View.VISIBLE);
                signDate.setText(listViewItem.getSignDate());
                userName.setVisibility(View.VISIBLE);
                switch (listViewItem.getStarRank()){
                    case "5":
                        star = "★★★★★";
                        break;
                    case "4":
                        star = "★★★★☆";
                        break;
                    case "3":
                        star = "★★★☆☆";
                        break;
                    case "2":
                        star = "★★☆☆☆";
                        break;
                    case "1":
                        star = "★☆☆☆☆";
                        break;
                    case "0":
                        star = "☆☆☆☆☆";
                        break;
                }
                userName.setText(star);
                userName.setTextColor(Color.argb(255,234,89,89));
                star="";
                replayImg.setVisibility(View.GONE);
                convertView.setPadding(listViewItem.getScreen(),5,listViewItem.getScreen(),5);

            }else{
                userName.setVisibility(View.VISIBLE);
                replayImg.setVisibility(View.VISIBLE);
                userName.setBackgroundColor(Color.argb(255,0,180,255));
                userName.setTextColor(Color.WHITE);
                convertView.setPadding(100,5,listViewItem.getScreen(),5);
                //signDate.setVisibility(View.VISIBLE);
                //starRank.setVisibility(View.GONE);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return convertView;
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(String userName, String signDate, String wrContent, String starRank, String wrCommentReply,int size) {
        ReplyListViewItem item = new ReplyListViewItem();

        item.setUserName(userName);
        item.setSignDate(signDate);
        item.setStarRank(starRank);
        item.setWrContent(wrContent);
        item.setWrCommentReply(wrCommentReply);
        item.setScreen(size);
        data.add(item);
    }

}
