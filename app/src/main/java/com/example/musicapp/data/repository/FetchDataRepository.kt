package com.example.musicapp.data.repository

import com.example.musicapp.bean.remote.PersonalInformation
import com.example.musicapp.bean.remote.PlayList
import com.example.musicapp.bean.remote.PlayListContent
import com.example.musicapp.bean.remote.Track
import com.example.musicapp.data.dao.GetRequestInterface
import com.example.musicapp.data.api.FetchDataApiRequests
import com.example.musicapp.data.requestResult.RequestResultWithData
import com.example.musicapp.localDatase.ISaveUserInfoInApp


class FetchDataRepository(private val saveUserInfoInApp: ISaveUserInfoInApp, requestInterface: GetRequestInterface) :IFetchDataRepository {

    private val fetchDataApiRequests = FetchDataApiRequests(requestInterface)
    override suspend fun getTotalCharts(territory: String): RequestResultWithData<List<PlayList>> = fetchDataApiRequests.fetchTotalCharts(territory,saveUserInfoInApp.getToken())
    override suspend fun getTotalFeaturedPlayList(territory: String): RequestResultWithData<List<PlayList>>  = fetchDataApiRequests.fetchTotalFeaturedPlayList(territory,saveUserInfoInApp.getToken())
    override suspend fun getTotalNewHitsPlayLists(territory: String): RequestResultWithData<List<PlayList>> = fetchDataApiRequests.fetchTotalNewHitsPlayLists(territory,saveUserInfoInApp.getToken())
    override suspend fun getTracksInChart(charId: String, offset: String, territory: String): RequestResultWithData<PlayListContent>  = fetchDataApiRequests.fetchTracksInChart(charId,offset,"500",territory,saveUserInfoInApp.getToken())
    override suspend fun getTracksInFeaturedPlayList(playListId: String, offset: String, territory: String): RequestResultWithData<PlayListContent> = fetchDataApiRequests.fetchTracksInFeaturedPlayList(playListId,offset,"500",territory,saveUserInfoInApp.getToken())
    override suspend fun getTracksInNewHitsPlayList(playListId: String, offset: String, territory: String): RequestResultWithData<PlayListContent> = fetchDataApiRequests.fetchTracksInNewHitsPlayList(playListId,offset,"500",territory,saveUserInfoInApp.getToken())
    override suspend fun getTopTracksOfArtist(artistId: String, offset: String, territory: String): RequestResultWithData<List<Track>> = fetchDataApiRequests.fetchTopTracksOfArtist(artistId,offset,"500",territory,saveUserInfoInApp.getToken())
    override suspend fun searchTrack(keyword: String,territory: String,offset: String): RequestResultWithData<List<Track>>  = fetchDataApiRequests.searchTrack(keyword,territory,saveUserInfoInApp.getToken(),offset,"50")
    override suspend fun getDailyRecommendedTracks(territory: String, offset: String): RequestResultWithData<List<Track>> = fetchDataApiRequests.getDailyRecommendedTracks(territory,offset,"50",saveUserInfoInApp.getToken())
    override suspend fun getPersonalRecommendedTracks( territory: String,offset: String): RequestResultWithData<List<Track>> = fetchDataApiRequests.getPersonalRecommendedTracks(territory,offset,"50",saveUserInfoInApp.getToken())
    override suspend fun getPersonalInformation():RequestResultWithData<PersonalInformation> = fetchDataApiRequests.getPersonalInformation(saveUserInfoInApp.getToken())
}