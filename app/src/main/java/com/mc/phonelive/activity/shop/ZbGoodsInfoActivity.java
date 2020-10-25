package com.mc.phonelive.activity.shop;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mc.phonelive.R;
import com.mc.phonelive.Utils;
import com.mc.phonelive.activity.AbsActivity;
import com.mc.phonelive.adapter.TypeSelectorAdapter;
import com.mc.phonelive.bean.GetGoodsAttrBean;
import com.mc.phonelive.bean.GoodsPicBean;
import com.mc.phonelive.bean.ScoreInfoBean;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.utils.AddSubUtils;
import com.mc.phonelive.utils.DataSafeUtils;
import com.mc.phonelive.utils.DisplayUtils;
import com.mc.phonelive.utils.EventBean;
import com.mc.phonelive.utils.GlideUtils;
import com.orhanobut.logger.Logger;
import com.rey.material.app.BottomSheetDialog;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoader;

import org.greenrobot.eventbus.Subscribe;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 *带直播的商品详情888888
 *
 *
 */

public class ZbGoodsInfoActivity extends AbsActivity {

    private com.youth.banner.Banner mBanner;
    private TextView mTv_title;
    private TextView mTv_score;
    private TextView mTv_ys;
    private LinearLayout mLl_ys;
    private TextView mTv_yx;
    private WebView mWv;
    private TextView mTv_jfz;
    private LinearLayout mLl_ljdh;
    private ImageView iv_back;
    private String goodsId="";
    private ArrayList<GetGoodsAttrBean> selectlist;
    private String pic="";
    private String popinfo="";
    private int defaultBuy=1;
    private String product_attrs="请选择规格";
    private TextView mTv_gg;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_zb_goods_info;
    }

    @Override
    protected void main() {
        goodsId=getIntent().getStringExtra("goodsId");
//        setBarModel(false);
        mBanner = (com.youth.banner.Banner) findViewById(R.id.banner);
        mTv_title = (TextView) findViewById(R.id.tv_title);
        mTv_score = (TextView) findViewById(R.id.tv_score);
        mTv_ys = (TextView) findViewById(R.id.tv_ys);
        mLl_ys = (LinearLayout) findViewById(R.id.ll_ys);
        mTv_yx = (TextView) findViewById(R.id.tv_yx);
        mWv = (WebView) findViewById(R.id.wv);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        mTv_jfz = (TextView) findViewById(R.id.tv_jfz);
        mLl_ljdh = (LinearLayout) findViewById(R.id.ll_ljdh);
        mBanner.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object o, ImageView imageView) {
                GoodsPicBean advBean= (GoodsPicBean) o;
                String url= advBean.getThumb();
                if(Utils.isNotEmpty(url)&&url.contains("http")){
                    GlideUtils.loadBannerImage(mContext,url,imageView);
                }
            }
        }).setBannerStyle(BannerConfig.CIRCLE_INDICATOR).setIndicatorGravity(BannerConfig.CENTER);
