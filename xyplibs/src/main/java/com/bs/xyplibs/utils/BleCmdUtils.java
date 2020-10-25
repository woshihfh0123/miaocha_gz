package com.bs.xyplibs.utils;

import android.bluetooth.BluetoothGatt;

import com.bs.xyplibs.bean.MessageEvent;
import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.utils.HexUtil;
import com.orhanobut.logger.Logger;

/**
 * Created by Administrator on 2018/9/20.
 */

public class BleCmdUtils {


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
    public static String[] getInfo(StringBuffer sb,String start,boolean isChaoGao){
        if(start.equals("FF54")){
            String disStr=sb.toString().substring(0,68);
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
                sb.setLength(0);
                return sta;
            }else{
                String[] sta=new String[]{"1"};
                sb.setLength(0);
                return sta;
            }
        }else {
            if (sb.toString().equals(SUtils.getInstance().getLastDatau())) {
                sb.setLength(0);
                String[] sta = new String[]{"1", "2", SUtils.getInstance().getLastDatau()};
                return sta;
            }
            SUtils.getInstance().setLastDatau(sb.toString());
            String disStr = sb.toString().substring(0, 38);
            String sum = StringUtils.makeChecksum(disStr.substring(0, disStr.length() - 4)).toUpperCase();
            String jym = sb.toString().substring(sb.length() - 6, sb.length() - 2).toUpperCase();
            if (sum.equals(jym)) {
                //校验正确，显示并发送A0
                String data = sb.toString().substring(6, 34);
                //有效数据29个字节，
                String cg = data.substring(0, 14);
                String gj = data.substring(14);
                String strone = StringUtils.getfhString(cg, isChaoGao);
                String strtwo = StringUtils.getfhString(gj, false);
                String[] sta = new String[]{strone, strtwo};
                sb.delete(0, sb.length());
                return sta;
            } else {//校验失败
                String[] sta = new String[]{"1"};
                sb.setLength(0);
                return sta;
            }

        }

    }

    /**
     * 手机发送命令，道尺相关
     * @param str
     */
    public static void sendCMD(BleDevice bleDevice, final String str) {
        BleManager.getInstance().write(
                bleDevice,
                Constant.services+"",
                Constant.cNUid+"",
                HexUtil.hexStringToBytes(str),
                new BleWriteCallback() {
                    @Override
                    public void onWriteSuccess(int current, int total, byte[] justWrite) {
                        // 发送数据到设备成功
                        Logger.e("发送成功："+str);
                    }

                    @Override
                    public void onWriteFailure(BleException exception) {
                        // 发送数据到设备失败
                        Logger.e("发送失败："+str);
                    }
                });

    }


    /**
     * 蓝牙连接
     */
    public static void connectBleDevice(final String mac){
        BleManager.getInstance().connect(mac, new BleGattCallback() {
            @Override
            public void onStartConnect() {
                Logger.e("ble:"+"开始连接蓝牙");
            }

            @Override
            public void onConnectFail(BleDevice bleDevice, BleException exception) {
                Logger.e("ble:"+"蓝牙连接失败"+bleDevice.getName());
                try {
                    Thread.sleep(200);
                    connectBleDevice(mac);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {
                Logger.e("ble:"+"蓝牙连接成功"+bleDevice.getMac()+"-"+bleDevice.getName());
                EventBusUtil.sendEvent(new MessageEvent(100,bleDevice));
            }

            @Override
            public void onDisConnected(boolean isActiveDisConnected, BleDevice device, BluetoothGatt gatt, int status) {
                Logger.e("ble:"+"蓝牙连接断开");

            }
        });
    }
}
