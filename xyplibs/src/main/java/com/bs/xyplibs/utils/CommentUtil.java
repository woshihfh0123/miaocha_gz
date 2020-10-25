package com.bs.xyplibs.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.allenliu.versionchecklib.v2.AllenVersionChecker;
import com.allenliu.versionchecklib.v2.builder.DownloadBuilder;
import com.allenliu.versionchecklib.v2.builder.NotificationBuilder;
import com.allenliu.versionchecklib.v2.builder.UIData;
import com.allenliu.versionchecklib.v2.callback.CustomVersionDialogListener;
import com.allenliu.versionchecklib.v2.callback.ForceUpdateListener;
import com.bs.xyplibs.R;
import com.bs.xyplibs.base.BaseApp;
import com.bs.xyplibs.dialog.DownloadDialog;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import static android.content.Context.TELEPHONY_SERVICE;
import static android.os.Environment.DIRECTORY_DOWNLOADS;

/**
 * Created by admin on 2017/12/18.
 */

public class CommentUtil {

    private static Toast sToast;


    /**
     * toast弹出文本，下一个弹出的文本覆盖上一个的文本
     * @param text 吐司内容
     */
    public static void showSingleToast(String text) {
        if (sToast == null) {
            sToast = Toast.makeText( BaseApp.getInstance(), text, Toast.LENGTH_SHORT);
        }
        sToast.setText(text);
        sToast.show();
    }

    /**
     * dip---px转化
     * @param dp
     * @return
     */

