package com.mc.phonelive.activity.shop;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.mc.phonelive.R;
import com.mc.phonelive.activity.AbsActivity;
import com.mc.phonelive.activity.WebViewActivity;
import com.mc.phonelive.adapter.shop.ShopMallMenuListAdapter;
import com.mc.phonelive.bean.ShopMallBean;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.utils.DataSafeUtils;
import com.mc.phonelive.views.shopmall.MainShopMainViewHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 更多分类
 * created by WWL on 2020/4/17 0017:14
 */
public class ShopMallMoreMenuListActivity extends AbsActivity {
    @BindView(R.id.titleView)
    TextView titleView;
    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.view_title)
    FrameLayout viewTitle;
    @BindView(R.id.menu_recyclerview)
    RecyclerView menu_recyclerview;
    List<ShopMallBean.DataBean.InfoBean.ShopMallMenuList> mMenuList = new ArrayList<>();
    ShopMallMenuListAdapter mMenuAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.more_menu_list;
    }

    protected boolean isStatusBarWhite() {
        return true;
    }

    @Override
    protected void main() {
        titleView.setText("全部分类");
        getShopMenuAdapter(mMenuList);

        initHttpData();
    }

    private void initHttpData() {
        HttpUtil.MoreCategory(new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
               List<ShopMallBean.DataBean.InfoBean.ShopMallMenuList> mList = JSON.parseArray(Arrays.toString(info), ShopMallBean.DataBean.InfoBean.ShopMallMenuList.class);
               if (mList!=null && mList.size()>0){
                   if (mMenuAdapter!=null){
                       mMenuAdapter.setNewData(mList);
                   }
               }
            }
        });
    }

    /**
     * menu按钮排序
     *
     * @param list
     */
    private void getShopMenuAdapter(List<ShopMallBean.DataBean.InfoBean.ShopMallMenuList> list) {

        mMenuAdapter = new ShopMallMenuListAdapter(mContext, R.layout.view_main_shop_menu_more, list);
        menu_recyclerview.setAdapter(mMenuAdapter);
        menu_recyclerview.setLayoutManager(new GridLayoutManager(mContext, 5));
        mMenuAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ShopMallBean.DataBean.InfoBean.ShopMallMenuList data = mMenuAdapter.getData().get(position);
                if (!DataSafeUtils.isEmpty(data.getWeb_url())) {
                    WebViewActivity.forward(mContext,data.getWeb_url());
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("typelist", MainShopMainViewHolder.mTypelist);
                    bundle.putString("id",data.getId());
                    Intent intent = new Intent(mContext,ShopMallGoodsListActivity.class);
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                }
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
