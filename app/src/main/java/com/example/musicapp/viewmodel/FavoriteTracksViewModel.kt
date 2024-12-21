package com.example.musicapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.musicapp.bean.local.FavoriteTrack
import com.example.musicapp.localDatase.ISaveAppSettingInApp
import com.example.musicapp.localDatase.ISaveUserInfoInApp
import com.example.musicapp.localDatase.sqLite.IFavoriteTracksStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavoriteTracksViewModel@Inject constructor(
    private val saveUserInfoInApp: ISaveUserInfoInApp,
    private val saveAppSettingInApp: ISaveAppSettingInApp,
    private val favoriteTracksStorage: IFavoriteTracksStorage
) :ViewModel(){
    private val favoriteTracks : MutableLiveData<ArrayList<FavoriteTrack>> by lazy { MutableLiveData<ArrayList<FavoriteTrack>>(ArrayList())  }

    fun getTotalFavoriteTracks() = favoriteTracks

    fun updateTotalFavoriteTracks(){
        val data = favoriteTracksStorage.getTotalFavoriteTracks()
        favoriteTracks.postValue(data)
    }

    fun removeFavoriteTrack(track: FavoriteTrack){
        favoriteTracksStorage.removeFavoriteTrack(track.id)
        val data = favoriteTracksStorage.getTotalFavoriteTracks()
        favoriteTracks.postValue(data)
    }

}