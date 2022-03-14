package com.hh.playandroid.api

import com.hh.common.bean.ApiPagerResponse
import com.hh.common.bean.UserInfo
import com.hh.common.network.ApiResponse
import com.hh.playandroid.bean.Tabs
import com.hh.playandroid.bean.BannerResponse
import com.hh.playandroid.bean.ArticleBean
import com.hh.playandroid.bean.SearchResponse
import retrofit2.http.*

/**
 * @Description: todo
 * @Author: yshh
 * @CreateDate: 2022/2/22  11:36
 */
interface ApiServices {

    //注册
    @FormUrlEncoded
    @POST("/user/register")
    suspend fun register(
        @Field("username") userName: String,
        @Field("password") password: String,
        @Field("repassword") repassword: String,
    ): ApiResponse<UserInfo>

    //登录
    @FormUrlEncoded
    @POST("/user/login")
    suspend fun login(
        @Field("username") userName: String,
        @Field("password") password: String,
    ): ApiResponse<UserInfo>

    /**
     * banner数据
     */
    @GET("/banner/json")
    suspend fun getBanner(): ApiResponse<List<BannerResponse>>

    /**
     * 首页数据
     */
    @GET("article/list/{page}/json")
    suspend fun getHomeList(@Path("page") pageNo: Int): ApiResponse<ApiPagerResponse<ArrayList<ArticleBean>>>

    /**
     * 置顶文章
     */
    @GET("article/top/json")
    suspend fun getTopHomeList(): ApiResponse<List<ArticleBean>>

    /**
     * 公众号分类
     */
    @GET("wxarticle/chapters/json")
    suspend fun getAccountTab(): ApiResponse<List<Tabs>>

    /**
     * 公众号数据
     */
    @GET("wxarticle/list/{id}/{page}/json")
    suspend fun getAccountData(
        @Path("page") pageNo: Int,
        @Path("id") id: Int
    ): ApiResponse<ApiPagerResponse<ArrayList<ArticleBean>>>

    /**
     * 热门搜索
     */
    @GET("hotkey/json")
    suspend fun getSearchData(): ApiResponse<List<SearchResponse>>
    /**
     * 搜索数据
     */
    @POST("article/query/{page}/json")
    suspend fun getSearchDataByKey(
        @Path("page") pageNo: Int,
        @Query("k") searchKey: String
    ): ApiResponse<ApiPagerResponse<ArrayList<ArticleBean>>>

    /**
     * 项目分类标题
     */
    @GET("project/tree/json")
    suspend fun getProjecTitle(): ApiResponse<ArrayList<Tabs>>

    /**
     * id项目数据
     */
    @GET("project/list/{page}/json")
    suspend fun getProjecDataByType(
        @Path("page") pageNo: Int,
        @Query("cid") cid: Int
    ): ApiResponse<ApiPagerResponse<ArrayList<ArticleBean>>>

    /**
     * 最新项目数据
     */
    @GET("article/listproject/{page}/json")
    suspend fun getProjecNewData(@Path("page") pageNo: Int): ApiResponse<ApiPagerResponse<ArrayList<ArticleBean>>>

}