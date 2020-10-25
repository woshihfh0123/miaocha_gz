package com.mc.phonelive.activity.foxtone;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.mc.phonelive.R;
import com.mc.phonelive.activity.AbsActivity;
import com.mc.phonelive.adapter.foxtone.YinDouAdapter;
import com.mc.phonelive.bean.foxtone.YindouBean;
import com.mc.phonelive.http.HttpCallback;
import com.mc.phonelive.http.HttpUtil;
import com.mc.phonelive.utils.DataSafeUtils;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 熟练度
 */
public class MyShulianduActivity extends AbsActivity {
    @BindView(R.id.yfz_num)
    TextView yfzNum;
    @BindView(R.id.me_yfz_level)
    TextView meYfzLevel;
    @BindView(R.id.Level_bar)
    ProgressBar LevelBar;
    @BindView(R.id.level_scale)
    TextView levelScale;
    @BindView(R.id.yfz_recyclerview)
    RecyclerView yfzRecyclerview;
    @BindView(R.id.rightView)
    TextView rightView;
    @BindView(R.id.no_data)
    TextView noData;
    private String mSkillRule="";//规则
    private String mSkillFee="";//手续费
    private YindouBean.InfoBean mData = new YindouBean.InfoBean();
    private YinDouAdapter mDouListAdapter;

    private int pages = 1;

    @Override
    protected int getLayoutId() {
        return R.layout.shuliandu_layout;
    }

    @Override
    protected void main() {

        getListAdapter(mData.getList());
        showDialog();
        initHttpData();
    }

    private void initHttpData() {

        HttpUtil.getSkillList(pages, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                dismissDialog();
                if (!DataSafeUtils.isEmpty(info) && info.length > 0) {
                    List<YindouBean.InfoBean> data = JSON.parseArray(Arrays.toString(info), YindouBean.InfoBean.class);
                    if (!DataSafeUtils.isEmpty(data.get(0).getProfits())) {
                        getSkillData(data.get(0).getProfits());
                    }
                    if (!DataSafeUtils.isEmpty(data.get(0).getList()) && data.get(0).getList().size() > 0) {
                        if (yfzRecyclerview != null) yfzRecyclerview.setVisibility(View.VISIBLE);
                        if (noData != null) noData.setVisibility(View.GONE);
                        if (mDouListAdapter != null) {
                            mDouListAdapter.setNewData(data.get(0).getList());
                        }
                    } else {
                        if (yfzRecyclerview != null) yfzRecyclerview.setVisibility(View.GONE);
                        if (noData != null) noData.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFinish() {
                dismissDialog();
            }
        });
    }

    private void getSkillData(YindouBean.InfoBean.ProfitsBean data) {
       if (!DataSafeUtils.isEmpty(data.getSkill_rule())){
           mSkillRule =data.getSkill_rule();
       }
       if (!DataSafeUtils.isEmpty(data.getSkill_fee())){
           mSkillFee =data.getSkill_fee();
       }
        if (!DataSafeUtils.isEmpty(data.getTotal()))
            yfzNum.setText(data.getTotal());

        if (!DataSafeUtils.isEmpty(data.getSkill_id()))
            meYfzLevel.setText("Lv." + data.getSkill_id());

        if (!DataSafeUtils.isEmpty(data.getNext_grade()))
            LevelBar.setMax(Integer.parseInt(data.getNext_grade()));

        if (!DataSafeUtils.isEmpty(data.getSkill_id()))
            LevelBar.setProgress(Integer.parseInt(data.getSkill_id()));

        if (!DataSafeUtils.isEmpty(data.getSkill_id()) && !DataSafeUtils.isEmpty(data.getNext_grade()))
            levelScale.setText(data.getSkill_id() + "/" + data.getNext_grade());
    }


    /**
     * 明细
     *
     * @param detalsList
     */
    private void getListAdapter(List<YindouBean.InfoBean.ListBean> detalsList) {
        mDouListAdapter = new YinDouAdapter(R.layout.dou_list_item_view, detalsList);
        yfzRecyclerview.setAdapter(mDouListAdapter);
        yfzRecyclerview.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @OnClick(R.id.rightView)
    public void onViewClicked() {
//        Dialog dialog = new Dialog(this);
//        dialog.setContentView(R.layout.proficiency_dialog_view);
//        dialog.create();dialog.show();

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.proficiency_dialog_view, null);
        TextView proficiency_tv =view.findViewById(R.id.proficiency_tv);
        TextView proficiency_tv1 =view.findViewById(R.id.proficiency_tv1);
        if (!DataSafeUtils.isEmpty(mSkillRule)){
            proficiency_tv.setText(mSkillRule);
        }
        if (!DataSafeUtils.isEmpty(mSkillFee)){
            proficiency_tv1.setText(mSkillFee);
        }

        final Dialog dialog = new Dialog(this, R.style.MyRedPackageDialog3);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(true);
        Window dialogWindow = dialog.getWindow();
        WindowManager m = this.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高度
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
//        p.width = WindowManager.LayoutParams.MATCH_PARENT;
//        p.height = WindowManager.LayoutParams.MATCH_PARENT;
        p.height = (int) (d.getHeight() * 0.80); // 高度设置为屏幕的0.6
        p.width = (int) (d.getWidth() * 0.85); // 宽度设置为屏幕的0.65
        dialogWindow.setAttributes(p);

        dialog.show();
        ImageView proficiency_close = view.findViewById(R.id.proficiency_close);
        proficiency_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null) {
                    dialog.dismiss();
                    dialog.cancel();
                }
            }
        });
    }
}
