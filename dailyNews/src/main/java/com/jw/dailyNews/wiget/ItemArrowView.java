package com.jw.dailyNews.wiget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jw.dailyNews.R;

/**
 * 创建时间：2017/7/28
 * 更新时间：2017/11/11 0011 上午 12:45
 * 作者：Mr.jin
 * 描述：主界面左面板正文字号item组合控件
 */
public class ItemArrowView extends RelativeLayout {
    ImageView ivIcon;
    TextView tvContent;
    private int iconResource;
    private String desc;
    private float dividerHeight;
    private int dividerColor;
    private View dividerLine;

    public ItemArrowView(Context context) {
        this(context, null);
    }

    public ItemArrowView(Context context,  AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public ItemArrowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.itemArrowView);
        iconResource = ta.getResourceId(R.styleable.itemArrowView_icon, 0);
        desc = ta.getString(R.styleable.itemArrowView_content);
        dividerHeight = ta.getDimension(R.styleable.itemArrowView_dividerHeight, 2);
        dividerColor = ta.getColor(R.styleable.itemArrowView_dividerColor, android.R.color.holo_orange_light);
        ta.recycle();
        initView();
    }

    private void initView() {
        View view = View.inflate(getContext(), R.layout.wight_arrow_view, this);
        ivIcon = (ImageView) view.findViewById(R.id.ivIconWav);
        tvContent = (TextView) view.findViewById(R.id.tvContentWav);
        dividerLine = view.findViewById(R.id.divider_line);
        ivIcon.setImageResource(iconResource);
        tvContent.setText(desc);
        dividerLine.setBackgroundColor(dividerColor);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
