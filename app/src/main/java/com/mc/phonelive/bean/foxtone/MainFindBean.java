package com.mc.phonelive.bean.foxtone;

import com.mc.phonelive.R;
import com.mc.phonelive.bean.BaseVO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 发现
 */
public class MainFindBean extends BaseVO implements Serializable {

    public static List<MainFindBean.InfoBean.GameBean> getFindData() {
        List<InfoBean.GameBean> gamelist = new ArrayList<>();
        InfoBean.GameBean data = new InfoBean.GameBean();
        data.setId(0 + "");
        data.setName("沙巴克传奇");
        data.setPic(R.mipmap.game_item_img);
        data.setNum("1266.7万");data.setAvator("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=1888305140,799898080&fm=26&gp=0.jpg");
        gamelist.add(data);

        InfoBean.GameBean data1 = new InfoBean.GameBean();
        data1.setId(1 + "");
        data1.setName("捕鱼达人之航海大冒险");
        data1.setPic(R.mipmap.game_item_img01);
        data1.setNum("2250.8万");data1.setAvator("https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2596227692,2671407931&fm=26&gp=0.jpg");
        gamelist.add(data1);

        return gamelist;
    }


    private InfoBean info;

    public InfoBean getInfo() {
        return info;
    }

    public void setInfo(InfoBean info) {
        this.info = info;
    }

    public static class InfoBean {

        private List<SlideBean> slide;
        private List<NoticeBean> notice;
        private List<ListBean> list;
        private List<GameBean> gamelist;
        private List<FindSchoolBean> newsList;

        public List<SlideBean> getSlide() {
            return slide;
        }

        public void setSlide(List<SlideBean> slide) {
            this.slide = slide;
        }

        public List<NoticeBean> getNotice() {
            return notice;
        }

        public void setNotice(List<NoticeBean> notice) {
            this.notice = notice;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public List<FindSchoolBean> getNewsList() {
            return newsList;
        }

        public void setNewsList(List<FindSchoolBean> newsList) {
            this.newsList = newsList;
        }

        public static class GameBean {
            private String id;
            private String name;
            private String num;
            private int pic;
            private String avator;

            public String getAvator() {
                return avator;
            }

            public void setAvator(String avator) {
                this.avator = avator;
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

            public int getPic() {
                return pic;
            }

            public void setPic(int pic) {
                this.pic = pic;
            }
        }


        public static class SlideBean {
            /**
             * slide_pic : /data/upload/20200406/5e8adeea00d48.jpg
             * slide_url :
             */

            private String slide_pic;
            private String slide_url;

            public String getSlide_pic() {
                return slide_pic;
            }

            public void setSlide_pic(String slide_pic) {
                this.slide_pic = slide_pic;
            }

            public String getSlide_url() {
                return slide_url;
            }

            public void setSlide_url(String slide_url) {
                this.slide_url = slide_url;
            }
        }

        public static class NoticeBean {
            /**
             * id : 26
             * title : 攻略介绍
             * web_url : http://huyin.yanshi.hbbeisheng.com/index.php?g=portal&m=page&a=index&id=
             */

            private String id;
            private String title;
            private String web_url;

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

            public String getWeb_url() {
                return web_url;
            }

            public void setWeb_url(String web_url) {
                this.web_url = web_url;
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
             */

            private String id;
            private String name;
            private String thumb;
            private String fee;
            private String output;
            private String days;
            private String upper;
            private String cent;
            private String unfreeze;
            private String status;

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
        }


        public List<GameBean> getGamelist() {
            return gamelist;
        }

        public void setGamelist(List<GameBean> gamelist) {
            this.gamelist = gamelist;
        }
    }
}
