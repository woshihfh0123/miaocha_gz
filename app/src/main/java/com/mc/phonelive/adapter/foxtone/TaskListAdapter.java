package com.mc.phonelive.adapter.foxtone;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mc.phonelive.R;
import com.mc.phonelive.activity.MainActivity;
import com.mc.phonelive.bean.foxtone.MusicCenterBean;
import com.mc.phonelive.im.EventBusModel;
import com.mc.phonelive.utils.DataSafeUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * 任务大厅
 */
public class TaskListAdapter extends BaseQuickAdapter<MusicCenterBean.InfoBean.NowListBean.ExtraListBean, BaseViewHolder> {
    TaskListener mListener;
    public TaskListAdapter(int layoutResId, @Nullable List<MusicCenterBean.InfoBean.NowListBean.ExtraListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MusicCenterBean.InfoBean.NowListBean.ExtraListBean item) {
        if (!DataSafeUtils.isEmpty(item.getIcon())) {
            Glide.with(mContext).load(item.getIcon()).into((ImageView) helper.getView(R.id.task_img));
        }
        if (!DataSafeUtils.isEmpty(item.getTitle())) {
            helper.setText(R.id.task_title, item.getTitle()+"("+item.getComplete_nums()+"/"+item.getNums()+")");
        }
//        if (!DataSafeUtils.isEmpty(item.getCount01())) {
//            helper.setText(R.id.task_yindou, "" + item.getCount01());
//        }
//        if (!DataSafeUtils.isEmpty(item.getCount02())) {
//            helper.setText(R.id.task_yinfenzhi, "" + item.getCount02());
//        }
//        if (!DataSafeUtils.isEmpty(item.getCoount03())) {
//            helper.setText(R.id.task_shuliandu, "" + item.getCoount03());
//        }


        TextView mMusicPay = helper.getView(R.id.task_submit);
        if (!DataSafeUtils.isEmpty(item.getIs_complete_name())){
            mMusicPay.setText(item.getIs_complete_name());
        }
        if (item.getIs_complete().equals("1")){//1去完成  2去领取 3 已领取
            mMusicPay.setBackgroundResource(R.drawable.address_submit_bg);
        }else {
            mMusicPay.setBackgroundResource(R.drawable.task_submit_bg);
            mMusicPay.setBackgroundResource(R.drawable.task_submit_bg);
        }

        mMusicPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.getIs_complete().equals("1")){
                    EventBus.getDefault().post(new EventBusModel("startIndex"));
                    MainActivity.forward(mContext);
                    return;
                }
                if (item.getIs_complete().equals("2")) {
                    mListener.taskReceive(item.getId(), item.getType());
                    return;
                }
            }
        });
    }


    public interface  TaskListener{
        void taskReceive(String id,String type);
    }

    public void setReceiveLister(TaskListener lister){
        this.mListener =lister;
    }
}
