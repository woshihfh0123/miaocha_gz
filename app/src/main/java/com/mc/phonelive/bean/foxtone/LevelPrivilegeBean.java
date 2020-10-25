package com.mc.phonelive.bean.foxtone;

import com.mc.phonelive.bean.BaseVO;

import java.io.Serializable;

/**
 * 等级特权
 */
public class LevelPrivilegeBean extends BaseVO implements Serializable {
    private InfoBean info;

    public InfoBean getInfo() {
        return info;
    }

    public void setInfo(InfoBean info) {
        this.info = info;
    }

    public static class InfoBean {
        private String id;
        private String name;
        private String thumb;
        private String content;

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

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
