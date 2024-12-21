package com.example.musicapp.tool.information

import android.content.Context

object AppInformationUtil {


    fun getAppVersion(context: Context):String{
        var version = ""
        try{
            val manager = context.getPackageManager();
            val  info = manager.getPackageInfo(context.getPackageName(), 0);
            version = info.versionName?:"";
        }catch (e:Exception){

        }
        return version;
    }


}