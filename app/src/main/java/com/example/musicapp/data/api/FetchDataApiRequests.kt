package com.example.musicapp.data.api

import com.example.musicapp.data.bean.PlayList
import com.example.musicapp.data.dao.GetRequestInterface
import com.example.musicapp.data.dao.RequestResultConverter.ChartsRequestResultConverter
import com.example.musicapp.data.dao.RequestResultConverter.FeaturedPlayListGetRequestConverter
import com.example.musicapp.data.dao.RequestResultConverter.NewHitsPlayListGetRequestResultConverter
import com.example.musicapp.data.requestResult.RequestResultWithData
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FetchDataApiRequests(private val requestInterface: GetRequestInterface) {

    private val chartsRequestResultConverter = ChartsRequestResultConverter()
    private val featuredPlayListRequestConverter = FeaturedPlayListGetRequestConverter()
    private val newHitsPlayListRequestResultConvert = NewHitsPlayListGetRequestResultConverter();
    suspend fun fetchTotalCharts(territory: String,token:String) = suspendCoroutine<RequestResultWithData<List<PlayList>>> {
        continuation ->
        requestInterface.getCharts(territory,"Bearer "+token).enqueue(object: Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) = continuation.resume(chartsRequestResultConverter.ConvertGetTotalChartsOnResponse(response))
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) = continuation.resume(chartsRequestResultConverter.ConvertGetTotalChartsOnFailure(call, t))
         }
       );
    }

    suspend fun fetchTotalFeaturedPlayList(territory: String,token:String) = suspendCoroutine<RequestResultWithData<List<PlayList>>> {
            continuation ->
            requestInterface.getFeaturedPlayLists(territory, "Bearer "+token).enqueue(object:Callback<ResponseBody>{
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) = continuation.resume(featuredPlayListRequestConverter.ConvertGetTotalFeaturedPlayListOnResponse(response));
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) = continuation.resume(featuredPlayListRequestConverter.ConvertGetTotalFeaturedPlayListOnFailure(call, t))
              }
            );
    }

    suspend fun fetchTotalNewHitsPlayLists(territory: String,token:String) = suspendCoroutine<RequestResultWithData<List<PlayList>>> {
         continuation ->
        requestInterface.getNewHitsPlayLists(territory,"Bearer "+token).enqueue(object:Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) = continuation.resume(newHitsPlayListRequestResultConvert.convertGetTotalNewHitsPlayListsOnResponse(response))
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) = continuation.resume(newHitsPlayListRequestResultConvert.convertGetTotalNewHitsPlayListsOnFailure(call, t))
           }
        );
    }


}