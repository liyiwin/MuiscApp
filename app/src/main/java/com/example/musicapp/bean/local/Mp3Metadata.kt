package com.example.musicapp.bean.local

import android.graphics.Bitmap
import java.io.File

data class MP3Metadata(
    val trackName:String?,
    val artistName:String?,
    val duration:Long?,
    val coverImage:Bitmap?,
    val originFile:File
)