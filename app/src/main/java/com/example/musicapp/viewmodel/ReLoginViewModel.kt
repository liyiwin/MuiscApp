package com.example.musicapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.data.repository.ILoginRepository
import com.example.musicapp.data.requestResult.RequestResultWithData
import com.example.musicapp.localDatase.ISaveAppSettingInApp
import com.example.musicapp.localDatase.ISaveUserInfoInApp
import com.example.musicapp.viewmodel.state.RequestState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReLoginViewModel@Inject constructor(
    private val repo: ILoginRepository,
    private val saveUserInfoInApp: ISaveUserInfoInApp,
    private val saveAppSettingInApp: ISaveAppSettingInApp,
): ViewModel()  {

   private  val reLoginRequestState: MutableLiveData<RequestState> by lazy{ MutableLiveData<RequestState>(RequestState.None("")) }

    fun getRequestState() = reLoginRequestState

    fun cleanRequestState(){
        reLoginRequestState.postValue(RequestState.None(""))
    }


    fun triggerReLogin(){
        viewModelScope.launch(Dispatchers.Default){
            reLoginRequestState.postValue(RequestState.Loading(""))
            val requestResult = repo.reLogin()
            when(requestResult){
                is RequestResultWithData.Success ->{
                    val reLoginResult = requestResult.data
                    saveUserInfoInApp.setToken(reLoginResult.accessToken)
                    saveUserInfoInApp.setRefreshToken(reLoginResult.refreshToken)
                    reLoginRequestState.postValue(RequestState.Success(requestResult.message))
                }
                is  RequestResultWithData.Failure->{
                    reLoginRequestState.postValue(RequestState.Failure(requestResult.message))
                }
                is RequestResultWithData.Error -> {
                    reLoginRequestState.postValue(RequestState.Failure(requestResult.message))
                }
            }
        }
    }
}