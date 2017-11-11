package com.jw.dailyNews.utils;

import android.util.Log;

import com.bumptech.glide.Glide;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Lib.MyNews;

/**
 * 创建时间：
 * 更新时间 2017/10/30 15:19
 * 版本：
 * 作者：Mr.jin
 * 描述：本应用工具类,不能公用
 */

public class CommonUtils {
    public static String articleHtml= ThemeUtils.readFromAssetsStream(
            ThemeUtils.getAssetsInputStream("article.html"));

    /**
     * 自动生成推荐新闻url
     * @param position
     * @return
     */
    public static String createRecommondUrl(int position){
        String url;
        if(position==0)
            url="http://3g.163.com/touch/jsonp/sy/recommend/"+position+"-10.html";
        else
            url="http://3g.163.com/touch/jsonp/sy/recommend/"+position+"0-10.html?hasad=1&miss=05&refresh=A";
        return url;
    }

    /**
     * 自动生成普通新闻url
     * @param position
     * @return
     */
    public static String createNewsUrl(String baseUrl,int position){
        String url;
        url= baseUrl+position+"0-10.html";
        return url;
    }

    /**
     * 由于从网页拿到的新闻json数据本地api有时候会解析错误，经尝试，
     * 将拿到的新闻json字符串进行如下替换，本地能够正确解析
     * @param type
     * @param str
     * @return
     */
    public static String correctJson(String type,String str){
        String json="";
        switch (type){
            case "news":
                json = str.replace("artiList(", "").replace(")", "");
                break;

            case "normal":
                json=str;
        }
        return json;
    }

    /**
     * 得到新闻文章url
     * @param url
     * @param type
     * @return
     */
    public static String getArticalUrl(String url,String type){
        if(type.equals("article")){
            String sample="http://3g.163.com/all/article/CSABRGKU0001899O_0.html";
            Pattern pattern1 = Pattern.compile("(?<=http://3g\\.163\\.com/all/article/).*(?=\\.html)");
            Matcher matcher1 = pattern1.matcher(sample);
            matcher1.find();
            if(!url.contains("news")&&!url.contains("article")){

            }  else if(!url.contains("news")) {
                String id2 = url.replace("http://3g.163.com/touch/article.html?docid=", "");
                url = sample.replace(matcher1.group(), id2);
            }
        } else{
            String sample2= "http://3g.163.com/all/photoview/0001/2271768.html#offset=0";

            Pattern pattern1 = Pattern.compile("http://3g\\.163\\.com/touch/photoview.*setid=");
            Matcher matcher1 = pattern1.matcher(url);
            matcher1.find();
            String setid = url.replace(matcher1.group(), "");

            Pattern pattern2 = Pattern.compile("(?<=http://3g\\.163\\.com/touch/photoview\\.html\\?channelid=).*(?=&setid)");
            Matcher matcher2 = pattern2.matcher(url);
            matcher2.find();
            String channelid = matcher2.group();

            Pattern pattern=Pattern.compile("(?<=http://3g\\.163\\.com/all/photoview/).*(?=\\.html)");
            Matcher matcher = pattern.matcher(sample2);
            matcher.find();
            url=sample2.replace(matcher.group(), channelid+"/"+setid);
        }
        Log.v("mUrl",url);
        return url;
    }

    /**
     * 重组从新闻详情网页拿到的html,原始html广告多
     * 本地html模板有自己的css样式，添加了其他功能的js，实现了js调用java，将网页中图片点击事件
     * 送到java语言处理
     * @param url
     * @return
     */
    public static String getArticleHtml(String url){
        Document html=null;
        try
        {
            ThemeUtils.getAssetsInputStream("article.html");
            Document document = Jsoup.connect(url).maxBodySize(0).get();
            Elements article = document.select("main").select("article");
            Elements elements = article.select("a").select("img");
            for(Element element:elements){
                element.attr("src",element.attr("data-src"));
            }
            article.select("div.footer").remove();
            article.select("div.page").addClass("on");
            html = Jsoup.parse(articleHtml);
            html.select("main").append(article.toString());
            Log.v("main",html.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return html.toString();
    }

    /**
     * 获取制定目录下存储的缓存大小
     * @param path
     * @return
     */
    public static Double getCacheSize(String path){
        return FileUtils.getFileOrFilesSize(path,3);
    }

    /**
     * 清空Glide的图片缓存
     */
    public static void clearImageCache(){
        Glide.get(MyNews.getInstance().getContext()).clearDiskCache();
        //Glide.get(context).clearMemory();
    }
}
