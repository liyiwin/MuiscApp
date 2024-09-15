package com.example.musicapp.data.repository

import com.example.musicapp.data.bean.PlayList
import com.example.musicapp.data.dao.GetRequestInterface
import com.example.musicapp.data.api.FetchDataApiRequests
import com.example.musicapp.data.requestResult.RequestResultWithData
import com.example.musicapp.localDatase.ISaveUserInfoInApp


class FetchDataRepository(private val saveUserInfoInApp: ISaveUserInfoInApp, requestInterface: GetRequestInterface) :IFetchDataRepository {

    private val fetchDataApiRequests = FetchDataApiRequests(requestInterface)
    override suspend fun getTotalCharts(territory: String): RequestResultWithData<List<PlayList>> = fetchDataApiRequests.fetchTotalCharts(territory,saveUserInfoInApp.getToken())
    override suspend fun getTotalFeaturedPlayList(territory: String): RequestResultWithData<List<PlayList>>  = fetchDataApiRequests.fetchTotalFeaturedPlayList(territory,saveUserInfoInApp.getToken())
    override suspend fun getTotalNewHitsPlayLists(territory: String): RequestResultWithData<List<PlayList>> = fetchDataApiRequests.fetchTotalNewHitsPlayLists(territory,saveUserInfoInApp.getToken())


}