package com.mc.phonelive.bean;

import java.util.List;

/**
 * Created by Administrator on 2019/1/19.
 */

public class ContactModel {

//    private String no= CommentUtil.MD5(CommentUtil.getUniquePsuedoID());
    private String nickname;

    private List<String>mobile;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }


    public String getMobile() {
        String strPhone="";
      for(int i=0;i<mobile.size();i++){
          if(i==mobile.size()-1){
              strPhone=strPhone+ mobile.get(i);
          }else{
              strPhone=strPhone+ mobile.get(i)+",";
          }
      }

        return strPhone;
    }

    public void setMobile(List<String> phones) {
        this.mobile = phones;
    }

//    public String getNo() {
//        return no;
//    }

//    public void setNo(String no) {
//        this.no = no;
//    }

    @Override
    public String toString() {
        return "{" +
                "nickname='" + nickname + '\'' +
                ", mobile=" + mobile +
                '}';
    }

}
