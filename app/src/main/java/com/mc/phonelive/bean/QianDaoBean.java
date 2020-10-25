package com.mc.phonelive.bean;

import java.util.List;

public class QianDaoBean {

    /**
     * bonus_switch : 1
     * bonus_day : 0
     * count_day : 1
     * is_bonus : 1
     * bonus_list : [{"day":"1","coin":"2"},{"day":"2","coin":"3"},{"day":"3","coin":"4"},{"day":"4","coin":"5"},{"day":"5","coin":"6"},{"day":"6","coin":"7"},{"day":"7","coin":"8"}]
     */

    private String bonus_switch;
    private String bonus_day;
    private String count_day;
    private int is_bonus;
    private List<BonusListBean> bonus_list;

    public String getBonus_switch() {
        return bonus_switch;
    }

    public void setBonus_switch(String bonus_switch) {
        this.bonus_switch = bonus_switch;
    }

    public String getBonus_day() {
        return bonus_day;
    }

    public void setBonus_day(String bonus_day) {
        this.bonus_day = bonus_day;
    }

    public String getCount_day() {
        return count_day;
    }

    public void setCount_day(String count_day) {
        this.count_day = count_day;
    }

    public int getIs_bonus() {
        return is_bonus;
    }

    public void setIs_bonus(int is_bonus) {
        this.is_bonus = is_bonus;
    }

    public List<BonusListBean> getBonus_list() {
        return bonus_list;
    }

    public void setBonus_list(List<BonusListBean> bonus_list) {
        this.bonus_list = bonus_list;
    }

    public static class BonusListBean {
        /**
         * day : 1
         * coin : 2
         */

        private String day;
        private String coin;

        public String getDay() {
            return day;
        }

        public void setDay(String day) {
            this.day = day;
        }

        public String getCoin() {
            return coin;
        }

        public void setCoin(String coin) {
            this.coin = coin;
        }
    }
}
