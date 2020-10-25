package com.mc.phonelive.views.foxtone;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.mc.phonelive.AppConfig;
import com.mc.phonelive.Constants;
import com.mc.phonelive.R;
import com.mc.phonelive.activity.MyShopDetailActivity;
import com.mc.phonelive.activity.WebViewActivity;
import com.mc.phonelive.activity.foxtone.MusicalCenterActivity;
import com.mc.phonelive.activity.foxtone.SchoolListActivity;
import com.mc.phonelive.adapter.foxtone.FindGameAdapter;
import com.mc.phonelive.adapter.foxtone.FindMusicalAdapter;
import com.mc.phonelive.adapter.foxtone.FindSchoolAdapter;
import com.mc.phonelive.bean.foxtone.FindSchoolBean;
import com.mc.phonelive.bean.foxtone.MainFindBean;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.utils.CommentUtil;
import com.mc.phonelive.utils.DataSafeUtils;
import com.mc.phonelive.utils.DialogUitl;
import com.mc.phonelive.utils.ToastUtil;
import com.mc.phonelive.views.AbsMainViewHolder;

import java.util.ArrayList;
import java.util.List;

import cece.com.bannerlib.callback.OnItemViewClickListener;
import cece.com.bannerlib.model.PicBean;

import static cece.com.bannerlib.BannerGetData.getBannerData;

/**
 * 发现模块
 */
public class MainFindVidewHolder extends AbsMainViewHolder implements OnItemViewClickListener, View.OnClickListener {
    ImageView shopSearchImg;
    LinearLayout topLayout;
    RelativeLayout rl_banner;
    TextView findStrategy;
    TextView moreMusical;
    LinearLayout music_layout;
    RecyclerView musicalRecyclerview;
    TextView moreGame;
    RecyclerView gameRecyclerview;
    ImageView shopmallImg;
    TextView moreSchool;
    RecyclerView schoolRecyclerview;
    private LinearLayout notice_layout;//公告布局

    private MainFindBean.InfoBean infoBean = new MainFindBean.InfoBean();
    private List<MainFindBean.InfoBean.SlideBean> mBanner = new ArrayList<>();
    private List<MainFindBean.InfoBean.NoticeBean> mStrategyList = new ArrayList<>();
    private List<MainFindBean.InfoBean.ListBean> mMusicList = new ArrayList<>();
    private List<MainFindBean.InfoBean.GameBean> mGameList = new ArrayList<>();
    private List<FindSchoolBean> mSchoolList = new ArrayList<>();
    private FindMusicalAdapter mMusicalAdapter;//乐器中心
    private FindGameAdapter mGameAdapter;//乐器中心
    private FindSchoolAdapter mSchoolAdapter;//商学院

