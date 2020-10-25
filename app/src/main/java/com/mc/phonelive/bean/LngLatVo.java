package com.mc.phonelive.bean;

import java.io.Serializable;

/**
 * 经纬度
 */
public class LngLatVo implements Serializable {
    private String id;
    private String lng;//经度
    private String lat;//维度
    private String cityname;//城市
    private String Countryname;//国家
    private String addressname;//地址名称
    private String adresslines;//街道

    public String getAdresslines() {
        return adresslines;
    }

    public void setAdresslines(String adresslines) {
        this.adresslines = adresslines;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }

    public String getCountryname() {
        return Countryname;
    }

    public void setCountryname(String countryname) {
        Countryname = countryname;
    }

    public String getAddressname() {
        return addressname;
    }

    public void setAddressname(String addressname) {
        this.addressname = addressname;
    }
}