    public static float dpToPx(float dp) {

        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, BaseApp.getInstance().getResources().getDisplayMetrics());
    }

    /**
     * 获取当前时间
     * @return
     */
    public static String getCurrentTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date());
    }
    public static String getCurrentTime1() {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(new Date());
    }

    /**

     *

     * @param context 上下文

     * @param cls  启动activity的类名

     */
    public static void startActivity(Context context, Class<?> cls){
        Intent intent=new Intent(context,cls);
        context.startActivity(intent);
    }

    /**
     * 判断是否联网
     * @return
     */
    public static boolean checkNetwork(){

        boolean rs = false;
        // step 1. 获取一个连接管理器

        ConnectivityManager cm = (ConnectivityManager) BaseApp.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        // step 2. 获取当前可用的网络的信息对象  android.Manifest.permission.ACCESS_NETWORK_STATE.

        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        if(networkInfo == null){
            return false;
        }

        // step 3.获取网络类型

        int type = networkInfo.getType();

        switch (type) {
            case ConnectivityManager.TYPE_WIFI:
            case ConnectivityManager.TYPE_MOBILE:
                rs = true;
                break;
            default:
                break;
        }
        return rs;
    }


    /**
     * 判断网络是否是WIFI状态
     * @return
     */
    public static  boolean checkWifi(){
        boolean rs = false;
        // step 1. 获取一个连接管理器
        ConnectivityManager cm = (ConnectivityManager) BaseApp.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        // step 2. 获取当前可用的网络的信息对象  android.Manifest.permission.ACCESS_NETWORK_STATE.
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if(networkInfo == null){
            return false;
        }
        // step 3.获取网络类型

        int type = networkInfo.getType();

        if (type== ConnectivityManager.TYPE_WIFI){
            return true;
        }
        return rs;
    }

    /**

     * 获取运营商名字

     */
    public static String getOperatorName() {
        TelephonyManager telephonyManager = (TelephonyManager) BaseApp.getInstance().getSystemService(TELEPHONY_SERVICE);
        String operator = telephonyManager.getSimOperator();
        if (operator != null) {
            if (operator.equals("46000") || operator.equals("46002")) {
                return "中国移动";
            } else if (operator.equals("46001")) {
                return  "中国联通";
            } else if (operator.equals("46003")) {
                return "中国电信";
            }
        }
        return "";
    }


    /**
     * 获取设备号
     * @return
     */

    public static String getDeviceId(){
        return getUniquePsuedoID()+getSystemVersion();
    }

    /**
     * 获取手机系统版本
     * @return
     */

    public static String getSystemVersion(){
        return  Build.VERSION.RELEASE;
    }


    //获得独一无二的Psuedo ID

    public static String getUniquePsuedoID() {
        String serial ="";

        String m_szDevIDShort = "35" +
                Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +

                Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 +

                Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +

                Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +

                Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +

                Build.TAGS.length() % 10 + Build.TYPE.length() % 10 +

                Build.USER.length() % 10; //13 位
        try {
            serial = Build.class.getField("SERIAL").get(null).toString();
            //API>=9 使用serial号

            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception) {
            //serial需要一个初始化

            serial = "serial"; // 随便一个初始化

        }
        //使用硬件信息拼凑出来的15位号码

        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }

    /**

     * 获取设备分辨率

     * @param context

     * @return

     */
    public static String getDisplay(Context context){

        return getDisplayHeight(context)+"X"+getDisplayWidth(context);

    }

    /**
     * 获取屏幕宽度
     * @param context
     * @return
     */
    public static int  getDisplayWidth(Context context){
        Activity activity= (Activity) context;
        WindowManager manager = activity.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);

        return outMetrics.widthPixels;

    }

    /**
     * 获取屏幕高度
     * @param context
     * @return
     */

    public static int getDisplayHeight(Context context){
        Activity activity= (Activity) context;
        WindowManager manager = activity.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    /**
     * 拷贝Assets文件到file里面
     * @param context
     * @param filename
     */

    public static void copyAssetsToFile(Context context, String filename){
        InputStream in = null;
        OutputStream out = null;
        if (!new File(context.getFilesDir(),filename).exists()){

            try {
                in =context.getResources().getAssets().open(filename);
                out = new FileOutputStream(new File(context.getFilesDir(),filename));

                byte[] buffer = new byte[1024];
                int len = 0;
                while((len=in.read(buffer)) != -1){
                    out.write(buffer, 0, len);
                }
                out.flush();
                Log.i("test","复制成功");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                try {
                    if(in!=null){
                        in.close();
                    }
                    if(out!=null){
                        out.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

    }


    /**
     * 获取APP版本名
     * @return
     */
    public static String getAppVersionName() {
        String versionName = "";
        try {
            // ---get the package info---

            PackageManager pm = BaseApp.getInstance().getPackageManager();
            PackageInfo pi = pm.getPackageInfo(BaseApp.getInstance().getPackageName(), 0);
            versionName = pi.versionName;
            //            versioncode = pi.versionCode;

            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versionName;
    }


    /**
     * 获取手机型号
     * @return
     */

    public static String getSystemModel(){
        return Build.MODEL;
    }

    /**
     * 如果输入法打开则关闭，如果没打开则打开
     */

    public static void openOrCloseInput(){
        InputMethodManager imm = (InputMethodManager) BaseApp.getInstance().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 显示键盘
     * @param view
     */
    public static void openInput(View view) {
        InputMethodManager im = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        im.showSoftInput(view, 0);
    }

    /**
     * 关闭输入法
     * @param view
     */

    public static void closeInput(View view){
        InputMethodManager inputMethodManager = (InputMethodManager)view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken()
                , InputMethodManager.HIDE_NOT_ALWAYS);
    }


    /**
     * 拨打客服电话
     */
    public static void callCustomTel(Context context){
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + "");
        intent.setData(data);
        context.startActivity(intent);
    }

    /**
     * 拨打客服电话
     */
    public static void callPhoneTel(Context context, String phoneNumber){
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:"+phoneNumber);
        intent.setData(data);
        context.startActivity(intent);
    }

    /**
     * 设置当前activity的状态栏颜色
     * @param statueBarColor   设置的颜色
     */
    public static void setStatueBarColor(Context context, int statueBarColor){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                AppCompatActivity activity= (AppCompatActivity)context;
                Window window = activity.getWindow();
                window.setStatusBarColor(statueBarColor);   //这里动态修改颜色
            }
    }


    /**
     * 播放mp3
     */
    public static MediaPlayer playMp3(Context context, int source){

        final MediaPlayer player = new MediaPlayer();

        try {
            player.setDataSource(context, Uri.parse("android.resource://"+context.getPackageName()+"/"+source));
            //媒体播放  先准备

            //本地准备

            //			player.prepare();

            //异步的准备

            player.prepareAsync();
            //网络准备

            //设置 准备成功的监听

            player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                @Override
                public void onPrepared(MediaPlayer mp) {
                    // TODO Auto-generated method stub

                    player.start();


                }
            });
        } catch (Exception e) {
            // TODO Auto-generated catch block

            e.printStackTrace();
        }
        return player;
    }

    /**
     * 播放mp3
     */
    public static MediaPlayer playMp3( String mp3_path){

        final MediaPlayer player = new MediaPlayer();

        try {

            player.setDataSource(mp3_path);
            //媒体播放  先准备

            //异步的准备

            player.prepareAsync();
            //网络准备

            //设置 准备成功的监听

            player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                @Override
                public void onPrepared(MediaPlayer mp) {
                    // TODO Auto-generated method stub

                    player.start();
                    player.setLooping(true);


                }
            });
        } catch (Exception e) {
            // TODO Auto-generated catch block

            e.printStackTrace();
        }
        return player;
    }


    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    //  读取assets中的json文件
        public static String getAssetsJson(Context context, String fileName) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                AssetManager assetManager = context.getAssets();
                InputStream inputStream = assetManager.open(fileName);
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                byte[] buffer = new byte[1024];
                int len;
                while ((len = bufferedInputStream.read(buffer)) != -1) {
                    baos.write(buffer, 0, len);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                try {
                    baos.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return baos.toString();
        }

    public static boolean sdCardIsAvailable() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().canWrite();
        } else
            return false;
    }

    public static File getAppRootPath(Context context) {
        if (sdCardIsAvailable()) {
            return Environment.getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS);
        } else {
            return context.getFilesDir();
        }
    }


    /**
     * 获取文件或者目录的大小
     * @param file
     * @return 返回的是以M为单位的
     */
    public static double getDirSize(File file) {
        //判断文件是否存在
        if (file.exists()) {
            //如果是目录则递归计算其内容的总大小
            if (file.isDirectory()) {
                File[] children = file.listFiles();
                double size = 0;
                for (File f : children)
                    size += getDirSize(f);
                return size;
            } else {//如果是文件则直接返回其大小,以“兆”为单位
                double size = (double) file.length() / 1024 / 1024;
                return size;
            }
        } else {
            System.out.println("文件或者文件夹不存在，请检查路径是否正确！");
            return 0.0;
        }
    }

    public static String doubleNumberFormat(double number){
        DecimalFormat decimalFormat=new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
        return decimalFormat.format(number);//format 返回的是字符串
    }


    /**
     * 删除文件
     * @param file
     */
    public static void deleteFile(File file){
        if (file.exists()){
            if (file.isDirectory()){
                File[] children = file.listFiles();
                for (File f: children) {
                    deleteFile(f);
                }
            }else {
                file.delete();
            }
        }
    }


    /**
     * 弹出确认对话框
     * @param context
     * @param content  内容
     * @param onClickListener  确定回调
     */
    public static void showDialog(Context context, String content, final View.OnClickListener onClickListener){

        final Dialog dialog = new Dialog(context, R.style.custom_confirm_dialog);
        LinearLayout root = (LinearLayout) LayoutInflater.from(context).inflate(
                R.layout.view_custom_confirm_dialog, null);

        TextView tv_dialog_content=root.findViewById(R.id.tv_dialog_content);
        TextView tv_dialog_cancel=root.findViewById(R.id.tv_dialog_cancel);
        TextView tv_dialog_confirm=root.findViewById(R.id.tv_dialog_confirm);
        tv_dialog_content.setText(content);
        tv_dialog_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    dialog.dismiss();
                }catch (Exception e){

                }
            }
        });

        tv_dialog_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             try {
                 dialog.dismiss();
                 if (onClickListener!=null){
                     onClickListener.onClick(view);
                 }
             }catch (Exception e){

             }
            }
        });


        dialog.setContentView(root);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent); //设置对话框背景透明 ，对于AlertDialog 就不管用了

        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        lp.x = 0; // 新位置X坐标
        lp.y = -20; // 新位置Y坐标
