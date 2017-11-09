package com.jw.dailyNews.wiget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jw.dailyNews.R;


/**
 * Created by Administrator on 2017/7/28.
 */

public class ItemSwitchView extends RelativeLayout {
    TextView tvContent;
    private int dividerColor;
    String title;
    private View divider;
    private SwitchListener mListener;
    private SwitchButton switchButton;

    public ItemSwitchView(Context context) {
        this(context, null);
    }

    public ItemSwitchView(Context context,  AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public ItemSwitchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.itemSwitchView);
        title = ta.getString(R.styleable.itemSwitchView_content2);
        dividerColor = ta.getColor(R.styleable.itemSwitchView_dividerColor2, android.R.color.holo_orange_light);
        ta.recycle();
        initView();
    }

    private void initView() {
        View view = View.inflate(getContext(), R.layout.wight_switch_view, this);
        divider = view.findViewById(R.id.divider_line);
        tvContent = (TextView) view.findViewById(R.id.tv_content2 );
        switchButton = (SwitchButton) view.findViewById(R.id.switch_button);
        tvContent.setText(title);
        divider.setBackgroundColor(dividerColor);
        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    mListener.onOpen();
                else
                    mListener.onClose();
            }
        });
    }

    public void setChecked(boolean ifChecked){
        switchButton.setChecked(ifChecked);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setSwitchListener(SwitchListener listener){
        this.mListener=listener;
    }

    public interface SwitchListener{
        void onOpen();
        void onClose();
    }
}
