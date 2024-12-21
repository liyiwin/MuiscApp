package com.example.musicapp.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.musicapp.bean.local.MP3Metadata
import com.example.musicapp.localDatase.RouterDataStorage
import com.example.musicapp.tool.file.IPlayListManager
import com.example.musicapp.tool.file.Mp3Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MusicPlayerViewModel@Inject constructor(
    private val playListManager: IPlayListManager
):ViewModel(){

    val tracks: MutableLiveData<List<MP3Metadata>> by lazy{ MutableLiveData<List<MP3Metadata>>(listOf()) }
    val currentIndex : MutableLiveData<Int> by lazy {  MutableLiveData<Int>(RouterDataStorage.localTrackIndex)}
    val isPause : MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>(true)}
    val isCycle:MutableLiveData<Boolean> by lazy {MutableLiveData<Boolean>(false)}
    val isRandom:MutableLiveData<Boolean> by lazy {MutableLiveData<Boolean>(false)}
    val getCurrentTrack = MediatorLiveData<MP3Metadata>() .apply {
        val update = {
            if(tracks.value!!.isNotEmpty())  value =  tracks.value!!.get(currentIndex.value!!)
        }
        addSource(tracks){update()}
        addSource(currentIndex){update()}
    }

    fun init(){
        currentIndex.postValue(RouterDataStorage.localTrackIndex)
        playListManager.getTotalTracks(RouterDataStorage.localPlayList!!.name)?.let { totalTracks ->
            val result = ArrayList<MP3Metadata>()
            totalTracks.toList().forEach { file -> result.add(Mp3Utils.getMp3MetaData(file))}
            tracks.postValue(result)
        }
    }

    fun cleanData(){
        tracks.postValue(listOf())
        isPause.postValue(true)
        isCycle.postValue(false)
        isRandom.postValue(false)
        getCurrentTrack.postValue(null)
    }

    fun changePlayState(){
        isPause.value = ! isPause.value!!
    }

    fun playNextTrack(){
        if(tracks.value!!.isNotEmpty()){
            val size = tracks.value!!.size
            if(isRandom.value!!){
                 val indexs = ((0 until size).toList() - currentIndex.value)
                currentIndex.value = indexs.random()
            }
            else if(isCycle.value!!){
                currentIndex.value = currentIndex.value
            }
            else{
                if(currentIndex.value!! == size-1) currentIndex.value = 0
                else currentIndex.value = currentIndex.value!!+1
            }
        }
    }


    fun jumpToLastTrack(){
          isCycle.value = false
          if(tracks.value!!.isNotEmpty()){
               val size = tracks.value!!.size
              if(isRandom.value!!){
                  val indexs = ((0 until size).toList() - currentIndex.value)
                  currentIndex.value =indexs.random()
              }else{
                  if(currentIndex.value!! == 0) currentIndex.value = size-1
                  else currentIndex.value = currentIndex.value!!-1
              }
          }
    }

    fun jumpToNextTrack(){
        isCycle.value = false
        if(tracks.value!!.isNotEmpty()){
            val size = tracks.value!!.size
            if(isRandom.value!!){
                val indexs = ((0 until size).toList() - currentIndex.value)
                currentIndex.value = indexs.random()
            }else{
                if(currentIndex.value!! == size-1) currentIndex.value = 0
                else currentIndex.value = currentIndex.value!!+1
            }
        }
    }

    fun triggerRandom(){
        isRandom.value = ! isRandom.value!!
        isCycle.value = false
    }

    fun triggerCycle(){
        isRandom.value = false
        isCycle.value = ! isCycle.value!!
    }

}