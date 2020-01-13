package com.wmsj.baselibs.ui.activity

import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import cn.jzvd.Jzvd
import cn.jzvd.JzvdStd
import com.wmsj.baselibs.R
import com.wmsj.baselibs.mvp.contract.ConvenientPhotoContract
import com.wmsj.baselibs.mvp.presenter.ConvenientPhotoPresenter
import com.wmsj.baselibs.ui.adapter.ConvenientPhotoAdapter
import com.wmsj.baselibs.weight.TakePhotoDialog
import com.wmsj.baselibs.Const
import com.wmsj.baselibs.base.BaseActivity
import com.wmsj.baselibs.bean.ConventientPhotoBean
import com.wmsj.baselibs.bean.HomeBanner
import com.wmsj.baselibs.bean.MyTakePhotoBean
import com.wmsj.baselibs.eventBus.EventBusMessage
import com.wmsj.baselibs.eventBus.EventBusUtils
import com.wmsj.baselibs.utils.IntentUtils
import com.wmsj.baselibs.utils.SPUtil
import com.wmsj.baselibs.view.RecycleViewDivider
import com.wmsj.baselibs.view.SetSuccessDialog
import kotlinx.android.synthetic.main.activity_convenient_photo.*
import java.util.*

/**
 * 文明随手拍
on 2019/8/12.
 */
class ConvenientPhotoActivity : BaseActivity(), ConvenientPhotoContract.View {


    private var photoList = ArrayList<ConventientPhotoBean>()
    private var bannerImageList = ArrayList<String>()
    private var name: String? = null
    private var page: Int = 1
    private var pagesize: Int = 8
    private val mConvenientPhotoAdapter by lazy { this?.let { ConvenientPhotoAdapter(it, photoList) } }
    private val mPresenter by lazy { ConvenientPhotoPresenter() }

    override fun layoutId(): Int {
        return R.layout.activity_convenient_photo
    }

    override fun initData(savedInstanceState: Bundle?) {
        name = intent.getStringExtra("name")
    }

    override fun initView() {
        mPresenter.attachView(this)
        initWhiteActionBar(View.VISIBLE, View.OnClickListener {
            finish()
        }, name)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = mConvenientPhotoAdapter
        recyclerView.addItemDecoration(RecycleViewDivider(
                this, LinearLayoutManager.VERTICAL, 2, resources.getColor(R.color.under_line)))
        smartRefresh.setOnRefreshListener {
            photoList.clear()
            page = 1
            mPresenter.requestBannerData()
            var map = HashMap<String, String>()
            map.put("page", page.toString())
            map.put("pagesize", "8")
            mPresenter.requestModelsData(map)
            smartRefresh.setNoMoreData(false)
        }
        smartRefresh.setOnLoadMoreListener {
            page++
            var map = HashMap<String, String>()
            map.put("page", page.toString())
            map.put("pagesize", "8")
            mPresenter.requestModelsData(map)
        }

        if (SPUtil.get(this, "role", "") == "org") {
            fab_button.visibility = View.GONE
        }

        fab_button.setOnClickListener {
            if (SPUtil.get(this, "isVolLogin", false) as Boolean || SPUtil.get(this, "isOrgLogin", false) as Boolean) {
                var takePhotoDialog = TakePhotoDialog(this, R.style.dialog)
                takePhotoDialog.setOnItemClickListener(object : TakePhotoDialog.onItemClickListener {
                    override fun onPhotoClick() {
                        takePhotoDialog.dismiss()
                        var bundle = Bundle()
                        bundle.putBoolean("isVideo", false)
                        IntentUtils.to(this@ConvenientPhotoActivity, TakePhotoOrVideoActivity::class.java, bundle)
                    }

                    override fun onVideoClick() {
                        takePhotoDialog.dismiss()
                        var bundle = Bundle()
                        bundle.putBoolean("isVideo", true)
                        IntentUtils.to(this@ConvenientPhotoActivity, TakePhotoOrVideoActivity::class.java, bundle)
                    }

                })
                takePhotoDialog.show()
            } else {
                SetSuccessDialog.Builder(this)
                        .setTitle(getString(R.string.tips))
                        .setMessage(getString(R.string.sure_to_login))
                        .setPositiveButton(getString(R.string.cancel), DialogInterface.OnClickListener { p0, p1 ->
                            p0.dismiss()
                        })
                        .setNegativeButton(getString(R.string.sure), DialogInterface.OnClickListener { p0, p1 ->
                            p0.dismiss()
                            EventBusUtils.post(EventBusMessage(Const.GO_LOGIN_VOL, ""))
                            finish()
                        })
                        .setWith(0.77f)
                        .create()
                        .show()
            }

        }
    }

    override fun start() {
        smartRefresh.autoRefresh()
    }


    override fun showLoading() {
    }

    override fun dismissLoading() {
        smartRefresh.finishRefresh()
        smartRefresh.finishLoadMore()
    }

    override fun setBannerData(data: List<HomeBanner>) {
        for (homeBanner in data) {
            bannerImageList.add(Const.BASE_URL + homeBanner.ad_image)
        }
        mConvenientPhotoAdapter.addHeadView(bannerImageList, data)
    }

    override fun setModelsData(data: List<ConventientPhotoBean>) {
        photoList.addAll(data)
        mConvenientPhotoAdapter.notifyDataSetChanged()
        if (page == 1) {
            smartRefresh.finishRefresh()
        } else {
            if ((data != null && data.isEmpty())) {
                smartRefresh.finishLoadMoreWithNoMoreData()
            } else if (data != null && data.size < pagesize) {
                smartRefresh.finishLoadMore()
            }
        }
    }

    override fun showError(msg: String, errorCode: Int) {
    }


    override fun onPause() {
        super.onPause()
        JzvdStd.goOnPlayOnPause()
    }

    override fun onResume() {
        super.onResume()
        JzvdStd.goOnPlayOnResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        Jzvd.releaseAllVideos()
        mPresenter.detachView()
    }

    override fun onBackPressed() {
        if (Jzvd.backPress()) {
            return
        }
        super.onBackPressed()

    }

    override fun setMyCulture(data: List<MyTakePhotoBean>) {
    }

    override fun deleteCultureResult(data: String) {
    }

}