package com.mc.phonelive;

import android.Manifest;
import android.app.Activity;
import android.app.AppOpsManager;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.DrawableRes;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import android.util.SparseLongArray;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import com.mc.phonelive.utils.SpUtil;
import com.mc.phonelive.utils.ToastUtil;
import com.orhanobut.logger.Logger;
import com.picture.android.view.AlertDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Map;


/**
 * Created by Administrator on 2018/7/4.
 */

public class Utils {
    private static final String CHECK_OP_NO_THROW = "checkOpNoThrow";

    private static final String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";

    /**
     * banner适配
     * @param context
     * @return
     */

    /**
     * 设置控件宽高比，
          若为banner，宽设置屏幕宽高比2:1   setViewWhp(this,2,1);
          若为普通View,确定宽度后，高度自适应或者设置宽高比  setViewWhp(this,475,97);
     * @param context
     * @param
     * @param
     */
//    public static void setViewWhp(Context context,View view,int intWith,int intheight){
//        LinearLayout.LayoutParams linearParams =(LinearLayout.LayoutParams) view.getLayoutParams(); //取控件textView当前的布局参数 linearParams.height = 20;// 控件的高强制设成20
//        linearParams.height =(int)( linearParams.width*intheight/intWith);// 控件的高为屏幕宽的一般即宽高比为2:1
//        view.setLayoutParams(linearParams);
//    }

    public static void setViewWhp(Context context,boolean isBanner,View view,int intWith,int intheight,int margin){
        LinearLayout.LayoutParams linearParams =(LinearLayout.LayoutParams) view.getLayoutParams(); //取控件textView当前的布局参数 linearParams.height = 20;// 控件的高强制设成20
        if(isBanner){
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            int width = wm.getDefaultDisplay().getWidth()-dip2px(context,margin);
            linearParams.width=width;
        }
        linearParams.height =(int)( linearParams.width*intheight/intWith);// 控件的高为屏幕宽的一般即宽高比为2:1
        view.setLayoutParams(linearParams);
    }
    public static void setViewWhp(Context context,boolean isBanner,View view,int intWith,int intheight){
        LinearLayout.LayoutParams linearParams =(LinearLayout.LayoutParams) view.getLayoutParams(); //取控件textView当前的布局参数 linearParams.height = 20;// 控件的高强制设成20
        if(isBanner){
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            int width = wm.getDefaultDisplay().getWidth();
            linearParams.width=width;
        }
        linearParams.height =(int)( linearParams.width*intheight/intWith);// 控件的高为屏幕宽的一般即宽高比为2:1
        view.setLayoutParams(linearParams);
    }
    public static LinearLayoutManager getLvManager(Context context){
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL, false);

