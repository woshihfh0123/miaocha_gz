package com.mc.phonelive.httpnet.md5;


import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES解密
 */
public class AesUtil {

//    public static final String AESKEY = MD5Utils.Md5("gC#gLeB#z@R33Tde");//从服务器要的密钥
    public static final String AESKEY = "QFYoTDW3t4oGuvao";
    public static final String VECTOR = "GbqV4qdZnr7oyaT4";//从服务器要的密钥

    public static String encrypt(String key, String initVector, String value) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(value.getBytes());
            System.out.println("encrypted string: " + Base64.encodeToString(encrypted, Base64.DEFAULT));

            return Base64.encodeToString(encrypted, Base64.DEFAULT);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public static String decrypt(String key, String initVector, String encrypted) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            byte[] original = cipher.doFinal(Base64.decode(encrypted, Base64.DEFAULT));

            return new String(original);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public static String getDioData(String dio) {
        if (!TextUtils.isEmpty(dio)) {

            String jiemi = AesUtil.decrypt(AesUtil.AESKEY, AesUtil.VECTOR, dio);
            Log.i("logger", "解密后:" + jiemi);

            if (!TextUtils.isEmpty(jiemi)) {
                return jiemi;

            }

        }
        return "";
    }
}
