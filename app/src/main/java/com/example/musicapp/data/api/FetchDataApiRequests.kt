package com.example.musicapp.data.api

import com.example.musicapp.bean.remote.PlayList
import com.example.musicapp.bean.remote.PlayListContent
import com.example.musicapp.bean.remote.Track
import com.example.musicapp.data.dao.GetRequestInterface
import com.example.musicapp.data.dao.RequestResultConverter.ChartsRequestResultConverter
import com.example.musicapp.data.dao.RequestResultConverter.FeaturedPlayListRequestConverter
import com.example.musicapp.data.dao.RequestResultConverter.FoundationDataRequestResultConverter
import com.example.musicapp.data.dao.RequestResultConverter.NewHitsPlayListRequestResultConverter
import com.example.musicapp.data.dao.RequestResultConverter.RecommendedTracksRequestConverter
import com.example.musicapp.data.dao.RequestResultConverter.SearchResultRequestConverter
import com.example.musicapp.data.requestResult.RequestResultWithData
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FetchDataApiRequests(private val requestInterface: GetRequestInterface) {

    private val chartsRequestResultConverter = ChartsRequestResultConverter()
    private val featuredPlayListRequestConverter = FeaturedPlayListRequestConverter()
    private val newHitsPlayListRequestResultConvert = NewHitsPlayListRequestResultConverter();
    private val foundationDataRequestResultConverter = FoundationDataRequestResultConverter()
    private val searchResultRequestConverter = SearchResultRequestConverter()
    private val recommendedTracksRequestConverter = RecommendedTracksRequestConverter()
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

    suspend fun fetchTracksInChart(playListId:String, offset:String, limit:String,  territory:String, token:String ) = suspendCoroutine<RequestResultWithData<PlayListContent>> {
            continuation ->
        requestInterface.getTracksInChart(playListId,offset,limit,territory,"Bearer "+token).enqueue(object:Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>)
                    = continuation.resume(chartsRequestResultConverter.ConvertGetChartOnResponse(response))
            override fun onFailure(call: Call<ResponseBody>, t: Throwable)
                    = continuation.resume(chartsRequestResultConverter.ConvertGetChartOnFailure(call, t))

        });
    }

    suspend  fun fetchTracksInFeaturedPlayList(playListId:String,  offset:String, limit:String,  territory:String, token:String)= suspendCoroutine<RequestResultWithData<PlayListContent>>{
            continuation ->
        requestInterface.getTracksInFeaturedPlayList( playListId,offset,limit,territory,"Bearer "+token).enqueue(object:Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>)
                    = continuation.resume(featuredPlayListRequestConverter.ConvertGetFeaturedPlayListOnResponse(response))
            override fun onFailure(call: Call<ResponseBody>, t: Throwable)
                    = continuation.resume(featuredPlayListRequestConverter.ConvertGetFeaturedPlayListOnFailure( call,t))
        });
    }

    suspend fun fetchTracksInNewHitsPlayList(playListId:String,offset:String, limit:String,  territory:String, token:String)= suspendCoroutine<RequestResultWithData<PlayListContent>>{
            continuation ->
        requestInterface.getTracksInNewHitsPlayList(playListId,offset,limit,territory,"Bearer "+token).enqueue(object:Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>)
                    = continuation.resume(newHitsPlayListRequestResultConvert.convertGetTracksInNewHitsPlayListOnResponse(response))

            override fun onFailure(call: Call<ResponseBody>, t: Throwable)
                    = continuation.resume(newHitsPlayListRequestResultConvert.convertGetTracksInNewHitsPlayListOnFailure(call, t))

        })
    }

    suspend fun fetchTopTracksOfArtist(artistId:String, offset:String,limit:String,territory:String, token:String) = suspendCoroutine<RequestResultWithData<List<Track>>>{
            continuation ->
        requestInterface.getTopTracksOfArtist(artistId, offset,limit,territory, "Bearer "+token).enqueue(object:Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>)
                    = continuation.resume(foundationDataRequestResultConverter.convertGetTopTracksOfArtistOnResponse(response))
            override fun onFailure(call: Call<ResponseBody>, t: Throwable)
                    = continuation.resume(foundationDataRequestResultConverter.convertGetTopTracksOfArtistOnFailure(call,t))

        });
    }

    suspend fun searchTrack(keyword:String, territory:String, token:String,offset:String,limit:String) = suspendCoroutine<RequestResultWithData<List<Track>>>{
            continuation ->
        requestInterface.search(keyword,"track", territory, "Bearer "+token,offset,limit).enqueue(object:Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>)
                    = continuation.resume(searchResultRequestConverter.convertSearchTrackOnResponse(response))

            override fun onFailure(call: Call<ResponseBody>, t: Throwable)
                    = continuation.resume(searchResultRequestConverter.convertSearchTrackOnFailure(call, t))

        });
    }

    suspend fun getDailyRecommendedTracks(territory:String,offset:String,limit:String,token:String) = suspendCoroutine<RequestResultWithData<List<Track>>> {
            continuation ->
            requestInterface.getDailyRecommendedTracks(territory, offset, limit, "Bearer "+token).enqueue(object:Callback<ResponseBody>{
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody> )
                = continuation.resume(recommendedTracksRequestConverter.convertDailyRecommendedTrackOnResponses(response))
                override fun onFailure(call: Call<ResponseBody>, t: Throwable)
                = continuation.resume(recommendedTracksRequestConverter.convertDailyRecommendedTrackOnFailure(call, t))

            })
    }

    suspend fun getPersonalRecommendedTracks(territory:String,offset:String,limit:String,token:String) = suspendCoroutine<RequestResultWithData<List<Track>>> {
            continuation ->
            requestInterface.getPersonalRecommendedTracks(territory, offset, limit,"Bearer "+ token).enqueue(object:Callback<ResponseBody>{
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody> )
                = continuation.resume(recommendedTracksRequestConverter.convertPersonalRecommendedTracksOnResponse(response))
               override fun onFailure(call: Call<ResponseBody>, t: Throwable)
                = continuation.resume(recommendedTracksRequestConverter.convertPersonalRecommendedTracksOnFailure(call, t))
            })
    }

}