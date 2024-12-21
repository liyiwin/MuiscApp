package com.example.musicapp.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.musicapp.bean.local.EditingFile
import com.example.musicapp.tool.file.IPlayListManager
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import javax.inject.Inject

@HiltViewModel
class LocalTrackFoldersVIewModel @Inject constructor(
    private val playListManager: IPlayListManager
):ViewModel() {

    val playLists:MutableLiveData<List<File>> by lazy{ MutableLiveData<List<File>>(listOf()) }
    val editingPayLists:MutableLiveData<List<EditingFile>> by lazy{ MutableLiveData<List<EditingFile>>(listOf()) }

    fun getTotalPlayLists() = playLists

    fun getTotalEditingPlayLists() = editingPayLists

    fun updateTotalPlayLists(){
        playListManager.getTotalPlayList()?.let { totalPlayLists ->
            playLists.postValue(totalPlayLists.toList())
        }
    }

    fun updateTotalEditingPlayLists(){
        playListManager.getTotalPlayList()?.let { totalPlayLists ->
            val convertResult = ArrayList<EditingFile>()
            totalPlayLists.forEach { playList ->
                convertResult.add(EditingFile(false,playList))
            }
            editingPayLists.postValue(convertResult)
        }
    }

    fun updateEditingPlayList(playList:EditingFile,isSelected:Boolean){
           val result = editingPayLists.value!!
            result.forEach {
                if(it.file.name == playList.file.name){
                    it.isSelected = isSelected
                }
            }
            Log.d("updateEditingPlayList","$result");
             editingPayLists.value = listOf()
             editingPayLists.value = result

    }

    fun resetEditingPlayList(){
        val result = editingPayLists.value!!
        result.forEach {
            it.isSelected = false
        }
        editingPayLists.value = listOf()
        editingPayLists.value = result
    }

    fun addPlayList(playListName:String){
        playListManager.createPlayList(playListName)
    }

    fun removeSelectedPlayList(){
        editingPayLists.value!!.forEach {
            if(it.isSelected) playListManager.deletePlayList(it.file)
        }
    }



}