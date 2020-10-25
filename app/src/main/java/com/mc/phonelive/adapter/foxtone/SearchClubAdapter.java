package com.mc.phonelive.adapter.foxtone;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mc.phonelive.AppConfig;
import com.mc.phonelive.R;
import com.mc.phonelive.adapter.RefreshAdapter;
import com.mc.phonelive.bean.foxtone.ClubBean;
import com.mc.phonelive.custom.RatioRoundImageView;
import com.mc.phonelive.glide.ImgLoader;
import com.mc.phonelive.utils.DataSafeUtils;

import java.util.List;

/**
 * Created by cxf on 2018/9/29.
 * 搜索列表
 */

public class SearchClubAdapter extends RefreshAdapter<ClubBean.InfoBean.ListBean> {
    private InfoClubListener mAddListener;
    private View.OnClickListener mFollowClickListener;
    private View.OnClickListener mClickListener;
    private String mUid;

    public SearchClubAdapter(Context context, int from) {
        super(context);
        mUid = AppConfig.getInstance().getUid();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Vh(mInflater.inflate(R.layout.club_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vh, int position, @NonNull List payloads) {
        Object payload = payloads.size() > 0 ? payloads.get(0) : null;
        ((Vh) vh).setData(mList.get(position), position, payload);
    }

    class Vh extends RecyclerView.ViewHolder {

        RatioRoundImageView club_head_img;
        TextView club_head_name;
        TextView club_head_phone;
        TextView club_head_people;
        TextView club_head_submit;
        public Vh(View itemView) {
            super(itemView);
            club_head_img = (RatioRoundImageView) itemView.findViewById(R.id.club_head_img);
            club_head_name = (TextView) itemView.findViewById(R.id.club_head_name);
            club_head_phone = (TextView) itemView.findViewById(R.id.club_head_phone);
            club_head_people = (TextView) itemView.findViewById(R.id.club_head_people);
            club_head_submit = (TextView) itemView.findViewById(R.id.club_head_submit);
        }

        void setData(ClubBean.InfoBean.ListBean item, int position, Object payload) {
            itemView.setTag(position);
            if (payload == null) {
                if (!DataSafeUtils.isEmpty(item.getBadge())) {
                    ImgLoader.displayAvatar(item.getBadge(), club_head_img);
                }
                if (!DataSafeUtils.isEmpty(item.getName())){
                    club_head_name.setText(item.getName());
                }
                if (!DataSafeUtils.isEmpty(item.getPhone())){
                    club_head_phone.setText("电话："+item.getPhone()+"");
                }
                if (!DataSafeUtils.isEmpty(item.getCounts())){
                    club_head_people.setText("人数："+item.getCounts()+"");
                }
                club_head_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mAddListener.addClubData(item.getId());
//                        ToastUtil.show("加入接口");
                    }
                });
            }
            }
        }



    public interface  InfoClubListener{
        void addClubData(String id);
    }

    public void AddClubListener(InfoClubListener mListener) {
        this.mAddListener = mListener;
    }

}
