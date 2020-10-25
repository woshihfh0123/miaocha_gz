package com.mc.phonelive.adapter;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.picture.android.PictureActivity;
import com.mc.phonelive.R;
import com.mc.phonelive.bean.CommentVO;
import com.mc.phonelive.utils.CommentUtil;
import com.mc.phonelive.utils.DataSafeUtils;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * created by WWL on 2019/4/22 0022:16
 */
public class FoodCommentAdapter extends BaseQuickAdapter<CommentVO.DataBean.ListBean, BaseViewHolder> {

    public FoodCommentAdapter(int layoutResId, @Nullable List<CommentVO.DataBean.ListBean> data) {
        super(R.layout.commentlist_item_adapter, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CommentVO.DataBean.ListBean item) {
        helper.setText(R.id.food_pj_item_titlename, item.getUser_nicename() + "");
        helper.setText(R.id.food_pj_item_time, item.getAddtime() + "");
        if (!TextUtils.isEmpty(item.getAvatar_thumb())) {
            RequestOptions options = new RequestOptions()
                    .placeholder(R.mipmap.ic_launcher)//图片加载出来前，显示的图片
                    .fallback( R.mipmap.ic_launcher) //url为空的时候,显示的图片
                    .error(R.mipmap.ic_launcher);//图片加载失败后，显示的图片
            Glide.with(mContext).load(item.getAvatar_thumb() + "").apply(options).into((CircleImageView) helper.getView(R.id.food_pj_item_img));
        }

        if (!TextUtils.isEmpty(item.getContent()))
        {
            helper.setText(R.id.food_pj_item_content, item.getContent() + "");
        }

        RelativeLayout mReplyLinear = helper.getView(R.id.shop_layout);
        TextView reply_tv =helper.getView(R.id.reply_tv);
        RecyclerView image_recyclerview = helper.getView(R.id.image_recyclerview);
            mReplyLinear.setVisibility(View.GONE);

        if (!DataSafeUtils.isEmpty(item.getImg_list()) && item.getImg_list().length>0){
            image_recyclerview.setVisibility(View.VISIBLE);
            List<CommentVO.DataBean.ListBean.ImageBean> imagelist = new ArrayList<>();
            for(int i=0;i<item.getImg_list().length;i++){
                CommentVO.DataBean.ListBean.ImageBean img = new CommentVO.DataBean.ListBean.ImageBean();
                img.setUrl(item.getImg_list()[i]);
                imagelist.add(img);
            }
            setImgAdapter(imagelist,image_recyclerview);
        }else{
            image_recyclerview.setVisibility(View.GONE);
        }
    }

    /**
     * 有图评论
     * @param imglist
     * @param image_recyclerview
     */
    private void setImgAdapter(final List<CommentVO.DataBean.ListBean.ImageBean> imglist, RecyclerView image_recyclerview) {

        BaseQuickAdapter<CommentVO.DataBean.ListBean.ImageBean, BaseViewHolder> mImgAdapter = new BaseQuickAdapter<CommentVO.DataBean.ListBean.ImageBean, BaseViewHolder>(R.layout.comment_image_layout,imglist) {
            @Override
            protected void convert(BaseViewHolder helper, CommentVO.DataBean.ListBean.ImageBean item) {
                ImageView comment_img =helper.getView(R.id.comment_img);
                if (!TextUtils.isEmpty(item.getUrl())){
                    LinearLayout.LayoutParams params= (LinearLayout.LayoutParams) comment_img.getLayoutParams();
                    params.width = CommentUtil.getDisplayWidth(mContext)/4;
                    params.height = params.width;
                    comment_img.setLayoutParams(params);
                    Glide.with(mContext).load(item.getUrl()).into(comment_img);
                }
            }
        };
        image_recyclerview.setAdapter(mImgAdapter);
        GridLayoutManager manager = new GridLayoutManager(mContext,3);
        image_recyclerview.setLayoutManager(manager);
        mImgAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                ArrayList<String> mList = new ArrayList<>();
                for (int j = 0; j < imglist.size(); j++) {
                    mList.add(imglist.get(j).getUrl());
                }

                Intent intent = new Intent(mContext, PictureActivity.class);
                intent.putStringArrayListExtra("list", mList);
                intent.putExtra("id", i + "");
                mContext.startActivity(intent);
            }
        });
    }
}
