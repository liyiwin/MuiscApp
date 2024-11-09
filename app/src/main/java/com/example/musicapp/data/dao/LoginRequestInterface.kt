package com.example.musicapp.data.dao

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface LoginRequestInterface {

    @POST("/oauth2/token")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    fun doLogin(
        @Field("grant_type")  grantType:String,
        @Field("code") code:String ,
        @Field("client_id")  clientId:String,
        @Field("client_secret")  clientSecret:String,
    ): Call<ResponseBody>;

    @POST("/oauth2/token")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    fun doReLogin(
        @Field("grant_type")  grantType:String,
        @Field("refresh_token") refreshToken:String,
        @Header("Authorization") authorization:String
    ): Call<ResponseBody>;
}