package com.example.musicapp.data.dao.RequestResultConverter

import com.example.musicapp.bean.remote.Track
import com.example.musicapp.data.requestResult.RequestResultWithData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response

class FoundationDataGetRequestResultConverter:GetRequestResultConverter() {

    private val gson = Gson()

    fun convertGetTopTracksOfArtistOnResponse(response: Response<ResponseBody>): RequestResultWithData<List<Track>>{
        val responseCode = response.code();
        if(responseCode >= 300 ){
            val errorMessage = ConvertResponseErrorMessage(response)
            return RequestResultWithData.Error(errorMessage)
        }
        else{
            try{
                val body = response.body();
                val jsonObject = getJsonObjectFromResponseBody(body);
                val data = jsonObject.getJSONArray("data");
                val paging = jsonObject.getJSONObject("paging");
                val summary = jsonObject.getJSONObject("summary")
                val offset = paging.getInt("offset");
                val total = summary.getInt("total");
                if(total == 0 ) return RequestResultWithData.Success("已到達最後一頁", listOf<Track>())
                else if(offset == total-1){
                    val type = object: TypeToken<List<Track>>() {}.type
                    val trackList = gson.fromJson<List<Track>>(data.toString(),type)
                    return RequestResultWithData.Success("已到達最後一頁", trackList)
                }
                else{
                    val type = object: TypeToken<List<Track>>() {}.type
                    val trackList = gson.fromJson<List<Track>>(data.toString(),type)
                    return RequestResultWithData.Success("Success!", trackList)
                }
            }catch (e:Exception){
                return RequestResultWithData.Error("Api 回傳資料轉換失敗，"+e);
            }
        }
    }

    fun convertGetTopTracksOfArtistOnFailure(call: Call<ResponseBody>, t: Throwable): RequestResultWithData<List<Track>>{
        val failureMessage = ConvertResponseFailureMessage(call,t)
        return RequestResultWithData.Failure(failureMessage)
    }
}