package com.mc.phonelive.activity.shop;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.nanchen.compresshelper.CompressHelper;
import com.mc.phonelive.AppConfig;
import com.mc.phonelive.R;
import com.mc.phonelive.activity.AbsActivity;
import com.mc.phonelive.bean.BaseVO;
import com.mc.phonelive.bean.EvaluationBean;
import com.mc.phonelive.bean.OrderDetailVO;
import com.mc.phonelive.constant.Constant;
import com.mc.phonelive.dialog.CancelORderDialog;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.httpnet.Callback;
import com.mc.phonelive.httpnet.HttpUtils;
import com.mc.phonelive.im.EventBusModel;
import com.mc.phonelive.utils.ButtonUtils;
import com.mc.phonelive.utils.DataSafeUtils;
import com.mc.phonelive.utils.DialogUitl;
import com.mc.phonelive.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerPreviewActivity;
import cn.bingoogolapple.photopicker.widget.BGASortableNinePhotoLayout;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

/**
 * created by WWL on 2019/4/27 0027:16
 * 发布评论
 */
@RuntimePermissions
public class FoodWritingCommentActivity extends AbsActivity {

    @BindView(R.id.comment_sj_ratingbar)
    RatingBar commentSjRatingbar;
    @BindView(R.id.type_tv)
    TextView typeTv;
    @BindView(R.id.my_titlebar)
    TextView my_titlebar;
    @BindView(R.id.comment_sj_edit)
    EditText commentSjEdit;
    @BindView(R.id.submit_ok)
    TextView submitOk;
    boolean isChoise = false;
    @BindView(R.id.comment_recyclerview)
    RecyclerView commentRecyclerview;
    @BindView(R.id.titleView)
    TextView titleView;
    @BindView(R.id.btn_back)
    ImageView btnBack;
    private ArrayList<String> mPicList = new ArrayList<>();
    private String mOrderNo = "";
    private String mUid = "";
    private String mToken = "";
    public static int SELECTIMGCOUNT = 9; // 可以上传图片的数量
    private static final int RC_CHOOSE_PHOTO = 1;
    private static final int RC_PHOTO_PREVIEW = 2;
    private List<EvaluationBean> evaluationBeans = new ArrayList<>();
    private List<OrderDetailVO.InfoBean.GoodsListBean> mGoodList = new ArrayList<>();
    BaseQuickAdapter<OrderDetailVO.InfoBean.GoodsListBean, BaseViewHolder> mAdapter;
    private BGASortableNinePhotoLayout mPhotoItemLayout;
    private int mTempPosition;//存放选择的商品position

    @Override
    protected int getLayoutId() {
        return R.layout.foodorder_write_comment_layout;
    }

    @Override
    protected void main() {
        titleView.setText("发布评论");
        setBarModel(true);
        String orderno = this.getIntent().getStringExtra("order_id");
        if (!TextUtils.isEmpty(orderno)) {
            this.mOrderNo = orderno;
        }
        mUid = AppConfig.getInstance().getUid();
        mToken = AppConfig.getInstance().getToken();

        my_titlebar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (evaluationBeans.size() > 0) {
                    JSONArray json = new JSONArray();
                    for (int i = 0; i < evaluationBeans.size(); i++) {
                        if (evaluationBeans.get(i).getContent().equals("")) {
                            Toast.makeText(FoodWritingCommentActivity.this, "请输入评价内容~", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        JSONObject jo = new JSONObject();
                        try {
                            jo.put("content", evaluationBeans.get(i).getContent());
                            jo.put("id", evaluationBeans.get(i).getId());
                            jo.put("goods_id", evaluationBeans.get(i).getGoods_id());
                            json.put(jo);
                        } catch (JSONException e) {
                        }
                    }
                    if (!ButtonUtils.isFastDoubleClick02()) {

                        writtingSubmitData(json + "");
                    }
                }
            }
        });


