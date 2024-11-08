package com.example.musicapp.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlin.math.ceil

@Composable
fun<T> GridView(columnCount:Int,items:List<T>,scrollable:Boolean = true,itemContent: @Composable BoxScope.(T) -> Unit){
    val rowCount = ceil(items.size.toFloat()/columnCount).toInt()
    if(scrollable){
        LazyColumn(
            modifier = Modifier.padding(top =10.dp)
        ){
            for(i in 0 until  rowCount){
                item{
                    Row(
                        modifier = Modifier.fillMaxHeight()
                    ){
                        for(j in 0 until columnCount){
                            val index = j+(i*columnCount);
                            if(index < items.size)
                                Box(
                                    modifier = Modifier
                                        .weight(1f, fill = true)
                                        .padding(5.dp)
                                ){
                                    itemContent.invoke(this,items[index]);
                                }
                            else
                                Spacer(modifier = Modifier.weight(1f, fill = true))
                        }
                    }
                }

            }
        }
    }
    else{
        Column(
            modifier = Modifier.padding(top =10.dp)
        ){
            for(i in 0 until  rowCount){
                Row(
                    modifier = Modifier.fillMaxHeight()
                ){
                    for(j in 0 until columnCount){
                        val index = j+(i*columnCount);
                        if(index < items.size)
                            Box(
                                modifier = Modifier
                                    .weight(1f, fill = true)
                                    .padding(5.dp)
                            ){
                                itemContent.invoke(this,items[index]);
                            }
                        else
                            Spacer(modifier = Modifier.weight(1f, fill = true))
                    }
                }
            }
        }
    }

}