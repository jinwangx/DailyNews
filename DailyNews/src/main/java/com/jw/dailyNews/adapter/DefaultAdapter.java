package com.jw.dailyNews.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import java.util.List;

/**
 * Created by Administrator on .
 */

/**
 * 创建时间：2017/8/1
 * 更新时间 2017/10/30 15:43
 * 版本：
 * 作者：Mr.jin
 * 描述：继承自RecyclerView.Adapter的多类型且有ItemClickListener的Adapter
 */

public abstract class DefaultAdapter<Data> extends RecyclerView.Adapter{
    protected List<Data> lists;
    protected Context mContext;
    protected final LayoutInflater mInflater;
    public OnItemClickListener mListener;


    public DefaultAdapter(Context context, List<Data> lists) {
        this.mContext = context;
        this.lists = lists;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClick(holder.itemView,position,lists.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }


    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener=listener;
    }

    public interface OnItemClickListener<Data>{
        void onItemClick(View v,int position,Data data);
    }

}
