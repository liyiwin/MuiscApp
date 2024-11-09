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
class LoginViewModel@Inject constructor(
    private val repo: ILoginRepository,
    private val saveUserInfoInApp: ISaveUserInfoInApp,
    private val saveAppSettingInApp: ISaveAppSettingInApp,
): ViewModel()  {

    private  val loginRequestState: MutableLiveData<RequestState> by lazy{ MutableLiveData<RequestState>(RequestState.None("")) }

    fun getRequestState() = loginRequestState

    fun cleanRequestState(){
        loginRequestState.postValue(RequestState.None(""))
    }

    fun triggerLogin(code:String){
        viewModelScope.launch(Dispatchers.Default){
            loginRequestState.postValue(RequestState.Loading(""))
            val requestResult = repo.login(code)
            when(requestResult){
                is RequestResultWithData.Success ->{
                    val loginResult = requestResult.data
                    saveUserInfoInApp.setToken(loginResult.accessToken)
                    saveUserInfoInApp.setRefreshToken(loginResult.refreshToken)
                    loginRequestState.postValue(RequestState.Success(requestResult.message))
                }
                is  RequestResultWithData.Failure->{
                  loginRequestState.postValue(RequestState.Failure(requestResult.message))
                }
                is RequestResultWithData.Error -> {
                   loginRequestState.postValue(RequestState.Failure(requestResult.message))
                }
            }
        }
    }

}