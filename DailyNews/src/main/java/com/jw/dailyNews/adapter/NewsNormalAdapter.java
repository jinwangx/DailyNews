package com.jw.dailyNews.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jw.dailyNews.R;
import com.jw.dailyNews.bean.NewsNormal;
import com.jw.dailyNews.utils.DateUtils;
import com.jw.dailyNews.utils.GlideUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Administrator on 2017/7/31.
 */

public class NewsNormalAdapter extends DefaultAdapter<NewsNormal> {


    public NewsNormalAdapter(Context context, List<NewsNormal> lists) {
        super(context, lists);
    }

    public  enum ITEM_TYPE {
        ITEM_TYPE_0,
        ITEM_TYPE_1,
        ITEM_TYPE_2,
        ITEM_TYPE_3,
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == NewsNormalAdapter.ITEM_TYPE.ITEM_TYPE_0.ordinal()) {
            View view=mInflater.inflate(R.layout.item_null,parent, false);
            return new NewsNormalAdapter.Holder0(view);
        }
        else if (viewType == NewsNormalAdapter.ITEM_TYPE.ITEM_TYPE_1.ordinal()) {
            View view=mInflater.inflate(R.layout.item_list_img_type1,parent, false);
            return new NewsNormalAdapter.Holder1(view);
        }
        else if (viewType == NewsNormalAdapter.ITEM_TYPE.ITEM_TYPE_2.ordinal()) {
            View view=mInflater.inflate(R.layout.item_list_img_type2,parent, false);
            return new NewsNormalAdapter.Holder2(view);
        }
        else if (viewType == NewsNormalAdapter.ITEM_TYPE.ITEM_TYPE_3.ordinal()) {
            View view=mInflater.inflate(R.layout.item_list_img_type3,parent, false);
            return new NewsNormalAdapter.Holder3(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder,position);
        final NewsNormal news=lists.get(position);
        if(holder instanceof Holder0){

        } else if(holder instanceof Holder1){
            ((Holder1) holder).titleType1.setText(news.getTitle());
            GlideUtils.load(mContext,news.getImgsrc(),((Holder1) holder).imageType1);
            ((Holder1) holder).categoryType1.setText(news.getSource());
            ((Holder1) holder).timeType1.setText(news.getPtime());
            ((Holder1) holder).tcountType1.setText(news.getCommentCount()+"");
        }else if(holder instanceof Holder2){
            ((Holder2) holder).titleType2.setText(news.getTitle());
            GlideUtils.load(mContext,news.getImgsrc(),((Holder2) holder).image1Type2);
            GlideUtils.load(mContext,news.getImgextra().get(0).getImgsrc(),((Holder2) holder).image2Type2);
            GlideUtils.load(mContext,news.getImgextra().get(1).getImgsrc(),((Holder2) holder).image3Type2);

            ((Holder2) holder).categoryType2.setText(news.getSource());
            ((Holder2) holder).timeType2.setText(DateUtils.fromNow("yyyy-MM-dd HH:mm:ss",news.getPtime()));
            ((Holder2) holder).tcountType2.setText(news.getCommentCount() + "");
        }else if(holder instanceof Holder3){
            ((Holder3) holder).titleType3.setText(news.getTitle());
            GlideUtils.load(mContext,news.getImgsrc(),((Holder3) holder).imageType3);
            ((Holder3) holder).categoryType3.setText(news.getSource());
            ((Holder3) holder).timeType3.setText(DateUtils.fromNow("yyyy-MM-dd HH:mm:ss",news.getPtime()));
            ((Holder3) holder).tcountType3.setText(news.getCommentCount() + "");
        }
    }

    @Override
    public int getItemViewType(int position) {
        String gtype = lists.get(position).getImgsrc3gtype();
        int ordinal = 0;
        switch (gtype) {
            case "0":
                ordinal = ITEM_TYPE.ITEM_TYPE_0.ordinal();
                break;
            case "1":
                ordinal = ITEM_TYPE.ITEM_TYPE_1.ordinal();
                break;
            case "2":
                ordinal = ITEM_TYPE.ITEM_TYPE_2.ordinal();
                break;
            case "3":
                ordinal = ITEM_TYPE.ITEM_TYPE_3.ordinal();
                break;

        }
        return ordinal;
    }

    public static class Holder0 extends RecyclerView.ViewHolder{

        public Holder0(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public static class Holder1 extends BaseHolder {
        @BindView(R.id.image_type1)
        ImageView imageType1;
        @BindView(R.id.title_type1)
        TextView titleType1;
        @BindView(R.id.category_type1)
        TextView categoryType1;
        @BindView(R.id.time_type1)
        TextView timeType1;
        @BindView(R.id.tcount_type1)
        TextView tcountType1;

        public Holder1(View itemView) {
            super(itemView);
        }
    }

    public static class Holder2 extends BaseHolder {
        @BindView(R.id.title_type2)
        TextView titleType2;
        @BindView(R.id.image1_type2)
        ImageView image1Type2;
        @BindView(R.id.image2_type2)
        ImageView image2Type2;
        @BindView(R.id.image3_type2)
        ImageView image3Type2;
        @BindView(R.id.category_type2)
        TextView categoryType2;
        @BindView(R.id.time_type2)
        TextView timeType2;
        @BindView(R.id.tcount_type2)
        TextView tcountType2;

        public Holder2(View itemView) {
            super(itemView);
        }
    }

    public static class Holder3 extends BaseHolder {
        @BindView(R.id.title_type3)
        TextView titleType3;
        @BindView(R.id.image_type3)
        ImageView imageType3;
        @BindView(R.id.category_type3)
        TextView categoryType3;
        @BindView(R.id.time_type3)
        TextView timeType3;
        @BindView(R.id.tcount_type3)
        TextView tcountType3;


        public Holder3(View itemView) {
            super(itemView);
        }
    }

    public static class BaseHolder extends RecyclerView.ViewHolder{

        public BaseHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
