package com.hh.common.base

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.os.Environment
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import com.hh.common.network.LogInterceptor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import java.io.File
import java.util.concurrent.TimeUnit
import okhttp3.logging.HttpLoggingInterceptor




/**
 * @Description: todo
 * @Author: yshh
 * @CreateDate: 2022/1/4  15:39
 */
open class YshhApplication : Application() {

    lateinit var okbuilder: OkHttpClient

    companion object {
        /**
         * application context.
         */
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context

        @SuppressLint("StaticFieldLeak")
        lateinit var instance: YshhApplication
        val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        instance = this
        initRetrofit()
    }


    private fun initRetrofit(token : String = ""): OkHttpClient {
        //请求头
        val headerInterceptor = Interceptor { chain: Interceptor.Chain ->
            val orignaRequest = chain.request()
            val request = orignaRequest.newBuilder()
                .header("Authorization", "Bearer $token")
                .method(orignaRequest.method, orignaRequest.body)
                .build()
            chain.proceed(request)
        }
        val logInterceptor = LogInterceptor {
//            it.logE()
        }
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        val cacheFile =
            File(getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString() + "http_cache")
        val cache = Cache(cacheFile, 104857600L) // 指定缓存大小100Mb
        val builder = OkHttpClient.Builder()
        //        builder.addInterceptor(addQueryParameterInterceptor);
        builder.addInterceptor(headerInterceptor)
        builder.cache(cache)
        builder.addNetworkInterceptor(logInterceptor)
        builder.cookieJar(cookieJar)
        builder.readTimeout(60000, TimeUnit.MILLISECONDS)
        //全局的写入超时时间60s
        builder.writeTimeout(60000, TimeUnit.MILLISECONDS)
        //全局的连接超时时间30s
        builder.connectTimeout(30000, TimeUnit.MILLISECONDS)
        okbuilder = builder.build()
        return builder.build()
    }

    private val cookieJar: PersistentCookieJar by lazy {
        PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(context))
    }


    fun getOkBuilder(): OkHttpClient {
        return okbuilder
    }
}