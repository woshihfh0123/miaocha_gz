package com.mc.phonelive.adapter.foxtone;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mc.phonelive.R;
import com.mc.phonelive.bean.foxtone.MusicCenterBean;
import com.mc.phonelive.glide.ImgLoader;
import com.mc.phonelive.utils.DataSafeUtils;

import java.util.List;

/**
 * 任务大厅
 */
public class FindMusicListAdapter extends BaseQuickAdapter<MusicCenterBean.InfoBean.ListBean, BaseViewHolder> {
    private MusicBuyListener mListener;
    public FindMusicListAdapter(int layoutResId, @Nullable List<MusicCenterBean.InfoBean.ListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MusicCenterBean.InfoBean.ListBean item) {
        if (!DataSafeUtils.isEmpty(item.getThumb())) {
            ImgLoader.display(item.getThumb(),(ImageView)helper.getView(R.id.music_bg));
        }
        if (!DataSafeUtils.isEmpty(item.getName())) {
            helper.setText(R.id.music_name, item.getName());
        }
        if (!DataSafeUtils.isEmpty(item.getFee())) {
            helper.setText(R.id.music_price, "价格：" + item.getFee() + "音豆");
        }
        if (!DataSafeUtils.isEmpty(item.getOutput())) {
            helper.setText(R.id.music_num, "产量：" + item.getOutput() + "音豆");
        }
        if (!DataSafeUtils.isEmpty(item.getCent())) {
            helper.setText(R.id.music_yin_count, "音分值：+" + item.getCent());
        }
        if (!DataSafeUtils.isEmpty(item.getDays())) {
            helper.setText(R.id.music_cycle, "周期：" + item.getDays() + "天");
        }
        if (!DataSafeUtils.isEmpty(item.getUpper())) {
            helper.setText(R.id.music_up_count, "领取上限：" + item.getUpper());
        }
        if (!DataSafeUtils.isEmpty(item.getCounts())) {
            helper.setText(R.id.music_now_count, "当前拥有：" + item.getCounts());
        }
        TextView mMusicPay = helper.getView(R.id.music_pay);
        TextView mMusicEndTime = helper.getView(R.id.music_end_time);
        if (!DataSafeUtils.isEmpty(item.getSurplus_days())) {
            mMusicPay.setVisibility(View.GONE);
            mMusicEndTime.setVisibility(View.VISIBLE);
            mMusicEndTime.setText("剩余周期：" + item.getSurplus_days() + "天");
        } else {
            mMusicPay.setVisibility(View.VISIBLE);
            mMusicEndTime.setVisibility(View.GONE);
        }
        if (item.getStatus().equals("1")) {
            mMusicPay.setText("立即购买");
            mMusicPay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (item.getStatus().equals("0")){
                        return;
                    }
                    mListener.MusicBuyData(item.getId(),item.getName());
                }
            });
        } else if (item.getStatus().equals("0")) {
            mMusicPay.setText("预留不开");
        }
    }

    public interface  MusicBuyListener{
       void MusicBuyData(String id,String name);
    }

    public void setMusicBuyLister(MusicBuyListener lister){
        this.mListener =lister;
    }
}
