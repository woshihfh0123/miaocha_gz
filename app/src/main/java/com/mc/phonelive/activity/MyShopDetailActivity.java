package com.mc.phonelive.activity;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.makeramen.roundedimageview.RoundedImageView;
import com.mc.phonelive.Constants;
import com.mc.phonelive.Utils;
import com.mc.phonelive.activity.shop.PayGoodsOrderActivity;
import com.mc.phonelive.adapter.TypeSelectorAdapter;
import com.mc.phonelive.adapter.YhqHvAdapter;
import com.mc.phonelive.adapter.YhqSelectorAdapter;
import com.mc.phonelive.bean.ConfigBean;
import com.mc.phonelive.bean.GetGoodsAttrBean;
import com.mc.phonelive.bean.TagYhqBean;
import com.mc.phonelive.bean.UserBean;
import com.mc.phonelive.bean.YhqSelBean;
import com.mc.phonelive.dialog.LiveShareDialogFragment;
import com.mc.phonelive.im.EventBusModel;
import com.mc.phonelive.mob.MobCallback;
import com.mc.phonelive.mob.MobConst;
import com.mc.phonelive.mob.MobShareUtil;
import com.mc.phonelive.mob.ShareData;
import com.mc.phonelive.utils.AddSubUtils;
import com.mc.phonelive.utils.DisplayUtils;
import com.mc.phonelive.utils.EventBean;
import com.mc.phonelive.utils.GlideUtils;
import com.mc.phonelive.utils.WordUtil;
import com.orhanobut.logger.Logger;
import com.picture.android.PictureActivity;
import com.mc.phonelive.AppConfig;
import com.mc.phonelive.R;
import com.mc.phonelive.activity.shop.CartMainActivity;
import com.mc.phonelive.activity.shop.MoreCommentListActivity;
import com.mc.phonelive.adapter.FoodCommentAdapter;
import com.mc.phonelive.bean.CommentVO;
import com.mc.phonelive.bean.MyShopDetailBean;
import com.mc.phonelive.dialog.CancelORderDialog;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpConsts;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.utils.CommentUtil;
import com.mc.phonelive.utils.DataSafeUtils;
import com.mc.phonelive.utils.DialogUitl;
import com.mc.phonelive.utils.ToastUtil;
import com.rey.material.app.BottomSheetDialog;
import com.mc.phonelive.views.LiveKsyPlayViewHolder;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cece.com.bannerlib.callback.OnItemViewClickListener;
import cece.com.bannerlib.model.PicBean;

import static cece.com.bannerlib.BannerGetData.getBannerData;


/**
 * 店铺详情
 */
public class MyShopDetailActivity extends AbsActivity implements View.OnTouchListener, View.OnClickListener, OnItemViewClickListener , LiveShareDialogFragment.ActionListener{
    @BindView(R.id.rl_banner)
    RelativeLayout rl_banner;
    @BindView(R.id.peisong_addr)
    TextView peisongAddr;
    @BindView(R.id.express_price)
    TextView expressPrice;
    @BindView(R.id.evaluation_title)
    TextView evaluationTitle;
    @BindView(R.id.evaluation_more)
    TextView evaluationMore;
    @BindView(R.id.evaluation_RecyclerView)
    RecyclerView commentRecyclerview;
    @BindView(R.id.img_recyclerview)
    RecyclerView img_recyclerview;
    @BindView(R.id.detail_layout)
    LinearLayout detail_layout;
    @BindView(R.id.goodsWebView)
    TextView goodsWebView;
    @BindView(R.id.detail_content)
    TextView detail_content;//标题描述
    @BindView(R.id.evaluation_layout)
    LinearLayout evaluationLayout;
    private TextView shop_forwad, titleView, detail_price, detail_title, shop_name, shop_content, shop_down, shop_del;
    private ImageView btn_back,iv_share;
    private RoundedImageView shop_img;
    private RecyclerView mRecyclerView;
    private LinearLayout business_layout, more_layout;//商家、更多推荐商品列表布局
    private LinearLayout live_layout, kefu_layout, cart_layout, cart_add_layout, buy_layout;
    private RelativeLayout kehu_layout;//客户
    private NestedScrollView scrollView;
    private FrameLayout Top_Layouat;
    private BaseQuickAdapter<MyShopDetailBean.AboutListBean, BaseViewHolder> mShopAdapter;
    private BaseQuickAdapter<PicBean, BaseViewHolder> mDetailImgAdapter;
    private List<MyShopDetailBean> mList = new ArrayList<>();
    private List<CommentVO.DataBean.ListBean> mCommentList = new ArrayList<>();
    MyShopDetailBean.StoreInfoBean store_info;
    FoodCommentAdapter commentAdapter;
    private String mType = "";//类型 如1客户  如0商家
    private String mGoodsId = "";
    private String is_show = "1";
    private String is_showName = "";
    private String store_id = "0";
    private String mStock = "";
    private String mSales = "";//销量
    private String mPhones = "";
    List<PicBean> Piclist = new ArrayList<>();
    private WebView mWv;
    private int defaultBuy = 1;
    private String goodsId = "";
    private String product_attrs = "请选择规格";
    private ArrayList<GetGoodsAttrBean> selectlist;
    private String pic = "";
    private String title = "";
    private String descp = "";
    private String gprice = "";
    private LinearLayout ll_selector_yhq;
    private ArrayList<YhqSelBean> yhqList;
    private RecyclerView rv_yhq;
    private YhqHvAdapter yhqHvAdapter;
    private ImageView iv_sc;
    private boolean isSelectorAll = false;
    private LiveKsyPlayViewHolder mLivePlayViewHolder;
    private String pull = "";
    private ImageView iv_close;
    private RelativeLayout play_container;
    private RelativeLayout rl_v;

