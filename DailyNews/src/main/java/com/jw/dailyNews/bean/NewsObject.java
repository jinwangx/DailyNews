package com.jw.dailyNews.bean;

import java.util.ArrayList;

/**
 * 创建时间：2017/7/12
 * 更新时间 2017/10/30 15:49
 * 版本：
 * 作者：Mr.jin
 * 描述：
 */

public class NewsObject {
    public ArrayList<News> getNews() {
        return news;
    }

    public void setNews(ArrayList<News> news) {
        this.news = news;
    }

    public ArrayList<News> getFocus() {
        return focus;
    }

    public void setFocus(ArrayList<News> focus) {
        this.focus = focus;
    }

    public ArrayList<News> getList() {
        return list;
    }

    public void setList(ArrayList<News> list) {
        this.list = list;
    }

    private ArrayList<News> news;
    private ArrayList<News> focus;
    private ArrayList<News> list;


    public class News{
        String category;
        String digest;

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getDigest() {
            return digest;
        }

        public void setDigest(String digest) {
            this.digest = digest;
        }

        public int getImgsrc3gtype() {
            return imgsrc3gtype;
        }

        public void setImgsrc3gtype(int imgsrc3gtype) {
            this.imgsrc3gtype = imgsrc3gtype;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public ArrayList<PicInfo> getPicInfo() {
            return picInfo;
        }

        public void setPicInfo(ArrayList<PicInfo> picInfo) {
            this.picInfo = picInfo;
        }

        public String getPtime() {
            return ptime;
        }

        public void setPtime(String ptime) {
            this.ptime = ptime;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public int getTcount() {
            return tcount;
        }

        public void setTcount(int tcount) {
            this.tcount = tcount;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        int imgsrc3gtype;
        String link;
        ArrayList<PicInfo> picInfo;
        String ptime;
        String source;
        String tag;
        int tcount;
        String title;
        String type;

        public class PicInfo{
            public String getHeight() {
                return height;
            }

            public void setHeight(String height) {
                this.height = height;
            }

            public String getRef() {
                return ref;
            }

            public void setRef(String ref) {
                this.ref = ref;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getWidth() {
                return width;
            }

            public void setWidth(String width) {
                this.width = width;
            }

            String height;
            String ref;
            String url;
            String width;
        }
    }

}
