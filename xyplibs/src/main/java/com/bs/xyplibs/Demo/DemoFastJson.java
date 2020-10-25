package com.bs.xyplibs.Demo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

/**
 * Created by Administrator on 2019/2/14.
 * fastjson 是一个性能很好的 Java 语言实现的 JSON 解析器和生成器，来自阿里巴巴的工程师开发。 主要特点：
 * 1.快速FAST(比其它任何基于Java的解析器和生成器更快，包括jackson） 强大（支持普通JDK类包括任意Java Bean
 * 2.Class、Collection、Map、Date或enum） 零依赖（没有依赖其它任何类库除了JDK）
 */

public class DemoFastJson {
    /**
     *
    public static void main(String[] args) {
        String json = "{\"name\":\"chenggang\",\"age\":24}";
        String arrayAyy = "[[\'马云',50],null,[\'马化腾',30]]";
//        Entity2json("zhangsan", 24);
//        list2Json();
        Complexdata();
//        Deserialization(json);
//        DateFormate(new Date());
//        Json2Eetity(json);
//        String2JSONArray(arrayAyy);
    }

    // 实体转为Json
    public static void Entity2json(String name, int age) {
        Userinfo info = new Userinfo(name, age);
        String str_json = JSON.toJSONString(info); //
        System.out.println("实体转化为Json" + str_json);
    }

    // list转Json
    public static void list2Json() {
        List<Userinfo> list = new ArrayList<Userinfo>();
        Userinfo userinfo1 = new Userinfo("lisi", 15);
        Userinfo userinfo2 = new Userinfo("wangwu", 16);
        list.add(userinfo1);
        list.add(userinfo2);
        String json = JSON.toJSONString(list, true);
        System.out.println("List集合转json格式字符串 :" + json);
    }

    // 字符数组转化为JSon
    private static void String2JSONArray(String arrayAyy) {
        JSONArray array = JSONArray.parseArray(arrayAyy);
        System.out.println("数组：" + array);
        System.out.println("数组长度: " + array.size());
        Collection nuCon = new Vector();
        nuCon.add(null);
        array.removeAll(nuCon);
        System.out.println("数组：" + array);
        System.out.println("数组长度: " + array.size());
    }

    // 复杂数据类型
    public static void Complexdata() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("username", "zhangsan");
        map.put("age", 24);
        map.put("sex", "男");

        // map集合
        HashMap<String, Object> temp = new HashMap<String, Object>();
        temp.put("name", "xiaohong");
        temp.put("age", "23");
        map.put("girlInfo", temp);

        // list集合
        List<String> list = new ArrayList<String>();
        list.add("爬山");
        list.add("骑车");
        list.add("旅游");
        map.put("hobby", list);
        String jsonString = JSON.toJSONString(map);
        System.out.println("复杂数据类型:" + jsonString);
    }

    public static void Deserialization(String json) {
        Userinfo userInfo = JSON.parseObject(json, Userinfo.class);
        System.out.println("姓名是:" + userInfo.getName() + ", 年龄是:"
                + userInfo.getAge());
    }

    // 格式话日期
    public static void DateFormate(Date date) {
        System.out.println("输出毫秒值：" + JSON.toJSONString(date));
        System.out.println("默认格式为:"
                + JSON.toJSONString(date,
                SerializerFeature.WriteDateUseDateFormat));
        System.out.println("自定义日期："
                + JSON.toJSONStringWithDateFormat(date, "yyyy-MM-dd",
                SerializerFeature.WriteDateUseDateFormat));
    }

    // Json转为实体
    private static void Json2Eetity(String json) {
        Userinfo userInfo = JSON.parseObject(json, Userinfo.class);
        System.out.println("输出对象的地址：" + userInfo.toString());
        System.out.println("输出对象的名字：" + userInfo.getName());
    }
     */
}
