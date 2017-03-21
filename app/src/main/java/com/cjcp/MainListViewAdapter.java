package com.cjcp;

import android.content.Context;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import java.util.ArrayList;

/**
 * Created by Pc on 2016-04-11.
 */
public class MainListViewAdapter extends BaseAdapter{
    private ArrayList<MainListViewItem> data = new ArrayList<MainListViewItem>() ;

    public MainListViewAdapter(){

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
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item, parent, false);
        }
        LinearLayout listFrame = (LinearLayout)convertView.findViewById(R.id.listFrame);
        TextView wr_subject = (TextView)convertView.findViewById(R.id.wr_subject);
        TextView price1 = (TextView)convertView.findViewById(R.id.price1);
        TextView price2 = (TextView)convertView.findViewById(R.id.price2);
        TextView pricecenter = (TextView)convertView.findViewById(R.id.pricecenter);
        TextView percent = (TextView)convertView.findViewById(R.id.percent);
        TextView starRank = (TextView)convertView.findViewById(R.id.starRank);
        TextView wr_comment = (TextView)convertView.findViewById(R.id.wr_comment);
        TextView wr_hit= (TextView)convertView.findViewById(R.id.wr_hit);
        TextView loc = (TextView)convertView.findViewById(R.id.loc);
        ImageView wr_file = (ImageView)convertView.findViewById(R.id.wr_file);
        ImageView flagImage = (ImageView)convertView.findViewById(R.id.flagImage);

        MainListViewItem listViewItem = data.get(position);

        try {
            wr_subject.setText(listViewItem.getTitle());

            String[] price = listViewItem.getPrice().split("@");
            if(width <= 480){
                wr_subject.setTextSize(16);
                price1.setTextSize(14);
                price2.setTextSize(14);
                pricecenter.setTextSize(10);
                price2.setMaxLines(1);
                starRank.setTextSize(10);
                wr_comment.setTextSize(10);
                wr_hit.setTextSize(10);
                loc.setTextSize(10);
                percent.setTextSize(20);
                wr_file.getLayoutParams().width = 155;
            }
            if(price[1].equals(" ")){
                price1.setText(price[1]);
                price1.setVisibility(View.GONE);
                pricecenter.setText("");
                pricecenter.setVisibility(View.GONE);
                price2.setText(price[0]);
            }else {
                price1.setVisibility(View.VISIBLE);
                price1.setPaintFlags(price1.getPaintFlags()|Paint.STRIKE_THRU_TEXT_FLAG);
                price1.setText(price[0]);
                pricecenter.setVisibility(View.VISIBLE);
                pricecenter.setText("  →  ");
                price2.setText(price[1]);
            }

            percent.setText(listViewItem.getPercent());
            starRank.setText(listViewItem.getStarRank());
            wr_comment.setText(listViewItem.getWr_comment());
            wr_hit.setText(listViewItem.getWr_hit());
            loc.setText(listViewItem.getLoc());
            Glide.with(context).load(listViewItem.getImage()).into(wr_file);
            Glide.with(context).load(listViewItem.getFlagImage()).into(flagImage);


        }catch (Exception e){
            e.printStackTrace();
        }

        return convertView;
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(String title, String price, String starRank, String wr_comment, String loc, String percent, String wr_hit, String wr_id,String image,String flagImage,double lat, double lng) {
        MainListViewItem item = new MainListViewItem();

        item.setTitle(title);
        item.setPrice(price);
        item.setPercent(percent);
        item.setStarRank(starRank);
        item.setWr_comment(wr_comment);
        item.setWr_hit(wr_hit);
        item.setLoc(loc);
        item.setWr_id(wr_id);
        item.setImage(image);
        item.setFlagImage(flagImage);
        item.setLat(lat);
        item.setLng(lng);
        data.add(item);
    }

}