//        Utils.setViewWhp(mContext,true,mBanner,16,9);
        getInfo();
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mLl_ljdh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectorPop();
            }
        });
    }
    private void showSelectorPop() {//3规格，2，添加仓库，0，加入采购单 1，立即下单
        BottomSheetDialog mDialog = new BottomSheetDialog(mContext);
        View dialogView = View.inflate(mContext,R.layout.selector_view, null);
        int popHeight = DisplayUtils.getScreenHeight(mContext) * 3 / 4;
        RecyclerView rv=dialogView.findViewById(R.id.rv);
        TextView tv_jrcgd=dialogView.findViewById(R.id.tv_jrcgd);
        TextView tv_ljxd=dialogView.findViewById(R.id.tv_ljxd);
        ImageView miv = dialogView.findViewById(R.id.miv);
        AddSubUtils add_sub=dialogView.findViewById(R.id.add_sub_2);
        add_sub.setCurrentNumber(defaultBuy);
        add_sub.setOnChangeValueListener(new AddSubUtils.OnChangeValueListener() {
            @Override
            public void onChangeValue(int value, int position) {
                defaultBuy=value;
            }
        });
        if(Utils.isNotEmpty(pic)&&pic.contains("http")){
            GlideUtils.loadImage(mContext,pic,miv);
        }
        TextView tv_name=dialogView.findViewById(R.id.tv_name);
        TextView tv_content=dialogView.findViewById(R.id.tv_content);
        TextView pop_tv_add=dialogView.findViewById(R.id.pop_tv_add);
         mTv_gg=dialogView.findViewById(R.id.tv_gg);
         mTv_gg.setText(product_attrs.replace(",","  "));
        tv_name.setText(mTv_title.getText().toString());
        tv_content.setText(popinfo);
        TypeSelectorAdapter tpAdapter=new TypeSelectorAdapter();
        rv.setLayoutManager(Utils.getLvManager(mContext));
        rv.setAdapter(tpAdapter);

        tpAdapter.setNewData(selectlist);
        mDialog.contentView(dialogView)
                .heightParam(popHeight)
                .inDuration(500)
                .cancelable(true)
                .show();
        pop_tv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        tv_jrcgd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        tv_ljxd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//判断事件//3规格，2，添加仓库，0，加入采购单 1，立即下单
                mDialog.dismiss();
                setBuy();

            }
        });
    }
    @Subscribe
    public void getEvent(EventBean event){
        switch(event.getCode()){
            case "send_pop_info_from_tag":
                Logger.e("AAA:",event.getFirstObject());
                try{
                    product_attrs="";
                    if(Utils.isNotEmpty(selectlist)){
                        for(int i=0;i<selectlist.size();i++){
                            List<GetGoodsAttrBean.DiyAttrValue> atts = selectlist.get(i).getAttrs();
                            for(int j=0;j<atts.size();j++){
                                String check= atts.get(j).getCheck();
                                if(check.equals("true")){
                                    product_attrs=product_attrs+atts.get(j).getAttr()+",";
                                }
                            }
                        }
                        mTv_gg.setText(product_attrs.replace(",","  "));
                    }
                }catch (Exception e){
                    Logger.e("bbb:",event.getFirstObject());
                }

                break;
            default:

                break;
        }
    };
    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }
    private void setBuy() {
        HttpUtil.setOrder(goodsId, product_attrs, defaultBuy+"", new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                Intent intent=new Intent(mContext,ScoreGoodsOrderActivity.class);
                intent.putExtra("info",info);
                startActivity(intent);
                finish();
//                startActivity(new Intent(mContext,ScoreGoodsOrderActivity.class));
            }
        });
    }

    private void getInfo() {
        HttpUtil.getScoreInfo(goodsId,new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (!DataSafeUtils.isEmpty(info)) {
                    JSONObject obj = JSON.parseObject(info[0]);
                    ScoreInfoBean scoreInfoBean=JSON.parseObject(obj.getString("info"), ScoreInfoBean.class);
                    if(Utils.isNotEmpty(scoreInfoBean)){
                        mTv_title.setText(scoreInfoBean.getTitle());
                        mTv_ys.setText("已售"+scoreInfoBean.getSalenum()+"件");
                        mTv_jfz.setText(scoreInfoBean.getPrice());
                        mTv_score.setText(scoreInfoBean.getPrice());
                        popinfo=scoreInfoBean.getInfo();
                        pic=scoreInfoBean.getThumb();
                        try {
                            mWv.loadData(URLEncoder.encode(scoreInfoBean.getInfo(), "utf-8"), "text/html",  "utf-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                    ArrayList<GoodsPicBean> goods_pic = (ArrayList<GoodsPicBean>) JSON.parseArray(obj.getString("goods_pic"), GoodsPicBean.class);
                    selectlist = (ArrayList<GetGoodsAttrBean>) JSON.parseArray(obj.getString("goods_attr"), GetGoodsAttrBean.class);
                    if(Utils.isNotEmpty(selectlist)){
                        for(int i=0;i<selectlist.size();i++){
                            GetGoodsAttrBean getAtsBean = selectlist.get(i);
                            List<String> listNb = getAtsBean.getAttr_value();
                            List<GetGoodsAttrBean.DiyAttrValue> atslist = new ArrayList<>();
                            for(int j=0;j<listNb.size();j++){
                                atslist.add(j,new GetGoodsAttrBean.DiyAttrValue(listNb.get(j)));
                            }
                            getAtsBean.setAttrs(atslist);
                           selectlist.set(i,getAtsBean);
                        }
                    }

                    if(Utils.isNotEmpty(goods_pic)){
                        mBanner.setImages(goods_pic);
                        mBanner.start();
                    }

                }
            }
            @Override
            public void onFinish() {
                super.onFinish();

            }
        });
    }
}
