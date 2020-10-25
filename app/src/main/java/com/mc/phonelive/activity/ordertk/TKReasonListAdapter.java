package com.mc.phonelive.activity.ordertk;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.mc.phonelive.R;
import com.mc.phonelive.bean.RefundORderBean;

import java.util.List;

public class TKReasonListAdapter extends BaseAdapter {

    private List<RefundORderBean.InfoBean.RefundReasonListBean> list;
    private Context context;
    private LayoutInflater mInflater;


    public TKReasonListAdapter(Context context, List<RefundORderBean.InfoBean.RefundReasonListBean> mlist) {
        this.context = context;
        this.mInflater = LayoutInflater.from(this.context);
        this.list = mlist;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        if (list != null)
            return list.size();
        else
            return 0;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        if (list != null)
            return list.get(position);
        else
            return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        if (list != null)
            return position;
        else
            return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.activity_tk_reason_item,
                    null);
            holder = new Holder();
            holder.detail_project1_tv = (TextView) convertView
                    .findViewById(R.id.reason_val);


            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        holder.detail_project1_tv.setText(list.get(position).getTitle());
        return convertView;
    }

    class Holder {
        TextView detail_project1_tv; // 项目1

    }

    // 退出
    public void exit() {
        list = null;
        notifyDataSetChanged();
        mInflater = null;
        context = null;
    }

}
