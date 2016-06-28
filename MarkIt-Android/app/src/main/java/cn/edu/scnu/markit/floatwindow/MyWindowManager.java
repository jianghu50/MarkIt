package cn.edu.scnu.markit.floatwindow;


import android.app.ActivityManager;
import android.content.Context;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by jialin on 2016/5/2.
 */
public class MyWindowManager {
    /**
     * 悬浮按钮View的实例
     */
    private static FloatButtonView floatButtonView;

    /**
     * 悬浮窗View的实例
     */
    private static FloatWindowView floatWindowView;

    /**
     * 联系人View的实例
     */
    private static FloatContactView floatContactView;

    /**
     * 悬浮按钮View的参数
     */
    private static WindowManager.LayoutParams floatButtonParams;

    /**
     * 悬浮窗View的参数
     */
    private static WindowManager.LayoutParams floatWindowParams;

    /**
     * 联系人对话框的参数
     */
    private static WindowManager.LayoutParams floatContactParams;

    private static String strFloatContact;

    /**
     * 用于控制在屏幕上添加或移除悬浮窗
     */
    private static WindowManager mWindowManager;

    /**
     * 用于获取手机可用内存
     */
    private static ActivityManager mActivityManager;

    /**
     * 创建一个悬浮按钮。初始位置为屏幕的右部中间位置。
     *
     * @param context 必须为应用程序的Context.
     */
    public static void createFloatButton(Context context) {
        WindowManager windowManager = getWindowManager(context);
        int screenWidth = windowManager.getDefaultDisplay().getWidth();
        int screenHeight = windowManager.getDefaultDisplay().getHeight();
        if (floatButtonView == null) {
            floatButtonView = new FloatButtonView(context);
            if (floatButtonParams == null) {
                floatButtonParams = new WindowManager.LayoutParams();
                floatButtonParams.type = WindowManager.LayoutParams.TYPE_PHONE;
                floatButtonParams.format = PixelFormat.RGBA_8888;
                floatButtonParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
                floatButtonParams.gravity = Gravity.LEFT | Gravity.TOP;
                floatButtonParams.width = FloatButtonView.viewWidth;
                floatButtonParams.height = FloatButtonView.viewHeight;
                floatButtonParams.x = screenWidth;
                floatButtonParams.y = screenHeight / 2;
            }
            floatButtonView.setParams(floatButtonParams);
            windowManager.addView(floatButtonView, floatButtonParams);
        }
    }

    /**
     * 将悬浮按钮从屏幕上移除。
     *
     * @param context 必须为应用程序的Context.
     */
    public static void removeFloatButton(Context context) {
        if ( floatButtonView != null) {
            WindowManager windowManager = getWindowManager(context);
            windowManager.removeView(floatButtonView);
            floatButtonView = null;
        }
    }

    /**
     * 创建一个悬浮窗。
     *
     * @param context 必须为应用程序的Context.
     */
    public static void createFloatWindow(Context context) {
        Log.i("windowManager","createFloatButton");
        WindowManager windowManager = getWindowManager(context);
        int screenWidth = windowManager.getDefaultDisplay().getWidth();
        int screenHeight = windowManager.getDefaultDisplay().getHeight();
        if (floatWindowView == null) {
            floatWindowView = new  FloatWindowView(context);
            if (floatWindowParams == null) {
                floatWindowParams = new WindowManager.LayoutParams();
                floatWindowParams.x = screenWidth -  FloatWindowView.viewWidth;
                floatWindowParams.y = screenHeight -  FloatWindowView.viewHeight;
                floatWindowParams.type = WindowManager.LayoutParams.TYPE_PHONE;
                floatWindowParams.format = PixelFormat.RGBA_8888;
                floatWindowParams.gravity = Gravity.LEFT | Gravity.TOP;
                floatWindowParams.width =  FloatWindowView.viewWidth;
                floatWindowParams.height =  FloatWindowView.viewHeight;

               //悬浮窗随输入法调整布局
               floatWindowParams.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE;


                floatWindowParams.windowAnimations = android.R.style.Animation_Translucent;//动画效果

            }
            windowManager.addView(floatWindowView, floatWindowParams);
        }
    }

    /**
     * 将悬浮窗从屏幕上移除。
     *
     * @param context 必须为应用程序的Context.
     */
    public static void removeFloatWindow(Context context) {
        if (floatWindowView != null) {
            WindowManager windowManager = getWindowManager(context);
            windowManager.removeView(floatWindowView);
            floatWindowView = null;
        }
    }


