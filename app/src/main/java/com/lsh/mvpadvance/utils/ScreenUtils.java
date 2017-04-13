package com.lsh.mvpadvance.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

/**
 * 获取屏幕状态相关类
 * Created by admin on 2015/12/4.
 */
public class ScreenUtils {

    /**
     * 私有构造不能实例化
     */
    public ScreenUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 获取屏幕宽度
     *
     * @param context 上下文对象
     * @return 返回当前屏幕的宽
     */
    public static int getScreenWidth(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        return width;
    }

    /**
     * 获取屏幕高度
     *
     * @param context 上下文对象
     * @return 返回当前屏幕的高
     */
    public static int getScreenHeight(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        return height;
    }

    /**
     * 获取当前屏幕截图包括状态栏
     *
     * @param activity
     * @return
     */
    public static Bitmap getScreenShotWithBar(Activity activity) {
        //检索顶层窗口的view（包含标准的窗口框架/状态栏和内容部分），
        // 可以作为窗口管理器添加到窗口。
        View view = activity.getWindow().getDecorView();
        //启用绘图缓存
        view.setDrawingCacheEnabled(true);
        //建立view的cache
        view.buildDrawingCache();
        //获取view的cache图片
        Bitmap bp = view.getDrawingCache();
        int width = getScreenWidth(activity);
        int height = getScreenHeight(activity);
        Bitmap bitmap = null;
        //从原位图截取一个新的位图
        bitmap = Bitmap.createBitmap(bp, 0, 0, width, height);
        //销毁cache
        view.destroyDrawingCache();
        return bitmap;
    }

    /**
     * 获取当前屏幕截图，不包括状态栏
     *
     * @param activity
     * @return
     */
    public static Bitmap getScreenShotWithoutBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bp = view.getDrawingCache();
        Rect frame = new Rect();
        //获取到程序显示的区域，包括标题栏，但不包括状态栏。
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        // 该区域的顶点坐标就是状态栏高度
        int statusBarHeight = frame.top;
        int width = getScreenWidth(activity);
        int height = getScreenHeight(activity);
        Bitmap bitmap = null;
        bitmap = Bitmap.createBitmap(bp, 0, statusBarHeight, width, height
                - statusBarHeight);
        view.destroyDrawingCache();
        return bitmap;
    }

}
