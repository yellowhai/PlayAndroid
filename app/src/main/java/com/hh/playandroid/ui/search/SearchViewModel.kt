package com.hh.playandroid.ui.search

import android.os.Bundle
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hh.common.api.TaskApi
import com.hh.common.base.BaseViewModel
import com.hh.common.base.YshhApplication.Companion.context
import com.hh.common.base.launch
import com.hh.common.bean.ModelPath
import com.hh.common.theme.CommonPurple200
import com.hh.common.theme.CommonPurple500
import com.hh.common.theme.CommonPurple700
import com.hh.common.theme.CommonTeal200
import com.hh.common.util.CacheUtils
import com.hh.common.util.CpNavigation
import com.hh.common.util.showToast
import com.hh.playandroid.R
import com.hh.playandroid.api.ApiServices
import com.hh.playandroid.bean.SearchResponse

/**
 * @ProjectName: CBook
 * @Package: com.hh.cbook.ui.viewmodel
 * @Description: 类描述
 * @Author: Hai Huang
 * @CreateDate: 2021/9/1  15:33
 */

const val SEARCH_NAME = "searchName"
class SearchViewModel : BaseViewModel() {
    var viewStates by mutableStateOf(SearchState())
        private set

    private var searchList by CacheUtils.safeKeyDelegate(stringPreferencesKey("shareHistory"),"")

    fun dispatch(action : SearchAction){
        when(action){
            SearchAction.ClearHistoryList -> clearHistoryData()
            SearchAction.GetHistoryList -> getHistoryData()
            SearchAction.GetHotList -> getHotList()
            SearchAction.Share -> search()
            SearchAction.ClearSearchName -> viewStates = viewStates.copy(shareName = "")
            is SearchAction.ChangeShareName -> viewStates = viewStates.copy(shareName = action.s)
            is SearchAction.RemoveHistory -> removeIt(action.s)
            is SearchAction.ShareHistory -> search(action.s)
        }
    }

    private fun getHotList(){
        launch({
               TaskApi.create(ApiServices::class.java).getSearchData()
        },{
            it.data?.apply {
                this.map {s ->
                    s.color = SHARE_COLOR[(SHARE_COLOR.indices).random()]
                }
                viewStates = viewStates.copy(hotList = this)
            }
        })
    }

    private fun getHistoryData() {
        if(searchList.isNotEmpty()){
            viewStates = viewStates.copy(historyList = Gson().fromJson(searchList
                , object : TypeToken<ArrayList<String>>() {}.type))
        }
    }

    private fun clearHistoryData(){
        searchList = ""
        viewStates =viewStates.copy(historyList = arrayListOf())
    }

    private fun removeIt(name : String){
        val list : ArrayList<String>  = Gson().fromJson(searchList
            , object : TypeToken<ArrayList<String>>() {}.type)
        list.remove(name)
        viewStates = viewStates.copy(historyList = list)
        searchList = viewStates.historyList.toString()
    }

    private fun search(){
        if(viewStates.shareName.isEmpty()){
            context.showToast(context.getString(R.string.please_key_search))
            return
        }
        CpNavigation.toBundle(ModelPath.SearchResult,Bundle().apply {
            putString(SEARCH_NAME,viewStates.shareName)
        })
        viewStates.historyList.let {
            if (it.contains(viewStates.shareName)) {
                //当搜索历史中包含该数据时 删除
                it.remove(viewStates.shareName)
            } else if (it.size >= 10) {
                //如果集合的size 有10个以上了，删除最后一个
                it.removeAt(it.size - 1)
            }
            //添加新数据到第一条
            it.add(0, viewStates.shareName)
            it
        }
        searchList = viewStates.historyList.toString()
    }

    private fun search(name : String){
        CpNavigation.toBundle(ModelPath.SearchResult,Bundle().apply {
            putString(SEARCH_NAME,name)
        })
    }
}



sealed class SearchAction{
    object GetHotList : SearchAction()
    object GetHistoryList : SearchAction()
    object ClearHistoryList : SearchAction()
    object ClearSearchName : SearchAction()
    object Share : SearchAction()
    data class RemoveHistory(val s : String) : SearchAction()
    data class ChangeShareName(val s : String) : SearchAction()
    data class ShareHistory(val s : String) : SearchAction()
}

data class SearchState(val historyList : ArrayList<String> = arrayListOf(),
                      val hotList : List<SearchResponse> = listOf(),
                      val shareName : String = ""
)

var SHARE_COLOR = listOf(
    Color(0xFFEF5350),
    Color(0xFFEC407A),
    Color(0xFFAB47BC),
    Color(0xFF7E57C2),
    Color(0xFF5C6BC0),
    Color(0xFF42A5F5),
    Color(0xFF29B6F6),
    Color(0xFF26C6DA),
    Color(0xFF26A69A),
    Color(0xFF66BB6A),
    Color(0xFF9CCC65),
    Color(0xFFFFEE58),
    Color(0xFFFFCA28),
    Color(0xFFFFA726),
    Color(0xFFFF7043),
    Color(0xFF8D6E63),
    Color(0xFFBDBDBD),
    Color(0xFF78909C),
    CommonPurple200,
    CommonPurple500,
    CommonPurple700,
    CommonTeal200
)