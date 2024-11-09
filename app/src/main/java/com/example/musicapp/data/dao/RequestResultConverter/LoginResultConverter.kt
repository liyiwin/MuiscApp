package com.example.musicapp.data.dao.RequestResultConverter

import com.example.musicapp.bean.remote.LoginResult
import com.example.musicapp.bean.remote.PlayList
import com.example.musicapp.bean.remote.ReLoginResult
import com.example.musicapp.data.requestResult.RequestResultWithData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response

class LoginResultConverter : RequestResultConverter(){

    fun convertLoginResult(response: Response<ResponseBody>): RequestResultWithData<LoginResult> {
        val responseCode = response.code();
        if(responseCode >= 300 ){
            val errorMessage = ConvertResponseErrorMessage(response)
            return RequestResultWithData.Error(errorMessage)
        }else{
            return try{
                val body = response.body();
                val jsonObject = getJsonObjectFromResponseBody(body);
                if(!jsonObject.isNull("access_token") && !jsonObject.isNull("refresh_token")){
                    val accessToken = jsonObject.getString("access_token");
                    val expireTime = jsonObject.getInt("expires_in")
                    val tokenType = jsonObject.getString("token_type")
                    val refreshToken = jsonObject.getString("refresh_token")
                    RequestResultWithData.Success("登入成功",LoginResult(accessToken,expireTime,tokenType,refreshToken))
                }else {
                    RequestResultWithData.Error("登入失敗");
                }
            }catch (e:Exception){
                RequestResultWithData.Error("Api 回傳資料轉換失敗，$e");
            }
        }
    }

    fun convertLoginFailure(call: Call<ResponseBody>, t: Throwable):RequestResultWithData<LoginResult>{
        val failureMessage = ConvertResponseFailureMessage(call,t)
        return RequestResultWithData.Failure(failureMessage)
    }

    fun convertReLoginResult(response: Response<ResponseBody>): RequestResultWithData<ReLoginResult>{
        val responseCode = response.code();
        if(responseCode >= 300 ){
            val errorMessage = ConvertResponseErrorMessage(response)
            return RequestResultWithData.Error(errorMessage)
        }else{
            return  try {
                val body = response.body();
                val jsonObject = getJsonObjectFromResponseBody(body);
                if(!jsonObject.isNull("access_token") && !jsonObject.isNull("refresh_token")){
                    val accessToken = jsonObject.getString("access_token");
                    val expireTime = jsonObject.getInt("expires_in")
                    val tokenType = jsonObject.getString("token_type")
                    val refreshToken = jsonObject.getString("refresh_token")
                    RequestResultWithData.Success("重新登入成功",ReLoginResult(accessToken,expireTime,tokenType,refreshToken))
                }else {
                    RequestResultWithData.Error("重新登入失敗");
                }
            }catch (e:Exception){
                RequestResultWithData.Error("重新登入失敗");
            }
        }
    }

    fun convertReLoginFailure(call: Call<ResponseBody>, t: Throwable): RequestResultWithData<ReLoginResult>{
        val failureMessage = ConvertResponseFailureMessage(call,t)
        return RequestResultWithData.Failure(failureMessage)
    }
}