package com.mc.phonelive.bean.foxtone;

import java.util.List;

/**
 * 申请代理
 */
public class CityBean01 {
    private List<InfoBean> info;

    public List<InfoBean> getInfo() {
        return info;
    }

    public void setInfo(List<InfoBean> info) {
        this.info = info;
    }

    public static class InfoBean {
        /**
         * name : 北京
         * code : 110000
         * cityList : [{"name":"北京市","code":"110100","areaList":[{"name":"东城区","code":"110101"},{"name":"西城区","code":"110102"},{"name":"朝阳区","code":"110105"},{"name":"丰台区","code":"110106"},{"name":"石景山区","code":"110107"},{"name":"海淀区","code":"110108"},{"name":"门头沟区","code":"110109"},{"name":"房山区","code":"110111"},{"name":"通州区","code":"110112"},{"name":"顺义区","code":"110113"},{"name":"昌平区","code":"110114"},{"name":"大兴区","code":"110115"},{"name":"怀柔区","code":"110116"},{"name":"平谷区","code":"110117"},{"name":"密云县","code":"110228"},{"name":"延庆县","code":"110229"}]}]
         */

        private String name;
        private String code;
        private List<CityListBean> cityList;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public List<CityListBean> getCityList() {
            return cityList;
        }

        public void setCityList(List<CityListBean> cityList) {
            this.cityList = cityList;
        }

        public static class CityListBean {
            /**
             * name : 北京市
             * code : 110100
             * areaList : [{"name":"东城区","code":"110101"},{"name":"西城区","code":"110102"},{"name":"朝阳区","code":"110105"},{"name":"丰台区","code":"110106"},{"name":"石景山区","code":"110107"},{"name":"海淀区","code":"110108"},{"name":"门头沟区","code":"110109"},{"name":"房山区","code":"110111"},{"name":"通州区","code":"110112"},{"name":"顺义区","code":"110113"},{"name":"昌平区","code":"110114"},{"name":"大兴区","code":"110115"},{"name":"怀柔区","code":"110116"},{"name":"平谷区","code":"110117"},{"name":"密云县","code":"110228"},{"name":"延庆县","code":"110229"}]
             */

            private String name;
            private String code;
            private List<AreaListBean> areaList;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }

            public List<AreaListBean> getAreaList() {
                return areaList;
            }

            public void setAreaList(List<AreaListBean> areaList) {
                this.areaList = areaList;
            }

            public static class AreaListBean {
                /**
                 * name : 东城区
                 * code : 110101
                 */

                private String name;
                private String code;

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getCode() {
                    return code;
                }

                public void setCode(String code) {
                    this.code = code;
                }
            }
        }
    }
}
