package com.example.musicapp.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.bean.local.TrackListTransferData
import com.example.musicapp.bean.remote.Track
import com.example.musicapp.data.repository.IFetchDataRepository
import com.example.musicapp.data.requestResult.RequestResultWithData
import com.example.musicapp.localDatase.ISaveAppSettingInApp
import com.example.musicapp.localDatase.ISaveUserInfoInApp
import com.example.musicapp.localDatase.RouterDataStorage
import com.example.musicapp.tool.extensionfunction.addListAndRemoveDuplicate
import com.example.musicapp.viewmodel.state.RequestState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class TrackListViewModel@Inject constructor(
    private val repo: IFetchDataRepository,
    private  val saveUserInfoInApp: ISaveUserInfoInApp,
    private val saveAppSettingInApp: ISaveAppSettingInApp,
): ViewModel() {


    private val tracks : MutableLiveData<ArrayList<Track>> by lazy {
        MutableLiveData<ArrayList<Track>>(ArrayList())
    }

    private  val requestState: MutableLiveData<RequestState> by lazy{ MutableLiveData<RequestState>(RequestState.None("")) }


    var currentPage = 0

    var isLastPage = false

    fun getTotalTracks() = tracks

    fun getRequestDisplayState() = requestState

    fun clearRequestDisplayState() {
        requestState.postValue(RequestState.None(""))
    }


    fun resetData(){
        tracks.value = ArrayList()
        currentPage = 0
        isLastPage = false
    }


    fun UpdatePlayList(){
        Log.d("currentPage","Update")
        RouterDataStorage.getTrackListTransferData()?.let {
            when(it){

                is TrackListTransferData.ChartPlayListTracksTransferData -> UpdateTracksInChart(it.playListId)

                is TrackListTransferData.FeaturedPlayListTracksTransferData ->UpdateTracksInFeaturedPlayList(it.playListId)

                is TrackListTransferData.HitPlayListTracksTransferData -> UpdateTracksInHitPlayList(it.playListId)

                is TrackListTransferData.ArtistTopTracksTransferData -> UpdateArtistTopTracks(it.artistd)

            }
        }
    }

    fun LoadPlayList(){
        Log.d("currentPage","Load")
        RouterDataStorage.getTrackListTransferData()?.let{
            when(it){

                is TrackListTransferData.ChartPlayListTracksTransferData -> LoadTracksInChart(it.playListId)

                is TrackListTransferData.FeaturedPlayListTracksTransferData ->LoadTracksInFeaturedPlayList(it.playListId)

                is TrackListTransferData.HitPlayListTracksTransferData -> LoadTracksInHitPlayList(it.playListId)

                is TrackListTransferData.ArtistTopTracksTransferData -> LoadArtistTopTracks(it.artistd)

            }
        }
    }

    private fun   UpdateTracksInChart(playListId:String){
        requestState.postValue(RequestState.Loading(""))
        viewModelScope.launch {
            val requestResult = repo.getTracksInChart(playListId,currentPage.toString(),"TW")
            when(requestResult){
                is RequestResultWithData.Success ->{
                    requestState.postValue(RequestState.Success(requestResult.message))
                    Log.d("currentPage","currentPage = "+currentPage)
                    Log.d("currentPage","currentPage = "+requestResult.data.tracks.get(0).name)
                    currentPage+=1
                    isLastPage = requestResult.message == "已到達最後一頁"
                    tracks.value = ArrayList(requestResult.data.tracks)
                }
                is  RequestResultWithData.Failure->{
                    requestState.postValue(RequestState.Failure(requestResult.message))
                }
                is RequestResultWithData.Error -> {
                    requestState.postValue(RequestState.Failure(requestResult.message))
                }
            }
        }
    }

    private fun LoadTracksInChart(playListId:String){
        if(isLastPage) return
        requestState.postValue(RequestState.Loading(""))
        viewModelScope.launch{
            val requestResult = repo.getTracksInChart(playListId,currentPage.toString(),"TW")
            when(requestResult){
                is RequestResultWithData.Success ->{
                    requestState.postValue(RequestState.Success(requestResult.message))
                    Log.d("currentPage","currentPage = "+currentPage)
                    Log.d("currentPage","currentPage = "+requestResult.data.tracks.get(0).name)
                    currentPage+=1
                    isLastPage = requestResult.message == "已到達最後一頁"
                    tracks.value = tracks.value!!.addListAndRemoveDuplicate(requestResult.data.tracks)
                }
                is  RequestResultWithData.Failure->{
                    requestState.postValue(RequestState.Failure(requestResult.message))
                }
                is RequestResultWithData.Error -> {
                    requestState.postValue(RequestState.Failure(requestResult.message))
                }
            }
        }
    }

    private fun  UpdateTracksInFeaturedPlayList(playListId:String){
        requestState.postValue(RequestState.Loading(""))
        viewModelScope.launch {
            val requestResult = repo.getTracksInFeaturedPlayList(playListId,currentPage.toString(),"TW")
            when(requestResult){
                is RequestResultWithData.Success ->{
                    requestState.postValue(RequestState.Success(requestResult.message))
                    Log.d("currentPage","currentPage = "+currentPage)
                    Log.d("currentPage","currentPage = "+requestResult.data.tracks.get(0).name)
                    currentPage+=1
                    isLastPage = requestResult.message == "已到達最後一頁"
                    tracks.value = ArrayList(requestResult.data.tracks)
                }
                is  RequestResultWithData.Failure->{
                    requestState.postValue(RequestState.Failure(requestResult.message))
                }
                is RequestResultWithData.Error -> {
                    requestState.postValue(RequestState.Failure(requestResult.message))
                }
            }
        }
    }

    private fun LoadTracksInFeaturedPlayList(playListId:String){
        if(isLastPage) return
        requestState.postValue(RequestState.Loading(""))
        viewModelScope.launch{
            val requestResult = repo.getTracksInFeaturedPlayList(playListId,currentPage.toString(),"TW")
            when(requestResult){
                is RequestResultWithData.Success ->{
                    requestState.postValue(RequestState.Success(requestResult.message))
                    Log.d("currentPage","currentPage = "+currentPage)
                    Log.d("currentPage","currentPage = "+requestResult.data.tracks.get(0).name)
                    currentPage+=1
                    isLastPage = requestResult.message == "已到達最後一頁"
                    tracks.value = tracks.value!!.addListAndRemoveDuplicate(requestResult.data.tracks)
                }
                is  RequestResultWithData.Failure->{
                    requestState.postValue(RequestState.Failure(requestResult.message))
                }
                is RequestResultWithData.Error -> {
                    requestState.postValue(RequestState.Failure(requestResult.message))
                }
            }
        }
    }

    private fun  UpdateTracksInHitPlayList(playListId:String){
        requestState.postValue(RequestState.Loading(""))
        viewModelScope.launch {
            val requestResult = repo.getTracksInNewHitsPlayList(playListId,currentPage.toString(),"TW")
            when(requestResult){
                is RequestResultWithData.Success ->{
                    requestState.postValue(RequestState.Success(requestResult.message))
                    Log.d("currentPage","currentPage = "+currentPage)
                    Log.d("currentPage","currentPage = "+requestResult.data.tracks.get(0).name)
                    currentPage+=1
                    isLastPage = requestResult.message == "已到達最後一頁"
                    tracks.value = ArrayList(requestResult.data.tracks)
                }
                is  RequestResultWithData.Failure->{
                    requestState.postValue(RequestState.Failure(requestResult.message))
                }
                is RequestResultWithData.Error -> {
                    requestState.postValue(RequestState.Failure(requestResult.message))
                }
            }

        }
    }

    private fun LoadTracksInHitPlayList(playListId:String){
        if(isLastPage) return
        requestState.postValue(RequestState.Loading(""))
        viewModelScope.launch {
            val requestResult =  repo.getTracksInNewHitsPlayList(playListId,currentPage.toString(),"TW")
            when(requestResult){
                is RequestResultWithData.Success ->{
                    requestState.postValue(RequestState.Success(requestResult.message))
                    Log.d("currentPage","currentPage = "+currentPage)
                    Log.d("currentPage","currentPage = "+requestResult.data.tracks.get(0).name)
                    currentPage+=1
                    isLastPage = requestResult.message == "已到達最後一頁"
                    tracks.value = tracks.value !!.addListAndRemoveDuplicate(requestResult.data.tracks)
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

    private fun UpdateArtistTopTracks(artistId:String){
        requestState.postValue(RequestState.Loading(""))
        viewModelScope.launch {
            val requestResult = repo.getTopTracksOfArtist(artistId,currentPage.toString(),"TW")
            when(requestResult){
                is RequestResultWithData.Success ->{
                    requestState.postValue(RequestState.Success(requestResult.message))
                    Log.d("currentPage","currentPage = "+currentPage)
                    Log.d("currentPage","currentPage = "+requestResult.data.get(0).name)
                    currentPage+=1
                    isLastPage = requestResult.message == "已到達最後一頁"
                    tracks.value = ArrayList(requestResult.data)
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

    private fun LoadArtistTopTracks(artistId:String){
        if(isLastPage) return
        requestState.value = RequestState.Loading("")
        viewModelScope.launch{
            val requestResult = repo.getTopTracksOfArtist(artistId,currentPage.toString(),"TW")
            when(requestResult){
                is RequestResultWithData.Success ->{
                    requestState.value = RequestState.Success(requestResult.message)
                    Log.d("currentPage","currentPage = "+currentPage)
                    Log.d("currentPage","currentPage = "+requestResult.data.get(0).name)
                    currentPage+=1
                    isLastPage = requestResult.message == "已到達最後一頁"
                    tracks.value = tracks.value!!.addListAndRemoveDuplicate(requestResult.data)
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