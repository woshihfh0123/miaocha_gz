package com.mc.phonelive.bean;

import java.util.List;

public class QbBean {

    /**
     * coin : 194476
     * rules : [{"id":"9","coin":"38","money":"38.00","money_ios":"38.00","product_id":"coin__38","give":"0"},{"id":"10","coin":"88","money":"0.01","money_ios":"0.01","product_id":"coin__888","give":"0"},{"id":"11","coin":"380","money":"380.00","money_ios":"380.00","product_id":"coin__3888","give":"0"},{"id":"12","coin":"588","money":"588.00","money_ios":"588.00","product_id":"coin__5888","give":"0"}]
     * aliapp_switch : 1
     * aliapp_partner : 2088031559005530
     * aliapp_seller_id : 18699926@qq.com
     * aliapp_key_android : MIICXgIBAAKBgQDFZMRqOhK3eHwdecv7EtL4nac+vKJSAMjzVP4mbAVG1qxiUFxShe7aIWbb85aMOopvud4vmcwCeJJkkgnItccopcpOnqpYhHIY6b7pq1K5SYRzTQQKwPnbosRGGDwAclK3L5vkhfwHQH79g055Vz8LoBzZjpkrj+UwQjvrtI/bywIDAQABAoGAOwTdAVHpxLeejIWdKq2/LJyeo9BszA32B2NTNhO4JC0Nj7utTvNCri+sfHlBex4JxEwTlbHrYJXCV5WenUdUi1iNMic9hCcyZWR0MVtkIq0wwXziE0u9Cx0WaioVQZQR6yJoCyzAYlLo8qF1dgmZtRjKCSH/ZMd2akdi2Wcxf7ECQQD6JEr3Rofph7VmXzD4c3WqqM2lZ6kYHYbRYZtTdjx2rNqywatfB6e/8NAwrQHi/TGzqxmRQvQtUHd+zr9pR6XzAkEAygQ6BdnZFnkVzqGVFxV7Lgo2n6+29eXe0S67brlOb9nDm8l3hmj1zlnswJ66yb3xQBun0dne7UWRJCRGikswyQJBAMZnrwZ/bSIwQBPZATCv8+7PQX4mwJqeIOdG8jq7F65R4I3Uy6bunYHC8n8JWuu+RIPr/LWZU6/1mTJ9rplE+T0CQQCl9F7uG45ZBdPCUb82nXD5224QNLtquhIXafqT9SrYe95ThmfoRSVZBUqW2k68GIutjIqKvB6EcNfRBvPj++zxAkEA2w2slztCENZXZMQ5hIhmPpQdGDDx0KYmT5hoJk2GTA6Kw4il+TpDp7hcx59F1WUGg1UGewjg+Fj37QIEOu0KTA==
     * aliapp_key_ios : MIICXgIBAAKBgQDFZMRqOhK3eHwdecv7EtL4nac+vKJSAMjzVP4mbAVG1qxiUFxShe7aIWbb85aMOopvud4vmcwCeJJkkgnItccopcpOnqpYhHIY6b7pq1K5SYRzTQQKwPnbosRGGDwAclK3L5vkhfwHQH79g055Vz8LoBzZjpkrj+UwQjvrtI/bywIDAQABAoGAOwTdAVHpxLeejIWdKq2/LJyeo9BszA32B2NTNhO4JC0Nj7utTvNCri+sfHlBex4JxEwTlbHrYJXCV5WenUdUi1iNMic9hCcyZWR0MVtkIq0wwXziE0u9Cx0WaioVQZQR6yJoCyzAYlLo8qF1dgmZtRjKCSH/ZMd2akdi2Wcxf7ECQQD6JEr3Rofph7VmXzD4c3WqqM2lZ6kYHYbRYZtTdjx2rNqywatfB6e/8NAwrQHi/TGzqxmRQvQtUHd+zr9pR6XzAkEAygQ6BdnZFnkVzqGVFxV7Lgo2n6+29eXe0S67brlOb9nDm8l3hmj1zlnswJ66yb3xQBun0dne7UWRJCRGikswyQJBAMZnrwZ/bSIwQBPZATCv8+7PQX4mwJqeIOdG8jq7F65R4I3Uy6bunYHC8n8JWuu+RIPr/LWZU6/1mTJ9rplE+T0CQQCl9F7uG45ZBdPCUb82nXD5224QNLtquhIXafqT9SrYe95ThmfoRSVZBUqW2k68GIutjIqKvB6EcNfRBvPj++zxAkEA2w2slztCENZXZMQ5hIhmPpQdGDDx0KYmT5hoJk2GTA6Kw4il+TpDp7hcx59F1WUGg1UGewjg+Fj37QIEOu0KTA==
     * wx_switch : 1
     * wx_appid : wx7362e75636eaf966
     * wx_appsecret : e4a746b4d53e6cfa443f4bc17100af98
     * wx_mchid : 1501545291
     * wx_key : jdq834020JISWuf983KSJD932uhr9f23
     */

