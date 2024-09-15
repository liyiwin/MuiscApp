package com.example.musicapp.localDatase

import com.example.musicapp.bean.local.TrackListTransferData
import com.example.musicapp.bean.remote.Track

object RouterDataStorage {

    val  trackListTransferDataStack = ArrayList<TrackListTransferData>()
    val trackStack = ArrayList<Track>()
    fun putTrackListTransferData(data:TrackListTransferData){
        trackListTransferDataStack.add(data)
    }

    fun popTrackListTransferData(){
        if(trackListTransferDataStack.isNotEmpty()) trackListTransferDataStack.removeLast()
    }

    fun getTrackListTransferData(): TrackListTransferData?{
        return if(trackListTransferDataStack.isEmpty()) null else trackListTransferDataStack.last()
    }

    fun putTrack(track: Track){
        trackStack.add(track)
    }

    fun popTrack(){
        if(trackStack.isNotEmpty()) trackStack.removeLast()
    }

    fun getTrack():Track? {
        return if(trackStack.isEmpty()) null else trackStack.last()
    }
}