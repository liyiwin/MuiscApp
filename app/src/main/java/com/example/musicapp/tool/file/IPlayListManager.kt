package com.example.musicapp.tool.file

import android.net.Uri
import java.io.File

interface IPlayListManager {
    fun createAppDirIfNotExist()

    fun createPlayList( playlistName:String)

    fun getTotalPlayList():Array<File>?

    fun deletePlayList(file:File)

    fun getTotalTracks( playlistName:String):Array<File>?

    fun deleteTrack(file:File)

}