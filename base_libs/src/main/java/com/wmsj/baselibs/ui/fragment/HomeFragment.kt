package com.wmsj.baselibs.ui.fragment

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.View
import android.widget.GridView
import android.widget.ImageView
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import com.wmsj.baselibs.Const
import com.wmsj.baselibs.R
import com.wmsj.baselibs.mvp.contract.HomeContract
import com.wmsj.baselibs.mvp.presenter.HomePresenter
import com.wmsj.baselibs.ui.adapter.HomeActivityListAdapter
import com.wmsj.baselibs.ui.adapter.HomeModelsAdapter
import com.wmsj.baselibs.base.BaseFragment
import com.wmsj.baselibs.bean.*
import com.wmsj.baselibs.eventBus.EventBusMessage
import com.wmsj.baselibs.recyclerview.adapter.OnItemClickListener
import com.wmsj.baselibs.ui.activity.*
import com.wmsj.baselibs.utils.*
import com.youth.banner.BannerConfig
import com.youth.banner.loader.ImageLoader
import kotlinx.android.synthetic.main.fragment_message.*
import java.util.*

/**
 * 首页
 * Created by tian
on 2019/7/16.
 */
class HomeFragment : BaseFragment(), HomeContract.View {

    private var mTitle: String? = null
    private var NUM = 4 // 每行显示个数
    private var itemList = ArrayList<HomeColumnsBean>()
    private var bannerList = ArrayList<HomeBanner>()
    private var bannerImageList = ArrayList<String>()
    private var newsActivityLists = ArrayList<HomeActivityListBean>()
    private val mModelsAdapter by lazy { activity?.let { HomeModelsAdapter(it, itemList) } }
    private val mHomeActivityListAdapter by lazy { activity?.let { HomeActivityListAdapter(it, newsActivityLists, R.layout.fragment_home_news) } }
    private val mPresenter by lazy { HomePresenter() }

    companion object {
        fun getInstance(title: String): HomeFragment {
            val fragment = HomeFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.mTitle = title
            return fragment
        }
    }


    override fun getLayoutId(): Int {
        return R.layout.fragment_message
    }

