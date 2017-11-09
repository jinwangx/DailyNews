package Lib;

import android.content.Context;

/**
 * 创建时间：2017/6/16
 * 更新时间 2017/10/30 15:06
 * 版本：
 * 作者：Mr.jin
 * 描述：获取应用上下文类
 */

public class MyNews {
    private static MyNews instance;
    Context context;
    public static MyNews getInstance(){
        synchronized (MyNews.class){
            if(instance==null)
                instance=new MyNews();
        }
        return instance;
    }
    private MyNews(){
    }

    public void init(Context context){
        this.context=context;
    }

    public Context getContext(){
        return context;
    }
}
