package com.example.musicapp.viewmodel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.bean.remote.Track
import com.example.musicapp.data.repository.IFetchDataRepository
import com.example.musicapp.data.requestResult.RequestResultWithData
import com.example.musicapp.localDatase.ISaveAppSettingInApp
import com.example.musicapp.localDatase.ISaveUserInfoInApp
import com.example.musicapp.localDatase.RouterDataStorage
import com.example.musicapp.viewmodel.state.RequestState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrackDetailViewModel@Inject constructor(
    private val repo: IFetchDataRepository,
    private val saveUserInfoInApp: ISaveUserInfoInApp,
    private val saveAppSettingInApp: ISaveAppSettingInApp,
): ViewModel()  {

    private val trackName : MutableLiveData<String> by lazy { MutableLiveData<String>("") }
    private val albumName: MutableLiveData<String> by lazy { MutableLiveData<String>("") }
    private val artistName: MutableLiveData<String> by lazy { MutableLiveData<String>("") }
    private val trackId: MutableLiveData<String> by lazy { MutableLiveData<String>("") }
    private val albumId: MutableLiveData<String> by lazy { MutableLiveData<String>("") }
    private val artistId: MutableLiveData<String> by lazy { MutableLiveData<String>("") }
    private val imageUrl : MutableLiveData<String> by lazy { MutableLiveData<String>("") }
    private val otherTracks: MutableLiveData<List<Track>> by lazy { MutableLiveData<List<Track>>(listOf()) }
    private val title = MediatorLiveData<String>().apply {
        val update = {
            value =  "歌名:" +trackName.value + "\n"  + "專輯:" + albumName.value
        }
        addSource(trackName){update()}
        addSource(albumName){update()}
    }
    private val trackUrl : MutableLiveData<String> by lazy { MutableLiveData<String>("") }
    private val requestState: MutableLiveData<RequestState> by lazy{ MutableLiveData<RequestState>(RequestState.None("")) }


    fun getTitle() = title

    fun getUrl() = trackUrl

    fun getName() = trackName

    fun getArtistNameOfTrack () = artistName

    fun getId () = trackId

    fun getArtistIdOfTrack() = artistId

    fun getTrackImageUrl() = imageUrl;

    fun getTracks() = otherTracks

    fun getRequestDisplayState() = requestState

    fun clearRequestDisplayState() {
        requestState.postValue(RequestState.None(""))
    }


    fun init(){
        val track = RouterDataStorage.getTrack()
        trackName.postValue(track?.name?:"")
        albumName.postValue(track?.album?.name?:"")
        artistName.postValue(track?.album?.artist?.name?:"")
        trackId.postValue(track?.id?:"")
        albumId.postValue(track?.album?.id?:"")
        artistId.postValue(track?.album?.artist?.id?:"")
        imageUrl.postValue(track?.album?.images?.get(1)?.url?:"")
        trackUrl.postValue(track?.url?:"")
        otherTracks.postValue(listOf())
    }

    fun UpdateArtistTopTracks(artistId:String){
        viewModelScope.launch{
            requestState.postValue(RequestState.Loading(""))
            val requestResult = repo.getTopTracksOfArtist(artistId,"0","TW")
            when(requestResult){
                is RequestResultWithData.Success-> {
                    requestState.postValue(RequestState.Success(requestResult.message))
                    otherTracks.postValue(requestResult.data)
                }
                is  RequestResultWithData.Failure->{
                    requestState.postValue(RequestState.Failure(requestResult.message))
                }
                is RequestResultWithData.Error ->{
                    requestState.postValue(RequestState.Failure(requestResult.message))
                }
            }
        }
    }


}