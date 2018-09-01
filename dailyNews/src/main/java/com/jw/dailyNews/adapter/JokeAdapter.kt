package com.jw.dailyNews.adapter

import Lib.NewsManager
import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.android.databinding.library.baseAdapters.BR
import com.jw.dailyNews.R
import com.jw.dailyNews.bean.Joke
import com.jw.dailyNews.databinding.ItemJokeGifBinding
import com.jw.dailyNews.databinding.ItemJokeImageBinding
import com.jw.dailyNews.databinding.ItemJokeTextBinding
import com.jw.dailyNews.databinding.ItemJokeVidioBinding
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
        var binding:ViewDataBinding?=null
        //评论内容
        when (holder) {
            is TextViewHolder -> binding= DataBindingUtil.bind<ItemJokeTextBinding>(holder.itemView)!!
            is ImageViewHolder -> {
                binding= DataBindingUtil.bind<ItemJokeImageBinding>(holder.itemView)!!
                binding.setClickListener {
                    when(it.id){
                        R.id.tvLarge->{
                            val dialog = ImageDetailDialog(mContext, joke.image!!.big!![0])
                            dialog.show()
                        }
                        R.id.llForward-> NewsManager.get().showShare(joke.text!!, joke.image!!.thumbnail_small!![0], joke.share_url!!)
                    }
                }
            }
            is GifViewHolder -> {
                binding= DataBindingUtil.bind<ItemJokeGifBinding>(holder.itemView)!!
                val url = joke.gif!!.images!![0]
                val imageLoadingPage = ImageLoadingPage(mContext)
                imageLoadingPage.layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                binding.llGif.removeAllViews()
                binding.llGif.addView(imageLoadingPage)
                binding.setClickListener {
                    when(it.id){
                        R.id.llGif->{
                            val dialog = ImageDetailDialog(mContext, joke.gif!!.images!![0])
                            dialog.show()
                        }
                        R.id.llForward->NewsManager.get().showShare(joke.text!!, joke.gif!!.gif_thumbnail!![0], joke.share_url!!)
                    }
                }
                imageLoadingPage.setImageUrl(url)

            }
            is VideoViewHolder -> {
                binding= DataBindingUtil.bind<ItemJokeVidioBinding>(holder.itemView)!!
                binding.jcPlayer.setUp(joke.video!!.video!![0], JCVideoPlayer.SCREEN_LAYOUT_LIST, joke.text!!)
                binding.setClickListener {
                    when(it.id){
                        R.id.llForward->NewsManager.get().showShare(joke.text!!, joke.video!!.thumbnail!![0],joke.share_url!!)
                    }
                }
                GlideUtils.load(mContext, joke.video!!.thumbnail!![0], binding.jcPlayer.thumbImageView)
            }
        }
        binding!!.setVariable(BR.joke,joke)
        addComment(joke,holder,binding)
    }

    private fun addComment(joke:Joke,holder:RecyclerView.ViewHolder,binding:ViewDataBinding){
        val top_comments = joke.top_comments
        if (top_comments != null && top_comments.size != 0) {
            if(holder is TextViewHolder )
                (binding as ItemJokeTextBinding).bottom!!.topComments.removeAllViews()
            else if(holder is GifViewHolder)
                (binding as ItemJokeGifBinding).bottom!!.topComments.removeAllViews()
            else if(holder is VideoViewHolder)
                (binding as ItemJokeVidioBinding).bottom!!.topComments.removeAllViews()
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

                if(holder is TextViewHolder )
                    (binding as ItemJokeTextBinding).bottom!!.topComments.addView(linearLayout)
                else if(holder is GifViewHolder)
                    (binding as ItemJokeGifBinding).bottom!!.topComments.addView(linearLayout)
                else if(holder is VideoViewHolder)
                    (binding as ItemJokeVidioBinding).bottom!!.topComments.addView(linearLayout)
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

    }

    class GifViewHolder(itemView: View) : BaseHolder(itemView) {
    }

    class VideoViewHolder(itemView: View) : BaseHolder(itemView) {
    }

    open class BaseHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }

}