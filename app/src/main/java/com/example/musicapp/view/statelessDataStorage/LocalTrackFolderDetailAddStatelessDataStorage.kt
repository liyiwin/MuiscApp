package com.example.musicapp.view.statelessDataStorage

import android.content.Context
import android.net.Uri
import com.example.musicapp.localDatase.RouterDataStorage
import com.example.musicapp.tool.file.FileUtils
import com.example.musicapp.tool.file.Mp3Utils

class LocalTrackFolderDetailAddStatelessDataStorage(val context:Context) {
    var trackUri: Uri? = null
    var trackName:String = ""
    var artistName:String = ""
    var coverImageUri: Uri? = null



    fun checkIsInputFinished():Boolean{
         return trackUri!=null && trackName.isNotEmpty() && artistName.isNotEmpty()  && coverImageUri!=null
    }

    fun saveTrack():Boolean{
        val contentResolver = context.contentResolver
        val trackFile = FileUtils.uriToFile(trackUri!!,contentResolver,RouterDataStorage.localPlayList!!,trackName+"${System.currentTimeMillis()}"+".mp3")
        val coverImage = FileUtils.uriToFile(coverImageUri!!,contentResolver,context.filesDir,trackName+"(封面)"+"${System.currentTimeMillis()}"+".jpg")
        if(trackFile == null  || coverImage == null) return false
        else{
            Mp3Utils.setMp3Metadata(trackFile,trackName,artistName,"",coverImage)
            return true
        }
    }

}