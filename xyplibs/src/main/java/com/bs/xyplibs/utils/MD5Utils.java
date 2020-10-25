package com.bs.xyplibs.utils;

import com.orhanobut.logger.Logger;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

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
        Logger.e("排序前："+ststemp[0]+"&"+ststemp[1]);
        Arrays.sort(ststemp);
        Logger.e("排序后："+ststemp[0]+"&"+ststemp[1]);
        String sorstr="";
        for(int i=ststemp.length-1;i>-1;i--){
            sorstr=sorstr+ststemp[i]+"&";
        }
        String sm=sorstr+Constant.MD5_TITLE;
        Logger.e("明码："+sm);
        Logger.e("加密："+ MD5Utils.encrypt(sm));
        String mdstr=MD5Utils.encrypt(sm);
        String []ps=new String[2];
        ps[1]=mdstr;
        ps[0]=tm.substring(0,10);
        return ps;
    }

    public final static String  encrypt(String plaintext) {
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
    // MD5変換
    public static String Md5(String str) {
        if (str != null && !str.equals("")) {
            try {
                MessageDigest md5 = MessageDigest.getInstance("MD5");
                char[] HEX = {
                        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
                };
                byte[] md5Byte = md5.digest(str.getBytes("UTF8"));
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < md5Byte.length; i++) {
                    sb.append(HEX[(int) (md5Byte[i] & 0xff) / 16]);
                    sb.append(HEX[(int) (md5Byte[i] & 0xff) % 16]);
                }
                str = sb.toString();
            } catch (NoSuchAlgorithmException e) {
            } catch (Exception e) {
            }
        }
        return str;
    }

}
