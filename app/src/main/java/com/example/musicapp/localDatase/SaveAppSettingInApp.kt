package com.example.musicapp.localDatase

import android.content.Context

class SaveAppSettingInApp(private val context: Context):ISaveAppSettingInApp {

    private val dataToken = "SaveAppSetting"
    private fun getSharePreference() = context.getSharedPreferences(dataToken, Context.MODE_PRIVATE);

    override fun cleanData(){
        getSharePreference().edit().clear().apply()
    }

}