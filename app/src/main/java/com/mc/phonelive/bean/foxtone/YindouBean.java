package com.mc.phonelive.bean.foxtone;

import com.mc.phonelive.bean.BaseVO;

import java.util.List;

/**
 * 我的音豆
 */
public class YindouBean extends BaseVO {

    private InfoBean info;

    public InfoBean getInfo() {
        return info;
    }

    public void setInfo(InfoBean info) {
        this.info = info;
    }

    public static class InfoBean {
        private ProfitsBean profits;
        private List<ListBean> list;

        public ProfitsBean getProfits() {
            return profits;
        }

        public void setProfits(ProfitsBean profits) {
            this.profits = profits;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }
        public static class ProfitsBean{
            /**
             * total : 0
             * fee : 0.00
             * freeze_fee : 0.00
             * web_url : http://huyin.yanshi.hbbeisheng.com/index.php?g=portal&m=page&a=index&id=24
             */

            private String total;
            private String fee;
            private String freeze_fee;
            private String web_url;

            //--下面几个是音分值字段
            private String grade_id;
            private String next_grade;

            //--下面几个是熟练度字段
            private String skill_id;
            private String skill_rule;//规则
            private String skill_fee;//手续费

            public String getSkill_id() {
                return skill_id;
            }

            public void setSkill_id(String skill_id) {
                this.skill_id = skill_id;
            }

            public String getSkill_rule() {
                return skill_rule;
            }

            public void setSkill_rule(String skill_rule) {
                this.skill_rule = skill_rule;
            }

            public String getSkill_fee() {
                return skill_fee;
            }

            public void setSkill_fee(String skill_fee) {
                this.skill_fee = skill_fee;
            }

            public String getGrade_id() {
                return grade_id;
            }

            public void setGrade_id(String grade_id) {
                this.grade_id = grade_id;
            }

            public String getNext_grade() {
                return next_grade;
            }

            public void setNext_grade(String next_grade) {
                this.next_grade = next_grade;
            }

            public String getTotal() {
                return total;
            }

            public void setTotal(String total) {
                this.total = total;
            }

            public String getFee() {
                return fee;
            }

            public void setFee(String fee) {
                this.fee = fee;
            }

            public String getFreeze_fee() {
                return freeze_fee;
            }

            public void setFreeze_fee(String freeze_fee) {
                this.freeze_fee = freeze_fee;
            }

            public String getWeb_url() {
                return web_url;
            }

            public void setWeb_url(String web_url) {
                this.web_url = web_url;
            }
        }
        public static class ListBean{
            private String id;
            private String changes;//变动数量
            private String note;//描述
            private String create_time;
            private String type;//类型(1 表示'+';2 表示'-')

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getChanges() {
                return changes;
            }

            public void setChanges(String changes) {
                this.changes = changes;
            }

            public String getNote() {
                return note;
            }

            public void setNote(String note) {
                this.note = note;
            }

            public String getCreate_time() {
                return create_time;
            }

            public void setCreate_time(String create_time) {
                this.create_time = create_time;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }
        }
    }
}
