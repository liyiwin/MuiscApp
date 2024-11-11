package com.example.musicapp.localDatase.sqLite

import com.example.musicapp.bean.local.FavoriteTrack
import com.example.musicapp.bean.remote.Track

interface IFavoriteTracksStorage {

    fun addFavoriteTrack(track: Track)

    fun removeFavoriteTrack(trackId:String)

    fun getTotalFavoriteTracks():ArrayList<FavoriteTrack>

    fun checkTrackIsAdded(trackId:String):Boolean
}