        getAdapter(mGoodList);


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message();
            }
        });

        initHttpData();
    }

    /**
     * 提交数据
     */
    private void writtingSubmitData(String json) {

        Map<String, String> map = new HashMap<>();
        map.put("uid", mUid);
        map.put("token", mToken);
        map.put("data", json + "");
        map.put("order_id", mOrderNo + "");
        Log.v("tags","json="+json);
        Map<String, String> singleFilePath = new HashMap<>();
        Map<String, List<String>> filepath = new HashMap<>();
        for (int i = 0; i < evaluationBeans.size(); i++) {
            if (evaluationBeans.get(i).getImages() != null) {
                List<String> mfileList = new ArrayList<>();
                for (int j = 0; j < evaluationBeans.get(i).getImages().size(); j++) {
                    mfileList.add(evaluationBeans.get(i).getImages().get(j).toString() + "");
                }
                filepath.put("images_" + evaluationBeans.get(i).getId(), mfileList);
            }
        }

        Log.v("tags","filepath="+filepath);

        HttpUtils.POST_WHITH_UPLOAD( AppConfig.HOST + "/api/public/appapi/?service=order.addOrderEvaluate", map, singleFilePath, filepath, true, BaseVO.class, new Callback<BaseVO>() {
            @Override
            public void onStart() {
                showDialog();
            }

            @Override
            public void onSucceed(String json, String code, BaseVO model) {
                Log.v("tags",json);
                ToastUtil.show("评论成功");
                EventBus.getDefault().post(new EventBusModel("orderdetail_refresh"));
                FoodWritingCommentActivity.this.finish();
            }

            @Override
            public void onOtherStatus(String json, String code) {
//                String msg = JsonUtils.getSinglePara(json, "msg");
//                ToastUtil.show(msg);
            }

            @Override
            public void onFailed(Throwable e) {
                dismissDialog();
            }

            @Override
            public void onFinish() {
                dismissDialog();
            }
        });

    }

    private void getAdapter(List<OrderDetailVO.InfoBean.GoodsListBean> mGoodList) {
        mAdapter = new BaseQuickAdapter<OrderDetailVO.InfoBean.GoodsListBean, BaseViewHolder>(R.layout.write_comment_view, mGoodList) {
            @Override
            protected void convert(BaseViewHolder helper, final OrderDetailVO.InfoBean.GoodsListBean item) {
                final int itemposition = mAdapter.getData().indexOf(item);
                ImageView imgs = helper.getView(R.id.food_img);
                final ImageView comment_anonymous = helper.getView(R.id.comment_anonymous);
                final LinearLayout choise_anonymous = helper.getView(R.id.choise_anonymous);
                final BGASortableNinePhotoLayout mPhotoLayout = helper.getView(R.id.phone_layout);
                RatingBar mRatingBar = helper.getView(R.id.food_ratingbar);
                EditText mEditText = helper.getView(R.id.food_remark);
                if (!TextUtils.isEmpty(item.getGoods_image())) {
                    Glide.with(mContext).load(item.getGoods_image()).into(imgs);
                }
                if (!TextUtils.isEmpty(item.getGoods_name())) {
                    helper.setText(R.id.food_name, item.getGoods_name());
                }
//                mRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
//                    @Override
//                    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
////                        Toast.makeText(mContext, "" + ratingBar.getRating(), Toast.LENGTH_SHORT).show();
//                        evaluationBeans.get(itemposition).setScores(ratingBar.getRating() + "");
//                    }
//                });
                //这里通过监听事件将EditText中的数据传到EvaluationBean中
                mEditText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        evaluationBeans.get(itemposition).setContent(s.toString());
                    }
                });