    private String isMS;

    boolean isToBug = false;//是否为购买

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_myshop_detail;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void main() {
        sp = this.getSharedPreferences("config", Context.MODE_PRIVATE);
        Log.e("DDDD:", "DPXQ");
        mRecyclerView = findViewById(R.id.recyclerView);
        titleView = findViewById(R.id.titleView);
        rl_v = findViewById(R.id.rl_v);
        cart_add_layout = findViewById(R.id.cart_add_layout);
        buy_layout = findViewById(R.id.buy_layout);
        play_container = findViewById(R.id.play_container);
        iv_close = findViewById(R.id.iv_close);
        live_layout = findViewById(R.id.live_layout);
        kefu_layout = findViewById(R.id.kefu_layout);
        cart_layout = findViewById(R.id.cart_layout);
        mLivePlayViewHolder = new LiveKsyPlayViewHolder(mContext, (ViewGroup) findViewById(R.id.play_container));
        mLivePlayViewHolder.addToParent();
        iv_sc = findViewById(R.id.iv_sc);
        mWv = (WebView) findViewById(R.id.wv);
        btn_back = findViewById(R.id.btn_back);
        iv_share = findViewById(R.id.iv_share);
        iv_share.setOnClickListener(this);
        rv_yhq = findViewById(R.id.rv_yhq);
        more_layout = findViewById(R.id.more_layout);
        detail_price = findViewById(R.id.detail_price);
        detail_title = findViewById(R.id.detail_title);
        ll_selector_yhq = findViewById(R.id.ll_selector_yhq);
        business_layout = findViewById(R.id.business_layout);
        kehu_layout = findViewById(R.id.kehu_layout);
        shop_name = findViewById(R.id.shop_name);
        shop_content = findViewById(R.id.shop_content);
        shop_del = findViewById(R.id.shop_del);
        shop_down = findViewById(R.id.shop_down);
        shop_img = findViewById(R.id.shop_img);
        scrollView = findViewById(R.id.scrollView);
        Top_Layouat = findViewById(R.id.Top_Layouat);
        evaluationMore = findViewById(R.id.evaluation_more);
        findViewById(R.id.shop_forwad).setOnClickListener(this);
        shop_down.setOnClickListener(this);
        shop_del.setOnClickListener(this);
        live_layout.setOnClickListener(this);
        kefu_layout.setOnClickListener(this);
        cart_layout.setOnClickListener(this);
        cart_add_layout.setOnClickListener(this);
        buy_layout.setOnClickListener(this);
        btn_back.setOnClickListener(this);
        rv_yhq.setLayoutManager(Utils.getHvManager(mContext));
        yhqHvAdapter = new YhqHvAdapter();
        rv_yhq.setAdapter(yhqHvAdapter);
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            String id = bundle.getString("id");
            String status = bundle.getString("status");
            String store_id = bundle.getString("store_id");
            String misMS = bundle.getString("isMs");
            if (!DataSafeUtils.isEmpty(misMS)) {
                isMS = misMS;
                cart_add_layout.setVisibility(View.GONE);
                cart_layout.setVisibility(View.GONE);
            }
            if (!DataSafeUtils.isEmpty(id)) {
                mGoodsId = id;
            }
            if (!DataSafeUtils.isEmpty(store_id)) {
                this.store_id = store_id;
            } else {
                this.store_id = AppConfig.getInstance().getUid();
            }
            if (!DataSafeUtils.isEmpty(status)) {
                this.mType = status;

//                if (status.equals("1")) {//客户
//                    kehu_layout.setVisibility(View.VISIBLE);
//                    business_layout.setVisibility(View.GONE);
//                } else {//商家自己
//                    kehu_layout.setVisibility(View.GONE);
//                    business_layout.setVisibility(View.VISIBLE);
//                }
            }

            getData();
            ll_selector_yhq.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showSelectorPop();
                }
            });
            rl_v.setOnTouchListener(this);
        }

        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            // 将透明度声明成局部变量用于判断
            int alpha = 0;
            int count = 0;
            float scale = 0;

            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                if (scrollY <= CommentUtil.getDisplayWidth(mContext)) {
                    scale = (float) scrollY / CommentUtil.getDisplayWidth(mContext);
                    alpha = (int) (255 * scale);
                    // 随着滑动距离改变透明度
                    // Log.e("al=","="+alpha);
//                    if(alpha<30){
//                        Top_Layouat.setBackgroundColor(Color.argb(30, 50, 50, 50));
//                    }else{

                    Top_Layouat.setBackgroundColor(Color.argb(alpha, 50, 50, 50));
//                    }
                    titleView.setText("");
                } else {
                    if (alpha < 255) {
                        // 防止频繁重复设置相同的值影响性能
                        alpha = 255;
                        Top_Layouat.setBackgroundColor(Color.argb(alpha, 50, 50, 50));
                        titleView.setText(detail_title.getText().toString());
                    }
                }
            }

        });

        getCommentAdapter(mCommentList);
        setDetailImgAdapter(Piclist);
        iv_sc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveGoods();
            }
        });
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mLivePlayViewHolder != null) {
                    mLivePlayViewHolder.release();
                }
                rl_v.setVisibility(View.GONE);
            }
        });
