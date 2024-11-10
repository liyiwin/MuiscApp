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
class SearchViewModel@Inject constructor(
    private val repo: IFetchDataRepository,
    private val saveUserInfoInApp: ISaveUserInfoInApp,
    private val saveAppSettingInApp: ISaveAppSettingInApp,
): ViewModel()  {

    private val  searResult: MutableLiveData<ArrayList<Track>> by lazy{ MutableLiveData<ArrayList<Track>>(ArrayList()) }

    private  val requestState: MutableLiveData<RequestState> by lazy{ MutableLiveData<RequestState>(RequestState.None("")) }

    fun getSearchResult() = searResult

    fun getRequestDisplayState() = requestState

    fun clearRequestDisplayState() = requestState.postValue(RequestState.None(""))

    var currentPage = 0

    var isLastPage = false

    var keyword  = ""


    fun reset(){
        searResult.value = ArrayList()
        currentPage = 0
        isLastPage = false
        keyword  = ""
    }

    fun searchTrack(key:String){
        requestState.postValue(RequestState.Loading(""))
        viewModelScope.launch {
            val requestResult = repo.searchTrack(key ,"TW",currentPage.toString())
            when(requestResult){
                is RequestResultWithData.Success ->{
                    currentPage += 1
                    isLastPage  = requestResult.message == "已到達最後一頁"
                    keyword  = key
                    requestState.postValue(RequestState.Success(requestResult.message))
                    searResult.postValue(ArrayList(requestResult.data))
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

    fun LoadSearchResult(){
        requestState.postValue(RequestState.Loading(""))
        viewModelScope.launch{
            val requestResult = repo.searchTrack(keyword ,"TW",currentPage.toString())
            when(requestResult){
                is RequestResultWithData.Success ->{
                    currentPage += 1
                    isLastPage  = requestResult.message == "已到達最後一頁"
                    requestState.postValue(RequestState.Success(requestResult.message))
                    searResult.postValue(ArrayList(searResult.value!!.addListAndRemoveDuplicate(requestResult.data)))
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