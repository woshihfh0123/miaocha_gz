package com.mc.phonelive.dialog;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.makeramen.roundedimageview.RoundedImageView;
import com.mc.phonelive.AppConfig;
import com.mc.phonelive.Constants;
import com.mc.phonelive.R;
import com.mc.phonelive.Utils;
import com.mc.phonelive.activity.LiveActivity;
import com.mc.phonelive.bean.LevelBean;
import com.mc.phonelive.bean.LiveUserGiftBean;
import com.mc.phonelive.glide.ImgLoader;
import com.mc.phonelive.utils.DataSafeUtils;
import com.mc.phonelive.utils.DpUtil;
import com.mc.phonelive.utils.IconUtil;

import java.util.ArrayList;

/**
 * Created by cxf on 2018/10/24.
 * 直播间私信聊天列表
 */

public class LivePeopleListDialogFragment extends AbsDialogFragment {

    private RecyclerView recycler_view;
    private String mLiveUid="";
    private ImageView btn_back;
    ArrayList<LiveUserGiftBean> mList=new ArrayList<>();
    BaseQuickAdapter<LiveUserGiftBean, BaseViewHolder> mAdapter;
    @Override
    protected int getLayoutId() {
        return R.layout.dialog_live_people;
    }

    @Override
    protected int getDialogStyle() {
        return R.style.dialog2;
    }

    @Override
    protected boolean canCancel() {
        return true;
    }

    @Override
    protected void setWindowAttributes(Window window) {
        window.setWindowAnimations(R.style.bottomToTopAnim);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = DpUtil.dp2px(300);
        params.gravity = Gravity.BOTTOM;
        window.setAttributes(params);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            String liveUid = bundle.getString(Constants.LIVE_UID);
            if (!DataSafeUtils.isEmpty(liveUid)){
                this.mLiveUid =liveUid;
            }
            ArrayList<LiveUserGiftBean>   list= bundle.getParcelableArrayList("userList");
            if (!DataSafeUtils.isEmpty(list)){
                mList = list;
            }
        }
        recycler_view =mRootView.findViewById(R.id.recycler_view);
        btn_back =mRootView.findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        setPeopleAdapter();
    }

    private void setPeopleAdapter() {
        mAdapter = new BaseQuickAdapter<LiveUserGiftBean, BaseViewHolder>(R.layout.dialog_live_people_item,mList) {
            @Override
            protected void convert(BaseViewHolder helper, LiveUserGiftBean item) {
                RoundedImageView mAvatar = helper.getView(R.id.avatar);
                TextView mName =helper.getView(R.id.name);
                ImageView mSex =helper.getView(R.id.sex);
                ImageView mLevel =helper.getView(R.id.level);

                ImgLoader.display(item.getAvatar(), mAvatar);
                mName.setText(item.getUserNiceName());
                if(Utils.isNotEmpty(item.getSex())){
                    mSex.setImageResource(IconUtil.getSexIcon(Integer.parseInt(item.getSex())));
                }
                LevelBean levelBean = AppConfig.getInstance().getLevel(item.getLevel());
                if (levelBean != null) {
                    ImgLoader.display(levelBean.getThumb(), mLevel);
                }
            }
        };
        recycler_view.setAdapter(mAdapter);
        recycler_view.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                LiveUserGiftBean bean = mAdapter.getData().get(position);
                showUserDialog(bean.getId());
            }
        });
    }

    /**
     * 显示个人资料弹窗
     */
    private void showUserDialog(String toUid) {
        if (!TextUtils.isEmpty(mLiveUid) && !TextUtils.isEmpty(toUid)) {
            LiveUserDialogFragment fragment = new LiveUserDialogFragment();
            Bundle bundle = new Bundle();
            bundle.putString(Constants.LIVE_UID, mLiveUid);
            bundle.putString(Constants.TO_UID, toUid);
            fragment.setArguments(bundle);
            fragment.show(((LiveActivity) mContext).getSupportFragmentManager(), "LiveUserDialogFragment");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
