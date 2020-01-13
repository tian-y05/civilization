package com.wmsj.baselibs.retrofit

import com.wmsj.baselibs.Const
import com.wmsj.baselibs.retrofit.api.ApiService
import com.wmsj.baselibs.BaseApplication
import com.wmsj.baselibs.utils.SPUtil
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * Created by tian
on 2019/7/2.
 */
object RetrofitManager {

    val service : ApiService by lazy (LazyThreadSafetyMode.SYNCHRONIZED) {
        getRetrofit(Const.BASE_URL).create(ApiService::class.java)
    }

    private fun getRetrofit(url: String): Retrofit {
        return Retrofit.Builder()
                .baseUrl(url)
                .client(getOkHttpClient())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }


    private fun getOkHttpClient(): OkHttpClient {

        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val cacheFile = File(BaseApplication.appInstance!!.cacheDir, "cache")
        val cache = Cache(cacheFile, 1024 * 1024 * 50)

        return OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(addHeaderInterceptor())
                .cache(cache)
                .connectTimeout(60L, TimeUnit.SECONDS)
                .readTimeout(60L, TimeUnit.SECONDS)
                .writeTimeout(60L, TimeUnit.SECONDS)
                .build()
    }

    private fun addHeaderInterceptor(): Interceptor {
        return Interceptor { it ->
            val request = it.request()
            val requestBuilder = request.newBuilder()
                    .header("api-token", SPUtil.get(BaseApplication.appInstance,"userId","").toString())
                    .method(request.method(), request.body())
            val build = requestBuilder.build()
            it.proceed(build)
        }
    }

}