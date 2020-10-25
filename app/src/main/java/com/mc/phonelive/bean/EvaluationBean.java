package com.mc.phonelive.bean;

import java.io.File;
import java.io.Serializable;
import java.util.List;

/**
 * @ProjectName: EvaluationDemo
 * @Package: com.itfitness.evaluationdemo.beans
 * @ClassName: EvaluationBean
 * @Description: 发表评价Bean，用于存储不同商品的评价
 */

public class EvaluationBean implements Serializable{
    private String  unique;
    private String  id;
    private String goods_id;
//    private String scores;//评分
//    private String is_anonymous;//是否匿名
    private String content;//评价内容
    private List<File> Images;//评价图片集合

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUnique() {
        return unique;
    }

    public void setUnique(String unique) {
        this.unique = unique;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }
//    public String getScores() {
//        return scores;
//    }
//
//    public void setScores(String scores) {
//        this.scores = scores;
//    }
//
//    public String getIs_anonymous() {
//        return is_anonymous;
//    }
//
//    public void setIs_anonymous(String is_anonymous) {
//        this.is_anonymous = is_anonymous;
//    }
//
//    public String getOrder_good_id() {
//        return order_good_id;
//    }
//
//    public void setOrder_good_id(String order_good_id) {
//        this.order_good_id = order_good_id;
//    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<File> getImages() {
        return Images;
    }

    public void setImages(List<File> images) {
        Images = images;
    }

    @Override
    public String toString() {
        return "EvaluationBean{" +
                ", content='" + content + '\'' +
                ", Images=" + Images +
                '}';
    }
}
