package com.bs.xyplibs.callback;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bs.xyplibs.base.BaseVO;
import com.google.gson.Gson;

/**
 * Created by Administrator on 2019/2/14.
 */

public class MyException extends IllegalStateException {
    private BaseVO baseVO;
    public MyException(String s){
        super(s);
//        baseVO=new Gson().fromJson(s,BaseVO.class);
        baseVO= JSON.parseObject(s,BaseVO.class);
    }
    public BaseVO getBaseVO(){
        return baseVO;
    }

}
