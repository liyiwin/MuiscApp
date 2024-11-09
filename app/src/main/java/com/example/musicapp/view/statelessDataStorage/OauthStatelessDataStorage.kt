package com.example.musicapp.view.statelessDataStorage

import android.webkit.WebView
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.musicapp.data.paramter.ApiParameterManager

class OauthStatelessDataStorage {

    var webView:WebView? = null
    var url:String =ApiParameterManager.authUrl

}