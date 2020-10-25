package com.mc.phonelive.adapter.foxtone;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mc.phonelive.AppConfig;
import com.mc.phonelive.R;
import com.mc.phonelive.bean.UserBean;
import com.mc.phonelive.bean.foxtone.TradingCenterListBean;
import com.mc.phonelive.glide.ImgLoader;
import com.mc.phonelive.utils.DataSafeUtils;

import java.util.List;

/**
 * 游戏中心
 */
public class TradingBuyListAdapter extends BaseQuickAdapter<TradingCenterListBean, BaseViewHolder> {
    private InfoClubListener mAddListener;
    public TradingBuyListAdapter(int layoutResId, @Nullable List<TradingCenterListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TradingCenterListBean item) {
        ImageView avator = helper.getView(R.id.item_avator);
        TextView mPhone = helper.getView(R.id.item_phone);
        TextView mLevel = helper.getView(R.id.item_level);
        TextView mCount = helper.getView(R.id.item_num);
        TextView mPrice = helper.getView(R.id.item_price);
        TextView mSellOut = helper.getView(R.id.item_sellout);

        UserBean bean = AppConfig.getInstance().getUserBean();
        if (!DataSafeUtils.isEmpty(bean)){
            String uid = bean.getId();
            if (item.getUid().equals(uid)){
                mSellOut.setVisibility(View.GONE);
            }else{
                mSellOut.setVisibility(View.VISIBLE);
            }
        }

        if (!DataSafeUtils.isEmpty(item.getAvatar_thumb())){
            ImgLoader.display(item.getAvatar_thumb(),avator);
        }
        if (!DataSafeUtils.isEmpty(item.getMobile())){
            mPhone.setText(item.getMobile());
        }
        if (!DataSafeUtils.isEmpty(item.getGrade_id())){
            mLevel.setText("Lv."+item.getGrade_id());
        }
        if (!DataSafeUtils.isEmpty(item.getNums())){
            mCount.setText("数量："+item.getNums());
        }
        if (!DataSafeUtils.isEmpty(item.getPrice())){
            mPrice.setText("单价："+item.getPrice());
        }


        mSellOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAddListener.addClubData(item.getId(),item.getNums());
            }
        });
    }

    public interface  InfoClubListener{
        void addClubData(String id,String count);
    }

    public void AddClubListener(InfoClubListener mListener) {
        this.mAddListener = mListener;
    }
}
