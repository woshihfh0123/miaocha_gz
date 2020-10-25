package com.mc.phonelive.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 代理级别
 * created by WWL on 2020/4/15 0015:09
 */
public class AgentLevelBean extends BaseVO implements Serializable {

    public static AgentLevelBean.InfoBean getLeveData(){
        InfoBean bean = new InfoBean();
        List<InfoBean.LevelsBean> levelsBeans = new ArrayList<>();
        String[] names={"成单","合伙人","初级队长","区县代理","中级队长","市代理","高级队长"};
        String[] nums={"0","10","30","50","200","500","1000"};
        String[] nums1={"10","20","150","300","500","1000","2000"};
        String[] progress={"10","8","0","0","0","0","0"};
        for (int i=0;i<names.length;i++){
            InfoBean.LevelsBean lev = new InfoBean.LevelsBean();
            lev.setId(i+"");
            lev.setName(names[i]);
            lev.setNum(nums[i]);
            lev.setMaxprogress(nums1[i]);
            lev.setProgress(progress[i]);
            if (i<2){
                lev.setStatus("1");
            }else {
                lev.setStatus("0");
            }
            levelsBeans.add(lev);
        }
        bean.setLevelnum1("18");
        bean.setLevelnum2("12");
        bean.setUser_level("初级队长");
        bean.setLevelist(levelsBeans);
        return bean;
    }




    private InfoBean info;

    public InfoBean getInfo() {
        return info;
    }

    public void setInfo(InfoBean info) {
        this.info = info;
    }

    public static class InfoBean{
        private String username;
        private String user_level;
        private String level_img;
        private String levelnum1;
        private String levelnum2;

        private List<LevelsBean> levelist;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getUser_level() {
            return user_level;
        }

        public void setUser_level(String user_level) {
            this.user_level = user_level;
        }

        public String getLevel_img() {
            return level_img;
        }

        public void setLevel_img(String level_img) {
            this.level_img = level_img;
        }

        public String getLevelnum1() {
            return levelnum1;
        }

        public void setLevelnum1(String levelnum1) {
            this.levelnum1 = levelnum1;
        }

        public String getLevelnum2() {
            return levelnum2;
        }

        public void setLevelnum2(String levelnum2) {
            this.levelnum2 = levelnum2;
        }

        public List<LevelsBean> getLevelist() {
            return levelist;
        }

        public void setLevelist(List<LevelsBean> levelist) {
            this.levelist = levelist;
        }

        public static class LevelsBean{
            private String id;
            private String name;
            private String num;
            private String status;
            private String maxprogress;
            private String progress;

            public String getMaxprogress() {
                return maxprogress;
            }

            public void setMaxprogress(String maxprogress) {
                this.maxprogress = maxprogress;
            }

            public String getProgress() {
                return progress;
            }

            public void setProgress(String progress) {
                this.progress = progress;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getNum() {
                return num;
            }

            public void setNum(String num) {
                this.num = num;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }
        }
    }
}
