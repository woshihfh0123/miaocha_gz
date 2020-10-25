package com.mc.phonelive.adapter.foxtone;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mc.phonelive.R;
import com.mc.phonelive.adapter.RefreshAdapter;
import com.mc.phonelive.bean.foxtone.ClubMemberBean;
import com.mc.phonelive.custom.RatioRoundImageView;
import com.mc.phonelive.glide.ImgLoader;
import com.mc.phonelive.utils.DataSafeUtils;

import java.util.List;

/**
 * Created by cxf on 2018/9/29.
 * 搜索列表
 */

public class SearchClubMemberAdapter extends RefreshAdapter<ClubMemberBean.InfoBean> {

    private String mType="";//是否是创始人
    InfoClubListener mClubListener;
    public SearchClubMemberAdapter(Context context,String type) {
        super(context);
        this.mType =type;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Vh(mInflater.inflate(R.layout.club_member_item_view, parent, false));
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

        RatioRoundImageView item_avator;
        TextView item_username;
        TextView item_level;
        TextView item_level_name;
        LinearLayout btn_layout;
        public Vh(View itemView) {
            super(itemView);
            item_avator = (RatioRoundImageView) itemView.findViewById(R.id.item_avator);
            item_username = (TextView) itemView.findViewById(R.id.item_username);
            item_level = (TextView) itemView.findViewById(R.id.item_level);
            btn_layout = (LinearLayout) itemView.findViewById(R.id.btn_layout);
            item_level_name = (TextView) itemView.findViewById(R.id.item_level_name);
        }

        void setData(ClubMemberBean.InfoBean item, int position, Object payload) {
            itemView.setTag(position);
            if (payload == null) {
                if (!DataSafeUtils.isEmpty(item.getAvatar_thumb())) {
                    ImgLoader.displayAvatar(item.getAvatar_thumb(), item_avator);
                }
                if (!DataSafeUtils.isEmpty(item.getMobile())) {
                    item_username.setText(item.getMobile());
                }
                if (!DataSafeUtils.isEmpty(item.getGrade_id())) {
                    item_level.setText( "Lv." + item.getGrade_id());
                }
                if (!DataSafeUtils.isEmpty(item.getArea())) {
                    item_level_name.setText( "" + item.getArea());
                }else{
                    item_level_name.setText( "无");
                }

                if (mType.equals("1")){
                    btn_layout.setVisibility(View.VISIBLE);
                }else{
                    btn_layout.setVisibility(View.GONE);
                }
                btn_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mClubListener.delClubData(item.getId(),item.getUid());
                    }
                });
            }
            }
        }


    public interface  InfoClubListener{
        void delClubData(String id,String touid);
    }

    public void DelClubListener(InfoClubListener mListener) {
        this.mClubListener = mListener;
    }

}
