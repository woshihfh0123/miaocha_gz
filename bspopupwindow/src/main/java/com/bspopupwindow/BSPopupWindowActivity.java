package com.bspopupwindow;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bspopupwindow.model.CareeVO;
import com.bspopupwindow.model.TreeVO;
import com.bspopupwindow.model.getDemoData;
import com.bspopupwindow.utils.Utils;
import com.bspopupwindow.view.BSPopupWindowsTitle;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static com.bspopupwindow.R.id.tv01;

/**
 * created by WWL on 2019/8/17 0017:11
 */
public class BSPopupWindowActivity extends AppCompatActivity {
    BSPopupWindowsTitle mBsPopupWindowsTitle;
    TextView mTv_px,tv02,tv03;
    View mDivider;
    private List<CareeVO.DataBean> careeList = new ArrayList<>();
    private String mParentId="";
    private String mChildId="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_activity);
        mTv_px =(TextView)findViewById(R.id.tv01);
        tv02 =findViewById(R.id.tv02);
        tv03 =findViewById(R.id.tv03);
        mDivider =findViewById(R.id.lines);
        mTv_px.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //一级菜单
                mBsPopupWindowsTitle = new BSPopupWindowsTitle(BSPopupWindowActivity.this, initPopDowns(), callback,500);
                mBsPopupWindowsTitle.showAsDropDown(mDivider);

            }
        });
        tv02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //二级菜单
                mBsPopupWindowsTitle = new BSPopupWindowsTitle(BSPopupWindowActivity.this, initPopDowns(), callback);
                mBsPopupWindowsTitle.showAsDropDown(mDivider);
            }
        });

    }

    BSPopupWindowsTitle.TreeCallBack callback = new BSPopupWindowsTitle.TreeCallBack() {

        @Override
        public void callback(TreeVO vo) {
            if (vo.getLevel() == 1) {
                // 审批一级菜单
                if(Utils.isNotEmpty(vo)&&Utils.isNotEmpty(vo.getId())){
                    mParentId =vo.getId()+"";
                    if(vo.getId()==-1){//全部
                        mTv_px.setText("选择职位");
                    }else{
                        mTv_px.setText(vo.getName());
                    }
                }
            } else {
                mParentId=vo.getParentId()+"";
                mChildId = vo.getId()+"";
                // 审批二级菜单
                mTv_px.setText(vo.getName());
            }
        }
    };


    private ArrayList<TreeVO> initPopDowns() {
        careeList = getDemoData.getData();
        ArrayList<TreeVO> list = new ArrayList<TreeVO>();
        TreeVO allTreeVo = new TreeVO();
        allTreeVo.setId(-1);
        allTreeVo.setParentId(0);
        allTreeVo.setName("全部");
        allTreeVo.setLevel(1);
        allTreeVo.setHaschild(false);
        allTreeVo.setDepartmentid("-1");
        allTreeVo.setDname("全部");
        list.add(allTreeVo);
        if(Utils.isNotEmpty(careeList)){
            for (int i = 0; i < careeList.size(); i++) {
                TreeVO parentVo = new TreeVO();
                parentVo.setId(Integer.parseInt(careeList.get(i).getId()));
                parentVo.setParentId(0);
                parentVo.setName(careeList.get(i).getTitle());
                parentVo.setParentSerachId(i+"");
                parentVo.setLevel(1);

                list.add(parentVo);
                List<CareeVO.DataBean.ListBean> housePriceList = careeList.get(i).getList();
                if(Utils.isNotEmpty(housePriceList)){
                    parentVo.setHaschild(true);
                    for (int j = 0; j < housePriceList.size(); j++) {
                        TreeVO childVo = new TreeVO();
                        childVo.setId(Integer.parseInt(housePriceList.get(j).getId()));
                        childVo.setParentId(Integer.parseInt(careeList.get(i).getId()));
                        childVo.setName(housePriceList.get(j).getTitle());
                        childVo.setChildSearchId(""+j);
                        childVo.setParentSerachId("A"+j);
                        childVo.setLevel(2);
                        list.add(childVo);

                    }
                }else{
                    parentVo.setHaschild(false);
                }

            }
        }

        return list;
    }

}
