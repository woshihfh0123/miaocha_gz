package com.bs.xyplibs.utils;

import android.content.Context;
import android.content.res.AssetManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/8/29.
 */

public class JsonUtils {
    /**
     * Convert object, list, ... to Json
     * 转为json格式输出
     * @param obj
     * @return
     */
    public static String toJson(Object obj) {
        Gson gson = new Gson();
        return gson.toJson(obj);
    }

    public static String getJson(Context context, String fileName) {

        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = context.getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
    /**
     * Convert a json string to Object
     * 将json转为object对象
     * @param json
     * @param clazz
     * @return
     */
    public static <T> T jsonToObject(String json, Class<T> clazz) {
        Gson gson = new Gson();
        return gson.fromJson(json, clazz);
    }

    /**
     * Convert a json string to List<?>
     * 将json转为list集合
     * @param json
     * @return
     */
    public static <T> List<T> jsonToList(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, new TypeToken<List<T>>() {
        }.getType());
    }

    /**
     * Convert a json string to ArrayList<?>
     * 将json转为ArrayList
     * @param json
     * @return
     */
    public static <T> ArrayList<T> jsonToArrayList(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, new TypeToken<ArrayList<T>>() {
        }.getType());
    }

    /**
     * Convert a json string to Map<?, ?>
     * 将json转为map
     * @param json
     * @return
     */
    public static <K, V> Map<K, V> jsonToMap(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, new TypeToken<Map<K, V>>() {
        }.getType());
    }

    /**
     * Convert a json string to Generic<T>
     * 将json转为泛型
     * @param json
     * @param <T>
     * @return
     */
    public static <T> T jsonToGeneric(String json, TypeToken<T> token) {
        Gson gson = new Gson();
        return gson.fromJson(json, token.getType());
    }
    /**
     * 得到一个json类型的字符串对象
     * @param key
     * @param value
     * @return
     */
    public static String getJsonString(String key, Object value)
    {
        JSONObject jsonObject = new JSONObject();
        //put和element都是往JSONObject对象中放入 key/value 对
        try {
            jsonObject.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        jsonObject.element(key, value);
        return jsonObject.toString();
    }

    /**
     * 得到一个json对象
     * @param key
     * @param value
     * @return
     */
    public static JSONObject getJsonObject(String key, Object value)
    {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
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
