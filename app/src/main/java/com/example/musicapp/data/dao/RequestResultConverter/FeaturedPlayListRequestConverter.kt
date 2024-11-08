package com.example.musicapp.data.dao.RequestResultConverter

import com.example.musicapp.bean.remote.PlayList
import com.example.musicapp.bean.remote.PlayListContent
import com.example.musicapp.bean.remote.Track
import com.example.musicapp.data.requestResult.RequestResultWithData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response

class FeaturedPlayListRequestConverter : RequestResultConverter() {

    private val gson = Gson()
    fun ConvertGetTotalFeaturedPlayListOnResponse(response: Response<ResponseBody>): RequestResultWithData<List<PlayList>> {
        val responseCode = response.code();
        if(responseCode >= 300 ){
            val errorMessage = ConvertResponseErrorMessage(response)
            return RequestResultWithData.Error(errorMessage)
        }
        else{
            return try{
                val body = response.body();
                val jsonObject = getJsonObjectFromResponseBody(body);
                val data = jsonObject.getJSONArray("data");
                val type = object: TypeToken<List<PlayList>>() {}.type
                val playLists = gson.fromJson<List<PlayList>>(data.toString(),type)
                RequestResultWithData.Success("已到達最後一頁", playLists)
            }catch (e:Exception){
                RequestResultWithData.Error("Api 回傳資料轉換失敗，$e");
            }
        }
    }


    fun ConvertGetTotalFeaturedPlayListOnFailure(call: Call<ResponseBody>, t: Throwable):RequestResultWithData<List<PlayList>>{
        val failureMessage = ConvertResponseFailureMessage(call,t)
        return RequestResultWithData.Failure(failureMessage)
    }


    fun ConvertGetFeaturedPlayListOnResponse(response: Response<ResponseBody>): RequestResultWithData<PlayListContent>{
        val responseCode = response.code();
        if(responseCode >= 300 ){
            val errorMessage = ConvertResponseErrorMessage(response)
            return RequestResultWithData.Error(errorMessage)
        }
        else{
            try{
                val body = response.body();
                val jsonObject = getJsonObjectFromResponseBody(body);
                val tracks = jsonObject.getJSONObject("tracks");
                val data = tracks.getJSONArray("data");
                val paging = tracks.getJSONObject("paging");
                val summary = tracks.getJSONObject("summary")
                val offset = paging.getInt("offset");
                val total = summary.getInt("total");
                val id = jsonObject.getString("id");
                val title =  jsonObject.getString("title")
                return when {
                    total == 0 -> {
                        val content = PlayListContent( id = id, title =title,  tracks = listOf() )
                        RequestResultWithData.Success("已到達最後一頁", content)
                    }
                    offset == total-1 -> {
                        val type = object: TypeToken<List<Track>>() {}.type
                        val trackList = gson.fromJson<List<Track>>(data.toString(),type)
                        val content =  PlayListContent( id = id, title =title, tracks = trackList)
                        RequestResultWithData.Success("已到達最後一頁", content)
                    }
                    else -> {
                        val type = object: TypeToken<List<Track>>() {}.type
                        val trackList = gson.fromJson<List<Track>>(data.toString(),type)
                        val content = PlayListContent(id = id,title = title, tracks = trackList)
                        RequestResultWithData.Success("Success !", content)
                    }
                }
            }catch (e:Exception){
                return RequestResultWithData.Error("Api 回傳資料轉換失敗，$e");
            }
        }
    }

    fun ConvertGetFeaturedPlayListOnFailure(call: Call<ResponseBody>, t: Throwable):RequestResultWithData<PlayListContent>{
        val failureMessage = ConvertResponseFailureMessage(call,t)
        return RequestResultWithData.Failure(failureMessage)
    }
}