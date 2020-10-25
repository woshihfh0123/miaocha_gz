package com.bs.xyplibs.utils;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.widget.TextView;

import com.bs.xyplibs.R;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by Administrator on 2018/3/8.
 */
public class StringUtils {
    private StringUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }
    public static String getMonth() {
        Date date = new Date();
        //设置要获取到什么样的时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        //获取String类型的时间
        String createdate = sdf.format(date);
        return createdate;
    }

    public static String getDouble2(double dou) {
        DecimalFormat df = new DecimalFormat("######0.00");
        String dd = df.format(dou);
        return dd;
    }

    public static String getDouble4(double dou) {
        DecimalFormat df = new DecimalFormat("######0.0000");
        String dd = df.format(dou);
        return dd;
    }

    /**
     * 改变指定字符串中数字的颜色和大小，size=1,不改变大小
     * @param str
     * @param size
     * @return
     */
    public static SpannableStringBuilder setStringNumColor(String str, float size) {
        SpannableStringBuilder style = new SpannableStringBuilder(str);
        for (int i = 0; i < str.length(); i++) {
            char a = str.charAt(i);
            if (a >= '0' && a <= '9') {
                style.setSpan(new ForegroundColorSpan(Color.RED), i, i + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                style.setSpan(new RelativeSizeSpan(size), i, i + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            }
        }
        return style;
    }
    // 2015年 06 月 六月突出，即中间的字体突出来
    public static void setTextThree(Context context, TextView tv, String before, String center, String last, int color, float textSize) {
        SpannableString spanString = new SpannableString(before + center + last);
        spanString.setSpan(new ForegroundColorSpan(context.getResources().getColor(color)), before.length(), before.length() + center.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 设置前景色为洋红色
        spanString.setSpan(new RelativeSizeSpan(textSize), before.length(), before.length() + center.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 2.0f表示默认字体大小的两倍
        // spanString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),
        // before.length(), before.length() + center.length(),
        // Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //
        // 粗体
        tv.setText(spanString);
    }

    // 58人 58突出，即前面的字体突出来
//    StringUtils.setTextTwoBefore(this, mTv_sum_money, "5659", ".36万P", R.color.colorBlack, 3f);
    public static void setTextTwoBefore(Context context, TextView tv, String before, String last, int color, float textSize) {
        SpannableString spanString = new SpannableString(before + last);
        spanString.setSpan(new ForegroundColorSpan(context.getResources().getColor(color)), 0, before.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 设置前景色为洋红色
        spanString.setSpan(new RelativeSizeSpan(textSize), 0, before.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 2.0f表示默认字体大小的两倍
        // spanString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),
        // before.length(), before.length() + last.length(),
        // Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //
        // 粗体
        tv.setText(spanString);
    }

    // 58人 58突出，即前面的字体突出来
    public static void setTextTwoBeforeNoColor(Context context, TextView tv, String before, String last, float textSize) {
        SpannableString spanString = new SpannableString(before + last);
//        spanString.setSpan(new ForegroundColorSpan(context.getResources().getColor(color)), 0, before.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 设置前景色为洋红色
        spanString.setSpan(new RelativeSizeSpan(textSize), 0, before.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 2.0f表示默认字体大小的两倍
        // spanString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),
        // before.length(), before.length() + last.length(),
        // Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //
        // 粗体
        tv.setText(spanString);
    }


    // 58人 人字体突出来
    public static void setTextTwoLast(Context context, TextView tv, String before, String last, int color, float textSize) {
        SpannableString spanString = new SpannableString(before + last);
        spanString.setSpan(new ForegroundColorSpan(context.getResources().getColor(color)), before.length(), spanString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 设置前景色为洋红色
        spanString.setSpan(new RelativeSizeSpan(textSize), before.length(), spanString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 2.0f表示默认字体大小的两倍
        // spanString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),
        // before.length(), before.length() + last.length(),
        // Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //
        // 粗体
        tv.setText(spanString);
    }

    // 10小时0分 10 和 0 突出
    public static void setTextFourBefore(Context context, TextView tv, String one, String two, String three, String four, int color, float textSize) {
        SpannableString spanString = new SpannableString(one + two + three + four);
        spanString.setSpan(new ForegroundColorSpan(context.getResources().getColor(color)), 0, one.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 设置前景色为洋红色
        spanString
                .setSpan(new ForegroundColorSpan(context.getResources().getColor(color)), two.length() + one.length(), two.length() + one.length() + three.length(), Spanned
                        .SPAN_EXCLUSIVE_EXCLUSIVE); // 设置前景色为洋红色
        spanString.setSpan(new RelativeSizeSpan(textSize), 0, one.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 2.0f表示默认字体大小的两倍
        spanString.setSpan(new RelativeSizeSpan(textSize), two.length() + one.length(), two.length() + one.length() + three.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 2.0f表示默认字体大小的两倍
        // spanString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),
        // before.length(), before.length() + last.length(),
        // Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //
        // 粗体
        tv.setText(spanString);
    }

    /**
     * 把字节数组保存为一个文件
     *
     * @EditTime 2007-8-13 上午11:45:56
     */
    public static File getFileFromBytes(byte[] b, String outputFile) {
        BufferedOutputStream stream = null;
        File file = null;
        try {
            file = new File(outputFile);
            FileOutputStream fstream = new FileOutputStream(file);
            stream = new BufferedOutputStream(fstream);
            stream.write(b);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }

        return file;
    }

    public static void setDifferentTextColor(TextView tv, String content, String unit, String color) {
        if (tv != null)
            tv.setText(Html.fromHtml(content + "<font color='" + color + "'>" + unit + "</font>"));
    }

    public static void setDifferentTextColor(TextView tv, String content, String unit, int color) {
        if (tv != null)
            tv.setText(Html.fromHtml(content + "<font color='" + color + "'>" + unit + "</font>"));
    }

    public static void setDifferentTextColorBefore(TextView tv, String content, String unit, String color) {
        tv.setText(Html.fromHtml("<font color='" + color + "'>" + content + "</font>" + unit));
    }
    /**
     * 如果是多级不同颜色拼接需要组装数据
     *
     *
     //首先是拼接字符串
     String content ="共"+ "<font color=\"#FF7701\">" + item.getNum() + "</font>"+"张 | 已领取"+
     "<font color=\"#FF7701\">" +item.getAlready_num()+ "</font>"+"张 | 剩余"+"<font color=\"#FF7701\">" + item.getSurplus_num()+ "</font>" +"张";
     helper.setText(R.id.tv_info, Html.fromHtml(content));

     *
     */

    /**
     * 设置中间颜色与两边不同
     *
         setDifferentTextColorMiddle(tv_content,"回复",item.getReply().get(0).getNick_name(), item.getReply().get(0).getContent(),"#0000ff");


     * @param tv
     * @param str
     * @param content
     * @param unit
     * @param color
     */

    public static void setDifferentTextColorMiddle(TextView tv, String str, String content, String unit, String color) {
        tv.setText(Html.fromHtml(str + "<font color='" + color + "'>" + content + "</font>" + unit));
    }

    /**
     * 得到一个json对象
     *
     * @param key
     * @param value
     * @return
     */
    public static JSONObject getJsonObject(String key, Object value) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public static boolean ListIsContainsString(String[] arr, String targetValue) {
        return Arrays.asList(arr).contains(targetValue);
    }

    /**
     * 轨距尺数据显示专用
     *
     * @param str
     * @return
     */

    public static String getStringFromString(String str, boolean iscg) {
        DecimalFormat df = new DecimalFormat("0.00");
        if (str.length() == 7) {
            if (str.indexOf(".") != 4) {
                return "";
            }
            String fh = str.substring(0, 1);
            String info = str.substring(1);
            double data = Double.parseDouble(info);
            if (iscg) {
                boolean left = SUtils.getInstance().getLeft();//左基准股为true
                if (left) {
                    if (fh.equals("+")) {
                        fh = "-";
                    } else {
                        fh = "+";
                    }
                }
            }
            String sdata = fh + df.format(data);
            if (sdata.length() > 7) {
                return "";
            }
            return sdata;
        }

        return "";
    }

    public static String getfhString(String str, boolean iscg) {
        DecimalFormat df = new DecimalFormat("0.00");
        if (str.length() == 14) {
            if (str.indexOf("2E") != 8) {
                return "";
            }
            if (str.startsWith("2B") || str.startsWith("2D")) {//此为有符号数据
                String fh = str.substring(0, 2);
                String info = str.substring(2);
                String[] infoa = info.split("2E");
                String zs = infoa[0];
                String xs = infoa[1];
                String zsdata = "";
                for (int i = 0; i < zs.length(); i += 2) {
                    zsdata = zsdata + Integer.parseInt(zs.substring(i, i + 2), 16);
                }
                int intzs = Integer.parseInt(zsdata);
                String data = "";
                if (xs.length() == 4) {
                    String gw = xs.substring(0, 2);
                    String sw = xs.substring(2);
                    String intgw = Integer.parseInt(gw, 16) + "";
                    String intsw = Integer.parseInt(sw, 16) + "";
                    data = intzs + "." + intgw + intsw;
                }
                if (iscg) {//超高的数据受左右基准股的影响，会导致数据正负号变化
                    boolean left = SUtils.getInstance().getLeft();//左基准股为true
                    if (left) {//左基准股跟尺子数据相反
                        if (fh.equals("2B")) {
                            fh = "-";
                        } else {
                            fh = "+";
                        }
                    } else {//右基准股是默认基准股，与尺子数据一样
                        if (fh.equals("2B")) {
                            fh = "+";
                        } else {
                            fh = "-";
                        }
                    }
                } else {
                    if (fh.equals("2B")) {
                        fh = "+";
                    } else {
                        fh = "-";
                    }
                }
                String sdata = fh + data;
                if (sdata.length() > 7) {
                    return "";
                }
                return sdata;
            } else {
                String[] infoa = str.split("2E");
                String zs = infoa[0];
                String xs = infoa[1];
                String zsdata = "";
                for (int i = 0; i < zs.length(); i += 2) {
                    zsdata = zsdata + Integer.parseInt(zs.substring(i, i + 2), 16);
                }

                int intzs = Integer.parseInt(zsdata);
                String data = "";
                if (xs.length() == 4) {
                    String gw = xs.substring(0, 2);
                    String sw = xs.substring(2);
                    String intgw = Integer.parseInt(gw, 16) + "";
                    String intsw = Integer.parseInt(sw, 16) + "";
                    data = intzs + "." + intgw + intsw;
                    if (data.length() > 7) {
                        return "";
                    }
                    return data;
                }
            }

        }

        return "";
    }

    public static String getStringFromString(String str) {
        DecimalFormat df = new DecimalFormat("0.00");
        if (str.length() == 7) {
            if (str.indexOf(".") != 4) {
                return "";
            }
            String fh = str.substring(0, 1);
            String info = str.substring(1);
            double data = Double.parseDouble(info);
            String sdata = fh + df.format(data);
            if (sdata.length() > 7) {
                return "";
            }
            return sdata;
        }

        return "";
    }

    /**
     * 数组转换成十六进制字符串
     *
     * @param
     * @return HexString
     */
    public static final String bytesToHexString(byte[] bArray) {
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase() + "");
        }
        return sb.toString();
    }

    public static byte[] hexStringToByte(String hex) {
        int len = (hex.length() / 2);
        byte[] result = new byte[len];
        char[] achar = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
        }
        return result;
    }

    private static int toByte(char c) {
        byte b = (byte) "0123456789ABCDEF".indexOf(c);
        return b;
    }

    public static String makeChecksum(String hexdata) {
        if (hexdata == null || hexdata.equals("")) {
            return "00";
        }
        hexdata = hexdata.replaceAll(" ", "");
        int total = 0;
        int len = hexdata.length();
        if (len % 2 != 0) {
            return "00";
        }
        int num = 0;
        while (num < len) {
            String s = hexdata.substring(num, num + 2);
            total += Integer.parseInt(s, 16);
            num = num + 2;
        }
        return hexInt(total);
    }

    /**
     *
     * @param s
     * @return
     */
    public static String SHA1(String s) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();
            return toHexString(messageDigest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String toHexString(byte[] keyData) {
        if (keyData == null) {
            return null;
        }
        int expectedStringLen = keyData.length * 2;
        StringBuilder sb = new StringBuilder(expectedStringLen);
        for (int i = 0; i < keyData.length; i++) {
            String hexStr = Integer.toString(keyData[i] & 0x00FF, 16);
            if (hexStr.length() == 1) {
                hexStr = "0" + hexStr;
            }
            sb.append(hexStr);
        }
        return sb.toString();
    }
    private static String hexInt(int total) {
        int a = total / 256;
        int b = total % 256;
        if (a > 255) {
            return hexInt(a) + format(b);
        }
        return format(a) + format(b);
    }

    private static String format(int hex) {
        String hexa = Integer.toHexString(hex);
        int len = hexa.length();
        if (len < 2) {
            hexa = "0" + hexa;
        }
        return hexa;
    }

    public static String convertHexToString(String hex) {

        StringBuilder sb = new StringBuilder();
        StringBuilder temp = new StringBuilder();

        //49204c6f7665204a617661 split into two characters 49, 20, 4c...
        for (int i = 0; i < hex.length() - 1; i += 2) {

            //grab the hex in pairs
            String output = hex.substring(i, (i + 2));
            //convert hex to decimal
            int decimal = Integer.parseInt(output, 16);
            //convert the decimal to character
            sb.append((char) decimal);

            temp.append(decimal);
        }

        return sb.toString();
    }

    public static byte[] toByteArray(String hexString) {
        if (TextUtils.isEmpty(hexString))
            throw new IllegalArgumentException("this hexString must not be empty");

        hexString = hexString.toLowerCase();
        final byte[] byteArray = new byte[hexString.length() / 2];
        int k = 0;
        for (int i = 0; i < byteArray.length; i++) {//因为是16进制，最多只会占用4位，转换成字节需要两个16进制的字符，高位在先
            byte high = (byte) (Character.digit(hexString.charAt(k), 16) & 0xff);
            byte low = (byte) (Character.digit(hexString.charAt(k + 1), 16) & 0xff);
            byteArray[i] = (byte) (high << 4 | low);
            k += 2;
        }
        return byteArray;
    }

    public static String StringToHex16String(String _str) {
        //将字符串转换成字节数组。
        byte[] buffer = toByteArray(_str);

        //定义一个string类型的变量，用于存储转换后的值。
        String result = "";
        for (int i = 0; i < buffer.length; i++) {
            //将每一个字节数组转换成16进制的字符串，以空格相隔开。
//            result += Convert.ToString(buffer[i], 16) + " ";
//            result += bytesToHexString(buffer[i], 16) + " ";
        }
        return result;
    }

    public static byte[] getByteFromFile(File file) {
        byte[] buffer = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

    /**
     * 根据byte数组，生成文件
     */
    public static File getFile(byte[] bfile, String filePath, String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            File dir = new File(filePath);
            if (!dir.exists() && dir.isDirectory()) {//判断文件目录是否存在
                dir.mkdirs();
            } else {
                dir.delete();
                dir.mkdir();
            }
            file = new File(filePath + "/" + fileName);
            Logger.e("pppppppppppppppppppp:" + filePath + "/" + fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bfile);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return file;
    }

    public static File byte2File(byte[] buf, String filePath, String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            File dir = new File(filePath);
            if (!dir.exists() && dir.isDirectory()) {
                dir.mkdirs();
            }
            file = new File(filePath + File.separator + fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(buf);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return file;
        }
        }
    /**
     * 判断两字符串是否相等
     *
     * @param a 待校验字符串a
     * @param b 待校验字符串b
     * @return {@code true}: 相等<br>{@code false}: 不相等
     */
    public static boolean equals(CharSequence a, CharSequence b) {
        if (a == b) return true;
        int length;
        if (a != null && b != null && (length = a.length()) == b.length()) {
            if (a instanceof String && b instanceof String) {
                return a.equals(b);
            } else {
                for (int i = 0; i < length; i++) {
                    if (a.charAt(i) != b.charAt(i)) return false;
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 判断两字符串忽略大小写是否相等
     *
     * @param a 待校验字符串a
     * @param b 待校验字符串b
     * @return {@code true}: 相等<br>{@code false}: 不相等
     */
    public static boolean equalsIgnoreCase(String a, String b) {
        return (a == b) || (b != null) && (a.length() == b.length()) && a.regionMatches(true, 0, b, 0, b.length());
    }

    /**
     * null转为长度为0的字符串
     *
     * @param s 待转字符串
     * @return s为null转为长度为0字符串，否则不改变
     */
    public static String null2Length0(String s) {
        return s == null ? "" : s;
    }

    /**
     * 返回字符串长度
     *
     * @param s 字符串
     * @return null返回0，其他返回自身长度
     */
    public static int length(CharSequence s) {
        return s == null ? 0 : s.length();
    }

    /**
     * 首字母大写
     *
     * @param s 待转字符串
     * @return 首字母大写字符串
     */
    public static String upperFirstLetter(String s) {
        if (Utils.isEmpty(s) || !Character.isLowerCase(s.charAt(0))) return s;
        return String.valueOf((char) (s.charAt(0) - 32)) + s.substring(1);
    }

    /**
     * 首字母小写
     *
     * @param s 待转字符串
     * @return 首字母小写字符串
     */
    public static String lowerFirstLetter(String s) {
        if (Utils.isEmpty(s) || !Character.isUpperCase(s.charAt(0))) return s;
        return String.valueOf((char) (s.charAt(0) + 32)) + s.substring(1);
    }

    /**
     * 反转字符串
     *
     * @param s 待反转字符串
     * @return 反转字符串
     */
    public static String reverse(String s) {
        int len = length(s);
        if (len <= 1) return s;
        int mid = len >> 1;
        char[] chars = s.toCharArray();
        char c;
        for (int i = 0; i < mid; ++i) {
            c = chars[i];
            chars[i] = chars[len - i - 1];
            chars[len - i - 1] = c;
        }
        return new String(chars);
    }

    /**
     * 转化为半角字符
     *
     * @param s 待转字符串
     * @return 半角字符串
     */
    public static String toDBC(String s) {
        if (Utils.isEmpty(s)) return s;
        char[] chars = s.toCharArray();
        for (int i = 0, len = chars.length; i < len; i++) {
            if (chars[i] == 12288) {
                chars[i] = ' ';
            } else if (65281 <= chars[i] && chars[i] <= 65374) {
                chars[i] = (char) (chars[i] - 65248);
            } else {
                chars[i] = chars[i];
            }
        }
        return new String(chars);
    }

    /**
     * 转化为全角字符
     *
     * @param s 待转字符串
     * @return 全角字符串
     */
    public static String toSBC(String s) {
        if (Utils.isEmpty(s)) return s;
        char[] chars = s.toCharArray();
        for (int i = 0, len = chars.length; i < len; i++) {
            if (chars[i] == ' ') {
                chars[i] = (char) 12288;
            } else if (33 <= chars[i] && chars[i] <= 126) {
                chars[i] = (char) (chars[i] + 65248);
            } else {
                chars[i] = chars[i];
            }
        }
        return new String(chars);
    }
}
