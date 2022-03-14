package com.hh.common.api

import com.hh.common.base.YshhApplication
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @Description: todo
 * @Author: yshh
 * @CreateDate: 2022/2/22  11:23
 */
object TaskApi {
    @JvmStatic
    fun <T> create(service: Class<T>): T {
        val retrofit = Retrofit.Builder()
            .baseUrl(HttpUrl.Base_Url)
            .client(YshhApplication.instance.getOkBuilder())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(service)
    }

    @JvmStatic
    fun <T> create(service: Class<T>, url: String): T {
        val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .client(YshhApplication.instance.getOkBuilder())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(service)
    }
}