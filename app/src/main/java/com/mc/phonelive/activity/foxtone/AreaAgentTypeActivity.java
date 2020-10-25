package com.mc.phonelive.activity.foxtone;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mc.phonelive.R;
import com.mc.phonelive.activity.AbsActivity;
import com.mc.phonelive.utils.DataSafeUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 合伙招募申请状态
 */
public class AreaAgentTypeActivity extends AbsActivity {
    @BindView(R.id.type_img)
    ImageView typeImg;
    @BindView(R.id.type_title)
    TextView typeTitle;
    @BindView(R.id.type_detail)
    TextView typeDetail;
    @BindView(R.id.type_btn)
    TextView typeBtn;

    public static void forward(Context context, String type) {
        Intent intent = new Intent(context, AreaAgentTypeActivity.class);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.agentapply_type_view;
    }

    @Override
    protected void main() {
        setTitle("合伙招募");

        String type = this.getIntent().getStringExtra("type");
        if (!DataSafeUtils.isEmpty(type)) {
            if (type.equals("1")) {
                Glide.with(mContext).load(R.mipmap.area_apply_success).into(typeImg);
                typeTitle.setText("申请审核成功");
                typeDetail.setText("恭喜您，成为城市代理商");
                typeBtn.setVisibility(View.GONE);
            } else if (type.equals("2")) {
                Glide.with(mContext).load(R.mipmap.area_apply_review).into(typeImg);
                typeTitle.setText("审核中");
                typeDetail.setText("您的申请已提交审核，请耐心等待");
                typeBtn.setVisibility(View.GONE);
            } else if (type.equals("3")) {
                Glide.with(mContext).load(R.mipmap.area_apply_error).into(typeImg);
                typeTitle.setText("审核失败");
                typeDetail.setText("您提交的申请未通过审核 未通过审核原因：\n1.信息不正确，2.您的级别不够");
                typeBtn.setVisibility(View.VISIBLE);
            }
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.type_btn)
    public void onViewClicked() {
        mContext.startActivity(new Intent(mContext, AgentApplyActivity.class));
    }
}
