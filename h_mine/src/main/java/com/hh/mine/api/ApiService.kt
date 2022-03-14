package com.hh.mine.api

import com.hh.common.bean.ApiPagerResponse
import com.hh.common.bean.Integral
import com.hh.common.bean.IntegralHistory
import com.hh.common.network.ApiResponse
import com.hh.mine.bean.CollectInside
import com.hh.mine.bean.CollectUrl
import com.hh.mine.bean.ShareResponseBody
import com.hh.mine.bean.TodoBean
import retrofit2.http.*

/**
 * @Description: todo
 * @Author: yshh
 * @CreateDate: 2022/2/24  14:42
 */
interface ApiService {
    /**
     * 获取当前账户的个人积分
     */
    @GET("lg/coin/userinfo/json")
    suspend fun getIntegral(): ApiResponse<Integral>

    //退出登录
    @GET("/user/logout/json")
    suspend fun logout(): ApiResponse<Any>

    /**
     * 历史积分
     */
    @GET("lg/coin/list/{page}/json")
    suspend fun getIntegralHistory(@Path("page") page: Int): ApiResponse<ApiPagerResponse<List<IntegralHistory>>>

    /**
     * 获取收藏内文章
     */
    @GET("lg/collect/list/{page}/json")
    suspend fun getCollectData(@Path("page") pageNo: Int): ApiResponse<ApiPagerResponse<List<CollectInside>>>

    /**
     * 获取收藏外地址
     */
    @GET("lg/collect/usertools/json")
    suspend fun getCollectUrlData(): ApiResponse<List<CollectUrl>>

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
    ): ApiResponse<CollectUrl>

    /**
     * 取消收藏收藏url
     */
    @POST("lg/collect/deletetool/json")
    suspend fun unCollectUrl(@Query("id") id: Int): ApiResponse<Any?>

    /**
     * 自己的分享的文章列表
     * https://wanandroid.com/user/lg/private_articles/1/json
     * @param page 页码 从1开始
     */
    @GET("user/lg/private_articles/{page}/json")
    suspend fun getShareList(@Path("page") page: Int): ApiResponse<ShareResponseBody>

    /**
     * 添加文章
     */
    @POST("lg/user_article/add/json")
    @FormUrlEncoded
    suspend fun addAriticle(
        @Field("title") title: String,
        @Field("link") content: String
    ): ApiResponse<Any?>

    /**
     * 删除自己分享的文章
     * https://wanandroid.com/lg/user_article/delete/9475/json
     * @param id 文章id，拼接在链接上
     */
    @POST("lg/user_article/delete/{id}/json")
    suspend fun deleteShareArticle(@Path("id") id: Int): ApiResponse<ApiResponse<Any>>


    /**
     * 获取Todo列表数据 根据完成时间排序
     */
    @GET("/lg/todo/v2/list/{page}/json")
    suspend fun getTodoData(@Path("page") page: Int): ApiResponse<ApiPagerResponse<ArrayList<TodoBean>>>

    /**
     * 添加一个TODO
     */
    @POST("/lg/todo/add/json")
    @FormUrlEncoded
    suspend fun addTodo(
        @Field("title") title: String,
        @Field("content") content: String,
        @Field("date") date: String,
        @Field("type") type: Int,
        @Field("priority") priority: Int
    ): ApiResponse<Any?>

    /**
     * 修改一个TODO
     */
    @POST("/lg/todo/update/{id}/json")
    @FormUrlEncoded
    suspend fun updateTodo(
        @Field("title") title: String,
        @Field("content") content: String,
        @Field("date") date: String,
        @Field("type") type: Int,
        @Field("priority") priority: Int,
        @Path("id") id: Int
    ): ApiResponse<Any?>

    /**
     * 删除一个TODO
     */
    @POST("/lg/todo/delete/{id}/json")
    suspend fun deleteTodo(@Path("id") id: Int): ApiResponse<Any?>

    /**
     * 完成一个TODO
     */
    @POST("/lg/todo/done/{id}/json")
    @FormUrlEncoded
    suspend fun doneTodo(@Path("id") id: Int, @Field("status") status: Int): ApiResponse<Any?>

}