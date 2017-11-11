package com.jw.dailyNews.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jw.dailyNews.R;
import com.jw.dailyNews.bean.Joke;
import com.jw.dailyNews.utils.DateUtils;
import com.jw.dailyNews.utils.GlideUtils;
import com.jw.dailyNews.wiget.ImageDetailDialog;
import com.jw.dailyNews.wiget.ImageLoadingPage;

import java.util.ArrayList;
import java.util.List;

import Lib.NewsManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

public class JokeAdapter extends DefaultAdapter<Joke> {

    public JokeAdapter(Context context, List<Joke> lists) {
        super(context, lists);
    }

    public enum ITEM_TYPE {
        ITEM_TYPE_TEXT,
        ITEM_TYPE_IMAGE,
        ITEM_TYPE_GIF,
        ITEM_TYPE_VIDEO
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE.ITEM_TYPE_IMAGE.ordinal()) {
            return new ImageViewHolder(mInflater.inflate(R.layout.item_joke_image, parent, false));
        } else if (viewType == ITEM_TYPE.ITEM_TYPE_TEXT.ordinal()) {
            return new TextViewHolder(mInflater.inflate(R.layout.item_joke_text, parent, false));
        } else if (viewType == ITEM_TYPE.ITEM_TYPE_GIF.ordinal()) {
            return new GifViewHolder(mInflater.inflate(R.layout.item_joke_gif, parent, false));
        } else if (viewType == ITEM_TYPE.ITEM_TYPE_VIDEO.ordinal()) {
            return new VideoViewHolder(mInflater.inflate(R.layout.item_joke_vidio, parent, false));
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final Joke joke = lists.get(position);
        if (holder instanceof TextViewHolder) {

        } else if (holder instanceof ImageViewHolder) {
            GlideUtils.load(mContext,joke.getImage().getBig().get(0),((ImageViewHolder) holder).ivImage);
            ((ImageViewHolder) holder).btnLarge.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageDetailDialog dialog = new ImageDetailDialog(mContext, joke.getImage().getBig().get(0));
                    dialog.show();
                }
            });
        } else if (holder instanceof GifViewHolder) {
            String url=joke.getGif().getImages().get(0);
            Log.v("gifurl",url);
            ImageLoadingPage imageLoadingPage=new ImageLoadingPage(mContext);
            imageLoadingPage.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            ((GifViewHolder) holder).lll.removeAllViews();
            ((GifViewHolder) holder).lll.addView(imageLoadingPage);
            ((GifViewHolder) holder).lll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageDetailDialog dialog = new ImageDetailDialog(mContext, joke.getGif().getImages().get(0));
                    dialog.show();
                }
            });
            imageLoadingPage.setImageUrl(url);

        } else if (holder instanceof VideoViewHolder) {
            ((VideoViewHolder) holder).jcPlayer.setUp(joke.getVideo().getVideo().get(0), JCVideoPlayer.SCREEN_LAYOUT_LIST, joke.getText());
            GlideUtils.load(mContext,joke.getVideo().getThumbnail().get(0),((VideoViewHolder) holder).jcPlayer.thumbImageView);
        }
        GlideUtils.load(mContext,joke.getU().getHeader().get(0),((BaseHolder) holder).ivIconPublisher);
        ((BaseHolder) holder).tvNamePublisher.setText(joke.getU().getName());
        ((BaseHolder) holder).tvTimePublish.setText(DateUtils.fromNow("yyyy-MM-dd HH:mm:ss", joke.getPasstime()));
        ((BaseHolder) holder).tvTitle.setText(joke.getText());
        ((BaseHolder) holder).tvUp.setText(joke.getUp());
        ((BaseHolder) holder).tvDown.setText(joke.getDown() + "");
        ((BaseHolder) holder).tvForward.setText(joke.getForwrard() + "");
        ((BaseHolder) holder).tvCommentCount.setText(joke.getComment() + "");
        ((BaseHolder) holder).llForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewsManager.getInstance().showShare(joke.getText(),"",
                joke.getShare_url());
            }
        });
        ArrayList<Joke.TopComments> top_comments = joke.getTop_comments();
        if (top_comments != null && top_comments.size() != 0) {
            ((BaseHolder) holder).topComments.removeAllViews();
            for (Joke.TopComments comment : top_comments) {
                //行
                LinearLayout linearLayout = new LinearLayout(mContext);
                LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                linearLayout.setLayoutParams(layoutParams1);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                linearLayout.setPadding(5, 2, 5, 2);

                //用户名
                TextView tv_name = new TextView(mContext);
                tv_name.setLayoutParams(layoutParams);
                tv_name.setText(comment.getU().getName() + ": ");
                tv_name.setTextColor(Color.parseColor("#ff33b5e5"));

                //评论内容
                TextView tv_content = new TextView(mContext);
                tv_name.setLayoutParams(layoutParams);
                tv_content.setText(comment.getContent());
                tv_content.setTextColor(Color.BLACK);

                linearLayout.addView(tv_name);
                linearLayout.addView(tv_content);
                linearLayout.setBackgroundColor(Color.parseColor("#ffd7d7d7"));
                ((BaseHolder) holder).topComments.addView(linearLayout);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        int ordinal = 0;
        switch (lists.get(position).getType()) {
            case "text":
                ordinal = ITEM_TYPE.ITEM_TYPE_TEXT.ordinal();   //序号
                break;
            case "image":
                ordinal = ITEM_TYPE.ITEM_TYPE_IMAGE.ordinal();
                break;
            case "gif":
                ordinal = ITEM_TYPE.ITEM_TYPE_GIF.ordinal();
                break;
            case "video":
                ordinal = ITEM_TYPE.ITEM_TYPE_VIDEO.ordinal();
        }
        return ordinal;
    }

    @Override
    public int getItemCount() {
        return lists == null ? 0 : lists.size();
    }


    public static class TextViewHolder extends BaseHolder {

        public TextViewHolder(View itemView) {
            super(itemView);
        }
    }

    public static class ImageViewHolder extends BaseHolder {
        @BindView(R.id.iv_image)
        ImageView ivImage;
        @BindView(R.id.btn_large)
        TextView btnLarge;

        public ImageViewHolder(View itemView) {
            super(itemView);
        }
    }

    public static class GifViewHolder extends BaseHolder {
        @BindView(R.id.llll)
        LinearLayout lll;

        public GifViewHolder(View itemView) {
            super(itemView);
        }
    }

    public static class VideoViewHolder extends BaseHolder {
        @BindView(R.id.jc_player)
        public JCVideoPlayerStandard jcPlayer;

        public VideoViewHolder(View itemView) {
            super(itemView);
        }
    }

    public static class BaseHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_icon_publisher)
        CircleImageView ivIconPublisher;
        @BindView(R.id.tv_name_publisher)
        TextView tvNamePublisher;
        @BindView(R.id.tv_time_publish)
        TextView tvTimePublish;
        @BindView(R.id.tv_title)
        TextView tvTitle;

        @BindView(R.id.ib_up)
        ImageButton ibUp;
        @BindView(R.id.tv_up)
        TextView tvUp;
        @BindView(R.id.ib_down)
        ImageButton ibDown;
        @BindView(R.id.tv_down)
        TextView tvDown;
        @BindView(R.id.ib_forward)
        ImageView ibForward;
        @BindView(R.id.tv_forward)
        TextView tvForward;
        @BindView(R.id.ib_comment)
        ImageButton ibComment;
        @BindView(R.id.tv_commentCount)
        TextView tvCommentCount;
        @BindView(R.id.ll_forward)
        LinearLayout llForward;

        @BindView(R.id.top_comments)
        LinearLayout topComments;

        public BaseHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}