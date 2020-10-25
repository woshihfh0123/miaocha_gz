package com.mc.phonelive.bean.foxtone;

import com.mc.phonelive.bean.BaseVO;

import java.util.List;

/**
 * 任务大厅
 */
public class MusicCenterBean extends BaseVO {


    //    public static MusicCenterBean.InfoBean getData() {
//        MusicCenterBean.InfoBean info = new MusicCenterBean.InfoBean();
//        List<MusicCenterBean.InfoBean.ListBean> mList = new ArrayList<>();
//        List<MusicCenterBean.InfoBean.TaskBean> mtasklist = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            MusicCenterBean.InfoBean.ListBean data = new MusicCenterBean.InfoBean.ListBean();
//            data.setCount(i + "");
//            data.setCycle("140天");
//            data.setId(i + "");
//            data.setName("乐器" + i);
//            data.setNum("10音豆");
//            data.setPrice("9音豆");
//            if (i < 4) {
//                data.setStatus(1 + "");
//            } else {
//                data.setStatus(0 + "");
//            }
//            ;
//            data.setUp_count("2");
//            data.setYin_count("4");
//            mList.add(data);
//        }
//        info.setList(mList);
//
//        for (int i = 0; i < 10; i++) {
//            MusicCenterBean.InfoBean.TaskBean data = new InfoBean.TaskBean();
//            data.setId(i+"");data.setImg("");data.setStatus("1");data.setTitle("每日观看视频（0/5）");
//            data.setCoount03("熟练度+50"); data.setCount01("音豆+0.3");data.setCount02("音分值+10");
//            mtasklist.add(data);
//        }
//        info.setTasklist(mtasklist);
//
//        return info;
//    }
//
    private InfoBean info;

    public InfoBean getInfo() {
        return info;
    }

    public void setInfo(InfoBean info) {
        this.info = info;
    }

    public static class InfoBean {
        /**
         * list : [{"id":"1","name":"长笛","thumb":"http://huyin.yanshi.hbbeisheng.com/data/upload/20200506/5eb27c6480686.png","fee":"8","output":"9","days":"30","upper":"7","cent":"1","unfreeze":"0","status":"1","status_name":"立即购买","counts":"0"},{"id":"2","name":"琵琶","thumb":"","fee":"100","output":"130","days":"40","upper":"5","cent":"10","unfreeze":"0","status":"1","status_name":"立即购买","counts":"0"}]
         * now_list : {"now_list":[],"common_list":[],"extra_list":[{"id":"3","title":"每日观看视频","icon":"","type":"2","nums":"5","is_complete":"1","is_complete_name":"去完成","complete_nums":"0"}]}
         * old_list : []
         */

        private NowListBean now_list;
        private List<ListBean> list;
        private List<ListBean> old_list;

        public NowListBean getNow_list() {
            return now_list;
        }

        public void setNow_list(NowListBean now_list) {
            this.now_list = now_list;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public List<ListBean> getOld_list() {
            return old_list;
        }

        public void setOld_list(List<ListBean> old_list) {
            this.old_list = old_list;
        }

        public static class NowListBean {
            private List<ListBean> now_list;
            private List<ExtraListBean> common_list;
            private List<ExtraListBean> extra_list;

            public List<ListBean> getNow_list() {
                return now_list;
            }

            public void setNow_list(List<ListBean> now_list) {
                this.now_list = now_list;
            }

            public List<ExtraListBean> getCommon_list() {
                return common_list;
            }

            public void setCommon_list(List<ExtraListBean> common_list) {
                this.common_list = common_list;
            }

            public List<ExtraListBean> getExtra_list() {
                return extra_list;
            }

            public void setExtra_list(List<ExtraListBean> extra_list) {
                this.extra_list = extra_list;
            }

            public static class ExtraListBean {
                /**
                 * id : 3
                 * title : 每日观看视频
                 * icon :
                 * type : 2
                 * nums : 5
                 * is_complete : 1
                 * is_complete_name : 去完成
                 * complete_nums : 0
                 */

                private String id;
                private String title;
                private String icon;
                private String type;
                private String nums;
                private String is_complete;//1去完成  2去领取 3 已领取
                private String is_complete_name;
                private String complete_nums;

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }

                public String getIcon() {
                    return icon;
                }

                public void setIcon(String icon) {
                    this.icon = icon;
                }

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }

                public String getNums() {
                    return nums;
                }

                public void setNums(String nums) {
                    this.nums = nums;
                }

                public String getIs_complete() {
                    return is_complete;
                }

                public void setIs_complete(String is_complete) {
                    this.is_complete = is_complete;
                }

                public String getIs_complete_name() {
                    return is_complete_name;
                }

                public void setIs_complete_name(String is_complete_name) {
                    this.is_complete_name = is_complete_name;
                }

                public String getComplete_nums() {
                    return complete_nums;
                }

                public void setComplete_nums(String complete_nums) {
                    this.complete_nums = complete_nums;
                }
            }
        }




        public static class ListBean {
            /**
             * id : 1
             * name : 长笛
             * thumb : http://huyin.yanshi.hbbeisheng.com/data/upload/20200506/5eb27c6480686.png
             * fee : 8
             * output : 9
             * days : 30
             * upper : 7
             * cent : 1
             * unfreeze : 0
             * status : 1
             * status_name : 立即购买
             * counts : 0
             */

