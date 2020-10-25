package com.mc.phonelive.bean;

/**
 * Created by cxf on 2017/9/21.
 * 我的钻石
 */

public class CoinBean {

    private String id;
    private String coin;
    private String money;
    private String give;
    private String money_ios;
    private String product_id;
    private boolean isChecked;

    public String getMoney_ios() {
        return money_ios;
    }

    public void setMoney_ios(String money_ios) {
        this.money_ios = money_ios;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCoin() {
        return coin;
    }

    public void setCoin(String coin) {
        this.coin = coin;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getGive() {
        return give;
    }

    public void setGive(String give) {
        this.give = give;
    }
}
