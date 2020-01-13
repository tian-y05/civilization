package com.wmsj.baselibs.ui.fragment

import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.wmsj.baselibs.R
import com.wmsj.baselibs.mvp.contract.ConvenientPhotoContract
import com.wmsj.baselibs.mvp.presenter.ConvenientPhotoPresenter
import com.wmsj.baselibs.ui.adapter.MyTakePhotoAdapter
import com.wmsj.baselibs.base.BaseFragment
import com.wmsj.baselibs.bean.ConventientPhotoBean
import com.wmsj.baselibs.bean.HomeBanner
import com.wmsj.baselibs.bean.MyTakePhotoBean
import com.wmsj.baselibs.utils.SPUtil
import com.wmsj.baselibs.utils.ToastUtils
import com.wmsj.baselibs.view.RecycleViewDivider
import com.wmsj.baselibs.view.SetSuccessDialog
import kotlinx.android.synthetic.main.fragment_my_org.*
import java.util.ArrayList
import kotlin.collections.HashMap

/**
 * 我的随手拍列表
on 2019/8/5.
 */
class MyTakePhotoFragment : BaseFragment(), ConvenientPhotoContract.View {


    private var state: Int = -1
    private var photoList = ArrayList<MyTakePhotoBean>()
    private var name: String? = null
    private var page: Int = 1
    private var deletPos: Int = -1
    private var pagesize: Int = 8
    private val mConvenientPhotoAdapter by lazy { activity?.let { MyTakePhotoAdapter(it, photoList, R.layout.convent_photo_item) } }
    private val mPresenter by lazy { ConvenientPhotoPresenter() }

    companion object {
        fun getInstance(name: String, state: Int): MyTakePhotoFragment {
            val fragment = MyTakePhotoFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.name = name
            fragment.state = state
            return fragment
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_my_org
    }

    override fun initView() {
        mPresenter.attachView(this)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = mConvenientPhotoAdapter
        activity?.let {
            recyclerView.addItemDecoration(RecycleViewDivider(
                    it, LinearLayoutManager.VERTICAL, 2, resources.getColor(R.color.under_line)))
        }
        smartRefresh.setOnRefreshListener {
            photoList.clear()
            page = 1
            var map = HashMap<String, String>()
            map.put("page", page.toString())
            map.put("pagesize", pagesize.toString())
            map.put("state", state.toString())
            mPresenter.requestMyCulture(map)
            smartRefresh.setNoMoreData(false)
        }
        smartRefresh.setOnLoadMoreListener {
            page++
            var map = HashMap<String, String>()
            map.put("page", page.toString())
            map.put("pagesize", pagesize.toString())
            mPresenter.requestMyCulture(map)
        }
        mConvenientPhotoAdapter?.setOnItemDeleteListener(object : MyTakePhotoAdapter.onItemDeleteListener {
            override fun onDelete(position: Int) {
                activity?.let {
                    SetSuccessDialog.Builder(it)
                            .setTitle(getString(R.string.tips))
                            .setMessage(getString(R.string.sure_to_delete))
                            .setPositiveButton(getString(R.string.cancel), DialogInterface.OnClickListener { p0, p1 ->
                                p0.dismiss()
                            })
                            .setNegativeButton(getString(R.string.sure), DialogInterface.OnClickListener { p0, p1 ->
                                p0.dismiss()
                                var map = HashMap<String, String>()
                                map.put("id", photoList[position].id.toString())
                                map.put("api-token", SPUtil.get(activity, "userId", "").toString())
                                deletPos = position
                                mPresenter.deleteMyCulture(map)
                            })
                            .setWith(0.77f)
                            .create()
                            .show()
                }

            }

        })
    }

    override fun lazyLoad() {
        smartRefresh.autoRefresh()
    }


    override fun showLoading() {
        loadingDialog.show()
    }

    override fun dismissLoading() {
        loadingDialog.dismiss()
        smartRefresh.finishRefresh()
        smartRefresh.finishLoadMore()
    }

    override fun setBannerData(data: List<HomeBanner>) {
    }

    override fun setModelsData(data: List<ConventientPhotoBean>) {
    }

    override fun showError(msg: String, errorCode: Int) {
        ToastUtils.showShort(activity, msg)
    }

    override fun setMyCulture(data: List<MyTakePhotoBean>) {
        photoList.addAll(data)
        mConvenientPhotoAdapter?.notifyDataSetChanged()
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

    override fun deleteCultureResult(data: String) {
        ToastUtils.showShort(activity, data)
        photoList.removeAt(deletPos)
        mConvenientPhotoAdapter?.notifyItemRemoved(deletPos)
        mConvenientPhotoAdapter?.notifyItemRangeChanged(deletPos, photoList.size - deletPos)
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
}