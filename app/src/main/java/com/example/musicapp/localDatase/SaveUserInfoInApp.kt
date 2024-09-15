package com.example.musicapp.localDatase

import android.content.Context

class SaveUserInfoInApp(private val context: Context):ISaveUserInfoInApp  {

    private val dataToken = "SaveUserInfo"

    private fun getSharePreference() = context.getSharedPreferences(dataToken, Context.MODE_PRIVATE)

    override fun setToken(token:String) = getSharePreference().edit().putString("token",token).apply();

    override fun getToken():String = getSharePreference().getString("token","")!!;
}