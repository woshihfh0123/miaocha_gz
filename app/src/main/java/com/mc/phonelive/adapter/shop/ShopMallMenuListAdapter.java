package com.mc.phonelive.adapter.shop;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mc.phonelive.R;
import com.mc.phonelive.bean.ShopMallBean;
import com.mc.phonelive.utils.DataSafeUtils;

import java.util.List;

/**
 * 购物车第一层店铺列表
 */
public class ShopMallMenuListAdapter extends BaseQuickAdapter<ShopMallBean.DataBean.InfoBean.ShopMallMenuList, BaseViewHolder> {
    private Context mContext;
    private FoodCartGoodsAdapter mFoodAdapter;
    private List<ShopMallBean.DataBean.InfoBean.ShopMallMenuList> shoplists;
    //    private String cartlist = "";
    int mFlash = 0;

    public ShopMallMenuListAdapter(Context context, int layoutResId, @Nullable List<ShopMallBean.DataBean.InfoBean.ShopMallMenuList> data) {
        super(layoutResId, data);
        this.mContext = context;
        this.shoplists = data;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final ShopMallBean.DataBean.InfoBean.ShopMallMenuList item) {
        if (!DataSafeUtils.isEmpty(item.getThumb())){
            Glide.with(mContext).load(item.getThumb()).into((ImageView) helper.getView(R.id.menu_img));
        }
        if (!DataSafeUtils.isEmpty(item.getCate_name())){
            helper.setText(R.id.menu_title,item.getCate_name());
        }
    }

}
