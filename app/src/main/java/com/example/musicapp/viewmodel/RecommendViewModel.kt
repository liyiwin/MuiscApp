package com.example.musicapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.bean.remote.Track
import com.example.musicapp.data.repository.IFetchDataRepository
import com.example.musicapp.data.requestResult.RequestResultWithData
import com.example.musicapp.localDatase.ISaveAppSettingInApp
import com.example.musicapp.localDatase.ISaveUserInfoInApp
import com.example.musicapp.tool.extensionfunction.addListAndRemoveDuplicate
import com.example.musicapp.viewmodel.state.RequestState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecommendViewModel@Inject constructor(
    private val repo: IFetchDataRepository,
    private val saveUserInfoInApp: ISaveUserInfoInApp,
    private val saveAppSettingInApp: ISaveAppSettingInApp,
): ViewModel()  {

    private  val requestState: MutableLiveData<RequestState> by lazy{ MutableLiveData<RequestState>(RequestState.None("")) }

    private val  dailyRecommendedTracks: MutableLiveData<List<Track>> by lazy{ MutableLiveData<List<Track>>(ArrayList()) }

    private val personalRecommendedTracks: MutableLiveData<List<Track>> by lazy{ MutableLiveData<List<Track>>(ArrayList()) }

    fun getLatestRequestState() = requestState

    fun cleanLatestRequestState() = requestState.postValue(RequestState.None(""))

    fun getLatestDailyRecommendedTracks() = dailyRecommendedTracks

    fun cleanLatestDailyRecommendedTracks() { dailyRecommendedTracks.value = listOf() }

    fun getLatestPersonalRecommendedTracks() = personalRecommendedTracks

    fun cleanLatestPersonalRecommendedTracks() {personalRecommendedTracks.value  = listOf()  }

    init {
        viewModelScope.launch {
            fetchDailyRecommendedTracks()
            fetchPersonalRecommendedTracks()
        }
    }

    fun fetchDailyRecommendedTracks(){
         requestState.postValue(RequestState.Loading(""))
         viewModelScope.launch{
             val requestResult = repo.getDailyRecommendedTracks("TW","0")
             when(requestResult){
                 is RequestResultWithData.Success ->{
                     requestState.postValue(RequestState.Success(requestResult.message))
                     val data = ArrayList<Track>().addListAndRemoveDuplicate(requestResult.data,30)
                     dailyRecommendedTracks.postValue(data)
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

     fun fetchPersonalRecommendedTracks(){
         requestState.postValue(RequestState.Loading(""))
         viewModelScope.launch{
             val requestResult = repo.getPersonalRecommendedTracks("TW","0")
             when(requestResult){
                 is RequestResultWithData.Success ->{
                     requestState.postValue(RequestState.Success(requestResult.message))
                     val data = ArrayList(requestResult.data.distinct())
                     personalRecommendedTracks.postValue(data)
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