//        lp.width = (int) BaseApp.getInstance().getResources().getDisplayMetrics().widthPixels; // 宽度
        //      lp.height = WindowManager.LayoutParams.WRAP_CONTENT; // 高度
        //      lp.alpha = 9f; // 透明度
        root.measure(0, 0);
        lp.width=root.getMeasuredWidth();
        lp.height = root.getMeasuredHeight();
        lp.alpha = 9f; // 透明度
        dialogWindow.setAttributes(lp);


        dialog.show();


    }


    /**

     * Bitmap保存成File

     *

     * @param bitmap input bitmap

     * @param name output file's name

     * @return String output file's path

     */

    public  static String bitmap2File(Bitmap bitmap, String name) {

        File f = new File(CommentUtil.getAppRootPath( BaseApp.getInstance()) +"/shiyixiu/"+ name +  ".jpg");

        if  (f.exists()) f.delete();

        FileOutputStream fOut = null;

        try  {

            fOut = new FileOutputStream(f);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);

            fOut.flush();

            fOut.close();

        } catch (IOException e) {

            return  null;

        }

        return  f.getAbsolutePath();

    }

    /**
     * 获取视频第一帧图片
     * @param videoPath
     * @return
     */
    public static  String getVideoFirstImage(String videoPath){
        MediaMetadataRetriever media = new MediaMetadataRetriever();
        media.setDataSource(videoPath);
        Bitmap bitmap = media.getFrameAtTime();
        return  bitmap2File(bitmap, System.currentTimeMillis() + "");
    }


    /**
     * 获取当前本地apk的版本
     *
     * @param mContext
     * @return
     */
    public static int getVersionCode(Context mContext) {
        int versionCode = 0;
        try {
            //获取软件版本号，对应AndroidManifest.xml下android:versionCode
            versionCode = mContext.getPackageManager().
                    getPackageInfo(mContext.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 版本更新
     */
    public static void updateVersion(Context context,String content, String apkPath, final boolean isForceUpdate) {
        DownloadBuilder builder= AllenVersionChecker
                .getInstance()
                .downloadOnly(UIData.create().setTitle("版本更新").setContent(content).setDownloadUrl(apkPath));

        builder.setNotificationBuilder(
                NotificationBuilder.create()
                        .setRingtone(true)
                        .setIcon(R.mipmap.ic_launcher)
                        .setTicker("正在下载新版本")
                        .setContentTitle("食艺秀")
                        .setContentText("正在下载,请稍后...")
        );

        /**
         * 强制更新
         */
        builder.setForceUpdateListener(new ForceUpdateListener() {
            @Override
            public void onShouldForceUpdate() {

                if (isForceUpdate){
                    BaseApp.removeAllActivity();

                }

            }
        });

        builder.setCustomVersionDialogListener(new CustomVersionDialogListener() {
            @Override
            public Dialog getCustomVersionDialog(Context context, UIData versionBundle) {
                DownloadDialog downloadDialog = new DownloadDialog(context, R.style.DownloadDialog, R.layout.custom_download_dialog_layout);
                TextView textView = downloadDialog.findViewById(R.id.tv_dialog_content);
                textView.setText(versionBundle.getContent());
                downloadDialog.setCanceledOnTouchOutside(true);
                return downloadDialog;
            }
        });

//        builder.excuteMission(context);

    }


}
