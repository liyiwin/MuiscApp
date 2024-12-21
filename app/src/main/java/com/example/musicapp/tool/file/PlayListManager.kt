package com.example.musicapp.tool.file

import android.content.Context
import android.util.Log
import java.io.File

class PlayListManager (private  val  context: Context): IPlayListManager {

    override fun createAppDirIfNotExist() {
        val appDir =  File(context.getFilesDir(), "/MusicApp")
        if(!appDir.exists()){
            appDir.mkdirs()
        }
    }
    override fun createPlayList(playlistName: String) {
        val file = File(context.getFilesDir(), "/MusicApp")
        val playList = File(file, "/$playlistName")
        val isSuccess = playList.mkdirs()
        Log.d("createPlayList","$isSuccess")
    }

    override fun getTotalPlayList() :Array<File>?{
        val file = File(context.getFilesDir(), "/MusicApp")
        return  file.listFiles()
    }

    override fun deletePlayList(file: File) {
         if(file.exists()) {
             if (file.isDirectory) {
                 file.listFiles()?.forEach { child ->
                     if (child.exists())   child.delete()
                 }
             }
             file.delete()
         }

    }

    override fun getTotalTracks(playlistName: String) :Array<File>?{
        val file = File(context.getFilesDir(), "/MusicApp")
        val playList = File(file, "/$playlistName")
        return   playList .listFiles()
    }

    override fun deleteTrack(file: File) {
        if(file.exists()) {
            if (file.isDirectory) {
                file.listFiles()?.forEach { child ->
                    if (child.exists())   child.delete()
                }
            }
            file.delete()
        }
    }

}