//        play_container.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });

//        rl_pl.addDragView(view, 0,400,380,760, true, false);

    }


    private void saveGoods() {
        HttpUtil.saveShop(mGoodsId, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                Log.v("tags", Arrays.toString(info) + "-----info");
                JSONObject obj = JSON.parseObject(info[0]);
                String djs = obj.getString("iscollect");
                String tsm = "";
                if (djs.equals("0")) {
                    tsm = "取消收藏成功";
                    iv_sc.setImageResource(R.drawable.iv_sc);
                } else {
                    tsm = "收藏成功";
                    iv_sc.setImageResource(R.drawable.collect_y);
                }
                ToastUtil.show(tsm);
            }

            @Override
            public boolean showLoadingDialog() {
                return true;
            }

            @Override
            public Dialog createLoadingDialog() {
                return DialogUitl.loadingDialog(MyShopDetailActivity.this);
            }
        });
    }

    private void getYhqList() {
        HttpUtil.getYhqList(store_id, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                Log.v("tags", Arrays.toString(info) + "-----info");
                if (!DataSafeUtils.isEmpty(info)) {
                    JSONObject obj = JSON.parseObject(info[0]);
                    yhqList = (ArrayList<YhqSelBean>) JSON.parseArray(obj.getString("list"), YhqSelBean.class);
                    if (Utils.isNotEmpty(yhqList)) {
                        ll_selector_yhq.setVisibility(View.VISIBLE);
                    } else {
                        ll_selector_yhq.setVisibility(View.GONE);
                    }

                }
            }

            @Override
            public boolean showLoadingDialog() {
                return true;
            }

            @Override
            public Dialog createLoadingDialog() {
                return DialogUitl.loadingDialog(MyShopDetailActivity.this);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
//        Bundle bundle = this.getIntent().getExtras();
//        if (bundle != null) {
//            String id = bundle.getString("id");
//            String status = bundle.getString("status");
//            String store_id = bundle.getString("store_id");
//            if (!DataSafeUtils.isEmpty(id)) {
//                mGoodsId = id;
//            }
//            if (!DataSafeUtils.isEmpty(store_id)) {
//                this.store_id = store_id;
//            } else {
//                this.store_id = AppConfig.getInstance().getUid();
//            }
//            if (!DataSafeUtils.isEmpty(status)) {
//                this.mType = status;
//
////                if (status.equals("1")) {//客户
////                    kehu_layout.setVisibility(View.VISIBLE);
////                    business_layout.setVisibility(View.GONE);
////                } else {//商家自己
////                    kehu_layout.setVisibility(View.GONE);
////                    business_layout.setVisibility(View.VISIBLE);
////                }
//            }
//
//            getData();
//            ll_selector_yhq.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    showSelectorPop();
//                }
//            });
//            rl_v.setOnTouchListener(this);
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLivePlayViewHolder != null) {
            mLivePlayViewHolder.release();
        }
    }

    private void showSelectorPop() {//3规格，2，添加仓库，0，加入采购单 1，立即下单
        BottomSheetDialog mDialog = new BottomSheetDialog(mContext);
        View dialogView = View.inflate(mContext, R.layout.selector_yhq, null);
        TextView tv_ljxd = dialogView.findViewById(R.id.tv_ljxd);
        int popHeight = DisplayUtils.getScreenHeight(mContext) * 3 / 4;
        RecyclerView rv = dialogView.findViewById(R.id.rv);
        YhqSelectorAdapter tpAdapter = new YhqSelectorAdapter();
        rv.setLayoutManager(Utils.getLvManager(mContext));
        rv.setAdapter(tpAdapter);
        tpAdapter.setNewData(yhqList);
        tpAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.tv_lq) {
                    tpAdapter.getData().get(position).setIs_get("1");
                    tpAdapter.notifyItemChanged(position);
                    lqYhq(tpAdapter.getData().get(position).getId());
                }
            }
        });
        mDialog.contentView(dialogView)
                .heightParam(popHeight)
                .inDuration(500)
                .cancelable(true)
                .show();
        tv_ljxd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//判断事件//3规格，2，添加仓库，0，加入采购单 1，立即下单
                mDialog.dismiss();

            }
        });
    }

    private void lqYhq(String yid) {
        HttpUtil.getYhqLq(yid, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                Log.v("tags", Arrays.toString(info) + "-----info");

            }

            @Override
            public boolean showLoadingDialog() {
                return true;
            }

            @Override
            public Dialog createLoadingDialog() {
                return DialogUitl.loadingDialog(MyShopDetailActivity.this);
            }
        });
    }

    @Override
    protected boolean isStatusBarWhite() {
        return true;
    }

    /**
     * 商品详情下面的图片
     *
     * @param piclist
     */
    private void setDetailImgAdapter(List<PicBean> piclist) {
        mDetailImgAdapter = new BaseQuickAdapter<PicBean, BaseViewHolder>(R.layout.layout_detail_img_view, piclist) {
            @Override
            protected void convert(BaseViewHolder helper, PicBean item) {
                ImageView imgs = helper.getView(R.id.detail_img);
                if (!DataSafeUtils.isEmpty(item.getPic())) {
                    ViewGroup.LayoutParams params = imgs.getLayoutParams();
                    params.width = CommentUtil.getDisplayWidth(mContext) - CommentUtil.dip2px(mContext, 10);
                    params.height = params.width;
                    imgs.setLayoutParams(params);
                    Glide.with(mContext).load(item.getPic()).into(imgs);
                }
            }
        };
        img_recyclerview.setAdapter(mDetailImgAdapter);
        img_recyclerview.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mDetailImgAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                ArrayList<String> mpiclist = new ArrayList<>();
                for (int i = 0; i < mDetailImgAdapter.getData().size(); i++) {
                    PicBean bean = mDetailImgAdapter.getData().get(i);
                    mpiclist.add(bean.getPic());
                }
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("list", mpiclist);
                bundle.putString("id", position + "");
                Intent intent = new Intent(mContext, PictureActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void getData() {
        HttpUtil.shopDetail(mGoodsId, store_id, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                Log.v("tags", Arrays.toString(info) + "-----info");
                if (code == 0) {
                    List<MyShopDetailBean> list = JSON.parseArray(Arrays.toString(info), MyShopDetailBean.class);
                    JSONObject obj = JSON.parseObject(info[0]);
                    mList = list;
                    mSales = list.get(0).getSales();
                    store_info = list.get(0).getStore_info();
                    setShopData(list.get(0));
                    pull = list.get(0).getPull();
                    if (Utils.isNotEmpty(pull)) {

                        mLivePlayViewHolder.play(pull);
                        rl_v.setVisibility(View.VISIBLE);
                        iv_close.setVisibility(View.VISIBLE);
                    } else {
                        rl_v.setVisibility(View.GONE);
                        iv_close.setVisibility(View.GONE);
                    }
                    if (!DataSafeUtils.isEmpty(list.get(0).getStore_info())) {
                        setShopMsg(list.get(0).getStore_info());
                    }
                    if (Utils.isNotEmpty(list.get(0).getStore_id())) {
                        store_id = list.get(0).getStore_id();
//                        ll_selector_yhq.setVisibility(View.VISIBLE);
                        getYhqList();
                    } else {
//                        ll_selector_yhq.setVisibility(View.GONE);
                    }
                    if (!DataSafeUtils.isEmpty(list.get(0).getAbout_list())) {
                        more_layout.setVisibility(View.VISIBLE);
                        setShopAdapater(list.get(0).getAbout_list());
                    } else {
                        more_layout.setVisibility(View.GONE);
                    }
                    selectlist = (ArrayList<GetGoodsAttrBean>) JSON.parseArray(obj.getString("goods_attr"), GetGoodsAttrBean.class);
                    ArrayList<TagYhqBean> disYhqList = (ArrayList<TagYhqBean>) JSON.parseArray(obj.getString("coupons_list"), TagYhqBean.class);
                    if (Utils.isNotEmpty(disYhqList)) {
                        yhqHvAdapter.setNewData(disYhqList);
                    }
                    if (Utils.isNotEmpty(selectlist)) {
                        for (int i = 0; i < selectlist.size(); i++) {
                            GetGoodsAttrBean getAtsBean = selectlist.get(i);
                            List<String> listNb = getAtsBean.getAttr_value();
                            List<GetGoodsAttrBean.DiyAttrValue> atslist = new ArrayList<>();
                            for (int j = 0; j < listNb.size(); j++) {
                                atslist.add(j, new GetGoodsAttrBean.DiyAttrValue(listNb.get(j)));
                            }
                            getAtsBean.setAttrs(atslist);
                            selectlist.set(i, getAtsBean);
                        }
                    }

                    if (!DataSafeUtils.isEmpty(list.get(0).getComment_list()) && list.get(0).getComment_list().size() > 0) {
                        if (evaluationLayout != null)
                            evaluationLayout.setVisibility(View.VISIBLE);

                        mCommentList.clear();
                        mCommentList = list.get(0).getComment_list();
                        if (commentAdapter != null) {
                            commentAdapter.setNewData(mCommentList);
                        }
                    } else {
                        if (evaluationLayout != null)
                            evaluationLayout.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public boolean showLoadingDialog() {
                return true;
            }

            @Override
            public Dialog createLoadingDialog() {
                return DialogUitl.loadingDialog(MyShopDetailActivity.this);
            }
        });
    }


    /**
     * 商品删除
     */
    private void GoodsDel() {
        CancelORderDialog oRderDialog = new CancelORderDialog(this, "删除", "确定删除此商品吗？") {
            @Override
            public void doConfirmUp() {
                HttpUtil.shopGoodsDel(mGoodsId, new HttpCallback() {
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
                        if (code == 0) {
                            Toast.makeText(mContext, "删除成功", Toast.LENGTH_SHORT).show();
                            MyShopDetailActivity.this.finish();
                        } else {
                            ToastUtil.show(msg);
                        }
                    }
                });
            }
        };
        oRderDialog.show();

    }

    /**
     * 商品下架
     */
    private void GoodsLower() {
        String mTitle = "";
        String msg = "";
        if (is_show.equals("1")) {
            mTitle = "上架";
            msg = "确定上架此商品吗？";
        } else {
            mTitle = "下架";
            msg = "确定下架此商品吗？";
        }
        CancelORderDialog oRderDialog = new CancelORderDialog(this, mTitle, msg) {
            @Override
            public void doConfirmUp() {

                HttpUtil.shopGoodsLower(mGoodsId, is_show, new HttpCallback() {
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
                        if (code == 0) {
                            Toast.makeText(mContext, "操作成功", Toast.LENGTH_SHORT).show();
                            MyShopDetailActivity.this.finish();
                        }
                    }
                });
            }
        };
        oRderDialog.show();
    }

    /**
     * 商品信息
     *
     * @param bean
     */
    private void setShopData(MyShopDetailBean bean) {
        mStock = bean.getStock();
        if (Utils.isNotEmpty(bean.getInfo())) {
            pic = bean.getInfo().getThumb();
        }
        title = bean.getTitle();
        descp = bean.getDescription();
        gprice = bean.getPrice();

//        is_showName = bean.getIs_show_name();
//        if (this.mType.equals("1")) {//客户
//            if (bean.getIs_show().equals("2")) {
//                kehu_layout.setVisibility(View.GONE);
//            } else {
//                kehu_layout.setVisibility(View.VISIBLE);
//            }
//        }
//
        if (!DataSafeUtils.isEmpty(bean.getGoods_freight())) {
            expressPrice.setText("￥" + bean.getGoods_freight());
        }
//
//        if (bean.getIs_show().equals("1")) {
//            shop_down.setText("下架");
//            is_show = "2";
//        } else {
//            shop_down.setText("上架");
//            is_show = "1";
//        }
        if (!DataSafeUtils.isEmpty(bean.getImg_list()) && bean.getImg_list().length > 0) {
            setAdvAdapterData(bean.getImg_list());
        }
        detail_price.setText("￥" + bean.getPrice());
        if (bean.getIscollect().equals("0")) {
            iv_sc.setImageResource(R.drawable.iv_sc);
        } else {
            iv_sc.setImageResource(R.drawable.collect_y);
        }
        detail_title.setText(bean.getTitle());

        if (!DataSafeUtils.isEmpty(bean.getInfo())) {
//            detail_content.setText(bean.getInfo());
//            detail_content.setVisibility(View.VISIBLE);
        } else {
//            detail_content.setVisibility(View.GONE);
        }
        mWv.loadData(bean.getContent(), "text/html; charset=UTF-8", null);//这种写法可以正确解码
        if (!DataSafeUtils.isEmpty(bean.getDescription())) {
            goodsWebView.setText(bean.getDescription());
        }

        if (!DataSafeUtils.isEmpty(bean.getComment_num())) {
            evaluationTitle.setText("全部评论（" + bean.getComment_num() + ")");
        }
    }

    /**
     * 店铺信息
     *
     * @param store_info
     */
    private void setShopMsg(MyShopDetailBean.StoreInfoBean store_info) {
        if (!DataSafeUtils.isEmpty(store_info.getMobile())) {
            mPhones = store_info.getMobile();
        }
        if (!DataSafeUtils.isEmpty(store_info.getLogo())) {
//            GlideUtils.loadImage(mContext,store_info.getLogo(),shop_img);

            Glide.with(mContext).load(store_info.getLogo()).into(shop_img);
        } else {

            shop_img.setImageResource(R.drawable.default_img);
        }
        shop_name.setText(store_info.getTitle());
        shop_content.setText("已售" + store_info.getCounts() + "件");
    }

    /**
     * BannerLayout实现轮播图效果
     *
     * @param adv_list
     */
    private void setAdvAdapterData(String[] adv_list) {
        ViewGroup.LayoutParams params = rl_banner.getLayoutParams();
        params.width = CommentUtil.getDisplayWidth(this);
        params.height = params.width;
        rl_banner.setLayoutParams(params);
        Piclist = new ArrayList<>();
        for (int i = 0; i < adv_list.length; i++) {
            PicBean b = new PicBean();
            b.setTitle("");
            b.setUrl("");
            b.setType("");
            b.setPic(adv_list[i]);
            b.setId(i + "");
            Piclist.add(b);
        }
        //两种效果 带滑动缩放效果以及圆角以及普通轮播图
        getBannerData(this, this, rl_banner, Piclist, false, false, getResources().getColor(R.color.white), getResources().getColor(R.color.redlive), 5, 5, 1);
        mDetailImgAdapter.setNewData(Piclist);
    }

    private void setShopAdapater(List<MyShopDetailBean.AboutListBean> list) {
        mShopAdapter = new BaseQuickAdapter<MyShopDetailBean.AboutListBean, BaseViewHolder>(R.layout.activity_myshop_item_layout, list) {
            @Override
            protected void convert(BaseViewHolder helper, MyShopDetailBean.AboutListBean item) {
                helper.setText(R.id.item_name, item.getGoods_name());
                helper.setText(R.id.item_price, "￥" + item.getPrice() + "");
//                helper.setText(R.id.item_price1,"￥"+item.getOldprice());
                TextView textView = helper.getView(R.id.item_price1);
                textView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                textView.setText("￥" + item.getOt_price());
                ImageView imgs = (ImageView) helper.getView(R.id.item_img);
                ViewGroup.LayoutParams params = imgs.getLayoutParams();
                params.width = CommentUtil.getDisplayWidth(mContext) / 2 - 20;
                params.height = params.width;
                imgs.setLayoutParams(params);
                if (!DataSafeUtils.isEmpty(item.getImg_list()) && item.getImg_list().length > 0)
                    Glide.with(mContext).load(item.getImg_list()[0]).into(imgs);
                else
                    Glide.with(mContext).load(R.mipmap.ic_launcher).into(imgs);
            }
        };
        mRecyclerView.setAdapter(mShopAdapter);
        GridLayoutManager manager = new GridLayoutManager(mContext, 2);
        mRecyclerView.setLayoutManager(manager);

        mShopAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                MyShopDetailBean.AboutListBean bean = mShopAdapter.getData().get(position);
                Intent intent = new Intent(mContext, MyShopDetailActivity.class);
                intent.putExtra("id", bean.getId() + "");
                intent.putExtra("status", mType + "");
                intent.putExtra("store_id", mList.get(0).getUid() + "");
                mContext.startActivity(intent);
                finish();
            }
        });
    }


    /**
     * 评论
     *
     * @param mCommentList
     */
    private void getCommentAdapter(List<CommentVO.DataBean.ListBean> mCommentList) {
        commentAdapter = new FoodCommentAdapter(R.layout.commentlist_item_adapter, mCommentList);
        commentRecyclerview.setAdapter(commentAdapter);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        commentRecyclerview.setLayoutManager(manager);
    }
    /**
     * 分享
     */
    private void share() {
        LiveShareDialogFragment fragment = new LiveShareDialogFragment();
        fragment.setActionListener(this);
        fragment.show(((AbsActivity) mContext).getSupportFragmentManager(), "LiveShareDialogFragment");
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_share:
                share();
                break;
            case R.id.shop_forwad:
//                if (this.mType.equals("0")) {
//
//                } else {
                Intent intent = new Intent(mContext, MyShopActivity.class);
                intent.putExtra("status", "1");
                intent.putExtra("store_id", mList.get(0).getStore_id() + "");
                mContext.startActivity(intent);
//                }
//                finish();
                break;
            case R.id.shop_down://下架
                GoodsLower();

                break;
            case R.id.shop_del://删除
                GoodsDel();
                break;
            case R.id.btn_back://删除
                finish();
                break;
            case R.id.live_layout://直播
                startActivity(new Intent(mContext, MainActivity.class));
                break;
            case R.id.kefu_layout://客服
                UserBean userBean=new UserBean();
                userBean.setId(store_info.getUid());
                userBean.setUserNiceName(store_info.getTitle());
                userBean.setAvatar(store_info.getLogo());
                userBean.setIs_attention("1");
                userBean.setConsumption("介绍一下这个商品"+"\n"+detail_title.getText()+"\n"+detail_price.getText());
                userBean.setIsSendMes("1");
                userBean.setCent("1");
                ChatRoomActivity.forward(this, userBean, false);
//                ChatRoomActivity.forward(mContext, userBean, 1);
//                if (!DataSafeUtils.isEmpty(mPhones))
//
//                    callPhoneTel(mContext, mPhones);
//                else
//                    ToastUtil.show("暂无联系方式");
////                showSelectorPop();
                break;
            case R.id.cart_layout://购物车
                startActivity(new Intent(mContext, CartMainActivity.class));
                break;
            case R.id.cart_add_layout://加入购物车
                isToBug = false;
//                if(Utils.isNotEmpty(selectlist)){
                showSelectorPopGg();
//                }else{
//                addGoodsToCart(mGoodsId);
//                }
                break;
            case R.id.buy_layout://立即购买
                isToBug = true;
//                if(Utils.isNotEmpty(selectlist)){
                showSelectorPopGg();
//                }else{
//                    setBuy();
//                }


//                if (Integer.parseInt(mStock) > 0) {
//                    Intent intent = new Intent(mContext, GoodsOrderActivity.class);
//                    intent.putExtra("goods_id", mGoodsId + "");
//                    mContext.startActivity(intent);
//                } else {
//                    ToastUtil.show("库存不足");
//                }
                break;
        }
    }

    TextView tv_gg;
    private void showSelectorPopGg() {//3规格，2，添加仓库，0，加入采购单 1，立即下单
        BottomSheetDialog mDialog = new BottomSheetDialog(mContext);
        View dialogView = View.inflate(mContext, R.layout.selector_view, null);
        int popHeight = DisplayUtils.getScreenHeight(mContext) * 1 / 3;
        if (Utils.isNotEmpty(selectlist)){
             popHeight = DisplayUtils.getScreenHeight(mContext) * 3 / 4;
        }

        RecyclerView rv = dialogView.findViewById(R.id.rv);
        TextView tv_jrcgd = dialogView.findViewById(R.id.tv_jrcgd);
        TextView tv_ljxd = dialogView.findViewById(R.id.tv_ljxd);
        ImageView miv = dialogView.findViewById(R.id.miv);
        ImageView iv_close = dialogView.findViewById(R.id.iv_close);
         tv_gg = dialogView.findViewById(R.id.tv_gg);

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        if (Utils.isNotEmpty(pic)) {
            GlideUtils.loadImage(mContext, pic, miv);
        }
        AddSubUtils add_sub = dialogView.findViewById(R.id.add_sub_2);
        add_sub.setCurrentNumber(defaultBuy);
        add_sub.setOnChangeValueListener(new AddSubUtils.OnChangeValueListener() {
            @Override
            public void onChangeValue(int value, int position) {
                defaultBuy = value;
            }
        });
        if (Utils.isNotEmpty(pic) && pic.contains("http")) {
            GlideUtils.loadImage(mContext, pic, miv);
        }
        TextView tv_name = dialogView.findViewById(R.id.tv_name);
        TextView tv_content = dialogView.findViewById(R.id.tv_content);
        TextView pop_tv_add = dialogView.findViewById(R.id.pop_tv_add);
//        TextView mTv_gg = dialogView.findViewById(R.id.tv_gg);
        tv_gg.setText(product_attrs.replace(",","  "));
        tv_name.setText(title);
        tv_content.setText("￥" + gprice);
        TypeSelectorAdapter tpAdapter = new TypeSelectorAdapter();
        rv.setLayoutManager(Utils.getLvManager(mContext));
        rv.setAdapter(tpAdapter);

//        List<GetGoodsAttrBean> selectlist=new ArrayList<>();
//        for(int i=0;i<10;i++){
//            selectlist.add(new GetGoodsAttrBean());
//        }
        if (Utils.isNotEmpty(selectlist)) {
            tpAdapter.setNewData(selectlist);
        }

        mDialog.contentView(dialogView)
                .heightParam(popHeight)
                .inDuration(200)
                .outDuration(200)
                .cancelable(true)
                .show();
        pop_tv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        tv_jrcgd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        tv_ljxd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//判断事件//3规格，2，添加仓库，0，加入采购单 1，立即下单
                if (Utils.isEmpty(selectlist)) {
                    mDialog.dismiss();

                    if (isToBug) {
                        setBuy();
                    } else {
                        addGoodsToCart(mGoodsId);
                    }
                } else if (Utils.isNotEmpty(selectlist) && isSelectorAll) {


                    mDialog.dismiss();

                    if (isToBug) {
                        setBuy();
                    } else {
                        addGoodsToCart(mGoodsId);
                    }

                } else {
                    ToastUtil.show("请先选择规格");
                }

            }
        });
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getEvent(EventBusModel event) {
        switch (event.getCode()) {
            case "refresh_detail_data":
                String goodID = (String) event.getObject();
                if(Utils.isNotEmpty(goodID)){
                    mGoodsId = goodID;
                }
                getData();
                break;
        }}
    @Subscribe
    public void getEvent(EventBean event) {
        switch (event.getCode()) {
            case "refresh_detail_data":
               String goodID = (String) event.getFirstObject();
               if(Utils.isNotEmpty(goodID)){
                   mGoodsId = goodID;
               }
               getData();
                break;
            case "send_pop_info_from_tag":
                Logger.e("AAA:", event.getFirstObject());
                try {
                    product_attrs = "";
                    if (Utils.isNotEmpty(selectlist)) {
                        int select = 0;
                        for (int i = 0; i < selectlist.size(); i++) {
                            List<GetGoodsAttrBean.DiyAttrValue> atts = selectlist.get(i).getAttrs();
                            for (int j = 0; j < atts.size(); j++) {
                                String check = atts.get(j).getCheck();
                                if (check.equals("true")) {
                                    select++;
                                    product_attrs = product_attrs + atts.get(j).getAttr() + ",";
                                }
                            }
                        }
                        tv_gg.setText(product_attrs.length()>0?"已选:"+product_attrs:"请选择规格");
                        int zcount = selectlist.size();
                        if (select != zcount) {
                            isSelectorAll = false;
                        } else {
                            isSelectorAll = true;
                        }
//                        mTv_gg.setText(product_attrs.replace(",","  "));
                    }
                } catch (Exception e) {
                    Logger.e("bbb:", event.getFirstObject());
                }

                break;
            default:

                break;
        }
    }

    ;

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    private void setBuy() {
        Intent intent = new Intent(mContext, PayGoodsOrderActivity.class);
        intent.putExtra("goods_id", mGoodsId);
        intent.putExtra("goods_num", defaultBuy + "");
        intent.putExtra("product_attrs", product_attrs.contains("请选择")?"":product_attrs);
        intent.putExtra("store_id",store_id);
        intent.putExtra("isMs",isMS);
        startActivity(intent);
//        finish();
//        HttpUtil.setPayOrder(mGoodsId, product_attrs, defaultBuy+"", new HttpCallback() {
//            @Override
//            public void onSuccess(int code, String msg, String[] info) {
//                Intent intent=new Intent(mContext, PayGoodsOrderActivity.class);
//                intent.putExtra("info",info);
//                startActivity(intent);
//                finish();
//            }
//        });
    }

    /**
     * 加入购物车
     *
     * @param id
     */
    private void addGoodsToCart(String id) {
        HttpUtil.addGoodsWithSpanToCart(id, product_attrs, "1", new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                Log.v("tags", code + "------" + msg);
                if (code == 0) {
                    ToastUtil.show("加入成功");
                }
            }
        });
    }

    /**
     * 拨打客服电话
     */
    public static void callPhoneTel(Context context, String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNumber);
        intent.setData(data);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.evaluation_more)
    public void onViewClicked() {
        Intent intent = new Intent(mContext, MoreCommentListActivity.class);
        intent.putExtra("id", mList.get(0).getId());
        startActivity(intent);
    }

    @Override
    protected void onDestroy01() {
        HttpUtil.cancel(HttpConsts.GET_GOODS_DETAIL);
        HttpUtil.cancel(HttpConsts.SET_GOODS_LOWER);
        HttpUtil.cancel(HttpConsts.DEL_GOODS);
        HttpUtil.cancel(HttpConsts.ADDCART);
    }

    @Override
    public void onItemClick(View view, PicBean bean) {
        ArrayList<String> mpiclist = new ArrayList<>();
        for (int i = 0; i < Piclist.size(); i++) {
            mpiclist.add(Piclist.get(i).getPic());
        }
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("list", mpiclist);
        bundle.putString("id", bean.getId());
        Intent intent = new Intent(this, PictureActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private int sx;
    private int sy;
    private SharedPreferences sp;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            // 如果手指放在imageView上拖动
            case R.id.rl_v:
                // event.getRawX(); //获取手指第一次接触屏幕在x方向的坐标
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:// 获取手指第一次接触屏幕
                        sx = (int) event.getRawX();
                        sy = (int) event.getRawY();
//                        iv_dv_view.setImageResource(R.drawable.t);
                        break;
                    case MotionEvent.ACTION_MOVE:// 手指在屏幕上移动对应的事件
                        int x = (int) event.getRawX();
                        int y = (int) event.getRawY();
                        // 获取手指移动的距离
                        int dx = x - sx;
                        int dy = y - sy;
                        // 得到imageView最开始的各顶点的坐标
                        int l = rl_v.getLeft();
                        int r = rl_v.getRight();
                        int t = rl_v.getTop();
                        int b = rl_v.getBottom();
                        // 更改imageView在窗体的位置
                        rl_v.layout(l + dx, t + dy, r + dx, b + dy);
                        // 获取移动后的位置
                        sx = (int) event.getRawX();
                        sy = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_UP:// 手指离开屏幕对应事件
                        // 记录最后图片在窗体的位置
                        int lasty = rl_v.getTop();
                        int lastx = rl_v.getLeft();
//                        rl_v.setImageResource(R.drawable.next);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putInt("lasty", lasty);
                        editor.putInt("lastx", lastx);
                        editor.commit();
                        break;
                }
                break;
        }
        return true;// 不会中断触摸事件的返回
    }

    @Override
    public void onItemClick(String type) {
        if (Constants.LINK.equals(type)) {
            copyLink();
        } else {
//            shareLive(type, null);
        }
    }



    /**
     * 复制直播间链接
     */
    private void copyLink() {
        if (TextUtils.isEmpty("")) {
            return;
        }
        ConfigBean configBean = AppConfig.getInstance().getConfig();
        if (configBean == null) {
            return;
        }
        String link = configBean.getLiveWxShareUrl() + "";
        ClipboardManager cm = (ClipboardManager) mContext.getSystemService(CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("text", link);
        cm.setPrimaryClip(clipData);
        ToastUtil.show(WordUtil.getString(R.string.copy_success));
    }


//    /**
//     * 分享直播间
//     */
//    public void shareLive(String type, MobCallback callback) {
//        ConfigBean configBean = AppConfig.getInstance().getConfig();
//        if (configBean == null) {
//            return;
//        }
//        if (mMobShareUtil == null) {
//            mMobShareUtil = new MobShareUtil();
//        }
//        ShareData data = new ShareData();
//        data.setTitle(configBean.getLiveShareTitle());
//        data.setDes(mLiveBean.getUserNiceName() + configBean.getLiveShareDes());
//        data.setImgUrl(mLiveBean.getAvatarThumb());
//        String webUrl = configBean.getDownloadApkUrl();
//        if (MobConst.Type.WX.equals(type) || MobConst.Type.WX_PYQ.equals(type)) {
//            webUrl = configBean.getLiveWxShareUrl() + mLiveUid;
//        }
//        data.setWebUrl(webUrl);
//        mMobShareUtil.execute(type, data, callback);
//    }


}
