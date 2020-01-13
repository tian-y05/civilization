package com.wmsj.baselibs.ui.activity

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import com.amap.api.services.help.Inputtips
import com.amap.api.services.help.InputtipsQuery
import com.amap.api.services.help.Tip
import com.wmsj.baselibs.R
import com.wmsj.baselibs.ui.adapter.SearchLocationAdapter
import com.wmsj.baselibs.Const
import com.wmsj.baselibs.base.BaseActivity
import com.wmsj.baselibs.eventBus.EventBusMessage
import com.wmsj.baselibs.eventBus.EventBusUtils
import com.wmsj.baselibs.recyclerview.adapter.OnItemClickListener
import com.wmsj.baselibs.view.RecycleViewDivider
import kotlinx.android.synthetic.main.activity_search_location.*

/**
 * Created by tian
on 2019/8/14.
 */
class SearchLocationActivity : BaseActivity() {

    //搜索列表数据
    private var mSearchList: ArrayList<Tip> = ArrayList()
    private val mSearchLocationAdapter by lazy { SearchLocationAdapter(this, mSearchList, R.layout.activity_location_item) }

    override fun layoutId(): Int {
        return R.layout.activity_search_location
    }

    override fun initData(savedInstanceState: Bundle?) {
        initWhiteActionBar(View.VISIBLE, View.OnClickListener {
            finish()
        }, getString(R.string.search))
    }

    override fun initView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = mSearchLocationAdapter
        recyclerView.addItemDecoration(RecycleViewDivider(
                this, LinearLayoutManager.VERTICAL, 2, resources.getColor(R.color.under_line)))
        et_search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                getInputquery(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        //点击键盘的搜索按钮
        et_search.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                mSearchList.clear()
                mSearchLocationAdapter.notifyDataSetChanged()
                //开始搜索
                val search_string = et_search.text.toString()
                getInputquery(search_string)
                // 当按了搜索之后关闭软键盘
                (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                        currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS
                )
            }
            false
        }

        //.district + mSearchList[position].address
        mSearchLocationAdapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(obj: Any?, position: Int) {
                EventBusUtils.post(EventBusMessage(Const.SEARCH_LOCATION, mSearchList[position]))
                finish()
            }

        })
    }

    override fun start() {
    }

    //输入内容自动提示
    private fun getInputquery(keyWord: String) {
        //第二个参数传入null或者“”代表在全国进行检索，否则按照传入的city进行检索
        val inputquery = InputtipsQuery(keyWord, intent.getStringExtra("city"))
        inputquery.cityLimit = true//限制在当前城市
        val inputTips = Inputtips(this, inputquery)
        inputTips.setInputtipsListener { p0, p1 ->
            mSearchList.clear()
            if (p1 == 1000 && p0.isNotEmpty()) {
                p0.filter { it.point != null || it.poiID.isNotEmpty() }.forEach {
                    mSearchList.add(it)
                }
            }
            mSearchLocationAdapter.setSearchText(keyWord)
            mSearchLocationAdapter.notifyDataSetChanged()
        }
        inputTips.requestInputtipsAsyn()
    }
}