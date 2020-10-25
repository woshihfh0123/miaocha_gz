package com.bs.xyplibs.utils;

import java.util.Arrays;
import java.util.Map;

/**
 * Created by Administrator on 2018/12/7.
 * 北盛加密专用
 */

public class BsSignUtils {
    /**
     * 根据参数生成加密sign数据
     * Constant.MD5_TITLE  没个加密项目专用区别字符串
     * @param map  参数map
     * @return  整个请求字符串
     */
    public static String getMdInfo(Map<String,String> map) {
        if(!Utils.isEmpty(map)){
            String tm=System.currentTimeMillis()+"";
            String temptime=tm.substring(0,10);
            map.put("token","xxoo");
            map.put("timestamp",temptime);
            String ss="";
            String []stra=new String[map.size()];
            int is=0;
            for(String key:map.keySet()){
                stra[is]=key+"="+map.get(key);
                is++;
            }
            Arrays.sort(stra);
            for(int i=stra.length-1;i>-1;i--){
                ss=ss+stra[i]+"&";
            }
            String stsign=ss+ Constant.MD5_TITLE;
            String mySign= MD5Utils.Md5(stsign);//此为加密后的Sign
           String getInfo= "?"+ss+"sign="+mySign;//此为网址后面所有的数据，包括？
            return getInfo;

        }
        return "";

    }
}
