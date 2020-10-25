package com.mc.phonelive.activity.foxtone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.mc.phonelive.R;
import com.mc.phonelive.activity.AbsActivity;
import com.mc.phonelive.bean.foxtone.ClubDetailBean;
import com.mc.phonelive.glide.ImgLoader;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.im.EventBusModel;
import com.mc.phonelive.utils.DataSafeUtils;
import com.mc.phonelive.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的俱乐部
 */
public class MyClubActivity extends AbsActivity {
    @BindView(R.id.club_img)
    ImageView clubImg;
    @BindView(R.id.club_name)
    TextView clubName;
    @BindView(R.id.club_phone)
    TextView clubPhone;
    @BindView(R.id.club_username)
    TextView clubUsername;
    @BindView(R.id.club_people)
    TextView clubPeople;
    @BindView(R.id.club_invite)
    TextView clubInvite;
    @BindView(R.id.club_content)
    TextView clubContent;
    @BindView(R.id.club_quit)
    TextView clubQuit;

    ClubDetailBean.InfoBean mData = new ClubDetailBean.InfoBean();
    private int page = 1;
    private String mIsfounder="";//是否是创始人
    @Override
    protected int getLayoutId() {
        return R.layout.my_club_layout;
    }

    @Override
    protected void main() {
        setTitle("我的俱乐部");
        initHttpData();
    }

    private void initHttpData() {
        HttpUtil.getClubDetail(new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                List<ClubDetailBean.InfoBean> mList = JSON.parseArray(Arrays.toString(info), ClubDetailBean.InfoBean.class);
                if (!DataSafeUtils.isEmpty(mList) && mList.size() > 0) {
                    mData = mList.get(0);
                    setInitData(mData);
                }
            }
        });
    }

    private void setInitData(ClubDetailBean.InfoBean data) {
        if (!DataSafeUtils.isEmpty(data.getBadge())) {
            ImgLoader.display(data.getBadge(), clubImg, R.drawable.default_img);
        }

        if (!DataSafeUtils.isEmpty(data.getName())) {
            clubName.setText(data.getName());
        }
        if (!DataSafeUtils.isEmpty(data.getPhone())) {
            clubPhone.setText("电话：" + data.getPhone());
        }
        if (!DataSafeUtils.isEmpty(data.getIs_founder())) {
            mIsfounder = data.getIs_founder();
            if (data.getIs_founder().equals("1")){
                clubQuit.setText("解散俱乐部");
            }
        }
        if (!DataSafeUtils.isEmpty(data.getUser_nicename())) {
            clubUsername.setText(data.getUser_nicename());
        }
        if (!DataSafeUtils.isEmpty(data.getCounts())) {
            clubPeople.setText(data.getCounts());
        }
        if (!DataSafeUtils.isEmpty(data.getBriefing())) {
            clubContent.setText(data.getBriefing());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.club_people, R.id.club_invite, R.id.club_quit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.club_people:
                Intent intent = new Intent(mContext, ClubMemberActivity.class);
                intent.putExtra("id",mData.getId());
                intent.putExtra("founder",mData.getIs_founder());
                mContext.startActivity(intent);
                break;
            case R.id.club_invite:
                mContext.startActivity(new Intent(mContext, InviteFriendsActivity.class));
                break;
            case R.id.club_quit:
                if (mData.getIs_founder().equals("1")){
                    dissClub(mData);
                }else{
                    outClub(mData);
                }
                break;
        }
    }

    /**
     * 退出俱乐部
     * @param mData
     */
    private void outClub(ClubDetailBean.InfoBean mData) {
        HttpUtil.outClub(new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                ToastUtil.show(msg);
                if (code==0){
                    EventBus.getDefault().post(new EventBusModel("club_refresh"));
                    MyClubActivity.this.finish();
                }
            }
        });
    }

    /**
     * 解散俱乐部
     * @param mData
     */
    private void dissClub(ClubDetailBean.InfoBean mData) {
        HttpUtil.dissClub(mData.getId(), new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {

                ToastUtil.show(msg);
                if (code==0){
                    EventBus.getDefault().post(new EventBusModel("club_refresh"));
                    MyClubActivity.this.finish();
                }
            }
        });
    }
}
