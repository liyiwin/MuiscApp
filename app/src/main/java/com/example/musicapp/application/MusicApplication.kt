package com.example.musicapp.application

import android.app.Application
import com.example.musicapp.tool.file.IPlayListManager
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MusicApplication  : Application(){
     @Inject
     lateinit var playListManager: IPlayListManager

    override fun onCreate() {
        super.onCreate()
        playListManager.createAppDirIfNotExist()
    }

}