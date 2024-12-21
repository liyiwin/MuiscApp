package com.example.musicapp.data.dao.RequestResultConverter

import android.util.Log
import com.example.musicapp.bean.remote.PersonalInformation
import com.example.musicapp.data.requestResult.RequestResultWithData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response

class PersonalInformationConverter:RequestResultConverter() {

    private val gson = Gson()

    fun convertPersonalInformationOnResponse(response: Response<ResponseBody>): RequestResultWithData<PersonalInformation> {
        val responseCode = response.code();
        if(responseCode >= 300 ){
            val errorMessage = ConvertResponseErrorMessage(response)
            return RequestResultWithData.Error(errorMessage)
        }else{
            try {
                val body = response.body();
                val jsonObject = getJsonObjectFromResponseBody(body);
                val type = object: TypeToken<PersonalInformation>() {}.type
                val personalInformation = gson.fromJson<PersonalInformation>(jsonObject.toString(),type)
                return RequestResultWithData.Success("Success!" , personalInformation)
            }catch(e:Exception){
                return RequestResultWithData.Error("Api 回傳資料轉換失敗，"+e);
            }
        }
    }


    fun convertPersonalInformationOnFailure(call: Call<ResponseBody>, t: Throwable): RequestResultWithData<PersonalInformation> {
        val failureMessage = ConvertResponseFailureMessage(call,t)
        return RequestResultWithData.Failure(failureMessage)
    }

}