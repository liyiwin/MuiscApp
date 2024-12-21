package com.example.musicapp.data.api

import android.util.Log
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

abstract class ResponseHandler: Callback<ResponseBody> {

    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
        Log.d("ResponseHandler onResponse","url ${call.request()}  responseCode ${response.code()} ");
        receiveResponse(call, response)
    }

    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
        Log.d("ResponseHandler onFailure"," url ${call.request()}  throwable  $t");
        receiveFailure(call,t)
    }

    abstract fun receiveResponse(call: Call<ResponseBody>, response: Response<ResponseBody>)

    abstract fun receiveFailure(call: Call<ResponseBody>, t: Throwable)

}