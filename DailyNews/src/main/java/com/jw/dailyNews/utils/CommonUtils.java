package com.jw.dailyNews.utils;

import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
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
 * 描述：本应用工具类
 */

public class CommonUtils {
    public static String articleHtml= StreamUtils.readfromStream(StreamUtils.getAssetsInputStream("article.html"));

    /**
     * @description 将拿到的json字符串进行正确检查，返回程序能识别的字符串
     * @version
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
     * json结果解析类
     * @param json
     * @return
     */
    public static String parseIatResult(String json) {
        StringBuffer ret = new StringBuffer();
        try {
            JSONTokener tokener = new JSONTokener(json);
            JSONObject joResult = new JSONObject(tokener);

            JSONArray words = joResult.getJSONArray("ws");
            for (int i = 0; i < words.length(); i++) {
                // 转写结果词，默认使用第一个结果
                JSONArray items = words.getJSONObject(i).getJSONArray("cw");
                JSONObject obj = items.getJSONObject(0);
                ret.append(obj.getString("w"));
//				如果需要多候选结果，解析数组其他字段
//				for(int j = 0; j < items.length(); j++)
//				{
//					JSONObject obj = items.getJSONObject(j);
//					ret.append(obj.getString("w"));
//				}
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret.toString();
    }

    /**
     * 重组从网页拿到的html
     * @param url
     * @return
     */
    public static String getArticleHtml(String url){
        Document html=null;
        try
        {
            StreamUtils.getAssetsInputStream("article.html");
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

    public static String createRecommondUrl(int position){
        String url;
        if(position==0)
            url="http://3g.163.com/touch/jsonp/sy/recommend/"+position+"-10.html";
        else
            url="http://3g.163.com/touch/jsonp/sy/recommend/"+position+"0-10.html?hasad=1&miss=05&refresh=A";
        return url;
    }
    public static String createNewsUrl(String baseUrl,int position){
        String url;
        url= baseUrl+position+"0-10.html";
        return url;
    }
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

    public static ImageView getImageView(){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        ImageView iv = new ImageView(MyNews.getInstance().getContext());
        iv.setMaxHeight(300);
        params.gravity= Gravity.CENTER_HORIZONTAL;
        iv.setLayoutParams(params);
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return iv;
    }
    public static TextView getTextView(){
        TextView tv=new TextView(MyNews.getInstance().getContext());
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tv.setLayoutParams(params);
        tv.setTextSize(15);
        return tv;
    }
}
