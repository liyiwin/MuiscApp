package com.example.musicapp.page

import android.graphics.Bitmap
import android.view.ViewGroup
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.musicapp.R
import com.example.musicapp.data.paramter.ApiParameterManager
import com.example.musicapp.localDatase.RouterDataStorage
import com.example.musicapp.routing.Screen
import com.example.musicapp.textConverter.OauthDataConverter
import com.example.musicapp.view.callback.LoginCallback
import com.example.musicapp.view.statelessDataStorage.OauthStatelessDataStorage
import com.example.musicapp.viewmodel.global.NetWorkViewModel
import com.google.accompanist.drawablepainter.rememberDrawablePainter


@Composable
fun OathPage (viewModel:NetWorkViewModel,navigationController:(screen: Screen)->Unit) {

    val statelessDataStorage = OauthStatelessDataStorage()
    var loadingVisible = remember { mutableStateOf<Boolean>(true) }
    val isNetworkConnectStatus  =viewModel.getConnectStatus ().observeAsState()
    val loginCallback = object:LoginCallback{
        override fun onStart() {
            loadingVisible.value = false
        }

        override fun onError() {
            viewModel.updateConnectStatus()
        }

        override fun onSuccess(authCode: String) {
            RouterDataStorage.putAuthCode(authCode)
            navigationController.invoke(Screen.LoginScreen())
        }
    }
    loadingView(loadingVisible)
    if(isNetworkConnectStatus.value!!) loginView( statelessDataStorage,loginCallback )
    else netWorkDisconnectedVIew()

}


@Composable
fun loadingView(loadingVisible: MutableState<Boolean>){
    if(loadingVisible.value){
        Dialog(
            onDismissRequest = { },
            properties = DialogProperties(
                usePlatformDefaultWidth = false // experimental
            )
        ) {
            Surface(modifier = Modifier.fillMaxSize()) {

                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = rememberDrawablePainter(
                            drawable = getDrawable(
                                LocalContext.current,
                                R.drawable.account_setting_img
                            )
                        ),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .height(200.dp)
                            .fillMaxWidth(),

                        )

                    Spacer(modifier = Modifier.height(20.dp))
                    //.........................Text: title
                    Text(
                        text = "Loading...",
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(top = 20.dp)
                            .fillMaxWidth(),
                        letterSpacing = 2.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    //.........................Text : description
                    Text(
                        text = "Please wait",
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(top = 10.dp, start = 25.dp, end = 25.dp)
                            .fillMaxWidth(),
                        letterSpacing = 3.sp,
                    )
                    //.........................Spacer
                    Spacer(modifier = Modifier.height(24.dp))

                }
            }
        }
    }
}


@Composable
fun loginView(statelessDataStorage:OauthStatelessDataStorage,loginCallback: LoginCallback){
    if(statelessDataStorage.webView != null){
        statelessDataStorage.webView!!.loadUrl( statelessDataStorage.url)
    }else{
        AndroidView(
            modifier = Modifier.windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Vertical)),
            factory = {
                WebView(it).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    settings.javaScriptEnabled = true
                    settings.userAgentString = ApiParameterManager.USER_AGENT_MOZILLA
                    settings.domStorageEnabled = true
                    settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                    settings.allowContentAccess = true
                    settings.loadWithOverviewMode = true
                    webViewClient = object : WebViewClient() {
                        override fun onReceivedError(view: WebView?, request: WebResourceRequest?,error: WebResourceError? ) {
                            val errorDescription:String = (error?.description ?: "").toString()
                            if(errorDescription != "net::ERR_BLOCKED_BY_ORB"){
                                try {
                                    view?.stopLoading();

                                } catch (e:Exception) {

                                }
                                if(errorDescription == "net::ERR_NAME_NOT_RESOLVED")statelessDataStorage.url = ApiParameterManager.authUrl
                                loginCallback.onError()

                            }
                            super.onReceivedError(view, request, error)
                        }

                        override fun onReceivedHttpError(view: WebView?,request: WebResourceRequest?, errorResponse: WebResourceResponse?) {
                            super.onReceivedHttpError(view, request, errorResponse)
                        }



                        override fun onPageCommitVisible(view: WebView?, url: String?) {
                            super.onPageCommitVisible(view, url)
                            loginCallback.onStart()
                        }

                        override fun onPageStarted(view: WebView?,url: String?,favicon: Bitmap? ) {
                            super.onPageStarted(view, url, favicon)
                        }

                        override fun onPageFinished(view: WebView?, url: String?) {
                            super.onPageFinished(view, url)
                        }

                        override fun shouldOverrideUrlLoading(view: WebView?,request:WebResourceRequest): Boolean {
                            request.url?.let{ url->
                                statelessDataStorage.url=  url.toString()
                                OauthDataConverter.convertAuthCode(url)?.let{ authCode -> loginCallback.onSuccess(authCode)}
                            }
                            return super.shouldOverrideUrlLoading(view,request)
                        }
                    }
                    statelessDataStorage.webView = this
                }
            }, update = {
                it.loadUrl(statelessDataStorage.url)
            })
    }
}

@Composable
fun netWorkDisconnectedVIew(){
    Column(modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Image(
            painter = rememberDrawablePainter(
                drawable = getDrawable(
                    LocalContext.current,
                    R.drawable.network_disconnected
                )
            ),
            contentDescription = "",
            modifier = Modifier
                .size(200.dp)
                .fillMaxSize(),
            alignment = Alignment.Center
        )
        Text(
            fontSize = 15.sp,
            text = "連線異常！請稍後再試。",
            textAlign = TextAlign.Center,
            color = Color.Black,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 25.dp),

            )
    }
}




