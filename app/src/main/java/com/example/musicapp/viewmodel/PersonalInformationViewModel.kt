package com.example.musicapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.bean.remote.PersonalInformation
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
class PersonalInformationViewModel@Inject constructor(
    private val repo: IFetchDataRepository,
    private val saveUserInfoInApp: ISaveUserInfoInApp,
    private val saveAppSettingInApp: ISaveAppSettingInApp,
): ViewModel()  {
    private  val requestState: MutableLiveData<RequestState> by lazy{ MutableLiveData<RequestState>(RequestState.None("")) }
    private val personalInformation:MutableLiveData<PersonalInformation> by lazy{ MutableLiveData<PersonalInformation>(null) }

    fun getInformation() = personalInformation

    fun getLatestRequestState() = requestState

    fun cleanLatestRequestState() = requestState.postValue(RequestState.None(""))

    fun fetchInformation(){
        requestState.postValue(RequestState.Loading(""))
        viewModelScope.launch{
            val requestResult = repo.getPersonalInformation()
            when(requestResult){
                is RequestResultWithData.Success ->{
                    requestState.postValue(RequestState.Success(requestResult.message))
                    personalInformation.postValue(requestResult.data)
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