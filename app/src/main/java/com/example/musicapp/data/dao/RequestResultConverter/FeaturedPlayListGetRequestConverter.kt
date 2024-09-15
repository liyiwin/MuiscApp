package com.example.musicapp.data.dao.RequestResultConverter

import com.example.musicapp.data.bean.PlayList
import com.example.musicapp.data.requestResult.RequestResultWithData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response

class FeaturedPlayListGetRequestConverter : GetRequestResultConverter() {

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
}