package Lib;

/**
 * 创建时间：2017/6/15
 * 更新时间 2017/10/30 15:11
 * 版本：
 * 作者：Mr.jin
 * 描述：
 */

public class NewsURL {
    //网易新闻
    public static String WANGYI_BASE_HTTP="http://3g.163.com";
    //百思不得姐笑话链接,一次获取80条数据
    public static String BAISIBUDEJIE_JOKE_HTTP="http://s.budejie.com/topic/list/jingxuan/1/budejie-android-6.2.8/0-80.json";

    /**
     * 新闻板块
     */
    public static String URL_HTTP_WORLD=WANGYI_BASE_HTTP+"/touch/reconstruct/article/list/BD29MJTVwangning/";         //国际


    /**
     * 方便格式工厂根据tag造fragment
     * @param type
     * @return
     */
    public static String getTag(String type){
        String tag;
        if(type.equals(URL_HTTP_WORLD))
            tag="BD29MJTVwangning";
        else
            tag=null;
        return tag;
    }

}
