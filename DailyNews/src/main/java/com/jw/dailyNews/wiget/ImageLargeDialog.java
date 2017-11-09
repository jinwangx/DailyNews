package com.jw.dailyNews.wiget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.jw.dailyNews.R;

import Lib.ThreadManager;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;


/**
 * Created by Administrator on 2017/7/23.
 */

public class ImageLargeDialog extends Dialog {
    ImageView ivBack;
    private String url;
    private Handler mHandler=new Handler(){};
    private PhotoView photoView;

    public ImageLargeDialog(Context context, String url) {
        super(context,R.style.dialog_logout);
        setCancelable(true);
        this.url = url;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        setContentView(R.layout.dialog_image_large);
        photoView = (PhotoView) findViewById(R.id.dialog_photoView);
        attributes.height= LinearLayout.LayoutParams.MATCH_PARENT;
        attributes.width= LinearLayout.LayoutParams.MATCH_PARENT;
        PhotoViewAttacher mAttacher = new PhotoViewAttacher(photoView);
        mAttacher.update();
        mAttacher.onGlobalLayout();
        photoView.setAdjustViewBounds(true);
        Glide.with(getContext()).load(url).into(photoView);
    }

    int startX=0;
    int startY=0;
    int endX=0;
    int endY=0;
    int count=0;
    int offsetX=0;
    int offsetY=0;


    @Override
    public boolean dispatchTouchEvent(@NonNull MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                startX= (int) ev.getX();
                startY= (int) ev.getY();
                if(count<2)
                    count++;
                else
                    count=1;
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                endX= (int) ev.getX();
                endY= (int) ev.getY();
                offsetX=(endX-startX)*(endX-startX);
                offsetY=(endY-startY)*(endY-startY);
                if(count<3) {
                    ThreadManager.getInstance().createLongPool(3, 3, 2l).execute(new Runnable() {
                        @Override
                        public void run() {
                            if (count == 1 && (Math.sqrt(offsetX + offsetY) < 3))
                                mHandler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(count==1)
                                            dismiss();
                                    }
                                }, 300);
                        }
                    });
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}
