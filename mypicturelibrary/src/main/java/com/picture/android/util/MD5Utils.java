package com.picture.android.util;

import android.util.Log;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Collections;

/**
 * Created by Administrator on 2018/3/2.
 */

public class MD5Utils {
    public static  String[] getString(String[] str){
        String tm=System.currentTimeMillis()+"";
        String temptime="timestamp="+tm.substring(0,10);
        String[]ststemp=new String[str.length+1];
        for(int i=0;i<ststemp.length;i++){
            if(i==ststemp.length-1){
                ststemp[i]=temptime;
            }else{
                ststemp[i]=str[i];
            }
        }
        Arrays.sort(ststemp,Collections.reverseOrder());
        String sorstr="";
        for(int i=0;i<ststemp.length;i++){
            sorstr=sorstr+ststemp[i]+"&";
        }

        /**
         * 签名
         */
        String SIGN = "LTAIrSF1cod3nXXC";
        String sm=sorstr+ SIGN;
        Log.e("msg","加密前："+sm);
        String mdstr=MD5Utils.encrypt(sm);
        Log.e("msg","加密后："+mdstr);
        String[]ps=new String[2];
        ps[1]=mdstr;
        ps[0]=tm.substring(0,10);
        return ps;
    }

    public final static String encrypt(String plaintext) {
        char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f' };
        try {
            byte[] btInput = plaintext.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }
}
