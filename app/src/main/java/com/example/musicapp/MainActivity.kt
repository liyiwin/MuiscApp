package com.example.musicapp

import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.ExperimentalComposeUiApi
import com.example.musicapp.tool.network.NetWorkSubject
import com.example.musicapp.tool.network.Observer
import com.example.musicapp.viewmodel.global.NetWorkViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var netWorkConnectionObserver: Observer<Boolean>? = null
    private var netWorkSubject: NetWorkSubject? = null
    private val netWorkViewModel: NetWorkViewModel by viewModels()

    @ExperimentalComposeUiApi
    @ExperimentalMaterialApi
    @ExperimentalPagerApi
    @ExperimentalFoundationApi
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent{
            MainView(netWorkViewModel)
       }
        setNetWorkSubject()
        registerNetWorkConnectionStatus()
    }

    override fun onDestroy() {
        super.onDestroy()
       unRegisterNetWorkConnectionStatus()
    }




    private fun setNetWorkSubject(){
        netWorkSubject = NetWorkSubject(this)
    }

    private fun registerNetWorkConnectionStatus() {
        if (netWorkConnectionObserver == null) {
            netWorkConnectionObserver = object : Observer<Boolean> {
                override fun update(data: Boolean) {
                    runOnUiThread {
                        netWorkViewModel.updateConnectStatus()
                    }
               }
            }
            netWorkSubject!!.AddObserver(netWorkConnectionObserver!!)
            netWorkSubject!!.registerNetWorkConnection()
        }
    }

    private fun unRegisterNetWorkConnectionStatus() {
        if (netWorkConnectionObserver != null) {
            netWorkSubject!!.removeObserver(netWorkConnectionObserver!!)
            netWorkSubject!!.unRegisterNetWorkConnection()
            netWorkConnectionObserver = null
        }
    }
}