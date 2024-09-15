package com.example.musicapp.data.repository

import com.example.musicapp.data.bean.PlayList
import com.example.musicapp.data.requestResult.RequestResultWithData

interface IFetchDataRepository {
    suspend fun getTotalCharts(territory:String): RequestResultWithData<List<PlayList>>

    suspend fun getTotalFeaturedPlayList(territory:String):RequestResultWithData<List<PlayList>>

    suspend fun getTotalNewHitsPlayLists(territory:String):RequestResultWithData<List<PlayList>>
}