    /**
     * 创建联系人对话框
     * @param context
     */
    public static void createFloatContact(final Context context, final TextView textView){
        Log.i("windowManager","createFloatContact");
        WindowManager windowManager = getWindowManager(context);
        int screenWidth = windowManager.getDefaultDisplay().getWidth();
        int screenHeight = windowManager.getDefaultDisplay().getHeight();
        if (floatContactView == null) {
            floatContactView = new FloatContactView(context, new FloatContactView.IFloatContactEventListener() {
                @Override
                public void floatContactEvent(String contact) {
                   strFloatContact = contact;
                    Log.i("contact from dialog",contact);
                    if (contact.length() != 0){
                        textView.setText(contact);

                    }
                }
            });
            if (floatContactParams == null) {
                floatContactParams = new WindowManager.LayoutParams();
                floatContactParams.x = screenWidth -  floatContactView.viewWidth;
                floatContactParams.y = screenHeight -  floatContactView.viewHeight;
                floatContactParams.type = WindowManager.LayoutParams.TYPE_PHONE;
                floatContactParams.format = PixelFormat.RGBA_8888;
                floatContactParams.gravity = Gravity.LEFT | Gravity.TOP;
                floatContactParams.width =  floatContactView.viewWidth;
                floatContactParams.height =  floatContactView.viewHeight;

                //悬浮窗随输入法调整布局
                floatWindowParams.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_UNCHANGED;


                //floatWindowParams.windowAnimations = android.R.style.Animation_Translucent;//动画效果

            }
            windowManager.addView(floatContactView, floatContactParams);
        }

    }


    /**
     * 将对话框从屏幕上移除。
     *
     * @param context 必须为应用程序的Context.
     */
    public static void removeFloatContact(Context context) {
        if (floatContactView != null) {
            WindowManager windowManager = getWindowManager(context);
            windowManager.removeView(floatContactView);
            floatContactView = null;
        }
    }

    /**
     * 更新小悬浮窗的TextView上的数据，显示内存使用的百分比。
     *
     * @param context 可传入应用程序上下文。
     */
    public static void updateUsedPercent(Context context) {
        if (floatButtonView != null) {
//            TextView percentView = (TextView) floatButtonView.findViewById(R.id.id_small_textView_test);
//            //percentView.setText(getUsedPercentValue(context));
//            percentView.setText(R.string.add_icon);
        }
    }

  /**
     * 是否有悬浮窗(包括悬浮按钮和悬浮窗)显示在屏幕上。
     *
     * @return 有悬浮窗显示在桌面上返回true，没有的话返回false。
     */
    public static boolean isWindowShowing() {
        return floatButtonView != null ||  floatWindowView != null;
    }

    /**
     * 如果WindowManager还未创建，则创建一个新的WindowManager返回。否则返回当前已创建的WindowManager。
     *
     * @param context 必须为应用程序的Context.
     * @return WindowManager的实例，用于控制在屏幕上添加或移除悬浮窗。
     */
    private static WindowManager getWindowManager(Context context) {
        if (mWindowManager == null) {
            mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }
        return mWindowManager;
    }

    /**
     * 如果ActivityManager还未创建，则创建一个新的ActivityManager返回。否则返回当前已创建的ActivityManager。
     *
     * @param context 可传入应用程序上下文。
     * @return ActivityManager的实例，用于获取手机可用内存。
     */
    private static ActivityManager getActivityManager(Context context) {
        if (mActivityManager == null) {
            mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        }
        return mActivityManager;
    }


    public static int getScreenHeight(Context context){
        WindowManager windowManager = getWindowManager(context);
        //int screenWidth = windowManager.getDefaultDisplay().getWidth();
        int screenHeight = windowManager.getDefaultDisplay().getHeight();
        return screenHeight;
    }
    /**
     * 计算已使用内存的百分比，并返回。
     *
     * @param context 可传入应用程序上下文。
     * @return 已使用内存的百分比，以字符串形式返回。
     */
    public static String getUsedPercentValue(Context context) {
        String dir = "/proc/meminfo";
        try {
            FileReader fr = new FileReader(dir);
            BufferedReader br = new BufferedReader(fr, 2048);
            String memoryLine = br.readLine();
            String subMemoryLine = memoryLine.substring(memoryLine.indexOf("MemTotal:"));
            br.close();
            long totalMemorySize = Integer.parseInt(subMemoryLine.replaceAll("\\D+", ""));
            long availableSize = getAvailableMemory(context) / 1024;
            int percent = (int) ((totalMemorySize - availableSize) / (float) totalMemorySize * 100);
            return percent + "%";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "悬浮窗";
    }

    /**
     * 获取当前可用内存，返回数据以字节为单位。
     *
     * @param context 可传入应用程序上下文。
     * @return 当前可用内存。
     */
    private static long getAvailableMemory(Context context) {
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        getActivityManager(context).getMemoryInfo(mi);
        return mi.availMem;
    }
}
