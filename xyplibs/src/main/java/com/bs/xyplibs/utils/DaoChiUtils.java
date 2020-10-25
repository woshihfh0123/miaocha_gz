package com.bs.xyplibs.utils;

import android.os.Handler;

import com.inuker.bluetooth.library.connect.response.BleWriteResponse;
import com.orhanobut.logger.Logger;

import java.util.Random;

/**
 * Created by Administrator on 2018/9/20.
 */

public class DaoChiUtils {
    /**
     *解析接收到的数据
     * @param sb 传入的完整数据
     * @param start 帧头
     * @param length  数据长度
     * @return
     */
    public static String[] getStrA(StringBuffer sb,String start,int length,boolean isChaoGao){

        if(length==70){
            if(sb.toString().indexOf(start)!=-1){
                sb.delete(0,sb.toString().lastIndexOf(start));
                if(sb.length()==length){
                    /**
                     * 数据解析
                     */
                    //校验数据
                    String disStr=sb.toString().substring(0,68);
                    /////////////////易报错
//                    String sum= StringUtils.makeChecksum(disStr.substring(0,sb.length()-6)).toUpperCase();
                    String sum= StringUtils.makeChecksum(disStr.substring(0,disStr.length()-4)).toUpperCase();
                    String jym=sb.toString().substring(sb.length()-6,sb.length()-2).toUpperCase();
                    if(sum.equals(jym)){
                        //校验正确，显示并发送A0
                        String data=sb.toString().substring(6,64);
                        //有效数据29个字节，
                        String cg=data.substring(0,14);
                        String gj=data.substring(14,28);
                        String cz=data.substring(28,42);
                        String hb=data.substring(42,56);
                        String dl=data.substring(56);
                        String discg=StringUtils.getfhString(cg,true);
                        String disgj=StringUtils.getfhString(gj,false);
                        String discz=StringUtils.getfhString(cz,false);
                        String dishb=StringUtils.getfhString(hb,false);
                        String disdl=Integer.parseInt(dl)+"";
                        String[] sta=new String[]{discg,disgj,discz,dishb,disdl};
                        sb.delete(0,sb.length());
                        return sta;
                    }else{
                        String[] sta=new String[]{"1"};
                        sb.delete(0,sb.length());
                        return sta;
                    }
                }else if(sb.length()>length){
                    String[] sta=new String[]{"1"};
                    sb.delete(0,sb.length());
                    return sta;
                }
            }
        }else if(length==40){
            if(sb.toString().indexOf(start)!=-1){
                sb.delete(0,sb.toString().lastIndexOf(start));
                Logger.e("tStringBuf:"+sb.toString());
                if(sb.length()==40){
                    Logger.e("PPPPPP2："+sb.toString());
                    /**
                     * 数据解析
                     */
                    //校验数据
                    if(sb.toString().equals(SUtils.getInstance().getLastDatau())){
                        sb.delete(0,sb.length());
                    }
                    SUtils.getInstance().setLastDatau(sb.toString());
                    String disStr=sb.toString().substring(0,38);
//                    String disStr=sb.toString().substring(0,sb.indexOf("CC")-1);
//                    String sum= StringUtils.makeChecksum(disStr.substring(0,sb.length()-6)).toUpperCase();
                    String sum= StringUtils.makeChecksum(disStr.substring(0,disStr.length()-4)).toUpperCase();
                    String jym=sb.toString().substring(sb.length()-6,sb.length()-2).toUpperCase();
                    Logger.e("tStringBuf校验码："+sum);
                    Logger.e("tStringBuf校验码jym："+jym);
                    if(sum.equals(jym)){
                        //校验正确，显示并发送A0
                        String data=sb.toString().substring(6,34);
                        //有效数据29个字节，
                        String cg=data.substring(0,14);
                        String gj=data.substring(14);
                        String strone = StringUtils.getfhString(cg, isChaoGao);
                        String strtwo = StringUtils.getfhString(gj, false);
                        String[] sta=new String[]{strone,strtwo};
                        sb.delete(0,sb.length());
                        return sta;
                    }else{
                        String[] sta=new String[]{"1"};
                        sb.delete(0,sb.length());
                        return sta;
                    }


                }else if(sb.length()>40){
                    String[] sta=new String[]{"1"};
                    sb.delete(0,sb.length());
                    return sta;
                }
            }
        }
        return null;
    }

    /**
     * 手机发送命令，道尺相关
     * @param str
     */
    public static void sendCMD(final String str) {
        new Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        final byte[] bytes=StringUtils.hexStringToByte(str);

                        ClientManager.getClient().write(SUtils.getInstance().getBleMac(), Constant.services, Constant.cNUid, bytes, new BleWriteResponse() {
                            @Override
                            public void onResponse(int code) {
                                if(str.equals("C0")){
                                    Logger.e("PPPPPP2:"+str);
                                    Logger.e("SEND_CMD_CCCC:"+str+"------"+code);
                                }else if(str.equals("E0")){
                                    Logger.e("PPPPPP2:"+str);
                                    Logger.e("SEND_CMD_EEEE:"+str+"------"+code);
                                }else{
                                    Logger.e("SEND_CMD_=====:"+str+"--------"+code);
                                }
                            }
                        });
                    }
                }, new Random().nextInt(50));

                }



}