    private String coin;
    private String aliapp_switch;
    private String votes;
    private String votestotal;
    private String ordernums;
    private String money;
    private String aliapp_partner;
    private String aliapp_seller_id;
    private String aliapp_key_android;
    private String aliapp_key_ios;
    private String wx_switch;
    private String wx_appid;
    private String wx_appsecret;
    private String wx_mchid;
    private String wx_key;
    private List<RulesBean> rules;

    public String getVotes() {
        return votes;
    }

    public void setVotes(String votes) {
        this.votes = votes;
    }

    public String getVotestotal() {
        return votestotal;
    }

    public void setVotestotal(String votestotal) {
        this.votestotal = votestotal;
    }

    public String getOrdernums() {
        return ordernums;
    }

    public void setOrdernums(String ordernums) {
        this.ordernums = ordernums;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getCoin() {
        return coin;
    }

    public void setCoin(String coin) {
        this.coin = coin;
    }

    public String getAliapp_switch() {
        return aliapp_switch;
    }

    public void setAliapp_switch(String aliapp_switch) {
        this.aliapp_switch = aliapp_switch;
    }

    public String getAliapp_partner() {
        return aliapp_partner;
    }

    public void setAliapp_partner(String aliapp_partner) {
        this.aliapp_partner = aliapp_partner;
    }

    public String getAliapp_seller_id() {
        return aliapp_seller_id;
    }

    public void setAliapp_seller_id(String aliapp_seller_id) {
        this.aliapp_seller_id = aliapp_seller_id;
    }

    public String getAliapp_key_android() {
        return aliapp_key_android;
    }

    public void setAliapp_key_android(String aliapp_key_android) {
        this.aliapp_key_android = aliapp_key_android;
    }

    public String getAliapp_key_ios() {
        return aliapp_key_ios;
    }

    public void setAliapp_key_ios(String aliapp_key_ios) {
        this.aliapp_key_ios = aliapp_key_ios;
    }

    public String getWx_switch() {
        return wx_switch;
    }

    public void setWx_switch(String wx_switch) {
        this.wx_switch = wx_switch;
    }

    public String getWx_appid() {
        return wx_appid;
    }

    public void setWx_appid(String wx_appid) {
        this.wx_appid = wx_appid;
    }

    public String getWx_appsecret() {
        return wx_appsecret;
    }

    public void setWx_appsecret(String wx_appsecret) {
        this.wx_appsecret = wx_appsecret;
    }

    public String getWx_mchid() {
        return wx_mchid;
    }

    public void setWx_mchid(String wx_mchid) {
        this.wx_mchid = wx_mchid;
    }

    public String getWx_key() {
        return wx_key;
    }

    public void setWx_key(String wx_key) {
        this.wx_key = wx_key;
    }

    public List<RulesBean> getRules() {
        return rules;
    }

    public void setRules(List<RulesBean> rules) {
        this.rules = rules;
    }

    public static class RulesBean extends CoinBean {
        /**
         * id : 9
         * coin : 38
         * money : 38.00
         * money_ios : 38.00
         * product_id : coin__38
         * give : 0
         */

        private String id;
        private String coin;
        private String money;
        private String money_ios;
        private String product_id;
        private String give;
        private boolean isChecked;

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCoin() {
            return coin;
        }

        public void setCoin(String coin) {
            this.coin = coin;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getMoney_ios() {
            return money_ios;
        }

        public void setMoney_ios(String money_ios) {
            this.money_ios = money_ios;
        }

        public String getProduct_id() {
            return product_id;
        }

        public void setProduct_id(String product_id) {
            this.product_id = product_id;
        }

        public String getGive() {
            return give;
        }

        public void setGive(String give) {
            this.give = give;
        }
    }
}
