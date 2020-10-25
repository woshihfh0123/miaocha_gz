package com.bs.xyplibs.bean;

import com.bs.xyplibs.base.BaseVO;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/2/26.
 */

public class YzLoginBean extends BaseVO{


    /**
     * data : {"id":21,"mobile":"18057261583","pwd":"E10ADC3949BA59ABBE56E057F20F883E","cover":"/file/pic/cover/82bbc24c-e1e9-4a6e-85d5-a882b5aab883.jpg","parentId":20,"hierarchyId":"0,1,18,20,","code":"e9bd","vip":1,"vipExpiredTime":null,"level":3,"status":1,"ip":"36.18.3.150","createTime":"2019-08-01T09:27:08.000+0000","smsCode":null,"inviteCode":null,"newPwd":null,"sessionId":"b1daadcf-35fb-4863-b467-cf32994dcf39","name":"海螺人","idCard":null,"address":null,"email":null,"hideData":"false","condition1":null,"condition2":null,"needSetNick":false,"nickName":null,"summary":"你比颜良文丑如何!!","hasVipGift":false,"icons":["/file/pic/medal/3bb84c40-498a-4751-8a67-356e7211657b.png"]}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        /**
         * id : 21
         * mobile : 18057261583
         * pwd : E10ADC3949BA59ABBE56E057F20F883E
         * cover : /file/pic/cover/82bbc24c-e1e9-4a6e-85d5-a882b5aab883.jpg
         * parentId : 20
         * hierarchyId : 0,1,18,20,
         * code : e9bd
         * vip : 1
         * vipExpiredTime : null
         * level : 3
         * status : 1
         * ip : 36.18.3.150
         * createTime : 2019-08-01T09:27:08.000+0000
         * smsCode : null
         * inviteCode : null
         * newPwd : null
         * sessionId : b1daadcf-35fb-4863-b467-cf32994dcf39
         * name : 海螺人
         * idCard : null
         * address : null
         * email : null
         * hideData : false
         * condition1 : null
         * condition2 : null
         * needSetNick : false
         * nickName : null
         * summary : 你比颜良文丑如何!!
         * hasVipGift : false
         * icons : ["/file/pic/medal/3bb84c40-498a-4751-8a67-356e7211657b.png"]
         */

        private int id;
        private String mobile;
        private String pwd;
        private String cover;
        private int parentId;
        private String hierarchyId;
        private String code;
        private int vip;
        private Object vipExpiredTime;
        private int level;
        private int status;
        private String ip;
        private String createTime;
        private Object smsCode;
        private Object inviteCode;
        private Object newPwd;
        private String sessionId;
        private String name;
        private Object idCard;
        private String address;
        private Object email;
        private String hideData;
        private Object condition1;
        private Object condition2;
        private boolean needSetNick;
        private String nickName;
        private String summary;
        private boolean hasVipGift;
        private List<String> icons;




        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getPwd() {
            return pwd;
        }

        public void setPwd(String pwd) {
            this.pwd = pwd;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public int getParentId() {
            return parentId;
        }

        public void setParentId(int parentId) {
            this.parentId = parentId;
        }

        public String getHierarchyId() {
            return hierarchyId;
        }

        public void setHierarchyId(String hierarchyId) {
            this.hierarchyId = hierarchyId;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public int getVip() {
            return vip;
        }

        public void setVip(int vip) {
            this.vip = vip;
        }

        public Object getVipExpiredTime() {
            return vipExpiredTime;
        }

        public void setVipExpiredTime(Object vipExpiredTime) {
            this.vipExpiredTime = vipExpiredTime;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public Object getSmsCode() {
            return smsCode;
        }

        public void setSmsCode(Object smsCode) {
            this.smsCode = smsCode;
        }

        public Object getInviteCode() {
            return inviteCode;
        }

        public void setInviteCode(Object inviteCode) {
            this.inviteCode = inviteCode;
        }

        public Object getNewPwd() {
            return newPwd;
        }

        public void setNewPwd(Object newPwd) {
            this.newPwd = newPwd;
        }

        public String getSessionId() {
            return sessionId;
        }

        public void setSessionId(String sessionId) {
            this.sessionId = sessionId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Object getIdCard() {
            return idCard;
        }

        public void setIdCard(Object idCard) {
            this.idCard = idCard;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public Object getEmail() {
            return email;
        }

        public void setEmail(Object email) {
            this.email = email;
        }

        public String getHideData() {
            return hideData;
        }

        public void setHideData(String hideData) {
            this.hideData = hideData;
        }

        public Object getCondition1() {
            return condition1;
        }

        public void setCondition1(Object condition1) {
            this.condition1 = condition1;
        }

        public Object getCondition2() {
            return condition2;
        }

        public void setCondition2(Object condition2) {
            this.condition2 = condition2;
        }

        public boolean isNeedSetNick() {
            return needSetNick;
        }

        public void setNeedSetNick(boolean needSetNick) {
            this.needSetNick = needSetNick;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public boolean isHasVipGift() {
            return hasVipGift;
        }

        public void setHasVipGift(boolean hasVipGift) {
            this.hasVipGift = hasVipGift;
        }

        public List<String> getIcons() {
            return icons;
        }

        public void setIcons(List<String> icons) {
            this.icons = icons;
        }
    }
}
