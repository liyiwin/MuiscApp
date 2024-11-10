package com.example.musicapp

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
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
    private val externalPageNavigationController: (uri: Uri)->Unit  = {   uri ->
       this@MainActivity.startActivity(Intent(Intent.ACTION_VIEW, uri))
    }

    @ExperimentalComposeUiApi
    @ExperimentalMaterialApi
    @ExperimentalPagerApi
    @ExperimentalFoundationApi
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent{
            MainView(netWorkViewModel,externalPageNavigationController)
       }
        setNetWorkSubject()
        registerNetWorkConnectionStatus()
    }

    override fun onDestroy() {
        super.onDestroy()
       unRegisterNetWorkConnectionStatus()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            return true;
        }
        return super.onKeyDown(keyCode, event)
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