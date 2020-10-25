package com.bs.xyplibs.bean;

import java.util.List;

/**
 * Created by Administrator on 2019/1/19.
 */

public class ContactModel {

//    private String no= CommentUtil.MD5(CommentUtil.getUniquePsuedoID());
    private String name;

    private List<String>phones;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhones() {
        String strPhone="";
      for(int i=0;i<phones.size();i++){
         strPhone=strPhone+ phones.get(i)+",";
      }

        return strPhone;
    }

    public void setPhones(List<String> phones) {
        this.phones = phones;
    }

//    public String getNo() {
//        return no;
//    }

//    public void setNo(String no) {
//        this.no = no;
//    }

    @Override
    public String toString() {
        return "ContactModel{" +
                "name='" + name + '\'' +
                ", phones=" + phones +
                '}';
    }

}
