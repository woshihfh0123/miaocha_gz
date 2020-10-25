package com.mc.phonelive.bean.foxtone;

import com.mc.phonelive.bean.BaseVO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 我的团队
 */
public class MyTeamBean extends BaseVO implements Serializable {

    public static MyTeamBean.InfoBean getTeamData() {
        MyTeamBean.InfoBean info = new MyTeamBean.InfoBean();
        List<MyTeamBean.InfoBean.TeamList> mtasklist = new ArrayList<>();


        for (int i = 0; i < 20; i++) {
            MyTeamBean.InfoBean.TeamList data = new MyTeamBean.InfoBean.TeamList();
            data.setId(i + "");
            data.setAvatar("https://dss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=3374416169,262924133&fm=111&gp=0.jpg");
            data.setMobile("1554877552" + i);
            data.setGrade_id("" + i);
            data.setFees("3" + i);
            mtasklist.add(data);
        }
        info.setTeam_list(mtasklist);

        return info;
    }


    private InfoBean info;

    public InfoBean getInfo() {
        return info;
    }

    public void setInfo(InfoBean info) {
        this.info = info;
    }

    public static class InfoBean {
        private TeamHeadBean data;
        private List<TeamList> team_list;

        public TeamHeadBean getData() {
            return data;
        }

        public void setData(TeamHeadBean data) {
            this.data = data;
        }

        public List<TeamList> getTeam_list() {
            return team_list;
        }

        public void setTeam_list(List<TeamList> team_list) {
            this.team_list = team_list;
        }

        public static class TeamHeadBean{
            private List<AgentBean> agent;
            private String team_fee;
            private String team_counts;
            private String area_fee;
            private String auth_fee;

            public List<AgentBean> getAgent() {
                return agent;
            }

            public void setAgent(List<AgentBean> agent) {
                this.agent = agent;
            }

            public String getTeam_fee() {
                return team_fee;
            }

            public void setTeam_fee(String team_fee) {
                this.team_fee = team_fee;
            }

            public String getTeam_counts() {
                return team_counts;
            }

            public void setTeam_counts(String team_counts) {
                this.team_counts = team_counts;
            }

            public String getArea_fee() {
                return area_fee;
            }

            public void setArea_fee(String area_fee) {
                this.area_fee = area_fee;
            }

            public String getAuth_fee() {
                return auth_fee;
            }

            public void setAuth_fee(String auth_fee) {
                this.auth_fee = auth_fee;
            }

            public static class AgentBean{
                private String id;
                private String avatar;
                private String user_nicename;

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getAvatar() {
                    return avatar;
                }

                public void setAvatar(String avatar) {
                    this.avatar = avatar;
                }

                public String getUser_nicename() {
                    return user_nicename;
                }

                public void setUser_nicename(String user_nicename) {
                    this.user_nicename = user_nicename;
                }
            }
        }

        public static class TeamList {
            private String id;
            private String avatar;
            private String user_nicename;
            private String mobile;
            private String grade_id;
            private String fees;
            private String create_time;

            public String getCreate_time() {
                return create_time;
            }

            public void setCreate_time(String create_time) {
                this.create_time = create_time;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public String getUser_nicename() {
                return user_nicename;
            }

            public void setUser_nicename(String user_nicename) {
                this.user_nicename = user_nicename;
            }

            public String getMobile() {
                return mobile;
            }

            public void setMobile(String mobile) {
                this.mobile = mobile;
            }

            public String getGrade_id() {
                return grade_id;
            }

            public void setGrade_id(String grade_id) {
                this.grade_id = grade_id;
            }

            public String getFees() {
                return fees;
            }

            public void setFees(String fees) {
                this.fees = fees;
            }
        }
    }
}