//                choise_anonymous.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (item.getIs_anonymous().equals("1")) {
//                            comment_anonymous.setImageResource(R.drawable.check_select);
//                            item.setIs_anonymous("0");
//                            mAdapter.getData().get(itemposition).setIs_anonymous("0");
//                            evaluationBeans.get(itemposition).setIs_anonymous("0");
//                        } else {
//                            comment_anonymous.setImageResource(R.drawable.check_normal);
//                            item.setIs_anonymous("1");
//                            mAdapter.getData().get(itemposition).setIs_anonymous("1");
//                            evaluationBeans.get(itemposition).setIs_anonymous("1");
//                        }
//                    }
//                });


                mPhotoLayout.setMaxItemCount(9);
                mPhotoLayout.setEditable(true);//有加号，有删除，可以点加号选择，false没有加号，点其他按钮选择，也没有删除
                mPhotoLayout.setPlusEnable(true);//有加号，可以点加号选择，false没有加号，点其他按钮选择
                mPhotoLayout.setSortable(true);//排序
                mPhotoLayout.setDelegate(new BGASortableNinePhotoLayout.Delegate() {
                    @Override
                    public void onClickAddNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, ArrayList<String> models) {
                        mPhotoItemLayout = mPhotoLayout;
                        mTempPosition = itemposition;

                        FoodWritingCommentActivityPermissionsDispatcher.choisePicDataWithPermissionCheck(FoodWritingCommentActivity.this);
                    }

                    @Override
                    public void onClickDeleteNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, String model, ArrayList<String> models) {
                        mPhotoLayout.removeItem(position);
                        evaluationBeans.get(itemposition).getImages().remove(position);

                    }

                    @Override
                    public void onClickNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, String model, ArrayList<String> models) {
                        Intent photoPickerPreviewIntent = new BGAPhotoPickerPreviewActivity.IntentBuilder(FoodWritingCommentActivity.this)
//                .cameraFileDir(mTakePhotoCb.isChecked() ? takePhotoDir : null) // 拍照后照片的存放目录，改成你自己拍照后要存放照片的目录。如果不传递该参数的话则不开启图库里的拍照功能
                                .previewPhotos(models) // 当前预览的图片路径集合
                                .selectedPhotos(models) // 当前已选中的图片路径集合
                                .maxChooseCount(9) // 图片选择张数的最大值
//                .maxChooseCount(mPhotoLayout.getMaxItemCount()) // 图片选择张数的最大值
                                .currentPosition(position) // 当前预览图片的位置
                                .isFromTakePhoto(false) // 是否是拍完照后跳转过来
                                .build();
                        startActivity(photoPickerPreviewIntent);
//        startActivityForResult(photoPickerPreviewIntent, RC_PHOTO_PREVIEW);
                    }

                    @Override
                    public void onNinePhotoItemExchanged(BGASortableNinePhotoLayout sortableNinePhotoLayout, int fromPosition, int toPosition, ArrayList<String> models) {

                    }
                });
            }
        };
        commentRecyclerview.setAdapter(mAdapter);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        commentRecyclerview.setLayoutManager(manager);
    }

    protected void initHttpData() {
        HttpUtil.OrderGetOrderDetail(mOrderNo, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0)
                    if (info.length > 0) {
                        OrderDetailVO.InfoBean bean = JSON.parseObject(info[0], OrderDetailVO.InfoBean.class);
                        evaluationBeans.clear();
                        if (!DataSafeUtils.isEmpty(bean.getGoods_list())) {
                            for (int x = 0; x < bean.getGoods_list().size(); x++) {
                                EvaluationBean evaluationBean = new EvaluationBean();
                                evaluationBean.setUnique("");
                                evaluationBean.setContent("");
                                evaluationBean.setId(bean.getGoods_list().get(x).getId());
                                evaluationBean.setGoods_id(bean.getGoods_list().get(x).getGoods_id());
                                evaluationBeans.add(evaluationBean);
                            }
                            mAdapter.setNewData(bean.getGoods_list());
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void choisePicData() {
        // 拍照后照片的存放目录，改成你自己拍照后要存放照片的目录。如果不传递该参数的话就没有拍照功能
        File takePhotoDir = new File(Environment.getExternalStorageDirectory(), Constant.SDCARD_CACHE);
        int mPicSize = 0;
        if (!DataSafeUtils.isEmpty(evaluationBeans.get(mTempPosition).getImages())) {
            mPicSize = evaluationBeans.get(mTempPosition).getImages().size();
        }
        Intent photoPickerIntent = new BGAPhotoPickerActivity.IntentBuilder(this)
                .cameraFileDir(TextUtils.isEmpty(Constant.SDCARD_CACHE) ? null : takePhotoDir) // 拍照后照片的存放目录，改成你自己拍照后要存放照片的目录。如果不传递该参数的话则不开启图库里的拍照功能
                .maxChooseCount(SELECTIMGCOUNT - mPicSize) // 图片选择张数的最大值
//                .maxChooseCount(SELECTIMGCOUNT - mPicList.size()) // 图片选择张数的最大值
                .selectedPhotos(null) // 当前已选中的图片路径集合
                .pauseOnScroll(false)
//                    .selectedPhotos(mSelectList)
                // 滚动列表时是否暂停加载图片
                .build();
        startActivityForResult(photoPickerIntent, RC_CHOOSE_PHOTO);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        FoodWritingCommentActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @OnShowRationale({Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void showPicData(final PermissionRequest request) {
//        Toast.makeText(mActivity, ""+request.toString(), Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        Log.e("mPhotoLayout","data:"+data);
        if (resultCode == RESULT_OK && requestCode == RC_CHOOSE_PHOTO) {
            //是否单选，单选走true 语句，多选走false语句，这么默认false
//            List<String> selectedPhotos = BGAPhotoPickerActivity.getSelectedPhotos(data);
            mPicList = BGAPhotoPickerActivity.getSelectedPhotos(data);
//            mPhotoLayout.setData(mPicList);
            mPhotoItemLayout.addMoreData(mPicList);

        } else if (requestCode == RC_PHOTO_PREVIEW) {
            // 在预览界面按返回也会回传预览界面已选择的图片集合
//            List<String> selectedPhotos = BGAPhotoPickerPreviewActivity.getSelectedPhotos(data);
//            mPhotoLayout.setData(BGAPhotoPickerPreviewActivity.getSelectedPhotos(data));
            mPicList = BGAPhotoPickerPreviewActivity.getSelectedPhotos(data);
//            mPhotoLayout.setData(mPicList);
            mPhotoItemLayout.addMoreData(mPicList);
        }

        List<File> mFilelist = new ArrayList<>();
        for (int i = 0; i < mPhotoItemLayout.getData().size(); i++) {
            File file = new File(mPhotoItemLayout.getData().get(i));
            File newFile = CompressHelper.getDefault(FoodWritingCommentActivity.this).compressToFile(file);
            mFilelist.add(newFile);
            evaluationBeans.get(mTempPosition).setImages(mFilelist);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Message();
            return true;
        }
        return true;
    }

    private void Message() {
        CancelORderDialog oRderDialog = new CancelORderDialog(this, "取消评论", "确定取消评论吗？") {
            @Override
            public void doConfirmUp() {
                finish();
            }
        };
        oRderDialog.show();
    }
}
