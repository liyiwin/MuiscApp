package com.example.musicapp.viewmodel.global

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.musicapp.tool.network.INetWorkConnectionChecker
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NetWorkViewModel@Inject constructor(
   private val networkStatusChecker: INetWorkConnectionChecker
) : ViewModel() {

    private val netWorkConnectStatus : MutableLiveData<Boolean> = MutableLiveData<Boolean>(true)

    fun getConnectStatus () = netWorkConnectStatus

    fun updateConnectStatus (){
        netWorkConnectStatus.value = networkStatusChecker.isConnectionNormal()
    }


}