package com.hh.common.view

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.hh.common.R
import java.net.ConnectException

/**
 * @Description: todo
 * @Author: yshh
 * @CreateDate: 2022/2/25  16:25
 */

@Composable
fun <T : Any> PagingItem(
    modifier: Modifier = Modifier,
    list: LazyPagingItems<T>,
    errorBlock: () -> Unit,
    successBlock: () -> Unit,
    errorAndSuccessClick: (() -> Unit?)? = null,
    actions: @Composable (list: LazyPagingItems<T>) -> Unit = {}
) {
    BoxWithConstraints(modifier) {
        when (list.loadState.refresh) {
            is LoadState.Error -> {
                errorBlock.invoke()
                ErrorBox(
                    modifier.height(maxHeight)
                        .width(maxWidth),
                    (list.loadState.refresh as LoadState.Error).error.localizedMessage!!
                ) { errorAndSuccessClick?.run { errorAndSuccessClick.invoke() } }
            }
            is LoadState.Loading -> {
                BoxProgress()
            }
            is LoadState.NotLoading -> {
                successBlock.invoke()
                if(list.loadState.prepend.endOfPaginationReached){
                    when (list.itemCount) {
                        0 -> {
                            LazyColumn {
                                item {
                                    ErrorBox(
                                        modifier
                                            .height(maxHeight)
                                            .width(maxWidth), stringResource(R.string.no_data)
                                    ){ errorAndSuccessClick?.run { errorAndSuccessClick.invoke() } }
                                }
                            }
                        }
                        else -> {
                            actions.invoke(list)
                        }
                    }
                }
            }
        }
    }
}