        return linearLayoutManager;
    }

    public static String getSinglePara(String json, String para){
        if (json!=null)
            try {
                JSONObject jsonObject=new JSONObject(json);
                return jsonObject.getString(para);
            } catch (JSONException e) {

            }
        return  "";
    }
    public static void setIndicator (TabLayout tabs, int leftDip, int rightDip) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        tabStrip.setAccessible(true);
        LinearLayout llTab = null;
        try {
            llTab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics());
        int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics());

        for (int i = 0; i < llTab.getChildCount(); i++) {
            View child = llTab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.leftMargin = left;
            params.rightMargin = right;
            child.setLayoutParams(params);
            child.invalidate();
        }
    }

    public static LinearLayoutManager getHvManager(Context context){
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context,
                LinearLayoutManager.HORIZONTAL, false);

        return linearLayoutManager;
    }
    public static StaggeredGridLayoutManager getStagManager(Context context, int spanCount){
        StaggeredGridLayoutManager lmg = new StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL);


        return lmg;
    }
    public static GridLayoutManager getGvManager(Context context){
        GridLayoutManager gvManager = new GridLayoutManager(context,3);
        return gvManager;
    }
    public static GridLayoutManager getGvManager(Context context, int spanCount){
        GridLayoutManager gvManager = new GridLayoutManager(context,spanCount);
        return gvManager;
    }


    public static void copyFile(String oldPath, String newPath) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { //文件存在时  
                InputStream inStream = new FileInputStream(oldPath); //读入原文件  
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                while ( (byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小  
                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
                fs.close();
            }
        }
        catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();
        }
    }
    public boolean checkReadPermission(Context context,String string_permission,int request_code) {
        boolean flag = false;
        if (ContextCompat.checkSelfPermission(context, string_permission) == PackageManager.PERMISSION_GRANTED) {//已有权限
            flag = true;
        } else {//申请权限
            ActivityCompat.requestPermissions((Activity) context, new String[]{string_permission}, request_code);
        }
        return flag;
    }
    /**
     * 拨打电话（直接拨打）
     * @param telPhone 电话
     */
    private String strPhone="";
    public static final int REQUEST_CALL_PERMISSION = 10111; //拨号请求码
    public void call(Context context,String telPhone){
        strPhone=telPhone;
        if(checkReadPermission(context,Manifest.permission.CALL_PHONE,REQUEST_CALL_PERMISSION)){
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(telPhone));
            context.startActivity(intent);
        }
    }
    /**
     * 打电话
     * @param context
     * @param phoneNum
     *
    if(Utils.isNotEmpty(info)&&Utils.isNotEmpty(info.getService_phone())){
    new AlertView("联系我们",info.getService_phone(), "取消", new String[]{"拨打"}, null, mActivity, AlertView.Style.Alert, new OnItemClickListener() {
    @Override
    public void onItemClick(Object o, int position) {
    if(position==0){
    Utils.callPhone(mActivity,info.getService_phone());
    //                                    call(mActivity,info.getService_phone());
    }
    }
    }).show();
    }
     *
     */
    public static void callPhone(Context context, String phoneNum){
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        context.startActivity(intent);
    }
    public static void tel(final Context context, final String phoneNum){
        if(Utils.isNotEmpty(phoneNum)) {
            new AlertDialog(context).builder()
                    .setTitle("拨打电话")
                    .setMsg(phoneNum)
                    .setNegativeButton("取消", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                        }
                    })
                    .setPositiveButton("拨打", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            Uri data = Uri.parse("tel:" + phoneNum);
                            intent.setData(data);
                            context.startActivity(intent);
                        }
                    })
                    .show();
        }
    }
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /** 获取手机的密度*/
    public static float getDensity(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.density;
    }
    /**
     * 打开指定网页
     * @param context
     * @param url
     */
    public static  void goWeb(Context context,String url){
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(intent);
    }
    /**
     * 原生分享文本
     */
    public static void ShareInfo(Context context,String title,String msg){
        Intent textIntent = new Intent(Intent.ACTION_SEND);
        textIntent.setType("text/plain");
        textIntent.putExtra(Intent.EXTRA_TEXT, msg);
        context.startActivity(Intent.createChooser(textIntent, title));
    }

    /**
     * 原生分享单张图片
     * @param context
     * @param title
     * @param msg
     */
    public static void SharePhoto(Context context,String title,String msg){
//        String path =getResourcesUri(R.drawable.msg_icon,context);
//        Intent imageIntent = new Intent(Intent.ACTION_SEND);
//        imageIntent.setType("image/jpeg");
//        imageIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(path));
//        context.startActivity(Intent.createChooser(imageIntent, "分享"));
        Intent share_intent = new Intent();
        share_intent.setAction(Intent.ACTION_SEND);//设置分享行为
        share_intent.setType("image/jpeg");//设置分享内容的类型
        share_intent.putExtra(Intent.EXTRA_SUBJECT, title);//添加分享内容标题
        share_intent.putExtra(Intent.EXTRA_TEXT, msg);//添加分享内容
        //创建分享的Dialog
        share_intent = Intent.createChooser(share_intent, "分享");
        context.startActivity(share_intent);

    }
    private static String getResourcesUri(@DrawableRes int id,Context context) {
        Resources resources = context.getResources();
        String uriPath = ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                resources.getResourcePackageName(id) + "/" +
                resources.getResourceTypeName(id) + "/" +
                resources.getResourceEntryName(id);
        return uriPath;
    }
    public static  void ShareWeiXin(Context context,String msg){
        Intent wechatIntent = new Intent(Intent.ACTION_SEND);
        wechatIntent.setPackage("com.tencent.mm");
        wechatIntent.setType("text/plain");
        wechatIntent.putExtra(Intent.EXTRA_TEXT, msg);
        context.startActivity(wechatIntent);
    }
    public static  void ShareQQ(Context context,String msg){
        Intent qqIntent = new Intent(Intent.ACTION_SEND);
        qqIntent.setPackage("com.tencent.mobileqq");
        qqIntent.setType("text/plain");
        qqIntent.putExtra(Intent.EXTRA_TEXT, msg);
        context.startActivity(qqIntent);
    }
    private static  String getVersionName(Context context) throws Exception
    {
        // 获取packagemanager的实例
        PackageManager packageManager =context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(),0);
        String version = packInfo.versionName;
        return version;
    }
    /**
     * 返回当前程序版本名
     */
    public static String getAppVersionName(Context context) {
        String versionName = "";
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
        }
        return versionName;
    }
    /**
     * 隐藏软件盘
     */
    public  void hideSoftInput(final Activity activity) {
        InputMethodManager imm =
                (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (imm == null) return;
        View view = activity.getCurrentFocus();
        if (view == null) view = new View(activity);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void changeShapeBg(View view,String strcolor) {
        int color =  Color.parseColor(strcolor);
        GradientDrawable mGroupDrawable= (GradientDrawable) view.getBackground();
        mGroupDrawable.setColor(color);
        mGroupDrawable.setStroke(1, color);
    }
    public static File saveBitmapFile(Bitmap bitmap, String filepath){
        File file=new File(filepath);//将要保存图片的路径
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
    public static File compressImage(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 500) {  //循环判断如果压缩后图片是否大于500kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            options -= 10;//每次都减少10
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            long length = baos.toByteArray().length;
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date(System.currentTimeMillis());
        //图片名
        String filename = format.format(date);

        File file = new File(Environment.getExternalStorageDirectory(), filename + ".png");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            try {
                fos.write(baos.toByteArray());
                fos.flush();
                fos.close();
            } catch (IOException e) {

                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {

            e.printStackTrace();
        }

        recycleBitmap(bitmap);
        return file;
    }
    public static void recycleBitmap(Bitmap... bitmaps) {
        if (bitmaps == null) {
            return;
        }
        for (Bitmap bm : bitmaps) {
            if (null != bm && !bm.isRecycled()) {
                bm.recycle();
            }
        }
    }


    /**
     * 判断是否开启通知
     * @param context
     * @return
     */
    public static boolean isNotificationEnabled(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return isEnableV26(context);
        } else {
            return isEnableV19(context);
        }
    }
    public static boolean isEnableV19(Context context) {
        AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        ApplicationInfo appInfo = context.getApplicationInfo();
        String pkg = context.getApplicationContext().getPackageName();
        int uid = appInfo.uid;

        Class appOpsClass;
        try {
            appOpsClass = Class.forName(AppOpsManager.class.getName());
            Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE,
                    String.class);
            Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);

            int value = (Integer) opPostNotificationValue.get(Integer.class);
            return ((Integer) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) == AppOpsManager.MODE_ALLOWED);

        } catch (ClassNotFoundException e) {
            Logger.e(e+"");
        } catch (NoSuchMethodException e) {
            Logger.e(e+"");
        } catch (NoSuchFieldException e) {
            Logger.e(e+"");
        } catch (InvocationTargetException e) {
            Logger.e(e+"");
        } catch (IllegalAccessException e) {
            Logger.e(e+"");
        }
        return false;
    }
    public static boolean isEnableV26(Context context) {
        try {
            NotificationManager notificationManager = (NotificationManager)
                    context.getSystemService(Context.NOTIFICATION_SERVICE);
            Method sServiceField = notificationManager.getClass().getDeclaredMethod("getService");
            sServiceField.setAccessible(true);
            Object sService = sServiceField.invoke(notificationManager);

            ApplicationInfo appInfo = context.getApplicationInfo();
            String pkg = context.getApplicationContext().getPackageName();
            int uid = appInfo.uid;

            Method method = sService.getClass().getDeclaredMethod("areNotificationsEnabledForPackage"
                    , String.class, Integer.TYPE);
            method.setAccessible(true);
            return (boolean) method.invoke(sService, pkg, uid);
        } catch (Exception e) {
            Logger.e(e+"");
        }
        return false;
    }



    /**
     * 跳转到系统开启或关闭通知页面
     * @param activity
     */
    public static void jumpSetNoticityActivity(Activity activity) {
        ApplicationInfo appInfo = activity.getApplicationInfo();
        String pkg = activity.getApplicationContext().getPackageName();
        int uid = appInfo.uid;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                //这种方案适用于 API 26, 即8.0（含8.0）以上可以用
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, pkg);
                intent.putExtra(Settings.EXTRA_CHANNEL_ID, uid);
                //这种方案适用于 API21——25，即 5.0——7.1 之间的版本可以使用
                intent.putExtra("app_package", pkg);
                intent.putExtra("app_uid", uid);
                activity.startActivity(intent);
            } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setData(Uri.parse("package:" + activity.getPackageName()));
                activity.startActivity(intent);
            } else {
                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                activity.startActivity(intent);
            }
        } catch (Exception e) {
            Intent intent = new Intent(Settings.ACTION_SETTINGS);
            activity.startActivity(intent);
        }
    }


