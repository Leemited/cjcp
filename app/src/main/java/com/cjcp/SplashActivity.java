package com.cjcp;

import android.*;
import android.app.Activity;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

public class SplashActivity extends Activity
{
    private final static String TAG = "SPLASH";
    Handler handler;
    int Count=1;
    public static final String WIFE_STATE = "WIFE";
    public static final String MOBILE_STATE = "MOBILE";
    public static final String NONE_STATE = "NONE";

    @Override
    public void onCreate(Bundle paramBundle)
    {
        super.onCreate(paramBundle);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);

        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork == null) {
            Toast.makeText(this, "청주쿠폰앱은 인터넷 연결이 필요한 앱입니다. \r\n인터넷 연결을 확인해 주세요.", Toast.LENGTH_SHORT).show();
            SystemClock.sleep(100);
            finish();
        }

        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);
        int size = Math.round(50 * metrics.density);

        ImageView img = (ImageView) findViewById(R.id.imageView3);
        LayoutParams params = (LayoutParams) img.getLayoutParams();
        params.width = metrics.widthPixels + size;
        params.height = metrics.heightPixels + size;
        img.setLayoutParams(params);
        img.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Animation animation = AnimationUtils.loadAnimation(getBaseContext(), R.anim.anim_translate_right);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if(Build.VERSION_CODES.LOLLIPOP < Build.VERSION.SDK_INT) {
                    PermissionListener permissionlistener = new PermissionListener() {
                        @Override
                        public void onPermissionGranted() {
                            Intent localIntent2 = new Intent(SplashActivity.this, MainActivity.class);
                            SplashActivity.this.startActivity(localIntent2);
                            finish();
                        }

                        @Override
                        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                            finish();
                        }

                    };

                    new TedPermission(SplashActivity.this).setPermissionListener(permissionlistener).setRationaleMessage("이앱을 사용하기 위해 다음과 같은 권한이 필요합니다.").setDeniedMessage("거부하실 경우 [설정] > [권한]에서 권한을 허용할 수 있으며 \r\n 권한 미승인시 앱을 이용할 수 없습니다.")
                            .setPermissions(android.Manifest.permission.CALL_PHONE, android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_WIFI_STATE, android.Manifest.permission.CHANGE_WIFI_STATE, android.Manifest.permission.INTERNET, android.Manifest.permission.WAKE_LOCK).check();
                }else{
                    Intent localIntent2 = new Intent(SplashActivity.this, MainActivity.class);
                    SplashActivity.this.startActivity(localIntent2);
                    finish();
                }

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        img.startAnimation(animation);

    }
}
