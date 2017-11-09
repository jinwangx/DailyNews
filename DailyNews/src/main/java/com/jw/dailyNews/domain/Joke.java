package com.jw.dailyNews.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on .
 */

/**
 * 创建时间：2017/7/20
 * 更新时间 2017/10/30 15:48
 * 版本：
 * 作者：Mr.jin
 * 描述：来自百思不得姐的json数据，封装为Joke类
 */

public class Joke {
    private int status;
    private String comment;        //评论
    private ArrayList<TopComments> top_comments;

    public List<Tags> getTags() {
        return tags;
    }

    public void setTags(List<Tags> tags) {
        this.tags = tags;
    }

    private List<Tags> tags;

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    private Image image;

    public Gif getGif() {
        return gif;
    }

    public void setGif(Gif gif) {
        this.gif = gif;
    }

    private Gif gif;
    private String bookmark;
    private String text;          //标题
    private String up;            //点赞

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public ArrayList<TopComments> getTop_comments() {
        return top_comments;
    }

    public void setTop_comments(ArrayList<TopComments> top_comments) {
        this.top_comments = top_comments;
    }


    public String getBookmark() {
        return bookmark;
    }

    public void setBookmark(String bookmark) {
        this.bookmark = bookmark;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUp() {
        return up;
    }

    public void setUp(String up) {
        this.up = up;
    }

    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }

    public int getDown() {
        return down;
    }

    public void setDown(int down) {
        this.down = down;
    }

    public int getForwrard() {
        return forwrard;
    }

    public void setForwrard(int forwrard) {
        this.forwrard = forwrard;
    }

    public U getU() {
        return u;
    }

    public void setU(U u) {
        this.u = u;
    }

    public String getPasstime() {
        return passtime;
    }

    public void setPasstime(String passtime) {
        this.passtime = passtime;
    }

    public MVedio getVideo() {
        return video;
    }

    public void setVideo(MVedio video) {
        this.video = video;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String share_url;
    private int down;             //鄙视
    private int forwrard;         //转发
    private U u;
    private String passtime;      //发布时间
    private MVedio video;
    private String type;
    private String id;
    public class Tags{
    }

    public class TopComments{
        private int voicetime;

        public int getVoicetime() {
            return voicetime;
        }

        public void setVoicetime(int voicetime) {
            this.voicetime = voicetime;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getHate_count() {
            return hate_count;
        }

        public void setHate_count(int hate_count) {
            this.hate_count = hate_count;
        }

        public String getCmt_type() {
            return cmt_type;
        }

        public void setCmt_type(String cmt_type) {
            this.cmt_type = cmt_type;
        }

        public int getPrecid() {
            return precid;
        }

        public void setPrecid(int precid) {
            this.precid = precid;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getLike_count() {
            return like_count;
        }

        public void setLike_count(int like_count) {
            this.like_count = like_count;
        }

        public U getU() {
            return u;
        }

        public void setU(U u) {
            this.u = u;
        }

        private int status;
        private int hate_count;
        private String cmt_type;   //评论内容类型

        private int precid;
        private String content;   //评论内容
        private int like_count;
        private U u;
    }

    public class U{
        public List<String> getHeader() {
            return header;
        }

        public void setHeader(List<String> header) {
            this.header = header;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public boolean is_vip() {
            return is_vip;
        }

        public void setIs_vip(boolean is_vip) {
            this.is_vip = is_vip;
        }

        public boolean is_v() {
            return is_v;
        }

        public void setIs_v(boolean is_v) {
            this.is_v = is_v;
        }

        public String getRoom_url() {
            return room_url;
        }

        public void setRoom_url(String room_url) {
            this.room_url = room_url;
        }

        public String getRoom_name() {
            return room_name;
        }

        public void setRoom_name(String room_name) {
            this.room_name = room_name;
        }

        public String getRoom_role() {
            return room_role;
        }

        public void setRoom_role(String room_role) {
            this.room_role = room_role;
        }

        public String getRoom_icon() {
            return room_icon;
        }

        public void setRoom_icon(String room_icon) {
            this.room_icon = room_icon;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        List<String> header;       //发布者头像
        private String uid;
        private boolean is_vip;
        private boolean is_v;
        private String room_url;
        private String room_name;
        private String room_role;
        private String room_icon;
        private String name;      //发布者姓名
    }
    public class MVedio{
        public int getPlayfcount() {
            return playfcount;
        }

        public void setPlayfcount(int playfcount) {
            this.playfcount = playfcount;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public List<String> getVideo() {
            return video;
        }

        public void setVideo(List<String> video) {
            this.video = video;
        }

        public List<String> getDownload() {
            return download;
        }

        public void setDownload(List<String> download) {
            this.download = download;
        }

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }

        public int getPlaycount() {
            return playcount;
        }

        public void setPlaycount(int playcount) {
            this.playcount = playcount;
        }

        public List<String> getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(List<String> thumbnail) {
            this.thumbnail = thumbnail;
        }

        public List<String> getThumbnail_small() {
            return thumbnail_small;
        }

        public void setThumbnail_small(List<String> thumbnail_small) {
            this.thumbnail_small = thumbnail_small;
        }

        private int playfcount;
        private int height;
        private int width;
        private List<String> video;
        private List<String> download;
        private int duration;    //时长
        private int playcount;   //播放量
        private List<String> thumbnail;
        private List<String> thumbnail_small;
    }

    public class Image{
        public List<String> getMedium() {
            return medium;
        }

        public void setMedium(List<String> medium) {
            this.medium = medium;
        }

        public List<String> getBig() {
            return big;
        }

        public void setBig(List<String> big) {
            this.big = big;
        }

        public List<String> getDownload_url() {
            return download_url;
        }

        public void setDownload_url(List<String> download_url) {
            this.download_url = download_url;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public List<String> getSmall() {
            return small;
        }

        public void setSmall(List<String> small) {
            this.small = small;
        }

        public List<String> getThumbnail_small() {
            return thumbnail_small;
        }

        public void setThumbnail_small(List<String> thumbnail_small) {
            this.thumbnail_small = thumbnail_small;
        }

        private List<String> medium;
        private List<String> big;
        private List<String> download_url;
        private int height;
        private int width;
        private List<String> small;
        private List<String> thumbnail_small;

    }

    public class Gif{
        private List<String> images;
        private int width;
        private List<String> gif_thumbnail;
        private List<String> download_url;
        private int height;

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public List<String> getGif_thumbnail() {
            return gif_thumbnail;
        }

        public void setGif_thumbnail(List<String> gif_thumbnail) {
            this.gif_thumbnail = gif_thumbnail;
        }

        public List<String> getDownload_url() {
            return download_url;
        }

        public void setDownload_url(List<String> download_url) {
            this.download_url = download_url;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }
    }
}
