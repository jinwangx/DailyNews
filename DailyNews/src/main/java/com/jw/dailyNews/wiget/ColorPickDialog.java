package com.jw.dailyNews.wiget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import com.jw.dailyNews.R;

/**
 * Author: Administrator
 * Created on:  2017/8/16.
 * Description:
 */

public class ColorPickDialog extends Dialog{

    private ColorPickView mView;
    private ColorPickView.OnColorChangedListener mListener;

    public ColorPickDialog(@NonNull Context context) {
        super(context,R.style.dialog_logout);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view=View.inflate(getContext(),R.layout.layout_color_pick,null);
        mView = (ColorPickView) view.findViewById(R.id.color_picker_view);
        setContentView(view);
    }

    public void setOnColorChangedListener(ColorPickView.OnColorChangedListener listener){
        this.mListener=listener;
        mView.setOnColorChangedListener(new ColorPickView.OnColorChangedListener() {

            @Override
            public void onColorChange(int color) {
                mListener.onColorChange(color);
            }

        });
    }


}
