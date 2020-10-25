package com.mc.phonelive.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.makeramen.roundedimageview.RoundedImageView;
import com.mc.phonelive.AppConfig;
import com.mc.phonelive.R;
import com.mc.phonelive.activity.shop.CartMainActivity;
import com.mc.phonelive.activity.shop.GoodsOrderActivity;
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
import com.picture.android.PictureActivity;

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
public class MyShopDetailActivityBackUp extends AbsActivity implements View.OnClickListener, OnItemViewClickListener {
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
    private ImageView btn_back;
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

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_myshop_detail_bp;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void main() {
        Log.e("DDDD:","DPXQ");
        mRecyclerView = findViewById(R.id.recyclerView);
        titleView = findViewById(R.id.titleView);
        cart_add_layout = findViewById(R.id.cart_add_layout);
        buy_layout = findViewById(R.id.buy_layout);
        live_layout = findViewById(R.id.live_layout);
        kefu_layout = findViewById(R.id.kefu_layout);
        cart_layout = findViewById(R.id.cart_layout);
        btn_back = findViewById(R.id.btn_back);
        more_layout = findViewById(R.id.more_layout);
        detail_price = findViewById(R.id.detail_price);
        detail_title = findViewById(R.id.detail_title);
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

        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            String id = bundle.getString("id");
            String status = bundle.getString("status");
            String store_id = bundle.getString("store_id");
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

                if (status.equals("1")) {//客户
                    kehu_layout.setVisibility(View.VISIBLE);
                    business_layout.setVisibility(View.GONE);
                } else {//商家自己
                    kehu_layout.setVisibility(View.GONE);
                    business_layout.setVisibility(View.VISIBLE);
                }
            }

            getData();
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
                    Top_Layouat.setBackgroundColor(Color.argb(alpha, 50, 50, 50));
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
                    mList = list;
                    mSales = list.get(0).getSales();
                    setShopData(list.get(0));
                    if (!DataSafeUtils.isEmpty(list.get(0).getStore_info())) {
                        setShopMsg(list.get(0).getStore_info());
                    }
                    if (!DataSafeUtils.isEmpty(list.get(0).getAbout_list())) {
                        more_layout.setVisibility(View.VISIBLE);
                        setShopAdapater(list.get(0).getAbout_list());
                    } else {
                        more_layout.setVisibility(View.GONE);
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
                return DialogUitl.loadingDialog(MyShopDetailActivityBackUp.this);
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
                            MyShopDetailActivityBackUp.this.finish();
                        }else{
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
        String mTitle="";
        String msg="";
        if (is_show.equals("1")){
            mTitle="上架";
            msg="确定上架此商品吗？";
        }else{
            mTitle="下架";
            msg="确定下架此商品吗？";
        }
        CancelORderDialog oRderDialog = new CancelORderDialog(this, mTitle, msg) {
            @Override
            public void doConfirmUp() {

                HttpUtil.shopGoodsLower(mGoodsId, is_show, new HttpCallback() {
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
                        if (code == 0) {
                            Toast.makeText(mContext, "操作成功", Toast.LENGTH_SHORT).show();
                            MyShopDetailActivityBackUp.this.finish();
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

        is_showName = bean.getIs_show_name();
        if (this.mType.equals("1")) {//客户
            if (bean.getIs_show().equals("2")) {
                kehu_layout.setVisibility(View.GONE);
            } else {
                kehu_layout.setVisibility(View.VISIBLE);
            }
        }

        if (!DataSafeUtils.isEmpty(bean.getGoods_freight())) {
            expressPrice.setText("￥" + bean.getGoods_freight());
        }

        if (bean.getIs_show().equals("1")) {
            shop_down.setText("下架");
            is_show = "2";
        } else {
            shop_down.setText("上架");
            is_show = "1";
        }
        if (!DataSafeUtils.isEmpty(bean.getImg_list()) && bean.getImg_list().length > 0) {
            setAdvAdapterData(bean.getImg_list());
        }
        detail_price.setText("￥" + bean.getPrice());
        detail_title.setText(bean.getTitle());

        if (!DataSafeUtils.isEmpty(bean.getInfo())) {
//            detail_content.setText(bean.getInfo());
            detail_content.setVisibility(View.VISIBLE);
        } else {
            detail_content.setVisibility(View.GONE);
        }

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
        if (!DataSafeUtils.isEmpty(store_info.getLogo()))
            Glide.with(mContext).load(store_info.getLogo()).into(shop_img);
        else
            Glide.with(mContext).load(R.mipmap.ic_launcher).into(shop_img);
        shop_name.setText(store_info.getTitle());
        shop_content.setText("已售" + mSales + "件");
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
                finish();
                MyShopDetailBean.AboutListBean bean = mShopAdapter.getData().get(position);
                Intent intent = new Intent(mContext, MyShopDetailActivityBackUp.class);
                intent.putExtra("id", bean.getId() + "");
                intent.putExtra("status", mType + "");
                intent.putExtra("store_id", mList.get(0).getUid() + "");
                mContext.startActivity(intent);
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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.shop_forwad:
                if (this.mType.equals("0")) {

                } else {
                    Intent intent = new Intent(mContext, MyShopActivity.class);
                    intent.putExtra("status", mType + "");
                    intent.putExtra("store_id", mList.get(0).getUid() + "");
                    mContext.startActivity(intent);
                }
                finish();
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
                if (!DataSafeUtils.isEmpty(mPhones))
                    callPhoneTel(mContext, mPhones);
                else
                    ToastUtil.show("暂无联系方式");
                break;
            case R.id.cart_layout://购物车
                startActivity(new Intent(mContext, CartMainActivity.class));
                break;
            case R.id.cart_add_layout://加入购物车
                addGoodsToCart(mGoodsId);
                break;
            case R.id.buy_layout://立即购买
                if (Integer.parseInt(mStock) > 0) {
                    Intent intent = new Intent(mContext, GoodsOrderActivity.class);
                    intent.putExtra("goods_id", mGoodsId + "");
                    mContext.startActivity(intent);
                } else {
                    ToastUtil.show("库存不足");
                }
                break;
        }
    }

    /**
     * 加入购物车
     *
     * @param id
     */
    private void addGoodsToCart(String id) {
        HttpUtil.addGoodsToCart(id, "1", new HttpCallback() {
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
}
