package com.wmsj.baselibs.filterview

import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewTreeObserver
import android.widget.LinearLayout
import com.wmsj.baselibs.R
import com.wmsj.baselibs.ui.adapter.FilterCateLeftListAdapter
import com.wmsj.baselibs.ui.adapter.FilterCateRightListAdapter
import com.wmsj.baselibs.ui.adapter.FilterCateTitleListAdapter
import com.wmsj.baselibs.bean.OrgCataBean
import com.wmsj.baselibs.bean.OrgSon
import com.wmsj.baselibs.bean.OrgSonList
import com.wmsj.baselibs.recyclerview.adapter.OnItemClickListener
import com.wmsj.baselibs.utils.StringUtils
import com.wmsj.baselibs.view.RecycleViewDivider
import kotlinx.android.synthetic.main.view_filter_layout.view.*


/**
 * Created by sunfusheng on 16/4/20.
 */
class FilterView : LinearLayout, View.OnClickListener {


    private var mContext: Context? = null
    private var mActivity: Activity? = null
    private var selectId = -1
    private var selectLastId = -1
    private var lastFilterPosition = -1
    private var sonPosition = 0
    private var panelHeight: Int = 0
    private var filterData = ArrayList<OrgCataBean>()
    private var filterOrgSon = ArrayList<OrgSon>()
    private var filterOrgSonList = ArrayList<OrgSonList>()
    private var filterSelect = ArrayList<Int>()

    private val orgSonAll by lazy { OrgSonList(-1, "全部", -1, -1, -1, -1, -1) }

    private val rvTitleAdapter by lazy { mContext?.let { filterData?.let { data -> FilterCateTitleListAdapter(it, data, R.layout.filter_cate_item) } } }
    private val rvLeftAdapter by lazy { mContext?.let { filterOrgSon?.let { data -> FilterCateLeftListAdapter(it, data) } } }
    private val rvRightAdapter by lazy { mContext?.let { filterOrgSonList?.let { data -> FilterCateRightListAdapter(it, data) } } }

    private var isShowing = false

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    private fun init(context: Context) {
        this.mContext = context
        LayoutInflater.from(context).inflate(R.layout.view_filter_layout, this)
        initListener()
    }

    private fun initListener() {
        view_mask_bg.setOnClickListener(this)
        ll_content_list_view.setOnTouchListener { _, _ -> true }
    }

    private fun initView() {
        view_mask_bg.visibility = View.GONE
        ll_content_list_view.visibility = View.GONE
        rv_title.layoutManager = filterData?.size?.let { GridLayoutManager(mContext, it) }
        mContext?.let {
            RecycleViewDivider(
                    it, LinearLayoutManager.HORIZONTAL, 2, resources.getColor(R.color.under_line))
        }?.let { rv_title.addItemDecoration(it) }
        rv_left.layoutManager = LinearLayoutManager(mContext)
        rv_right.layoutManager = LinearLayoutManager(mContext)
    }

    fun setFilterData(activity: FragmentActivity?, data: ArrayList<OrgCataBean>) {
        this.mActivity = activity
        this.filterData = data
        initView()
        setCategoryAdapter()
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.view_mask_bg -> {
                hide()
            }
        }
    }


    // 筛选视图点击
    private var onFilterClickListener: OnFilterClickListener? = null

    fun setOnFilterClickListener(onFilterClickListener: OnFilterClickListener) {
        this.onFilterClickListener = onFilterClickListener
    }

    interface OnFilterClickListener {
        fun onFilterClick(select: String)
    }

    // 动画显示
    fun show(position: Int) {
        if (isShowing && lastFilterPosition == position) {
            hide()
            return
        } else if (!isShowing) {
            view_mask_bg.visibility = View.VISIBLE
            ll_content_list_view.visibility = View.VISIBLE
            ll_content_list_view.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    ll_content_list_view.viewTreeObserver.removeGlobalOnLayoutListener(this)
                    panelHeight = ll_content_list_view.height
                    ObjectAnimator.ofFloat(ll_content_list_view, "translationY", -panelHeight.toFloat(), 0f).setDuration(200).start()
                }
            })
        }
        isShowing = true
        lastFilterPosition = position
        filterData?.let {
            filterOrgSon.addAll(it[lastFilterPosition].son)
            rvLeftAdapter?.setCurrentSelect(it[lastFilterPosition].select_id)
        }

        filterOrgSon?.let {
            selectLastId = it[filterData[lastFilterPosition].select_id].id
            filterOrgSonList.add(orgSonAll)
            filterOrgSonList.addAll(it[filterData[lastFilterPosition].select_id].last)
            rvRightAdapter?.setCurrentSelect(filterData[lastFilterPosition].select_lastid)
        }
        rv_left.visibility = View.VISIBLE
        rv_right.visibility = View.VISIBLE

    }

    private fun setCategoryAdapter() {

        rv_title.adapter = rvTitleAdapter
        rv_left.adapter = rvLeftAdapter
        rv_right.adapter = rvRightAdapter

        rvTitleAdapter?.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(obj: Any?, position: Int) {
                filterSelect.clear()
                filterOrgSon.clear()
                filterOrgSonList.clear()
                if (lastFilterPosition != position) {
                    selectLastId = -1
                    selectId = -1
                    sonPosition = 0
                }
                show(position)
            }

        })

        rvLeftAdapter?.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(obj: Any?, position: Int) {
                filterOrgSonList.clear()
                sonPosition = position
                filterOrgSon?.let {
                    filterOrgSonList.add(orgSonAll)
                    filterOrgSonList.addAll(it[position].last)
                    rvLeftAdapter?.setCurrentSelect(position)
                    filterData[lastFilterPosition].select_name = it[position].cate_name
                }
                selectLastId = filterOrgSon[position].id

                if(filterData[lastFilterPosition].select_id == sonPosition){
                    rvRightAdapter!!.setCurrentSelect(filterData[lastFilterPosition].select_lastid)
                }else{
                    rvRightAdapter!!.setCurrentSelect(0)
                }
            }

        })

        rvRightAdapter?.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(obj: Any?, position: Int) {
                selectId = filterOrgSonList[position].id
                filterData[lastFilterPosition].select_id = sonPosition
                filterData[lastFilterPosition].select_lastid = position
                if (selectId != 0) {
                    filterData[lastFilterPosition].select_name = filterOrgSonList[position].cate_name
                }
                rvTitleAdapter!!.notifyItemChanged(lastFilterPosition)
                filterSelect.add(filterData[lastFilterPosition].id)
                filterSelect.add(selectLastId)
                filterSelect.add(selectId)
                var stringSelect = StringUtils.stringBufferSpell(filterSelect)

                if (onFilterClickListener != null) {
                    onFilterClickListener!!.onFilterClick(stringSelect.toString())
                }
                hide()
            }

        })

    }

    // 隐藏动画
    fun hide() {
        isShowing = false
        view_mask_bg.visibility = View.GONE
        ObjectAnimator.ofFloat(ll_content_list_view, "translationY", 0f, -panelHeight.toFloat()).setDuration(200).start()
        ll_content_list_view.visibility = View.GONE
    }
}
