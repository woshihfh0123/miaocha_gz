package com.mc.phonelive.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.bspopupwindow.utils.Utils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mc.phonelive.R;
import com.mc.phonelive.bean.GetGoodsAttrBean;
import com.mc.phonelive.utils.EventBean;
import com.mc.phonelive.utils.EventBusUtil;
import com.orhanobut.logger.Logger;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/8/7.
 */

public class TypeSelectorAdapter extends BaseQuickAdapter<GetGoodsAttrBean, BaseViewHolder> {

    public TypeSelectorAdapter() {
        super(R.layout.item_type_selector);
    }

    @Override
    protected void convert(BaseViewHolder helper, final GetGoodsAttrBean item) {
        if(Utils.isNotEmpty(item)){
            TextView tv_text=helper.getView(R.id.tv_text);
            tv_text.setText(item.getAttr_name());
            TagFlowLayout mFlowlayout=helper.getView(R.id.flowlayout);
            List<String> listValues = item.getAttr_value();
            List<GetGoodsAttrBean.DiyAttrValue> nlist = new ArrayList<>();
            List<GetGoodsAttrBean.DiyAttrValue> tagList = item.getAttrs();
            TagAdapter adapter = new TagAdapter(tagList) {
                @Override
                public void onSelected(int position, View view) {
                    super.onSelected(position, view);
                    for(int i=0;i<tagList.size();i++){
                        if(i==position){
                            if(tagList.get(i).getCheck().equals("false")){
                                tagList.get(position).setCheck("true");
                            }else{
                                tagList.get(position).setCheck("false");
                            }
                        }else{
                            tagList.get(i).setCheck("false");
                        }
                    }
//                    setSelected(position,true);
                    notifyDataChanged();
                    EventBusUtil.postEvent(new EventBean("send_pop_info_from_tag",position+""));
                    Logger.e("sssss:"+tagList.get(position).getCheck());
                }
                @Override
                public void unSelected(int position, View view) {
                    super.unSelected(position, view);
//                    tagList.get(position).setCheck("0");
//                    setSelected(position,false);
//                    notifyDataChanged();
                }

                @Override
                public View getView(FlowLayout parent, int position, Object o) {
                    GetGoodsAttrBean.DiyAttrValue hob = (GetGoodsAttrBean.DiyAttrValue) o;
                    TextView tv_name = (TextView) LayoutInflater.from(mContext).inflate(R.layout.flowlayout_selector_type,
                            mFlowlayout, false);
                    tv_name.setText(hob.getAttr());
                        String isHob =hob.getCheck();
                        if(isHob.equals("true")){
                            tv_name.setBackgroundColor(Color.parseColor("#fff6e9"));
                            tv_name.setTextColor(mContext.getResources().getColor(R.color.yellow1));
                        }else{
                            tv_name.setBackgroundColor(mContext.getResources().getColor(R.color.gameGray));
                            tv_name.setTextColor(mContext.getResources().getColor(R.color.black));
                        }
                    return tv_name;
                }
            };
            mFlowlayout.setAdapter(adapter);
        }


    }
}
