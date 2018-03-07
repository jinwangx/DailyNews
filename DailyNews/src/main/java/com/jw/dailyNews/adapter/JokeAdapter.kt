package com.jw.dailyNews.adapter

import Lib.NewsManager
import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.jw.dailyNews.R
import com.jw.dailyNews.bean.Joke
import com.jw.dailyNews.utils.DateUtils
import com.jw.dailyNews.utils.GlideUtils
import com.jw.dailyNews.wiget.ImageDetailDialog
import com.jw.dailyNews.wiget.ImageLoadingPage
import de.hdodenhof.circleimageview.CircleImageView
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard

class JokeAdapter(context: Context, lists: List<Joke>) : DefaultAdapter<Joke>(context, lists) {

    enum class ITEM_TYPE {
        ITEM_TYPE_TEXT,
        ITEM_TYPE_IMAGE,
        ITEM_TYPE_GIF,
        ITEM_TYPE_VIDEO
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder? {
        return if (viewType == ITEM_TYPE.ITEM_TYPE_IMAGE.ordinal) {
            ImageViewHolder(mInflater.inflate(R.layout.item_joke_image, parent, false))
        } else if (viewType == ITEM_TYPE.ITEM_TYPE_TEXT.ordinal) {
            TextViewHolder(mInflater.inflate(R.layout.item_joke_text, parent, false))
        } else if (viewType == ITEM_TYPE.ITEM_TYPE_GIF.ordinal) {
            GifViewHolder(mInflater.inflate(R.layout.item_joke_gif, parent, false))
        } else if (viewType == ITEM_TYPE.ITEM_TYPE_VIDEO.ordinal) {
            VideoViewHolder(mInflater.inflate(R.layout.item_joke_vidio, parent, false))
        } else {
            null
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val joke = lists[position]
        if (holder is TextViewHolder) {

        } else if (holder is ImageViewHolder) {
            GlideUtils.load(mContext, joke.image!!.big!![0], holder.ivImage)
            holder.btnLarge.setOnClickListener {
                val dialog = ImageDetailDialog(mContext, joke.image!!.big!![0])
                dialog.show()
            }
            (holder as BaseHolder).llForward.setOnClickListener {
                NewsManager.get().showShare(joke.text!!, joke.image!!.thumbnail_small!![0],
                        joke.share_url!!)
            }
        } else if (holder is GifViewHolder) {
            val url = joke.gif!!.images!![0]
            Log.v("gifurl", url)
            val imageLoadingPage = ImageLoadingPage(mContext)
            imageLoadingPage.layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            holder.lll.removeAllViews()
            holder.lll.addView(imageLoadingPage)
            holder.lll.setOnClickListener {
                val dialog = ImageDetailDialog(mContext, joke.gif!!.images!![0])
                dialog.show()
            }
            imageLoadingPage.setImageUrl(url)
            holder.llForward.setOnClickListener { NewsManager.get().showShare(joke.text!!, joke.gif!!.gif_thumbnail!![0], joke.share_url!!) }

        } else if (holder is VideoViewHolder) {
            holder.jcPlayer.setUp(joke.video!!.video!![0], JCVideoPlayer.SCREEN_LAYOUT_LIST, joke.text!!)
            GlideUtils.load(mContext, joke.video!!.thumbnail!![0], holder.jcPlayer.thumbImageView)
            (holder as BaseHolder).llForward.setOnClickListener {
                NewsManager.get().showShare(joke.text!!, joke.video!!.thumbnail!![0],
                        joke.share_url!!)
            }
        }
        GlideUtils.load(mContext, joke.u!!.header!![0], (holder as BaseHolder).ivIconPublisher)
        holder.tvNamePublisher.text = joke.u!!.name
        holder.tvTimePublish.text = DateUtils.fromNow("yyyy-MM-dd HH:mm:ss", joke.passtime)
        holder.tvTitle.text = joke.text
        holder.tvUp.text = joke.up
        holder.tvDown.text = joke.down.toString() + ""
        holder.tvForward.text = joke.forwrard.toString() + ""
        holder.tvCommentCount.text = joke.comment!! + ""
        val top_comments = joke.top_comments
        if (top_comments != null && top_comments.size != 0) {
            holder.topComments.removeAllViews()
            for (comment in top_comments) {
                //行
                val linearLayout = LinearLayout(mContext)
                val layoutParams1 = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                val layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                linearLayout.layoutParams = layoutParams1
                linearLayout.orientation = LinearLayout.HORIZONTAL
                linearLayout.setPadding(5, 2, 5, 2)

                //用户名
                val tv_name = TextView(mContext)
                tv_name.layoutParams = layoutParams
                tv_name.text = comment.u!!.name!! + ": "
                tv_name.setTextColor(Color.parseColor("#ff33b5e5"))

                //评论内容
                val tv_content = TextView(mContext)
                tv_name.layoutParams = layoutParams
                tv_content.text = comment.content
                tv_content.setTextColor(Color.BLACK)

                linearLayout.addView(tv_name)
                linearLayout.addView(tv_content)
                linearLayout.setBackgroundColor(Color.parseColor("#ffd7d7d7"))
                holder.topComments.addView(linearLayout)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        var ordinal = 0
        when (lists[position].type) {
            "text" -> ordinal = ITEM_TYPE.ITEM_TYPE_TEXT.ordinal   //序号
            "image" -> ordinal = ITEM_TYPE.ITEM_TYPE_IMAGE.ordinal
            "gif" -> ordinal = ITEM_TYPE.ITEM_TYPE_GIF.ordinal
            "video" -> ordinal = ITEM_TYPE.ITEM_TYPE_VIDEO.ordinal
        }
        return ordinal
    }

    override fun getItemCount(): Int {
        return if (lists == null) 0 else lists.size
    }


    class TextViewHolder(itemView: View) : BaseHolder(itemView)

    class ImageViewHolder(itemView: View) : BaseHolder(itemView) {
        internal var ivImage = itemView.findViewById<ImageView>(R.id.ivImage)
        internal var btnLarge = itemView.findViewById<TextView>(R.id.tvLarge)
    }

    class GifViewHolder(itemView: View) : BaseHolder(itemView) {
        internal var lll = itemView.findViewById<LinearLayout>(R.id.llGif)
    }

    class VideoViewHolder(itemView: View) : BaseHolder(itemView) {
        var jcPlayer = itemView.findViewById<JCVideoPlayerStandard>(R.id.jcPlayer)
    }

    open class BaseHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var ivIconPublisher = itemView.findViewById<CircleImageView>(R.id.ivIconPublisher)
        internal var tvNamePublisher = itemView.findViewById<TextView>(R.id.tvNamePublisher)
        internal var tvTimePublish = itemView.findViewById<TextView>(R.id.tvTimePublish)
        internal var tvTitle = itemView.findViewById<TextView>(R.id.tvtTitleWcv)

        internal var ibUp = itemView.findViewById<ImageButton>(R.id.ibUp)
        internal var tvUp = itemView.findViewById<TextView>(R.id.tvUp)
        internal var ibDown = itemView.findViewById<ImageButton>(R.id.ibDown)
        internal var tvDown = itemView.findViewById<TextView>(R.id.tvDown)
        internal var ibForward = itemView.findViewById<ImageView>(R.id.ibForward)
        internal var tvForward = itemView.findViewById<TextView>(R.id.tvForward)
        internal var ibComment = itemView.findViewById<ImageButton>(R.id.ibComment)
        internal var tvCommentCount = itemView.findViewById<TextView>(R.id.tvNumComment)
        internal var llForward = itemView.findViewById<LinearLayout>(R.id.llForward)
        internal var topComments = itemView.findViewById<LinearLayout>(R.id.topComments)

        init {
        }
    }

}