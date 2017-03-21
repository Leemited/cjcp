package com.cjcp;

import android.support.v4.view.GravityCompat;
import android.view.View;

public class Util {

    // ContentsView 열기
    static void openLayer() {
        if (MainActivity.drawer.isDrawerOpen(GravityCompat.START)) {
            MainActivity.drawer.closeDrawer(GravityCompat.START);
        }
        MainActivity.slider.setVisibility(View.VISIBLE);
        MainActivity.main_drawer.openDrawer(MainActivity.slider);
    }

    // ContentsView 닫기
    static void closeLayer() {
        MainActivity.main_drawer.closeDrawer(MainActivity.slider);
        MainActivity.slider.setVisibility(View.GONE);
    }

    // 페이지 레이아웃 열기
    static void openPage(View view) {
        // 서랍(drawer) 닫기
        if (MainActivity.drawer.isDrawerOpen(GravityCompat.START)) { MainActivity.drawer.closeDrawer(GravityCompat.START); }
        if (MainActivity.slider.getVisibility() == View.VISIBLE) { closeLayer(); }
        // 모든 페이지 닫기
        int id = view.getId();
        for(View page: MainActivity.views) page.setVisibility(View.GONE);

        view.setVisibility(View.VISIBLE);
    }

}