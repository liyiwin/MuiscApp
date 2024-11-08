package com.example.musicapp.localDatase

import com.example.musicapp.bean.local.TrackListTransferData
import com.example.musicapp.bean.remote.Track

object RouterDataStorage {

    val  trackListTransferDataStack = ArrayList<TrackListTransferData>()
    val trackStack = ArrayList<Track>()
    val tracklistTitle = ArrayList<String>()
    fun putTrackListTransferData(data:TrackListTransferData){
        trackListTransferDataStack.add(data)
    }

    fun popTrackListTransferData(){
        if(trackListTransferDataStack.isNotEmpty()) trackListTransferDataStack.removeAt(trackListTransferDataStack.count()-1)
    }

    fun getTrackListTransferData(): TrackListTransferData?{
        return if(trackListTransferDataStack.isEmpty()) null else trackListTransferDataStack.last()
    }

    fun putTrack(track: Track){
        trackStack.add(track)
    }

    fun popTrack(){
        if(trackStack.isNotEmpty()) trackStack.removeAt(trackStack.count()-1)
    }

    fun putTrackListTitle( title:String){
        tracklistTitle.add(title)
    }

    fun removeCurrentTrackListTitle(){
          if(!tracklistTitle.isEmpty() ){
              tracklistTitle.removeAt(tracklistTitle.size-1)
          }
    }

    fun getTrackListTitle():String{
        if(!tracklistTitle.isEmpty() )return tracklistTitle[tracklistTitle.size-1]
       else return ""
    }

    fun getTrack():Track? {
        return if(trackStack.isEmpty()) null else trackStack.last()
    }
}