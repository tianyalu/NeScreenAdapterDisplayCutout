### NeScreenAdapterDisplayCutout 刘海屏适配
Android P 刘海屏适配，只有在全屏模式下才需要考虑适配，普通模式无需考虑
#### 适配步骤及源码
##### 1. 设置全屏
```android
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    Window window = getWindow();
    window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
```
##### 2. 判断手机厂商

##### 3. 判断手机是否为刘海屏
```android
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
        return false; 
    }
```
##### 4. 设置是否让内容区域延伸进刘海
```android
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
```
##### 5. 设置控件是否避开刘海区域
* 方式一
```android
   Button button = findViewById(R.id.button);
   RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) button.getLayoutParams();
   layoutParams.topMargin = heightForDisplayCutout();
   button.setLayoutParams(layoutParams);
```
* 方式二
```android
    RelativeLayout layout = findViewById(R.id.rl_container);
    layout.setPadding(layout.getPaddingLeft(), heightForDisplayCutout(), layout.getPaddingRight(), layout.getPaddingBottom());
```
##### 6. 获取刘海高度
```android
    //通常情况下，刘海的高就是状态栏的高
    public int heightForDisplayCutout(){
        int resID = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resID > 0){
            return getResources().getDimensionPixelSize(resID);
        }
        return 96;
    }
```
更多代码可参考 `Utils` 类   


#### 其他手机厂商刘海屏适配官方地址
* [华为](https://devcenter-test.huawei.com/consumer/cn/devservice/doc/50114)
* [小米](https://dev.mi.com/console/doc/detail?pId=1293)
* [OPPO](https://open.oppomobile.com/service/message/detail?id=61876)
* [VIVO](https://dev.vivo.com.cn/documentCenter/doc/103)
