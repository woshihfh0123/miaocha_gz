package com.mc.phonelive.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.RelativeLayout;

import com.mc.phonelive.R;
import com.mc.phonelive.adapter.ContinentModel;
import com.mc.phonelive.adapter.IndexZhouTextAdapter;
import com.mc.phonelive.utils.EventBean;
import com.mc.phonelive.utils.EventBusUtil;
import com.mc.phonelive.views.LocateCenterHorizontalView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

/**
 *
 * 直播拍摄界面999999
 */

public class MainPsAndZb extends AbsActivity {

    private RecyclerView rv;
    private RelativeLayout rl_left;
    private RelativeLayout rl_right;
    private LocateCenterHorizontalView zhouText;
    private ArrayList<ContinentModel> list;

    @Override
    protected int getLayoutId() {

        return R.layout.activity_main_ps_zb;
    }

    @Override
    protected boolean isStatusBarWhite() {
        return true;
    }

    @Override
    protected void main() {
        rv=findViewById(R.id.rv);
        rl_left=findViewById(R.id.rl_left);
        zhouText = findViewById(R.id.zhouText);
        initData();
        initZhouText();

    }

    private void initZhouText() {
        zhouText.setHasFixedSize(true);
        zhouText.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        IndexZhouTextAdapter zhouTextAdapter = new IndexZhouTextAdapter(this, list, 1);
        zhouText.setInitPos(2);
        zhouText.setAdapter(zhouTextAdapter);
        zhouText.setOnSelectedPositionChangedListener(new LocateCenterHorizontalView.OnSelectedPositionChangedListener() {
            @Override
            public void selectedPositionChanged(int pos) {
                changeView(pos);
                Log.e("pppp:",pos+"");
            }
        });

    }

    private void changeView(int pos) {
        EventBusUtil.postEvent(new EventBean("send_position_from_change_view",pos+""));
//        if(pos==3){
//            rl_right.setVisibility(View.VISIBLE);
//            rl_left.setVisibility(View.GONE);
//        }else{
//            rl_right.setVisibility(View.GONE);
//            rl_left.setVisibility(View.VISIBLE);
//
//        }
    }


    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getEvent(EventBean event){
        switch(event.getCode()){
            case "send_posi_from_click":
                String pos= (String) event.getFirstObject();
                zhouText.moveToPosition(Integer.parseInt(pos));
                changeView(Integer.parseInt(pos));
                break;
            case "close_act_from_go_live":
                    finish();
                break;
            default:

                break;
        }
    };

    private void initData() {
        list = new ArrayList<>();
        list.add(new ContinentModel(0, "拍照", "1"));
        list.add(new ContinentModel(1, "拍120秒", "2"));
        list.add(new ContinentModel(2, "拍30秒", "3"));
        list.add(new ContinentModel(3, "开直播", "4"));
    }

}
