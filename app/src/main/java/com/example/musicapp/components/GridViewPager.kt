package com.example.musicapp.components

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

fun <T> LazyListScope.GridViewPager(
    data: List<T>,
    perRowCount:Int,
    rowCount: Int,
    key: ((item: T) -> Any)? = null,
    itemContent: @Composable BoxScope.(T) -> Unit,
){
    val totalCount = data.size
    val perPageCount = perRowCount* rowCount
    val fillPageCount =  if (data.isEmpty()) 0 else  totalCount/perPageCount;
    val pageCount = if (data.isEmpty()) 0 else if (totalCount % (perPageCount) != 0)  fillPageCount+1 else fillPageCount;
    Log.d("pageCount",pageCount.toString())
    items(pageCount){  pageIndex->
        Column(
            modifier = Modifier
                .fillParentMaxWidth()
                .fillMaxHeight()
        ) {
            for (rowIndex in 0 until rowCount) {
                Box(
                    modifier = Modifier.weight(1f, fill = true),
                    // propagateMinConstraints = true
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center
                    ){
                        for(index in 0 until perRowCount){
                            val itemIndex = (pageIndex*perPageCount)+(rowIndex*perRowCount)+index
                            if (itemIndex < data.count()) {
                                val item = data[itemIndex]
                                androidx.compose.runtime.key(key?.invoke(item)) {
                                    Box(
                                        modifier = Modifier.weight(1f, fill = true),
                                        // propagateMinConstraints = true
                                    ) {
                                        itemContent.invoke(this, item)
                                    }
                                }
                            } else {
                                Spacer(Modifier.weight(1f, fill = true))
                            }
                        }
                    }
                }

            }
        }
    }
}


fun LazyListState.isScrolledToEnd():Boolean {
    return if( layoutInfo.visibleItemsInfo.lastOrNull() == null) false
    else layoutInfo.visibleItemsInfo.last().index == layoutInfo.totalItemsCount - 1
}