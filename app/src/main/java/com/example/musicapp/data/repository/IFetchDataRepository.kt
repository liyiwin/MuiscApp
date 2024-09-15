package com.example.musicapp.data.repository

import com.example.musicapp.bean.remote.PlayList
import com.example.musicapp.bean.remote.PlayListContent
import com.example.musicapp.bean.remote.Track
import com.example.musicapp.data.requestResult.RequestResultWithData

interface IFetchDataRepository {
    suspend fun getTotalCharts(territory:String): RequestResultWithData<List<PlayList>>

    suspend fun getTotalFeaturedPlayList(territory:String):RequestResultWithData<List<PlayList>>

    suspend fun getTotalNewHitsPlayLists(territory:String):RequestResultWithData<List<PlayList>>

    suspend fun getTracksInChart(charId:String,offset:String,territory:String ) :RequestResultWithData<PlayListContent>

    suspend fun   getTracksInFeaturedPlayList(playListId: String, offset:String,  territory:String):RequestResultWithData<PlayListContent>

    suspend  fun getTracksInNewHitsPlayList( playListId:String,offset:String,territory:String):RequestResultWithData<PlayListContent>

    suspend fun getTopTracksOfArtist(artistId:String,offset:String, territory:String):RequestResultWithData<List<Track>>


}