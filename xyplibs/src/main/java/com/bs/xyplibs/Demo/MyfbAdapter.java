package com.bs.xyplibs.Demo;

import android.view.View;
import android.widget.ImageView;

import com.bs.xyplibs.R;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2018/8/7.
 * 局部刷新
 *
 */
/**
public class MyfbAdapter extends BaseQuickAdapter<MyFbBean.DataBean.ListBean,BaseViewHolder> {

    public MyfbAdapter() {
        super(R.layout.item_my_fb);
    }
    public int type;
    public static final int NOTIFY_CHECK = 10089;

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
        int pos=holder.getAdapterPosition();
        MyFbBean.DataBean.ListBean item = mData.get(pos);
        if(payloads.isEmpty()){//为空，即不是调用notifyItemChanged(position,payloads)后执行的，也即在初始化时执行的--正常刷新
            if(pos%3==0){
                holder.getView(R.id.v_left_line).setVisibility(View.GONE);
            }else {
                holder.getView(R.id.v_left_line).setVisibility(View.VISIBLE);
            }
            if(item.getLogo()!=null&&item.getLogo().contains("http")){
                Glide.with(mContext).load(item.getLogo()).into((ImageView) holder.getView(R.id.iv_video_back));
            }
            holder.setText(R.id.tv_count,item.getCount());
            holder.addOnClickListener(R.id.iv_check);
            holder.addOnClickListener(R.id.iv_video_back);
            if(item.isCheck()){
                holder.setImageResource(R.id.iv_check,R.drawable.check_icon);
            }else{
                holder.setImageResource(R.id.iv_check,R.drawable.no_check);
            }


        }else if(payloads.size()>0&&payloads.get(0)instanceof Integer){//条目内局部刷新逻辑
             type= (int) payloads.get(0);// 刷新哪个部分 标志位
            switch (type){
                case NOTIFY_CHECK:
                    if(item.isCheck()){
                        holder.setImageResource(R.id.iv_check,R.drawable.check_icon);
                    }else{
                        holder.setImageResource(R.id.iv_check,R.drawable.no_check);
                    }
                   break;
            }
        }
    }

    @Override
    protected void convert(BaseViewHolder helper, MyFbBean.DataBean.ListBean item) {

    }
}
*/
