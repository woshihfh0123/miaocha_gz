package com.mc.phonelive.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mc.phonelive.R;
import com.mc.phonelive.bean.UserItemBean;
import com.mc.phonelive.glide.ImgLoader;
import com.mc.phonelive.interfaces.OnItemClickListener;
import com.mc.phonelive.utils.CommentUtil;

import java.util.List;

/**
 * Created by cxf on 2018/9/28.
 * 我的
 */

public class MainMeOneMenuAdapter extends RecyclerView.Adapter<MainMeOneMenuAdapter.Vh> {

    private static final int NORMAL = 0;
    private static final int GROUP_LAST = 1;
    private static final int ALL_LAST = 2;

    private List<UserItemBean> mList;
    private LayoutInflater mInflater;
    private View.OnClickListener mOnClickListener;
    private OnItemClickListener<UserItemBean> mOnItemClickListener;
    private Context mContext;
    private String mType="1";
    public MainMeOneMenuAdapter(Context context, List<UserItemBean> list,String type) {
        mList = list;
        this.mContext =context;
        this.mType =type;
        mInflater = LayoutInflater.from(context);
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = v.getTag();
                if (tag != null) {
                    UserItemBean bean = (UserItemBean) tag;
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(bean, 0);
                    }
                }
            }
        };
    }

    public void setOnItemClickListener(OnItemClickListener<UserItemBean> onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemViewType(int position) {
            return NORMAL;
    }


    public void setList(List<UserItemBean> list) {
        if (list == null) {
            return;
        }
        boolean changed = false;
        if (mList.size() != list.size()) {
            changed = true;
        } else {
            for (int i = 0, size = mList.size(); i < size; i++) {
                if (!mList.get(i).equals(list.get(i))) {
                    changed = true;
                    break;
                }
            }
        }
        if (changed) {
            mList = list;
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public Vh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int res  = R.layout.item_main_me_one_view;
        return new Vh(mInflater.inflate(res, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Vh vh, int position) {
        vh.setData(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class Vh extends RecyclerView.ViewHolder {

        ImageView mThumb;
        TextView mName;

        public Vh(View itemView) {
            super(itemView);
            mThumb = (ImageView) itemView.findViewById(R.id.thumb);
            mName = (TextView) itemView.findViewById(R.id.name);
            itemView.setOnClickListener(mOnClickListener);
        }

        void setData(UserItemBean bean) {
            itemView.setTag(bean);
            if (bean.getThumb()!=null) {
                ViewGroup.LayoutParams params = mThumb.getLayoutParams();
                if (mType.equals("1")) {
                    params.height = CommentUtil.dip2px(mContext, 50);
                    params.width = params.height;
                }else{
                    params.height = CommentUtil.dip2px(mContext, 32);
                    params.width = params.height;
                }
                mThumb.setLayoutParams(params);
                ImgLoader.display(bean.getThumb(), mThumb);
            }
            mName.setText(bean.getName());
        }
    }
}
