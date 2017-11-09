package Lib;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 创建时间：2017/5/22
 * 更新时间 2017/10/30 15:11
 * 版本：
 * 作者：Mr.jin
 * 描述：线程池管理类
 */

public class ThreadManager {
    private static ThreadManager threadManager;
    ThreadPoolProxy threadPoolProxy;
    private ThreadManager(){

    }
    public synchronized static ThreadManager getInstance(){
        if(threadManager==null)
            threadManager=new ThreadManager();
        return threadManager;
    }
    public ThreadPoolProxy createLongPool(int corePoolSize,int maximumPoolSize,long keepAliveTime){
        if(threadPoolProxy==null)
            threadPoolProxy=new ThreadPoolProxy(corePoolSize, maximumPoolSize,keepAliveTime);
        return threadPoolProxy;
    }
    public void createShortPool(){

    }
    public class ThreadPoolProxy {
        private ThreadPoolExecutor executor;
        private int corePoolSize;
        private int maximumPoolSize;
        private long keepAliveTime;
        public ThreadPoolProxy(int corePoolSize,int maximumPoolSize,long keepAliveTime) {
            this.corePoolSize=corePoolSize;
            this.maximumPoolSize=maximumPoolSize;
            this.keepAliveTime=keepAliveTime;
        }

        public void execute(Runnable runnable) {
            if(executor==null)
                executor=new ThreadPoolExecutor(corePoolSize,maximumPoolSize,keepAliveTime, TimeUnit.MINUTES,
                        new LinkedBlockingQueue<Runnable>(10));
            executor.execute(runnable);
        }

    }
}
