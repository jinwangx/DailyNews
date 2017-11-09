package com.jw.dailyNews.wiget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jw.dailyNews.R;

public class ItemCheckView extends RelativeLayout {

    private TextView tv_title, tv_Desc;
    private CheckBox cb_status;
    private String mTitle, mDesc_off, mDesc_on;

    public ItemCheckView(Context context) {
        this(context,null);
    }
    public ItemCheckView(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }
    public ItemCheckView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.itemCheckBoxView);
        mTitle = ta.getString(R.styleable.itemCheckBoxView_title_checkbox);
        mDesc_on = ta.getString(R.styleable.itemCheckBoxView_desc_on);
        mDesc_off = ta.getString(R.styleable.itemCheckBoxView_desc_off);
        initView();
    }

    private void initView() {
        View view = View.inflate(getContext(), R.layout.wight_checkbox_view, this);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_Desc = (TextView) view.findViewById(R.id.tv_Desc);
        cb_status = (CheckBox) view.findViewById(R.id.cb_status);
        tv_title.setText(mTitle);
    }

    public boolean isChecked() {
        return cb_status.isChecked();
    }

    public void setChecked(boolean isChecked) {
        if (isChecked) {
            cb_status.setChecked(true);
            tv_Desc.setText(mDesc_on);
        } else {
            cb_status.setChecked(false);
            tv_Desc.setText(mDesc_off);
        }
    }

}