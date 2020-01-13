package com.router.wmsj

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.wmsj.baselibs.scheduler.SchedulerUtils
import com.wmsj.baselibs.ui.activity.MainActivity
import com.wmsj.baselibs.utils.IntentUtils
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

/**
 * Created by tian
on 2019/9/26.
 */
class StartActivity : AppCompatActivity() {

    private val count = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        initData()
    }


    fun initData() {
        Observable.interval(0, 1, TimeUnit.SECONDS)
                .take((count + 1).toLong())
                .map { t -> count - t }
                .compose(SchedulerUtils.ioToMain())
                .doOnSubscribe { }
                .subscribe(object : Observer<Long> {
                    override fun onNext(t: Long) {
                    }

                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onError(e: Throwable) {
                        //回复原来初始状态
                    }

                    override fun onComplete() {
                        //回复原来初始状态
                        IntentUtils.to(this@StartActivity, MainActivity::class.java)
                        finish()
                    }
                })

    }

}