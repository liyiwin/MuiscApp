package com.example.musicapp.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.musicapp.bean.local.EditingFile
import com.example.musicapp.bean.local.EditingMP3Metadata
import com.example.musicapp.bean.local.MP3Metadata
import com.example.musicapp.localDatase.RouterDataStorage
import com.example.musicapp.tool.file.IPlayListManager
import com.example.musicapp.tool.file.Mp3Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import javax.inject.Inject

@HiltViewModel
class LocalTrackFolderDetailViewModel@Inject constructor(
    private val playListManager: IPlayListManager
): ViewModel()  {

    val tracks: MutableLiveData<List<MP3Metadata>> by lazy{ MutableLiveData<List<MP3Metadata>>(listOf()) }
    val editingTracks:MutableLiveData<List<EditingMP3Metadata>> by lazy{ MutableLiveData<List<EditingMP3Metadata>>(listOf()) }

    fun getFolderTracks() = tracks

    fun getEditingFolderTracks() = editingTracks

    fun updateFolderTracks(){
        playListManager.getTotalTracks(RouterDataStorage.localPlayList!!.name)?.let {  totalTracks ->
            val result = ArrayList<MP3Metadata>()
            totalTracks.toList().forEach { file ->
                result.add(Mp3Utils.getMp3MetaData(file))
            }
            tracks.postValue(result)
        }
   }

   fun updateFolderEditTracks(){
       playListManager.getTotalTracks(RouterDataStorage.localPlayList!!.name)?.let {  totalTracks ->
           val result = ArrayList<EditingMP3Metadata>()
           totalTracks.toList().forEach { file ->
               result.add(EditingMP3Metadata(false,Mp3Utils.getMp3MetaData(file)))
           }
           editingTracks.postValue(result)
       }
   }

   fun updateEditTracksState(editTrack:EditingMP3Metadata , isSelected:Boolean){
       val result = editingTracks.value!!
       result.forEach { it ->
           if(it.mP3Metadata.originFile.name == editTrack.mP3Metadata.originFile .name){
               it.isSelected = isSelected
           }
       }
       editingTracks.value = listOf()
       editingTracks.value =  result
   }

    fun resetEditTracksState(){
        val result = editingTracks.value!!
        result.forEach { it ->
            it.isSelected = false
        }
        editingTracks.value = listOf()
        editingTracks.value =  result
    }

    fun removeSelectedTrack(){
         editingTracks.value!!.forEach {
              if(it.isSelected) playListManager.deleteTrack(it.mP3Metadata.originFile)
         }
    }

}