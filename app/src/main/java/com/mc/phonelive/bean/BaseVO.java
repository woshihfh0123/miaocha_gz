package com.mc.phonelive.bean;

import java.io.Serializable;

/**
 * created by WWL on 2020/4/7 0007:11
 */
public class BaseVO implements Serializable {


    /**
     * ret : 200
     * data : {"code":0,"msg":"","info":[{"id":"1","uid":"11521","real_name":"旺仔","phone":"15571005523","district":"普陀龙湾","detail":"湖北省襄阳市樊城区卧龙大道88号","addtime":"2020-04-07 11:17","is_default":"1"}]}
     * msg :
     */

    private int ret;
    private String msg;

    public int getRet() {
        return ret;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