            private String id;
            private String name;
            private String thumb;
            private String fee;//价格
            private String output;//预估产值
            private String days;//周期
            private String upper;//领取上线
            private String cent;//音分值
            private String unfreeze;//解冻
            private String status;//状态(1 表示'立即购买';2 表示'预留不开')
            private String status_name;//状态名称
            private String counts;//当前拥有
            //下面几个是now_list中的
            private String create_time;//创建时间
            private String surplus_days;//剩余天数
            private String surplus_time;//到期时间

            public String getCreate_time() {
                return create_time;
            }

            public void setCreate_time(String create_time) {
                this.create_time = create_time;
            }

            public String getSurplus_days() {
                return surplus_days;
            }

            public void setSurplus_days(String surplus_days) {
                this.surplus_days = surplus_days;
            }

            public String getSurplus_time() {
                return surplus_time;
            }

            public void setSurplus_time(String surplus_time) {
                this.surplus_time = surplus_time;
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

            public String getThumb() {
                return thumb;
            }

            public void setThumb(String thumb) {
                this.thumb = thumb;
            }

            public String getFee() {
                return fee;
            }

            public void setFee(String fee) {
                this.fee = fee;
            }

            public String getOutput() {
                return output;
            }

            public void setOutput(String output) {
                this.output = output;
            }

            public String getDays() {
                return days;
            }

            public void setDays(String days) {
                this.days = days;
            }

            public String getUpper() {
                return upper;
            }

            public void setUpper(String upper) {
                this.upper = upper;
            }

            public String getCent() {
                return cent;
            }

            public void setCent(String cent) {
                this.cent = cent;
            }

            public String getUnfreeze() {
                return unfreeze;
            }

            public void setUnfreeze(String unfreeze) {
                this.unfreeze = unfreeze;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getStatus_name() {
                return status_name;
            }

            public void setStatus_name(String status_name) {
                this.status_name = status_name;
            }

            public String getCounts() {
                return counts;
            }

            public void setCounts(String counts) {
                this.counts = counts;
            }
        }
    }


//        private List<ListBean> list;
//        private List<TaskBean> tasklist;
//
//        public List<TaskBean> getTasklist() {
//            return tasklist;
//        }
//
//        public void setTasklist(List<TaskBean> tasklist) {
//            this.tasklist = tasklist;
//        }
//
//        public List<ListBean> getList() {
//            return list;
//        }
//
//        public void setList(List<ListBean> list) {
//            this.list = list;
//        }
//
//        public static class ListBean {
//            private String id;
//            private String name;
//            private String price;
//            private String num;
//            private String cycle;
//            private String count;
//            private String yin_count;
//            private String up_count;
//            private String status;
//
//            public String getId() {
//                return id;
//            }
//
//            public void setId(String id) {
//                this.id = id;
//            }
//
//            public String getName() {
//                return name;
//            }
//
//            public void setName(String name) {
//                this.name = name;
//            }
//
//            public String getPrice() {
//                return price;
//            }
//
//            public void setPrice(String price) {
//                this.price = price;
//            }
//
//            public String getNum() {
//                return num;
//            }
//
//            public void setNum(String num) {
//                this.num = num;
//            }
//
//            public String getCycle() {
//                return cycle;
//            }
//
//            public void setCycle(String cycle) {
//                this.cycle = cycle;
//            }
//
//            public String getCount() {
//                return count;
//            }
//
//            public void setCount(String count) {
//                this.count = count;
//            }
//
//            public String getYin_count() {
//                return yin_count;
//            }
//
//            public void setYin_count(String yin_count) {
//                this.yin_count = yin_count;
//            }
//
//            public String getUp_count() {
//                return up_count;
//            }
//
//            public void setUp_count(String up_count) {
//                this.up_count = up_count;
//            }
//
//            public String getStatus() {
//                return status;
//            }
//
//            public void setStatus(String status) {
//                this.status = status;
//            }
//        }
//
//        public static class TaskBean{
//            private String id;
//            private String img;
//            private String title;
//            private String count01;
//            private String count02;
//            private String coount03;
//            private String status;
//
//            public String getId() {
//                return id;
//            }
//
//            public void setId(String id) {
//                this.id = id;
//            }
//
//            public String getImg() {
//                return img;
//            }
//
//            public void setImg(String img) {
//                this.img = img;
//            }
//
//            public String getTitle() {
//                return title;
//            }
//
//            public void setTitle(String title) {
//                this.title = title;
//            }
//
//            public String getCount01() {
//                return count01;
//            }
//
//            public void setCount01(String count01) {
//                this.count01 = count01;
//            }
//
//            public String getCount02() {
//                return count02;
//            }
//
//            public void setCount02(String count02) {
//                this.count02 = count02;
//            }
//
//            public String getCoount03() {
//                return coount03;
//            }
//
//            public void setCoount03(String coount03) {
//                this.coount03 = coount03;
//            }
//
//            public String getStatus() {
//                return status;
//            }
//
//            public void setStatus(String status) {
//                this.status = status;
//            }
//        }
//    }
}
