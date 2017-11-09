package com.jw.dailyNews.wiget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.jw.dailyNews.R;

/**
 * Created by Administrator on 2017/7/28.
 */

public class ItemTextView extends RelativeLayout {

    TextView tvTitle;
    TextView tvContent;
    private String desc;
    private String title;
    private int color;
    private View dividerColor;

    public ItemTextView(Context context) {
        this(context, null);
    }

    public ItemTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ItemTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.itemTextView);
        title=ta.getString(R.styleable.itemTextView_title3);
        desc = ta.getString(R.styleable.itemTextView_desc3);
        color = ta.getColor(R.styleable.itemTextView_dividerColor3, android.R.color.holo_orange_light);
        ta.recycle();
        initView();
    }

    private void initView() {
        View view = View.inflate(getContext(), R.layout.wight_text_view, this);
        tvTitle = (TextView) view.findViewById(R.id.title3);
        tvContent = (TextView) view.findViewById(R.id.desc3);
        dividerColor = view.findViewById(R.id.divider_color3);
        tvTitle.setText(title);
        tvContent.setText(desc);
        dividerColor.setBackgroundColor(color);
    }

    public void setText(String text){
        tvContent.setText(text);
    }

}
