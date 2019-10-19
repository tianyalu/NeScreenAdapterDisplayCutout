package com.sty.ne.screen.adapter.displaycutout;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.DisplayCutout;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preInitView();
        setContentView(R.layout.activity_main);

        //设置成沉浸式后刘海肯能遮挡控件，将控件下移
        Button button = findViewById(R.id.button);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) button.getLayoutParams();
        layoutParams.topMargin = heightForDisplayCutout();
        button.setLayoutParams(layoutParams);

        //第二种方式避免控件被刘海平遮挡
        RelativeLayout layout = findViewById(R.id.rl_container);
        layout.setPadding(layout.getPaddingLeft(), heightForDisplayCutout(), layout.getPaddingRight(), layout.getPaddingBottom());
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void preInitView() {
        //1.设置全屏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //华为，小米， OPPO
        //1.判断手机厂商， 2.判断手机是否刘海 3.设置是否让内容区域延伸进刘海 4.设置控件是否避开刘海区域 5.获取刘海的高度

        //判断手机是否是刘海屏
        if(hasDisplayCutout(window)) {
            //2.让内容区域延申至刘海
            WindowManager.LayoutParams params = window.getAttributes();
            /**
             * #LAYOUT_IN_DISPLAY_CUTOUT_MODE_DEFAULT   全屏模式，内容下移，非全屏不受影响
             * #LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES   允许内容延伸进刘海
             * #LAYOUT_IN_DISPLAY_CUTOUT_MODE_NEVER     不允许内容延伸至刘海
             */
            params.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            window.setAttributes(params);

            //设置成沉浸式
            int flags = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            int visibility = window.getDecorView().getSystemUiVisibility();
            visibility |= flags; //追加沉浸式设置
            window.getDecorView().setSystemUiVisibility(visibility);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean hasDisplayCutout(Window window) {
        DisplayCutout displayCutout;
        View rootView = window.getDecorView();
        WindowInsets insets = rootView.getRootWindowInsets();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P && insets != null) {
            displayCutout = insets.getDisplayCutout();
            if(displayCutout != null) {
                if(displayCutout.getBoundingRects() != null && displayCutout.getBoundingRects().size() > 0
                        && displayCutout.getSafeInsetTop() > 0) {
                    return true;
                }
            }
        }
        return false; //因为是模拟器的原因，这里设置成true
    }

    //通常情况下，刘海的高度就是状态栏的高度
    public int heightForDisplayCutout() {
        int resID = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if(resID > 0) {
            return getResources().getDimensionPixelSize(resID);
        }
        return 96;
    }
}
