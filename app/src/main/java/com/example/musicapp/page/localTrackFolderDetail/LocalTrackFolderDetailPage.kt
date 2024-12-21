package com.example.musicapp.page.localTrackFolderDetail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.musicapp.routing.Screen
import com.example.musicapp.viewmodel.LocalTrackFolderDetailViewModel

@Composable
fun LocalTrackFolderDetailPage(viewModel: LocalTrackFolderDetailViewModel, navigationController:(screen: Screen)->Unit){
    val isRemoveMode = remember { mutableStateOf(false) }
    val isAddMode = remember{mutableStateOf(false)}
    if(isRemoveMode.value){
        LocalTrackFolderDetailDeletePage(viewModel,isRemoveMode)
    }
    else if(isAddMode.value)   {
        LocalTrackFolderDetailAddPage(viewModel,isAddMode)
    }
    else{
      LocalTrackFolderDetailMainPage(viewModel,navigationController,isAddMode,isRemoveMode)
    }
}

