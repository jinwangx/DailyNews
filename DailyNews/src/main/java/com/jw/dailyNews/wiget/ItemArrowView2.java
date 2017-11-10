package com.jw.dailyNews.wiget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jw.dailyNews.R;

/**
 * 创建时间：
 * 更新时间：2017/11/11 0011 上午 12:45
 * 作者：Mr.jin
 * 描述：
 */
public class ItemArrowView2 extends RelativeLayout {

    private String title;
    private String desc;
    private TextView tv_title;
    private TextView tv_desc;

    public ItemArrowView2(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ItemArrowView2);
        title = typedArray.getString(R.styleable.ItemArrowView2_title_arrow);
        desc = typedArray.getString(R.styleable.ItemArrowView2_desc);
        initView();
    }

    public ItemArrowView2(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public ItemArrowView2(Context context) {
        this(context,null);
    }

    private void initView() {
        View view = View.inflate(getContext(), R.layout.wight_arrow_view2, this);
        tv_title = (TextView) view.findViewById(R.id.tvStyle);
        tv_desc = (TextView) view.findViewById(R.id.tv_style);
        tv_title.setText(title);
        tv_desc.setText(desc);
    }
}
