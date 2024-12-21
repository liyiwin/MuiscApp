package com.example.musicapp.data.api

import com.example.musicapp.bean.remote.LoginResult
import com.example.musicapp.bean.remote.ReLoginResult
import com.example.musicapp.data.dao.LoginRequestInterface
import com.example.musicapp.data.dao.RequestResultConverter.LoginResultConverter
import com.example.musicapp.data.requestResult.RequestResultWithData
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class LoginApiRequest(private val requestInterface: LoginRequestInterface)  {

    private val loginResultConverter = LoginResultConverter()

    suspend fun requestLogin(grantType:String,code:String,clientId:String, clientSecret:String) = suspendCoroutine<RequestResultWithData<LoginResult>> { continuation ->
        requestInterface.doLogin(grantType,code,clientId,clientSecret).enqueue(object: ResponseHandler() {
            override fun receiveResponse(call: Call<ResponseBody>, response: Response<ResponseBody>)=continuation.resume(loginResultConverter.convertLoginResult(response))
            override fun  receiveFailure(call: Call<ResponseBody>, t: Throwable) = continuation.resume(loginResultConverter.convertLoginFailure(call,t))

        })
    }

    suspend fun requestReLogin(grantType:String,refreshToken:String,authorization:String) = suspendCoroutine<RequestResultWithData<ReLoginResult>> { continuation ->
        requestInterface.doReLogin(grantType, refreshToken, authorization).enqueue(object: ResponseHandler() {
            override fun  receiveResponse( call: Call<ResponseBody>,response: Response<ResponseBody>) = continuation.resume(loginResultConverter.convertReLoginResult(response))
            override fun  receiveFailure(call: Call<ResponseBody>, t: Throwable) = continuation.resume(loginResultConverter.convertReLoginFailure(call, t))
        })
   }

}