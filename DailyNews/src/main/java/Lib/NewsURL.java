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
    public static String URL_HTTP_GUONEI=WANGYI_BASE_HTTP+"/touch/reconstruct/article/list/BD29LPUBwangning/";   //国内
    public static String URL_HTTP_WORLD=WANGYI_BASE_HTTP+"/touch/reconstruct/article/list/BD29MJTVwangning/";         //国际
    public static String URL_HTTP_SHEHUI=WANGYI_BASE_HTTP+"/touch/reconstruct/article/list/BCR1UC1Qwangning/";        //社会
    public static String URL_HTTP_HISTORY=WANGYI_BASE_HTTP+"/touch/reconstruct/article/list/C275ML7Gwangning/";      //历史
    public static String URL_HTTP_ENTERTAINMENT=WANGYI_BASE_HTTP+"/touch/reconstruct/article/list/BA10TA81wangning/";  //娱乐
    public static String URL_HTTP_SPORTS=WANGYI_BASE_HTTP+"/touch/reconstruct/article/list/BA8E6OEOwangning/";    //体育
    public static String URL_HTTP_ECONOMIC=WANGYI_BASE_HTTP+"/touch/reconstruct/article/list/BA8EE5GMwangning/";   //财经
    public static String URL_HTTP_CAR=WANGYI_BASE_HTTP+"/touch/reconstruct/article/list/BA8EE5GMwangning/";   //汽车。。
    public static String URL_HTTP_WAR=WANGYI_BASE_HTTP+"/touch/reconstruct/article/list/BAI67OGGwangning/";    //军事
    public static String URL_HTTP_GAME=WANGYI_BASE_HTTP+"/touch/reconstruct/article/list/BAI6RHDKwangning/";    //游戏
    public static String URL_HTTP_EDU=WANGYI_BASE_HTTP+"/touch/reconstruct/article/list/BA8FF5PRwangning/";    //教育
    public static String URL_HTTP_OWN=WANGYI_BASE_HTTP+"/touch/reconstruct/article/list/BAI5E21Owangning/";    //独家


    /**
     * 方便格式工厂根据tag造fragment
     * @param type
     * @return
     */
    public static String getTag(String type){
        String tag;
        if(type.equals(URL_HTTP_GUONEI))
            tag="BD29LPUBwangning";
        else if(type.equals(URL_HTTP_WORLD))
            tag="BD29MJTVwangning";
        else if(type.equals(URL_HTTP_SHEHUI))
            tag="BCR1UC1Qwangning";
        else if(type.equals(URL_HTTP_HISTORY))
            tag="C275ML7Gwangning";
        else if(type.equals(URL_HTTP_ENTERTAINMENT))
            tag="BA10TA81wangning";
        else if(type.equals(URL_HTTP_ECONOMIC))
            tag="BA8EE5GMwangning";

        else if(type.equals(URL_HTTP_EDU))
            tag="BA8FF5PRwangning";
        else if(type.equals(URL_HTTP_GAME))
            tag="BAI6RHDKwangning";
        else if(type.equals(URL_HTTP_WAR))
            tag="BAI67OGGwangning";
        else if(type.equals(URL_HTTP_SPORTS))
            tag="BA8E6OEOwangning";
        else if(type.equals(URL_HTTP_OWN))
            tag="BAI5E21Owangning";
        else
            tag=null;
        return tag;
    }

}
