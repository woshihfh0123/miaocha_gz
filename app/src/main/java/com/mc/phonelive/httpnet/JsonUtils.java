package com.mc.phonelive.httpnet;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by LYK on 2017/12/18.
 */

public class JsonUtils {

        public static String getSinglePara(String json, String para){
            if (json!=null)
                try {
                    JSONObject jsonObject=new JSONObject(json);
                    return jsonObject.getString(para);
                } catch (JSONException e) {

                }
            return  "";
        }

    public static int getIntPara(String json, String para){
        if (json!=null)
            try {
                JSONObject jsonObject=new JSONObject(json);
                return jsonObject.getInt(para);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        return  -1024;
    }

}
