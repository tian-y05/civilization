package com.wmsj.baselibs.ui.activity

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.app.FragmentTransaction
import com.flyco.tablayout.listener.CustomTabEntity
import com.flyco.tablayout.listener.OnTabSelectListener
import com.google.gson.Gson
import com.wmsj.baselibs.Const
import com.wmsj.baselibs.R
import com.wmsj.baselibs.mvp.contract.MainContract
import com.wmsj.baselibs.mvp.presenter.MainPresenter
import com.wmsj.baselibs.ui.fragment.*
import com.wmsj.baselibs.update.DownLoadBuilder
import com.wmsj.baselibs.update.UpdateActivity
import com.wmsj.baselibs.base.BaseActivity
import com.wmsj.baselibs.bean.BaseInfoBean
import com.wmsj.baselibs.bean.TabEntity
import com.wmsj.baselibs.bean.VersionBean
import com.wmsj.baselibs.eventBus.EventBusMessage
import com.wmsj.baselibs.utils.*
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : BaseActivity(), MainContract.View {

    private val mTitles = arrayOf("首页", "志愿者", "志愿组织")

    // 未被选中的图标
    private val mIconUnSelectIds = intArrayOf(R.mipmap.main_unselect, R.mipmap.volunteer_unselect, R.mipmap.organization_unselect)
    // 被选中的图标
    private val mIconSelectIds = intArrayOf(R.mipmap.main_select, R.mipmap.volunteer_select, R.mipmap.organization_select)
    //默认为0
    private var mIndex = 0

    private val mTabEntities = ArrayList<CustomTabEntity>()

    private var mHomeFragment: HomeFragment? = null
    private var mVolunteerFragment: VolunteerFragment? = null
    private var mLoginFragment: LoginFragment? = null
    private var mLoginOrgFragment: LoginOrgFragment? = null
    private var mVoluntaryOrganizationFragment: VoluntaryOrganizationFragment? = null
    private val mPresenter by lazy { MainPresenter() }
    private var downloadCompleteBroadcast: DownloadCompleteBroadcast? = null

    override fun layoutId(): Int {
        return R.layout.activity_main
    }

    override fun initData(savedInstanceState: Bundle?) {
        mPresenter.attachView(this)
    }


    override fun initView() {
        initWhiteActionBar(-1, null, mTitles[0])
        initTab()
        tab_layout.currentTab = mIndex
        switchFragment(mIndex)

        downloadCompleteBroadcast = DownloadCompleteBroadcast()
        val intentFilter = IntentFilter()
        intentFilter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        registerReceiver(downloadCompleteBroadcast, intentFilter)
    }

    override fun start() {
        mPresenter.getClientsAuth()
        mPresenter.getVersion()
//        IntentUtils.to(this, UpdateActivity::class.java)
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


    //初始化底部菜单
    private fun initTab() {
        (0 until mTitles.size)
                .mapTo(mTabEntities) { TabEntity(mTitles[it], mIconSelectIds[it], mIconUnSelectIds[it]) }
        //为Tab赋值
        tab_layout.setTabData(mTabEntities)
        tab_layout.setOnTabSelectListener(object : OnTabSelectListener {
            override fun onTabSelect(position: Int) {
                //切换Fragment
                switchFragment(position)
            }

            override fun onTabReselect(position: Int) {

            }
        })
    }


    /**
     * 切换Fragment
     * @param position 下标
     */
    private fun switchFragment(position: Int) {
        val transaction = supportFragmentManager.beginTransaction()
        initWhiteActionBar(-1, null, mTitles[position])
        L.d("isVolLogin" + SPUtil.get(this, "isVolLogin", false))
        hideFragments(transaction)
        when (position) {
            0 // 首页
            -> {
                mHomeFragment?.let {
                    transaction.show(it)
                } ?: HomeFragment.getInstance(mTitles[position]).let {
                    mHomeFragment = it
                    transaction.add(R.id.fl_container, it, "home")
                }
            }

            1  //志愿者
            -> {
                if (SPUtil.get(this, "isVolLogin", false) as Boolean) {
                    mVolunteerFragment?.let {
                        transaction.show(it)
                    } ?: VolunteerFragment.getInstance(mTitles[position]).let {
                        mVolunteerFragment = it
                        transaction.add(R.id.fl_container, it, "volunteer")
                    }
                } else {
                    LoginFragment.getInstance(mTitles[position]).let {
                        mLoginFragment = it
                        transaction.add(R.id.fl_container, it, "loginvolunteer")
                    }
                }

            }
            2  //志愿组织
            -> {
                if (SPUtil.get(this, "isOrgLogin", false) as Boolean) {
                    mVoluntaryOrganizationFragment?.let {
                        transaction.show(it)
                    } ?: VoluntaryOrganizationFragment.getInstance(mTitles[position]).let {
                        mVoluntaryOrganizationFragment = it
                        transaction.add(R.id.fl_container, it, "organization")
                    }
                } else {
                    LoginOrgFragment.getInstance(mTitles[position]).let {
                        mLoginOrgFragment = it
                        transaction.add(R.id.fl_container, it, "loginorganization")
                    }
                }
            }

            else -> {

            }
        }

        mIndex = position
        tab_layout.currentTab = mIndex
        transaction.commitAllowingStateLoss()
    }


    /**
     * 隐藏所有的Fragment
     * @param transaction transaction
     */
    private fun hideFragments(transaction: FragmentTransaction) {
        mHomeFragment?.let { transaction.hide(it) }
        mVolunteerFragment?.let { transaction.hide(it) }
        mLoginFragment?.let { transaction.remove(it) }
        mLoginOrgFragment?.let { transaction.remove(it) }
        mVoluntaryOrganizationFragment?.let { transaction.hide(it) }
    }


    override fun showLoading() {
    }

    override fun dismissLoading() {

    }

    override fun setClientsAuth(string: String) {
        SPUtil.put(this, "auth", string)
    }

    override fun showError(errorMsg: String, errorCode: Int) {
        ToastUtils.showShort(this, errorMsg)
    }

    override fun onReceiveEvent(event: EventBusMessage<Any>) {
        when (event.code) {
            Const.LOGIN_VOL_SUCCESS, Const.LOGIN_EXIT -> {
                val index = mTitles.asList().indexOf(event.data)
                index?.let {
                    switchFragment(it)
                }
            }
            Const.LOGIN_ORG_SUCCESS, Const.LOGIN_EXIT -> {
                val index = mTitles.asList().indexOf(event.data)
                index?.let {
                    switchFragment(it)
                }
            }
            Const.VOL_EDIT_PWD, Const.VOL_EDIT_TEL -> {
                SPUtil.clear(this)
                if (event.data == "org") {
                    switchFragment(2)
                } else {
                    switchFragment(1)
                }
                getBaseInfo()
            }
            Const.GO_LOGIN_VOL -> {
                switchFragment(1)
            }
            Const.GO_LOGIN_ORG -> {
                switchFragment(2)
            }
        }
    }


    override fun getBaseInfoResult(data: List<BaseInfoBean>, type: String) {
        SPUtil.put(this, type, Gson().toJson(data))
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
        unregisterReceiver(downloadCompleteBroadcast)
    }


    override fun setVersionResult(data: VersionBean) {
        if (!StringUtils.isEmpty(data.version) && !StringUtils.isEmpty(data.url) && data.version > PackageManage.getPackageCode(this).toString()) {
            var bundle = Bundle()
            bundle.putString("url", data.url)
            bundle.putString("content", data.desc)
            IntentUtils.to(this, UpdateActivity::class.java, bundle)
        }
    }

    internal inner class DownloadCompleteBroadcast : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == DownloadManager.ACTION_DOWNLOAD_COMPLETE) {
                DownLoadBuilder.intallApk(this@MainActivity, PackageManage.getAppProcessName(context))
            }
        }
    }
}