    public MainFindVidewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.main_find_layout;
    }

    @Override
    public void init() {
        notice_layout = (LinearLayout) findViewById(R.id.notice_layout);
        shopSearchImg = (ImageView) findViewById(R.id.shop_search_img);
        topLayout = (LinearLayout) findViewById(R.id.top_layout);
        rl_banner = (RelativeLayout) findViewById(R.id.rl_banner);
        findStrategy = (TextView) findViewById(R.id.find_strategy);
        moreMusical = (TextView) findViewById(R.id.more_musical);
        music_layout = (LinearLayout) findViewById(R.id.music_layout);
        musicalRecyclerview = (RecyclerView) findViewById(R.id.musical_recyclerview);
        moreGame = (TextView) findViewById(R.id.more_game);
        gameRecyclerview = (RecyclerView) findViewById(R.id.game_recyclerview);
        shopmallImg = (ImageView) findViewById(R.id.shopmall_img);
        moreSchool = (TextView) findViewById(R.id.more_school);
        schoolRecyclerview = (RecyclerView) findViewById(R.id.school_recyclerview);
        findStrategy.setOnClickListener(this);
        moreMusical.setOnClickListener(this);
        moreGame.setOnClickListener(this);
        shopmallImg.setOnClickListener(this);
        moreSchool.setOnClickListener(this);

        setMusicalAdapter(mMusicList);
        setGameAdapter(mGameList);
        setSchoolAdapter(mSchoolList);

        initHttpData();
    }


    private void initHttpData() {

        HttpUtil.getFindIndex(new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0) {
                    JSONObject obj = JSON.parseObject(info[0]);
                    List<MainFindBean.InfoBean.SlideBean> slideList = JSON.parseArray(obj.getString("slide"), MainFindBean.InfoBean.SlideBean.class);
                    List<MainFindBean.InfoBean.NoticeBean> noticeList = JSON.parseArray(obj.getString("notice"), MainFindBean.InfoBean.NoticeBean.class);
                    List<MainFindBean.InfoBean.ListBean> musicList = JSON.parseArray(obj.getString("list"), MainFindBean.InfoBean.ListBean.class);
                    List<MainFindBean.InfoBean.GameBean> gameList = MainFindBean.getFindData();
                    List<FindSchoolBean> newsList = JSON.parseArray(obj.getString("newsList"), FindSchoolBean.class);


                    if (!DataSafeUtils.isEmpty(slideList) && slideList.size() > 0) {
                        mBanner = slideList;
                        rl_banner.setVisibility(View.VISIBLE);
                        setAdvAdapterData(slideList);
                    } else {
                        rl_banner.setVisibility(View.GONE);
                    }

                    if (!DataSafeUtils.isEmpty(noticeList) && noticeList.size() > 0) {
                        mStrategyList = noticeList;
                        notice_layout.setVisibility(View.VISIBLE);
                        findStrategy.setText(noticeList.get(0).getTitle());
                    } else {
                        notice_layout.setVisibility(View.GONE);
                    }

                    if (!DataSafeUtils.isEmpty(musicList) && musicList.size() > 0) {
                        music_layout.setVisibility(View.VISIBLE);
                        mMusicList = musicList;
                        mMusicalAdapter.setNewData(musicList);
                    } else {
                        music_layout.setVisibility(View.GONE);
                    }

                    if (!DataSafeUtils.isEmpty(gameList) && gameList.size() > 0) {
                        mGameList = gameList;
                        mGameAdapter.setNewData(gameList);
                    }

                    if (!DataSafeUtils.isEmpty(newsList) && newsList.size() > 0) {
                        mSchoolList = newsList;
                        mSchoolAdapter.setNewData(newsList);
                    }


                }
            }

            @Override
            public boolean showLoadingDialog() {
                return true;
            }

            @Override
            public Dialog createLoadingDialog() {
                return DialogUitl.loadingDialog(mContext);
            }
        });
    }

    /**
     * 乐器中心
     *
     * @param musicList
     */
    private void setMusicalAdapter(List<MainFindBean.InfoBean.ListBean> musicList) {
        mMusicalAdapter = new FindMusicalAdapter(R.layout.main_find_music_item, musicList);
        musicalRecyclerview.setAdapter(mMusicalAdapter);
        musicalRecyclerview.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        mMusicalAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                mContext.startActivity(new Intent(mContext, MusicalCenterActivity.class));
            }
        });
    }

    /**
     * 游戏中心
     *
     * @param mGameList
     */
    private void setGameAdapter(List<MainFindBean.InfoBean.GameBean> mGameList) {
        mGameAdapter = new FindGameAdapter(R.layout.main_find_game_item, mGameList);
        gameRecyclerview.setAdapter(mGameAdapter);
        gameRecyclerview.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
    }

    /**
     * 商学院
     *
     * @param mSchoolList
     */
    private void setSchoolAdapter(List<FindSchoolBean> mSchoolList) {
        mSchoolAdapter = new FindSchoolAdapter(R.layout.main_find_school_item, mSchoolList);
        schoolRecyclerview.setAdapter(mSchoolAdapter);
        schoolRecyclerview.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mSchoolAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                FindSchoolBean data = mSchoolAdapter.getData().get(position);
                if (!DataSafeUtils.isEmpty(data)) {
                    if (!DataSafeUtils.isEmpty(data.getWeb_url())) {
                        forward(mContext, data.getWeb_url());
                    }
                }
            }
        });
    }


    /**
     * BannerLayout实现轮播图效果
     *
     * @param adv_list
     */
    private void setAdvAdapterData(List<MainFindBean.InfoBean.SlideBean> adv_list) {
        ViewGroup.LayoutParams params = rl_banner.getLayoutParams();
        params.width = CommentUtil.getDisplayWidth(mContext);
        params.height = (int) ((int) (params.width * 0.42));
        rl_banner.setLayoutParams(params);
        List<PicBean> list = new ArrayList<>();
        for (int i = 0; i < adv_list.size(); i++) {
            PicBean b = new PicBean();
            b.setTitle("");
            b.setUrl(adv_list.get(i).getSlide_url());
            b.setType("0");
            b.setPic(adv_list.get(i).getSlide_pic());
            b.setId("0");
            list.add(b);
        }
        //两种效果 带滑动缩放效果以及圆角以及普通轮播图
        getBannerData(mContext, this, rl_banner, list, false, true, mContext.getResources().getColor(R.color.gray1), mContext.getResources().getColor(R.color.colorAccent), 5, 5, 1);

    }

    @Override
    public void onItemClick(View view, PicBean bean) {
        if (!DataSafeUtils.isEmpty(bean.getUrl())) {
            forward(mContext, bean.getUrl());
        } else if (DataSafeUtils.isEmpty(bean.getUrl()) && !DataSafeUtils.isEmpty(bean.getId()) && !bean.getId().equals("0")) {
            Intent intent = new Intent(mContext, MyShopDetailActivity.class);
            intent.putExtra("id", bean.getId() + "");
            if (bean.getUid().equals(AppConfig.getInstance().getUid())) {
                intent.putExtra("status", "0");
            } else {
                intent.putExtra("status", "1");
            }
            intent.putExtra("store_id", AppConfig.getInstance().getUid());

            mContext.startActivity(intent);
        } else if (!DataSafeUtils.isEmpty(bean.getUrl()) && !DataSafeUtils.isEmpty(bean.getId())) {
            forward(mContext, bean.getUrl());
        }
    }

    private void forward(Context context, String url) {
        url += "&uid=" + AppConfig.getInstance().getUid() + "&token=" + AppConfig.getInstance().getToken();
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(Constants.URL, url);
        context.startActivity(intent);
    }


    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.find_strategy://公告
                if (!DataSafeUtils.isEmpty(mStrategyList.get(0).getWeb_url())) {
                    forward(mContext, mStrategyList.get(0).getWeb_url());
                }
//                intent = new Intent(mContext, StrategyActivity.class);
//                mContext.startActivity(intent);
                break;
            case R.id.more_musical:
                intent = new Intent(mContext, MusicalCenterActivity.class);
                mContext.startActivity(intent);
                break;
            case R.id.more_game:


//                ToastUtil.show("尽请期待");
//                ToastUtil.show("暂无更多");
                break;
            case R.id.shopmall_img:
                ToastUtil.show("尽请期待");

                break;
            case R.id.more_school:
                intent = new Intent(mContext, SchoolListActivity.class);
                mContext.startActivity(intent);
                break;
        }
    }
}
