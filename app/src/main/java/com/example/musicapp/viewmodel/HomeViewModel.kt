package com.example.musicapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.bean.remote.PlayList
import com.example.musicapp.data.repository.IFetchDataRepository
import com.example.musicapp.data.requestResult.RequestResultWithData
import com.example.musicapp.viewmodel.state.RequestState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel@Inject constructor(private val fetchDataRepository: IFetchDataRepository): ViewModel(){

    private val chartList : MutableLiveData<List<PlayList>> by lazy{  MutableLiveData<List<PlayList>>(ArrayList()) }

    private  val featuredPlayLists : MutableLiveData<List<PlayList>> by lazy{  MutableLiveData<List<PlayList>>(ArrayList()) }

    private  val hitMusicList: MutableLiveData<List<PlayList>> by lazy{  MutableLiveData<List<PlayList>>(ArrayList()) }

    private  val requestState: MutableLiveData<RequestState> by lazy{ MutableLiveData<RequestState>(RequestState.None("")) }

    private  var isInitialized =  false

    fun getIsInitialized() = isInitialized;

    fun setIsInitialized(value:Boolean){
        isInitialized =value;
    }

    fun getTotalChartList() = chartList

    fun getTotalFeaturedPlayLists() = featuredPlayLists

    fun getTotalHitMusicList() = hitMusicList

    fun getRequestDisplayState() = requestState

    fun clearRequestDisplayState() {
        requestState.postValue(RequestState.None(""))
    }


    fun UpdateTotalChartList(){
        viewModelScope.launch(Dispatchers.Default) {
            requestState.postValue(RequestState.Loading(""))
            val requestResult = fetchDataRepository.getTotalCharts("TW")
            when(requestResult){
                is RequestResultWithData.Success ->{
                    requestState.postValue(RequestState.Success(requestResult.message))
                    chartList.postValue(requestResult.data)
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

    fun  UpdateTotalFeaturedPlayListCategories(){
        viewModelScope.launch(Dispatchers.Default){
            requestState.postValue(RequestState.Loading(""))
            val requestResult = fetchDataRepository.getTotalFeaturedPlayList("TW")
            when(requestResult){
                is RequestResultWithData.Success ->{
                    requestState.postValue(RequestState.Success(requestResult.message))
                    featuredPlayLists.postValue(requestResult.data)
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

    fun UpdateTotalHitMusicList(){
        viewModelScope.launch(Dispatchers.Default){
            requestState.postValue(RequestState.Loading(""))
            val requestResult = fetchDataRepository.getTotalNewHitsPlayLists("TW")
            when(requestResult){
                is RequestResultWithData.Success ->{
                    requestState.postValue(RequestState.Success(requestResult.message))
                    hitMusicList.postValue(requestResult.data)
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
}