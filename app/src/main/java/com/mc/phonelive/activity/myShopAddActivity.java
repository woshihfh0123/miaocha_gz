package com.mc.phonelive.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nanchen.compresshelper.CompressHelper;
import com.mc.phonelive.AppConfig;
import com.mc.phonelive.R;
import com.mc.phonelive.bean.ShopGoodsAddBean;
import com.mc.phonelive.httpnet.Callback;
import com.mc.phonelive.httpnet.HttpUtils;
import com.mc.phonelive.httpnet.JsonUtils;
import com.mc.phonelive.utils.DataSafeUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerPreviewActivity;
import cn.bingoogolapple.photopicker.widget.BGASortableNinePhotoLayout;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

/**
 * 添加商品
 */
@RuntimePermissions
public class myShopAddActivity extends AbsActivity {
    TextView condition_apply,titleView;
    private EditText add_link, add_name, add_oldprice, add_price, add_content,add_stock,add_postage,add_detail;
    private BGASortableNinePhotoLayout mPhotoLayout;

    private ArrayList<String> mPicList = new ArrayList<>();
    List<File> mFilelist = new ArrayList<>();
    public static int SELECTIMGCOUNT = 6; // 可以上传图片的数量
    private static final int RC_CHOOSE_PHOTO = 1;
    private static final int RC_PHOTO_PREVIEW = 2;
    public static final String SDCARD_CACHE = "com.zhiboshow/files/"; // 文件sdk缓存
    private boolean isSend=false;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_myshop_add;
    }

    @Override
    protected void main() {
        condition_apply = findViewById(R.id.condition_apply);
        titleView = findViewById(R.id.titleView);
        titleView.setText("添加商品");
        add_link = findViewById(R.id.add_link);
        add_name = findViewById(R.id.add_name);
        add_oldprice = findViewById(R.id.add_oldprice);
        add_price = findViewById(R.id.add_price);
        add_content = findViewById(R.id.add_content);
        add_stock = findViewById(R.id.add_stock);
        add_postage = findViewById(R.id.add_postage);
        add_detail = findViewById(R.id.add_detail);

        mPhotoLayout = findViewById(R.id.phone_layout);
        mPhotoLayout.setMaxItemCount(5);
        mPhotoLayout.setEditable(true);//有加号，有删除，可以点加号选择，false没有加号，点其他按钮选择，也没有删除
        mPhotoLayout.setPlusEnable(true);//有加号，可以点加号选择，false没有加号，点其他按钮选择
        mPhotoLayout.setSortable(true);//排序
        mPhotoLayout.setDelegate(new BGASortableNinePhotoLayout.Delegate() {
            @Override
            public void onClickAddNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, ArrayList<String> models) {

                myShopAddActivityPermissionsDispatcher.choisePicDataWithPermissionCheck(myShopAddActivity.this);
            }

            @Override
            public void onClickDeleteNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, String model, ArrayList<String> models) {
                mPhotoLayout.removeItem(position);
//                evaluationBeans.get(itemposition).getImages().remove(position);
                mFilelist.remove(position);
            }

            @Override
            public void onClickNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, String model, ArrayList<String> models) {
                Intent photoPickerPreviewIntent = new BGAPhotoPickerPreviewActivity.IntentBuilder(myShopAddActivity.this)
//                .cameraFileDir(mTakePhotoCb.isChecked() ? takePhotoDir : null) // 拍照后照片的存放目录，改成你自己拍照后要存放照片的目录。如果不传递该参数的话则不开启图库里的拍照功能
                        .previewPhotos(models) // 当前预览的图片路径集合
                        .selectedPhotos(models) // 当前已选中的图片路径集合
                        .maxChooseCount(SELECTIMGCOUNT) // 图片选择张数的最大值
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

        condition_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (DataSafeUtils.isEmpty(add_name.getText().toString())) {
                    Toast.makeText(myShopAddActivity.this, "商品名称不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (DataSafeUtils.isEmpty(add_oldprice.getText().toString())) {
                    Toast.makeText(myShopAddActivity.this, "原价不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (DataSafeUtils.isEmpty(add_price.getText().toString())) {
                    Toast.makeText(myShopAddActivity.this, "现价不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (DataSafeUtils.isEmpty(add_content.getText().toString())) {
                    Toast.makeText(myShopAddActivity.this, "商品简介不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (DataSafeUtils.isEmpty(add_stock.getText().toString())) {
                    Toast.makeText(myShopAddActivity.this, "请添加库存", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (DataSafeUtils.isEmpty(add_postage.getText().toString().trim())) {
                    Toast.makeText(myShopAddActivity.this, "请填写邮费", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (DataSafeUtils.isEmpty(mFilelist)) {
                    Toast.makeText(myShopAddActivity.this, "请选择图片", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent();
                intent.putExtra("addlink", "");
                intent.putExtra("addname", "");
                intent.putExtra("addcontent", "");
                intent.putExtra("addimg", "");
                if (!isSend) {
                    isSend=true;
                    condition_apply.setBackgroundResource(R.drawable.bg_myshop_condition1);
                    getData(add_name.getText().toString(), add_oldprice.getText().toString(), add_price.getText().toString(),add_stock.getText().toString(),add_postage.getText().toString(), add_content.getText().toString(),add_detail.getText().toString(), mFilelist, mPicList);
                }
            }
        });

    }

    /**
     * 提交数据
     */
    private void getData(String name, String oldPrice, String price,String stock,String postage, String content,String AddDetail, List<File> mFilelist, ArrayList<String> mPicList) {

        Map<String, String> map = new HashMap<>();
        map.put("uid", AppConfig.getInstance().getUid());
        map.put("token", AppConfig.getInstance().getToken());
        map.put("title", name);
        map.put("ot_price", oldPrice+"");
        map.put("price", price);
        map.put("stock", stock);
        map.put("postage", postage);
        map.put("info", content+"");
        map.put("description", AddDetail+"");
        Map<String, List<String>> filepath = new HashMap<>();

        Map<String, String> singleFilePath = new HashMap<>();
        for (int i = 0; i < mPicList.size(); i++) {
            singleFilePath.put("file_" + i, mPicList.get(i));
        }

        String mUrls = AppConfig.HOST + "/api/public/?service=";
        HttpUtils.POST_WHITH_UPLOAD(mUrls + "User.AddGood", map, singleFilePath, filepath, true, ShopGoodsAddBean.class, new Callback<ShopGoodsAddBean>() {
            @Override
            public void onStart() {
            }

            @Override
            public void onSucceed(String json, String code, ShopGoodsAddBean model) {
                isSend=false;
                condition_apply.setBackgroundResource(R.drawable.bg_myshop_condition);
                String msg = JsonUtils.getSinglePara(json, "msg");
                Log.v("tags", model.getData().getCode() + "---");
//                Toast.makeText(myShopAddActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                if (model.getData().getCode() == 0)
                    myShopAddActivity.this.finish();
                else
                    Toast.makeText(myShopAddActivity.this, model.getData().getMsg(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onOtherStatus(String json, String code) {
                String msg = JsonUtils.getSinglePara(json, "msg");
                Log.v("tags", msg);
            }

            @Override
            public void onFailed(Throwable e) {
                Log.v("tags", "---错误---");
            }

            @Override
            public void onFinish() {
                condition_apply.setBackgroundResource(R.drawable.bg_myshop_condition);
                isSend=false;
                Log.v("tags", "---结束---");
            }
        });

    }


    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void choisePicData() {
        // 拍照后照片的存放目录，改成你自己拍照后要存放照片的目录。如果不传递该参数的话就没有拍照功能
        File takePhotoDir = new File(Environment.getExternalStorageDirectory(), SDCARD_CACHE);
        int mPicSize = 0;
        if (!DataSafeUtils.isEmpty(mPhotoLayout.getData().size())) {
            mPicSize = mPhotoLayout.getData().size();
        }
        Intent photoPickerIntent = new BGAPhotoPickerActivity.IntentBuilder(this)
                .cameraFileDir(TextUtils.isEmpty(SDCARD_CACHE) ? null : takePhotoDir) // 拍照后照片的存放目录，改成你自己拍照后要存放照片的目录。如果不传递该参数的话则不开启图库里的拍照功能
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
        myShopAddActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
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
            ArrayList<String> selectedPhotos = BGAPhotoPickerActivity.getSelectedPhotos(data);
//            mPicList = BGAPhotoPickerActivity.getSelectedPhotos(data);
//            mPhotoLayout.setData(mPicList);
            mPhotoLayout.addMoreData(selectedPhotos);

        } else if (requestCode == RC_PHOTO_PREVIEW) {
            // 在预览界面按返回也会回传预览界面已选择的图片集合
            ArrayList<String> selectedPhotos = BGAPhotoPickerPreviewActivity.getSelectedPhotos(data);
//            mPhotoLayout.setData(BGAPhotoPickerPreviewActivity.getSelectedPhotos(data));
//            mPicList = BGAPhotoPickerPreviewActivity.getSelectedPhotos(data);
//            mPhotoLayout.setData(mPicList);
            mPhotoLayout.addMoreData(selectedPhotos);
        }

        for (int i = 0; i < mPhotoLayout.getData().size(); i++) {
            mPicList.add(mPhotoLayout.getData().get(i));
            File file = new File(mPhotoLayout.getData().get(i));
            File newFile = CompressHelper.getDefault(myShopAddActivity.this).compressToFile(file);
            mFilelist.add(newFile);
            Log.v("tags", mPhotoLayout.getData().get(i));
        }
    }

}