//    public static boolean isEmpty(List<?> list) {
//        return list == null || list.size() <= 0;
//    }
//    public static boolean isEmpty(@Nullable CharSequence str) {
//        return str == null || str.length() == 0;
//    }
//    public static boolean isEmpty(Object obj) {
//        return obj == null;
//    }
public static void viewSaveToImage(View view,Context context) {
    view.setDrawingCacheEnabled(true);
    view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
    view.setDrawingCacheBackgroundColor(Color.WHITE);

    // 把一个View转换成图片
    Bitmap cachebmp = loadBitmapFromView(view);

    FileOutputStream fos;
    String imagePath = "";
    File file;
    try {
        // 判断手机设备是否有SD卡
        boolean isHasSDCard = Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);
        if (isHasSDCard) {
            // SD卡根目录
            File sdRoot = Environment.getExternalStorageDirectory();
             file = new File(sdRoot, Calendar.getInstance().getTimeInMillis()+".png");
            fos = new FileOutputStream(file);
            imagePath = file.getAbsolutePath();
        } else
            throw new Exception("创建文件失败!");

        cachebmp.compress(Bitmap.CompressFormat.PNG, 90, fos);

        fos.flush();
        fos.close();
        Log.e("FFFF:",file.getAbsolutePath());
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getAbsolutePath())));
        SpUtil.getInstance().setStringValue("ewmPath",file.getAbsolutePath());
        ToastUtil.show("保存成功："+file.getAbsolutePath());
    } catch (Exception e) {
        e.printStackTrace();
    }
