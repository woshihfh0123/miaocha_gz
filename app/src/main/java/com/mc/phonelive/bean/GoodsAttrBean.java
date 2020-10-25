package com.mc.phonelive.bean;

import java.util.List;

public class GoodsAttrBean {

    private String id;
    private String goods_id;
    private String attr_name;
    private List<AttrValue> attr_value;

    public List<AttrValue> getAttr_value() {
        return attr_value;
    }

    public void setAttr_value(List<AttrValue> attr_value) {
        this.attr_value = attr_value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getAttr_name() {
        return attr_name;
    }

    public void setAttr_name(String attr_name) {
        this.attr_name = attr_name;
    }
    public class AttrValue {
        private String attr;
        private String check="0";

        public String getAttr() {
            return attr;
        }

        public void setAttr(String attr) {
            this.attr = attr;
        }

        public String getCheck() {
            return check;
        }

        public void setCheck(String check) {
            this.check = check;
        }
    }

}