    override fun initView() {
        mPresenter.attachView(this)
        home_recyclerview.layoutManager = LinearLayoutManager(activity)
        home_recyclerview.adapter = mHomeActivityListAdapter
        rl_more.setOnClickListener {
            activity?.let { IntentUtils.to(it, EventsManagementActivity::class.java) }
        }
        mHomeActivityListAdapter?.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(obj: Any?, position: Int) {
                var bundle = Bundle()
                bundle.putString("id", newsActivityLists[position].aid)
                activity?.let { IntentUtils.to(it, EventsDetailsActivity::class.java, bundle) }
            }
        })

        smartRefresh.setOnRefreshListener {
            newsActivityLists.clear()
            mPresenter.requestActivityData(1)
        }
    }


    /**
     * 需要根据具体数据展示GridView列数
     */
    private fun initGridView() {
        var count = itemList.size
        var columns = if (count % 2 == 0) count / 2 else count / 2 + 1
        if (count <= 8) {
            columns = NUM
        }

        var params = LinearLayout.LayoutParams(columns * (DisplayManager.getScreenWidth()!!) / NUM,
                LinearLayout.LayoutParams.WRAP_CONTENT)
        gv_model.layoutParams = params
        gv_model.columnWidth = DisplayManager.getScreenWidth()!! / NUM
        gv_model.stretchMode = GridView.NO_STRETCH
        if (count <= 8) {
            gv_model.numColumns = NUM
        } else {
            gv_model.numColumns = columns
        }
        gv_model.adapter = mModelsAdapter
        gv_model.setOnItemClickListener { parent, view, position, id ->

            var bundle = Bundle()
            bundle.putInt("key", itemList[position].id)
            bundle.putString("name", if (TextUtils.isEmpty(itemList[position].name)) itemList[position].app_name else itemList[position].name)
            when (itemList[position].app_package) {
                Const.ARTICLE -> {//多元
                    if (itemList[position].name == "志愿服务") {
                        bundle.putString("type", "polymerize")
                        bundle.putStringArray("title", arrayOf("组织列表", "活动列表"))
                        activity?.let { IntentUtils.to(it, ArticleSortActivity::class.java, bundle) }
                    } else {
                        activity?.let { IntentUtils.to(it, ArticleListActivity::class.java, bundle) }
                    }

                }
                Const.PLATFORM -> {//平台展示
                    activity?.let { IntentUtils.to(it, PlatformShowActivity::class.java, bundle) }
                }
                Const.PRACTICE -> {//实践基地
                    activity?.let { IntentUtils.to(it, PracticeListActivity::class.java, bundle) }
                }
                Const.STATION -> {//所站风貌
                    bundle.putString("type", "station")
                    bundle.putStringArray("title", arrayOf("所站简介", "所站动态"))
                    activity?.let { IntentUtils.to(it, ArticleSortActivity::class.java, bundle) }
                }
                Const.POLYMERIZE -> {//志愿服务
                    bundle.putString("type", "polymerize")
                    bundle.putStringArray("title", arrayOf("组织列表", "活动列表"))
                    activity?.let { IntentUtils.to(it, ArticleSortActivity::class.java, bundle) }
                }
                Const.PATTERN -> {//好人榜
                    bundle.putString("type", Const.PATTERN)
                    activity?.let { IntentUtils.to(it, WebViewDetailActivity::class.java, bundle) }
                }
                Const.CULTURE -> { //文明随手拍
                    activity?.let { IntentUtils.to(it, ConvenientPhotoActivity::class.java, bundle) }
                }

            }
        }
    }

    override fun lazyLoad() {
        mPresenter.requestBannerData()
        mPresenter.requestColumnsData(0)

    }


    override fun showLoading() {
        loadingDialog?.show()
    }

    override fun dismissLoading() {
        loadingDialog?.dismiss()
        smartRefresh.finishRefresh()
    }

    override fun setBannerData(data: List<HomeBanner>) {
        bannerList.addAll(data)
        for (homeBanner in data) {
            bannerImageList.add(Const.BASE_URL + homeBanner.ad_image)
        }
        banner.isAutoPlay(true)
        banner.setDelayTime(3000)
        banner.setImageLoader(MyImageLoader)
        banner.setIndicatorGravity(BannerConfig.CENTER)
        if (bannerImageList != null && bannerImageList.size > 1)
            banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
        else
            banner.setBannerStyle(BannerConfig.NOT_INDICATOR)
        banner.setImages(bannerImageList).setOnBannerListener { position ->
            if (bannerList[position].ad_url != null && !TextUtils.isEmpty(bannerList[position].ad_url)) {
                var bundle = Bundle()
                bundle.putString("type", Const.BANNER)
                bundle.putString("url", bannerList[position].ad_url)
                activity?.let { IntentUtils.to(it, WebViewDetailActivity::class.java, bundle) }
            }

        }.start()

    }

    override fun setColumnsData(data: List<HomeColumnsBean>) {
        itemList.add(HomeColumnsBean("", "平台展示", "platform"))
        itemList.addAll(data)
        mPresenter.requestAppsData()
    }

    override fun setAppsData(data: List<HomeColumnsBean>) {
        itemList.addAll(data)
        initGridView()
        mModelsAdapter?.notifyDataSetChanged()
        mPresenter.requestActivityData(1)
    }

    override fun setActivityData(data: List<HomeActivityListBean>) {
        loadingDialog?.dismiss()
        newsActivityLists.addAll(data)
        mHomeActivityListAdapter!!.notifyDataSetChanged()
        smartRefresh.finishRefresh()
        getBaseInfo()
    }

    override fun getBaseInfoResult(data: List<BaseInfoBean>, type: String) {
        SPUtil.put(activity, type, Gson().toJson(data))
    }

    override fun showError(msg: String, errorCode: Int) {
        activity!!.let { ToastUtils.showShort(it, msg) }
    }

    /**
     * 图片加载类
     */
    object MyImageLoader : ImageLoader() {
        override fun displayImage(context: Context?, path: Any?, imageView: ImageView?) {
            Glide.with(context!!.applicationContext).load(path).into(imageView!!)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }

    override fun onReceiveEvent(event: EventBusMessage<Any>) {
        when (event.code) {
            Const.ZYZ_INFO -> {
                rl_detail.visibility = View.VISIBLE
                var bean = event.data as UserCentreBean
                L.d("UserCentreBean:" + bean.toString())
                var requestOptions = RequestOptions()
                requestOptions.error(R.mipmap.author_default).placeholder(R.mipmap.author_default)
                activity?.let { Glide.with(it).load(Const.BASE_URL + bean.logo).apply(requestOptions).apply(RequestOptions.bitmapTransform(CircleCrop())).into(iv_logo) }
                tv_user_name.text = bean.name
                tv_org_num.text = "NO." + bean.volunteer_number
                if (StringUtils.isEmpty(bean.service_count)) {
                    tv_service.text = "0小时"
                } else {
                    tv_service.text = bean.service_count + "小时"
                }
                rating_bar.setStar(bean.service_star.toFloat())

            }
            Const.LOGIN_ORG_SUCCESS, Const.LOGIN_EXIT -> {
                rl_detail.visibility = View.GONE
            }

        }

    }


    private fun getBaseInfo() {
        mPresenter.getBaseInfo("ZJLX")
        mPresenter.getBaseInfo("ZZMM")
        mPresenter.getBaseInfo("GJ")
        mPresenter.getBaseInfo("MZ")
        mPresenter.getBaseInfo("WHCD")
        mPresenter.getBaseInfo("FWLY")
        mPresenter.getBaseInfo("FWDX")
        mPresenter.getBaseInfo("GRJN")
        mPresenter.getBaseInfo("ZYZLB")
        mPresenter.getBaseInfo("YXFWSJ")
        mPresenter.getBaseInfo("ZYZZLX")
        mPresenter.getBaseInfo("DWXZ")
        mPresenter.getBaseInfo("ZZLX")
        mPresenter.getBaseInfo("GKFW")
    }
}