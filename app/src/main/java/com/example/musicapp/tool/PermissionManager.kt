package com.example.musicapp.tool

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.runtime.Composable
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

val externalStorageRequestCode = 2


fun requestExternalStoragePermission(activity: Activity){
    val requestList = ArrayList<String>()

    if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
        requestList.add(Manifest.permission.READ_EXTERNAL_STORAGE)
    }
    if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
        requestList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }
    if(requestList.size > 0) ActivityCompat.requestPermissions(activity, requestList.toArray(arrayOf<String>()), externalStorageRequestCode);
}
