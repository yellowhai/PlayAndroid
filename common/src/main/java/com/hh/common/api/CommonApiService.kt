package com.hh.common.api

import com.hh.common.network.ApiResponse
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * @Description: todo
 * @Author: yshh
 * @CreateDate: 2022/3/2  15:48
 */
interface CommonApiService {
    /**
     * 收藏
     */
    @POST("lg/collect/{id}/json")
    suspend fun setCollect(@Path("id") id: Int): ApiResponse<Any?>

    /**
     * 取消收藏
     */
    @POST("lg/uncollect_originId/{id}/json")
    suspend fun unCollect(@Path("id") id: Int): ApiResponse<Any>

    /**
     * 收藏url
     */
    @POST("lg/collect/addtool/json")
    suspend fun collectUrl(
        @Query("name") name: String,
        @Query("link") link: String
    ): ApiResponse<Any>

    /**
     * 取消收藏收藏url
     */
    @POST("lg/collect/deletetool/json")
    suspend fun unCollectUrl(@Query("id") id: Int): ApiResponse<Any?>
}