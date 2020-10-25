package com.mc.phonelive.adapter.shop;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mc.phonelive.R;
import com.mc.phonelive.activity.shop.AddressAddActivity;
import com.mc.phonelive.bean.AddressVO;
import com.mc.phonelive.im.EventBusModel;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Administrator on 2017/2/13.
 */

public class AddressListAdapter extends BaseQuickAdapter<AddressVO.DataBean.InfoBean, BaseViewHolder> {

    public AddressListAdapter(int layoutResId, @Nullable List<AddressVO.DataBean.InfoBean> data) {
        super(R.layout.address_adapter, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final AddressVO.DataBean.InfoBean vo) {
        helper.setText(R.id.name_tv, ""+vo.getReal_name() + "");
        helper.setText(R.id.phone_tv, vo.getPhone() + "");
        helper.setText(R.id.address_tv, vo.getDetail());
        if ("1".equals(vo.getIs_default())) {
            helper.setImageResource(R.id.check_img, R.drawable.check_select);
        } else {
            helper.setImageResource(R.id.check_img, R.drawable.check_normal);
        }
        helper.getView(R.id.delete_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("确定要删除该地址吗?")
                        .setConfirmText("确定")
                        .setCancelText("取消")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();//直接消失
                                EventBus.getDefault().post(new EventBusModel("address_delete", vo.getId()));
                            }
                        })
                        .show();

            }
        });
        helper.getView(R.id.edit_text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("address", (Serializable) vo);
                intent.setClass(mContext, AddressAddActivity.class);
                mContext.startActivity(intent);
            }
        });
        helper.getView(R.id.check_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("1".equals(vo.getIs_default()))
                    return;

                new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("确定设为默认地址吗?")
                        .setConfirmText("确定")
                        .setCancelText("取消")

                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();//直接消失
                                EventBus.getDefault().post(new EventBusModel("address_defalut", vo.getId()));
                            }
                        })
                        .show();


            }
        });
    }


}
