package com.mc.phonelive.bean;

/**
 * 实时追踪物流
 * 作者： 周旭 on 2017/5/15/0015.
 * 邮箱：374952705@qq.com
 * 博客：http://www.jianshu.com/u/56db5d78044d
 */

public class Trace {
    private int type; //类型，0：当前位置（最新的一条物流信息），1：历史记录
    private String AcceptTime; //接收时间
    private String AcceptStation; //接收站点和描述
    private String Remark;//

    public Trace() {
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Trace(int type, String acceptTime, String acceptStation,String remark) {
        this.type = type;
        this.AcceptTime = acceptTime;
        this.AcceptStation = acceptStation;
        this.Remark =remark;
    }

    public String getAcceptTime() {
        return AcceptTime;
    }

    public void setAcceptTime(String acceptTime) {
        AcceptTime = acceptTime;
    }

    public String getAcceptStation() {
        return AcceptStation;
    }

    public void setAcceptStation(String acceptStation) {
        AcceptStation = acceptStation;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }
}
