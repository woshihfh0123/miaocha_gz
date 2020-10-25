package com.mc.phonelive.fragment;

import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.mc.phonelive.R;
import com.mc.phonelive.bean.foxtone.ShareBean;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.utils.DataSafeUtils;
import com.mc.phonelive.utils.ToastUtil;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 任务大厅
 */
public class ShareFragment01 extends BaseFragment {

    Unbinder unbinder;
    View layoutView;
    @BindView(R.id.code_tv)
    TextView codeTv;
    @BindView(R.id.copy_tv)
    TextView copyTv;
    @BindView(R.id.link_tv)
    TextView linkTv;
    @BindView(R.id.link_copy)
    TextView linkCopy;

    public static ShareFragment01 newInstance() {
        ShareFragment01 fragment = new ShareFragment01();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutView = inflater.inflate(R.layout.share_fragment01, container, false);
        unbinder = ButterKnife.bind(this, layoutView);
        return layoutView;
    }


    @Override
    protected void initView(View view) {

    }

    @Override
    protected void initHttpData() {
        HttpUtil.getTeamInvite( new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                List<ShareBean.InfoBean> data = JSON.parseArray(Arrays.toString(info),ShareBean.InfoBean.class);
                    if (!DataSafeUtils.isEmpty(data) && data.size()>0){
                        if (!DataSafeUtils.isEmpty(data.get(0).getInvite_code())){
                            codeTv.setText(data.get(0).getInvite_code());
                        }
                        if (!DataSafeUtils.isEmpty(data.get(0).getInvite_url())){
                            linkTv.setText(data.get(0).getInvite_url());
                        }
                    }
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.copy_tv, R.id.link_copy})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.copy_tv:
                if (!DataSafeUtils.isEmpty(codeTv.getText().toString()))
                    copyData(codeTv.getText().toString());
                else
                    ToastUtil.show("邀请码不能为空");
                break;
            case R.id.link_copy:
                if (!DataSafeUtils.isEmpty(linkTv.getText().toString()))
                    copyData(linkTv.getText().toString());
                else
                    ToastUtil.show("邀请连接不能为空");
                break;
        }
    }

    private void copyData(String copytv) {
        ClipboardManager copy = (ClipboardManager) getActivity()
                .getSystemService(Context.CLIPBOARD_SERVICE);
        if (!TextUtils.isEmpty(copytv)) {
            copy.setText(copytv + "");
        }
        Toast.makeText(getActivity(), "复制成功", Toast.LENGTH_SHORT).show();
    }
}
