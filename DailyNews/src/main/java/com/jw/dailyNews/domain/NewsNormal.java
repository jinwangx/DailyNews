package com.jw.dailyNews.domain;

import java.util.List;

/**
 * 创建时间：2017/7/18
 * 更新时间 2017/10/30 15:49
 * 版本：
 * 作者：Mr.jin
 * 描述：
 */

public class NewsNormal {
    private String title;


    public List<ImageSrc> getImgextra() {
        return imgextra;
    }

    public void setImgextra(List<ImageSrc> imgextra) {
        this.imgextra = imgextra;
    }

    private List<ImageSrc> imgextra;
    private String source;
    private String imgsrc;
    private String digest;
    private int commentCount;
    private String ptime;
    private String url;
    private String imgsrc3gtype;

    public String getSkipURL() {
        return skipURL;
    }

    public void setSkipURL(String skipURL) {
        this.skipURL = skipURL;
    }

    private String skipURL;


    public class ImageSrc{
        public String getImgsrc() {
            return imgsrc;
        }

        public void setImgsrc(String imgsrc) {
            this.imgsrc = imgsrc;
        }

        private String imgsrc;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getImgsrc() {
        return imgsrc;
    }

    public void setImgsrc(String imgsrc) {
        this.imgsrc = imgsrc;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public String getPtime() {
        return ptime;
    }

    public void setPtime(String ptime) {
        this.ptime = ptime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImgsrc3gtype() {
        return imgsrc3gtype;
    }

    public void setImgsrc3gtype(String imgsrc3gtype) {
        this.imgsrc3gtype = imgsrc3gtype;
    }
}
