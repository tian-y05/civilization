package com.wmsj.baselibs.ui.adapter

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import cn.jzvd.JzvdStd
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.wmsj.baselibs.R
import com.wmsj.baselibs.ui.activity.PhotoShowActivity
import com.wmsj.baselibs.ui.activity.WebViewDetailActivity
import com.wmsj.baselibs.Const
import com.wmsj.baselibs.bean.ConventientPhotoBean
import com.wmsj.baselibs.bean.HomeBanner
import com.wmsj.baselibs.recyclerview.RecyclerViewHolder
import com.wmsj.baselibs.recyclerview.adapter.CommonRecyclerAdapter
import com.wmsj.baselibs.utils.IntentUtils
import com.wmsj.baselibs.utils.StringUtils
import com.wmsj.baselibs.view.MyGridView
import com.youth.banner.Banner
import com.youth.banner.BannerConfig
import com.youth.banner.loader.ImageLoader

/**
 * Created by tian
on 2019/8/12.
 */
class ConvenientPhotoAdapter(mContext: Context, photoList: ArrayList<ConventientPhotoBean>) : CommonRecyclerAdapter<ConventientPhotoBean>(mContext, photoList, -1) {

    companion object {
        private const val ITEM_TYPE_NORMAL = 1
        private const val ITEM_TYPE_HEAD = 2
    }

    private var isHeadView = false
    private var bannerLists = ArrayList<String>()
    private var bannerAllLists = ArrayList<HomeBanner>()

    override fun bindData(holder: RecyclerViewHolder, data: ConventientPhotoBean, position: Int) {
        when (getItemViewType(position)) {
            ITEM_TYPE_HEAD -> {
                var banner = holder.getView<Banner>(R.id.convenient_banner)
                banner.isAutoPlay(true)
                banner.setDelayTime(3000)
                banner.setImageLoader(MyImageLoader)
                banner.setIndicatorGravity(BannerConfig.CENTER)
                if (bannerLists != null && bannerLists.size > 1)
                    banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
                else
                    banner.setBannerStyle(BannerConfig.NOT_INDICATOR)
                banner.setImages(bannerLists).setOnBannerListener { position ->
                    if (bannerAllLists[position].ad_url != null && !TextUtils.isEmpty(bannerAllLists[position].ad_url)) {
                        var bundle = Bundle()
                        bundle.putString("type", Const.BANNER)
                        bundle.putString("url", bannerAllLists[position].ad_url)
                        IntentUtils.to(mContext, WebViewDetailActivity::class.java, bundle)
                    }
                }.start()
            }
            ITEM_TYPE_NORMAL -> {
                holder.setImagePath(R.id.iv_logo, object : RecyclerViewHolder.HolderImageLoader(Const.BASE_URL + data.user_logo) {
                    override fun loadImage(iv: ImageView, path: String) {
                        var requestOptions = RequestOptions()
                        requestOptions.error(R.mipmap.author_default).placeholder(R.mipmap.author_default)
                        Glide.with(mContext)
                                .load(path)
                                .apply(requestOptions)
                                .into(iv)
                    }
                })
                data.username?.let { holder.setText(R.id.tv_author, it) }
                data.cul_content?.let { holder.setText(R.id.tv_content, it) }
                if (data.cul_address != null && !TextUtils.isEmpty(data.cul_address)) {
                    holder.setViewVisibility(R.id.tv_address, View.VISIBLE)
                    holder.setText(R.id.tv_address, data.cul_address!!)
                }
                data.cul_createtime?.let { holder.setText(R.id.tv_time, it) }
                if (data.cul_video != null && !TextUtils.isEmpty(data.cul_video)) {
                    holder.setViewVisibility(R.id.gv_photos, View.GONE)
                    holder.setViewVisibility(R.id.jz_video, View.VISIBLE)
                    holder.getView<JzvdStd>(R.id.jz_video).setUp(Const.BASE_URL + data.cul_video, "", JzvdStd.SCREEN_NORMAL)
                    Glide.with(mContext).load(Const.BASE_URL + data.cover).into(holder.getView<JzvdStd>(R.id.jz_video).thumbImageView)
                } else {
                    data.cul_images?.let {
                        holder.setViewVisibility(R.id.jz_video, View.GONE)
                        var photos = StringUtils.split(it, ",")
                        holder.setViewVisibility(R.id.gv_photos, View.VISIBLE)
                        holder.getView<MyGridView>(R.id.gv_photos).adapter = PhotoModelsAdapter(mContext, photos)
                        holder.getView<MyGridView>(R.id.gv_photos).setOnItemClickListener { parent, view, position, id ->
                            var bundle = Bundle()
                            bundle.putStringArray("photos", photos)
                            IntentUtils.to(mContext, PhotoShowActivity::class.java, bundle)
                        }
                    }

                }
            }
        }


    }

    override fun getItemViewType(position: Int): Int {
        return if (isHeadView && position == 0)
            ITEM_TYPE_HEAD
        else
            ITEM_TYPE_NORMAL
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        return when (viewType) {
            ITEM_TYPE_HEAD ->
                RecyclerViewHolder(inflaterView(R.layout.convenient_photo_header, parent))
            else ->
                RecyclerViewHolder(inflaterView(R.layout.convent_photo_item, parent))

        }

    }

    fun addHeadView(bannerLists: ArrayList<String>, data: List<HomeBanner>) {
        isHeadView = true
        this.bannerLists = bannerLists
        this.bannerAllLists = data as ArrayList<HomeBanner>
        mData.add(0, ConventientPhotoBean(-1))
        notifyDataSetChanged()
    }

    /**
     * 图片加载类
     */
    object MyImageLoader : ImageLoader() {
        override fun displayImage(context: Context?, path: Any?, imageView: ImageView?) {
            Glide.with(context!!.applicationContext).load(path).into(imageView!!)
        }
    }
}