//    LogUtil.e("imagePath="+imagePath);

    view.destroyDrawingCache();
}

    private static Bitmap loadBitmapFromView(View v) {
        int w = v.getWidth();
        int h = v.getHeight();

        Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmp);

        c.drawColor(Color.WHITE);
        /** 如果不设置canvas画布为白色，则生成透明 */

        v.layout(0, 0, w, h);
        v.draw(c);

        return bmp;
    }
    /**
     * 判断字符串是否为null或全为空格
     *
     * @param s 待校验字符串
     * @return {@code true}: null或全空格<br> {@code false}: 不为null且不全空格
     */
    public static boolean isSpace(String s) {
        return (s == null || s.trim().length() == 0);
    }
    /**
     * 判断对象是否为空
     *
     * @param obj 对象
     * @return {@code true}: 为空<br>{@code false}: 不为空
     */
    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        }
        if (obj instanceof String && obj.toString().length() == 0) {
            return true;
        }
        if (obj.getClass().isArray() && Array.getLength(obj) == 0) {
            return true;
        }
        if (obj instanceof Collection && ((Collection) obj).isEmpty()) {
            return true;
        }
        if (obj instanceof Map && ((Map) obj).isEmpty()) {
            return true;
        }
        if (obj instanceof SparseArray && ((SparseArray) obj).size() == 0) {
            return true;
        }
        if (obj instanceof SparseBooleanArray && ((SparseBooleanArray) obj).size() == 0) {
            return true;
        }
        if (obj instanceof SparseIntArray && ((SparseIntArray) obj).size() == 0) {
            return true;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            if (obj instanceof SparseLongArray && ((SparseLongArray) obj).size() == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断对象是否非空
     *
     * @param obj 对象
     * @return {@code true}: 非空<br>{@code false}: 空
     */
    public static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }

}
