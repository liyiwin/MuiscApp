package com.example.musicapp

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.webkit.CookieManager
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.ExperimentalComposeUiApi
import com.example.musicapp.localDatase.ISaveAppSettingInApp
import com.example.musicapp.localDatase.ISaveUserInfoInApp
import com.example.musicapp.localDatase.RouterDataStorage
import com.example.musicapp.localDatase.sqLite.SQLiteCleaner
import com.example.musicapp.tool.network.NetWorkSubject
import com.example.musicapp.tool.network.Observer
import com.example.musicapp.viewmodel.global.NetWorkViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var netWorkConnectionObserver: Observer<Boolean>? = null
    private var netWorkSubject: NetWorkSubject? = null
    private val netWorkViewModel: NetWorkViewModel by viewModels()
    @Inject
    lateinit var saveUserInfoInApp: ISaveUserInfoInApp
    @Inject
    lateinit var saveAppSettingInApp: ISaveAppSettingInApp
    private val externalPageNavigationController: (uri: Uri)->Unit  = {   uri ->
       this@MainActivity.startActivity(Intent(Intent.ACTION_VIEW, uri))
    }



    private val cleanAppDataController:()->Unit = {
        clearCache(this)
        SQLiteCleaner.clearFavoriteTracksStorage(this)
        saveUserInfoInApp.cleanData()
        saveAppSettingInApp.cleanData()
        RouterDataStorage.cleanData()
        clearWebViewCookies()
    }



    private fun clearCache(context: Context) {
        val cacheDir = context.cacheDir
        deleteFilesInDir(cacheDir)  // 刪除緩存目錄中的所有檔案
    }

    private  fun deleteFilesInDir(dir: File) {
        if (dir.isDirectory) {
            val files = dir.listFiles()
            files?.forEach { it.delete() }  // 刪除每個檔案
        } else {
            dir.delete()  // 刪除檔案
        }
    }

    private  fun clearWebViewCookies() {
        val cookieManager = CookieManager.getInstance()
        cookieManager.removeAllCookies(null)  // 清除所有 cookies
        cookieManager.flush()  // 確保所有 cookies 已清除
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
            MainView(netWorkViewModel,externalPageNavigationController,cleanAppDataController)
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