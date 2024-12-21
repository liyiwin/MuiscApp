package com.example.musicapp.page.localTrackFolders

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import com.example.musicapp.routing.Screen
import com.example.musicapp.viewmodel.LocalTrackFoldersVIewModel

@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun  LocalTrackFoldersPage(vIewModel: LocalTrackFoldersVIewModel, navigationController:(screen: Screen)->Unit){
    vIewModel.updateTotalPlayLists()

    val isRemoveMode = remember { mutableStateOf(false) }
    if(isRemoveMode.value){
        LocalTrackFoldersEditPage(vIewModel,isRemoveMode)
    }else{
        LocalTrackFoldersMainPage(vIewModel,isRemoveMode,navigationController)
    }

}


